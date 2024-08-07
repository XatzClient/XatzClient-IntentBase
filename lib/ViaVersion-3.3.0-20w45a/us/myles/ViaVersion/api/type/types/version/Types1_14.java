// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.type.types.version;

import us.myles.ViaVersion.api.type.types.minecraft.Particle1_14Type;
import us.myles.ViaVersion.api.type.types.Particle;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.List;
import us.myles.ViaVersion.api.type.Type;

public class Types1_14
{
    public static final Type<List<Metadata>> METADATA_LIST;
    public static final Type<Metadata> METADATA;
    public static final Type<Particle> PARTICLE;
    
    static {
        METADATA_LIST = new MetadataList1_14Type();
        METADATA = new Metadata1_14Type();
        PARTICLE = new Particle1_14Type();
    }
}
