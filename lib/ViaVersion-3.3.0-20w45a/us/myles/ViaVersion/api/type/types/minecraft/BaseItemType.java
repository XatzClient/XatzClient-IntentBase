// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.type.types.minecraft;

import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.type.Type;

public abstract class BaseItemType extends Type<Item>
{
    public BaseItemType() {
        super(Item.class);
    }
    
    public BaseItemType(final String typeName) {
        super(typeName, Item.class);
    }
    
    @Override
    public Class<? extends Type> getBaseClass() {
        return BaseItemType.class;
    }
}
