// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.javassist.tools.rmi;

public class RemoteException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
    public RemoteException(final String msg) {
        super(msg);
    }
    
    public RemoteException(final Exception e) {
        super("by " + e.toString());
    }
}
