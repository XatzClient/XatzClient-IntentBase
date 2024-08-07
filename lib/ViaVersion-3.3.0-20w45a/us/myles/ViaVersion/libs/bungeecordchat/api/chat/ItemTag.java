// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.bungeecordchat.api.chat;

import us.myles.viaversion.libs.gson.JsonSerializationContext;
import us.myles.viaversion.libs.gson.JsonParseException;
import us.myles.viaversion.libs.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.viaversion.libs.gson.JsonDeserializer;
import us.myles.viaversion.libs.gson.JsonSerializer;

public final class ItemTag
{
    private final String nbt;
    
    private ItemTag(final String nbt) {
        this.nbt = nbt;
    }
    
    public static ItemTag ofNbt(final String nbt) {
        return new ItemTag(nbt);
    }
    
    private static Builder builder() {
        return new Builder();
    }
    
    @Override
    public String toString() {
        return "ItemTag(nbt=" + this.getNbt() + ")";
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ItemTag)) {
            return false;
        }
        final ItemTag other = (ItemTag)o;
        final Object this$nbt = this.getNbt();
        final Object other$nbt = other.getNbt();
        if (this$nbt == null) {
            if (other$nbt == null) {
                return true;
            }
        }
        else if (this$nbt.equals(other$nbt)) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $nbt = this.getNbt();
        result = result * 59 + (($nbt == null) ? 43 : $nbt.hashCode());
        return result;
    }
    
    public String getNbt() {
        return this.nbt;
    }
    
    private static class Builder
    {
        private String nbt;
        
        Builder() {
        }
        
        private Builder nbt(final String nbt) {
            this.nbt = nbt;
            return this;
        }
        
        private ItemTag build() {
            return new ItemTag(this.nbt, null);
        }
        
        @Override
        public String toString() {
            return "ItemTag.Builder(nbt=" + this.nbt + ")";
        }
    }
    
    public static class Serializer implements JsonSerializer<ItemTag>, JsonDeserializer<ItemTag>
    {
        @Override
        public ItemTag deserialize(final JsonElement element, final Type type, final JsonDeserializationContext context) throws JsonParseException {
            return ItemTag.ofNbt(element.getAsJsonPrimitive().getAsString());
        }
        
        @Override
        public JsonElement serialize(final ItemTag itemTag, final Type type, final JsonSerializationContext context) {
            return context.serialize(itemTag.getNbt());
        }
    }
}
