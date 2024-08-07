// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.protocol;

import us.myles.ViaVersion.api.platform.ViaPlatform;
import java.util.logging.Level;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.packets.Direction;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import us.myles.ViaVersion.protocols.base.ProtocolInfo;
import java.util.concurrent.CopyOnWriteArrayList;
import us.myles.ViaVersion.api.data.UserConnection;
import java.util.List;

public class ProtocolPipeline extends SimpleProtocol
{
    private List<Protocol> protocolList;
    private UserConnection userConnection;
    
    public ProtocolPipeline(final UserConnection userConnection) {
        this.init(userConnection);
    }
    
    @Override
    protected void registerPackets() {
        (this.protocolList = new CopyOnWriteArrayList<Protocol>()).add(ProtocolRegistry.BASE_PROTOCOL);
    }
    
    @Override
    public void init(final UserConnection userConnection) {
        this.userConnection = userConnection;
        final ProtocolInfo protocolInfo = new ProtocolInfo(userConnection);
        protocolInfo.setPipeline(this);
        userConnection.setProtocolInfo(protocolInfo);
        for (final Protocol protocol : this.protocolList) {
            protocol.init(userConnection);
        }
    }
    
    public void add(final Protocol protocol) {
        if (this.protocolList != null) {
            this.protocolList.add(protocol);
            protocol.init(this.userConnection);
            final List<Protocol> toMove = new ArrayList<Protocol>();
            for (final Protocol p : this.protocolList) {
                if (ProtocolRegistry.isBaseProtocol(p)) {
                    toMove.add(p);
                }
            }
            this.protocolList.removeAll(toMove);
            this.protocolList.addAll(toMove);
            return;
        }
        throw new NullPointerException("Tried to add protocol too early");
    }
    
    @Override
    public void transform(final Direction direction, final State state, final PacketWrapper packetWrapper) throws Exception {
        final int originalID = packetWrapper.getId();
        packetWrapper.apply(direction, state, 0, this.protocolList, direction == Direction.OUTGOING);
        super.transform(direction, state, packetWrapper);
        if (Via.getManager().isDebug()) {
            this.logPacket(direction, state, packetWrapper, originalID);
        }
    }
    
    private void logPacket(final Direction direction, final State state, final PacketWrapper packetWrapper, final int originalID) {
        final int clientProtocol = this.userConnection.getProtocolInfo().getProtocolVersion();
        final ViaPlatform platform = Via.getPlatform();
        final String actualUsername = packetWrapper.user().getProtocolInfo().getUsername();
        final String username = (actualUsername != null) ? (actualUsername + " ") : "";
        platform.getLogger().log(Level.INFO, "{0}{1} {2}: {3} (0x{4}) -> {5} (0x{6}) [{7}] {8}", new Object[] { username, direction, state, originalID, Integer.toHexString(originalID), packetWrapper.getId(), Integer.toHexString(packetWrapper.getId()), Integer.toString(clientProtocol), packetWrapper });
    }
    
    public boolean contains(final Class<? extends Protocol> pipeClass) {
        for (final Protocol protocol : this.protocolList) {
            if (protocol.getClass().equals(pipeClass)) {
                return true;
            }
        }
        return false;
    }
    
    public <P extends Protocol> P getProtocol(final Class<P> pipeClass) {
        for (final Protocol protocol : this.protocolList) {
            if (protocol.getClass() == pipeClass) {
                return (P)protocol;
            }
        }
        return null;
    }
    
    public boolean filter(final Object o, final List list) throws Exception {
        for (final Protocol protocol : this.protocolList) {
            if (protocol.isFiltered(o.getClass())) {
                protocol.filterPacket(this.userConnection, o, list);
                return true;
            }
        }
        return false;
    }
    
    public List<Protocol> pipes() {
        return this.protocolList;
    }
    
    public void cleanPipes() {
        this.pipes().clear();
        this.registerPackets();
    }
}
