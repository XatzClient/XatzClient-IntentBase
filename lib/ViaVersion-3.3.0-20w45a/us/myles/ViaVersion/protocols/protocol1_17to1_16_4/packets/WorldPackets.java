// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_17to1_16_4.packets;

import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.protocols.protocol1_17to1_16_4.storage.BiomeStorage;
import us.myles.ViaVersion.protocols.protocol1_17to1_16_4.types.Chunk1_17Type;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.types.Chunk1_16_2Type;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.rewriters.BlockRewriter;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_17to1_16_4.Protocol1_17To1_16_4;

public class WorldPackets
{
    public static void register(final Protocol1_17To1_16_4 protocol) {
        final BlockRewriter blockRewriter = new BlockRewriter(protocol, Type.POSITION1_14);
        blockRewriter.registerBlockAction(ClientboundPackets1_16_2.BLOCK_ACTION);
        blockRewriter.registerBlockChange(ClientboundPackets1_16_2.BLOCK_CHANGE);
        blockRewriter.registerVarLongMultiBlockChange(ClientboundPackets1_16_2.MULTI_BLOCK_CHANGE);
        blockRewriter.registerAcknowledgePlayerDigging(ClientboundPackets1_16_2.ACKNOWLEDGE_PLAYER_DIGGING);
        ((Protocol<ClientboundPackets1_16_2, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_16_2.UPDATE_LIGHT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    wrapper.passthrough((Type<Object>)Type.VAR_INT);
                    wrapper.passthrough((Type<Object>)Type.VAR_INT);
                    wrapper.passthrough(Type.BOOLEAN);
                    wrapper.write(Type.VAR_LONG, (long)wrapper.read((Type<Integer>)Type.VAR_INT));
                    wrapper.write(Type.VAR_LONG, (long)wrapper.read((Type<Integer>)Type.VAR_INT));
                    wrapper.write(Type.VAR_LONG, (long)wrapper.read((Type<Integer>)Type.VAR_INT));
                    wrapper.write(Type.VAR_LONG, (long)wrapper.read((Type<Integer>)Type.VAR_INT));
                });
            }
        });
        ((Protocol<ClientboundPackets1_16_2, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_16_2.CHUNK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     1: aload_0         /* this */
                //     2: getfield        us/myles/ViaVersion/protocols/protocol1_17to1_16_4/packets/WorldPackets$2.val$protocol:Lus/myles/ViaVersion/protocols/protocol1_17to1_16_4/Protocol1_17To1_16_4;
                //     5: invokedynamic   BootstrapMethod #0, handle:(Lus/myles/ViaVersion/protocols/protocol1_17to1_16_4/Protocol1_17To1_16_4;)Lus/myles/ViaVersion/api/remapper/PacketHandler;
                //    10: invokevirtual   us/myles/ViaVersion/protocols/protocol1_17to1_16_4/packets/WorldPackets$2.handler:(Lus/myles/ViaVersion/api/remapper/PacketHandler;)V
                //    13: return         
                // 
                // The error that occurred was:
                // 
                // java.lang.IllegalStateException: Could not infer any expression.
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:374)
                //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
                //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
                //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
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
                //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:141)
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
        });
        ((Protocol<ClientboundPackets1_16_2, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_16_2.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.BYTE);
                this.map(Type.STRING_ARRAY);
                this.map(Type.NBT);
                this.map(Type.NBT);
                final String world;
                this.handler(wrapper -> {
                    world = wrapper.passthrough(Type.STRING);
                    wrapper.user().get(BiomeStorage.class).setWorld(world);
                });
            }
        });
        ((Protocol<ClientboundPackets1_16_2, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_16_2.RESPAWN, new PacketRemapper() {
            @Override
            public void registerMap() {
                final String world;
                final BiomeStorage biomeStorage;
                this.handler(wrapper -> {
                    wrapper.passthrough(Type.NBT);
                    world = wrapper.passthrough(Type.STRING);
                    biomeStorage = wrapper.user().get(BiomeStorage.class);
                    if (!world.equals(biomeStorage.getWorld())) {
                        biomeStorage.clearBiomes();
                    }
                    biomeStorage.setWorld(world);
                });
            }
        });
        ((Protocol<ClientboundPackets1_16_2, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_16_2.UNLOAD_CHUNK, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int x;
                final int z;
                this.handler(wrapper -> {
                    x = wrapper.passthrough(Type.INT);
                    z = wrapper.passthrough(Type.INT);
                    wrapper.user().get(BiomeStorage.class).clearBiomes(x, z);
                });
            }
        });
        blockRewriter.registerEffect(ClientboundPackets1_16_2.EFFECT, 1010, 2001);
    }
}
