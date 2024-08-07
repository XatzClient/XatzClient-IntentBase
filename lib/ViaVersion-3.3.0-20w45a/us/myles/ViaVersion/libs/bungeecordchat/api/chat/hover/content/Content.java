// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.bungeecordchat.api.chat.hover.content;

import us.myles.viaversion.libs.bungeecordchat.api.chat.HoverEvent;

public abstract class Content
{
    public abstract HoverEvent.Action requiredAction();
    
    public void assertAction(final HoverEvent.Action input) throws UnsupportedOperationException {
        if (input != this.requiredAction()) {
            throw new UnsupportedOperationException("Action " + input + " not compatible! Expected " + this.requiredAction());
        }
    }
    
    @Override
    public String toString() {
        return "Content()";
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Content)) {
            return false;
        }
        final Content other = (Content)o;
        return other.canEqual(this);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Content;
    }
    
    @Override
    public int hashCode() {
        final int result = 1;
        return 1;
    }
}
