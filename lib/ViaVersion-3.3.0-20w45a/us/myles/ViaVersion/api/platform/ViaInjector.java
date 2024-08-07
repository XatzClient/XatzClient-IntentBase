// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.platform;

import us.myles.viaversion.libs.gson.JsonObject;

public interface ViaInjector
{
    void inject() throws Exception;
    
    void uninject() throws Exception;
    
    int getServerProtocolVersion() throws Exception;
    
    String getEncoderName();
    
    String getDecoderName();
    
    JsonObject getDump();
}
