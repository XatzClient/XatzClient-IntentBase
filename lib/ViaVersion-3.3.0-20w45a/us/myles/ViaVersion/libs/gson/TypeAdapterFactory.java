// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.gson;

import us.myles.viaversion.libs.gson.reflect.TypeToken;

public interface TypeAdapterFactory
{
     <T> TypeAdapter<T> create(final Gson p0, final TypeToken<T> p1);
}
