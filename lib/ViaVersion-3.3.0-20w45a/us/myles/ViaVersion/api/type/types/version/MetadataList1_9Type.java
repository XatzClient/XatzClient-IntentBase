// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.type.types.version;

import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.type.types.minecraft.ModernMetaListType;

public class MetadataList1_9Type extends ModernMetaListType
{
    @Override
    protected Type<Metadata> getType() {
        return Types1_9.METADATA;
    }
}
