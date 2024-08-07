// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.bungeecordchat.api.chat;

public final class SelectorComponent extends BaseComponent
{
    private String selector;
    
    public SelectorComponent(final SelectorComponent original) {
        super(original);
        this.setSelector(original.getSelector());
    }
    
    @Override
    public SelectorComponent duplicate() {
        return new SelectorComponent(this);
    }
    
    protected void toPlainText(final StringBuilder builder) {
        builder.append(this.selector);
        super.toPlainText(builder);
    }
    
    protected void toLegacyText(final StringBuilder builder) {
        this.addFormat(builder);
        builder.append(this.selector);
        super.toLegacyText(builder);
    }
    
    public String getSelector() {
        return this.selector;
    }
    
    public void setSelector(final String selector) {
        this.selector = selector;
    }
    
    @Override
    public String toString() {
        return "SelectorComponent(selector=" + this.getSelector() + ")";
    }
    
    public SelectorComponent(final String selector) {
        this.selector = selector;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SelectorComponent)) {
            return false;
        }
        final SelectorComponent other = (SelectorComponent)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final Object this$selector = this.getSelector();
        final Object other$selector = other.getSelector();
        if (this$selector == null) {
            if (other$selector == null) {
                return true;
            }
        }
        else if (this$selector.equals(other$selector)) {
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof SelectorComponent;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        final Object $selector = this.getSelector();
        result = result * 59 + (($selector == null) ? 43 : $selector.hashCode());
        return result;
    }
}
