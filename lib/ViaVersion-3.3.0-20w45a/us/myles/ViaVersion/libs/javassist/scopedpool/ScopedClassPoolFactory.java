// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.javassist.scopedpool;

import us.myles.viaversion.libs.javassist.ClassPool;

public interface ScopedClassPoolFactory
{
    ScopedClassPool create(final ClassLoader p0, final ClassPool p1, final ScopedClassPoolRepository p2);
    
    ScopedClassPool create(final ClassPool p0, final ScopedClassPoolRepository p1);
}
