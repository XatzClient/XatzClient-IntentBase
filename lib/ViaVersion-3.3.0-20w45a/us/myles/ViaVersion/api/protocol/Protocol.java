// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.protocol;

import us.myles.ViaVersion.api.data.MappingData;
import us.myles.ViaVersion.exception.CancelException;
import us.myles.ViaVersion.exception.InformativeException;
import us.myles.ViaVersion.packets.Direction;
import java.util.Arrays;
import us.myles.ViaVersion.api.PacketWrapper;
import java.util.logging.Level;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.platform.providers.ViaProviders;
import java.util.List;
import us.myles.ViaVersion.api.data.UserConnection;
import com.google.common.base.Preconditions;
import us.myles.ViaVersion.packets.State;
import java.util.HashMap;
import org.jetbrains.annotations.Nullable;
import java.util.Map;

public abstract class Protocol<C1 extends ClientboundPacketType, C2 extends ClientboundPacketType, S1 extends ServerboundPacketType, S2 extends ServerboundPacketType>
{
    private final Map<Packet, ProtocolPacket> incoming;
    private final Map<Packet, ProtocolPacket> outgoing;
    private final Map<Class, Object> storedObjects;
    protected final Class<C1> oldClientboundPacketEnum;
    protected final Class<C2> newClientboundPacketEnum;
    protected final Class<S1> oldServerboundPacketEnum;
    protected final Class<S2> newServerboundPacketEnum;
    
    protected Protocol() {
        this(null, null, null, null);
    }
    
    protected Protocol(@Nullable final Class<C1> oldClientboundPacketEnum, @Nullable final Class<C2> clientboundPacketEnum, @Nullable final Class<S1> oldServerboundPacketEnum, @Nullable final Class<S2> serverboundPacketEnum) {
        this.incoming = new HashMap<Packet, ProtocolPacket>();
        this.outgoing = new HashMap<Packet, ProtocolPacket>();
        this.storedObjects = new HashMap<Class, Object>();
        this.oldClientboundPacketEnum = oldClientboundPacketEnum;
        this.newClientboundPacketEnum = clientboundPacketEnum;
        this.oldServerboundPacketEnum = oldServerboundPacketEnum;
        this.newServerboundPacketEnum = serverboundPacketEnum;
        this.registerPackets();
        if (oldClientboundPacketEnum != null && clientboundPacketEnum != null && oldClientboundPacketEnum != clientboundPacketEnum) {
            this.registerOutgoingChannelIdChanges();
        }
        if (oldServerboundPacketEnum != null && serverboundPacketEnum != null && oldServerboundPacketEnum != serverboundPacketEnum) {
            this.registerIncomingChannelIdChanges();
        }
    }
    
    protected void registerOutgoingChannelIdChanges() {
        final ClientboundPacketType[] newConstants = this.newClientboundPacketEnum.getEnumConstants();
        final Map<String, ClientboundPacketType> newClientboundPackets = new HashMap<String, ClientboundPacketType>(newConstants.length);
        for (final ClientboundPacketType newConstant : newConstants) {
            newClientboundPackets.put(newConstant.name(), newConstant);
        }
        for (final ClientboundPacketType packet : this.oldClientboundPacketEnum.getEnumConstants()) {
            final ClientboundPacketType mappedPacket = newClientboundPackets.get(packet.name());
            final int oldId = packet.ordinal();
            if (mappedPacket == null) {
                Preconditions.checkArgument(this.hasRegisteredOutgoing(State.PLAY, oldId), (Object)("Packet " + packet + " in " + this.getClass().getSimpleName() + " has no mapping - it needs to be manually cancelled or remapped!"));
            }
            else {
                final int newId = mappedPacket.ordinal();
                if (!this.hasRegisteredOutgoing(State.PLAY, oldId)) {
                    this.registerOutgoing(State.PLAY, oldId, newId);
                }
            }
        }
    }
    
    protected void registerIncomingChannelIdChanges() {
        final ServerboundPacketType[] oldConstants = this.oldServerboundPacketEnum.getEnumConstants();
        final Map<String, ServerboundPacketType> oldServerboundConstants = new HashMap<String, ServerboundPacketType>(oldConstants.length);
        for (final ServerboundPacketType oldConstant : oldConstants) {
            oldServerboundConstants.put(oldConstant.name(), oldConstant);
        }
        for (final ServerboundPacketType packet : this.newServerboundPacketEnum.getEnumConstants()) {
            final ServerboundPacketType mappedPacket = oldServerboundConstants.get(packet.name());
            final int newId = packet.ordinal();
            if (mappedPacket == null) {
                Preconditions.checkArgument(this.hasRegisteredIncoming(State.PLAY, newId), (Object)("Packet " + packet + " in " + this.getClass().getSimpleName() + " has no mapping - it needs to be manually cancelled or remapped!"));
            }
            else {
                final int oldId = mappedPacket.ordinal();
                if (!this.hasRegisteredIncoming(State.PLAY, newId)) {
                    this.registerIncoming(State.PLAY, oldId, newId);
                }
            }
        }
    }
    
    public boolean isFiltered(final Class packetClass) {
        return false;
    }
    
    protected void filterPacket(final UserConnection info, final Object packet, final List output) throws Exception {
        output.add(packet);
    }
    
    protected void registerPackets() {
    }
    
    protected final void loadMappingData() {
        this.getMappingData().load();
        this.onMappingDataLoaded();
    }
    
    protected void onMappingDataLoaded() {
    }
    
    protected void register(final ViaProviders providers) {
    }
    
    public void init(final UserConnection userConnection) {
    }
    
    public void registerIncoming(final State state, final int oldPacketID, final int newPacketID) {
        this.registerIncoming(state, oldPacketID, newPacketID, null);
    }
    
    public void registerIncoming(final State state, final int oldPacketID, final int newPacketID, final PacketRemapper packetRemapper) {
        this.registerIncoming(state, oldPacketID, newPacketID, packetRemapper, false);
    }
    
    public void registerIncoming(final State state, final int oldPacketID, final int newPacketID, final PacketRemapper packetRemapper, final boolean override) {
        final ProtocolPacket protocolPacket = new ProtocolPacket(state, oldPacketID, newPacketID, packetRemapper);
        final Packet packet = new Packet(state, newPacketID);
        if (!override && this.incoming.containsKey(packet)) {
            Via.getPlatform().getLogger().log(Level.WARNING, packet + " already registered! If this override is intentional, set override to true. Stacktrace: ", new Exception());
        }
        this.incoming.put(packet, protocolPacket);
    }
    
    public void cancelIncoming(final State state, final int oldPacketID, final int newPacketID) {
        this.registerIncoming(state, oldPacketID, newPacketID, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(PacketWrapper::cancel);
            }
        });
    }
    
    public void cancelIncoming(final State state, final int newPacketID) {
        this.cancelIncoming(state, -1, newPacketID);
    }
    
    public void registerOutgoing(final State state, final int oldPacketID, final int newPacketID) {
        this.registerOutgoing(state, oldPacketID, newPacketID, null);
    }
    
    public void registerOutgoing(final State state, final int oldPacketID, final int newPacketID, final PacketRemapper packetRemapper) {
        this.registerOutgoing(state, oldPacketID, newPacketID, packetRemapper, false);
    }
    
    public void cancelOutgoing(final State state, final int oldPacketID, final int newPacketID) {
        this.registerOutgoing(state, oldPacketID, newPacketID, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(PacketWrapper::cancel);
            }
        });
    }
    
    public void cancelOutgoing(final State state, final int oldPacketID) {
        this.cancelOutgoing(state, oldPacketID, -1);
    }
    
    public void registerOutgoing(final State state, final int oldPacketID, final int newPacketID, final PacketRemapper packetRemapper, final boolean override) {
        final ProtocolPacket protocolPacket = new ProtocolPacket(state, oldPacketID, newPacketID, packetRemapper);
        final Packet packet = new Packet(state, oldPacketID);
        if (!override && this.outgoing.containsKey(packet)) {
            Via.getPlatform().getLogger().log(Level.WARNING, packet + " already registered! If override is intentional, set override to true. Stacktrace: ", new Exception());
        }
        this.outgoing.put(packet, protocolPacket);
    }
    
    public void registerOutgoing(final C1 packetType, @Nullable final PacketRemapper packetRemapper) {
        this.checkPacketType(packetType, packetType.getClass() == this.oldClientboundPacketEnum);
        final ClientboundPacketType mappedPacket = (this.oldClientboundPacketEnum == this.newClientboundPacketEnum) ? packetType : ((C2)Arrays.stream(this.newClientboundPacketEnum.getEnumConstants()).filter(en -> en.name().equals(packetType.name())).findAny().orElse(null));
        Preconditions.checkNotNull((Object)mappedPacket, (Object)("Packet type " + packetType + " in " + packetType.getClass().getSimpleName() + " could not be automatically mapped!"));
        final int oldId = packetType.ordinal();
        final int newId = mappedPacket.ordinal();
        this.registerOutgoing(State.PLAY, oldId, newId, packetRemapper);
    }
    
    public void registerOutgoing(final C1 packetType, @Nullable final C2 mappedPacketType, @Nullable final PacketRemapper packetRemapper) {
        this.checkPacketType(packetType, packetType.getClass() == this.oldClientboundPacketEnum);
        this.checkPacketType(mappedPacketType, mappedPacketType == null || mappedPacketType.getClass() == this.newClientboundPacketEnum);
        this.registerOutgoing(State.PLAY, packetType.ordinal(), (mappedPacketType != null) ? mappedPacketType.ordinal() : -1, packetRemapper);
    }
    
    public void registerOutgoing(final C1 packetType, @Nullable final C2 mappedPacketType) {
        this.registerOutgoing(packetType, mappedPacketType, null);
    }
    
    public void cancelOutgoing(final C1 packetType) {
        this.cancelOutgoing(State.PLAY, packetType.ordinal(), packetType.ordinal());
    }
    
    public void registerIncoming(final S2 packetType, @Nullable final PacketRemapper packetRemapper) {
        this.checkPacketType(packetType, packetType.getClass() == this.newServerboundPacketEnum);
        final ServerboundPacketType mappedPacket = (this.oldServerboundPacketEnum == this.newServerboundPacketEnum) ? packetType : ((S1)Arrays.stream(this.oldServerboundPacketEnum.getEnumConstants()).filter(en -> en.name().equals(packetType.name())).findAny().orElse(null));
        Preconditions.checkNotNull((Object)mappedPacket, (Object)("Packet type " + packetType + " in " + packetType.getClass().getSimpleName() + " could not be automatically mapped!"));
        final int oldId = mappedPacket.ordinal();
        final int newId = packetType.ordinal();
        this.registerIncoming(State.PLAY, oldId, newId, packetRemapper);
    }
    
    public void registerIncoming(final S2 packetType, @Nullable final S1 mappedPacketType, @Nullable final PacketRemapper packetRemapper) {
        this.checkPacketType(packetType, packetType.getClass() == this.newServerboundPacketEnum);
        this.checkPacketType(mappedPacketType, mappedPacketType == null || mappedPacketType.getClass() == this.oldServerboundPacketEnum);
        this.registerIncoming(State.PLAY, (mappedPacketType != null) ? mappedPacketType.ordinal() : -1, packetType.ordinal(), packetRemapper);
    }
    
    public void cancelIncoming(final S2 packetType) {
        Preconditions.checkArgument(packetType.getClass() == this.newServerboundPacketEnum);
        this.cancelIncoming(State.PLAY, -1, packetType.ordinal());
    }
    
    public boolean hasRegisteredOutgoing(final State state, final int oldPacketID) {
        final Packet packet = new Packet(state, oldPacketID);
        return this.outgoing.containsKey(packet);
    }
    
    public boolean hasRegisteredIncoming(final State state, final int newPacketId) {
        final Packet packet = new Packet(state, newPacketId);
        return this.incoming.containsKey(packet);
    }
    
    public void transform(final Direction direction, final State state, final PacketWrapper packetWrapper) throws Exception {
        final Packet statePacket = new Packet(state, packetWrapper.getId());
        final Map<Packet, ProtocolPacket> packetMap = (direction == Direction.OUTGOING) ? this.outgoing : this.incoming;
        final ProtocolPacket protocolPacket = packetMap.get(statePacket);
        if (protocolPacket == null) {
            return;
        }
        final int oldId = packetWrapper.getId();
        final int newId = (direction == Direction.OUTGOING) ? protocolPacket.getNewID() : protocolPacket.getOldID();
        packetWrapper.setId(newId);
        final PacketRemapper remapper = protocolPacket.getRemapper();
        if (remapper != null) {
            try {
                remapper.remap(packetWrapper);
            }
            catch (InformativeException e) {
                this.throwRemapError(direction, state, oldId, newId, e);
                return;
            }
            if (packetWrapper.isCancelled()) {
                throw CancelException.generate();
            }
        }
    }
    
    private void throwRemapError(final Direction direction, final State state, final int oldId, final int newId, final InformativeException e) throws InformativeException {
        if (state == State.HANDSHAKE) {
            throw e;
        }
        final Class<? extends PacketType> packetTypeClass = (Class<? extends PacketType>)((state == State.PLAY) ? ((direction == Direction.OUTGOING) ? this.oldClientboundPacketEnum : this.newServerboundPacketEnum) : null);
        if (packetTypeClass != null) {
            final PacketType[] enumConstants = (PacketType[])packetTypeClass.getEnumConstants();
            final PacketType packetType = (oldId < enumConstants.length && oldId >= 0) ? enumConstants[oldId] : null;
            Via.getPlatform().getLogger().warning("ERROR IN " + this.getClass().getSimpleName() + " IN REMAP OF " + packetType + " (" + this.toNiceHex(oldId) + ")");
        }
        else {
            Via.getPlatform().getLogger().warning("ERROR IN " + this.getClass().getSimpleName() + " IN REMAP OF " + this.toNiceHex(oldId) + "->" + this.toNiceHex(newId));
        }
        throw e;
    }
    
    private String toNiceHex(final int id) {
        final String hex = Integer.toHexString(id).toUpperCase();
        return ((hex.length() == 1) ? "0x0" : "0x") + hex;
    }
    
    private void checkPacketType(final PacketType packetType, final boolean isValid) {
        if (!isValid) {
            throw new IllegalArgumentException("Packet type " + packetType + " in " + packetType.getClass().getSimpleName() + " is taken from the wrong enum");
        }
    }
    
    @Nullable
    public <T> T get(final Class<T> objectClass) {
        return (T)this.storedObjects.get(objectClass);
    }
    
    public void put(final Object object) {
        this.storedObjects.put(object.getClass(), object);
    }
    
    public boolean hasMappingDataToLoad() {
        return this.getMappingData() != null;
    }
    
    @Nullable
    public MappingData getMappingData() {
        return null;
    }
    
    @Override
    public String toString() {
        return "Protocol:" + this.getClass().getSimpleName();
    }
    
    public static class Packet
    {
        private final State state;
        private final int packetId;
        
        public Packet(final State state, final int packetId) {
            this.state = state;
            this.packetId = packetId;
        }
        
        public State getState() {
            return this.state;
        }
        
        public int getPacketId() {
            return this.packetId;
        }
        
        @Override
        public String toString() {
            return "Packet{state=" + this.state + ", packetId=" + this.packetId + '}';
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final Packet that = (Packet)o;
            return this.packetId == that.packetId && this.state == that.state;
        }
        
        @Override
        public int hashCode() {
            int result = (this.state != null) ? this.state.hashCode() : 0;
            result = 31 * result + this.packetId;
            return result;
        }
    }
    
    public static class ProtocolPacket
    {
        private final State state;
        private final int oldID;
        private final int newID;
        private final PacketRemapper remapper;
        
        public ProtocolPacket(final State state, final int oldID, final int newID, @Nullable final PacketRemapper remapper) {
            this.state = state;
            this.oldID = oldID;
            this.newID = newID;
            this.remapper = remapper;
        }
        
        public State getState() {
            return this.state;
        }
        
        public int getOldID() {
            return this.oldID;
        }
        
        public int getNewID() {
            return this.newID;
        }
        
        @Nullable
        public PacketRemapper getRemapper() {
            return this.remapper;
        }
    }
}
