// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.type.types;

import us.myles.viaversion.libs.gson.JsonSyntaxException;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.util.GsonUtil;
import io.netty.buffer.ByteBuf;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.ViaVersion.api.type.Type;

public class ComponentType extends Type<JsonElement>
{
    private static final StringType STRING_TAG;
    
    public ComponentType() {
        super(JsonElement.class);
    }
    
    @Override
    public JsonElement read(final ByteBuf buffer) throws Exception {
        final String s = ComponentType.STRING_TAG.read(buffer);
        try {
            return GsonUtil.getJsonParser().parse(s);
        }
        catch (JsonSyntaxException e) {
            Via.getPlatform().getLogger().severe("Error when trying to parse json: " + s);
            throw e;
        }
    }
    
    @Override
    public void write(final ByteBuf buffer, final JsonElement object) throws Exception {
        ComponentType.STRING_TAG.write(buffer, object.toString());
    }
    
    static {
        STRING_TAG = new StringType(262144);
    }
}
