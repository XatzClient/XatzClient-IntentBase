// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.type.types.version;

import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.types.Particle1_13Type;
import us.myles.ViaVersion.api.type.types.Particle;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.List;
import us.myles.ViaVersion.api.type.Type;

public class Types1_13
{
    public static final Type<List<Metadata>> METADATA_LIST;
    public static final Type<Metadata> METADATA;
    public static final Type<ChunkSection> CHUNK_SECTION;
    public static final Type<Particle> PARTICLE;
    
    static {
        METADATA_LIST = new MetadataList1_13Type();
        METADATA = new Metadata1_13Type();
        CHUNK_SECTION = new ChunkSectionType1_13();
        PARTICLE = new Particle1_13Type();
    }
}
