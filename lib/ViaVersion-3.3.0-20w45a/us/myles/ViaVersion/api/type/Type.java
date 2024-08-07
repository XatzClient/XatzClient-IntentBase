// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.type;

import us.myles.ViaVersion.api.type.types.minecraft.FlatVarIntItemArrayType;
import us.myles.ViaVersion.api.type.types.minecraft.FlatItemArrayType;
import us.myles.ViaVersion.api.type.types.minecraft.FlatVarIntItemType;
import us.myles.ViaVersion.api.type.types.minecraft.FlatItemType;
import us.myles.ViaVersion.api.type.types.minecraft.VillagerDataType;
import us.myles.ViaVersion.api.type.types.minecraft.VarLongBlockChangeRecordType;
import us.myles.ViaVersion.api.type.types.minecraft.BlockChangeRecordType;
import us.myles.ViaVersion.api.type.types.minecraft.ItemArrayType;
import us.myles.ViaVersion.api.type.types.minecraft.ItemType;
import us.myles.ViaVersion.api.type.types.minecraft.OptPosition1_14Type;
import us.myles.ViaVersion.api.type.types.minecraft.OptPositionType;
import us.myles.ViaVersion.api.type.types.minecraft.OptionalComponentType;
import us.myles.ViaVersion.api.type.types.minecraft.OptUUIDType;
import us.myles.ViaVersion.api.type.types.minecraft.NBTType;
import us.myles.ViaVersion.api.type.types.minecraft.VectorType;
import us.myles.ViaVersion.api.type.types.minecraft.EulerAngleType;
import us.myles.ViaVersion.api.type.types.minecraft.Position1_14Type;
import us.myles.ViaVersion.api.type.types.minecraft.PositionType;
import us.myles.ViaVersion.api.type.types.VoidType;
import us.myles.ViaVersion.api.type.types.minecraft.OptionalVarIntType;
import us.myles.ViaVersion.api.type.types.VarIntArrayType;
import us.myles.ViaVersion.api.type.types.UUIDIntArrayType;
import us.myles.ViaVersion.api.type.types.UUIDType;
import us.myles.ViaVersion.api.type.types.StringType;
import us.myles.ViaVersion.api.type.types.ComponentType;
import us.myles.ViaVersion.api.type.types.UnsignedShortType;
import us.myles.ViaVersion.api.type.types.LongType;
import us.myles.ViaVersion.api.type.types.DoubleType;
import us.myles.ViaVersion.api.type.types.IntType;
import us.myles.ViaVersion.api.type.types.BooleanType;
import us.myles.ViaVersion.api.type.types.UnsignedByteType;
import us.myles.ViaVersion.api.type.types.RemainingBytesType;
import us.myles.ViaVersion.api.type.types.ByteArrayType;
import us.myles.ViaVersion.api.type.types.ArrayType;
import us.myles.ViaVersion.api.type.types.ByteType;
import us.myles.ViaVersion.api.minecraft.VillagerData;
import us.myles.ViaVersion.api.minecraft.BlockChangeRecord;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.minecraft.Vector;
import us.myles.ViaVersion.api.minecraft.EulerAngle;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.type.types.VarLongType;
import us.myles.ViaVersion.api.type.types.VarIntType;
import java.util.UUID;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.ViaVersion.api.type.types.ShortType;
import us.myles.ViaVersion.api.type.types.FloatType;

public abstract class Type<T> implements ByteBufReader<T>, ByteBufWriter<T>
{
    public static final Type<Byte> BYTE;
    @Deprecated
    public static final Type<Byte[]> BYTE_ARRAY;
    public static final Type<byte[]> BYTE_ARRAY_PRIMITIVE;
    public static final Type<byte[]> REMAINING_BYTES;
    public static final Type<Short> UNSIGNED_BYTE;
    @Deprecated
    public static final Type<Short[]> UNSIGNED_BYTE_ARRAY;
    public static final Type<Boolean> BOOLEAN;
    @Deprecated
    public static final Type<Boolean[]> BOOLEAN_ARRAY;
    public static final Type<Integer> INT;
    @Deprecated
    public static final Type<Integer[]> INT_ARRAY;
    public static final Type<Double> DOUBLE;
    @Deprecated
    public static final Type<Double[]> DOUBLE_ARRAY;
    public static final Type<Long> LONG;
    @Deprecated
    public static final Type<Long[]> LONG_ARRAY;
    public static final FloatType FLOAT;
    @Deprecated
    public static final Type<Float[]> FLOAT_ARRAY;
    public static final ShortType SHORT;
    @Deprecated
    public static final Type<Short[]> SHORT_ARRAY;
    public static final Type<Integer> UNSIGNED_SHORT;
    @Deprecated
    public static final Type<Integer[]> UNSIGNED_SHORT_ARRAY;
    public static final Type<JsonElement> COMPONENT;
    public static final Type<String> STRING;
    public static final Type<String[]> STRING_ARRAY;
    public static final Type<UUID> UUID;
    public static final Type<UUID> UUID_INT_ARRAY;
    public static final Type<UUID[]> UUID_ARRAY;
    public static final VarIntType VAR_INT;
    @Deprecated
    public static final Type<Integer[]> VAR_INT_ARRAY;
    public static final Type<int[]> VAR_INT_ARRAY_PRIMITIVE;
    public static final Type<Integer> OPTIONAL_VAR_INT;
    public static final VarLongType VAR_LONG;
    @Deprecated
    public static final Type<Long[]> VAR_LONG_ARRAY;
    public static final Type<Void> NOTHING;
    public static final Type<Position> POSITION;
    public static final Type<Position> POSITION1_14;
    public static final Type<EulerAngle> ROTATION;
    public static final Type<Vector> VECTOR;
    public static final Type<CompoundTag> NBT;
    public static final Type<CompoundTag[]> NBT_ARRAY;
    public static final Type<UUID> OPTIONAL_UUID;
    public static final Type<JsonElement> OPTIONAL_COMPONENT;
    public static final Type<Position> OPTIONAL_POSITION;
    public static final Type<Position> OPTIONAL_POSITION_1_14;
    public static final Type<Item> ITEM;
    public static final Type<Item[]> ITEM_ARRAY;
    public static final Type<BlockChangeRecord> BLOCK_CHANGE_RECORD;
    public static final Type<BlockChangeRecord[]> BLOCK_CHANGE_RECORD_ARRAY;
    public static final Type<BlockChangeRecord> VAR_LONG_BLOCK_CHANGE_RECORD;
    public static final Type<BlockChangeRecord[]> VAR_LONG_BLOCK_CHANGE_RECORD_ARRAY;
    public static final Type<VillagerData> VILLAGER_DATA;
    public static final Type<Item> FLAT_ITEM;
    public static final Type<Item> FLAT_VAR_INT_ITEM;
    public static final Type<Item[]> FLAT_ITEM_ARRAY;
    public static final Type<Item[]> FLAT_VAR_INT_ITEM_ARRAY;
    public static final Type<Item[]> FLAT_ITEM_ARRAY_VAR_INT;
    public static final Type<Item[]> FLAT_VAR_INT_ITEM_ARRAY_VAR_INT;
    private final Class<? super T> outputClass;
    private final String typeName;
    
    public Type(final Class<? super T> outputClass) {
        this(outputClass.getSimpleName(), outputClass);
    }
    
    public Type(final String typeName, final Class<? super T> outputClass) {
        this.outputClass = outputClass;
        this.typeName = typeName;
    }
    
    public Class<? super T> getOutputClass() {
        return this.outputClass;
    }
    
    public String getTypeName() {
        return this.typeName;
    }
    
    public Class<? extends Type> getBaseClass() {
        return this.getClass();
    }
    
    @Override
    public String toString() {
        return "Type|" + this.typeName;
    }
    
    static {
        BYTE = new ByteType();
        BYTE_ARRAY = new ArrayType((Type<Object>)Type.BYTE);
        BYTE_ARRAY_PRIMITIVE = new ByteArrayType();
        REMAINING_BYTES = new RemainingBytesType();
        UNSIGNED_BYTE = new UnsignedByteType();
        UNSIGNED_BYTE_ARRAY = new ArrayType((Type<Object>)Type.UNSIGNED_BYTE);
        BOOLEAN = new BooleanType();
        BOOLEAN_ARRAY = new ArrayType((Type<Object>)Type.BOOLEAN);
        INT = new IntType();
        INT_ARRAY = new ArrayType((Type<Object>)Type.INT);
        DOUBLE = new DoubleType();
        DOUBLE_ARRAY = new ArrayType((Type<Object>)Type.DOUBLE);
        LONG = new LongType();
        LONG_ARRAY = new ArrayType((Type<Object>)Type.LONG);
        FLOAT = new FloatType();
        FLOAT_ARRAY = new ArrayType((Type<Object>)Type.FLOAT);
        SHORT = new ShortType();
        SHORT_ARRAY = new ArrayType((Type<Object>)Type.SHORT);
        UNSIGNED_SHORT = new UnsignedShortType();
        UNSIGNED_SHORT_ARRAY = new ArrayType((Type<Object>)Type.UNSIGNED_SHORT);
        COMPONENT = new ComponentType();
        STRING = new StringType();
        STRING_ARRAY = new ArrayType((Type<Object>)Type.STRING);
        UUID = new UUIDType();
        UUID_INT_ARRAY = new UUIDIntArrayType();
        UUID_ARRAY = new ArrayType((Type<Object>)Type.UUID);
        VAR_INT = new VarIntType();
        VAR_INT_ARRAY = new ArrayType((Type<Object>)Type.VAR_INT);
        VAR_INT_ARRAY_PRIMITIVE = new VarIntArrayType();
        OPTIONAL_VAR_INT = new OptionalVarIntType();
        VAR_LONG = new VarLongType();
        VAR_LONG_ARRAY = new ArrayType((Type<Object>)Type.VAR_LONG);
        NOTHING = new VoidType();
        POSITION = new PositionType();
        POSITION1_14 = new Position1_14Type();
        ROTATION = new EulerAngleType();
        VECTOR = new VectorType();
        NBT = new NBTType();
        NBT_ARRAY = new ArrayType((Type<Object>)Type.NBT);
        OPTIONAL_UUID = new OptUUIDType();
        OPTIONAL_COMPONENT = new OptionalComponentType();
        OPTIONAL_POSITION = new OptPositionType();
        OPTIONAL_POSITION_1_14 = new OptPosition1_14Type();
        ITEM = new ItemType();
        ITEM_ARRAY = new ItemArrayType();
        BLOCK_CHANGE_RECORD = new BlockChangeRecordType();
        BLOCK_CHANGE_RECORD_ARRAY = new ArrayType((Type<Object>)Type.BLOCK_CHANGE_RECORD);
        VAR_LONG_BLOCK_CHANGE_RECORD = new VarLongBlockChangeRecordType();
        VAR_LONG_BLOCK_CHANGE_RECORD_ARRAY = new ArrayType((Type<Object>)Type.VAR_LONG_BLOCK_CHANGE_RECORD);
        VILLAGER_DATA = new VillagerDataType();
        FLAT_ITEM = new FlatItemType();
        FLAT_VAR_INT_ITEM = new FlatVarIntItemType();
        FLAT_ITEM_ARRAY = new FlatItemArrayType();
        FLAT_VAR_INT_ITEM_ARRAY = new FlatVarIntItemArrayType();
        FLAT_ITEM_ARRAY_VAR_INT = new ArrayType((Type<Object>)Type.FLAT_ITEM);
        FLAT_VAR_INT_ITEM_ARRAY_VAR_INT = new ArrayType((Type<Object>)Type.FLAT_VAR_INT_ITEM);
    }
}
