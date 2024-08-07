// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types;

import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.type.types.minecraft.MetaTypeTemplate;

public class MetadataType extends MetaTypeTemplate
{
    public Metadata read(final ByteBuf buffer) throws Exception {
        final byte item = buffer.readByte();
        if (item == 127) {
            return null;
        }
        final int typeID = (item & 0xE0) >> 5;
        final MetaType1_7_6_10 type = MetaType1_7_6_10.byId(typeID);
        final int id = item & 0x1F;
        return new Metadata(id, (MetaType)type, type.getType().read(buffer));
    }
    
    public void write(final ByteBuf buffer, final Metadata meta) throws Exception {
        final int item = (meta.getMetaType().getTypeID() << 5 | (meta.getId() & 0x1F)) & 0xFF;
        buffer.writeByte(item);
        meta.getMetaType().getType().write(buffer, meta.getValue());
    }
}
