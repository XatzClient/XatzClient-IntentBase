// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.bungeecordchat.api.chat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import us.myles.viaversion.libs.bungeecordchat.api.ChatColor;

public abstract class BaseComponent
{
    BaseComponent parent;
    private ChatColor color;
    private String font;
    private Boolean bold;
    private Boolean italic;
    private Boolean underlined;
    private Boolean strikethrough;
    private Boolean obfuscated;
    private String insertion;
    private List<BaseComponent> extra;
    private ClickEvent clickEvent;
    private HoverEvent hoverEvent;
    
    @Deprecated
    public BaseComponent() {
    }
    
    BaseComponent(final BaseComponent old) {
        this.copyFormatting(old, ComponentBuilder.FormatRetention.ALL, true);
        if (old.getExtra() != null) {
            for (final BaseComponent extra : old.getExtra()) {
                this.addExtra(extra.duplicate());
            }
        }
    }
    
    public void copyFormatting(final BaseComponent component) {
        this.copyFormatting(component, ComponentBuilder.FormatRetention.ALL, true);
    }
    
    public void copyFormatting(final BaseComponent component, final boolean replace) {
        this.copyFormatting(component, ComponentBuilder.FormatRetention.ALL, replace);
    }
    
    public void copyFormatting(final BaseComponent component, final ComponentBuilder.FormatRetention retention, final boolean replace) {
        if (retention == ComponentBuilder.FormatRetention.EVENTS || retention == ComponentBuilder.FormatRetention.ALL) {
            if (replace || this.clickEvent == null) {
                this.setClickEvent(component.getClickEvent());
            }
            if (replace || this.hoverEvent == null) {
                this.setHoverEvent(component.getHoverEvent());
            }
        }
        if (retention == ComponentBuilder.FormatRetention.FORMATTING || retention == ComponentBuilder.FormatRetention.ALL) {
            if (replace || this.color == null) {
                this.setColor(component.getColorRaw());
            }
            if (replace || this.font == null) {
                this.setFont(component.getFontRaw());
            }
            if (replace || this.bold == null) {
                this.setBold(component.isBoldRaw());
            }
            if (replace || this.italic == null) {
                this.setItalic(component.isItalicRaw());
            }
            if (replace || this.underlined == null) {
                this.setUnderlined(component.isUnderlinedRaw());
            }
            if (replace || this.strikethrough == null) {
                this.setStrikethrough(component.isStrikethroughRaw());
            }
            if (replace || this.obfuscated == null) {
                this.setObfuscated(component.isObfuscatedRaw());
            }
            if (replace || this.insertion == null) {
                this.setInsertion(component.getInsertion());
            }
        }
    }
    
    public void retain(final ComponentBuilder.FormatRetention retention) {
        if (retention == ComponentBuilder.FormatRetention.FORMATTING || retention == ComponentBuilder.FormatRetention.NONE) {
            this.setClickEvent(null);
            this.setHoverEvent(null);
        }
        if (retention == ComponentBuilder.FormatRetention.EVENTS || retention == ComponentBuilder.FormatRetention.NONE) {
            this.setColor(null);
            this.setBold(null);
            this.setItalic(null);
            this.setUnderlined(null);
            this.setStrikethrough(null);
            this.setObfuscated(null);
            this.setInsertion(null);
        }
    }
    
    public abstract BaseComponent duplicate();
    
    @Deprecated
    public BaseComponent duplicateWithoutFormatting() {
        final BaseComponent component = this.duplicate();
        component.retain(ComponentBuilder.FormatRetention.NONE);
        return component;
    }
    
    public static String toLegacyText(final BaseComponent... components) {
        final StringBuilder builder = new StringBuilder();
        for (final BaseComponent msg : components) {
            builder.append(msg.toLegacyText());
        }
        return builder.toString();
    }
    
    public static String toPlainText(final BaseComponent... components) {
        final StringBuilder builder = new StringBuilder();
        for (final BaseComponent msg : components) {
            builder.append(msg.toPlainText());
        }
        return builder.toString();
    }
    
    public ChatColor getColor() {
        if (this.color != null) {
            return this.color;
        }
        if (this.parent == null) {
            return ChatColor.WHITE;
        }
        return this.parent.getColor();
    }
    
    public ChatColor getColorRaw() {
        return this.color;
    }
    
    public String getFont() {
        if (this.font != null) {
            return this.font;
        }
        if (this.parent == null) {
            return null;
        }
        return this.parent.getFont();
    }
    
    public String getFontRaw() {
        return this.font;
    }
    
    public boolean isBold() {
        if (this.bold == null) {
            return this.parent != null && this.parent.isBold();
        }
        return this.bold;
    }
    
    public Boolean isBoldRaw() {
        return this.bold;
    }
    
    public boolean isItalic() {
        if (this.italic == null) {
            return this.parent != null && this.parent.isItalic();
        }
        return this.italic;
    }
    
    public Boolean isItalicRaw() {
        return this.italic;
    }
    
    public boolean isUnderlined() {
        if (this.underlined == null) {
            return this.parent != null && this.parent.isUnderlined();
        }
        return this.underlined;
    }
    
    public Boolean isUnderlinedRaw() {
        return this.underlined;
    }
    
    public boolean isStrikethrough() {
        if (this.strikethrough == null) {
            return this.parent != null && this.parent.isStrikethrough();
        }
        return this.strikethrough;
    }
    
    public Boolean isStrikethroughRaw() {
        return this.strikethrough;
    }
    
    public boolean isObfuscated() {
        if (this.obfuscated == null) {
            return this.parent != null && this.parent.isObfuscated();
        }
        return this.obfuscated;
    }
    
    public Boolean isObfuscatedRaw() {
        return this.obfuscated;
    }
    
    public void setExtra(final List<BaseComponent> components) {
        for (final BaseComponent component : components) {
            component.parent = this;
        }
        this.extra = components;
    }
    
    public void addExtra(final String text) {
        this.addExtra(new TextComponent(text));
    }
    
    public void addExtra(final BaseComponent component) {
        if (this.extra == null) {
            this.extra = new ArrayList<BaseComponent>();
        }
        component.parent = this;
        this.extra.add(component);
    }
    
    public boolean hasFormatting() {
        return this.color != null || this.font != null || this.bold != null || this.italic != null || this.underlined != null || this.strikethrough != null || this.obfuscated != null || this.insertion != null || this.hoverEvent != null || this.clickEvent != null;
    }
    
    public String toPlainText() {
        final StringBuilder builder = new StringBuilder();
        this.toPlainText(builder);
        return builder.toString();
    }
    
    void toPlainText(final StringBuilder builder) {
        if (this.extra != null) {
            for (final BaseComponent e : this.extra) {
                e.toPlainText(builder);
            }
        }
    }
    
    public String toLegacyText() {
        final StringBuilder builder = new StringBuilder();
        this.toLegacyText(builder);
        return builder.toString();
    }
    
    void toLegacyText(final StringBuilder builder) {
        if (this.extra != null) {
            for (final BaseComponent e : this.extra) {
                e.toLegacyText(builder);
            }
        }
    }
    
    void addFormat(final StringBuilder builder) {
        builder.append(this.getColor());
        if (this.isBold()) {
            builder.append(ChatColor.BOLD);
        }
        if (this.isItalic()) {
            builder.append(ChatColor.ITALIC);
        }
        if (this.isUnderlined()) {
            builder.append(ChatColor.UNDERLINE);
        }
        if (this.isStrikethrough()) {
            builder.append(ChatColor.STRIKETHROUGH);
        }
        if (this.isObfuscated()) {
            builder.append(ChatColor.MAGIC);
        }
    }
    
    public void setColor(final ChatColor color) {
        this.color = color;
    }
    
    public void setFont(final String font) {
        this.font = font;
    }
    
    public void setBold(final Boolean bold) {
        this.bold = bold;
    }
    
    public void setItalic(final Boolean italic) {
        this.italic = italic;
    }
    
    public void setUnderlined(final Boolean underlined) {
        this.underlined = underlined;
    }
    
    public void setStrikethrough(final Boolean strikethrough) {
        this.strikethrough = strikethrough;
    }
    
    public void setObfuscated(final Boolean obfuscated) {
        this.obfuscated = obfuscated;
    }
    
    public void setInsertion(final String insertion) {
        this.insertion = insertion;
    }
    
    public void setClickEvent(final ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }
    
    public void setHoverEvent(final HoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
    }
    
    @Override
    public String toString() {
        return "BaseComponent(color=" + this.getColor() + ", font=" + this.getFont() + ", bold=" + this.bold + ", italic=" + this.italic + ", underlined=" + this.underlined + ", strikethrough=" + this.strikethrough + ", obfuscated=" + this.obfuscated + ", insertion=" + this.getInsertion() + ", extra=" + this.getExtra() + ", clickEvent=" + this.getClickEvent() + ", hoverEvent=" + this.getHoverEvent() + ")";
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BaseComponent)) {
            return false;
        }
        final BaseComponent other = (BaseComponent)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$color = this.getColor();
        final Object other$color = other.getColor();
        Label_0065: {
            if (this$color == null) {
                if (other$color == null) {
                    break Label_0065;
                }
            }
            else if (this$color.equals(other$color)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$font = this.getFont();
        final Object other$font = other.getFont();
        Label_0102: {
            if (this$font == null) {
                if (other$font == null) {
                    break Label_0102;
                }
            }
            else if (this$font.equals(other$font)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$bold = this.bold;
        final Object other$bold = other.bold;
        Label_0139: {
            if (this$bold == null) {
                if (other$bold == null) {
                    break Label_0139;
                }
            }
            else if (this$bold.equals(other$bold)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$italic = this.italic;
        final Object other$italic = other.italic;
        Label_0176: {
            if (this$italic == null) {
                if (other$italic == null) {
                    break Label_0176;
                }
            }
            else if (this$italic.equals(other$italic)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$underlined = this.underlined;
        final Object other$underlined = other.underlined;
        Label_0213: {
            if (this$underlined == null) {
                if (other$underlined == null) {
                    break Label_0213;
                }
            }
            else if (this$underlined.equals(other$underlined)) {
                break Label_0213;
            }
            return false;
        }
        final Object this$strikethrough = this.strikethrough;
        final Object other$strikethrough = other.strikethrough;
        Label_0250: {
            if (this$strikethrough == null) {
                if (other$strikethrough == null) {
                    break Label_0250;
                }
            }
            else if (this$strikethrough.equals(other$strikethrough)) {
                break Label_0250;
            }
            return false;
        }
        final Object this$obfuscated = this.obfuscated;
        final Object other$obfuscated = other.obfuscated;
        Label_0287: {
            if (this$obfuscated == null) {
                if (other$obfuscated == null) {
                    break Label_0287;
                }
            }
            else if (this$obfuscated.equals(other$obfuscated)) {
                break Label_0287;
            }
            return false;
        }
        final Object this$insertion = this.getInsertion();
        final Object other$insertion = other.getInsertion();
        Label_0324: {
            if (this$insertion == null) {
                if (other$insertion == null) {
                    break Label_0324;
                }
            }
            else if (this$insertion.equals(other$insertion)) {
                break Label_0324;
            }
            return false;
        }
        final Object this$extra = this.getExtra();
        final Object other$extra = other.getExtra();
        Label_0361: {
            if (this$extra == null) {
                if (other$extra == null) {
                    break Label_0361;
                }
            }
            else if (this$extra.equals(other$extra)) {
                break Label_0361;
            }
            return false;
        }
        final Object this$clickEvent = this.getClickEvent();
        final Object other$clickEvent = other.getClickEvent();
        Label_0398: {
            if (this$clickEvent == null) {
                if (other$clickEvent == null) {
                    break Label_0398;
                }
            }
            else if (this$clickEvent.equals(other$clickEvent)) {
                break Label_0398;
            }
            return false;
        }
        final Object this$hoverEvent = this.getHoverEvent();
        final Object other$hoverEvent = other.getHoverEvent();
        if (this$hoverEvent == null) {
            if (other$hoverEvent == null) {
                return true;
            }
        }
        else if (this$hoverEvent.equals(other$hoverEvent)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof BaseComponent;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $color = this.getColor();
        result = result * 59 + (($color == null) ? 43 : $color.hashCode());
        final Object $font = this.getFont();
        result = result * 59 + (($font == null) ? 43 : $font.hashCode());
        final Object $bold = this.bold;
        result = result * 59 + (($bold == null) ? 43 : $bold.hashCode());
        final Object $italic = this.italic;
        result = result * 59 + (($italic == null) ? 43 : $italic.hashCode());
        final Object $underlined = this.underlined;
        result = result * 59 + (($underlined == null) ? 43 : $underlined.hashCode());
        final Object $strikethrough = this.strikethrough;
        result = result * 59 + (($strikethrough == null) ? 43 : $strikethrough.hashCode());
        final Object $obfuscated = this.obfuscated;
        result = result * 59 + (($obfuscated == null) ? 43 : $obfuscated.hashCode());
        final Object $insertion = this.getInsertion();
        result = result * 59 + (($insertion == null) ? 43 : $insertion.hashCode());
        final Object $extra = this.getExtra();
        result = result * 59 + (($extra == null) ? 43 : $extra.hashCode());
        final Object $clickEvent = this.getClickEvent();
        result = result * 59 + (($clickEvent == null) ? 43 : $clickEvent.hashCode());
        final Object $hoverEvent = this.getHoverEvent();
        result = result * 59 + (($hoverEvent == null) ? 43 : $hoverEvent.hashCode());
        return result;
    }
    
    public String getInsertion() {
        return this.insertion;
    }
    
    public List<BaseComponent> getExtra() {
        return this.extra;
    }
    
    public ClickEvent getClickEvent() {
        return this.clickEvent;
    }
    
    public HoverEvent getHoverEvent() {
        return this.hoverEvent;
    }
}
