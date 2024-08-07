// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.type.types.minecraft;

import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import io.netty.buffer.ByteBuf;

public abstract class ModernMetaType extends MetaTypeTemplate
{
    @Override
    public Metadata read(final ByteBuf buffer) throws Exception {
        final short index = buffer.readUnsignedByte();
        if (index == 255) {
            return null;
        }
        final MetaType type = this.getType(buffer.readByte());
        return new Metadata(index, type, type.getType().read(buffer));
    }
    
    protected abstract MetaType getType(final int p0);
    
    @Override
    public void write(final ByteBuf buffer, final Metadata object) throws Exception {
        if (object == null) {
            buffer.writeByte(255);
        }
        else {
            buffer.writeByte(object.getId());
            final MetaType type = object.getMetaType();
            buffer.writeByte(type.getTypeID());
            type.getType().write(buffer, object.getValue());
        }
    }
}
