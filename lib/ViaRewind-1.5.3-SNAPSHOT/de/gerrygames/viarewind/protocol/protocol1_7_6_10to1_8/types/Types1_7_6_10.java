// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types;

import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.List;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.type.Type;

public class Types1_7_6_10
{
    public static final Type<CompoundTag> COMPRESSED_NBT;
    public static final Type<Item[]> ITEM_ARRAY;
    public static final Type<Item[]> COMPRESSED_NBT_ITEM_ARRAY;
    public static final Type<Item> ITEM;
    public static final Type<Item> COMPRESSED_NBT_ITEM;
    public static final Type<List<Metadata>> METADATA_LIST;
    public static final Type<Metadata> METADATA;
    public static final Type<CompoundTag> NBT;
    public static final Type<int[]> INT_ARRAY;
    
    static {
        COMPRESSED_NBT = new CompressedNBTType();
        ITEM_ARRAY = new ItemArrayType(false);
        COMPRESSED_NBT_ITEM_ARRAY = new ItemArrayType(true);
        ITEM = new ItemType(false);
        COMPRESSED_NBT_ITEM = new ItemType(true);
        METADATA_LIST = (Type)new MetadataListType();
        METADATA = (Type)new MetadataType();
        NBT = new NBTType();
        INT_ARRAY = new IntArrayType();
    }
}
