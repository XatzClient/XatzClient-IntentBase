// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.javassist.bytecode;

import us.myles.viaversion.libs.javassist.CannotCompileException;

public class DuplicateMemberException extends CannotCompileException
{
    private static final long serialVersionUID = 1L;
    
    public DuplicateMemberException(final String msg) {
        super(msg);
    }
}
