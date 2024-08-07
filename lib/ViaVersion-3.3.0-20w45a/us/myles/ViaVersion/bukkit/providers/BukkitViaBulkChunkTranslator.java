// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.bukkit.providers;

import us.myles.ViaVersion.bukkit.util.NMSUtil;
import java.util.logging.Level;
import us.myles.ViaVersion.ViaVersionPlugin;
import us.myles.ViaVersion.api.Via;
import com.google.common.collect.Lists;
import java.util.List;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.storage.ClientChunks;
import java.lang.reflect.Method;
import us.myles.ViaVersion.util.ReflectionUtil;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.BulkChunkTranslatorProvider;

public class BukkitViaBulkChunkTranslator extends BulkChunkTranslatorProvider
{
    private static ReflectionUtil.ClassReflection mapChunkBulkRef;
    private static ReflectionUtil.ClassReflection mapChunkRef;
    private static Method obfuscateRef;
    
    @Override
    public List<Object> transformMapChunkBulk(final Object packet, final ClientChunks clientChunks) {
        final List<Object> list = (List<Object>)Lists.newArrayList();
        try {
            final int[] xcoords = BukkitViaBulkChunkTranslator.mapChunkBulkRef.getFieldValue("a", packet, int[].class);
            final int[] zcoords = BukkitViaBulkChunkTranslator.mapChunkBulkRef.getFieldValue("b", packet, int[].class);
            final Object[] chunkMaps = BukkitViaBulkChunkTranslator.mapChunkBulkRef.getFieldValue("c", packet, Object[].class);
            if (Via.getConfig().isAntiXRay() && ((ViaVersionPlugin)Via.getPlatform()).isSpigot()) {
                try {
                    final Object world = BukkitViaBulkChunkTranslator.mapChunkBulkRef.getFieldValue("world", packet, Object.class);
                    final Object spigotConfig = ReflectionUtil.getPublic(world, "spigotConfig", Object.class);
                    final Object antiXrayInstance = ReflectionUtil.getPublic(spigotConfig, "antiXrayInstance", Object.class);
                    for (int i = 0; i < xcoords.length; ++i) {
                        final Object b = ReflectionUtil.get(chunkMaps[i], "b", Object.class);
                        final Object a = ReflectionUtil.get(chunkMaps[i], "a", Object.class);
                        BukkitViaBulkChunkTranslator.obfuscateRef.invoke(antiXrayInstance, xcoords[i], zcoords[i], b, a, world);
                    }
                }
                catch (Exception ex) {}
            }
            for (int j = 0; j < chunkMaps.length; ++j) {
                final int x = xcoords[j];
                final int z = zcoords[j];
                final Object chunkMap = chunkMaps[j];
                final Object chunkPacket = BukkitViaBulkChunkTranslator.mapChunkRef.newInstance();
                BukkitViaBulkChunkTranslator.mapChunkRef.setFieldValue("a", chunkPacket, x);
                BukkitViaBulkChunkTranslator.mapChunkRef.setFieldValue("b", chunkPacket, z);
                BukkitViaBulkChunkTranslator.mapChunkRef.setFieldValue("c", chunkPacket, chunkMap);
                BukkitViaBulkChunkTranslator.mapChunkRef.setFieldValue("d", chunkPacket, true);
                clientChunks.getBulkChunks().add(ClientChunks.toLong(x, z));
                list.add(chunkPacket);
            }
        }
        catch (Exception e) {
            Via.getPlatform().getLogger().log(Level.WARNING, "Failed to transform chunks bulk", e);
        }
        return list;
    }
    
    @Override
    public boolean isFiltered(final Class<?> packetClass) {
        return packetClass.getName().endsWith("PacketPlayOutMapChunkBulk");
    }
    
    @Override
    public boolean isPacketLevel() {
        return false;
    }
    
    static {
        try {
            BukkitViaBulkChunkTranslator.mapChunkBulkRef = new ReflectionUtil.ClassReflection(NMSUtil.nms("PacketPlayOutMapChunkBulk"));
            BukkitViaBulkChunkTranslator.mapChunkRef = new ReflectionUtil.ClassReflection(NMSUtil.nms("PacketPlayOutMapChunk"));
            if (((ViaVersionPlugin)Via.getPlatform()).isSpigot()) {
                BukkitViaBulkChunkTranslator.obfuscateRef = Class.forName("org.spigotmc.AntiXray").getMethod("obfuscate", Integer.TYPE, Integer.TYPE, Integer.TYPE, byte[].class, NMSUtil.nms("World"));
            }
        }
        catch (ClassNotFoundException ex) {}
        catch (Exception e) {
            Via.getPlatform().getLogger().log(Level.WARNING, "Failed to initialise chunks reflection", e);
        }
    }
}
