// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8;

import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.PlayerAbilities;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.WorldBorder;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.Scoreboard;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.storage.ClientChunks;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.GameProfileStorage;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.PlayerPosition;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.EntityTracker;
import us.myles.ViaVersion.api.data.StoredObject;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.Windows;
import de.gerrygames.viarewind.utils.Ticker;
import us.myles.ViaVersion.api.data.UserConnection;
import io.netty.channel.Channel;
import de.gerrygames.viarewind.netty.ForwardMessageToByteEncoder;
import io.netty.channel.ChannelHandler;
import de.gerrygames.viarewind.netty.EmptyChannelHandler;
import us.myles.ViaVersion.packets.Direction;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.CompressionSendStorage;
import us.myles.ViaVersion.api.type.types.CustomByteType;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.packets.State;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets.WorldPackets;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets.SpawnPackets;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets.ScoreboardPackets;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets.PlayerPackets;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets.InventoryPackets;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets.EntityPackets;
import us.myles.ViaVersion.api.protocol.Protocol;

public class Protocol1_7_6_10TO1_8 extends Protocol
{
    protected void registerPackets() {
        EntityPackets.register(this);
        InventoryPackets.register(this);
        PlayerPackets.register(this);
        ScoreboardPackets.register(this);
        SpawnPackets.register(this);
        WorldPackets.register(this);
        this.registerOutgoing(State.PLAY, 0, 0, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT, Type.INT);
            }
        });
        this.registerOutgoing(State.PLAY, 70, -1, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.cancel();
                    }
                });
            }
        });
        this.registerIncoming(State.PLAY, 0, 0, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT, (Type)Type.VAR_INT);
            }
        });
        this.registerOutgoing(State.LOGIN, 1, 1, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.STRING);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int publicKeyLength = (int)packetWrapper.read((Type)Type.VAR_INT);
                        packetWrapper.write((Type)Type.SHORT, (Object)(short)publicKeyLength);
                        packetWrapper.passthrough((Type)new CustomByteType(Integer.valueOf(publicKeyLength)));
                        final int verifyTokenLength = (int)packetWrapper.read((Type)Type.VAR_INT);
                        packetWrapper.write((Type)Type.SHORT, (Object)(short)verifyTokenLength);
                        packetWrapper.passthrough((Type)new CustomByteType(Integer.valueOf(verifyTokenLength)));
                    }
                });
            }
        });
        this.registerOutgoing(State.LOGIN, 3, 3, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.cancel();
                        ((CompressionSendStorage)packetWrapper.user().get((Class)CompressionSendStorage.class)).setCompressionSend(true);
                    }
                });
            }
        });
        this.registerIncoming(State.LOGIN, 1, 1, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int sharedSecretLength = (short)packetWrapper.read((Type)Type.SHORT);
                        packetWrapper.write((Type)Type.VAR_INT, (Object)sharedSecretLength);
                        packetWrapper.passthrough((Type)new CustomByteType(Integer.valueOf(sharedSecretLength)));
                        final int verifyTokenLength = (short)packetWrapper.read((Type)Type.SHORT);
                        packetWrapper.write((Type)Type.VAR_INT, (Object)verifyTokenLength);
                        packetWrapper.passthrough((Type)new CustomByteType(Integer.valueOf(verifyTokenLength)));
                    }
                });
            }
        });
    }
    
    public void transform(final Direction direction, final State state, final PacketWrapper packetWrapper) throws Exception {
        final CompressionSendStorage compressionSendStorage = (CompressionSendStorage)packetWrapper.user().get((Class)CompressionSendStorage.class);
        if (compressionSendStorage.isCompressionSend()) {
            final Channel channel = packetWrapper.user().getChannel();
            if (channel.pipeline().get("compress") != null) {
                channel.pipeline().replace("decompress", "decompress", (ChannelHandler)new EmptyChannelHandler());
                channel.pipeline().replace("compress", "compress", (ChannelHandler)new ForwardMessageToByteEncoder());
            }
            else if (channel.pipeline().get("compression-encoder") != null) {
                channel.pipeline().replace("compression-decoder", "compression-decoder", (ChannelHandler)new EmptyChannelHandler());
                channel.pipeline().replace("compression-encoder", "compression-encoder", (ChannelHandler)new ForwardMessageToByteEncoder());
            }
            compressionSendStorage.setCompressionSend(false);
        }
        super.transform(direction, state, packetWrapper);
    }
    
    public void init(final UserConnection userConnection) {
        Ticker.init();
        userConnection.put((StoredObject)new Windows(userConnection));
        userConnection.put((StoredObject)new EntityTracker(userConnection));
        userConnection.put((StoredObject)new PlayerPosition(userConnection));
        userConnection.put((StoredObject)new GameProfileStorage(userConnection));
        userConnection.put((StoredObject)new ClientChunks(userConnection));
        userConnection.put((StoredObject)new Scoreboard(userConnection));
        userConnection.put((StoredObject)new CompressionSendStorage(userConnection));
        userConnection.put((StoredObject)new WorldBorder(userConnection));
        userConnection.put((StoredObject)new PlayerAbilities(userConnection));
        userConnection.put((StoredObject)new ClientWorld(userConnection));
    }
}
