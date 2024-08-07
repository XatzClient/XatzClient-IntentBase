// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.bungeecordchat.api.chat;

public final class ClickEvent
{
    private final Action action;
    private final String value;
    
    public Action getAction() {
        return this.action;
    }
    
    public String getValue() {
        return this.value;
    }
    
    @Override
    public String toString() {
        return "ClickEvent(action=" + this.getAction() + ", value=" + this.getValue() + ")";
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ClickEvent)) {
            return false;
        }
        final ClickEvent other = (ClickEvent)o;
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
        final Object this$value = this.getValue();
        final Object other$value = other.getValue();
        if (this$value == null) {
            if (other$value == null) {
                return true;
            }
        }
        else if (this$value.equals(other$value)) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $action = this.getAction();
        result = result * 59 + (($action == null) ? 43 : $action.hashCode());
        final Object $value = this.getValue();
        result = result * 59 + (($value == null) ? 43 : $value.hashCode());
        return result;
    }
    
    public ClickEvent(final Action action, final String value) {
        this.action = action;
        this.value = value;
    }
    
    public enum Action
    {
        OPEN_URL, 
        OPEN_FILE, 
        RUN_COMMAND, 
        SUGGEST_COMMAND, 
        CHANGE_PAGE, 
        COPY_TO_CLIPBOARD;
    }
}
