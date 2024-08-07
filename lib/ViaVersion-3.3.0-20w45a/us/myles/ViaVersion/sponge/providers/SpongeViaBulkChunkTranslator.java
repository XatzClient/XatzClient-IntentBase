// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.sponge.providers;

import java.util.logging.Level;
import us.myles.ViaVersion.api.Via;
import com.google.common.collect.Lists;
import java.util.List;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.storage.ClientChunks;
import us.myles.ViaVersion.util.ReflectionUtil;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.BulkChunkTranslatorProvider;

public class SpongeViaBulkChunkTranslator extends BulkChunkTranslatorProvider
{
    private static ReflectionUtil.ClassReflection mapChunkBulkRef;
    private static ReflectionUtil.ClassReflection mapChunkRef;
    
    @Override
    public List<Object> transformMapChunkBulk(final Object packet, final ClientChunks clientChunks) {
        final List<Object> list = (List<Object>)Lists.newArrayList();
        try {
            final int[] xcoords = SpongeViaBulkChunkTranslator.mapChunkBulkRef.getFieldValue("field_149266_a", packet, int[].class);
            final int[] zcoords = SpongeViaBulkChunkTranslator.mapChunkBulkRef.getFieldValue("field_149264_b", packet, int[].class);
            final Object[] chunkMaps = SpongeViaBulkChunkTranslator.mapChunkBulkRef.getFieldValue("field_179755_c", packet, Object[].class);
            for (int i = 0; i < chunkMaps.length; ++i) {
                final int x = xcoords[i];
                final int z = zcoords[i];
                final Object chunkMap = chunkMaps[i];
                final Object chunkPacket = SpongeViaBulkChunkTranslator.mapChunkRef.newInstance();
                SpongeViaBulkChunkTranslator.mapChunkRef.setFieldValue("field_149284_a", chunkPacket, x);
                SpongeViaBulkChunkTranslator.mapChunkRef.setFieldValue("field_149282_b", chunkPacket, z);
                SpongeViaBulkChunkTranslator.mapChunkRef.setFieldValue("field_179758_c", chunkPacket, chunkMap);
                SpongeViaBulkChunkTranslator.mapChunkRef.setFieldValue("field_149279_g", chunkPacket, true);
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
        return packetClass.getName().endsWith("S26PacketMapChunkBulk");
    }
    
    @Override
    public boolean isPacketLevel() {
        return false;
    }
    
    static {
        try {
            SpongeViaBulkChunkTranslator.mapChunkBulkRef = new ReflectionUtil.ClassReflection(Class.forName("net.minecraft.network.play.server.S26PacketMapChunkBulk"));
            SpongeViaBulkChunkTranslator.mapChunkRef = new ReflectionUtil.ClassReflection(Class.forName("net.minecraft.network.play.server.S21PacketChunkData"));
        }
        catch (ClassNotFoundException ex) {}
        catch (Exception e) {
            Via.getPlatform().getLogger().log(Level.WARNING, "Failed to initialise chunks reflection", e);
        }
    }
}
