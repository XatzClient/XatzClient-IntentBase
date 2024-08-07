// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.type.types.minecraft;

import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.type.Type;

public abstract class BaseItemArrayType extends Type<Item[]>
{
    public BaseItemArrayType() {
        super(Item[].class);
    }
    
    public BaseItemArrayType(final String typeName) {
        super(typeName, Item[].class);
    }
    
    @Override
    public Class<? extends Type> getBaseClass() {
        return BaseItemArrayType.class;
    }
}
