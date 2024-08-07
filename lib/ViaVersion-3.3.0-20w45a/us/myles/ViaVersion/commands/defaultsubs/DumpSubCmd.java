// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.commands.defaultsubs;

import java.util.Map;
import java.io.OutputStream;
import java.io.InvalidObjectException;
import us.myles.viaversion.libs.gson.JsonObject;
import com.google.common.io.CharStreams;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import us.myles.ViaVersion.util.GsonUtil;
import java.io.IOException;
import java.util.logging.Level;
import us.myles.viaversion.libs.bungeecordchat.api.ChatColor;
import java.net.URL;
import java.net.HttpURLConnection;
import us.myles.ViaVersion.dump.DumpTemplate;
import java.util.Set;
import us.myles.ViaVersion.dump.VersionInfo;
import us.myles.ViaVersion.ViaManager;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;
import us.myles.ViaVersion.api.command.ViaCommandSender;
import us.myles.ViaVersion.api.command.ViaSubCommand;

public class DumpSubCmd extends ViaSubCommand
{
    @Override
    public String name() {
        return "dump";
    }
    
    @Override
    public String description() {
        return "Dump information about your server, this is helpful if you report bugs.";
    }
    
    @Override
    public boolean execute(final ViaCommandSender sender, final String[] args) {
        final VersionInfo version = new VersionInfo(System.getProperty("java.version"), System.getProperty("os.name"), ProtocolRegistry.SERVER_PROTOCOL, ProtocolRegistry.getSupportedVersions(), Via.getPlatform().getPlatformName(), Via.getPlatform().getPlatformVersion(), Via.getPlatform().getPluginVersion(), ViaManager.class.getPackage().getImplementationVersion(), Via.getManager().getSubPlatforms());
        final Map<String, Object> configuration = Via.getPlatform().getConfigurationProvider().getValues();
        final DumpTemplate template = new DumpTemplate(version, configuration, Via.getPlatform().getDump(), Via.getManager().getInjector().getDump());
        Via.getPlatform().runAsync(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                try {
                    con = (HttpURLConnection)new URL("https://dump.viaversion.com/documents").openConnection();
                }
                catch (IOException e) {
                    sender.sendMessage(ChatColor.RED + "Failed to dump, please check the console for more information");
                    Via.getPlatform().getLogger().log(Level.WARNING, "Could not paste ViaVersion dump to ViaVersion Dump", e);
                    return;
                }
                try {
                    con.setRequestProperty("Content-Type", "text/plain");
                    con.addRequestProperty("User-Agent", "ViaVersion/" + version.getPluginVersion());
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    final OutputStream out = con.getOutputStream();
                    out.write(GsonUtil.getGsonBuilder().setPrettyPrinting().create().toJson(template).getBytes(StandardCharsets.UTF_8));
                    out.close();
                    if (con.getResponseCode() == 429) {
                        sender.sendMessage(ChatColor.RED + "You can only paste ones every minute to protect our systems.");
                        return;
                    }
                    final String rawOutput = CharStreams.toString((Readable)new InputStreamReader(con.getInputStream()));
                    con.getInputStream().close();
                    final JsonObject output = GsonUtil.getGson().fromJson(rawOutput, JsonObject.class);
                    if (!output.has("key")) {
                        throw new InvalidObjectException("Key is not given in Hastebin output");
                    }
                    sender.sendMessage(ChatColor.GREEN + "We've made a dump with useful information, report your issue and provide this url: " + DumpSubCmd.this.getUrl(output.get("key").getAsString()));
                }
                catch (Exception e2) {
                    sender.sendMessage(ChatColor.RED + "Failed to dump, please check the console for more information");
                    Via.getPlatform().getLogger().log(Level.WARNING, "Could not paste ViaVersion dump to Hastebin", e2);
                    try {
                        if (con.getResponseCode() < 200 || con.getResponseCode() > 400) {
                            final String rawOutput = CharStreams.toString((Readable)new InputStreamReader(con.getErrorStream()));
                            con.getErrorStream().close();
                            Via.getPlatform().getLogger().log(Level.WARNING, "Page returned: " + rawOutput);
                        }
                    }
                    catch (IOException e3) {
                        Via.getPlatform().getLogger().log(Level.WARNING, "Failed to capture further info", e3);
                    }
                }
            }
        });
        return true;
    }
    
    private String getUrl(final String id) {
        return String.format("https://dump.viaversion.com/%s", id);
    }
}
