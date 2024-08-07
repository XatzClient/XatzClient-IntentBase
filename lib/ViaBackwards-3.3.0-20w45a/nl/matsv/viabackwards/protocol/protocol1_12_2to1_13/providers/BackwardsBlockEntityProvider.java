// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.providers;

import us.myles.viaversion.libs.opennbt.tag.builtin.IntTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.storage.BackwardsBlockStorage;
import nl.matsv.viabackwards.ViaBackwards;
import us.myles.ViaVersion.api.Via;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.data.UserConnection;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers.PistonHandler;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers.SpawnerHandler;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers.SkullHandler;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers.BannerHandler;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers.BedHandler;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers.FlowerPotHandler;
import java.util.HashMap;
import java.util.Map;
import us.myles.ViaVersion.api.platform.providers.Provider;

public class BackwardsBlockEntityProvider implements Provider
{
    private final Map<String, BackwardsBlockEntityHandler> handlers;
    
    public BackwardsBlockEntityProvider() {
        (this.handlers = new HashMap<String, BackwardsBlockEntityHandler>()).put("minecraft:flower_pot", new FlowerPotHandler());
        this.handlers.put("minecraft:bed", new BedHandler());
        this.handlers.put("minecraft:banner", new BannerHandler());
        this.handlers.put("minecraft:skull", new SkullHandler());
        this.handlers.put("minecraft:mob_spawner", new SpawnerHandler());
        this.handlers.put("minecraft:piston", new PistonHandler());
    }
    
    public boolean isHandled(final String key) {
        return this.handlers.containsKey(key);
    }
    
    public CompoundTag transform(final UserConnection user, final Position position, final CompoundTag tag) throws Exception {
        final String id = (String)tag.get("id").getValue();
        final BackwardsBlockEntityHandler handler = this.handlers.get(id);
        if (handler == null) {
            if (Via.getManager().isDebug()) {
                ViaBackwards.getPlatform().getLogger().warning("Unhandled BlockEntity " + id + " full tag: " + tag);
            }
            return tag;
        }
        final BackwardsBlockStorage storage = (BackwardsBlockStorage)user.get((Class)BackwardsBlockStorage.class);
        final Integer blockId = storage.get(position);
        if (blockId == null) {
            if (Via.getManager().isDebug()) {
                ViaBackwards.getPlatform().getLogger().warning("Handled BlockEntity does not have a stored block :( " + id + " full tag: " + tag);
            }
            return tag;
        }
        return handler.transform(user, blockId, tag);
    }
    
    public CompoundTag transform(final UserConnection user, final Position position, final String id) throws Exception {
        final CompoundTag tag = new CompoundTag("");
        tag.put((Tag)new StringTag("id", id));
        tag.put((Tag)new IntTag("x", Math.toIntExact(position.getX())));
        tag.put((Tag)new IntTag("y", Math.toIntExact(position.getY())));
        tag.put((Tag)new IntTag("z", Math.toIntExact(position.getZ())));
        return this.transform(user, position, tag);
    }
    
    public interface BackwardsBlockEntityHandler
    {
        CompoundTag transform(final UserConnection p0, final int p1, final CompoundTag p2);
    }
}
