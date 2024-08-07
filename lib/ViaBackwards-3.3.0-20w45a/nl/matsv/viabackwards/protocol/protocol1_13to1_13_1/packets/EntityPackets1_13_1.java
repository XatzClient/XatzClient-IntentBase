// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_13to1_13_1.packets;

import us.myles.ViaVersion.api.data.UserConnection;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import nl.matsv.viabackwards.api.entities.meta.MetaHandlerEvent;
import us.myles.ViaVersion.api.type.types.Particle;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_13;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import nl.matsv.viabackwards.api.entities.storage.MetaStorage;
import java.util.List;
import us.myles.ViaVersion.api.type.types.version.Types1_13;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.entities.EntityType;
import nl.matsv.viabackwards.ViaBackwards;
import us.myles.ViaVersion.api.entities.Entity1_13Types;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import nl.matsv.viabackwards.protocol.protocol1_13to1_13_1.Protocol1_13To1_13_1;
import nl.matsv.viabackwards.api.rewriters.LegacyEntityRewriter;

public class EntityPackets1_13_1 extends LegacyEntityRewriter<Protocol1_13To1_13_1>
{
    public EntityPackets1_13_1(final Protocol1_13To1_13_1 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        ((Protocol1_13To1_13_1)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.SPAWN_ENTITY, (PacketRemapper)new PacketRemapper() {
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
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int entityId = (int)wrapper.get((Type)Type.VAR_INT, 0);
                        final byte type = (byte)wrapper.get(Type.BYTE, 0);
                        final Entity1_13Types.EntityType entType = Entity1_13Types.getTypeFromId((int)type, true);
                        if (entType == null) {
                            ViaBackwards.getPlatform().getLogger().warning("Could not find 1.13 entity type " + type);
                            return;
                        }
                        if (entType.is((EntityType)Entity1_13Types.EntityType.FALLING_BLOCK)) {
                            final int data = (int)wrapper.get(Type.INT, 0);
                            wrapper.set(Type.INT, 0, (Object)((Protocol1_13To1_13_1)EntityPackets1_13_1.this.protocol).getMappingData().getNewBlockStateId(data));
                        }
                        EntityRewriterBase.this.addTrackedEntity(wrapper, entityId, (EntityType)entType);
                    }
                });
            }
        });
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_13.SPAWN_EXPERIENCE_ORB, (EntityType)Entity1_13Types.EntityType.EXPERIENCE_ORB);
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_13.SPAWN_GLOBAL_ENTITY, (EntityType)Entity1_13Types.EntityType.LIGHTNING_BOLT);
        ((Protocol1_13To1_13_1)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.SPAWN_MOB, (PacketRemapper)new PacketRemapper() {
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
                this.map(Types1_13.METADATA_LIST);
                this.handler(EntityRewriterBase.this.getTrackerHandler());
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final MetaStorage storage = new MetaStorage((List<Metadata>)wrapper.get(Types1_13.METADATA_LIST, 0));
                        EntityRewriterBase.this.handleMeta(wrapper.user(), (int)wrapper.get((Type)Type.VAR_INT, 0), storage);
                        wrapper.set(Types1_13.METADATA_LIST, 0, (Object)storage.getMetaDataList());
                    }
                });
            }
        });
        ((Protocol1_13To1_13_1)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.SPAWN_PLAYER, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Types1_13.METADATA_LIST);
                this.handler(LegacyEntityRewriter.this.getTrackerAndMetaHandler((Type<List<Metadata>>)Types1_13.METADATA_LIST, (EntityType)Entity1_13Types.EntityType.PLAYER));
            }
        });
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_13.SPAWN_PAINTING, (EntityType)Entity1_13Types.EntityType.PAINTING);
        this.registerJoinGame((ClientboundPacketType)ClientboundPackets1_13.JOIN_GAME, (EntityType)Entity1_13Types.EntityType.PLAYER);
        this.registerRespawn((ClientboundPacketType)ClientboundPackets1_13.RESPAWN);
        this.registerEntityDestroy((ClientboundPacketType)ClientboundPackets1_13.DESTROY_ENTITIES);
        this.registerMetadataRewriter((ClientboundPacketType)ClientboundPackets1_13.ENTITY_METADATA, (Type<List<Metadata>>)Types1_13.METADATA_LIST);
    }
    
    @Override
    protected void registerRewrites() {
        final Metadata meta;
        int data;
        this.registerMetaHandler().handle(e -> {
            meta = e.getData();
            if (meta.getMetaType() == MetaType1_13.Slot) {
                InventoryPackets1_13_1.toClient((Item)meta.getValue());
            }
            else if (meta.getMetaType() == MetaType1_13.BlockID) {
                data = (int)meta.getValue();
                meta.setValue((Object)((Protocol1_13To1_13_1)this.protocol).getMappingData().getNewBlockStateId(data));
            }
            else if (meta.getMetaType() == MetaType1_13.PARTICLE) {
                this.rewriteParticle((Particle)meta.getValue());
            }
            return meta;
        });
        this.registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.ABSTRACT_ARROW, true, 7).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.SPECTRAL_ARROW, 8).handleIndexChange(7);
        this.registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.TRIDENT, 8).handleIndexChange(7);
        final Metadata meta2;
        final int data2;
        this.registerMetaHandler().filter((EntityType)Entity1_13Types.EntityType.MINECART_ABSTRACT, true, 9).handle(e -> {
            meta2 = e.getData();
            data2 = (int)meta2.getValue();
            meta2.setValue((Object)((Protocol1_13To1_13_1)this.protocol).getMappingData().getNewBlockStateId(data2));
            return meta2;
        });
    }
    
    @Override
    protected EntityType getTypeFromId(final int typeId) {
        return (EntityType)Entity1_13Types.getTypeFromId(typeId, false);
    }
    
    @Override
    protected EntityType getObjectTypeFromId(final int typeId) {
        return (EntityType)Entity1_13Types.getTypeFromId(typeId, true);
    }
}
