// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.type.types.minecraft;

import us.myles.ViaVersion.api.minecraft.BlockChangeRecord1_16_2;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.minecraft.BlockChangeRecord;
import us.myles.ViaVersion.api.type.Type;

public class VarLongBlockChangeRecordType extends Type<BlockChangeRecord>
{
    public VarLongBlockChangeRecordType() {
        super(BlockChangeRecord.class);
    }
    
    @Override
    public BlockChangeRecord read(final ByteBuf buffer) throws Exception {
        final long data = Type.VAR_LONG.readPrimitive(buffer);
        final short position = (short)(data & 0xFFFL);
        return new BlockChangeRecord1_16_2(position >>> 8 & 0xF, position & 0xF, position >>> 4 & 0xF, (int)(data >>> 12));
    }
    
    @Override
    public void write(final ByteBuf buffer, final BlockChangeRecord object) throws Exception {
        final short position = (short)(object.getSectionX() << 8 | object.getSectionZ() << 4 | object.getSectionY());
        Type.VAR_LONG.writePrimitive(buffer, (long)object.getBlockId() << 12 | (long)position);
    }
}
