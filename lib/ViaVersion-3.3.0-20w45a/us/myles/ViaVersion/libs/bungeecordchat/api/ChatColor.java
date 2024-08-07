// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.bungeecordchat.api;

import java.util.HashMap;
import com.google.common.base.Preconditions;
import java.util.Objects;
import java.util.Locale;
import java.awt.Color;
import java.util.Map;
import java.util.regex.Pattern;

public final class ChatColor
{
    public static final char COLOR_CHAR = '§';
    public static final String ALL_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx";
    public static final Pattern STRIP_COLOR_PATTERN;
    private static final Map<Character, ChatColor> BY_CHAR;
    private static final Map<String, ChatColor> BY_NAME;
    public static final ChatColor BLACK;
    public static final ChatColor DARK_BLUE;
    public static final ChatColor DARK_GREEN;
    public static final ChatColor DARK_AQUA;
    public static final ChatColor DARK_RED;
    public static final ChatColor DARK_PURPLE;
    public static final ChatColor GOLD;
    public static final ChatColor GRAY;
    public static final ChatColor DARK_GRAY;
    public static final ChatColor BLUE;
    public static final ChatColor GREEN;
    public static final ChatColor AQUA;
    public static final ChatColor RED;
    public static final ChatColor LIGHT_PURPLE;
    public static final ChatColor YELLOW;
    public static final ChatColor WHITE;
    public static final ChatColor MAGIC;
    public static final ChatColor BOLD;
    public static final ChatColor STRIKETHROUGH;
    public static final ChatColor UNDERLINE;
    public static final ChatColor ITALIC;
    public static final ChatColor RESET;
    private static int count;
    private final String toString;
    private final String name;
    private final int ordinal;
    private final Color color;
    
    private ChatColor(final char code, final String name) {
        this(code, name, null);
    }
    
    private ChatColor(final char code, final String name, final Color color) {
        this.name = name;
        this.toString = new String(new char[] { '§', code });
        this.ordinal = ChatColor.count++;
        this.color = color;
        ChatColor.BY_CHAR.put(code, this);
        ChatColor.BY_NAME.put(name.toUpperCase(Locale.ROOT), this);
    }
    
    private ChatColor(final String name, final String toString, final int rgb) {
        this.name = name;
        this.toString = toString;
        this.ordinal = -1;
        this.color = new Color(rgb);
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.toString);
        return hash;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final ChatColor other = (ChatColor)obj;
        return Objects.equals(this.toString, other.toString);
    }
    
    @Override
    public String toString() {
        return this.toString;
    }
    
    public static String stripColor(final String input) {
        if (input == null) {
            return null;
        }
        return ChatColor.STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }
    
    public static String translateAlternateColorCodes(final char altColorChar, final String textToTranslate) {
        final char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; ++i) {
            if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(b[i + 1]) > -1) {
                b[i] = '§';
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        return new String(b);
    }
    
    public static ChatColor getByChar(final char code) {
        return ChatColor.BY_CHAR.get(code);
    }
    
    public static ChatColor of(final Color color) {
        return of("#" + String.format("%08x", color.getRGB()).substring(2));
    }
    
    public static ChatColor of(final String string) {
        Preconditions.checkArgument(string != null, (Object)"string cannot be null");
        if (string.startsWith("#") && string.length() == 7) {
            int rgb;
            try {
                rgb = Integer.parseInt(string.substring(1), 16);
            }
            catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Illegal hex string " + string);
            }
            final StringBuilder magic = new StringBuilder("§x");
            for (final char c : string.substring(1).toCharArray()) {
                magic.append('§').append(c);
            }
            return new ChatColor(string, magic.toString(), rgb);
        }
        final ChatColor defined = ChatColor.BY_NAME.get(string.toUpperCase(Locale.ROOT));
        if (defined != null) {
            return defined;
        }
        throw new IllegalArgumentException("Could not parse ChatColor " + string);
    }
    
    @Deprecated
    public static ChatColor valueOf(final String name) {
        Preconditions.checkNotNull((Object)name, (Object)"Name is null");
        final ChatColor defined = ChatColor.BY_NAME.get(name);
        Preconditions.checkArgument(defined != null, (Object)("No enum constant " + ChatColor.class.getName() + "." + name));
        return defined;
    }
    
    @Deprecated
    public static ChatColor[] values() {
        return ChatColor.BY_CHAR.values().toArray(new ChatColor[ChatColor.BY_CHAR.values().size()]);
    }
    
    @Deprecated
    public String name() {
        return this.getName().toUpperCase(Locale.ROOT);
    }
    
    @Deprecated
    public int ordinal() {
        Preconditions.checkArgument(this.ordinal >= 0, (Object)"Cannot get ordinal of hex color");
        return this.ordinal;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Color getColor() {
        return this.color;
    }
    
    static {
        STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf('§') + "[0-9A-FK-ORX]");
        BY_CHAR = new HashMap<Character, ChatColor>();
        BY_NAME = new HashMap<String, ChatColor>();
        BLACK = new ChatColor('0', "black", new Color(0));
        DARK_BLUE = new ChatColor('1', "dark_blue", new Color(170));
        DARK_GREEN = new ChatColor('2', "dark_green", new Color(43520));
        DARK_AQUA = new ChatColor('3', "dark_aqua", new Color(43690));
        DARK_RED = new ChatColor('4', "dark_red", new Color(11141120));
        DARK_PURPLE = new ChatColor('5', "dark_purple", new Color(11141290));
        GOLD = new ChatColor('6', "gold", new Color(16755200));
        GRAY = new ChatColor('7', "gray", new Color(11184810));
        DARK_GRAY = new ChatColor('8', "dark_gray", new Color(5592405));
        BLUE = new ChatColor('9', "blue", new Color(5592575));
        GREEN = new ChatColor('a', "green", new Color(5635925));
        AQUA = new ChatColor('b', "aqua", new Color(5636095));
        RED = new ChatColor('c', "red", new Color(16733525));
        LIGHT_PURPLE = new ChatColor('d', "light_purple", new Color(16733695));
        YELLOW = new ChatColor('e', "yellow", new Color(16777045));
        WHITE = new ChatColor('f', "white", new Color(16777215));
        MAGIC = new ChatColor('k', "obfuscated");
        BOLD = new ChatColor('l', "bold");
        STRIKETHROUGH = new ChatColor('m', "strikethrough");
        UNDERLINE = new ChatColor('n', "underline");
        ITALIC = new ChatColor('o', "italic");
        RESET = new ChatColor('r', "reset");
        ChatColor.count = 0;
    }
}
