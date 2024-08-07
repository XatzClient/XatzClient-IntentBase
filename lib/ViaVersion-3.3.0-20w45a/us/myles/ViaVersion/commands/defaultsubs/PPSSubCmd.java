// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.commands.defaultsubs;

import java.util.Iterator;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.protocol.ProtocolVersion;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashSet;
import us.myles.ViaVersion.api.Via;
import java.util.Set;
import java.util.HashMap;
import us.myles.ViaVersion.api.command.ViaCommandSender;
import us.myles.ViaVersion.api.command.ViaSubCommand;

public class PPSSubCmd extends ViaSubCommand
{
    @Override
    public String name() {
        return "pps";
    }
    
    @Override
    public String description() {
        return "Shows the packets per second of online players";
    }
    
    @Override
    public String usage() {
        return "pps";
    }
    
    @Override
    public boolean execute(final ViaCommandSender sender, final String[] args) {
        final Map<Integer, Set<String>> playerVersions = new HashMap<Integer, Set<String>>();
        int totalPackets = 0;
        int clients = 0;
        long max = 0L;
        for (final ViaCommandSender p : Via.getPlatform().getOnlinePlayers()) {
            final int playerVersion = Via.getAPI().getPlayerVersion(p.getUUID());
            if (!playerVersions.containsKey(playerVersion)) {
                playerVersions.put(playerVersion, new HashSet<String>());
            }
            final UserConnection uc = Via.getManager().getConnection(p.getUUID());
            if (uc != null && uc.getPacketsPerSecond() > -1L) {
                playerVersions.get(playerVersion).add(p.getName() + " (" + uc.getPacketsPerSecond() + " PPS)");
                totalPackets += (int)uc.getPacketsPerSecond();
                if (uc.getPacketsPerSecond() > max) {
                    max = uc.getPacketsPerSecond();
                }
                ++clients;
            }
        }
        final Map<Integer, Set<String>> sorted = new TreeMap<Integer, Set<String>>(playerVersions);
        this.sendMessage(sender, "&4Live Packets Per Second", new Object[0]);
        if (clients > 1) {
            this.sendMessage(sender, "&cAverage: &f" + totalPackets / clients, new Object[0]);
            this.sendMessage(sender, "&cHighest: &f" + max, new Object[0]);
        }
        if (clients == 0) {
            this.sendMessage(sender, "&cNo clients to display.", new Object[0]);
        }
        for (final Map.Entry<Integer, Set<String>> entry : sorted.entrySet()) {
            this.sendMessage(sender, "&8[&6%s&8]: &b%s", ProtocolVersion.getProtocol(entry.getKey()).getName(), entry.getValue());
        }
        sorted.clear();
        return true;
    }
}
