// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.packets;

import java.util.function.Function;
import nl.matsv.viabackwards.api.entities.storage.MetaStorage;
import nl.matsv.viabackwards.api.entities.meta.MetaHandlerEvent;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import nl.matsv.viabackwards.api.entities.storage.EntityStorage;
import nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.data.ParrotStorage;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_12;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.List;
import io.netty.buffer.ByteBuf;
import nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.data.ShoulderTracker;
import us.myles.ViaVersion.api.type.types.version.Types1_12;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.entities.ObjectType;
import nl.matsv.viabackwards.utils.Block;
import java.util.Optional;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.entities.Entity1_12Types;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_12to1_11_1.ClientboundPackets1_12;
import nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.Protocol1_11_1To1_12;
import nl.matsv.viabackwards.api.rewriters.LegacyEntityRewriter;

public class EntityPackets1_12 extends LegacyEntityRewriter<Protocol1_11_1To1_12>
{
    public EntityPackets1_12(final Protocol1_11_1To1_12 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        ((Protocol1_11_1To1_12)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_12.SPAWN_ENTITY, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.BYTE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.INT);
                this.handler(LegacyEntityRewriter.this.getObjectTrackerHandler());
                this.handler(LegacyEntityRewriter.this.getObjectRewriter(id -> Entity1_12Types.ObjectType.findById((int)id).orElse(null)));
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final Optional<Entity1_12Types.ObjectType> type = (Optional<Entity1_12Types.ObjectType>)Entity1_12Types.ObjectType.findById((int)(byte)wrapper.get(Type.BYTE, 0));
                        if (type.isPresent() && type.get() == Entity1_12Types.ObjectType.FALLING_BLOCK) {
                            final int objectData = (int)wrapper.get(Type.INT, 0);
                            final int objType = objectData & 0xFFF;
                            final int data = objectData >> 12 & 0xF;
                            final Block block = EntityPackets1_12.this.getProtocol().getBlockItemPackets().handleBlock(objType, data);
                            if (block == null) {
                                return;
                            }
                            wrapper.set(Type.INT, 0, (Object)(block.getId() | block.getData() << 12));
                        }
                    }
                });
            }
        });
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_12.SPAWN_EXPERIENCE_ORB, (EntityType)Entity1_12Types.EntityType.EXPERIENCE_ORB);
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_12.SPAWN_GLOBAL_ENTITY, (EntityType)Entity1_12Types.EntityType.WEATHER);
        ((Protocol1_11_1To1_12)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_12.SPAWN_MOB, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type)Type.VAR_INT);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map((Type)Type.SHORT);
                this.map((Type)Type.SHORT);
                this.map((Type)Type.SHORT);
                this.map(Types1_12.METADATA_LIST);
                this.handler(EntityRewriterBase.this.getTrackerHandler());
                this.handler(LegacyEntityRewriter.this.getMobSpawnRewriter((Type<List<Metadata>>)Types1_12.METADATA_LIST));
            }
        });
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_12.SPAWN_PAINTING, (EntityType)Entity1_12Types.EntityType.PAINTING);
        ((Protocol1_11_1To1_12)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_12.SPAWN_PLAYER, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Types1_12.METADATA_LIST);
                this.handler(LegacyEntityRewriter.this.getTrackerAndMetaHandler((Type<List<Metadata>>)Types1_12.METADATA_LIST, (EntityType)Entity1_12Types.EntityType.PLAYER));
            }
        });
        ((Protocol1_11_1To1_12)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_12.JOIN_GAME, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.INT);
                this.handler(EntityRewriterBase.this.getTrackerHandler((EntityType)Entity1_12Types.EntityType.PLAYER, Type.INT));
                this.handler(EntityRewriterBase.this.getDimensionHandler(1));
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ShoulderTracker tracker = (ShoulderTracker)wrapper.user().get((Class)ShoulderTracker.class);
                        tracker.setEntityId((int)wrapper.get(Type.INT, 0));
                    }
                });
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final PacketWrapper wrapper = new PacketWrapper(7, (ByteBuf)null, packetWrapper.user());
                        wrapper.write((Type)Type.VAR_INT, (Object)1);
                        wrapper.write(Type.STRING, (Object)"achievement.openInventory");
                        wrapper.write((Type)Type.VAR_INT, (Object)1);
                        wrapper.send((Class)Protocol1_11_1To1_12.class);
                    }
                });
            }
        });
        this.registerRespawn((ClientboundPacketType)ClientboundPackets1_12.RESPAWN);
        this.registerEntityDestroy((ClientboundPacketType)ClientboundPackets1_12.DESTROY_ENTITIES);
        this.registerMetadataRewriter((ClientboundPacketType)ClientboundPackets1_12.ENTITY_METADATA, (Type<List<Metadata>>)Types1_12.METADATA_LIST);
        ((Protocol1_11_1To1_12)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_12.ENTITY_PROPERTIES, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.INT);
                this.handler(wrapper -> {
                    int newSize;
                    final int size = newSize = (int)wrapper.get(Type.INT, 0);
                    for (int i = 0; i < size; ++i) {
                        final String key = (String)wrapper.read(Type.STRING);
                        if (key.equals("generic.flyingSpeed")) {
                            --newSize;
                            wrapper.read(Type.DOUBLE);
                            for (int modSize = (int)wrapper.read((Type)Type.VAR_INT), j = 0; j < modSize; ++j) {
                                wrapper.read(Type.UUID);
                                wrapper.read(Type.DOUBLE);
                                wrapper.read(Type.BYTE);
                            }
                        }
                        else {
                            wrapper.write(Type.STRING, (Object)key);
                            wrapper.passthrough(Type.DOUBLE);
                            for (int modSize = (int)wrapper.passthrough((Type)Type.VAR_INT), j = 0; j < modSize; ++j) {
                                wrapper.passthrough(Type.UUID);
                                wrapper.passthrough(Type.DOUBLE);
                                wrapper.passthrough(Type.BYTE);
                            }
                        }
                    }
                    if (newSize != size) {
                        wrapper.set(Type.INT, 0, (Object)newSize);
                    }
                });
            }
        });
    }
    
    @Override
    protected void registerRewrites() {
        this.mapEntity((EntityType)Entity1_12Types.EntityType.PARROT, (EntityType)Entity1_12Types.EntityType.BAT).mobName("Parrot").spawnMetadata(storage -> storage.add(new Metadata(12, (MetaType)MetaType1_12.Byte, (Object)0)));
        this.mapEntity((EntityType)Entity1_12Types.EntityType.ILLUSION_ILLAGER, (EntityType)Entity1_12Types.EntityType.EVOCATION_ILLAGER).mobName("Illusioner");
        this.registerMetaHandler().filter((EntityType)Entity1_12Types.EntityType.EVOCATION_ILLAGER, true, 12).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_12Types.EntityType.EVOCATION_ILLAGER, true, 13).handleIndexChange(12);
        byte mask;
        this.registerMetaHandler().filter((EntityType)Entity1_12Types.EntityType.ILLUSION_ILLAGER, 0).handle(e -> {
            mask = (byte)e.getData().getValue();
            if ((mask & 0x20) == 0x20) {
                mask &= 0xFFFFFFDF;
            }
            e.getData().setValue((Object)mask);
            return e.getData();
        });
        this.registerMetaHandler().filter((EntityType)Entity1_12Types.EntityType.PARROT, true).handle(e -> {
            if (!e.getEntity().has(ParrotStorage.class)) {
                e.getEntity().put(new ParrotStorage());
            }
            return e.getData();
        });
        this.registerMetaHandler().filter((EntityType)Entity1_12Types.EntityType.PARROT, 12).removed();
        final Metadata data;
        final ParrotStorage storage2;
        final boolean isSitting;
        final boolean isTamed;
        this.registerMetaHandler().filter((EntityType)Entity1_12Types.EntityType.PARROT, 13).handle(e -> {
            data = e.getData();
            storage2 = e.getEntity().get(ParrotStorage.class);
            isSitting = (((byte)data.getValue() & 0x1) == 0x1);
            isTamed = (((byte)data.getValue() & 0x4) == 0x4);
            if (storage2.isTamed() || isTamed) {}
            storage2.setTamed(isTamed);
            if (isSitting) {
                data.setId(12);
                data.setValue((Object)1);
                storage2.setSitting(true);
            }
            else if (storage2.isSitting()) {
                data.setId(12);
                data.setValue((Object)0);
                storage2.setSitting(false);
            }
            else {
                throw RemovedValueException.EX;
            }
            return data;
        });
        this.registerMetaHandler().filter((EntityType)Entity1_12Types.EntityType.PARROT, 14).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_12Types.EntityType.PARROT, 15).removed();
        final CompoundTag tag;
        final ShoulderTracker tracker;
        String id;
        this.registerMetaHandler().filter((EntityType)Entity1_12Types.EntityType.PLAYER, 15).handle(e -> {
            tag = (CompoundTag)e.getData().getValue();
            tracker = (ShoulderTracker)e.getUser().get((Class)ShoulderTracker.class);
            if (tag.isEmpty() && tracker.getLeftShoulder() != null) {
                tracker.setLeftShoulder(null);
                tracker.update();
            }
            else if (tag.contains("id") && e.getEntity().getEntityId() == tracker.getEntityId()) {
                id = (String)tag.get("id").getValue();
                if (tracker.getLeftShoulder() == null || !tracker.getLeftShoulder().equals(id)) {
                    tracker.setLeftShoulder(id);
                    tracker.update();
                }
            }
            throw RemovedValueException.EX;
        });
        final CompoundTag tag2;
        final ShoulderTracker tracker2;
        String id2;
        this.registerMetaHandler().filter((EntityType)Entity1_12Types.EntityType.PLAYER, 16).handle(e -> {
            tag2 = (CompoundTag)e.getData().getValue();
            tracker2 = (ShoulderTracker)e.getUser().get((Class)ShoulderTracker.class);
            if (tag2.isEmpty() && tracker2.getRightShoulder() != null) {
                tracker2.setRightShoulder(null);
                tracker2.update();
            }
            else if (tag2.contains("id") && e.getEntity().getEntityId() == tracker2.getEntityId()) {
                id2 = (String)tag2.get("id").getValue();
                if (tracker2.getRightShoulder() == null || !tracker2.getRightShoulder().equals(id2)) {
                    tracker2.setRightShoulder(id2);
                    tracker2.update();
                }
            }
            throw RemovedValueException.EX;
        });
    }
    
    @Override
    protected EntityType getTypeFromId(final int typeId) {
        return (EntityType)Entity1_12Types.getTypeFromId(typeId, false);
    }
    
    @Override
    protected EntityType getObjectTypeFromId(final int typeId) {
        return (EntityType)Entity1_12Types.getTypeFromId(typeId, true);
    }
}
