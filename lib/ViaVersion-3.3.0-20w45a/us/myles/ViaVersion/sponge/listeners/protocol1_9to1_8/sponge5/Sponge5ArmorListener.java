// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.sponge.listeners.protocol1_9to1_8.sponge5;

import us.myles.ViaVersion.api.Via;
import org.spongepowered.api.world.World;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.event.action.InteractEvent;
import org.spongepowered.api.event.Listener;
import java.util.Iterator;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.transaction.SlotTransaction;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.ArmorType;
import us.myles.ViaVersion.api.type.Type;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.PacketWrapper;
import org.spongepowered.api.item.inventory.ItemStack;
import java.util.Optional;
import org.spongepowered.api.entity.living.player.Player;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import us.myles.ViaVersion.SpongePlugin;
import java.util.UUID;
import us.myles.ViaVersion.sponge.listeners.ViaSpongeListener;

public class Sponge5ArmorListener extends ViaSpongeListener
{
    private static final UUID ARMOR_ATTRIBUTE;
    
    public Sponge5ArmorListener(final SpongePlugin plugin) {
        super(plugin, Protocol1_9To1_8.class);
    }
    
    public void sendArmorUpdate(final Player player) {
        if (!this.isOnPipe(player.getUniqueId())) {
            return;
        }
        int armor = 0;
        armor += this.calculate(player.getHelmet());
        armor += this.calculate(player.getChestplate());
        armor += this.calculate(player.getLeggings());
        armor += this.calculate(player.getBoots());
        final PacketWrapper wrapper = new PacketWrapper(75, null, this.getUserConnection(player.getUniqueId()));
        try {
            wrapper.write(Type.VAR_INT, this.getEntityId(player));
            wrapper.write(Type.INT, 1);
            wrapper.write(Type.STRING, "generic.armor");
            wrapper.write(Type.DOUBLE, 0.0);
            wrapper.write(Type.VAR_INT, 1);
            wrapper.write(Type.UUID, Sponge5ArmorListener.ARMOR_ATTRIBUTE);
            wrapper.write(Type.DOUBLE, (double)armor);
            wrapper.write(Type.BYTE, (Byte)0);
            wrapper.send(Protocol1_9To1_8.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private int calculate(final Optional<ItemStack> itemStack) {
        if (itemStack.isPresent()) {
            return ArmorType.findByType(itemStack.get().getItem().getType().getId()).getArmorPoints();
        }
        return 0;
    }
    
    @Listener
    public void onInventoryClick(final ClickInventoryEvent e, @Root final Player player) {
        for (final SlotTransaction transaction : e.getTransactions()) {
            if (ArmorType.isArmor(((ItemStackSnapshot)transaction.getFinal()).getType().getId()) || ArmorType.isArmor(((ItemStackSnapshot)e.getCursorTransaction().getFinal()).getType().getId())) {
                this.sendDelayedArmorUpdate(player);
                break;
            }
        }
    }
    
    @Listener
    public void onInteract(final InteractEvent event, @Root final Player player) {
        if (player.getItemInHand(HandTypes.MAIN_HAND).isPresent() && ArmorType.isArmor(player.getItemInHand(HandTypes.MAIN_HAND).get().getItem().getId())) {
            this.sendDelayedArmorUpdate(player);
        }
    }
    
    @Listener
    public void onJoin(final ClientConnectionEvent.Join e) {
        this.sendArmorUpdate(e.getTargetEntity());
    }
    
    @Listener
    public void onRespawn(final RespawnPlayerEvent e) {
        this.sendDelayedArmorUpdate(e.getTargetEntity());
    }
    
    @Listener
    public void onWorldChange(final MoveEntityEvent.Teleport e) {
        if (!(e.getTargetEntity() instanceof Player)) {
            return;
        }
        if (!((World)e.getFromTransform().getExtent()).getUniqueId().equals(((World)e.getToTransform().getExtent()).getUniqueId())) {
            this.sendArmorUpdate((Player)e.getTargetEntity());
        }
    }
    
    public void sendDelayedArmorUpdate(final Player player) {
        if (!this.isOnPipe(player.getUniqueId())) {
            return;
        }
        Via.getPlatform().runSync(new Runnable() {
            @Override
            public void run() {
                Sponge5ArmorListener.this.sendArmorUpdate(player);
            }
        });
    }
    
    static {
        ARMOR_ATTRIBUTE = UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150");
    }
}
