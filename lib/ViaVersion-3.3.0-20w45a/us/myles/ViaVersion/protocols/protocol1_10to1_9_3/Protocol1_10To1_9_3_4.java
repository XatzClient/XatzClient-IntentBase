// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_10to1_9_3;

import java.util.Iterator;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.protocols.protocol1_10to1_9_3.storage.ResourcePackTracker;
import us.myles.ViaVersion.api.type.types.version.Types1_9;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.List;
import us.myles.ViaVersion.api.remapper.ValueTransformer;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ServerboundPackets1_9_3;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import us.myles.ViaVersion.api.protocol.Protocol;

public class Protocol1_10To1_9_3_4 extends Protocol<ClientboundPackets1_9_3, ClientboundPackets1_9_3, ServerboundPackets1_9_3, ServerboundPackets1_9_3>
{
    public static final ValueTransformer<Short, Float> TO_NEW_PITCH;
    public static final ValueTransformer<List<Metadata>, List<Metadata>> TRANSFORM_METADATA;
    
    public Protocol1_10To1_9_3_4() {
        super(ClientboundPackets1_9_3.class, ClientboundPackets1_9_3.class, ServerboundPackets1_9_3.class, ServerboundPackets1_9_3.class);
    }
    
    @Override
    protected void registerPackets() {
        this.registerOutgoing(State.PLAY, 25, 25, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.VAR_INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.FLOAT);
                this.map(Type.UNSIGNED_BYTE, Protocol1_10To1_9_3_4.TO_NEW_PITCH);
            }
        });
        this.registerOutgoing(State.PLAY, 70, 70, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.FLOAT);
                this.map(Type.UNSIGNED_BYTE, Protocol1_10To1_9_3_4.TO_NEW_PITCH);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int id = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        wrapper.set(Type.VAR_INT, 0, Protocol1_10To1_9_3_4.this.getNewSoundId(id));
                    }
                });
            }
        });
        this.registerOutgoing(State.PLAY, 57, 57, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Types1_9.METADATA_LIST, Protocol1_10To1_9_3_4.TRANSFORM_METADATA);
            }
        });
        this.registerOutgoing(State.PLAY, 3, 3, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Types1_9.METADATA_LIST, Protocol1_10To1_9_3_4.TRANSFORM_METADATA);
            }
        });
        this.registerOutgoing(State.PLAY, 5, 5, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Types1_9.METADATA_LIST, Protocol1_10To1_9_3_4.TRANSFORM_METADATA);
            }
        });
        this.registerOutgoing(State.PLAY, 50, 50, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.STRING);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ResourcePackTracker tracker = wrapper.user().get(ResourcePackTracker.class);
                        tracker.setLastHash(wrapper.get(Type.STRING, 1));
                    }
                });
            }
        });
        this.registerIncoming(State.PLAY, 22, 22, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ResourcePackTracker tracker = wrapper.user().get(ResourcePackTracker.class);
                        wrapper.write(Type.STRING, tracker.getLastHash());
                        wrapper.write((Type<Object>)Type.VAR_INT, wrapper.read((Type<T>)Type.VAR_INT));
                    }
                });
            }
        });
    }
    
    public int getNewSoundId(final int id) {
        int newId = id;
        if (id >= 24) {
            ++newId;
        }
        if (id >= 248) {
            newId += 4;
        }
        if (id >= 296) {
            newId += 6;
        }
        if (id >= 354) {
            newId += 4;
        }
        if (id >= 372) {
            newId += 4;
        }
        return newId;
    }
    
    @Override
    public void init(final UserConnection userConnection) {
        userConnection.put(new ResourcePackTracker(userConnection));
    }
    
    static {
        TO_NEW_PITCH = new ValueTransformer<Short, Float>(Type.FLOAT) {
            @Override
            public Float transform(final PacketWrapper wrapper, final Short inputValue) throws Exception {
                return inputValue / 63.0f;
            }
        };
        TRANSFORM_METADATA = new ValueTransformer<List<Metadata>, List<Metadata>>(Types1_9.METADATA_LIST) {
            @Override
            public List<Metadata> transform(final PacketWrapper wrapper, final List<Metadata> inputValue) throws Exception {
                final List<Metadata> metaList = new CopyOnWriteArrayList<Metadata>(inputValue);
                for (final Metadata m : metaList) {
                    if (m.getId() >= 5) {
                        m.setId(m.getId() + 1);
                    }
                }
                return metaList;
            }
        };
    }
}
