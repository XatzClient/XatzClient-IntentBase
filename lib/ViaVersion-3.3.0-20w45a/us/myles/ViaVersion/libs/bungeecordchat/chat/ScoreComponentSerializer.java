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
import us.myles.viaversion.libs.bungeecordchat.api.chat.ScoreComponent;
import us.myles.viaversion.libs.gson.JsonSerializer;

public class ScoreComponentSerializer extends BaseComponentSerializer implements JsonSerializer<ScoreComponent>, JsonDeserializer<ScoreComponent>
{
    @Override
    public ScoreComponent deserialize(final JsonElement element, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        final JsonObject json = element.getAsJsonObject();
        if (!json.has("score")) {
            throw new JsonParseException("Could not parse JSON: missing 'score' property");
        }
        final JsonObject score = json.get("score").getAsJsonObject();
        if (!score.has("name") || !score.has("objective")) {
            throw new JsonParseException("A score component needs at least a name and an objective");
        }
        final String name = score.get("name").getAsString();
        final String objective = score.get("objective").getAsString();
        final ScoreComponent component = new ScoreComponent(name, objective);
        if (score.has("value") && !score.get("value").getAsString().isEmpty()) {
            component.setValue(score.get("value").getAsString());
        }
        this.deserialize(json, component, context);
        return component;
    }
    
    @Override
    public JsonElement serialize(final ScoreComponent component, final Type type, final JsonSerializationContext context) {
        final JsonObject root = new JsonObject();
        this.serialize(root, component, context);
        final JsonObject json = new JsonObject();
        json.addProperty("name", component.getName());
        json.addProperty("objective", component.getObjective());
        json.addProperty("value", component.getValue());
        root.add("score", json);
        return root;
    }
}
