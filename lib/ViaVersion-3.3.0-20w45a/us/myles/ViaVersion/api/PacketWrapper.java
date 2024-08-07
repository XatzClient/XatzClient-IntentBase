// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api;

import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.api.remapper.ValueCreator;
import io.netty.channel.ChannelFuture;
import java.util.NoSuchElementException;
import us.myles.ViaVersion.util.PipelineUtil;
import us.myles.ViaVersion.exception.CancelException;
import us.myles.ViaVersion.packets.Direction;
import java.util.Collection;
import us.myles.ViaVersion.api.type.TypeConverter;
import java.io.IOException;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import us.myles.ViaVersion.exception.InformativeException;
import java.util.ArrayList;
import java.util.List;
import us.myles.ViaVersion.api.type.Type;
import java.util.LinkedList;
import us.myles.ViaVersion.api.data.UserConnection;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.protocol.Protocol;

public class PacketWrapper
{
    public static final int PASSTHROUGH_ID = 1000;
    private static final Protocol[] PROTOCOL_ARRAY;
    private final ByteBuf inputBuffer;
    private final UserConnection userConnection;
    private boolean send;
    private int id;
    private final LinkedList<Pair<Type, Object>> readableObjects;
    private final List<Pair<Type, Object>> packetValues;
    
    public PacketWrapper(final int packetID, final ByteBuf inputBuffer, final UserConnection userConnection) {
        this.send = true;
        this.id = -1;
        this.readableObjects = new LinkedList<Pair<Type, Object>>();
        this.packetValues = new ArrayList<Pair<Type, Object>>();
        this.id = packetID;
        this.inputBuffer = inputBuffer;
        this.userConnection = userConnection;
    }
    
    public <T> T get(final Type<T> type, final int index) throws Exception {
        int currentIndex = 0;
        for (final Pair<Type, Object> packetValue : this.packetValues) {
            if (packetValue.getKey() == type) {
                if (currentIndex == index) {
                    return (T)packetValue.getValue();
                }
                ++currentIndex;
            }
        }
        final Exception e = new ArrayIndexOutOfBoundsException("Could not find type " + type.getTypeName() + " at " + index);
        throw new InformativeException(e).set("Type", type.getTypeName()).set("Index", index).set("Packet ID", this.getId()).set("Data", this.packetValues);
    }
    
    public boolean is(final Type type, final int index) {
        int currentIndex = 0;
        for (final Pair<Type, Object> packetValue : this.packetValues) {
            if (packetValue.getKey() == type) {
                if (currentIndex == index) {
                    return true;
                }
                ++currentIndex;
            }
        }
        return false;
    }
    
    public boolean isReadable(final Type type, final int index) {
        int currentIndex = 0;
        for (final Pair<Type, Object> packetValue : this.readableObjects) {
            if (packetValue.getKey().getBaseClass() == type.getBaseClass()) {
                if (currentIndex == index) {
                    return true;
                }
                ++currentIndex;
            }
        }
        return false;
    }
    
    public <T> void set(final Type<T> type, final int index, final T value) throws Exception {
        int currentIndex = 0;
        for (final Pair<Type, Object> packetValue : this.packetValues) {
            if (packetValue.getKey() == type) {
                if (currentIndex == index) {
                    packetValue.setValue(value);
                    return;
                }
                ++currentIndex;
            }
        }
        final Exception e = new ArrayIndexOutOfBoundsException("Could not find type " + type.getTypeName() + " at " + index);
        throw new InformativeException(e).set("Type", type.getTypeName()).set("Index", index).set("Packet ID", this.getId());
    }
    
    public <T> T read(final Type<T> type) throws Exception {
        if (type == Type.NOTHING) {
            return null;
        }
        if (this.readableObjects.isEmpty()) {
            Preconditions.checkNotNull((Object)this.inputBuffer, (Object)"This packet does not have an input buffer.");
            try {
                return type.read(this.inputBuffer);
            }
            catch (Exception e) {
                throw new InformativeException(e).set("Type", type.getTypeName()).set("Packet ID", this.getId()).set("Data", this.packetValues);
            }
        }
        final Pair<Type, Object> read = this.readableObjects.poll();
        final Type rtype = read.getKey();
        if (rtype.equals(type) || (type.getBaseClass().equals(rtype.getBaseClass()) && type.getOutputClass().equals(rtype.getOutputClass()))) {
            return (T)read.getValue();
        }
        if (rtype == Type.NOTHING) {
            return (T)this.read((Type<Object>)type);
        }
        final Exception e2 = new IOException("Unable to read type " + type.getTypeName() + ", found " + read.getKey().getTypeName());
        throw new InformativeException(e2).set("Type", type.getTypeName()).set("Packet ID", this.getId()).set("Data", this.packetValues);
    }
    
    public <T> void write(final Type<T> type, T value) {
        if (value != null && !type.getOutputClass().isAssignableFrom(value.getClass())) {
            if (type instanceof TypeConverter) {
                value = ((TypeConverter)type).from(value);
            }
            else {
                Via.getPlatform().getLogger().warning("Possible type mismatch: " + value.getClass().getName() + " -> " + type.getOutputClass());
            }
        }
        this.packetValues.add(new Pair<Type, Object>(type, value));
    }
    
    public <T> T passthrough(final Type<T> type) throws Exception {
        final T value = (T)this.read((Type<Object>)type);
        this.write(type, value);
        return value;
    }
    
    public void passthroughAll() throws Exception {
        this.packetValues.addAll(this.readableObjects);
        this.readableObjects.clear();
        if (this.inputBuffer.readableBytes() > 0) {
            this.passthrough(Type.REMAINING_BYTES);
        }
    }
    
    public void writeToBuffer(final ByteBuf buffer) throws Exception {
        if (this.id != -1) {
            Type.VAR_INT.writePrimitive(buffer, this.id);
        }
        if (!this.readableObjects.isEmpty()) {
            this.packetValues.addAll(this.readableObjects);
            this.readableObjects.clear();
        }
        int index = 0;
        for (final Pair<Type, Object> packetValue : this.packetValues) {
            try {
                Object value = packetValue.getValue();
                if (value != null && !packetValue.getKey().getOutputClass().isAssignableFrom(value.getClass())) {
                    if (packetValue.getKey() instanceof TypeConverter) {
                        value = ((TypeConverter)packetValue.getKey()).from(value);
                    }
                    else {
                        Via.getPlatform().getLogger().warning("Possible type mismatch: " + value.getClass().getName() + " -> " + packetValue.getKey().getOutputClass());
                    }
                }
                packetValue.getKey().write(buffer, value);
            }
            catch (Exception e) {
                throw new InformativeException(e).set("Index", index).set("Type", packetValue.getKey().getTypeName()).set("Packet ID", this.getId()).set("Data", this.packetValues);
            }
            ++index;
        }
        this.writeRemaining(buffer);
    }
    
    public void clearInputBuffer() {
        if (this.inputBuffer != null) {
            this.inputBuffer.clear();
        }
        this.readableObjects.clear();
    }
    
    public void clearPacket() {
        this.clearInputBuffer();
        this.packetValues.clear();
    }
    
    private void writeRemaining(final ByteBuf output) {
        if (this.inputBuffer != null) {
            output.writeBytes(this.inputBuffer, this.inputBuffer.readableBytes());
        }
    }
    
    public void send(final Class<? extends Protocol> packetProtocol, final boolean skipCurrentPipeline) throws Exception {
        this.send(packetProtocol, skipCurrentPipeline, false);
    }
    
    public void send(final Class<? extends Protocol> packetProtocol, final boolean skipCurrentPipeline, final boolean currentThread) throws Exception {
        if (!this.isCancelled()) {
            try {
                final ByteBuf output = this.constructPacket(packetProtocol, skipCurrentPipeline, Direction.OUTGOING);
                this.user().sendRawPacket(output, currentThread);
            }
            catch (Exception e) {
                if (!PipelineUtil.containsCause(e, CancelException.class)) {
                    throw e;
                }
            }
        }
    }
    
    private ByteBuf constructPacket(final Class<? extends Protocol> packetProtocol, final boolean skipCurrentPipeline, final Direction direction) throws Exception {
        final Protocol[] protocols = this.user().getProtocolInfo().getPipeline().pipes().toArray(PacketWrapper.PROTOCOL_ARRAY);
        final boolean reverse = direction == Direction.OUTGOING;
        int index = -1;
        for (int i = 0; i < protocols.length; ++i) {
            if (protocols[i].getClass() == packetProtocol) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new NoSuchElementException(packetProtocol.getCanonicalName());
        }
        if (skipCurrentPipeline) {
            index = (reverse ? (index - 1) : (index + 1));
        }
        this.resetReader();
        this.apply(direction, this.user().getProtocolInfo().getState(), index, protocols, reverse);
        final ByteBuf output = (this.inputBuffer == null) ? this.user().getChannel().alloc().buffer() : this.inputBuffer.alloc().buffer();
        this.writeToBuffer(output);
        return output;
    }
    
    public void send(final Class<? extends Protocol> packetProtocol) throws Exception {
        this.send(packetProtocol, true);
    }
    
    public ChannelFuture sendFuture(final Class<? extends Protocol> packetProtocol) throws Exception {
        if (!this.isCancelled()) {
            final ByteBuf output = this.constructPacket(packetProtocol, true, Direction.OUTGOING);
            return this.user().sendRawPacketFuture(output);
        }
        return this.user().getChannel().newFailedFuture((Throwable)new Exception("Cancelled packet"));
    }
    
    @Deprecated
    public void send() throws Exception {
        if (!this.isCancelled()) {
            final ByteBuf output = (this.inputBuffer == null) ? this.user().getChannel().alloc().buffer() : this.inputBuffer.alloc().buffer();
            this.writeToBuffer(output);
            this.user().sendRawPacket(output);
        }
    }
    
    public PacketWrapper create(final int packetID) {
        return new PacketWrapper(packetID, null, this.user());
    }
    
    public PacketWrapper create(final int packetID, final ValueCreator init) throws Exception {
        final PacketWrapper wrapper = this.create(packetID);
        init.write(wrapper);
        return wrapper;
    }
    
    public PacketWrapper apply(final Direction direction, final State state, final int index, final List<Protocol> pipeline, final boolean reverse) throws Exception {
        final Protocol[] array = pipeline.toArray(PacketWrapper.PROTOCOL_ARRAY);
        return this.apply(direction, state, reverse ? (array.length - 1) : index, array, reverse);
    }
    
    public PacketWrapper apply(final Direction direction, final State state, final int index, final List<Protocol> pipeline) throws Exception {
        return this.apply(direction, state, index, pipeline.toArray(PacketWrapper.PROTOCOL_ARRAY), false);
    }
    
    private PacketWrapper apply(final Direction direction, final State state, final int index, final Protocol[] pipeline, final boolean reverse) throws Exception {
        if (reverse) {
            for (int i = index; i >= 0; --i) {
                pipeline[i].transform(direction, state, this);
                this.resetReader();
            }
        }
        else {
            for (int i = index; i < pipeline.length; ++i) {
                pipeline[i].transform(direction, state, this);
                this.resetReader();
            }
        }
        return this;
    }
    
    public void cancel() {
        this.send = false;
    }
    
    public boolean isCancelled() {
        return !this.send;
    }
    
    public UserConnection user() {
        return this.userConnection;
    }
    
    public void resetReader() {
        this.packetValues.addAll(this.readableObjects);
        this.readableObjects.clear();
        this.readableObjects.addAll(this.packetValues);
        this.packetValues.clear();
    }
    
    @Deprecated
    public void sendToServer() throws Exception {
        if (!this.isCancelled()) {
            final ByteBuf output = (this.inputBuffer == null) ? this.user().getChannel().alloc().buffer() : this.inputBuffer.alloc().buffer();
            this.writeToBuffer(output);
            this.user().sendRawPacketToServer(output, true);
        }
    }
    
    public void sendToServer(final Class<? extends Protocol> packetProtocol, final boolean skipCurrentPipeline, final boolean currentThread) throws Exception {
        if (!this.isCancelled()) {
            try {
                final ByteBuf output = this.constructPacket(packetProtocol, skipCurrentPipeline, Direction.INCOMING);
                this.user().sendRawPacketToServer(output, currentThread);
            }
            catch (Exception e) {
                if (!PipelineUtil.containsCause(e, CancelException.class)) {
                    throw e;
                }
            }
        }
    }
    
    public void sendToServer(final Class<? extends Protocol> packetProtocol, final boolean skipCurrentPipeline) throws Exception {
        this.sendToServer(packetProtocol, skipCurrentPipeline, false);
    }
    
    public void sendToServer(final Class<? extends Protocol> packetProtocol) throws Exception {
        this.sendToServer(packetProtocol, true);
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        return "PacketWrapper{packetValues=" + this.packetValues + ", readableObjects=" + this.readableObjects + ", id=" + this.id + '}';
    }
    
    static {
        PROTOCOL_ARRAY = new Protocol[0];
    }
}
