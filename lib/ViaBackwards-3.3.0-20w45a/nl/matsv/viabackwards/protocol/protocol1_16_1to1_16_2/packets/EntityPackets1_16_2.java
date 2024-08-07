// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_16_1to1_16_2.packets;

import us.myles.ViaVersion.api.remapper.PacketHandler;
import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import nl.matsv.viabackwards.api.entities.meta.MetaHandlerEvent;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.entities.Entity1_16Types;
import us.myles.ViaVersion.api.type.types.Particle;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_14;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.packets.EntityPackets;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.List;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.type.types.version.Types1_14;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.entities.Entity1_16_2Types;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import com.google.common.collect.Sets;
import java.util.Set;
import nl.matsv.viabackwards.protocol.protocol1_16_1to1_16_2.Protocol1_16_1To1_16_2;
import nl.matsv.viabackwards.api.rewriters.EntityRewriter;

public class EntityPackets1_16_2 extends EntityRewriter<Protocol1_16_1To1_16_2>
{
    private final Set<String> oldDimensions;
    
    public EntityPackets1_16_2(final Protocol1_16_1To1_16_2 protocol) {
        super(protocol);
        this.oldDimensions = (Set<String>)Sets.newHashSet((Object[])new String[] { "minecraft:overworld", "minecraft:the_nether", "minecraft:the_end" });
    }
    
    @Override
    protected void registerPackets() {
        this.registerSpawnTrackerWithData((ClientboundPacketType)ClientboundPackets1_16_2.SPAWN_ENTITY, (EntityType)Entity1_16_2Types.EntityType.FALLING_BLOCK);
        this.registerSpawnTracker((ClientboundPacketType)ClientboundPackets1_16_2.SPAWN_MOB);
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_16_2.SPAWN_EXPERIENCE_ORB, (EntityType)Entity1_16_2Types.EntityType.EXPERIENCE_ORB);
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_16_2.SPAWN_PAINTING, (EntityType)Entity1_16_2Types.EntityType.PAINTING);
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_16_2.SPAWN_PLAYER, (EntityType)Entity1_16_2Types.EntityType.PLAYER);
        this.registerEntityDestroy((ClientboundPacketType)ClientboundPackets1_16_2.DESTROY_ENTITIES);
        this.registerMetadataRewriter((ClientboundPacketType)ClientboundPackets1_16_2.ENTITY_METADATA, (Type<List<Metadata>>)Types1_14.METADATA_LIST);
        ((Protocol1_16_1To1_16_2)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16_2.JOIN_GAME, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.handler(wrapper -> {
                    final boolean hardcore = (boolean)wrapper.read(Type.BOOLEAN);
                    short gamemode = (short)wrapper.read(Type.UNSIGNED_BYTE);
                    if (hardcore) {
                        gamemode |= 0x8;
                    }
                    wrapper.write(Type.UNSIGNED_BYTE, (Object)gamemode);
                });
                this.map(Type.BYTE);
                this.map(Type.STRING_ARRAY);
                this.handler(wrapper -> {
                    wrapper.read(Type.NBT);
                    wrapper.write(Type.NBT, (Object)EntityPackets.DIMENSIONS_TAG);
                    final CompoundTag dimensionData = (CompoundTag)wrapper.read(Type.NBT);
                    wrapper.write(Type.STRING, (Object)EntityPackets1_16_2.this.getDimensionFromData(dimensionData));
                });
                this.map(Type.STRING);
                this.map(Type.LONG);
                this.handler(wrapper -> {
                    final int maxPlayers = (int)wrapper.read((Type)Type.VAR_INT);
                    wrapper.write(Type.UNSIGNED_BYTE, (Object)(short)Math.max(maxPlayers, 255));
                });
                this.handler(EntityRewriterBase.this.getTrackerHandler((EntityType)Entity1_16_2Types.EntityType.PLAYER, Type.INT));
            }
        });
        ((Protocol1_16_1To1_16_2)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16_2.RESPAWN, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    final CompoundTag dimensionData = (CompoundTag)wrapper.read(Type.NBT);
                    wrapper.write(Type.STRING, (Object)EntityPackets1_16_2.this.getDimensionFromData(dimensionData));
                });
            }
        });
    }
    
    private String getDimensionFromData(final CompoundTag dimensionData) {
        final StringTag effectsLocation = (StringTag)dimensionData.get("effects");
        return (effectsLocation != null && this.oldDimensions.contains(effectsLocation.getValue())) ? effectsLocation.getValue() : "minecraft:overworld";
    }
    
    @Override
    protected void registerRewrites() {
        final Metadata meta2;
        final MetaType type;
        JsonElement text;
        this.registerMetaHandler().handle(e -> {
            meta2 = e.getData();
            type = meta2.getMetaType();
            if (type == MetaType1_14.Slot) {
                meta2.setValue((Object)((Protocol1_16_1To1_16_2)this.protocol).getBlockItemPackets().handleItemToClient((Item)meta2.getValue()));
            }
            else if (type == MetaType1_14.BlockID) {
                meta2.setValue((Object)((Protocol1_16_1To1_16_2)this.protocol).getMappingData().getNewBlockStateId((int)meta2.getValue()));
            }
            else if (type == MetaType1_14.OptChat) {
                text = (JsonElement)meta2.getCastedValue();
                if (text != null) {
                    ((Protocol1_16_1To1_16_2)this.protocol).getTranslatableRewriter().processText(text);
                }
            }
            else if (type == MetaType1_14.PARTICLE) {
                this.rewriteParticle((Particle)meta2.getValue());
            }
            return meta2;
        });
        this.mapTypes((EntityType[])Entity1_16_2Types.EntityType.values(), Entity1_16Types.EntityType.class);
        this.mapEntity((EntityType)Entity1_16_2Types.EntityType.PIGLIN_BRUTE, (EntityType)Entity1_16_2Types.EntityType.PIGLIN).jsonName("Piglin Brute");
        this.registerMetaHandler().filter((EntityType)Entity1_16_2Types.EntityType.ABSTRACT_PIGLIN, true).handle(meta -> {
            if (meta.getIndex() == 15) {
                meta.getData().setId(16);
            }
            else if (meta.getIndex() == 16) {
                meta.getData().setId(15);
            }
            return meta.getData();
        });
    }
    
    @Override
    protected EntityType getTypeFromId(final int typeId) {
        return (EntityType)Entity1_16_2Types.getTypeFromId(typeId);
    }
}
