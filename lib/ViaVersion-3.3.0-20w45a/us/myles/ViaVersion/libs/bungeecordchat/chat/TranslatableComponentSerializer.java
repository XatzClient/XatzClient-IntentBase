// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.bungeecordchat.chat;

import us.myles.viaversion.libs.gson.JsonSerializationContext;
import us.myles.viaversion.libs.gson.JsonObject;
import java.util.Arrays;
import us.myles.viaversion.libs.gson.JsonParseException;
import us.myles.viaversion.libs.bungeecordchat.api.chat.BaseComponent;
import us.myles.viaversion.libs.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.viaversion.libs.gson.JsonDeserializer;
import us.myles.viaversion.libs.bungeecordchat.api.chat.TranslatableComponent;
import us.myles.viaversion.libs.gson.JsonSerializer;

public class TranslatableComponentSerializer extends BaseComponentSerializer implements JsonSerializer<TranslatableComponent>, JsonDeserializer<TranslatableComponent>
{
    @Override
    public TranslatableComponent deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        final TranslatableComponent component = new TranslatableComponent();
        final JsonObject object = json.getAsJsonObject();
        this.deserialize(object, component, context);
        if (!object.has("translate")) {
            throw new JsonParseException("Could not parse JSON: missing 'translate' property");
        }
        component.setTranslate(object.get("translate").getAsString());
        if (object.has("with")) {
            component.setWith(Arrays.asList((BaseComponent[])context.deserialize(object.get("with"), BaseComponent[].class)));
        }
        return component;
    }
    
    @Override
    public JsonElement serialize(final TranslatableComponent src, final Type typeOfSrc, final JsonSerializationContext context) {
        final JsonObject object = new JsonObject();
        this.serialize(object, src, context);
        object.addProperty("translate", src.getTranslate());
        if (src.getWith() != null) {
            object.add("with", context.serialize(src.getWith()));
        }
        return object;
    }
}
