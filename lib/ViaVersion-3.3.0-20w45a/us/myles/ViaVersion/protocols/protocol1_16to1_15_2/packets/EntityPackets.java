// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_16to1_15_2.packets;

import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ListTag;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.storage.InventoryTracker1_16;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.ServerboundPackets1_16;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.data.MappingData;
import us.myles.ViaVersion.api.type.types.version.Types1_14;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.PacketWrapper;
import java.util.UUID;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.entities.Entity1_16Types;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.storage.EntityTracker1_16;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.metadata.MetadataRewriter1_16To1_15_2;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.Protocol1_16To1_15_2;
import us.myles.viaversion.libs.opennbt.tag.builtin.LongTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.FloatTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ByteTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.remapper.PacketHandler;

public class EntityPackets
{
    private static final PacketHandler DIMENSION_HANDLER;
    public static final CompoundTag DIMENSIONS_TAG;
    private static final String[] WORLD_NAMES;
    
    private static CompoundTag createOverworldEntry() {
        final CompoundTag tag = new CompoundTag("");
        tag.put(new StringTag("name", "minecraft:overworld"));
        tag.put(new ByteTag("has_ceiling", (byte)0));
        addSharedOverwaldEntries(tag);
        return tag;
    }
    
    private static CompoundTag createOverworldCavesEntry() {
        final CompoundTag tag = new CompoundTag("");
        tag.put(new StringTag("name", "minecraft:overworld_caves"));
        tag.put(new ByteTag("has_ceiling", (byte)1));
        addSharedOverwaldEntries(tag);
        return tag;
    }
    
    private static void addSharedOverwaldEntries(final CompoundTag tag) {
        tag.put(new ByteTag("piglin_safe", (byte)0));
        tag.put(new ByteTag("natural", (byte)1));
        tag.put(new FloatTag("ambient_light", 0.0f));
        tag.put(new StringTag("infiniburn", "minecraft:infiniburn_overworld"));
        tag.put(new ByteTag("respawn_anchor_works", (byte)0));
        tag.put(new ByteTag("has_skylight", (byte)1));
        tag.put(new ByteTag("bed_works", (byte)1));
        tag.put(new ByteTag("has_raids", (byte)1));
        tag.put(new IntTag("logical_height", 256));
        tag.put(new ByteTag("shrunk", (byte)0));
        tag.put(new ByteTag("ultrawarm", (byte)0));
    }
    
    private static CompoundTag createNetherEntry() {
        final CompoundTag tag = new CompoundTag("");
        tag.put(new ByteTag("piglin_safe", (byte)1));
        tag.put(new ByteTag("natural", (byte)0));
        tag.put(new FloatTag("ambient_light", 0.1f));
        tag.put(new StringTag("infiniburn", "minecraft:infiniburn_nether"));
        tag.put(new ByteTag("respawn_anchor_works", (byte)1));
        tag.put(new ByteTag("has_skylight", (byte)0));
        tag.put(new ByteTag("bed_works", (byte)0));
        tag.put(new LongTag("fixed_time", 18000L));
        tag.put(new ByteTag("has_raids", (byte)0));
        tag.put(new StringTag("name", "minecraft:the_nether"));
        tag.put(new IntTag("logical_height", 128));
        tag.put(new ByteTag("shrunk", (byte)1));
        tag.put(new ByteTag("ultrawarm", (byte)1));
        tag.put(new ByteTag("has_ceiling", (byte)1));
        return tag;
    }
    
    private static CompoundTag createEndEntry() {
        final CompoundTag tag = new CompoundTag("");
        tag.put(new ByteTag("piglin_safe", (byte)0));
        tag.put(new ByteTag("natural", (byte)0));
        tag.put(new FloatTag("ambient_light", 0.0f));
        tag.put(new StringTag("infiniburn", "minecraft:infiniburn_end"));
        tag.put(new ByteTag("respawn_anchor_works", (byte)0));
        tag.put(new ByteTag("has_skylight", (byte)0));
        tag.put(new ByteTag("bed_works", (byte)0));
        tag.put(new LongTag("fixed_time", 6000L));
        tag.put(new ByteTag("has_raids", (byte)1));
        tag.put(new StringTag("name", "minecraft:the_end"));
        tag.put(new IntTag("logical_height", 256));
        tag.put(new ByteTag("shrunk", (byte)0));
        tag.put(new ByteTag("ultrawarm", (byte)0));
        tag.put(new ByteTag("has_ceiling", (byte)0));
        return tag;
    }
    
    public static void register(final Protocol1_16To1_15_2 protocol) {
        final MetadataRewriter1_16To1_15_2 metadataRewriter = protocol.get(MetadataRewriter1_16To1_15_2.class);
        ((Protocol<ClientboundPackets1_15, ClientboundPackets1_16, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_15.SPAWN_GLOBAL_ENTITY, ClientboundPackets1_16.SPAWN_ENTITY, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int entityId;
                this.handler(wrapper -> {
                    entityId = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                    wrapper.user().get(EntityTracker1_16.class).addEntity(entityId, Entity1_16Types.EntityType.LIGHTNING_BOLT);
                    wrapper.write(Type.UUID, UUID.randomUUID());
                    wrapper.write(Type.VAR_INT, Entity1_16Types.EntityType.LIGHTNING_BOLT.getId());
                    wrapper.read(Type.BYTE);
                    wrapper.passthrough(Type.DOUBLE);
                    wrapper.passthrough(Type.DOUBLE);
                    wrapper.passthrough(Type.DOUBLE);
                    wrapper.write(Type.BYTE, (Byte)0);
                    wrapper.write(Type.BYTE, (Byte)0);
                    wrapper.write(Type.INT, 0);
                    wrapper.write(Type.SHORT, (Short)0);
                    wrapper.write(Type.SHORT, (Short)0);
                    wrapper.write(Type.SHORT, (Short)0);
                });
            }
        });
        metadataRewriter.registerSpawnTrackerWithData(ClientboundPackets1_15.SPAWN_ENTITY, Entity1_16Types.EntityType.FALLING_BLOCK);
        metadataRewriter.registerTracker(ClientboundPackets1_15.SPAWN_MOB);
        metadataRewriter.registerTracker(ClientboundPackets1_15.SPAWN_PLAYER, Entity1_16Types.EntityType.PLAYER);
        metadataRewriter.registerMetadataRewriter(ClientboundPackets1_15.ENTITY_METADATA, Types1_14.METADATA_LIST);
        metadataRewriter.registerEntityDestroy(ClientboundPackets1_15.DESTROY_ENTITIES);
        ((Protocol<ClientboundPackets1_15, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_15.RESPAWN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(EntityPackets.DIMENSION_HANDLER);
                this.map(Type.LONG);
                this.map(Type.UNSIGNED_BYTE);
                final String levelType;
                this.handler(wrapper -> {
                    wrapper.write(Type.BYTE, (Byte)(-1));
                    levelType = wrapper.read(Type.STRING);
                    wrapper.write(Type.BOOLEAN, false);
                    wrapper.write(Type.BOOLEAN, levelType.equals("flat"));
                    wrapper.write(Type.BOOLEAN, true);
                });
            }
        });
        ((Protocol<ClientboundPackets1_15, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_15.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.handler(wrapper -> {
                    wrapper.write(Type.BYTE, (Byte)(-1));
                    wrapper.write(Type.STRING_ARRAY, EntityPackets.WORLD_NAMES);
                    wrapper.write(Type.NBT, EntityPackets.DIMENSIONS_TAG);
                    return;
                });
                this.handler(EntityPackets.DIMENSION_HANDLER);
                this.map(Type.LONG);
                this.map(Type.UNSIGNED_BYTE);
                final String type;
                this.handler(wrapper -> {
                    wrapper.user().get(EntityTracker1_16.class).addEntity(wrapper.get(Type.INT, 0), Entity1_16Types.EntityType.PLAYER);
                    type = wrapper.read(Type.STRING);
                    wrapper.passthrough((Type<Object>)Type.VAR_INT);
                    wrapper.passthrough(Type.BOOLEAN);
                    wrapper.passthrough(Type.BOOLEAN);
                    wrapper.write(Type.BOOLEAN, false);
                    wrapper.write(Type.BOOLEAN, type.equals("flat"));
                });
            }
        });
        ((Protocol<ClientboundPackets1_15, C2, S1, S2>)protocol).registerOutgoing(ClientboundPackets1_15.ENTITY_PROPERTIES, new PacketRemapper() {
            @Override
            public void registerMap() {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     1: aload_0         /* this */
                //     2: getfield        us/myles/ViaVersion/protocols/protocol1_16to1_15_2/packets/EntityPackets$4.val$protocol:Lus/myles/ViaVersion/protocols/protocol1_16to1_15_2/Protocol1_16To1_15_2;
                //     5: invokedynamic   BootstrapMethod #0, handle:(Lus/myles/ViaVersion/protocols/protocol1_16to1_15_2/Protocol1_16To1_15_2;)Lus/myles/ViaVersion/api/remapper/PacketHandler;
                //    10: invokevirtual   us/myles/ViaVersion/protocols/protocol1_16to1_15_2/packets/EntityPackets$4.handler:(Lus/myles/ViaVersion/api/remapper/PacketHandler;)V
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
        ((Protocol<C1, C2, S1, ServerboundPackets1_16>)protocol).registerIncoming(ServerboundPackets1_16.ANIMATION, new PacketRemapper() {
            @Override
            public void registerMap() {
                final InventoryTracker1_16 inventoryTracker;
                this.handler(wrapper -> {
                    inventoryTracker = wrapper.user().get(InventoryTracker1_16.class);
                    if (inventoryTracker.getInventory() != -1) {
                        wrapper.cancel();
                    }
                });
            }
        });
    }
    
    static {
        final int dimension;
        String dimensionName = null;
        DIMENSION_HANDLER = (wrapper -> {
            dimension = wrapper.read(Type.INT);
            switch (dimension) {
                case -1: {
                    dimensionName = "minecraft:the_nether";
                    break;
                }
                case 0: {
                    dimensionName = "minecraft:overworld";
                    break;
                }
                case 1: {
                    dimensionName = "minecraft:the_end";
                    break;
                }
                default: {
                    Via.getPlatform().getLogger().warning("Invalid dimension id: " + dimension);
                    dimensionName = "minecraft:overworld";
                    break;
                }
            }
            wrapper.write(Type.STRING, dimensionName);
            wrapper.write(Type.STRING, dimensionName);
            return;
        });
        DIMENSIONS_TAG = new CompoundTag("");
        WORLD_NAMES = new String[] { "minecraft:overworld", "minecraft:the_nether", "minecraft:the_end" };
        final ListTag list = new ListTag("dimension", CompoundTag.class);
        list.add(createOverworldEntry());
        list.add(createOverworldCavesEntry());
        list.add(createNetherEntry());
        list.add(createEndEntry());
        EntityPackets.DIMENSIONS_TAG.put(list);
    }
}
