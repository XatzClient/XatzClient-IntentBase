// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.type.types.minecraft;

import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.minecraft.VillagerData;
import us.myles.ViaVersion.api.type.Type;

public class VillagerDataType extends Type<VillagerData>
{
    public VillagerDataType() {
        super(VillagerData.class);
    }
    
    @Override
    public VillagerData read(final ByteBuf buffer) throws Exception {
        return new VillagerData(Type.VAR_INT.readPrimitive(buffer), Type.VAR_INT.readPrimitive(buffer), Type.VAR_INT.readPrimitive(buffer));
    }
    
    @Override
    public void write(final ByteBuf buffer, final VillagerData object) throws Exception {
        Type.VAR_INT.writePrimitive(buffer, object.getType());
        Type.VAR_INT.writePrimitive(buffer, object.getProfession());
        Type.VAR_INT.writePrimitive(buffer, object.getLevel());
    }
}