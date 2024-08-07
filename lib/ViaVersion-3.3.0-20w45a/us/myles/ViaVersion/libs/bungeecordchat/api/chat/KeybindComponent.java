// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.bungeecordchat.api.chat;

public final class KeybindComponent extends BaseComponent
{
    private String keybind;
    
    public KeybindComponent(final KeybindComponent original) {
        super(original);
        this.setKeybind(original.getKeybind());
    }
    
    public KeybindComponent(final String keybind) {
        this.setKeybind(keybind);
    }
    
    @Override
    public KeybindComponent duplicate() {
        return new KeybindComponent(this);
    }
    
    protected void toPlainText(final StringBuilder builder) {
        builder.append(this.getKeybind());
        super.toPlainText(builder);
    }
    
    protected void toLegacyText(final StringBuilder builder) {
        this.addFormat(builder);
        builder.append(this.getKeybind());
        super.toLegacyText(builder);
    }
    
    public String getKeybind() {
        return this.keybind;
    }
    
    public void setKeybind(final String keybind) {
        this.keybind = keybind;
    }
    
    @Override
    public String toString() {
        return "KeybindComponent(keybind=" + this.getKeybind() + ")";
    }
    
    public KeybindComponent() {
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof KeybindComponent)) {
            return false;
        }
        final KeybindComponent other = (KeybindComponent)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final Object this$keybind = this.getKeybind();
        final Object other$keybind = other.getKeybind();
        if (this$keybind == null) {
            if (other$keybind == null) {
                return true;
            }
        }
        else if (this$keybind.equals(other$keybind)) {
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof KeybindComponent;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        final Object $keybind = this.getKeybind();
        result = result * 59 + (($keybind == null) ? 43 : $keybind.hashCode());
        return result;
    }
}
