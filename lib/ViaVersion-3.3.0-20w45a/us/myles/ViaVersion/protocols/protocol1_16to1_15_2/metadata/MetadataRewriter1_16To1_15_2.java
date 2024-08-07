// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_16to1_15_2.metadata;

import us.myles.ViaVersion.api.type.types.Particle;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.packets.InventoryPackets;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_14;
import us.myles.ViaVersion.api.data.UserConnection;
import java.util.List;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.entities.Entity1_16Types;
import us.myles.ViaVersion.api.entities.Entity1_15Types;
import us.myles.ViaVersion.api.storage.EntityTracker;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.storage.EntityTracker1_16;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.Protocol1_16To1_15_2;
import us.myles.ViaVersion.api.rewriters.MetadataRewriter;

public class MetadataRewriter1_16To1_15_2 extends MetadataRewriter
{
    public MetadataRewriter1_16To1_15_2(final Protocol1_16To1_15_2 protocol) {
        super(protocol, EntityTracker1_16.class);
        this.mapType(Entity1_15Types.EntityType.ZOMBIE_PIGMAN, Entity1_16Types.EntityType.ZOMBIFIED_PIGLIN);
        this.mapTypes(Entity1_15Types.EntityType.values(), Entity1_16Types.EntityType.class);
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
        if (type == Entity1_16Types.EntityType.AREA_EFFECT_CLOUD) {
            if (metadata.getId() == 10) {
                this.rewriteParticle((Particle)metadata.getValue());
            }
        }
        else if (type.isOrHasParent(Entity1_16Types.EntityType.ABSTRACT_ARROW)) {
            if (metadata.getId() == 8) {
                metadatas.remove(metadata);
            }
            else if (metadata.getId() > 8) {
                metadata.setId(metadata.getId() - 1);
            }
        }
    }
    
    @Override
    protected EntityType getTypeFromId(final int type) {
        return Entity1_16Types.getTypeFromId(type);
    }
}
