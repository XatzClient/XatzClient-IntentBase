// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.type.types.version;

import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.List;
import us.myles.ViaVersion.api.type.Type;

public class Types1_12
{
    public static final Type<List<Metadata>> METADATA_LIST;
    public static final Type<Metadata> METADATA;
    
    static {
        METADATA_LIST = new MetadataList1_12Type();
        METADATA = new Metadata1_12Type();
    }
}
