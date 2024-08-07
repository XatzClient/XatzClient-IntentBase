// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.bungeecordchat.api.chat;

import java.util.Iterator;
import us.myles.viaversion.libs.bungeecordchat.api.ChatColor;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;

public final class ComponentBuilder
{
    private int cursor;
    private final List<BaseComponent> parts;
    private BaseComponent dummy;
    
    private ComponentBuilder(final BaseComponent[] parts) {
        this.cursor = -1;
        this.parts = new ArrayList<BaseComponent>();
        for (final BaseComponent baseComponent : parts) {
            this.parts.add(baseComponent.duplicate());
        }
        this.resetCursor();
    }
    
    public ComponentBuilder(final ComponentBuilder original) {
        this(original.parts.toArray(new BaseComponent[original.parts.size()]));
    }
    
    public ComponentBuilder(final String text) {
        this(new TextComponent(text));
    }
    
    public ComponentBuilder(final BaseComponent component) {
        this(new BaseComponent[] { component });
    }
    
    private BaseComponent getDummy() {
        if (this.dummy == null) {
            this.dummy = new BaseComponent() {
                @Override
                public BaseComponent duplicate() {
                    return this;
                }
            };
        }
        return this.dummy;
    }
    
    public ComponentBuilder resetCursor() {
        this.cursor = this.parts.size() - 1;
        return this;
    }
    
    public ComponentBuilder setCursor(final int pos) throws IndexOutOfBoundsException {
        if (this.cursor != pos && (pos < 0 || pos >= this.parts.size())) {
            throw new IndexOutOfBoundsException("Cursor out of bounds (expected between 0 + " + (this.parts.size() - 1) + ")");
        }
        this.cursor = pos;
        return this;
    }
    
    public ComponentBuilder append(final BaseComponent component) {
        return this.append(component, FormatRetention.ALL);
    }
    
    public ComponentBuilder append(final BaseComponent component, final FormatRetention retention) {
        BaseComponent previous = this.parts.isEmpty() ? null : this.parts.get(this.parts.size() - 1);
        if (previous == null) {
            previous = this.dummy;
            this.dummy = null;
        }
        if (previous != null) {
            component.copyFormatting(previous, retention, false);
        }
        this.parts.add(component);
        this.resetCursor();
        return this;
    }
    
    public ComponentBuilder append(final BaseComponent[] components) {
        return this.append(components, FormatRetention.ALL);
    }
    
    public ComponentBuilder append(final BaseComponent[] components, final FormatRetention retention) {
        Preconditions.checkArgument(components.length != 0, (Object)"No components to append");
        for (final BaseComponent component : components) {
            this.append(component, retention);
        }
        return this;
    }
    
    public ComponentBuilder append(final String text) {
        return this.append(text, FormatRetention.ALL);
    }
    
    public ComponentBuilder appendLegacy(final String text) {
        return this.append(TextComponent.fromLegacyText(text));
    }
    
    public ComponentBuilder append(final String text, final FormatRetention retention) {
        return this.append(new TextComponent(text), retention);
    }
    
    public ComponentBuilder append(final Joiner joiner) {
        return joiner.join(this, FormatRetention.ALL);
    }
    
    public ComponentBuilder append(final Joiner joiner, final FormatRetention retention) {
        return joiner.join(this, retention);
    }
    
    public void removeComponent(final int pos) throws IndexOutOfBoundsException {
        if (this.parts.remove(pos) != null) {
            this.resetCursor();
        }
    }
    
    public BaseComponent getComponent(final int pos) throws IndexOutOfBoundsException {
        return this.parts.get(pos);
    }
    
    public BaseComponent getCurrentComponent() {
        return (this.cursor == -1) ? this.getDummy() : this.parts.get(this.cursor);
    }
    
    public ComponentBuilder color(final ChatColor color) {
        this.getCurrentComponent().setColor(color);
        return this;
    }
    
    public ComponentBuilder font(final String font) {
        this.getCurrentComponent().setFont(font);
        return this;
    }
    
    public ComponentBuilder bold(final boolean bold) {
        this.getCurrentComponent().setBold(bold);
        return this;
    }
    
    public ComponentBuilder italic(final boolean italic) {
        this.getCurrentComponent().setItalic(italic);
        return this;
    }
    
    public ComponentBuilder underlined(final boolean underlined) {
        this.getCurrentComponent().setUnderlined(underlined);
        return this;
    }
    
    public ComponentBuilder strikethrough(final boolean strikethrough) {
        this.getCurrentComponent().setStrikethrough(strikethrough);
        return this;
    }
    
    public ComponentBuilder obfuscated(final boolean obfuscated) {
        this.getCurrentComponent().setObfuscated(obfuscated);
        return this;
    }
    
    public ComponentBuilder insertion(final String insertion) {
        this.getCurrentComponent().setInsertion(insertion);
        return this;
    }
    
    public ComponentBuilder event(final ClickEvent clickEvent) {
        this.getCurrentComponent().setClickEvent(clickEvent);
        return this;
    }
    
    public ComponentBuilder event(final HoverEvent hoverEvent) {
        this.getCurrentComponent().setHoverEvent(hoverEvent);
        return this;
    }
    
    public ComponentBuilder reset() {
        return this.retain(FormatRetention.NONE);
    }
    
    public ComponentBuilder retain(final FormatRetention retention) {
        this.getCurrentComponent().retain(retention);
        return this;
    }
    
    public BaseComponent[] create() {
        final BaseComponent[] cloned = new BaseComponent[this.parts.size()];
        int i = 0;
        for (final BaseComponent part : this.parts) {
            cloned[i++] = part.duplicate();
        }
        return cloned;
    }
    
    public ComponentBuilder() {
        this.cursor = -1;
        this.parts = new ArrayList<BaseComponent>();
    }
    
    public int getCursor() {
        return this.cursor;
    }
    
    public List<BaseComponent> getParts() {
        return this.parts;
    }
    
    public enum FormatRetention
    {
        NONE, 
        FORMATTING, 
        EVENTS, 
        ALL;
    }
    
    public interface Joiner
    {
        ComponentBuilder join(final ComponentBuilder p0, final FormatRetention p1);
    }
}
