// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.commands.defaultsubs;

import java.util.Iterator;
import java.util.Map;
import java.util.HashSet;
import us.myles.ViaVersion.api.Via;
import java.util.Set;
import java.util.TreeMap;
import us.myles.ViaVersion.api.protocol.ProtocolVersion;
import java.util.Comparator;
import us.myles.ViaVersion.api.command.ViaCommandSender;
import us.myles.ViaVersion.api.command.ViaSubCommand;

public class ListSubCmd extends ViaSubCommand
{
    @Override
    public String name() {
        return "list";
    }
    
    @Override
    public String description() {
        return "Shows lists of the versions from logged in players";
    }
    
    @Override
    public String usage() {
        return "list";
    }
    
    @Override
    public boolean execute(final ViaCommandSender sender, final String[] args) {
        final Map<ProtocolVersion, Set<String>> playerVersions = new TreeMap<ProtocolVersion, Set<String>>(new Comparator<ProtocolVersion>() {
            @Override
            public int compare(final ProtocolVersion o1, final ProtocolVersion o2) {
                return ProtocolVersion.getIndex(o2) - ProtocolVersion.getIndex(o1);
            }
        });
        for (final ViaCommandSender p : Via.getPlatform().getOnlinePlayers()) {
            final int playerVersion = Via.getAPI().getPlayerVersion(p.getUUID());
            final ProtocolVersion key = ProtocolVersion.getProtocol(playerVersion);
            if (!playerVersions.containsKey(key)) {
                playerVersions.put(key, new HashSet<String>());
            }
            playerVersions.get(key).add(p.getName());
        }
        for (final Map.Entry<ProtocolVersion, Set<String>> entry : playerVersions.entrySet()) {
            this.sendMessage(sender, "&8[&6%s&8] (&7%d&8): &b%s", entry.getKey().getName(), entry.getValue().size(), entry.getValue());
        }
        playerVersions.clear();
        return true;
    }
}
