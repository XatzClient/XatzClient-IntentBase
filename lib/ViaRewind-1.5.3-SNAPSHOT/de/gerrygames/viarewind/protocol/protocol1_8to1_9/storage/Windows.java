// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage;

import us.myles.ViaVersion.api.protocol.Protocol;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import us.myles.ViaVersion.api.type.Type;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.minecraft.item.Item;
import java.util.HashMap;
import us.myles.ViaVersion.api.data.StoredObject;

public class Windows extends StoredObject
{
    private HashMap<Short, String> types;
    private HashMap<Short, Item[]> brewingItems;
    
    public Windows(final UserConnection user) {
        super(user);
        this.types = new HashMap<Short, String>();
        this.brewingItems = new HashMap<Short, Item[]>();
    }
    
    public String get(final short windowId) {
        return this.types.get(windowId);
    }
    
    public void put(final short windowId, final String type) {
        this.types.put(windowId, type);
    }
    
    public void remove(final short windowId) {
        this.types.remove(windowId);
        this.brewingItems.remove(windowId);
    }
    
    public Item[] getBrewingItems(final short windowId) {
        return this.brewingItems.computeIfAbsent(windowId, key -> new Item[] { new Item(), new Item(), new Item(), new Item() });
    }
    
    public static void updateBrewingStand(final UserConnection user, final Item blazePowder, final short windowId) {
        if (blazePowder != null && blazePowder.getIdentifier() != 377) {
            return;
        }
        final int amount = (blazePowder == null) ? 0 : blazePowder.getAmount();
        final PacketWrapper openWindow = new PacketWrapper(45, (ByteBuf)null, user);
        openWindow.write(Type.UNSIGNED_BYTE, (Object)windowId);
        openWindow.write(Type.STRING, (Object)"minecraft:brewing_stand");
        openWindow.write(Type.STRING, (Object)("[{\"translate\":\"container.brewing\"},{\"text\":\": \",\"color\":\"dark_gray\"},{\"text\":\"§4" + amount + " \",\"color\":\"dark_red\"},{\"translate\":\"item.blazePowder.name\",\"color\":\"dark_red\"}]"));
        openWindow.write(Type.UNSIGNED_BYTE, (Object)420);
        PacketUtil.sendPacket(openWindow, Protocol1_8TO1_9.class);
        final Item[] items = ((Windows)user.get((Class)Windows.class)).getBrewingItems(windowId);
        for (int i = 0; i < items.length; ++i) {
            final PacketWrapper setSlot = new PacketWrapper(47, (ByteBuf)null, user);
            setSlot.write(Type.BYTE, (Object)(byte)windowId);
            setSlot.write((Type)Type.SHORT, (Object)(short)i);
            setSlot.write(Type.ITEM, (Object)items[i]);
            PacketUtil.sendPacket(setSlot, Protocol1_8TO1_9.class);
        }
    }
}
