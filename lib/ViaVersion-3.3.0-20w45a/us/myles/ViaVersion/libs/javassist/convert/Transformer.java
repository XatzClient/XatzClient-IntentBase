// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.javassist.convert;

import us.myles.viaversion.libs.javassist.bytecode.BadBytecode;
import us.myles.viaversion.libs.javassist.bytecode.CodeIterator;
import us.myles.viaversion.libs.javassist.CannotCompileException;
import us.myles.viaversion.libs.javassist.bytecode.MethodInfo;
import us.myles.viaversion.libs.javassist.CtClass;
import us.myles.viaversion.libs.javassist.bytecode.CodeAttribute;
import us.myles.viaversion.libs.javassist.bytecode.ConstPool;
import us.myles.viaversion.libs.javassist.bytecode.Opcode;

public abstract class Transformer implements Opcode
{
    private Transformer next;
    
    public Transformer(final Transformer t) {
        this.next = t;
    }
    
    public Transformer getNext() {
        return this.next;
    }
    
    public void initialize(final ConstPool cp, final CodeAttribute attr) {
    }
    
    public void initialize(final ConstPool cp, final CtClass clazz, final MethodInfo minfo) throws CannotCompileException {
        this.initialize(cp, minfo.getCodeAttribute());
    }
    
    public void clean() {
    }
    
    public abstract int transform(final CtClass p0, final int p1, final CodeIterator p2, final ConstPool p3) throws CannotCompileException, BadBytecode;
    
    public int extraLocals() {
        return 0;
    }
    
    public int extraStack() {
        return 0;
    }
}
