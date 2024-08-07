// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.bungeecordchat.chat;

import us.myles.viaversion.libs.bungeecordchat.api.chat.ItemTag;
import us.myles.viaversion.libs.bungeecordchat.api.chat.hover.content.ItemSerializer;
import us.myles.viaversion.libs.bungeecordchat.api.chat.hover.content.Item;
import us.myles.viaversion.libs.bungeecordchat.api.chat.hover.content.TextSerializer;
import us.myles.viaversion.libs.bungeecordchat.api.chat.hover.content.Text;
import us.myles.viaversion.libs.bungeecordchat.api.chat.hover.content.EntitySerializer;
import us.myles.viaversion.libs.bungeecordchat.api.chat.hover.content.Entity;
import us.myles.viaversion.libs.gson.GsonBuilder;
import us.myles.viaversion.libs.gson.JsonParseException;
import us.myles.viaversion.libs.gson.JsonObject;
import us.myles.viaversion.libs.bungeecordchat.api.chat.SelectorComponent;
import us.myles.viaversion.libs.bungeecordchat.api.chat.ScoreComponent;
import us.myles.viaversion.libs.bungeecordchat.api.chat.KeybindComponent;
import us.myles.viaversion.libs.bungeecordchat.api.chat.TranslatableComponent;
import us.myles.viaversion.libs.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import us.myles.viaversion.libs.bungeecordchat.api.chat.TextComponent;
import us.myles.viaversion.libs.gson.JsonElement;
import java.util.Set;
import us.myles.viaversion.libs.gson.Gson;
import us.myles.viaversion.libs.gson.JsonParser;
import us.myles.viaversion.libs.bungeecordchat.api.chat.BaseComponent;
import us.myles.viaversion.libs.gson.JsonDeserializer;

public class ComponentSerializer implements JsonDeserializer<BaseComponent>
{
    private static final JsonParser JSON_PARSER;
    private static final Gson gson;
    public static final ThreadLocal<Set<BaseComponent>> serializedComponents;
    
    public static BaseComponent[] parse(final String json) {
        final JsonElement jsonElement = ComponentSerializer.JSON_PARSER.parse(json);
        if (jsonElement.isJsonArray()) {
            return ComponentSerializer.gson.fromJson(jsonElement, BaseComponent[].class);
        }
        return new BaseComponent[] { ComponentSerializer.gson.fromJson(jsonElement, BaseComponent.class) };
    }
    
    public static String toString(final Object object) {
        return ComponentSerializer.gson.toJson(object);
    }
    
    public static String toString(final BaseComponent component) {
        return ComponentSerializer.gson.toJson(component);
    }
    
    public static String toString(final BaseComponent... components) {
        if (components.length == 1) {
            return ComponentSerializer.gson.toJson(components[0]);
        }
        return ComponentSerializer.gson.toJson(new TextComponent(components));
    }
    
    @Override
    public BaseComponent deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonPrimitive()) {
            return new TextComponent(json.getAsString());
        }
        final JsonObject object = json.getAsJsonObject();
        if (object.has("translate")) {
            return context.deserialize(json, TranslatableComponent.class);
        }
        if (object.has("keybind")) {
            return context.deserialize(json, KeybindComponent.class);
        }
        if (object.has("score")) {
            return context.deserialize(json, ScoreComponent.class);
        }
        if (object.has("selector")) {
            return context.deserialize(json, SelectorComponent.class);
        }
        return context.deserialize(json, TextComponent.class);
    }
    
    static {
        JSON_PARSER = new JsonParser();
        gson = new GsonBuilder().registerTypeAdapter(BaseComponent.class, new ComponentSerializer()).registerTypeAdapter(TextComponent.class, new TextComponentSerializer()).registerTypeAdapter(TranslatableComponent.class, new TranslatableComponentSerializer()).registerTypeAdapter(KeybindComponent.class, new KeybindComponentSerializer()).registerTypeAdapter(ScoreComponent.class, new ScoreComponentSerializer()).registerTypeAdapter(SelectorComponent.class, new SelectorComponentSerializer()).registerTypeAdapter(Entity.class, new EntitySerializer()).registerTypeAdapter(Text.class, new TextSerializer()).registerTypeAdapter(Item.class, new ItemSerializer()).registerTypeAdapter(ItemTag.class, new ItemTag.Serializer()).create();
        serializedComponents = new ThreadLocal<Set<BaseComponent>>();
    }
}
