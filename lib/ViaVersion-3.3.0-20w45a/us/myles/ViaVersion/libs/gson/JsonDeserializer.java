// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.gson;

import java.lang.reflect.Type;

public interface JsonDeserializer<T>
{
    T deserialize(final JsonElement p0, final Type p1, final JsonDeserializationContext p2) throws JsonParseException;
}