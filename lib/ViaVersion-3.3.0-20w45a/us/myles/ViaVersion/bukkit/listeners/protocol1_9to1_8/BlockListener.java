// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.bukkit.listeners.protocol1_9to1_8;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.block.Block;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;
import org.bukkit.event.block.BlockPlaceEvent;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import org.bukkit.plugin.Plugin;
import us.myles.ViaVersion.bukkit.listeners.ViaBukkitListener;

public class BlockListener extends ViaBukkitListener
{
    public BlockListener(final Plugin plugin) {
        super(plugin, Protocol1_9To1_8.class);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void placeBlock(final BlockPlaceEvent e) {
        if (this.isOnPipe(e.getPlayer())) {
            final Block b = e.getBlockPlaced();
            this.getUserConnection(e.getPlayer()).get(EntityTracker1_9.class).addBlockInteraction(new Position(b.getX(), (short)b.getY(), b.getZ()));
        }
    }
}
