// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_9to1_8.packets;

import java.util.Optional;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.storage.PlaceBlockTracker;
import us.myles.ViaVersion.api.remapper.ValueCreator;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.ServerboundPackets1_9;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import io.netty.buffer.ByteBuf;
import java.util.Iterator;
import java.util.List;
import java.io.IOException;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.BulkChunkTranslatorProvider;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.CommandBlockProvider;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk1_8;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.types.Chunk1_9to1_8Type;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.storage.ClientChunks;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.sounds.SoundEffect;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.ItemRewriter;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.sounds.Effect;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_8.ClientboundPackets1_8;
import us.myles.ViaVersion.api.protocol.Protocol;

public class WorldPackets
{
    public static void register(final Protocol protocol) {
        protocol.registerOutgoing(ClientboundPackets1_8.UPDATE_SIGN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
            }
        });
        protocol.registerOutgoing(ClientboundPackets1_8.EFFECT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.POSITION);
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        int id = wrapper.get(Type.INT, 0);
                        id = Effect.getNewId(id);
                        wrapper.set(Type.INT, 0, id);
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int id = wrapper.get(Type.INT, 0);
                        if (id == 2002) {
                            final int data = wrapper.get(Type.INT, 1);
                            final int newData = ItemRewriter.getNewEffectID(data);
                            wrapper.set(Type.INT, 1, newData);
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(ClientboundPackets1_8.NAMED_SOUND, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final String name = wrapper.get(Type.STRING, 0);
                        final SoundEffect effect = SoundEffect.getByName(name);
                        int catid = 0;
                        String newname = name;
                        if (effect != null) {
                            catid = effect.getCategory().getId();
                            newname = effect.getNewName();
                        }
                        wrapper.set(Type.STRING, 0, newname);
                        wrapper.write(Type.VAR_INT, catid);
                        if (effect != null && effect.isBreaksound()) {
                            final EntityTracker1_9 tracker = wrapper.user().get(EntityTracker1_9.class);
                            final int x = wrapper.passthrough(Type.INT);
                            final int y = wrapper.passthrough(Type.INT);
                            final int z = wrapper.passthrough(Type.INT);
                            if (tracker.interactedBlockRecently((int)Math.floor(x / 8.0), (int)Math.floor(y / 8.0), (int)Math.floor(z / 8.0))) {
                                wrapper.cancel();
                            }
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(ClientboundPackets1_8.CHUNK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ClientChunks clientChunks = wrapper.user().get(ClientChunks.class);
                        final Chunk1_9to1_8Type type = new Chunk1_9to1_8Type(clientChunks);
                        final Chunk1_8 chunk = wrapper.read((Type<Chunk1_8>)type);
                        if (chunk.isUnloadPacket()) {
                            wrapper.setId(29);
                            wrapper.write(Type.INT, chunk.getX());
                            wrapper.write(Type.INT, chunk.getZ());
                            final CommandBlockProvider provider = Via.getManager().getProviders().get(CommandBlockProvider.class);
                            provider.unloadChunk(wrapper.user(), chunk.getX(), chunk.getZ());
                        }
                        else {
                            wrapper.write((Type<Chunk1_8>)type, chunk);
                        }
                        wrapper.read(Type.REMAINING_BYTES);
                    }
                });
            }
        });
        protocol.registerOutgoing(ClientboundPackets1_8.MAP_BULK_CHUNK, null, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        wrapper.cancel();
                        final BulkChunkTranslatorProvider provider = Via.getManager().getProviders().get(BulkChunkTranslatorProvider.class);
                        if (!provider.isPacketLevel()) {
                            return;
                        }
                        final List<Object> list = provider.transformMapChunkBulk(wrapper, wrapper.user().get(ClientChunks.class));
                        for (final Object obj : list) {
                            if (!(obj instanceof PacketWrapper)) {
                                throw new IOException("transformMapChunkBulk returned the wrong object type");
                            }
                            final PacketWrapper output = (PacketWrapper)obj;
                            final ByteBuf buffer = wrapper.user().getChannel().alloc().buffer();
                            try {
                                output.setId(-1);
                                output.writeToBuffer(buffer);
                                final PacketWrapper chunkPacket = new PacketWrapper(33, buffer, wrapper.user());
                                chunkPacket.send(Protocol1_9To1_8.class, false, true);
                            }
                            finally {
                                buffer.release();
                            }
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(ClientboundPackets1_8.BLOCK_ENTITY_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.NBT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int action = wrapper.get(Type.UNSIGNED_BYTE, 0);
                        if (action == 1) {
                            final CompoundTag tag = wrapper.get(Type.NBT, 0);
                            if (tag != null) {
                                if (tag.contains("EntityId")) {
                                    final String entity = (String)tag.get("EntityId").getValue();
                                    final CompoundTag spawn = new CompoundTag("SpawnData");
                                    spawn.put(new StringTag("id", entity));
                                    tag.put(spawn);
                                }
                                else {
                                    final CompoundTag spawn2 = new CompoundTag("SpawnData");
                                    spawn2.put(new StringTag("id", "AreaEffectCloud"));
                                    tag.put(spawn2);
                                }
                            }
                        }
                        if (action == 2) {
                            final CommandBlockProvider provider = Via.getManager().getProviders().get(CommandBlockProvider.class);
                            provider.addOrUpdateBlock(wrapper.user(), wrapper.get(Type.POSITION, 0), wrapper.get(Type.NBT, 0));
                            wrapper.cancel();
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(ClientboundPackets1_8.BLOCK_CHANGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.VAR_INT);
            }
        });
        protocol.registerIncoming(ServerboundPackets1_9.UPDATE_SIGN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
                this.map(Type.STRING, Protocol1_9To1_8.FIX_JSON);
            }
        });
        protocol.registerIncoming(ServerboundPackets1_9.PLAYER_DIGGING, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT, Type.UNSIGNED_BYTE);
                this.map(Type.POSITION);
                this.map(Type.BYTE);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int status = wrapper.get(Type.UNSIGNED_BYTE, 0);
                        if (status == 6) {
                            wrapper.cancel();
                        }
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int status = wrapper.get(Type.UNSIGNED_BYTE, 0);
                        if (status == 5 || status == 4 || status == 3) {
                            final EntityTracker1_9 entityTracker = wrapper.user().get(EntityTracker1_9.class);
                            if (entityTracker.isBlocking()) {
                                entityTracker.setBlocking(false);
                                entityTracker.setSecondHand(null);
                            }
                        }
                    }
                });
            }
        });
        protocol.registerIncoming(ServerboundPackets1_9.USE_ITEM, null, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int hand = wrapper.read((Type<Integer>)Type.VAR_INT);
                        wrapper.clearInputBuffer();
                        wrapper.setId(8);
                        wrapper.write(Type.POSITION, new Position(-1, (short)(-1), -1));
                        wrapper.write(Type.UNSIGNED_BYTE, (Short)255);
                        final Item item = Protocol1_9To1_8.getHandItem(wrapper.user());
                        if (Via.getConfig().isShieldBlocking()) {
                            final EntityTracker1_9 tracker = wrapper.user().get(EntityTracker1_9.class);
                            if (item != null && Protocol1_9To1_8.isSword(item.getIdentifier())) {
                                if (hand == 0) {
                                    if (!tracker.isBlocking()) {
                                        tracker.setBlocking(true);
                                        final Item shield = new Item(442, (byte)1, (short)0, null);
                                        tracker.setSecondHand(shield);
                                    }
                                    wrapper.cancel();
                                }
                            }
                            else {
                                tracker.setSecondHand(null);
                                tracker.setBlocking(false);
                            }
                        }
                        wrapper.write(Type.ITEM, item);
                        wrapper.write(Type.UNSIGNED_BYTE, (Short)0);
                        wrapper.write(Type.UNSIGNED_BYTE, (Short)0);
                        wrapper.write(Type.UNSIGNED_BYTE, (Short)0);
                    }
                });
            }
        });
        protocol.registerIncoming(ServerboundPackets1_9.PLAYER_BLOCK_PLACEMENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.VAR_INT, Type.UNSIGNED_BYTE);
                this.map(Type.VAR_INT, Type.NOTHING);
                this.create(new ValueCreator() {
                    @Override
                    public void write(final PacketWrapper wrapper) throws Exception {
                        final Item item = Protocol1_9To1_8.getHandItem(wrapper.user());
                        wrapper.write(Type.ITEM, item);
                    }
                });
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final Position position = wrapper.get(Type.POSITION, 0);
                        final PlaceBlockTracker tracker = wrapper.user().get(PlaceBlockTracker.class);
                        if (tracker.getLastPlacedPosition() != null && tracker.getLastPlacedPosition().equals(position) && !tracker.isExpired(50)) {
                            wrapper.cancel();
                        }
                        tracker.updateTime();
                        tracker.setLastPlacedPosition(position);
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int face = wrapper.get(Type.UNSIGNED_BYTE, 0);
                        if (face == 255) {
                            return;
                        }
                        final Position p = wrapper.get(Type.POSITION, 0);
                        int x = p.getX();
                        short y = p.getY();
                        int z = p.getZ();
                        switch (face) {
                            case 0: {
                                --y;
                                break;
                            }
                            case 1: {
                                ++y;
                                break;
                            }
                            case 2: {
                                --z;
                                break;
                            }
                            case 3: {
                                ++z;
                                break;
                            }
                            case 4: {
                                --x;
                                break;
                            }
                            case 5: {
                                ++x;
                                break;
                            }
                        }
                        final EntityTracker1_9 tracker = wrapper.user().get(EntityTracker1_9.class);
                        tracker.addBlockInteraction(new Position(x, y, z));
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final CommandBlockProvider provider = Via.getManager().getProviders().get(CommandBlockProvider.class);
                        final Position pos = wrapper.get(Type.POSITION, 0);
                        final Optional<CompoundTag> tag = provider.get(wrapper.user(), pos);
                        if (tag.isPresent()) {
                            final PacketWrapper updateBlockEntity = new PacketWrapper(9, null, wrapper.user());
                            updateBlockEntity.write(Type.POSITION, pos);
                            updateBlockEntity.write(Type.UNSIGNED_BYTE, (Short)2);
                            updateBlockEntity.write(Type.NBT, tag.get());
                            updateBlockEntity.send(Protocol1_9To1_8.class);
                        }
                    }
                });
            }
        });
    }
}
