// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.sponge.commands;

import org.spongepowered.api.util.Identifiable;
import java.util.UUID;
import us.myles.viaversion.libs.bungeecordchat.chat.ComponentSerializer;
import us.myles.viaversion.libs.bungeecordchat.api.chat.TextComponent;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.command.CommandSource;
import us.myles.ViaVersion.api.command.ViaCommandSender;

public class SpongeCommandSender implements ViaCommandSender
{
    private final CommandSource source;
    
    public SpongeCommandSender(final CommandSource source) {
        this.source = source;
    }
    
    @Override
    public boolean hasPermission(final String permission) {
        return this.source.hasPermission(permission);
    }
    
    @Override
    public void sendMessage(final String msg) {
        this.source.sendMessage(TextSerializers.JSON.deserialize(ComponentSerializer.toString(TextComponent.fromLegacyText(msg))));
    }
    
    @Override
    public UUID getUUID() {
        if (this.source instanceof Identifiable) {
            return ((Identifiable)this.source).getUniqueId();
        }
        return UUID.fromString(this.getName());
    }
    
    @Override
    public String getName() {
        return this.source.getName();
    }
}
