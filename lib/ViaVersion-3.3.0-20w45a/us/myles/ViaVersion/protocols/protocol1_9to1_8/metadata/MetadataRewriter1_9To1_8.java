// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_9to1_8.metadata;

import us.myles.ViaVersion.api.minecraft.EulerAngle;
import us.myles.ViaVersion.api.minecraft.Vector;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.ItemRewriter;
import us.myles.ViaVersion.api.minecraft.item.Item;
import java.util.UUID;
import us.myles.ViaVersion.api.entities.Entity1_10Types;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_8;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_9;
import us.myles.ViaVersion.api.data.UserConnection;
import java.util.List;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.storage.EntityTracker;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import us.myles.ViaVersion.api.rewriters.MetadataRewriter;

public class MetadataRewriter1_9To1_8 extends MetadataRewriter
{
    public MetadataRewriter1_9To1_8(final Protocol1_9To1_8 protocol) {
        super(protocol, EntityTracker1_9.class);
    }
    
    @Override
    protected void handleMetadata(final int entityId, final EntityType type, final Metadata metadata, final List<Metadata> metadatas, final UserConnection connection) throws Exception {
        final MetaIndex metaIndex = MetaIndex.searchIndex(type, metadata.getId());
        if (metaIndex == null) {
            throw new Exception("Could not find valid metadata");
        }
        if (metaIndex.getNewType() == MetaType1_9.Discontinued) {
            metadatas.remove(metadata);
            return;
        }
        metadata.setId(metaIndex.getNewIndex());
        metadata.setMetaType(metaIndex.getNewType());
        Object value = metadata.getValue();
        switch (metaIndex.getNewType()) {
            case Byte: {
                if (metaIndex.getOldType() == MetaType1_8.Byte) {
                    metadata.setValue(value);
                }
                if (metaIndex.getOldType() == MetaType1_8.Int) {
                    metadata.setValue(((Integer)value).byteValue());
                }
                if (metaIndex == MetaIndex.ENTITY_STATUS && type == Entity1_10Types.EntityType.PLAYER) {
                    Byte val = 0;
                    if (((byte)value & 0x10) == 0x10) {
                        val = 1;
                    }
                    final int newIndex = MetaIndex.PLAYER_HAND.getNewIndex();
                    final MetaType metaType = MetaIndex.PLAYER_HAND.getNewType();
                    metadatas.add(new Metadata(newIndex, metaType, val));
                    break;
                }
                break;
            }
            case OptUUID: {
                final String owner = (String)value;
                UUID toWrite = null;
                if (!owner.isEmpty()) {
                    try {
                        toWrite = UUID.fromString(owner);
                    }
                    catch (Exception ex) {}
                }
                metadata.setValue(toWrite);
                break;
            }
            case VarInt: {
                if (metaIndex.getOldType() == MetaType1_8.Byte) {
                    metadata.setValue((int)value);
                }
                if (metaIndex.getOldType() == MetaType1_8.Short) {
                    metadata.setValue((int)value);
                }
                if (metaIndex.getOldType() == MetaType1_8.Int) {
                    metadata.setValue(value);
                    break;
                }
                break;
            }
            case Float: {
                metadata.setValue(value);
                break;
            }
            case String: {
                metadata.setValue(value);
                break;
            }
            case Boolean: {
                if (metaIndex == MetaIndex.AGEABLE_AGE) {
                    metadata.setValue((byte)value < 0);
                    break;
                }
                metadata.setValue((byte)value != 0);
                break;
            }
            case Slot: {
                metadata.setValue(value);
                ItemRewriter.toClient((Item)metadata.getValue());
                break;
            }
            case Position: {
                final Vector vector = (Vector)value;
                metadata.setValue(vector);
                break;
            }
            case Vector3F: {
                final EulerAngle angle = (EulerAngle)value;
                metadata.setValue(angle);
                break;
            }
            case Chat: {
                value = Protocol1_9To1_8.fixJson(value.toString());
                metadata.setValue(value);
                break;
            }
            case BlockID: {
                metadata.setValue(((Number)value).intValue());
                break;
            }
            default: {
                metadatas.remove(metadata);
                throw new Exception("Unhandled MetaDataType: " + metaIndex.getNewType());
            }
        }
    }
    
    @Override
    protected EntityType getTypeFromId(final int type) {
        return Entity1_10Types.getTypeFromId(type, false);
    }
    
    @Override
    protected EntityType getObjectTypeFromId(final int type) {
        return Entity1_10Types.getTypeFromId(type, true);
    }
}
