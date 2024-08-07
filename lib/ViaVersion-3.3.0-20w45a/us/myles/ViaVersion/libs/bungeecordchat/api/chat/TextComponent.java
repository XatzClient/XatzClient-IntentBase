// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.bungeecordchat.api.chat;

import java.util.List;
import java.util.Collection;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.ArrayList;
import us.myles.viaversion.libs.bungeecordchat.api.ChatColor;
import java.util.regex.Pattern;

public final class TextComponent extends BaseComponent
{
    private static final Pattern url;
    private String text;
    
    public static BaseComponent[] fromLegacyText(final String message) {
        return fromLegacyText(message, ChatColor.WHITE);
    }
    
    public static BaseComponent[] fromLegacyText(final String message, final ChatColor defaultColor) {
        final ArrayList<BaseComponent> components = new ArrayList<BaseComponent>();
        StringBuilder builder = new StringBuilder();
        TextComponent component = new TextComponent();
        final Matcher matcher = TextComponent.url.matcher(message);
        for (int i = 0; i < message.length(); ++i) {
            char c = message.charAt(i);
            if (c == '§') {
                if (++i >= message.length()) {
                    break;
                }
                c = message.charAt(i);
                if (c >= 'A' && c <= 'Z') {
                    c += ' ';
                }
                ChatColor format;
                if (c == 'x' && i + 12 < message.length()) {
                    final StringBuilder hex = new StringBuilder("#");
                    for (int j = 0; j < 6; ++j) {
                        hex.append(message.charAt(i + 2 + j * 2));
                    }
                    try {
                        format = ChatColor.of(hex.toString());
                    }
                    catch (IllegalArgumentException ex) {
                        format = null;
                    }
                    i += 12;
                }
                else {
                    format = ChatColor.getByChar(c);
                }
                if (format != null) {
                    if (builder.length() > 0) {
                        final TextComponent old = component;
                        component = new TextComponent(old);
                        old.setText(builder.toString());
                        builder = new StringBuilder();
                        components.add(old);
                    }
                    if (format == ChatColor.BOLD) {
                        component.setBold(true);
                    }
                    else if (format == ChatColor.ITALIC) {
                        component.setItalic(true);
                    }
                    else if (format == ChatColor.UNDERLINE) {
                        component.setUnderlined(true);
                    }
                    else if (format == ChatColor.STRIKETHROUGH) {
                        component.setStrikethrough(true);
                    }
                    else if (format == ChatColor.MAGIC) {
                        component.setObfuscated(true);
                    }
                    else if (format == ChatColor.RESET) {
                        format = defaultColor;
                        component = new TextComponent();
                        component.setColor(format);
                    }
                    else {
                        component = new TextComponent();
                        component.setColor(format);
                    }
                }
            }
            else {
                int pos = message.indexOf(32, i);
                if (pos == -1) {
                    pos = message.length();
                }
                if (matcher.region(i, pos).find()) {
                    if (builder.length() > 0) {
                        final TextComponent old = component;
                        component = new TextComponent(old);
                        old.setText(builder.toString());
                        builder = new StringBuilder();
                        components.add(old);
                    }
                    final TextComponent old = component;
                    component = new TextComponent(old);
                    final String urlString = message.substring(i, pos);
                    component.setText(urlString);
                    component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, urlString.startsWith("http") ? urlString : ("http://" + urlString)));
                    components.add(component);
                    i += pos - i - 1;
                    component = old;
                }
                else {
                    builder.append(c);
                }
            }
        }
        component.setText(builder.toString());
        components.add(component);
        return components.toArray(new BaseComponent[components.size()]);
    }
    
    public TextComponent() {
        this.text = "";
    }
    
    public TextComponent(final TextComponent textComponent) {
        super(textComponent);
        this.setText(textComponent.getText());
    }
    
    public TextComponent(final BaseComponent... extras) {
        this();
        if (extras.length == 0) {
            return;
        }
        this.setExtra(new ArrayList<BaseComponent>(Arrays.asList(extras)));
    }
    
    @Override
    public TextComponent duplicate() {
        return new TextComponent(this);
    }
    
    protected void toPlainText(final StringBuilder builder) {
        builder.append(this.text);
        super.toPlainText(builder);
    }
    
    protected void toLegacyText(final StringBuilder builder) {
        this.addFormat(builder);
        builder.append(this.text);
        super.toLegacyText(builder);
    }
    
    @Override
    public String toString() {
        return String.format("TextComponent{text=%s, %s}", this.text, super.toString());
    }
    
    public String getText() {
        return this.text;
    }
    
    public void setText(final String text) {
        this.text = text;
    }
    
    public TextComponent(final String text) {
        this.text = text;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TextComponent)) {
            return false;
        }
        final TextComponent other = (TextComponent)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final Object this$text = this.getText();
        final Object other$text = other.getText();
        if (this$text == null) {
            if (other$text == null) {
                return true;
            }
        }
        else if (this$text.equals(other$text)) {
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof TextComponent;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        final Object $text = this.getText();
        result = result * 59 + (($text == null) ? 43 : $text.hashCode());
        return result;
    }
    
    static {
        url = Pattern.compile("^(?:(https?)://)?([-\\w_\\.]{2,}\\.[a-z]{2,4})(/\\S*)?$");
    }
}
