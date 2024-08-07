// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.bukkit.providers;

import us.myles.ViaVersion.bukkit.util.NMSUtil;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import java.util.logging.Level;
import us.myles.ViaVersion.util.ReflectionUtil;
import org.bukkit.event.inventory.InventoryType;
import us.myles.ViaVersion.protocols.protocol1_12to1_11_1.storage.ItemTransaction;
import org.bukkit.entity.Player;
import us.myles.ViaVersion.protocols.base.ProtocolInfo;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.protocol.ProtocolVersion;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;
import us.myles.ViaVersion.api.data.UserConnection;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import us.myles.ViaVersion.bukkit.tasks.protocol1_12to1_11_1.BukkitInventoryUpdateTask;
import java.util.UUID;
import java.util.Map;
import us.myles.ViaVersion.protocols.protocol1_12to1_11_1.providers.InventoryQuickMoveProvider;

public class BukkitInventoryQuickMoveProvider extends InventoryQuickMoveProvider
{
    private final Map<UUID, BukkitInventoryUpdateTask> updateTasks;
    private final boolean supported;
    private Class<?> windowClickPacketClass;
    private Object clickTypeEnum;
    private Method nmsItemMethod;
    private Method craftPlayerHandle;
    private Field connection;
    private Method packetMethod;
    
    public BukkitInventoryQuickMoveProvider() {
        this.updateTasks = new ConcurrentHashMap<UUID, BukkitInventoryUpdateTask>();
        this.supported = this.isSupported();
        this.setupReflection();
    }
    
    @Override
    public boolean registerQuickMoveAction(final short windowId, final short slotId, final short actionId, final UserConnection userConnection) {
        if (!this.supported) {
            return false;
        }
        if (slotId < 0) {
            return false;
        }
        if (windowId == 0 && slotId >= 36 && slotId <= 44) {
            final int protocolId = ProtocolRegistry.SERVER_PROTOCOL;
            if (protocolId == ProtocolVersion.v1_8.getVersion()) {
                return false;
            }
        }
        final ProtocolInfo info = userConnection.getProtocolInfo();
        final UUID uuid = info.getUuid();
        BukkitInventoryUpdateTask updateTask = this.updateTasks.get(uuid);
        final boolean registered = updateTask != null;
        if (!registered) {
            updateTask = new BukkitInventoryUpdateTask(this, uuid);
            this.updateTasks.put(uuid, updateTask);
        }
        updateTask.addItem(windowId, slotId, actionId);
        if (!registered) {
            Via.getPlatform().runSync(updateTask, 5L);
        }
        return true;
    }
    
    public Object buildWindowClickPacket(final Player p, final ItemTransaction storage) {
        if (!this.supported) {
            return null;
        }
        final InventoryView inv = p.getOpenInventory();
        short slotId = storage.getSlotId();
        final Inventory tinv = inv.getTopInventory();
        final InventoryType tinvtype = (tinv == null) ? null : tinv.getType();
        if (tinvtype != null) {
            final int protocolId = ProtocolRegistry.SERVER_PROTOCOL;
            if (protocolId == ProtocolVersion.v1_8.getVersion() && tinvtype == InventoryType.BREWING && slotId >= 5 && slotId <= 40) {
                --slotId;
            }
        }
        ItemStack itemstack = null;
        if (slotId <= inv.countSlots()) {
            itemstack = inv.getItem((int)slotId);
        }
        else {
            final String cause = "Too many inventory slots: slotId: " + slotId + " invSlotCount: " + inv.countSlots() + " invType: " + inv.getType() + " topInvType: " + tinvtype;
            Via.getPlatform().getLogger().severe("Failed to get an item to create a window click packet. Please report this issue to the ViaVersion Github: " + cause);
        }
        Object packet = null;
        try {
            packet = this.windowClickPacketClass.getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
            final Object nmsItem = (itemstack == null) ? null : this.nmsItemMethod.invoke(null, itemstack);
            ReflectionUtil.set(packet, "a", (int)storage.getWindowId());
            ReflectionUtil.set(packet, "slot", (int)slotId);
            ReflectionUtil.set(packet, "button", 0);
            ReflectionUtil.set(packet, "d", storage.getActionId());
            ReflectionUtil.set(packet, "item", nmsItem);
            final int protocolId2 = ProtocolRegistry.SERVER_PROTOCOL;
            if (protocolId2 == ProtocolVersion.v1_8.getVersion()) {
                ReflectionUtil.set(packet, "shift", 1);
            }
            else if (protocolId2 >= ProtocolVersion.v1_9.getVersion()) {
                ReflectionUtil.set(packet, "shift", this.clickTypeEnum);
            }
        }
        catch (Exception e) {
            Via.getPlatform().getLogger().log(Level.SEVERE, "Failed to create a window click packet. Please report this issue to the ViaVersion Github: " + e.getMessage(), e);
        }
        return packet;
    }
    
    public boolean sendPacketToServer(final Player p, final Object packet) {
        if (packet == null) {
            return true;
        }
        try {
            final Object entityPlayer = this.craftPlayerHandle.invoke(p, new Object[0]);
            final Object playerConnection = this.connection.get(entityPlayer);
            this.packetMethod.invoke(playerConnection, packet);
        }
        catch (IllegalAccessException | InvocationTargetException ex2) {
            final ReflectiveOperationException ex;
            final ReflectiveOperationException e = ex;
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public void onTaskExecuted(final UUID uuid) {
        this.updateTasks.remove(uuid);
    }
    
    private void setupReflection() {
        if (!this.supported) {
            return;
        }
        try {
            this.windowClickPacketClass = NMSUtil.nms("PacketPlayInWindowClick");
            final int protocolId = ProtocolRegistry.SERVER_PROTOCOL;
            if (protocolId >= ProtocolVersion.v1_9.getVersion()) {
                final Class<?> eclassz = NMSUtil.nms("InventoryClickType");
                final Object[] constants = (Object[])eclassz.getEnumConstants();
                this.clickTypeEnum = constants[1];
            }
            final Class<?> craftItemStack = NMSUtil.obc("inventory.CraftItemStack");
            this.nmsItemMethod = craftItemStack.getDeclaredMethod("asNMSCopy", ItemStack.class);
        }
        catch (Exception e) {
            throw new RuntimeException("Couldn't find required inventory classes", e);
        }
        try {
            this.craftPlayerHandle = NMSUtil.obc("entity.CraftPlayer").getDeclaredMethod("getHandle", (Class<?>[])new Class[0]);
        }
        catch (NoSuchMethodException | ClassNotFoundException ex4) {
            final ReflectiveOperationException ex;
            final ReflectiveOperationException e2 = ex;
            throw new RuntimeException("Couldn't find CraftPlayer", e2);
        }
        try {
            this.connection = NMSUtil.nms("EntityPlayer").getDeclaredField("playerConnection");
        }
        catch (NoSuchFieldException | ClassNotFoundException ex5) {
            final ReflectiveOperationException ex2;
            final ReflectiveOperationException e2 = ex2;
            throw new RuntimeException("Couldn't find Player Connection", e2);
        }
        try {
            this.packetMethod = NMSUtil.nms("PlayerConnection").getDeclaredMethod("a", this.windowClickPacketClass);
        }
        catch (NoSuchMethodException | ClassNotFoundException ex6) {
            final ReflectiveOperationException ex3;
            final ReflectiveOperationException e2 = ex3;
            throw new RuntimeException("Couldn't find CraftPlayer", e2);
        }
    }
    
    private boolean isSupported() {
        final int protocolId = ProtocolRegistry.SERVER_PROTOCOL;
        return protocolId >= ProtocolVersion.v1_8.getVersion() && protocolId <= ProtocolVersion.v1_11_1.getVersion();
    }
}
