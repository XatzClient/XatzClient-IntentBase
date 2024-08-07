// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.Protocol1_15_2To1_16;
import nl.matsv.viabackwards.BukkitPlugin;
import us.myles.ViaVersion.bukkit.listeners.ViaBukkitListener;

public class FireExtinguishListener extends ViaBukkitListener
{
    public FireExtinguishListener(final BukkitPlugin plugin) {
        super((Plugin)plugin, (Class)Protocol1_15_2To1_16.class);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFireExtinguish(final PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }
        final Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }
        final Player player = event.getPlayer();
        if (!this.isOnPipe(player)) {
            return;
        }
        final Block relative = block.getRelative(event.getBlockFace());
        if (relative.getType() == Material.FIRE) {
            event.setCancelled(true);
            relative.setType(Material.AIR);
        }
    }
}
