// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_14to1_13_2.metadata;

import us.myles.ViaVersion.api.type.types.Particle;
import us.myles.ViaVersion.api.minecraft.VillagerData;
import us.myles.ViaVersion.api.type.Type;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.packets.InventoryPackets;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_14;
import us.myles.ViaVersion.api.data.UserConnection;
import java.util.List;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.entities.Entity1_14Types;
import us.myles.ViaVersion.api.entities.Entity1_13Types;
import us.myles.ViaVersion.api.storage.EntityTracker;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.storage.EntityTracker1_14;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.Protocol1_14To1_13_2;
import us.myles.ViaVersion.api.rewriters.MetadataRewriter;

public class MetadataRewriter1_14To1_13_2 extends MetadataRewriter
{
    public MetadataRewriter1_14To1_13_2(final Protocol1_14To1_13_2 protocol) {
        super(protocol, EntityTracker1_14.class);
        this.mapTypes(Entity1_13Types.EntityType.values(), Entity1_14Types.EntityType.class);
        this.mapType(Entity1_13Types.EntityType.OCELOT, Entity1_14Types.EntityType.CAT);
    }
    
    @Override
    protected void handleMetadata(final int entityId, final EntityType type, final Metadata metadata, final List<Metadata> metadatas, final UserConnection connection) throws Exception {
        metadata.setMetaType(MetaType1_14.byId(metadata.getMetaType().getTypeID()));
        final EntityTracker1_14 tracker = connection.get(EntityTracker1_14.class);
        if (metadata.getMetaType() == MetaType1_14.Slot) {
            InventoryPackets.toClient((Item)metadata.getValue());
        }
        else if (metadata.getMetaType() == MetaType1_14.BlockID) {
            final int data = (int)metadata.getValue();
            metadata.setValue(this.protocol.getMappingData().getNewBlockStateId(data));
        }
        if (type == null) {
            return;
        }
        if (metadata.getId() > 5) {
            metadata.setId(metadata.getId() + 1);
        }
        if (metadata.getId() == 8 && type.isOrHasParent(Entity1_14Types.EntityType.LIVINGENTITY)) {
            final float v = ((Number)metadata.getValue()).floatValue();
            if (Float.isNaN(v) && Via.getConfig().is1_14HealthNaNFix()) {
                metadata.setValue(1.0f);
            }
        }
        if (metadata.getId() > 11 && type.isOrHasParent(Entity1_14Types.EntityType.LIVINGENTITY)) {
            metadata.setId(metadata.getId() + 1);
        }
        if (type.isOrHasParent(Entity1_14Types.EntityType.ABSTRACT_INSENTIENT) && metadata.getId() == 13) {
            tracker.setInsentientData(entityId, (byte)((((Number)metadata.getValue()).byteValue() & 0xFFFFFFFB) | (tracker.getInsentientData(entityId) & 0x4)));
            metadata.setValue(tracker.getInsentientData(entityId));
        }
        if (type.isOrHasParent(Entity1_14Types.EntityType.PLAYER)) {
            if (entityId != tracker.getClientEntityId()) {
                if (metadata.getId() == 0) {
                    final byte flags = ((Number)metadata.getValue()).byteValue();
                    tracker.setEntityFlags(entityId, flags);
                }
                else if (metadata.getId() == 7) {
                    tracker.setRiptide(entityId, (((Number)metadata.getValue()).byteValue() & 0x4) != 0x0);
                }
                if (metadata.getId() == 0 || metadata.getId() == 7) {
                    metadatas.add(new Metadata(6, MetaType1_14.Pose, recalculatePlayerPose(entityId, tracker)));
                }
            }
        }
        else if (type.isOrHasParent(Entity1_14Types.EntityType.ZOMBIE)) {
            if (metadata.getId() == 16) {
                tracker.setInsentientData(entityId, (byte)((tracker.getInsentientData(entityId) & 0xFFFFFFFB) | (metadata.getValue() ? 4 : 0)));
                metadatas.remove(metadata);
                metadatas.add(new Metadata(13, MetaType1_14.Byte, tracker.getInsentientData(entityId)));
            }
            else if (metadata.getId() > 16) {
                metadata.setId(metadata.getId() - 1);
            }
        }
        if (type.isOrHasParent(Entity1_14Types.EntityType.MINECART_ABSTRACT)) {
            if (metadata.getId() == 10) {
                final int data = (int)metadata.getValue();
                metadata.setValue(this.protocol.getMappingData().getNewBlockStateId(data));
            }
        }
        else if (type.is(Entity1_14Types.EntityType.HORSE)) {
            if (metadata.getId() == 18) {
                metadatas.remove(metadata);
                final int armorType = (int)metadata.getValue();
                Item armorItem = null;
                if (armorType == 1) {
                    armorItem = new Item(this.protocol.getMappingData().getNewItemId(727), (byte)1, (short)0, null);
                }
                else if (armorType == 2) {
                    armorItem = new Item(this.protocol.getMappingData().getNewItemId(728), (byte)1, (short)0, null);
                }
                else if (armorType == 3) {
                    armorItem = new Item(this.protocol.getMappingData().getNewItemId(729), (byte)1, (short)0, null);
                }
                final PacketWrapper equipmentPacket = new PacketWrapper(70, null, connection);
                equipmentPacket.write(Type.VAR_INT, entityId);
                equipmentPacket.write(Type.VAR_INT, 4);
                equipmentPacket.write(Type.FLAT_VAR_INT_ITEM, armorItem);
                equipmentPacket.send(Protocol1_14To1_13_2.class);
            }
        }
        else if (type.is(Entity1_14Types.EntityType.VILLAGER)) {
            if (metadata.getId() == 15) {
                metadata.setValue(new VillagerData(2, getNewProfessionId((int)metadata.getValue()), 0));
                metadata.setMetaType(MetaType1_14.VillagerData);
            }
        }
        else if (type.is(Entity1_14Types.EntityType.ZOMBIE_VILLAGER)) {
            if (metadata.getId() == 18) {
                metadata.setValue(new VillagerData(2, getNewProfessionId((int)metadata.getValue()), 0));
                metadata.setMetaType(MetaType1_14.VillagerData);
            }
        }
        else if (type.isOrHasParent(Entity1_14Types.EntityType.ABSTRACT_ARROW)) {
            if (metadata.getId() >= 9) {
                metadata.setId(metadata.getId() + 1);
            }
        }
        else if (type.is(Entity1_14Types.EntityType.FIREWORK_ROCKET)) {
            if (metadata.getId() == 8) {
                if (metadata.getValue().equals(0)) {
                    metadata.setValue(null);
                }
                metadata.setMetaType(MetaType1_14.OptVarInt);
            }
        }
        else if (type.isOrHasParent(Entity1_14Types.EntityType.ABSTRACT_SKELETON)) {
            if (metadata.getId() == 14) {
                tracker.setInsentientData(entityId, (byte)((tracker.getInsentientData(entityId) & 0xFFFFFFFB) | (metadata.getValue() ? 4 : 0)));
                metadatas.remove(metadata);
                metadatas.add(new Metadata(13, MetaType1_14.Byte, tracker.getInsentientData(entityId)));
            }
        }
        else if (type.is(Entity1_14Types.EntityType.AREA_EFFECT_CLOUD) && metadata.getId() == 10) {
            this.rewriteParticle((Particle)metadata.getValue());
        }
        if (type.isOrHasParent(Entity1_14Types.EntityType.ABSTRACT_ILLAGER_BASE) && metadata.getId() == 14) {
            tracker.setInsentientData(entityId, (byte)((tracker.getInsentientData(entityId) & 0xFFFFFFFB) | ((((Number)metadata.getValue()).byteValue() != 0) ? 4 : 0)));
            metadatas.remove(metadata);
            metadatas.add(new Metadata(13, MetaType1_14.Byte, tracker.getInsentientData(entityId)));
        }
        if ((type.is(Entity1_14Types.EntityType.WITCH) || type.is(Entity1_14Types.EntityType.RAVAGER) || type.isOrHasParent(Entity1_14Types.EntityType.ABSTRACT_ILLAGER_BASE)) && metadata.getId() >= 14) {
            metadata.setId(metadata.getId() + 1);
        }
    }
    
    @Override
    protected EntityType getTypeFromId(final int type) {
        return Entity1_14Types.getTypeFromId(type);
    }
    
    private static boolean isSneaking(final byte flags) {
        return (flags & 0x2) != 0x0;
    }
    
    private static boolean isSwimming(final byte flags) {
        return (flags & 0x10) != 0x0;
    }
    
    private static int getNewProfessionId(final int old) {
        switch (old) {
            case 0: {
                return 5;
            }
            case 1: {
                return 9;
            }
            case 2: {
                return 4;
            }
            case 3: {
                return 1;
            }
            case 4: {
                return 2;
            }
            case 5: {
                return 11;
            }
            default: {
                return 0;
            }
        }
    }
    
    private static boolean isFallFlying(final int entityFlags) {
        return (entityFlags & 0x80) != 0x0;
    }
    
    public static int recalculatePlayerPose(final int entityId, final EntityTracker1_14 tracker) {
        final byte flags = tracker.getEntityFlags(entityId);
        int pose = 0;
        if (isFallFlying(flags)) {
            pose = 1;
        }
        else if (tracker.isSleeping(entityId)) {
            pose = 2;
        }
        else if (isSwimming(flags)) {
            pose = 3;
        }
        else if (tracker.isRiptide(entityId)) {
            pose = 4;
        }
        else if (isSneaking(flags)) {
            pose = 5;
        }
        return pose;
    }
}
