// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_13to1_12_2.storage;

import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.type.Type;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.data.StoredObject;

public class TabCompleteTracker extends StoredObject
{
    private int transactionId;
    private String input;
    private String lastTabComplete;
    private long timeToSend;
    
    public TabCompleteTracker(final UserConnection user) {
        super(user);
    }
    
    public void sendPacketToServer() {
        if (this.lastTabComplete == null || this.timeToSend > System.currentTimeMillis()) {
            return;
        }
        final PacketWrapper wrapper = new PacketWrapper(1, null, this.getUser());
        wrapper.write(Type.STRING, this.lastTabComplete);
        wrapper.write(Type.BOOLEAN, false);
        wrapper.write(Type.OPTIONAL_POSITION, null);
        try {
            wrapper.sendToServer(Protocol1_13To1_12_2.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.lastTabComplete = null;
    }
    
    public int getTransactionId() {
        return this.transactionId;
    }
    
    public void setTransactionId(final int transactionId) {
        this.transactionId = transactionId;
    }
    
    public String getInput() {
        return this.input;
    }
    
    public void setInput(final String input) {
        this.input = input;
    }
    
    public String getLastTabComplete() {
        return this.lastTabComplete;
    }
    
    public void setLastTabComplete(final String lastTabComplete) {
        this.lastTabComplete = lastTabComplete;
    }
    
    public long getTimeToSend() {
        return this.timeToSend;
    }
    
    public void setTimeToSend(final long timeToSend) {
        this.timeToSend = timeToSend;
    }
}
