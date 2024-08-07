// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.minecraft.item;

import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.gson.annotations.SerializedName;

public class Item
{
    @SerializedName(value = "identifier", alternate = { "id" })
    private int identifier;
    private byte amount;
    private short data;
    private CompoundTag tag;
    
    public Item() {
    }
    
    public Item(final int identifier, final byte amount, final short data, @Nullable final CompoundTag tag) {
        this.identifier = identifier;
        this.amount = amount;
        this.data = data;
        this.tag = tag;
    }
    
    public Item(final Item toCopy) {
        this(toCopy.getIdentifier(), toCopy.getAmount(), toCopy.getData(), toCopy.getTag());
    }
    
    public int getIdentifier() {
        return this.identifier;
    }
    
    public void setIdentifier(final int identifier) {
        this.identifier = identifier;
    }
    
    public byte getAmount() {
        return this.amount;
    }
    
    public void setAmount(final byte amount) {
        this.amount = amount;
    }
    
    public short getData() {
        return this.data;
    }
    
    public void setData(final short data) {
        this.data = data;
    }
    
    @Nullable
    public CompoundTag getTag() {
        return this.tag;
    }
    
    public void setTag(@Nullable final CompoundTag tag) {
        this.tag = tag;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Item item = (Item)o;
        return this.identifier == item.identifier && this.amount == item.amount && this.data == item.data && Objects.equals(this.tag, item.tag);
    }
    
    @Override
    public int hashCode() {
        int result = this.identifier;
        result = 31 * result + this.amount;
        result = 31 * result + this.data;
        result = 31 * result + ((this.tag != null) ? this.tag.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "Item{identifier=" + this.identifier + ", amount=" + this.amount + ", data=" + this.data + ", tag=" + this.tag + '}';
    }
}
