// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.minecraft.metadata.types;

import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;

public enum MetaType1_8 implements MetaType
{
    Byte(0, (Type)Type.BYTE), 
    Short(1, (Type)Type.SHORT), 
    Int(2, (Type)Type.INT), 
    Float(3, (Type)Type.FLOAT), 
    String(4, (Type)Type.STRING), 
    Slot(5, (Type)Type.ITEM), 
    Position(6, (Type)Type.VECTOR), 
    Rotation(7, (Type)Type.ROTATION), 
    NonExistent(-1, (Type)Type.NOTHING);
    
    private final int typeID;
    private final Type type;
    
    private MetaType1_8(final int typeID, final Type type) {
        this.typeID = typeID;
        this.type = type;
    }
    
    public static MetaType1_8 byId(final int id) {
        return values()[id];
    }
    
    @Override
    public int getTypeID() {
        return this.typeID;
    }
    
    @Override
    public Type getType() {
        return this.type;
    }
}
