// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.bungeecordchat.api.chat;

import java.util.regex.Matcher;
import us.myles.viaversion.libs.bungeecordchat.chat.TranslationRegistry;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class TranslatableComponent extends BaseComponent
{
    private final Pattern format;
    private String translate;
    private List<BaseComponent> with;
    
    public TranslatableComponent(final TranslatableComponent original) {
        super(original);
        this.format = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");
        this.setTranslate(original.getTranslate());
        if (original.getWith() != null) {
            final List<BaseComponent> temp = new ArrayList<BaseComponent>();
            for (final BaseComponent baseComponent : original.getWith()) {
                temp.add(baseComponent.duplicate());
            }
            this.setWith(temp);
        }
    }
    
    public TranslatableComponent(final String translate, final Object... with) {
        this.format = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");
        this.setTranslate(translate);
        if (with != null && with.length != 0) {
            final List<BaseComponent> temp = new ArrayList<BaseComponent>();
            for (final Object w : with) {
                if (w instanceof BaseComponent) {
                    temp.add((BaseComponent)w);
                }
                else {
                    temp.add(new TextComponent(String.valueOf(w)));
                }
            }
            this.setWith(temp);
        }
    }
    
    @Override
    public TranslatableComponent duplicate() {
        return new TranslatableComponent(this);
    }
    
    public void setWith(final List<BaseComponent> components) {
        for (final BaseComponent component : components) {
            component.parent = this;
        }
        this.with = components;
    }
    
    public void addWith(final String text) {
        this.addWith(new TextComponent(text));
    }
    
    public void addWith(final BaseComponent component) {
        if (this.with == null) {
            this.with = new ArrayList<BaseComponent>();
        }
        component.parent = this;
        this.with.add(component);
    }
    
    protected void toPlainText(final StringBuilder builder) {
        this.convert(builder, false);
        super.toPlainText(builder);
    }
    
    protected void toLegacyText(final StringBuilder builder) {
        this.convert(builder, true);
        super.toLegacyText(builder);
    }
    
    private void convert(final StringBuilder builder, final boolean applyFormat) {
        final String trans = TranslationRegistry.INSTANCE.translate(this.translate);
        final Matcher matcher = this.format.matcher(trans);
        int position = 0;
        int i = 0;
        while (matcher.find(position)) {
            final int pos = matcher.start();
            if (pos != position) {
                if (applyFormat) {
                    this.addFormat(builder);
                }
                builder.append(trans.substring(position, pos));
            }
            position = matcher.end();
            final String formatCode = matcher.group(2);
            switch (formatCode.charAt(0)) {
                case 'd':
                case 's': {
                    final String withIndex = matcher.group(1);
                    final BaseComponent withComponent = this.with.get((withIndex != null) ? (Integer.parseInt(withIndex) - 1) : i++);
                    if (applyFormat) {
                        withComponent.toLegacyText(builder);
                        continue;
                    }
                    withComponent.toPlainText(builder);
                    continue;
                }
                case '%': {
                    if (applyFormat) {
                        this.addFormat(builder);
                    }
                    builder.append('%');
                    continue;
                }
            }
        }
        if (trans.length() != position) {
            if (applyFormat) {
                this.addFormat(builder);
            }
            builder.append(trans.substring(position, trans.length()));
        }
    }
    
    public Pattern getFormat() {
        return this.format;
    }
    
    public String getTranslate() {
        return this.translate;
    }
    
    public List<BaseComponent> getWith() {
        return this.with;
    }
    
    public void setTranslate(final String translate) {
        this.translate = translate;
    }
    
    @Override
    public String toString() {
        return "TranslatableComponent(format=" + this.getFormat() + ", translate=" + this.getTranslate() + ", with=" + this.getWith() + ")";
    }
    
    public TranslatableComponent() {
        this.format = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TranslatableComponent)) {
            return false;
        }
        final TranslatableComponent other = (TranslatableComponent)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final Object this$format = this.getFormat();
        final Object other$format = other.getFormat();
        Label_0075: {
            if (this$format == null) {
                if (other$format == null) {
                    break Label_0075;
                }
            }
            else if (this$format.equals(other$format)) {
                break Label_0075;
            }
            return false;
        }
        final Object this$translate = this.getTranslate();
        final Object other$translate = other.getTranslate();
        Label_0112: {
            if (this$translate == null) {
                if (other$translate == null) {
                    break Label_0112;
                }
            }
            else if (this$translate.equals(other$translate)) {
                break Label_0112;
            }
            return false;
        }
        final Object this$with = this.getWith();
        final Object other$with = other.getWith();
        if (this$with == null) {
            if (other$with == null) {
                return true;
            }
        }
        else if (this$with.equals(other$with)) {
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof TranslatableComponent;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        final Object $format = this.getFormat();
        result = result * 59 + (($format == null) ? 43 : $format.hashCode());
        final Object $translate = this.getTranslate();
        result = result * 59 + (($translate == null) ? 43 : $translate.hashCode());
        final Object $with = this.getWith();
        result = result * 59 + (($with == null) ? 43 : $with.hashCode());
        return result;
    }
}
