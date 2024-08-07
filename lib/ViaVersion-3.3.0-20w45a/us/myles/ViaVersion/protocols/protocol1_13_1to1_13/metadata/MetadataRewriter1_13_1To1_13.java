// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_13_1to1_13.metadata;

import us.myles.ViaVersion.api.type.types.Particle;
import us.myles.ViaVersion.api.entities.Entity1_13Types;
import us.myles.ViaVersion.protocols.protocol1_13_1to1_13.packets.InventoryPackets;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_13;
import us.myles.ViaVersion.api.data.UserConnection;
import java.util.List;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.storage.EntityTracker;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.storage.EntityTracker1_13;
import us.myles.ViaVersion.protocols.protocol1_13_1to1_13.Protocol1_13_1To1_13;
import us.myles.ViaVersion.api.rewriters.MetadataRewriter;

public class MetadataRewriter1_13_1To1_13 extends MetadataRewriter
{
    public MetadataRewriter1_13_1To1_13(final Protocol1_13_1To1_13 protocol) {
        super(protocol, EntityTracker1_13.class);
    }
    
    @Override
    protected void handleMetadata(final int entityId, final EntityType type, final Metadata metadata, final List<Metadata> metadatas, final UserConnection connection) {
        if (metadata.getMetaType() == MetaType1_13.Slot) {
            InventoryPackets.toClient((Item)metadata.getValue());
        }
        else if (metadata.getMetaType() == MetaType1_13.BlockID) {
            final int data = (int)metadata.getValue();
            metadata.setValue(this.protocol.getMappingData().getNewBlockStateId(data));
        }
        if (type == null) {
            return;
        }
        if (type.isOrHasParent(Entity1_13Types.EntityType.MINECART_ABSTRACT) && metadata.getId() == 9) {
            final int data = (int)metadata.getValue();
            metadata.setValue(this.protocol.getMappingData().getNewBlockStateId(data));
        }
        else if (type.isOrHasParent(Entity1_13Types.EntityType.ABSTRACT_ARROW) && metadata.getId() >= 7) {
            metadata.setId(metadata.getId() + 1);
        }
        else if (type.is(Entity1_13Types.EntityType.AREA_EFFECT_CLOUD) && metadata.getId() == 10) {
            this.rewriteParticle((Particle)metadata.getValue());
        }
    }
    
    @Override
    protected EntityType getTypeFromId(final int type) {
        return Entity1_13Types.getTypeFromId(type, false);
    }
    
    @Override
    protected EntityType getObjectTypeFromId(final int type) {
        return Entity1_13Types.getTypeFromId(type, true);
    }
}
