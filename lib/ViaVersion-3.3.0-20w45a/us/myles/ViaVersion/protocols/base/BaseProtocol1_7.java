// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.base;

import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFuture;
import us.myles.viaversion.libs.bungeecordchat.api.ChatColor;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import com.google.common.base.Joiner;
import java.util.logging.Level;
import java.util.function.Predicate;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.Pair;
import java.util.List;
import us.myles.viaversion.libs.gson.JsonParseException;
import us.myles.ViaVersion.api.protocol.ProtocolVersion;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;
import us.myles.ViaVersion.api.Via;
import us.myles.viaversion.libs.gson.JsonObject;
import us.myles.ViaVersion.util.GsonUtil;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.api.protocol.SimpleProtocol;

public class BaseProtocol1_7 extends SimpleProtocol
{
    @Override
    protected void registerPackets() {
        this.registerOutgoing(State.STATUS, 0, 0, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ProtocolInfo info = wrapper.user().getProtocolInfo();
                        final String originalStatus = wrapper.get(Type.STRING, 0);
                        try {
                            JsonElement json = GsonUtil.getGson().fromJson(originalStatus, JsonElement.class);
                            int protocolVersion = 0;
                            JsonObject version;
                            if (json.isJsonObject()) {
                                if (json.getAsJsonObject().has("version")) {
                                    version = json.getAsJsonObject().get("version").getAsJsonObject();
                                    if (version.has("protocol")) {
                                        protocolVersion = version.get("protocol").getAsLong().intValue();
                                    }
                                }
                                else {
                                    json.getAsJsonObject().add("version", version = new JsonObject());
                                }
                            }
                            else {
                                json = new JsonObject();
                                json.getAsJsonObject().add("version", version = new JsonObject());
                            }
                            if (Via.getConfig().isSendSupportedVersions()) {
                                version.add("supportedVersions", GsonUtil.getGson().toJsonTree(Via.getAPI().getSupportedVersions()));
                            }
                            if (ProtocolRegistry.SERVER_PROTOCOL == -1) {
                                ProtocolRegistry.SERVER_PROTOCOL = ProtocolVersion.getProtocol(protocolVersion).getVersion();
                            }
                            final VersionProvider versionProvider = Via.getManager().getProviders().get(VersionProvider.class);
                            if (versionProvider == null) {
                                wrapper.user().setActive(false);
                                return;
                            }
                            final int protocol = versionProvider.getServerProtocol(wrapper.user());
                            List<Pair<Integer, Protocol>> protocols = null;
                            if (info.getProtocolVersion() >= protocol || Via.getPlatform().isOldClientsAllowed()) {
                                protocols = ProtocolRegistry.getProtocolPath(info.getProtocolVersion(), protocol);
                            }
                            if (protocols != null) {
                                if (protocolVersion == protocol || protocolVersion == 0) {
                                    final ProtocolVersion prot = ProtocolVersion.getProtocol(info.getProtocolVersion());
                                    version.addProperty("protocol", prot.getOriginalVersion());
                                }
                            }
                            else {
                                wrapper.user().setActive(false);
                            }
                            if (Via.getConfig().getBlockedProtocols().contains(info.getProtocolVersion())) {
                                version.addProperty("protocol", -1);
                            }
                            wrapper.set(Type.STRING, 0, GsonUtil.getGson().toJson(json));
                        }
                        catch (JsonParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        this.registerOutgoing(State.STATUS, 1, 1);
        this.registerOutgoing(State.LOGIN, 0, 0);
        this.registerOutgoing(State.LOGIN, 1, 1);
        this.registerOutgoing(State.LOGIN, 2, 2, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ProtocolInfo info = wrapper.user().getProtocolInfo();
                        info.setState(State.PLAY);
                        final UUID uuid = BaseProtocol1_7.this.passthroughLoginUUID(wrapper);
                        info.setUuid(uuid);
                        final String username = wrapper.passthrough(Type.STRING);
                        info.setUsername(username);
                        Via.getManager().handleLoginSuccess(wrapper.user());
                        if (info.getPipeline().pipes().stream().allMatch((Predicate<? super Object>)ProtocolRegistry::isBaseProtocol)) {
                            wrapper.user().setActive(false);
                        }
                        if (Via.getManager().isDebug()) {
                            Via.getPlatform().getLogger().log(Level.INFO, "{0} logged in with protocol {1}, Route: {2}", new Object[] { username, info.getProtocolVersion(), Joiner.on(", ").join((Object)info.getPipeline().pipes(), (Object)", ", new Object[0]) });
                        }
                    }
                });
            }
        });
        this.registerOutgoing(State.LOGIN, 3, 3);
        this.registerIncoming(State.LOGIN, 4, 4);
        this.registerIncoming(State.STATUS, 0, 0);
        this.registerIncoming(State.STATUS, 1, 1);
        this.registerIncoming(State.LOGIN, 0, 0, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int protocol = wrapper.user().getProtocolInfo().getProtocolVersion();
                        if (Via.getConfig().getBlockedProtocols().contains(protocol)) {
                            if (!wrapper.user().getChannel().isOpen()) {
                                return;
                            }
                            if (!wrapper.user().shouldApplyBlockProtocol()) {
                                return;
                            }
                            final PacketWrapper disconnectPacket = new PacketWrapper(0, null, wrapper.user());
                            Protocol1_9To1_8.FIX_JSON.write(disconnectPacket, ChatColor.translateAlternateColorCodes('&', Via.getConfig().getBlockedDisconnectMsg()));
                            wrapper.cancel();
                            final ChannelFuture future = disconnectPacket.sendFuture(BaseProtocol.class);
                            future.addListener(f -> wrapper.user().getChannel().close());
                        }
                    }
                });
            }
        });
        this.registerIncoming(State.LOGIN, 1, 1);
        this.registerIncoming(State.LOGIN, 2, 2);
    }
    
    public static String addDashes(final String trimmedUUID) {
        final StringBuilder idBuff = new StringBuilder(trimmedUUID);
        idBuff.insert(20, '-');
        idBuff.insert(16, '-');
        idBuff.insert(12, '-');
        idBuff.insert(8, '-');
        return idBuff.toString();
    }
    
    protected UUID passthroughLoginUUID(final PacketWrapper wrapper) throws Exception {
        String uuidString = wrapper.passthrough(Type.STRING);
        if (uuidString.length() == 32) {
            uuidString = addDashes(uuidString);
        }
        return UUID.fromString(uuidString);
    }
}
