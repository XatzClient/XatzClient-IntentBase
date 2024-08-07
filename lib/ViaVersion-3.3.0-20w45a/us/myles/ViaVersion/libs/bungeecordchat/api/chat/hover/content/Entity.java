// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.bungeecordchat.api.chat.hover.content;

import us.myles.viaversion.libs.bungeecordchat.api.chat.HoverEvent;
import us.myles.viaversion.libs.bungeecordchat.api.chat.BaseComponent;
import lombok.NonNull;

public class Entity extends Content
{
    private String type;
    @NonNull
    private String id;
    private BaseComponent name;
    
    @Override
    public HoverEvent.Action requiredAction() {
        return HoverEvent.Action.SHOW_ENTITY;
    }
    
    public String getType() {
        return this.type;
    }
    
    @NonNull
    public String getId() {
        return this.id;
    }
    
    public BaseComponent getName() {
        return this.name;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public void setId(@NonNull final String id) {
        if (id == null) {
            throw new NullPointerException("id is marked non-null but is null");
        }
        this.id = id;
    }
    
    public void setName(final BaseComponent name) {
        this.name = name;
    }
    
    public Entity(final String type, @NonNull final String id, final BaseComponent name) {
        if (id == null) {
            throw new NullPointerException("id is marked non-null but is null");
        }
        this.type = type;
        this.id = id;
        this.name = name;
    }
    
    @Override
    public String toString() {
        return "Entity(type=" + this.getType() + ", id=" + this.getId() + ", name=" + this.getName() + ")";
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Entity)) {
            return false;
        }
        final Entity other = (Entity)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        Label_0075: {
            if (this$type == null) {
                if (other$type == null) {
                    break Label_0075;
                }
            }
            else if (this$type.equals(other$type)) {
                break Label_0075;
            }
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        Label_0112: {
            if (this$id == null) {
                if (other$id == null) {
                    break Label_0112;
                }
            }
            else if (this$id.equals(other$id)) {
                break Label_0112;
            }
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null) {
            if (other$name == null) {
                return true;
            }
        }
        else if (this$name.equals(other$name)) {
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof Entity;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        return result;
    }
}
