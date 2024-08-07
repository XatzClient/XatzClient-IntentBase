// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.bungeecordchat.api.chat.hover.content;

import java.util.Arrays;
import us.myles.viaversion.libs.bungeecordchat.api.chat.HoverEvent;
import us.myles.viaversion.libs.bungeecordchat.api.chat.BaseComponent;

public class Text extends Content
{
    private final Object value;
    
    public Text(final BaseComponent[] value) {
        this.value = value;
    }
    
    public Text(final String value) {
        this.value = value;
    }
    
    @Override
    public HoverEvent.Action requiredAction() {
        return HoverEvent.Action.SHOW_TEXT;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this.value instanceof BaseComponent[]) {
            return o instanceof Text && ((Text)o).value instanceof BaseComponent[] && Arrays.equals((Object[])this.value, (Object[])((Text)o).value);
        }
        return this.value.equals(o);
    }
    
    @Override
    public int hashCode() {
        return (this.value instanceof BaseComponent[]) ? Arrays.hashCode((Object[])this.value) : this.value.hashCode();
    }
    
    public Object getValue() {
        return this.value;
    }
    
    @Override
    public String toString() {
        return "Text(value=" + this.getValue() + ")";
    }
}
