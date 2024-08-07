// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.bungeecordchat.chat;

import us.myles.viaversion.libs.gson.JsonSerializationContext;
import us.myles.viaversion.libs.gson.JsonObject;
import us.myles.viaversion.libs.bungeecordchat.api.chat.BaseComponent;
import us.myles.viaversion.libs.gson.JsonParseException;
import us.myles.viaversion.libs.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.viaversion.libs.gson.JsonDeserializer;
import us.myles.viaversion.libs.bungeecordchat.api.chat.KeybindComponent;
import us.myles.viaversion.libs.gson.JsonSerializer;

public class KeybindComponentSerializer extends BaseComponentSerializer implements JsonSerializer<KeybindComponent>, JsonDeserializer<KeybindComponent>
{
    @Override
    public KeybindComponent deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        final JsonObject object = json.getAsJsonObject();
        if (!object.has("keybind")) {
            throw new JsonParseException("Could not parse JSON: missing 'keybind' property");
        }
        final KeybindComponent component = new KeybindComponent();
        this.deserialize(object, component, context);
        component.setKeybind(object.get("keybind").getAsString());
        return component;
    }
    
    @Override
    public JsonElement serialize(final KeybindComponent src, final Type typeOfSrc, final JsonSerializationContext context) {
        final JsonObject object = new JsonObject();
        this.serialize(object, src, context);
        object.addProperty("keybind", src.getKeybind());
        return object;
    }
}
