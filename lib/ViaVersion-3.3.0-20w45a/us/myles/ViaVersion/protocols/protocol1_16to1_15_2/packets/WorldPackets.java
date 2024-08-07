// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_16to1_15_2.packets;

import us.myles.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
import us.myles.ViaVersion.api.type.types.UUIDIntArrayType;
import java.util.UUID;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.ViaVersion.api.minecraft.Position;
import java.util.Iterator;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.util.CompactArrayUtil;
import us.myles.viaversion.libs.opennbt.tag.builtin.LongArrayTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.types.Chunk1_16Type;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.types.Chunk1_15Type;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.rewriters.BlockRewriter;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.Protocol1_16To1_15_2;

public class WorldPackets
{
    public static void register(final Protocol1_16To1_15_2 protocol) {
        final BlockRewriter blockRewriter = new BlockRewriter(protocol, Type.POSITION1_14);
        blockRewriter.registerBlockAction(ClientboundPackets1_15.BLOCK_ACTION);
        blockRewriter.registerBlockChange(ClientboundPackets1_15.BLOCK_CHANGE);
        blockRewriter.registerMultiBlockChange(ClientboundPackets1_15.MULTI_BLOCK_CHANGE);
        blockRewriter.registerAcknowledgePlayerDigging(ClientboundPackets1_15.ACKNOWLEDGE_PLAYER_DIGGING);
        ((Protocol<ClientboundPackets1_15, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_15.UPDATE_LIGHT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.handler(wrapper -> wrapper.write(Type.BOOLEAN, true));
            }
        });
        ((Protocol<ClientboundPackets1_15, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_15.CHUNK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     1: aload_0         /* this */
                //     2: getfield        us/myles/ViaVersion/protocols/protocol1_16to1_15_2/packets/WorldPackets$2.val$protocol:Lus/myles/ViaVersion/protocols/protocol1_16to1_15_2/Protocol1_16To1_15_2;
                //     5: invokedynamic   BootstrapMethod #0, handle:(Lus/myles/ViaVersion/protocols/protocol1_16to1_15_2/Protocol1_16To1_15_2;)Lus/myles/ViaVersion/api/remapper/PacketHandler;
                //    10: invokevirtual   us/myles/ViaVersion/protocols/protocol1_16to1_15_2/packets/WorldPackets$2.handler:(Lus/myles/ViaVersion/api/remapper/PacketHandler;)V
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
        ((Protocol<ClientboundPackets1_15, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_15.BLOCK_ENTITY_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                final Position position;
                final short action;
                final CompoundTag tag;
                this.handler(wrapper -> {
                    position = wrapper.passthrough(Type.POSITION1_14);
                    action = wrapper.passthrough(Type.UNSIGNED_BYTE);
                    tag = wrapper.passthrough(Type.NBT);
                    handleBlockEntity(tag);
                });
            }
        });
        blockRewriter.registerEffect(ClientboundPackets1_15.EFFECT, 1010, 2001);
    }
    
    private static void handleBlockEntity(final CompoundTag compoundTag) {
        final StringTag idTag = compoundTag.get("id");
        if (idTag == null) {
            return;
        }
        final String id = idTag.getValue();
        if (id.equals("minecraft:conduit")) {
            final Tag targetUuidTag = compoundTag.remove("target_uuid");
            if (!(targetUuidTag instanceof StringTag)) {
                return;
            }
            final UUID targetUuid = UUID.fromString((String)targetUuidTag.getValue());
            compoundTag.put(new IntArrayTag("Target", UUIDIntArrayType.uuidToIntArray(targetUuid)));
        }
        else if (id.equals("minecraft:skull") && compoundTag.get("Owner") instanceof CompoundTag) {
            final CompoundTag ownerTag = compoundTag.remove("Owner");
            final StringTag ownerUuidTag = ownerTag.remove("Id");
            if (ownerUuidTag != null) {
                final UUID ownerUuid = UUID.fromString(ownerUuidTag.getValue());
                ownerTag.put(new IntArrayTag("Id", UUIDIntArrayType.uuidToIntArray(ownerUuid)));
            }
            final CompoundTag skullOwnerTag = new CompoundTag("SkullOwner");
            for (final Tag tag : ownerTag) {
                skullOwnerTag.put(tag);
            }
            compoundTag.put(skullOwnerTag);
        }
    }
}
