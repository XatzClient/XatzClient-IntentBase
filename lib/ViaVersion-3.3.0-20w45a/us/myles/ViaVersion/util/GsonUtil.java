// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.util;

import us.myles.viaversion.libs.gson.GsonBuilder;
import us.myles.viaversion.libs.gson.Gson;
import us.myles.viaversion.libs.gson.JsonParser;

public final class GsonUtil
{
    private static final JsonParser JSON_PARSER;
    private static final Gson GSON;
    
    public static Gson getGson() {
        return GsonUtil.GSON;
    }
    
    public static GsonBuilder getGsonBuilder() {
        return new GsonBuilder();
    }
    
    public static JsonParser getJsonParser() {
        return GsonUtil.JSON_PARSER;
    }
    
    static {
        JSON_PARSER = new JsonParser();
        GSON = getGsonBuilder().create();
    }
}
