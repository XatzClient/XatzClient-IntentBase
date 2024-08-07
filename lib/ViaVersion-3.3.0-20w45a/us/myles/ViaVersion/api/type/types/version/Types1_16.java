// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.type.types.version;

import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import us.myles.ViaVersion.api.type.Type;

public class Types1_16
{
    public static final Type<ChunkSection> CHUNK_SECTION;
    
    static {
        CHUNK_SECTION = new ChunkSectionType1_16();
    }
}
