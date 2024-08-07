// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_9to1_8.chat;

import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.viaversion.libs.gson.JsonArray;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.viaversion.libs.gson.JsonObject;

public class ChatRewriter
{
    public static void toClient(final JsonObject obj, final UserConnection user) {
        if (obj.get("translate") != null && obj.get("translate").getAsString().equals("gameMode.changed")) {
            final String gameMode = user.get(EntityTracker1_9.class).getGameMode().getText();
            final JsonObject gameModeObject = new JsonObject();
            gameModeObject.addProperty("text", gameMode);
            gameModeObject.addProperty("color", "gray");
            gameModeObject.addProperty("italic", true);
            final JsonArray array = new JsonArray();
            array.add(gameModeObject);
            obj.add("with", array);
        }
    }
}
