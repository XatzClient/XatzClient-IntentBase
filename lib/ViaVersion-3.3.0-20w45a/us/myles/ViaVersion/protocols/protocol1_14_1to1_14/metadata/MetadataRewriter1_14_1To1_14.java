// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_14_1to1_14.metadata;

import us.myles.ViaVersion.api.entities.Entity1_14Types;
import us.myles.ViaVersion.api.data.UserConnection;
import java.util.List;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.storage.EntityTracker;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_14_1to1_14.storage.EntityTracker1_14_1;
import us.myles.ViaVersion.protocols.protocol1_14_1to1_14.Protocol1_14_1To1_14;
import us.myles.ViaVersion.api.rewriters.MetadataRewriter;

public class MetadataRewriter1_14_1To1_14 extends MetadataRewriter
{
    public MetadataRewriter1_14_1To1_14(final Protocol1_14_1To1_14 protocol) {
        super(protocol, EntityTracker1_14_1.class);
    }
    
    public void handleMetadata(final int entityId, final EntityType type, final Metadata metadata, final List<Metadata> metadatas, final UserConnection connection) {
        if (type == null) {
            return;
        }
        if ((type == Entity1_14Types.EntityType.VILLAGER || type == Entity1_14Types.EntityType.WANDERING_TRADER) && metadata.getId() >= 15) {
            metadata.setId(metadata.getId() + 1);
        }
    }
    
    @Override
    protected EntityType getTypeFromId(final int type) {
        return Entity1_14Types.getTypeFromId(type);
    }
}
