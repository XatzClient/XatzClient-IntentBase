// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.javassist.util.proxy;

public interface ProxyObject extends Proxy
{
    void setHandler(final MethodHandler p0);
    
    MethodHandler getHandler();
}
