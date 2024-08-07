// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.base;

import us.myles.ViaVersion.packets.Direction;
import us.myles.ViaVersion.api.platform.providers.ViaProviders;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.PacketWrapper;
import java.util.Iterator;
import us.myles.ViaVersion.api.protocol.ProtocolPipeline;
import java.util.List;
import us.myles.ViaVersion.api.protocol.ProtocolVersion;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.Pair;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.api.protocol.SimpleProtocol;

public class BaseProtocol extends SimpleProtocol
{
    @Override
    protected void registerPackets() {
        this.registerIncoming(State.HANDSHAKE, 0, 0, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int protocolVersion;
                final int state;
                final ProtocolInfo info;
                int serverProtocol;
                List<Pair<Integer, Protocol>> protocols;
                ProtocolPipeline pipeline;
                final Iterator<Pair<Integer, Protocol>> iterator;
                Pair<Integer, Protocol> prot;
                ProtocolVersion protocol;
                this.handler(wrapper -> {
                    protocolVersion = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                    wrapper.passthrough(Type.STRING);
                    wrapper.passthrough(Type.UNSIGNED_SHORT);
                    state = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                    info = wrapper.user().getProtocolInfo();
                    info.setProtocolVersion(protocolVersion);
                    if (Via.getManager().getProviders().get(VersionProvider.class) == null) {
                        wrapper.user().setActive(false);
                    }
                    else {
                        serverProtocol = Via.getManager().getProviders().get(VersionProvider.class).getServerProtocol(wrapper.user());
                        info.setServerProtocolVersion(serverProtocol);
                        protocols = null;
                        if (info.getProtocolVersion() >= serverProtocol || Via.getPlatform().isOldClientsAllowed()) {
                            protocols = ProtocolRegistry.getProtocolPath(info.getProtocolVersion(), serverProtocol);
                        }
                        pipeline = wrapper.user().getProtocolInfo().getPipeline();
                        if (protocols != null) {
                            protocols.iterator();
                            while (iterator.hasNext()) {
                                prot = iterator.next();
                                pipeline.add(prot.getValue());
                                ProtocolRegistry.completeMappingDataLoading(prot.getValue().getClass());
                            }
                            protocol = ProtocolVersion.getProtocol(serverProtocol);
                            wrapper.set(Type.VAR_INT, 0, protocol.getOriginalVersion());
                        }
                        pipeline.add(ProtocolRegistry.getBaseProtocol(serverProtocol));
                        if (state == 1) {
                            info.setState(State.STATUS);
                        }
                        if (state == 2) {
                            info.setState(State.LOGIN);
                        }
                    }
                });
            }
        });
    }
    
    @Override
    public void init(final UserConnection userConnection) {
    }
    
    @Override
    protected void register(final ViaProviders providers) {
        providers.register(VersionProvider.class, new VersionProvider());
    }
    
    @Override
    public void transform(final Direction direction, final State state, final PacketWrapper packetWrapper) throws Exception {
        super.transform(direction, state, packetWrapper);
        if (direction == Direction.INCOMING && state == State.HANDSHAKE && packetWrapper.getId() != 0) {
            packetWrapper.user().setActive(false);
        }
    }
}
