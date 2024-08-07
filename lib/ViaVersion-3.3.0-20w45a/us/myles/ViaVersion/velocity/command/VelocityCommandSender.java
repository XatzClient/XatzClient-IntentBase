// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.velocity.command;

import com.velocitypowered.api.proxy.Player;
import java.util.UUID;
import us.myles.viaversion.libs.bungeecordchat.chat.ComponentSerializer;
import us.myles.viaversion.libs.bungeecordchat.api.chat.TextComponent;
import net.kyori.text.serializer.gson.GsonComponentSerializer;
import com.velocitypowered.api.command.CommandSource;
import us.myles.ViaVersion.api.command.ViaCommandSender;

public class VelocityCommandSender implements ViaCommandSender
{
    private final CommandSource source;
    
    public VelocityCommandSender(final CommandSource source) {
        this.source = source;
    }
    
    @Override
    public boolean hasPermission(final String permission) {
        return this.source.hasPermission(permission);
    }
    
    @Override
    public void sendMessage(final String msg) {
        this.source.sendMessage(GsonComponentSerializer.INSTANCE.deserialize(ComponentSerializer.toString(TextComponent.fromLegacyText(msg))));
    }
    
    @Override
    public UUID getUUID() {
        if (this.source instanceof Player) {
            return ((Player)this.source).getUniqueId();
        }
        return UUID.fromString(this.getName());
    }
    
    @Override
    public String getName() {
        if (this.source instanceof Player) {
            return ((Player)this.source).getUsername();
        }
        return "?";
    }
}
