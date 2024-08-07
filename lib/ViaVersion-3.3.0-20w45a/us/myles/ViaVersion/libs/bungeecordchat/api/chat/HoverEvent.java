// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.bungeecordchat.api.chat;

import us.myles.viaversion.libs.bungeecordchat.api.chat.hover.content.Item;
import us.myles.viaversion.libs.bungeecordchat.api.chat.hover.content.Entity;
import us.myles.viaversion.libs.bungeecordchat.chat.ComponentSerializer;
import java.util.Collection;
import java.util.Collections;
import us.myles.viaversion.libs.bungeecordchat.api.chat.hover.content.Text;
import java.util.ArrayList;
import com.google.common.base.Preconditions;
import us.myles.viaversion.libs.bungeecordchat.api.chat.hover.content.Content;
import java.util.List;

public final class HoverEvent
{
    private final Action action;
    private final List<Content> contents;
    private boolean legacy;
    
    public HoverEvent(final Action action, final Content... contents) {
        this.legacy = false;
        Preconditions.checkArgument(contents.length != 0, (Object)"Must contain at least one content");
        this.action = action;
        this.contents = new ArrayList<Content>();
        for (final Content it : contents) {
            this.addContent(it);
        }
    }
    
    @Deprecated
    public HoverEvent(final Action action, final BaseComponent[] value) {
        this.legacy = false;
        this.action = action;
        this.contents = new ArrayList<Content>(Collections.singletonList(new Text(value)));
        this.legacy = true;
    }
    
    @Deprecated
    public BaseComponent[] getValue() {
        final Content content = this.contents.get(0);
        if (content instanceof Text && ((Text)content).getValue() instanceof BaseComponent[]) {
            return (BaseComponent[])((Text)content).getValue();
        }
        final TextComponent component = new TextComponent(ComponentSerializer.toString(content));
        return new BaseComponent[] { component };
    }
    
    public void addContent(final Content content) throws UnsupportedOperationException {
        Preconditions.checkArgument(!this.legacy || this.contents.size() == 0, (Object)"Legacy HoverEvent may not have more than one content");
        content.assertAction(this.action);
        this.contents.add(content);
    }
    
    public static Class<?> getClass(final Action action, final boolean array) {
        Preconditions.checkArgument(action != null, (Object)"action");
        switch (action) {
            case SHOW_TEXT: {
                return (Class<?>)(array ? Text[].class : Text.class);
            }
            case SHOW_ENTITY: {
                return (Class<?>)(array ? Entity[].class : Entity.class);
            }
            case SHOW_ITEM: {
                return (Class<?>)(array ? Item[].class : Item.class);
            }
            default: {
                throw new UnsupportedOperationException("Action '" + action.name() + " not supported");
            }
        }
    }
    
    public Action getAction() {
        return this.action;
    }
    
    public List<Content> getContents() {
        return this.contents;
    }
    
    public boolean isLegacy() {
        return this.legacy;
    }
    
    @Override
    public String toString() {
        return "HoverEvent(action=" + this.getAction() + ", contents=" + this.getContents() + ", legacy=" + this.isLegacy() + ")";
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof HoverEvent)) {
            return false;
        }
        final HoverEvent other = (HoverEvent)o;
        final Object this$action = this.getAction();
        final Object other$action = other.getAction();
        Label_0055: {
            if (this$action == null) {
                if (other$action == null) {
                    break Label_0055;
                }
            }
            else if (this$action.equals(other$action)) {
                break Label_0055;
            }
            return false;
        }
        final Object this$contents = this.getContents();
        final Object other$contents = other.getContents();
        if (this$contents == null) {
            if (other$contents == null) {
                return this.isLegacy() == other.isLegacy();
            }
        }
        else if (this$contents.equals(other$contents)) {
            return this.isLegacy() == other.isLegacy();
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $action = this.getAction();
        result = result * 59 + (($action == null) ? 43 : $action.hashCode());
        final Object $contents = this.getContents();
        result = result * 59 + (($contents == null) ? 43 : $contents.hashCode());
        result = result * 59 + (this.isLegacy() ? 79 : 97);
        return result;
    }
    
    public HoverEvent(final Action action, final List<Content> contents) {
        this.legacy = false;
        this.action = action;
        this.contents = contents;
    }
    
    public void setLegacy(final boolean legacy) {
        this.legacy = legacy;
    }
    
    public enum Action
    {
        SHOW_TEXT, 
        SHOW_ITEM, 
        SHOW_ENTITY, 
        @Deprecated
        SHOW_ACHIEVEMENT;
    }
}
