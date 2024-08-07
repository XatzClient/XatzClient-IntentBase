// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.type.types.version;

import us.myles.ViaVersion.protocols.protocol1_13_2to1_13_1.types.Particle1_13_2Type;
import us.myles.ViaVersion.api.type.types.Particle;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.List;
import us.myles.ViaVersion.api.type.Type;

public class Types1_13_2
{
    public static final Type<List<Metadata>> METADATA_LIST;
    public static final Type<Metadata> METADATA;
    public static Type<Particle> PARTICLE;
    
    static {
        METADATA_LIST = new MetadataList1_13_2Type();
        METADATA = new Metadata1_13_2Type();
        Types1_13_2.PARTICLE = new Particle1_13_2Type();
    }
}
