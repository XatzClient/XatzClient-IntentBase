// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.bungee.listeners;

import net.md_5.bungee.event.EventHandler;
import us.myles.ViaVersion.api.data.UserConnection;
import java.util.Collections;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_9;
import us.myles.ViaVersion.api.type.types.version.Types1_9;
import us.myles.ViaVersion.api.type.Type;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import us.myles.ViaVersion.api.Via;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;

public class ElytraPatch implements Listener
{
    @EventHandler(priority = 32)
    public void onServerConnected(final ServerConnectedEvent event) {
        final UserConnection user = Via.getManager().getConnection(event.getPlayer().getUniqueId());
        if (user == null) {
            return;
        }
        try {
            if (user.getProtocolInfo().getPipeline().contains(Protocol1_9To1_8.class)) {
                final int entityId = user.get(EntityTracker1_9.class).getProvidedEntityId();
                final PacketWrapper wrapper = new PacketWrapper(57, null, user);
                wrapper.write(Type.VAR_INT, entityId);
                wrapper.write(Types1_9.METADATA_LIST, Collections.singletonList(new Metadata(0, MetaType1_9.Byte, 0)));
                wrapper.send(Protocol1_9To1_8.class);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
