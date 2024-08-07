// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.bungeecordchat.api.chat.hover.content;

import us.myles.viaversion.libs.gson.JsonSerializationContext;
import us.myles.viaversion.libs.gson.JsonParseException;
import us.myles.viaversion.libs.bungeecordchat.api.chat.BaseComponent;
import us.myles.viaversion.libs.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.viaversion.libs.gson.JsonDeserializer;
import us.myles.viaversion.libs.gson.JsonSerializer;

public class TextSerializer implements JsonSerializer<Text>, JsonDeserializer<Text>
{
    @Override
    public Text deserialize(final JsonElement element, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        if (element.isJsonArray()) {
            return new Text(context.deserialize(element, BaseComponent[].class));
        }
        if (element.isJsonPrimitive()) {
            return new Text(element.getAsJsonPrimitive().getAsString());
        }
        return new Text(new BaseComponent[] { context.deserialize(element, BaseComponent.class) });
    }
    
    @Override
    public JsonElement serialize(final Text content, final Type type, final JsonSerializationContext context) {
        return context.serialize(content.getValue());
    }
}
