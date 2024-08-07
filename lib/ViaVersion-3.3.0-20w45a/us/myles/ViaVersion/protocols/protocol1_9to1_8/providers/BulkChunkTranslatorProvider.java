// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_9to1_8.providers;

import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.type.types.CustomByteType;
import us.myles.ViaVersion.api.type.Type;
import java.util.ArrayList;
import us.myles.ViaVersion.api.PacketWrapper;
import java.util.List;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.storage.ClientChunks;
import us.myles.ViaVersion.api.platform.providers.Provider;

public class BulkChunkTranslatorProvider implements Provider
{
    public List<Object> transformMapChunkBulk(final Object packet, final ClientChunks clientChunks) throws Exception {
        if (!(packet instanceof PacketWrapper)) {
            throw new IllegalArgumentException("The default packet has to be a PacketWrapper for transformMapChunkBulk, unexpected " + packet.getClass());
        }
        final List<Object> packets = new ArrayList<Object>();
        final PacketWrapper wrapper = (PacketWrapper)packet;
        final boolean skyLight = wrapper.read(Type.BOOLEAN);
        final int count = wrapper.read((Type<Integer>)Type.VAR_INT);
        final ChunkBulkSection[] metas = new ChunkBulkSection[count];
        for (int i = 0; i < count; ++i) {
            metas[i] = ChunkBulkSection.read(wrapper, skyLight);
        }
        for (final ChunkBulkSection meta : metas) {
            final CustomByteType customByteType = new CustomByteType(meta.getLength());
            meta.setData(wrapper.read((Type<byte[]>)customByteType));
            final PacketWrapper chunkPacket = new PacketWrapper(33, null, wrapper.user());
            chunkPacket.write(Type.INT, meta.getX());
            chunkPacket.write(Type.INT, meta.getZ());
            chunkPacket.write(Type.BOOLEAN, true);
            chunkPacket.write(Type.UNSIGNED_SHORT, meta.getBitMask());
            chunkPacket.write(Type.VAR_INT, meta.getLength());
            chunkPacket.write((Type<byte[]>)customByteType, meta.getData());
            clientChunks.getBulkChunks().add(ClientChunks.toLong(meta.getX(), meta.getZ()));
            packets.add(chunkPacket);
        }
        return packets;
    }
    
    public boolean isFiltered(final Class<?> packet) {
        return false;
    }
    
    public boolean isPacketLevel() {
        return true;
    }
    
    private static class ChunkBulkSection
    {
        private int x;
        private int z;
        private int bitMask;
        private int length;
        private byte[] data;
        
        public static ChunkBulkSection read(final PacketWrapper wrapper, final boolean skylight) throws Exception {
            final ChunkBulkSection bulkSection = new ChunkBulkSection();
            bulkSection.setX(wrapper.read(Type.INT));
            bulkSection.setZ(wrapper.read(Type.INT));
            bulkSection.setBitMask(wrapper.read(Type.UNSIGNED_SHORT));
            final int bitCount = Integer.bitCount(bulkSection.getBitMask());
            bulkSection.setLength(bitCount * 10240 + (skylight ? (bitCount * 2048) : 0) + 256);
            return bulkSection;
        }
        
        public int getX() {
            return this.x;
        }
        
        public void setX(final int x) {
            this.x = x;
        }
        
        public int getZ() {
            return this.z;
        }
        
        public void setZ(final int z) {
            this.z = z;
        }
        
        public int getBitMask() {
            return this.bitMask;
        }
        
        public void setBitMask(final int bitMask) {
            this.bitMask = bitMask;
        }
        
        public int getLength() {
            return this.length;
        }
        
        public void setLength(final int length) {
            this.length = length;
        }
        
        public byte[] getData() {
            return this.data;
        }
        
        public void setData(final byte[] data) {
            this.data = data;
        }
    }
}
