// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.type.types.version;

import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_8;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.type.types.minecraft.MetaTypeTemplate;

public class Metadata1_8Type extends MetaTypeTemplate
{
    @Override
    public Metadata read(final ByteBuf buffer) throws Exception {
        final byte item = buffer.readByte();
        if (item == 127) {
            return null;
        }
        final int typeID = (item & 0xE0) >> 5;
        final MetaType1_8 type = MetaType1_8.byId(typeID);
        final int id = item & 0x1F;
        return new Metadata(id, type, type.getType().read(buffer));
    }
    
    @Override
    public void write(final ByteBuf buffer, final Metadata meta) throws Exception {
        final byte item = (byte)(meta.getMetaType().getTypeID() << 5 | (meta.getId() & 0x1F));
        buffer.writeByte((int)item);
        meta.getMetaType().getType().write(buffer, meta.getValue());
    }
}
