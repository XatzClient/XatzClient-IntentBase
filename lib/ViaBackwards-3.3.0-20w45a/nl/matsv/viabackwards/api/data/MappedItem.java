// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.api.data;

import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ChatRewriter;
import us.myles.viaversion.libs.bungeecordchat.api.ChatColor;

public class MappedItem
{
    private final int id;
    private final String jsonName;
    
    public MappedItem(final int id, final String name) {
        this.id = id;
        this.jsonName = ChatRewriter.legacyTextToJson(ChatColor.RESET + name).toString();
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getJsonName() {
        return this.jsonName;
    }
}
