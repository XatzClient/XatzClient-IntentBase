// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets;

import de.gerrygames.viarewind.types.VarLongType;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.WorldBorder;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Types1_7_6_10;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;
import us.myles.ViaVersion.api.type.types.CustomByteType;
import io.netty.buffer.ByteBuf;
import us.myles.viaversion.libs.bungeecordchat.api.ChatColor;
import de.gerrygames.viarewind.utils.ChatUtil;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Particle;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.chunks.ChunkPacketTransformer;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.minecraft.BlockChangeRecord;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.items.ReplacementRegistry1_7_6_10to1_8;
import de.gerrygames.viarewind.storage.BlockState;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Chunk1_7_10Type;
import us.myles.ViaVersion.api.type.Type;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.types.Chunk1_8Type;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.api.protocol.Protocol;

public class WorldPackets
{
    public static void register(final Protocol protocol) {
        protocol.registerOutgoing(State.PLAY, 33, 33, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final ClientWorld world = (ClientWorld)packetWrapper.user().get((Class)ClientWorld.class);
                        final Chunk chunk = (Chunk)packetWrapper.read((Type)new Chunk1_8Type(world));
                        packetWrapper.write((Type)new Chunk1_7_10Type(world), (Object)chunk);
                        for (final ChunkSection section : chunk.getSections()) {
                            if (section != null) {
                                for (int i = 0; i < section.getPaletteSize(); ++i) {
                                    final int block = section.getPaletteEntry(i);
                                    BlockState state = BlockState.rawToState(block);
                                    state = ReplacementRegistry1_7_6_10to1_8.replace(state);
                                    section.setPaletteEntry(i, BlockState.stateToRaw(state));
                                }
                            }
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 34, 34, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final BlockChangeRecord[] records = (BlockChangeRecord[])packetWrapper.read(Type.BLOCK_CHANGE_RECORD_ARRAY);
                        packetWrapper.write((Type)Type.SHORT, (Object)(short)records.length);
                        packetWrapper.write(Type.INT, (Object)(records.length * 4));
                        for (final BlockChangeRecord record : records) {
                            final short data = (short)(record.getSectionX() << 12 | record.getSectionZ() << 8 | record.getY());
                            packetWrapper.write((Type)Type.SHORT, (Object)data);
                            BlockState state = BlockState.rawToState(record.getBlockId());
                            state = ReplacementRegistry1_7_6_10to1_8.replace(state);
                            packetWrapper.write((Type)Type.SHORT, (Object)(short)BlockState.stateToRaw(state));
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 35, 35, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final Position position = (Position)packetWrapper.read(Type.POSITION);
                        packetWrapper.write(Type.INT, (Object)position.getX());
                        packetWrapper.write(Type.UNSIGNED_BYTE, (Object)position.getY());
                        packetWrapper.write(Type.INT, (Object)position.getZ());
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int data = (int)packetWrapper.read((Type)Type.VAR_INT);
                        int blockId = data >> 4;
                        int meta = data & 0xF;
                        final BlockState state = ReplacementRegistry1_7_6_10to1_8.replace(new BlockState(blockId, meta));
                        blockId = state.getId();
                        meta = state.getData();
                        packetWrapper.write((Type)Type.VAR_INT, (Object)blockId);
                        packetWrapper.write(Type.UNSIGNED_BYTE, (Object)(short)meta);
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 36, 36, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final Position position = (Position)packetWrapper.read(Type.POSITION);
                        packetWrapper.write(Type.INT, (Object)position.getX());
                        packetWrapper.write((Type)Type.SHORT, (Object)position.getY());
                        packetWrapper.write(Type.INT, (Object)position.getZ());
                    }
                });
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map((Type)Type.VAR_INT);
            }
        });
        protocol.registerOutgoing(State.PLAY, 37, 37, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final Position position = (Position)packetWrapper.read(Type.POSITION);
                        packetWrapper.write(Type.INT, (Object)position.getX());
                        packetWrapper.write(Type.INT, (Object)(int)position.getY());
                        packetWrapper.write(Type.INT, (Object)position.getZ());
                    }
                });
                this.map(Type.BYTE);
            }
        });
        protocol.registerOutgoing(State.PLAY, 38, 38, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        ChunkPacketTransformer.transformChunkBulk(packetWrapper);
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 40, 40, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final Position position = (Position)packetWrapper.read(Type.POSITION);
                        packetWrapper.write(Type.INT, (Object)position.getX());
                        packetWrapper.write(Type.BYTE, (Object)(byte)position.getY());
                        packetWrapper.write(Type.INT, (Object)position.getZ());
                    }
                });
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
            }
        });
        protocol.registerOutgoing(State.PLAY, 42, 42, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int particleId = (int)packetWrapper.read(Type.INT);
                        Particle particle = Particle.find(particleId);
                        if (particle == null) {
                            particle = Particle.CRIT;
                        }
                        packetWrapper.write(Type.STRING, (Object)particle.name);
                        packetWrapper.read(Type.BOOLEAN);
                    }
                });
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map(Type.INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        String name = (String)packetWrapper.get(Type.STRING, 0);
                        Particle particle = Particle.find(name);
                        if (particle == Particle.ICON_CRACK || particle == Particle.BLOCK_CRACK || particle == Particle.BLOCK_DUST) {
                            final int id = (int)packetWrapper.read((Type)Type.VAR_INT);
                            final int data = (int)((particle == Particle.ICON_CRACK) ? packetWrapper.read((Type)Type.VAR_INT) : 0);
                            if ((id >= 256 && id <= 422) || (id >= 2256 && id <= 2267)) {
                                particle = Particle.ICON_CRACK;
                            }
                            else {
                                if ((id < 0 || id > 164) && (id < 170 || id > 175)) {
                                    packetWrapper.cancel();
                                    return;
                                }
                                if (particle == Particle.ICON_CRACK) {
                                    particle = Particle.BLOCK_CRACK;
                                }
                            }
                            name = particle.name + "_" + id + "_" + data;
                        }
                        packetWrapper.set(Type.STRING, 0, (Object)name);
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 51, 51, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final Position position = (Position)packetWrapper.read(Type.POSITION);
                        packetWrapper.write(Type.INT, (Object)position.getX());
                        packetWrapper.write((Type)Type.SHORT, (Object)position.getY());
                        packetWrapper.write(Type.INT, (Object)position.getZ());
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        for (int i = 0; i < 4; ++i) {
                            String line = (String)packetWrapper.read(Type.STRING);
                            line = ChatUtil.jsonToLegacy(line);
                            line = ChatUtil.removeUnusedColor(line, '0');
                            if (line.length() > 15) {
                                line = ChatColor.stripColor(line);
                                if (line.length() > 15) {
                                    line = line.substring(0, 15);
                                }
                            }
                            packetWrapper.write(Type.STRING, (Object)line);
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 52, 52, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.cancel();
                        final int id = (int)packetWrapper.read((Type)Type.VAR_INT);
                        final byte scale = (byte)packetWrapper.read(Type.BYTE);
                        final int count = (int)packetWrapper.read((Type)Type.VAR_INT);
                        final byte[] icons = new byte[count * 4];
                        for (int i = 0; i < count; ++i) {
                            final int j = (byte)packetWrapper.read(Type.BYTE);
                            icons[i * 4] = (byte)(j >> 4 & 0xF);
                            icons[i * 4 + 1] = (byte)packetWrapper.read(Type.BYTE);
                            icons[i * 4 + 2] = (byte)packetWrapper.read(Type.BYTE);
                            icons[i * 4 + 3] = (byte)(j & 0xF);
                        }
                        final short columns = (short)packetWrapper.read(Type.UNSIGNED_BYTE);
                        if (columns > 0) {
                            final short rows = (short)packetWrapper.read(Type.UNSIGNED_BYTE);
                            final byte x = (byte)packetWrapper.read(Type.BYTE);
                            final byte z = (byte)packetWrapper.read(Type.BYTE);
                            final byte[] data = (byte[])packetWrapper.read(Type.BYTE_ARRAY_PRIMITIVE);
                            for (int column = 0; column < columns; ++column) {
                                final byte[] columnData = new byte[rows + 3];
                                columnData[0] = 0;
                                columnData[1] = (byte)(x + column);
                                columnData[2] = z;
                                for (int k = 0; k < rows; ++k) {
                                    columnData[k + 3] = data[column + k * columns];
                                }
                                final PacketWrapper columnUpdate = new PacketWrapper(52, (ByteBuf)null, packetWrapper.user());
                                columnUpdate.write((Type)Type.VAR_INT, (Object)id);
                                columnUpdate.write((Type)Type.SHORT, (Object)(short)columnData.length);
                                columnUpdate.write((Type)new CustomByteType(Integer.valueOf(columnData.length)), (Object)columnData);
                                PacketUtil.sendPacket(columnUpdate, Protocol1_7_6_10TO1_8.class, true, true);
                            }
                        }
                        if (count > 0) {
                            final byte[] iconData = new byte[count * 3 + 1];
                            iconData[0] = 1;
                            for (int l = 0; l < count; ++l) {
                                iconData[l * 3 + 1] = (byte)(icons[l * 4] << 4 | (icons[l * 4 + 3] & 0xF));
                                iconData[l * 3 + 2] = icons[l * 4 + 1];
                                iconData[l * 3 + 3] = icons[l * 4 + 2];
                            }
                            final PacketWrapper iconUpdate = new PacketWrapper(52, (ByteBuf)null, packetWrapper.user());
                            iconUpdate.write((Type)Type.VAR_INT, (Object)id);
                            iconUpdate.write((Type)Type.SHORT, (Object)(short)iconData.length);
                            final CustomByteType customByteType = new CustomByteType(Integer.valueOf(iconData.length));
                            iconUpdate.write((Type)customByteType, (Object)iconData);
                            PacketUtil.sendPacket(iconUpdate, Protocol1_7_6_10TO1_8.class, true, true);
                        }
                        final PacketWrapper scaleUpdate = new PacketWrapper(52, (ByteBuf)null, packetWrapper.user());
                        scaleUpdate.write((Type)Type.VAR_INT, (Object)id);
                        scaleUpdate.write((Type)Type.SHORT, (Object)2);
                        scaleUpdate.write((Type)new CustomByteType(Integer.valueOf(2)), (Object)new byte[] { 2, scale });
                        PacketUtil.sendPacket(scaleUpdate, Protocol1_7_6_10TO1_8.class, true, true);
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 53, 53, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final Position position = (Position)packetWrapper.read(Type.POSITION);
                        packetWrapper.write(Type.INT, (Object)position.getX());
                        packetWrapper.write((Type)Type.SHORT, (Object)position.getY());
                        packetWrapper.write(Type.INT, (Object)position.getZ());
                    }
                });
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.NBT, (Type)Types1_7_6_10.COMPRESSED_NBT);
            }
        });
        protocol.registerOutgoing(State.PLAY, 65, -1, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.cancel();
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 66, -1, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.cancel();
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 68, -1, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int action = (int)packetWrapper.read((Type)Type.VAR_INT);
                        final WorldBorder worldBorder = (WorldBorder)packetWrapper.user().get((Class)WorldBorder.class);
                        if (action == 0) {
                            worldBorder.setSize((double)packetWrapper.read(Type.DOUBLE));
                        }
                        else if (action == 1) {
                            worldBorder.lerpSize((double)packetWrapper.read(Type.DOUBLE), (double)packetWrapper.read(Type.DOUBLE), (long)packetWrapper.read((Type)VarLongType.VAR_LONG));
                        }
                        else if (action == 2) {
                            worldBorder.setCenter((double)packetWrapper.read(Type.DOUBLE), (double)packetWrapper.read(Type.DOUBLE));
                        }
                        else if (action == 3) {
                            worldBorder.init((double)packetWrapper.read(Type.DOUBLE), (double)packetWrapper.read(Type.DOUBLE), (double)packetWrapper.read(Type.DOUBLE), (double)packetWrapper.read(Type.DOUBLE), (long)packetWrapper.read((Type)VarLongType.VAR_LONG), (int)packetWrapper.read((Type)Type.VAR_INT), (int)packetWrapper.read((Type)Type.VAR_INT), (int)packetWrapper.read((Type)Type.VAR_INT));
                        }
                        else if (action == 4) {
                            worldBorder.setWarningTime((int)packetWrapper.read((Type)Type.VAR_INT));
                        }
                        else if (action == 5) {
                            worldBorder.setWarningBlocks((int)packetWrapper.read((Type)Type.VAR_INT));
                        }
                        packetWrapper.cancel();
                    }
                });
            }
        });
    }
}
