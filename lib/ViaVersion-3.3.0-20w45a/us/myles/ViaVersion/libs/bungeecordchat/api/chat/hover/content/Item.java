// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.bungeecordchat.api.chat.hover.content;

import us.myles.viaversion.libs.bungeecordchat.api.chat.HoverEvent;
import us.myles.viaversion.libs.bungeecordchat.api.chat.ItemTag;

public class Item extends Content
{
    private String id;
    private int count;
    private ItemTag tag;
    
    @Override
    public HoverEvent.Action requiredAction() {
        return HoverEvent.Action.SHOW_ITEM;
    }
    
    public String getId() {
        return this.id;
    }
    
    public int getCount() {
        return this.count;
    }
    
    public ItemTag getTag() {
        return this.tag;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setCount(final int count) {
        this.count = count;
    }
    
    public void setTag(final ItemTag tag) {
        this.tag = tag;
    }
    
    public Item(final String id, final int count, final ItemTag tag) {
        this.count = -1;
        this.id = id;
        this.count = count;
        this.tag = tag;
    }
    
    @Override
    public String toString() {
        return "Item(id=" + this.getId() + ", count=" + this.getCount() + ", tag=" + this.getTag() + ")";
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        final Item other = (Item)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        Label_0065: {
            if (this$id == null) {
                if (other$id == null) {
                    break Label_0065;
                }
            }
            else if (this$id.equals(other$id)) {
                break Label_0065;
            }
            return false;
        }
        if (this.getCount() != other.getCount()) {
            return false;
        }
        final Object this$tag = this.getTag();
        final Object other$tag = other.getTag();
        if (this$tag == null) {
            if (other$tag == null) {
                return true;
            }
        }
        else if (this$tag.equals(other$tag)) {
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof Item;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getCount();
        final Object $tag = this.getTag();
        result = result * 59 + (($tag == null) ? 43 : $tag.hashCode());
        return result;
    }
}
