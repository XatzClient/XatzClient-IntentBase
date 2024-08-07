// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.javassist.tools.reflect;

import us.myles.viaversion.libs.javassist.NotFoundException;
import us.myles.viaversion.libs.javassist.CannotCompileException;
import us.myles.viaversion.libs.javassist.Translator;
import us.myles.viaversion.libs.javassist.ClassPool;

public class Loader extends us.myles.viaversion.libs.javassist.Loader
{
    protected Reflection reflection;
    
    public static void main(final String[] args) throws Throwable {
        final Loader cl = new Loader();
        cl.run(args);
    }
    
    public Loader() throws CannotCompileException, NotFoundException {
        this.delegateLoadingOf("us.myles.viaversion.libs.javassist.tools.reflect.Loader");
        this.reflection = new Reflection();
        final ClassPool pool = ClassPool.getDefault();
        this.addTranslator(pool, this.reflection);
    }
    
    public boolean makeReflective(final String clazz, final String metaobject, final String metaclass) throws CannotCompileException, NotFoundException {
        return this.reflection.makeReflective(clazz, metaobject, metaclass);
    }
}
