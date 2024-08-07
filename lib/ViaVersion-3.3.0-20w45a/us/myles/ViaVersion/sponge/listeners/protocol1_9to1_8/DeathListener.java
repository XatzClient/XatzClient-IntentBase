// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.sponge.listeners.protocol1_9to1_8;

import us.myles.ViaVersion.api.data.UserConnection;
import java.util.UUID;
import us.myles.ViaVersion.api.type.Type;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.PacketWrapper;
import java.util.Optional;
import org.spongepowered.api.world.World;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.Listener;
import us.myles.ViaVersion.api.Via;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import us.myles.ViaVersion.SpongePlugin;
import us.myles.ViaVersion.sponge.listeners.ViaSpongeListener;

public class DeathListener extends ViaSpongeListener
{
    public DeathListener(final SpongePlugin plugin) {
        super(plugin, Protocol1_9To1_8.class);
    }
    
    @Listener(order = Order.LAST)
    public void onDeath(final DestructEntityEvent.Death e) {
        if (!(e.getTargetEntity() instanceof Player)) {
            return;
        }
        final Player p = (Player)e.getTargetEntity();
        if (this.isOnPipe(p.getUniqueId()) && Via.getConfig().isShowNewDeathMessages() && this.checkGamerule(p.getWorld())) {
            this.sendPacket(p, e.getMessage().toPlain());
        }
    }
    
    public boolean checkGamerule(final World w) {
        final Optional<String> gamerule = (Optional<String>)w.getGameRule("showDeathMessages");
        if (gamerule.isPresent()) {
            try {
                return Boolean.parseBoolean(gamerule.get());
            }
            catch (Exception e) {
                return false;
            }
        }
        return false;
    }
    
    private void sendPacket(final Player p, final String msg) {
        Via.getPlatform().runSync(new Runnable() {
            @Override
            public void run() {
                final PacketWrapper wrapper = new PacketWrapper(44, null, ViaListener.this.getUserConnection(p.getUniqueId()));
                try {
                    final int entityId = ViaSpongeListener.this.getEntityId(p);
                    wrapper.write(Type.VAR_INT, 2);
                    wrapper.write(Type.VAR_INT, entityId);
                    wrapper.write(Type.INT, entityId);
                    Protocol1_9To1_8.FIX_JSON.write(wrapper, msg);
                    wrapper.send(Protocol1_9To1_8.class);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
