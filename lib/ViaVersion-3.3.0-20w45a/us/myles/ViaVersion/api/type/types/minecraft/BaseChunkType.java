// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.type.types.minecraft;

import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.api.type.Type;

public abstract class BaseChunkType extends Type<Chunk>
{
    public BaseChunkType() {
        super(Chunk.class);
    }
    
    public BaseChunkType(final String typeName) {
        super(typeName, Chunk.class);
    }
    
    @Override
    public Class<? extends Type> getBaseClass() {
        return BaseChunkType.class;
    }
}
