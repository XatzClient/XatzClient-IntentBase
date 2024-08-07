// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_15to1_14_4.metadata;

import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.packets.EntityPackets;
import us.myles.ViaVersion.api.type.types.Particle;
import us.myles.ViaVersion.api.entities.Entity1_15Types;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.packets.InventoryPackets;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_14;
import us.myles.ViaVersion.api.data.UserConnection;
import java.util.List;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.storage.EntityTracker;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.storage.EntityTracker1_15;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.Protocol1_15To1_14_4;
import us.myles.ViaVersion.api.rewriters.MetadataRewriter;

public class MetadataRewriter1_15To1_14_4 extends MetadataRewriter
{
    public MetadataRewriter1_15To1_14_4(final Protocol1_15To1_14_4 protocol) {
        super(protocol, EntityTracker1_15.class);
    }
    
    public void handleMetadata(final int entityId, final EntityType type, final Metadata metadata, final List<Metadata> metadatas, final UserConnection connection) throws Exception {
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
        if (metadata.getId() > 11 && type.isOrHasParent(Entity1_15Types.EntityType.LIVINGENTITY)) {
            metadata.setId(metadata.getId() + 1);
        }
        if (type.isOrHasParent(Entity1_15Types.EntityType.WOLF)) {
            if (metadata.getId() == 18) {
                metadatas.remove(metadata);
            }
            else if (metadata.getId() > 18) {
                metadata.setId(metadata.getId() - 1);
            }
        }
        else if (type == Entity1_15Types.EntityType.AREA_EFFECT_CLOUD && metadata.getId() == 10) {
            this.rewriteParticle((Particle)metadata.getValue());
        }
    }
    
    @Override
    public int getNewEntityId(final int oldId) {
        return EntityPackets.getNewEntityId(oldId);
    }
    
    @Override
    protected EntityType getTypeFromId(final int type) {
        return Entity1_15Types.getTypeFromId(type);
    }
}
