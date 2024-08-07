// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage;

import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.data.StoredObject;

public class CompressionSendStorage extends StoredObject
{
    private boolean compressionSend;
    
    public CompressionSendStorage(final UserConnection user) {
        super(user);
        this.compressionSend = false;
    }
    
    public boolean isCompressionSend() {
        return this.compressionSend;
    }
    
    public void setCompressionSend(final boolean compressionSend) {
        this.compressionSend = compressionSend;
    }
    
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CompressionSendStorage)) {
            return false;
        }
        final CompressionSendStorage other = (CompressionSendStorage)o;
        return other.canEqual(this) && this.isCompressionSend() == other.isCompressionSend();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof CompressionSendStorage;
    }
    
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isCompressionSend() ? 79 : 97);
        return result;
    }
    
    public String toString() {
        return "CompressionSendStorage(compressionSend=" + this.isCompressionSend() + ")";
    }
}
