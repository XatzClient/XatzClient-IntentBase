// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.javassist.tools;

import us.myles.viaversion.libs.javassist.CtClass;
import us.myles.viaversion.libs.javassist.bytecode.analysis.FramePrinter;
import us.myles.viaversion.libs.javassist.ClassPool;

public class framedump
{
    private framedump() {
    }
    
    public static void main(final String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: java javassist.tools.framedump <fully-qualified class name>");
            return;
        }
        final ClassPool pool = ClassPool.getDefault();
        final CtClass clazz = pool.get(args[0]);
        System.out.println("Frame Dump of " + clazz.getName() + ":");
        FramePrinter.print(clazz, System.out);
    }
}
