// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets;

import de.gerrygames.viarewind.ViaRewind;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.sound.Effect;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk1_8;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.types.Chunk1_8Type;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.sound.SoundRemapper;
import us.myles.ViaVersion.api.minecraft.BlockChangeRecord;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.items.ReplacementRegistry1_8to1_9;
import de.gerrygames.viarewind.storage.BlockState;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.api.protocol.Protocol;

public class WorldPackets
{
    public static void register(final Protocol protocol) {
        protocol.registerOutgoing(State.PLAY, 8, 37);
        protocol.registerOutgoing(State.PLAY, 9, 53, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.NBT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final CompoundTag tag = (CompoundTag)packetWrapper.get(Type.NBT, 0);
                        if (tag != null && tag.contains("SpawnData")) {
                            final String entity = (String)((CompoundTag)tag.get("SpawnData")).get("id").getValue();
                            tag.remove("SpawnData");
                            tag.put((Tag)new StringTag("entityId", entity));
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 10, 36, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.POSITION);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.UNSIGNED_BYTE);
                this.map((Type)Type.VAR_INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        int block = (int)packetWrapper.get((Type)Type.VAR_INT, 0);
                        if (block >= 219 && block <= 234) {
                            packetWrapper.set((Type)Type.VAR_INT, 0, (Object)(block = 130));
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 11, 35, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.POSITION);
                this.map((Type)Type.VAR_INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int combined = (int)packetWrapper.get((Type)Type.VAR_INT, 0);
                        BlockState state = BlockState.rawToState(combined);
                        state = ReplacementRegistry1_8to1_9.replace(state);
                        packetWrapper.set((Type)Type.VAR_INT, 0, (Object)BlockState.stateToRaw(state));
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 13, 65);
        protocol.registerOutgoing(State.PLAY, 16, 34, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.BLOCK_CHANGE_RECORD_ARRAY);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        for (final BlockChangeRecord record : (BlockChangeRecord[])packetWrapper.get(Type.BLOCK_CHANGE_RECORD_ARRAY, 0)) {
                            BlockState state = BlockState.rawToState(record.getBlockId());
                            state = ReplacementRegistry1_8to1_9.replace(state);
                            record.setBlockId(BlockState.stateToRaw(state));
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 25, 41, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.STRING);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        String name = (String)packetWrapper.get(Type.STRING, 0);
                        name = SoundRemapper.getOldName(name);
                        if (name == null) {
                            packetWrapper.cancel();
                        }
                        else {
                            packetWrapper.set(Type.STRING, 0, (Object)name);
                        }
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.read((Type)Type.VAR_INT);
                    }
                });
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map((Type)Type.FLOAT);
                this.map(Type.UNSIGNED_BYTE);
            }
        });
        protocol.registerOutgoing(State.PLAY, 28, 39, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int count = (int)packetWrapper.read(Type.INT);
                        packetWrapper.write(Type.INT, (Object)count);
                        for (int i = 0; i < count; ++i) {
                            packetWrapper.passthrough(Type.UNSIGNED_BYTE);
                            packetWrapper.passthrough(Type.UNSIGNED_BYTE);
                            packetWrapper.passthrough(Type.UNSIGNED_BYTE);
                        }
                    }
                });
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
            }
        });
        protocol.registerOutgoing(State.PLAY, 29, 33, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int chunkX = (int)packetWrapper.read(Type.INT);
                        final int chunkZ = (int)packetWrapper.read(Type.INT);
                        final ClientWorld world = (ClientWorld)packetWrapper.user().get((Class)ClientWorld.class);
                        packetWrapper.write((Type)new Chunk1_8Type(world), (Object)new Chunk1_8(chunkX, chunkZ));
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 32, 33, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        // 
                        // This method could not be decompiled.
                        // 
                        // Original Bytecode:
                        // 
                        //     1: invokevirtual   us/myles/ViaVersion/api/PacketWrapper.user:()Lus/myles/ViaVersion/api/data/UserConnection;
                        //     4: ldc             Lus/myles/ViaVersion/protocols/protocol1_9_3to1_9_1_2/storage/ClientWorld;.class
                        //     6: invokevirtual   us/myles/ViaVersion/api/data/UserConnection.get:(Ljava/lang/Class;)Lus/myles/ViaVersion/api/data/StoredObject;
                        //     9: checkcast       Lus/myles/ViaVersion/protocols/protocol1_9_3to1_9_1_2/storage/ClientWorld;
                        //    12: astore_2        /* world */
                        //    13: aload_1         /* packetWrapper */
                        //    14: new             Lus/myles/ViaVersion/protocols/protocol1_9_3to1_9_1_2/types/Chunk1_9_1_2Type;
                        //    17: dup            
                        //    18: aload_2         /* world */
                        //    19: invokespecial   us/myles/ViaVersion/protocols/protocol1_9_3to1_9_1_2/types/Chunk1_9_1_2Type.<init>:(Lus/myles/ViaVersion/protocols/protocol1_9_3to1_9_1_2/storage/ClientWorld;)V
                        //    22: invokevirtual   us/myles/ViaVersion/api/PacketWrapper.read:(Lus/myles/ViaVersion/api/type/Type;)Ljava/lang/Object;
                        //    25: checkcast       Lus/myles/ViaVersion/api/minecraft/chunks/Chunk;
                        //    28: astore_3        /* chunk */
                        //    29: aload_3         /* chunk */
                        //    30: invokeinterface us/myles/ViaVersion/api/minecraft/chunks/Chunk.getSections:()[Lus/myles/ViaVersion/api/minecraft/chunks/ChunkSection;
                        //    35: astore          4
                        //    37: aload           4
                        //    39: arraylength    
                        //    40: istore          5
                        //    42: iconst_0       
                        //    43: istore          6
                        //    45: iload           6
                        //    47: iload           5
                        //    49: if_icmpge       127
                        //    52: aload           4
                        //    54: iload           6
                        //    56: aaload         
                        //    57: astore          section
                        //    59: aload           section
                        //    61: ifnonnull       67
                        //    64: goto            121
                        //    67: iconst_0       
                        //    68: istore          i
                        //    70: iload           i
                        //    72: aload           section
                        //    74: invokevirtual   us/myles/ViaVersion/api/minecraft/chunks/ChunkSection.getPaletteSize:()I
                        //    77: if_icmpge       121
                        //    80: aload           section
                        //    82: iload           i
                        //    84: invokevirtual   us/myles/ViaVersion/api/minecraft/chunks/ChunkSection.getPaletteEntry:(I)I
                        //    87: istore          block
                        //    89: iload           block
                        //    91: invokestatic    de/gerrygames/viarewind/storage/BlockState.rawToState:(I)Lde/gerrygames/viarewind/storage/BlockState;
                        //    94: astore          state
                        //    96: aload           state
                        //    98: invokestatic    de/gerrygames/viarewind/protocol/protocol1_8to1_9/items/ReplacementRegistry1_8to1_9.replace:(Lde/gerrygames/viarewind/storage/BlockState;)Lde/gerrygames/viarewind/storage/BlockState;
                        //   101: astore          state
                        //   103: aload           section
                        //   105: iload           i
                        //   107: aload           state
                        //   109: invokestatic    de/gerrygames/viarewind/storage/BlockState.stateToRaw:(Lde/gerrygames/viarewind/storage/BlockState;)I
                        //   112: invokevirtual   us/myles/ViaVersion/api/minecraft/chunks/ChunkSection.setPaletteEntry:(II)V
                        //   115: iinc            i, 1
                        //   118: goto            70
                        //   121: iinc            6, 1
                        //   124: goto            45
                        //   127: aload_3         /* chunk */
                        //   128: invokeinterface us/myles/ViaVersion/api/minecraft/chunks/Chunk.isFullChunk:()Z
                        //   133: ifeq            241
                        //   136: aload_3         /* chunk */
                        //   137: invokeinterface us/myles/ViaVersion/api/minecraft/chunks/Chunk.getBitmask:()I
                        //   142: ifne            241
                        //   145: aload_2         /* world */
                        //   146: invokevirtual   us/myles/ViaVersion/protocols/protocol1_9_3to1_9_1_2/storage/ClientWorld.getEnvironment:()Lus/myles/ViaVersion/api/minecraft/Environment;
                        //   149: getstatic       us/myles/ViaVersion/api/minecraft/Environment.NORMAL:Lus/myles/ViaVersion/api/minecraft/Environment;
                        //   152: if_acmpne       159
                        //   155: iconst_1       
                        //   156: goto            160
                        //   159: iconst_0       
                        //   160: istore          skylight
                        //   162: bipush          16
                        //   164: anewarray       Lus/myles/ViaVersion/api/minecraft/chunks/ChunkSection;
                        //   167: astore          sections
                        //   169: new             Lus/myles/ViaVersion/api/minecraft/chunks/ChunkSection;
                        //   172: dup            
                        //   173: invokespecial   us/myles/ViaVersion/api/minecraft/chunks/ChunkSection.<init>:()V
                        //   176: astore          section
                        //   178: aload           sections
                        //   180: iconst_0       
                        //   181: aload           section
                        //   183: aastore        
                        //   184: aload           section
                        //   186: iconst_0       
                        //   187: invokevirtual   us/myles/ViaVersion/api/minecraft/chunks/ChunkSection.addPaletteEntry:(I)V
                        //   190: iload           skylight
                        //   192: ifeq            205
                        //   195: aload           section
                        //   197: sipush          2048
                        //   200: newarray        B
                        //   202: invokevirtual   us/myles/ViaVersion/api/minecraft/chunks/ChunkSection.setSkyLight:([B)V
                        //   205: new             Lus/myles/ViaVersion/api/minecraft/chunks/Chunk1_8;
                        //   208: dup            
                        //   209: aload_3         /* chunk */
                        //   210: invokeinterface us/myles/ViaVersion/api/minecraft/chunks/Chunk.getX:()I
                        //   215: aload_3         /* chunk */
                        //   216: invokeinterface us/myles/ViaVersion/api/minecraft/chunks/Chunk.getZ:()I
                        //   221: iconst_1       
                        //   222: iconst_1       
                        //   223: aload           sections
                        //   225: aload_3         /* chunk */
                        //   226: invokeinterface us/myles/ViaVersion/api/minecraft/chunks/Chunk.getBiomeData:()[I
                        //   231: aload_3         /* chunk */
                        //   232: invokeinterface us/myles/ViaVersion/api/minecraft/chunks/Chunk.getBlockEntities:()Ljava/util/List;
                        //   237: invokespecial   us/myles/ViaVersion/api/minecraft/chunks/Chunk1_8.<init>:(IIZI[Lus/myles/ViaVersion/api/minecraft/chunks/ChunkSection;[ILjava/util/List;)V
                        //   240: astore_3        /* chunk */
                        //   241: aload_1         /* packetWrapper */
                        //   242: new             Lde/gerrygames/viarewind/protocol/protocol1_8to1_9/types/Chunk1_8Type;
                        //   245: dup            
                        //   246: aload_2         /* world */
                        //   247: invokespecial   de/gerrygames/viarewind/protocol/protocol1_8to1_9/types/Chunk1_8Type.<init>:(Lus/myles/ViaVersion/protocols/protocol1_9_3to1_9_1_2/storage/ClientWorld;)V
                        //   250: aload_3         /* chunk */
                        //   251: invokevirtual   us/myles/ViaVersion/api/PacketWrapper.write:(Lus/myles/ViaVersion/api/type/Type;Ljava/lang/Object;)V
                        //   254: aload_1         /* packetWrapper */
                        //   255: invokevirtual   us/myles/ViaVersion/api/PacketWrapper.user:()Lus/myles/ViaVersion/api/data/UserConnection;
                        //   258: astore          user
                        //   260: aload_3         /* chunk */
                        //   261: invokeinterface us/myles/ViaVersion/api/minecraft/chunks/Chunk.getBlockEntities:()Ljava/util/List;
                        //   266: aload           user
                        //   268: invokedynamic   BootstrapMethod #0, accept:(Lus/myles/ViaVersion/api/data/UserConnection;)Ljava/util/function/Consumer;
                        //   273: invokeinterface java/util/List.forEach:(Ljava/util/function/Consumer;)V
                        //   278: return         
                        //    Exceptions:
                        //  throws java.lang.Exception
                        //    StackMapTable: 00 09 FF 00 2D 00 07 07 00 02 07 00 21 07 00 27 07 00 38 07 00 3E 01 01 00 00 FC 00 15 07 00 40 FC 00 02 01 F9 00 32 F8 00 05 1F 40 01 FE 00 2C 01 07 00 3E 07 00 40 F8 00 23
                        // 
                        // The error that occurred was:
                        // 
                        // java.lang.NullPointerException
                        //     at com.strobel.decompiler.languages.java.ast.NameVariables.generateNameForVariable(NameVariables.java:264)
                        //     at com.strobel.decompiler.languages.java.ast.NameVariables.assignNamesToVariables(NameVariables.java:198)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:276)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1164)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1164)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
                        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
                        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
                        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
                        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
                        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
                        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
                        // 
                        throw new IllegalStateException("An error occurred while decompiling this method.");
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 33, 40, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.POSITION);
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        int id = (int)packetWrapper.get(Type.INT, 0);
                        id = Effect.getOldId(id);
                        if (id == -1) {
                            packetWrapper.cancel();
                            return;
                        }
                        packetWrapper.set(Type.INT, 0, (Object)id);
                        if (id == 2001) {
                            BlockState state = BlockState.rawToState((int)packetWrapper.get(Type.INT, 1));
                            state = ReplacementRegistry1_8to1_9.replace(state);
                            packetWrapper.set(Type.INT, 1, (Object)BlockState.stateToRaw(state));
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 34, 42, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int type = (int)packetWrapper.get(Type.INT, 0);
                        if (type > 41 && !ViaRewind.getConfig().isReplaceParticles()) {
                            packetWrapper.cancel();
                            return;
                        }
                        if (type == 42) {
                            packetWrapper.set(Type.INT, 0, (Object)24);
                        }
                        else if (type == 43) {
                            packetWrapper.set(Type.INT, 0, (Object)3);
                        }
                        else if (type == 44) {
                            packetWrapper.set(Type.INT, 0, (Object)34);
                        }
                        else if (type == 45) {
                            packetWrapper.set(Type.INT, 0, (Object)1);
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 36, 52, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.BYTE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.read(Type.BOOLEAN);
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 44, 66);
        protocol.registerOutgoing(State.PLAY, 53, 68);
        protocol.registerOutgoing(State.PLAY, 68, 3);
        protocol.registerOutgoing(State.PLAY, 70, 51);
        protocol.registerOutgoing(State.PLAY, 71, 41, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final int soundId = (int)packetWrapper.read((Type)Type.VAR_INT);
                        final String sound = SoundRemapper.oldNameFromId(soundId);
                        if (sound == null) {
                            packetWrapper.cancel();
                        }
                        else {
                            packetWrapper.write(Type.STRING, (Object)sound);
                        }
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.read((Type)Type.VAR_INT);
                    }
                });
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map((Type)Type.FLOAT);
                this.map(Type.UNSIGNED_BYTE);
            }
        });
    }
}
