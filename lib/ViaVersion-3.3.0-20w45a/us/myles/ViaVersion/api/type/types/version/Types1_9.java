// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.type.types.version;

import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.List;
import us.myles.ViaVersion.api.type.Type;

public class Types1_9
{
    public static final Type<List<Metadata>> METADATA_LIST;
    public static final Type<Metadata> METADATA;
    public static final Type<ChunkSection> CHUNK_SECTION;
    
    static {
        METADATA_LIST = new MetadataList1_9Type();
        METADATA = new Metadata1_9Type();
        CHUNK_SECTION = new ChunkSectionType1_9();
    }
}
