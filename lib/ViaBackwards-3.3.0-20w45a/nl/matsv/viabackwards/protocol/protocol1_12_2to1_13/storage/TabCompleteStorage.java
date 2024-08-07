// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.storage;

import java.util.HashMap;
import us.myles.ViaVersion.api.data.UserConnection;
import java.util.UUID;
import java.util.Map;
import us.myles.ViaVersion.api.data.StoredObject;

public class TabCompleteStorage extends StoredObject
{
    public int lastId;
    public String lastRequest;
    public boolean lastAssumeCommand;
    public Map<UUID, String> usernames;
    
    public TabCompleteStorage(final UserConnection user) {
        super(user);
        this.usernames = new HashMap<UUID, String>();
    }
}
