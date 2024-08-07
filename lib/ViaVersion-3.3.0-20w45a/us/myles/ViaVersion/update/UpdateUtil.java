// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.update;

import us.myles.viaversion.libs.bungeecordchat.api.ChatColor;
import java.io.IOException;
import java.net.MalformedURLException;
import us.myles.viaversion.libs.gson.JsonParseException;
import us.myles.ViaVersion.util.GsonUtil;
import us.myles.viaversion.libs.gson.JsonObject;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.jetbrains.annotations.Nullable;
import java.util.Locale;
import us.myles.ViaVersion.api.Via;
import java.util.UUID;

public class UpdateUtil
{
    public static final String PREFIX;
    private static final String URL = "https://api.spiget.org/v2/resources/";
    private static final int PLUGIN = 19254;
    private static final String LATEST_VERSION = "/versions/latest";
    
    public static void sendUpdateMessage(final UUID uuid) {
        final String message;
        Via.getPlatform().runAsync(() -> {
            message = getUpdateMessage(false);
            if (message != null) {
                Via.getPlatform().runSync(() -> Via.getPlatform().sendMessage(uuid, UpdateUtil.PREFIX + message));
            }
        });
    }
    
    public static void sendUpdateMessage() {
        final String message;
        Via.getPlatform().runAsync(() -> {
            message = getUpdateMessage(true);
            if (message != null) {
                Via.getPlatform().runSync(() -> Via.getPlatform().getLogger().warning(message));
            }
        });
    }
    
    @Nullable
    private static String getUpdateMessage(final boolean console) {
        if (Via.getPlatform().getPluginVersion().equals("${project.version}")) {
            return "You are using a debug/custom version, consider updating.";
        }
        final String newestString = getNewestVersion();
        if (newestString == null) {
            if (console) {
                return "Could not check for updates, check your connection.";
            }
            return null;
        }
        else {
            Version current;
            try {
                current = new Version(Via.getPlatform().getPluginVersion());
            }
            catch (IllegalArgumentException e) {
                return "You are using a custom version, consider updating.";
            }
            final Version newest = new Version(newestString);
            if (current.compareTo(newest) < 0) {
                return "There is a newer version available: " + newest.toString() + ", you're on: " + current.toString();
            }
            if (!console || current.compareTo(newest) == 0) {
                return null;
            }
            if (current.getTag().toLowerCase(Locale.ROOT).startsWith("dev") || current.getTag().toLowerCase(Locale.ROOT).startsWith("snapshot")) {
                return "You are running a development version, please report any bugs to GitHub.";
            }
            return "You are running a newer version than is released!";
        }
    }
    
    @Nullable
    private static String getNewestVersion() {
        try {
            final URL url = new URL("https://api.spiget.org/v2/resources/19254/versions/latest?" + System.currentTimeMillis());
            final HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setUseCaches(true);
            connection.addRequestProperty("User-Agent", "ViaVersion " + Via.getPlatform().getPluginVersion() + " " + Via.getPlatform().getPlatformName());
            connection.setDoOutput(true);
            final BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            final StringBuilder builder = new StringBuilder();
            String input;
            while ((input = br.readLine()) != null) {
                builder.append(input);
            }
            br.close();
            JsonObject statistics;
            try {
                statistics = GsonUtil.getGson().fromJson(builder.toString(), JsonObject.class);
            }
            catch (JsonParseException e) {
                e.printStackTrace();
                return null;
            }
            return statistics.get("name").getAsString();
        }
        catch (MalformedURLException e2) {
            return null;
        }
        catch (IOException e3) {
            return null;
        }
    }
    
    static {
        PREFIX = ChatColor.GREEN + "" + ChatColor.BOLD + "[ViaVersion] " + ChatColor.GREEN;
    }
}
