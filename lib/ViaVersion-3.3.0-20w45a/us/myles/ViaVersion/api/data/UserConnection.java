// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.data;

import us.myles.ViaVersion.exception.CancelException;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.packets.Direction;
import java.util.function.Function;
import io.netty.channel.ChannelHandlerContext;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.util.PipelineUtil;
import us.myles.viaversion.libs.bungeecordchat.api.ChatColor;
import us.myles.ViaVersion.api.ViaVersionConfig;
import io.netty.channel.ChannelFuture;
import us.myles.ViaVersion.api.Via;
import io.netty.buffer.ByteBuf;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.Nullable;
import us.myles.ViaVersion.protocols.base.ProtocolInfo;
import java.util.Map;
import io.netty.channel.Channel;
import java.util.concurrent.atomic.AtomicLong;

public class UserConnection
{
    private static final AtomicLong IDS;
    private final long id;
    private final Channel channel;
    private final boolean clientSide;
    Map<Class, StoredObject> storedObjects;
    private ProtocolInfo protocolInfo;
    private boolean active;
    private boolean pendingDisconnect;
    private Object lastPacket;
    private long sentPackets;
    private long receivedPackets;
    private long startTime;
    private long intervalPackets;
    private long packetsPerSecond;
    private int secondsObserved;
    private int warnings;
    
    public UserConnection(@Nullable final Channel channel, final boolean clientSide) {
        this.id = UserConnection.IDS.incrementAndGet();
        this.storedObjects = new ConcurrentHashMap<Class, StoredObject>();
        this.active = true;
        this.packetsPerSecond = -1L;
        this.channel = channel;
        this.clientSide = clientSide;
    }
    
    public UserConnection(@Nullable final Channel channel) {
        this(channel, false);
    }
    
    @Nullable
    public <T extends StoredObject> T get(final Class<T> objectClass) {
        return (T)this.storedObjects.get(objectClass);
    }
    
    public boolean has(final Class<? extends StoredObject> objectClass) {
        return this.storedObjects.containsKey(objectClass);
    }
    
    public void put(final StoredObject object) {
        this.storedObjects.put(object.getClass(), object);
    }
    
    public void clearStoredObjects() {
        this.storedObjects.clear();
    }
    
    public void sendRawPacket(final ByteBuf packet, final boolean currentThread) {
        Runnable act;
        if (this.clientSide) {
            act = (() -> this.getChannel().pipeline().context(Via.getManager().getInjector().getDecoderName()).fireChannelRead((Object)packet));
        }
        else {
            act = (() -> this.channel.pipeline().context(Via.getManager().getInjector().getEncoderName()).writeAndFlush((Object)packet));
        }
        if (currentThread) {
            act.run();
        }
        else {
            try {
                this.channel.eventLoop().submit(act);
            }
            catch (Throwable e) {
                packet.release();
                e.printStackTrace();
            }
        }
    }
    
    public ChannelFuture sendRawPacketFuture(final ByteBuf packet) {
        if (this.clientSide) {
            return this.sendRawPacketFutureClientSide(packet);
        }
        return this.sendRawPacketFutureServerSide(packet);
    }
    
    private ChannelFuture sendRawPacketFutureServerSide(final ByteBuf packet) {
        return this.channel.pipeline().context(Via.getManager().getInjector().getEncoderName()).writeAndFlush((Object)packet);
    }
    
    private ChannelFuture sendRawPacketFutureClientSide(final ByteBuf packet) {
        this.getChannel().pipeline().context(Via.getManager().getInjector().getDecoderName()).fireChannelRead((Object)packet);
        return this.getChannel().newSucceededFuture();
    }
    
    public void sendRawPacket(final ByteBuf packet) {
        this.sendRawPacket(packet, false);
    }
    
    public void incrementSent() {
        ++this.sentPackets;
    }
    
    public boolean incrementReceived() {
        final long diff = System.currentTimeMillis() - this.startTime;
        if (diff >= 1000L) {
            this.packetsPerSecond = this.intervalPackets;
            this.startTime = System.currentTimeMillis();
            this.intervalPackets = 1L;
            return true;
        }
        ++this.intervalPackets;
        ++this.receivedPackets;
        return false;
    }
    
    public boolean exceedsMaxPPS() {
        if (this.clientSide) {
            return false;
        }
        final ViaVersionConfig conf = Via.getConfig();
        if (conf.getMaxPPS() > 0 && this.packetsPerSecond >= conf.getMaxPPS()) {
            this.disconnect(conf.getMaxPPSKickMessage().replace("%pps", Long.toString(this.packetsPerSecond)));
            return true;
        }
        if (conf.getMaxWarnings() > 0 && conf.getTrackingPeriod() > 0) {
            if (this.secondsObserved > conf.getTrackingPeriod()) {
                this.warnings = 0;
                this.secondsObserved = 1;
            }
            else {
                ++this.secondsObserved;
                if (this.packetsPerSecond >= conf.getWarningPPS()) {
                    ++this.warnings;
                }
                if (this.warnings >= conf.getMaxWarnings()) {
                    this.disconnect(conf.getMaxWarningsKickMessage().replace("%pps", Long.toString(this.packetsPerSecond)));
                    return true;
                }
            }
        }
        return false;
    }
    
    public void disconnect(final String reason) {
        if (!this.channel.isOpen() || this.pendingDisconnect) {
            return;
        }
        this.pendingDisconnect = true;
        Via.getPlatform().runSync(() -> {
            if (!Via.getPlatform().disconnect(this, ChatColor.translateAlternateColorCodes('&', reason))) {
                this.channel.close();
            }
        });
    }
    
    public void sendRawPacketToServer(final ByteBuf packet, final boolean currentThread) {
        if (this.clientSide) {
            this.sendRawPacketToServerClientSide(packet, currentThread);
        }
        else {
            this.sendRawPacketToServerServerSide(packet, currentThread);
        }
    }
    
    private void sendRawPacketToServerServerSide(final ByteBuf packet, final boolean currentThread) {
        final ByteBuf buf = packet.alloc().buffer();
        try {
            final ChannelHandlerContext context = PipelineUtil.getPreviousContext(Via.getManager().getInjector().getDecoderName(), this.channel.pipeline());
            try {
                Type.VAR_INT.writePrimitive(buf, 1000);
            }
            catch (Exception e) {
                Via.getPlatform().getLogger().warning("Type.VAR_INT.write thrown an exception: " + e);
            }
            buf.writeBytes(packet);
            final ChannelHandlerContext channelHandlerContext;
            final Object o;
            final Runnable act = () -> {
                if (channelHandlerContext != null) {
                    channelHandlerContext.fireChannelRead(o);
                }
                else {
                    this.channel.pipeline().fireChannelRead(o);
                }
                return;
            };
            if (currentThread) {
                act.run();
            }
            else {
                try {
                    this.channel.eventLoop().submit(act);
                }
                catch (Throwable t) {
                    buf.release();
                    throw t;
                }
            }
        }
        finally {
            packet.release();
        }
    }
    
    private void sendRawPacketToServerClientSide(final ByteBuf packet, final boolean currentThread) {
        final Runnable act = () -> this.getChannel().pipeline().context(Via.getManager().getInjector().getEncoderName()).writeAndFlush((Object)packet);
        if (currentThread) {
            act.run();
        }
        else {
            try {
                this.getChannel().eventLoop().submit(act);
            }
            catch (Throwable e) {
                e.printStackTrace();
                packet.release();
            }
        }
    }
    
    public void sendRawPacketToServer(final ByteBuf packet) {
        this.sendRawPacketToServer(packet, false);
    }
    
    public boolean checkIncomingPacket() {
        if (this.clientSide) {
            return this.checkClientBound();
        }
        return this.checkServerBound();
    }
    
    private boolean checkClientBound() {
        this.incrementSent();
        return true;
    }
    
    private boolean checkServerBound() {
        return !this.pendingDisconnect && (!this.incrementReceived() || !this.exceedsMaxPPS());
    }
    
    public boolean checkOutgoingPacket() {
        if (this.clientSide) {
            return this.checkServerBound();
        }
        return this.checkClientBound();
    }
    
    public boolean shouldTransformPacket() {
        return this.active;
    }
    
    public void transformOutgoing(final ByteBuf buf, final Function<Throwable, Exception> cancelSupplier) throws Exception {
        if (!buf.isReadable()) {
            return;
        }
        this.transform(buf, this.clientSide ? Direction.INCOMING : Direction.OUTGOING, cancelSupplier);
    }
    
    public void transformIncoming(final ByteBuf buf, final Function<Throwable, Exception> cancelSupplier) throws Exception {
        if (!buf.isReadable()) {
            return;
        }
        this.transform(buf, this.clientSide ? Direction.OUTGOING : Direction.INCOMING, cancelSupplier);
    }
    
    private void transform(final ByteBuf buf, final Direction direction, final Function<Throwable, Exception> cancelSupplier) throws Exception {
        final int id = Type.VAR_INT.readPrimitive(buf);
        if (id == 1000) {
            return;
        }
        final PacketWrapper wrapper = new PacketWrapper(id, buf, this);
        try {
            this.protocolInfo.getPipeline().transform(direction, this.protocolInfo.getState(), wrapper);
        }
        catch (CancelException ex) {
            throw cancelSupplier.apply(ex);
        }
        final ByteBuf transformed = buf.alloc().buffer();
        try {
            wrapper.writeToBuffer(transformed);
            buf.clear().writeBytes(transformed);
        }
        finally {
            transformed.release();
        }
    }
    
    public long getId() {
        return this.id;
    }
    
    @Nullable
    public Channel getChannel() {
        return this.channel;
    }
    
    @Nullable
    public ProtocolInfo getProtocolInfo() {
        return this.protocolInfo;
    }
    
    public void setProtocolInfo(@Nullable final ProtocolInfo protocolInfo) {
        this.protocolInfo = protocolInfo;
        if (protocolInfo != null) {
            this.storedObjects.put(ProtocolInfo.class, protocolInfo);
        }
        else {
            this.storedObjects.remove(ProtocolInfo.class);
        }
    }
    
    public Map<Class, StoredObject> getStoredObjects() {
        return this.storedObjects;
    }
    
    public boolean isActive() {
        return this.active;
    }
    
    public void setActive(final boolean active) {
        this.active = active;
    }
    
    public boolean isPendingDisconnect() {
        return this.pendingDisconnect;
    }
    
    public void setPendingDisconnect(final boolean pendingDisconnect) {
        this.pendingDisconnect = pendingDisconnect;
    }
    
    @Nullable
    public Object getLastPacket() {
        return this.lastPacket;
    }
    
    public void setLastPacket(@Nullable final Object lastPacket) {
        this.lastPacket = lastPacket;
    }
    
    public long getSentPackets() {
        return this.sentPackets;
    }
    
    public void setSentPackets(final long sentPackets) {
        this.sentPackets = sentPackets;
    }
    
    public long getReceivedPackets() {
        return this.receivedPackets;
    }
    
    public void setReceivedPackets(final long receivedPackets) {
        this.receivedPackets = receivedPackets;
    }
    
    public long getStartTime() {
        return this.startTime;
    }
    
    public void setStartTime(final long startTime) {
        this.startTime = startTime;
    }
    
    public long getIntervalPackets() {
        return this.intervalPackets;
    }
    
    public void setIntervalPackets(final long intervalPackets) {
        this.intervalPackets = intervalPackets;
    }
    
    public long getPacketsPerSecond() {
        return this.packetsPerSecond;
    }
    
    public void setPacketsPerSecond(final long packetsPerSecond) {
        this.packetsPerSecond = packetsPerSecond;
    }
    
    public int getSecondsObserved() {
        return this.secondsObserved;
    }
    
    public void setSecondsObserved(final int secondsObserved) {
        this.secondsObserved = secondsObserved;
    }
    
    public int getWarnings() {
        return this.warnings;
    }
    
    public void setWarnings(final int warnings) {
        this.warnings = warnings;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final UserConnection that = (UserConnection)o;
        return this.id == that.id;
    }
    
    @Override
    public int hashCode() {
        return Long.hashCode(this.id);
    }
    
    public boolean isClientSide() {
        return this.clientSide;
    }
    
    public boolean shouldApplyBlockProtocol() {
        return !this.clientSide;
    }
    
    static {
        IDS = new AtomicLong();
    }
}
