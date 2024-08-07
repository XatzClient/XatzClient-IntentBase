// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.packets;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import nl.matsv.viabackwards.api.entities.meta.MetaHandlerEvent;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.entities.Entity1_15Types;
import us.myles.ViaVersion.api.type.types.Particle;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_14;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.List;
import us.myles.ViaVersion.api.type.types.version.Types1_14;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.entities.Entity1_16Types;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.ValueTransformer;
import nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.Protocol1_15_2To1_16;
import nl.matsv.viabackwards.api.rewriters.EntityRewriter;

public class EntityPackets1_16 extends EntityRewriter<Protocol1_15_2To1_16>
{
    private final ValueTransformer<String, Integer> dimensionTransformer;
    
    public EntityPackets1_16(final Protocol1_15_2To1_16 protocol) {
        super(protocol);
        this.dimensionTransformer = new ValueTransformer<String, Integer>(Type.STRING, Type.INT) {
            public Integer transform(final PacketWrapper wrapper, final String input) throws Exception {
                switch (input) {
                    case "minecraft:the_nether": {
                        return -1;
                    }
                    default: {
                        return 0;
                    }
                    case "minecraft:the_end": {
                        return 1;
                    }
                }
            }
        };
    }
    
    @Override
    protected void registerPackets() {
        this.registerSpawnTrackerWithData((ClientboundPacketType)ClientboundPackets1_16.SPAWN_ENTITY, (EntityType)Entity1_16Types.EntityType.FALLING_BLOCK);
        this.registerSpawnTracker((ClientboundPacketType)ClientboundPackets1_16.SPAWN_MOB);
        ((Protocol1_15_2To1_16)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16.RESPAWN, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(EntityPackets1_16.this.dimensionTransformer);
                this.map(Type.STRING, Type.NOTHING);
                this.map(Type.LONG);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.BYTE, Type.NOTHING);
                this.handler(wrapper -> {
                    final ClientWorld clientWorld = (ClientWorld)wrapper.user().get((Class)ClientWorld.class);
                    final int dimension = (int)wrapper.get(Type.INT, 0);
                    if (clientWorld.getEnvironment() != null && dimension == clientWorld.getEnvironment().getId()) {
                        final PacketWrapper packet = wrapper.create(ClientboundPackets1_15.RESPAWN.ordinal());
                        packet.write(Type.INT, (Object)((dimension == 0) ? -1 : 0));
                        packet.write(Type.LONG, (Object)0L);
                        packet.write(Type.UNSIGNED_BYTE, (Object)0);
                        packet.write(Type.STRING, (Object)"default");
                        packet.send((Class)Protocol1_15_2To1_16.class, true, true);
                    }
                    clientWorld.setEnvironment(dimension);
                    wrapper.write(Type.STRING, (Object)"default");
                    wrapper.read(Type.BOOLEAN);
                    if (wrapper.read(Type.BOOLEAN)) {
                        wrapper.set(Type.STRING, 0, (Object)"flat");
                    }
                    wrapper.read(Type.BOOLEAN);
                });
            }
        });
        ((Protocol1_15_2To1_16)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16.JOIN_GAME, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.BYTE, Type.NOTHING);
                this.map(Type.STRING_ARRAY, Type.NOTHING);
                this.map(Type.NBT, Type.NOTHING);
                this.map(EntityPackets1_16.this.dimensionTransformer);
                this.map(Type.STRING, Type.NOTHING);
                this.map(Type.LONG);
                this.map(Type.UNSIGNED_BYTE);
                this.handler(wrapper -> {
                    final ClientWorld clientChunks = (ClientWorld)wrapper.user().get((Class)ClientWorld.class);
                    clientChunks.setEnvironment((int)wrapper.get(Type.INT, 1));
                    EntityPackets1_16.this.getEntityTracker(wrapper.user()).trackEntityType((int)wrapper.get(Type.INT, 0), (EntityType)Entity1_16Types.EntityType.PLAYER);
                    wrapper.write(Type.STRING, (Object)"default");
                    wrapper.passthrough((Type)Type.VAR_INT);
                    wrapper.passthrough(Type.BOOLEAN);
                    wrapper.passthrough(Type.BOOLEAN);
                    wrapper.read(Type.BOOLEAN);
                    if (wrapper.read(Type.BOOLEAN)) {
                        wrapper.set(Type.STRING, 0, (Object)"flat");
                    }
                });
            }
        });
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_16.SPAWN_EXPERIENCE_ORB, (EntityType)Entity1_16Types.EntityType.EXPERIENCE_ORB);
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_16.SPAWN_PAINTING, (EntityType)Entity1_16Types.EntityType.PAINTING);
        this.registerExtraTracker((ClientboundPacketType)ClientboundPackets1_16.SPAWN_PLAYER, (EntityType)Entity1_16Types.EntityType.PLAYER);
        this.registerEntityDestroy((ClientboundPacketType)ClientboundPackets1_16.DESTROY_ENTITIES);
        this.registerMetadataRewriter((ClientboundPacketType)ClientboundPackets1_16.ENTITY_METADATA, (Type<List<Metadata>>)Types1_14.METADATA_LIST);
        ((Protocol1_15_2To1_16)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16.ENTITY_PROPERTIES, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    wrapper.passthrough((Type)Type.VAR_INT);
                    for (int size = (int)wrapper.passthrough(Type.INT), i = 0; i < size; ++i) {
                        final String attributeIdentifier = (String)wrapper.read(Type.STRING);
                        final String oldKey = ((Protocol1_15_2To1_16)EntityPackets1_16.this.protocol).getMappingData().getAttributeMappings().get(attributeIdentifier);
                        wrapper.write(Type.STRING, (Object)((oldKey != null) ? oldKey : attributeIdentifier.replace("minecraft:", "")));
                        wrapper.passthrough(Type.DOUBLE);
                        for (int modifierSize = (int)wrapper.passthrough((Type)Type.VAR_INT), j = 0; j < modifierSize; ++j) {
                            wrapper.passthrough(Type.UUID);
                            wrapper.passthrough(Type.DOUBLE);
                            wrapper.passthrough(Type.BYTE);
                        }
                    }
                });
            }
        });
        ((Protocol1_15_2To1_16)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16.PLAYER_INFO, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(packetWrapper -> {
                    final int action = (int)packetWrapper.passthrough((Type)Type.VAR_INT);
                    for (int playerCount = (int)packetWrapper.passthrough((Type)Type.VAR_INT), i = 0; i < playerCount; ++i) {
                        packetWrapper.passthrough(Type.UUID);
                        if (action == 0) {
                            packetWrapper.passthrough(Type.STRING);
                            for (int properties = (int)packetWrapper.passthrough((Type)Type.VAR_INT), j = 0; j < properties; ++j) {
                                packetWrapper.passthrough(Type.STRING);
                                packetWrapper.passthrough(Type.STRING);
                                if (packetWrapper.passthrough(Type.BOOLEAN)) {
                                    packetWrapper.passthrough(Type.STRING);
                                }
                            }
                            packetWrapper.passthrough((Type)Type.VAR_INT);
                            packetWrapper.passthrough((Type)Type.VAR_INT);
                            if (packetWrapper.passthrough(Type.BOOLEAN)) {
                                ((Protocol1_15_2To1_16)EntityPackets1_16.this.protocol).getTranslatableRewriter().processText((JsonElement)packetWrapper.passthrough(Type.COMPONENT));
                            }
                        }
                        else if (action == 1) {
                            packetWrapper.passthrough((Type)Type.VAR_INT);
                        }
                        else if (action == 2) {
                            packetWrapper.passthrough((Type)Type.VAR_INT);
                        }
                        else if (action == 3 && (boolean)packetWrapper.passthrough(Type.BOOLEAN)) {
                            ((Protocol1_15_2To1_16)EntityPackets1_16.this.protocol).getTranslatableRewriter().processText((JsonElement)packetWrapper.passthrough(Type.COMPONENT));
                        }
                    }
                });
            }
        });
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
                meta2.setValue((Object)((Protocol1_15_2To1_16)this.protocol).getBlockItemPackets().handleItemToClient((Item)meta2.getValue()));
            }
            else if (type == MetaType1_14.BlockID) {
                meta2.setValue((Object)((Protocol1_15_2To1_16)this.protocol).getMappingData().getNewBlockStateId((int)meta2.getValue()));
            }
            else if (type == MetaType1_14.PARTICLE) {
                this.rewriteParticle((Particle)meta2.getValue());
            }
            else if (type == MetaType1_14.OptChat) {
                text = (JsonElement)meta2.getCastedValue();
                if (text != null) {
                    ((Protocol1_15_2To1_16)this.protocol).getTranslatableRewriter().processText(text);
                }
            }
            return meta2;
        });
        this.mapEntityDirect((EntityType)Entity1_16Types.EntityType.ZOMBIFIED_PIGLIN, (EntityType)Entity1_15Types.EntityType.ZOMBIE_PIGMAN);
        this.mapTypes((EntityType[])Entity1_16Types.EntityType.values(), Entity1_15Types.EntityType.class);
        this.mapEntity((EntityType)Entity1_16Types.EntityType.HOGLIN, (EntityType)Entity1_16Types.EntityType.COW).jsonName("Hoglin");
        this.mapEntity((EntityType)Entity1_16Types.EntityType.ZOGLIN, (EntityType)Entity1_16Types.EntityType.COW).jsonName("Zoglin");
        this.mapEntity((EntityType)Entity1_16Types.EntityType.PIGLIN, (EntityType)Entity1_16Types.EntityType.ZOMBIFIED_PIGLIN).jsonName("Piglin");
        this.mapEntity((EntityType)Entity1_16Types.EntityType.STRIDER, (EntityType)Entity1_16Types.EntityType.MAGMA_CUBE).jsonName("Strider");
        this.registerMetaHandler().filter((EntityType)Entity1_16Types.EntityType.ZOGLIN, 16).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_16Types.EntityType.HOGLIN, 15).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_16Types.EntityType.PIGLIN, 16).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_16Types.EntityType.PIGLIN, 17).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_16Types.EntityType.PIGLIN, 18).removed();
        final boolean baby;
        this.registerMetaHandler().filter((EntityType)Entity1_16Types.EntityType.STRIDER, 15).handle(meta -> {
            baby = (boolean)meta.getData().getCastedValue();
            meta.getData().setValue((Object)(baby ? 1 : 3));
            meta.getData().setMetaType((MetaType)MetaType1_14.VarInt);
            return meta.getData();
        });
        this.registerMetaHandler().filter((EntityType)Entity1_16Types.EntityType.STRIDER, 16).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_16Types.EntityType.STRIDER, 17).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_16Types.EntityType.STRIDER, 18).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_16Types.EntityType.FISHING_BOBBER, 8).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_16Types.EntityType.ABSTRACT_ARROW, true, 8).removed();
        this.registerMetaHandler().filter((EntityType)Entity1_16Types.EntityType.ABSTRACT_ARROW, true).handle(meta -> {
            if (meta.getIndex() >= 8) {
                meta.getData().setId(meta.getIndex() + 1);
            }
            return meta.getData();
        });
    }
    
    @Override
    protected EntityType getTypeFromId(final int typeId) {
        return (EntityType)Entity1_16Types.getTypeFromId(typeId);
    }
}
