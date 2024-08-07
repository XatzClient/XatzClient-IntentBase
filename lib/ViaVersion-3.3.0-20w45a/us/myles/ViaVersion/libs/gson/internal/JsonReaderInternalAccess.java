// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.gson.internal;

import java.io.IOException;
import us.myles.viaversion.libs.gson.stream.JsonReader;

public abstract class JsonReaderInternalAccess
{
    public static JsonReaderInternalAccess INSTANCE;
    
    public abstract void promoteNameToValue(final JsonReader p0) throws IOException;
}
