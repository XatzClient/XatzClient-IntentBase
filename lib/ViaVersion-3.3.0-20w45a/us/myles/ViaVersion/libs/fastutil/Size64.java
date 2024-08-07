// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.fastutil;

public interface Size64
{
    long size64();
    
    @Deprecated
    default int size() {
        return (int)Math.min(2147483647L, this.size64());
    }
}
