// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.javassist.compiler.ast;

import us.myles.viaversion.libs.javassist.compiler.CompileError;
import java.io.Serializable;

public abstract class ASTree implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    public ASTree getLeft() {
        return null;
    }
    
    public ASTree getRight() {
        return null;
    }
    
    public void setLeft(final ASTree _left) {
    }
    
    public void setRight(final ASTree _right) {
    }
    
    public abstract void accept(final Visitor p0) throws CompileError;
    
    @Override
    public String toString() {
        final StringBuffer sbuf = new StringBuffer();
        sbuf.append('<');
        sbuf.append(this.getTag());
        sbuf.append('>');
        return sbuf.toString();
    }
    
    protected String getTag() {
        final String name = this.getClass().getName();
        return name.substring(name.lastIndexOf(46) + 1);
    }
}
