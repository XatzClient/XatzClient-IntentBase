// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.gson;

import java.lang.reflect.Type;

public interface JsonSerializationContext
{
    JsonElement serialize(final Object p0);
    
    JsonElement serialize(final Object p0, final Type p1);
}
