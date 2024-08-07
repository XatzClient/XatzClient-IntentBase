// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.chunks;

import java.util.zip.Deflater;
import us.myles.ViaVersion.api.type.types.CustomByteType;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.PacketWrapper;
import io.netty.buffer.ByteBuf;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.items.ReplacementRegistry1_7_6_10to1_8;
import de.gerrygames.viarewind.storage.BlockState;
import io.netty.buffer.Unpooled;

public class ChunkPacketTransformer
{
    private static byte[] transformChunkData(byte[] data, final int primaryBitMask, final boolean skyLight, final boolean groundUp) {
        int dataSize = 0;
        final ByteBuf buf = Unpooled.buffer();
        final ByteBuf blockDataBuf = Unpooled.buffer();
        for (int i = 0; i < 16; ++i) {
            if ((primaryBitMask & 1 << i) != 0x0) {
                byte tmp = 0;
                for (int j = 0; j < 4096; ++j) {
                    final short blockData = (short)((data[dataSize + 1] & 0xFF) << 8 | (data[dataSize] & 0xFF));
                    dataSize += 2;
                    BlockState state = BlockState.rawToState(blockData);
                    state = ReplacementRegistry1_7_6_10to1_8.replace(state);
                    buf.writeByte(state.getId());
                    if (j % 2 == 0) {
                        tmp = (byte)(state.getData() & 0xF);
                    }
                    else {
                        blockDataBuf.writeByte(tmp | (state.getData() & 0xF) << 4);
                    }
                }
            }
        }
        buf.writeBytes(blockDataBuf);
        blockDataBuf.release();
        final int columnCount = Integer.bitCount(primaryBitMask);
        buf.writeBytes(data, dataSize, 2048 * columnCount);
        dataSize += 2048 * columnCount;
        if (skyLight) {
            buf.writeBytes(data, dataSize, 2048 * columnCount);
            dataSize += 2048 * columnCount;
        }
        if (groundUp && dataSize + 256 <= data.length) {
            buf.writeBytes(data, dataSize, 256);
            dataSize += 256;
        }
        data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        buf.release();
        return data;
    }
    
    private static int calcSize(final int i, final boolean flag, final boolean flag1) {
        final int j = i * 2 * 16 * 16 * 16;
        final int k = i * 16 * 16 * 16 / 2;
        final int l = flag ? (i * 16 * 16 * 16 / 2) : 0;
        final int i2 = flag1 ? 256 : 0;
        return j + k + l + i2;
    }
    
    public static void transformChunkBulk(final PacketWrapper packetWrapper) throws Exception {
        final boolean skyLightSent = (boolean)packetWrapper.read(Type.BOOLEAN);
        final int columnCount = (int)packetWrapper.read((Type)Type.VAR_INT);
        final int[] chunkX = new int[columnCount];
        final int[] chunkZ = new int[columnCount];
        final int[] primaryBitMask = new int[columnCount];
        final byte[][] data = new byte[columnCount][];
        for (int i = 0; i < columnCount; ++i) {
            chunkX[i] = (int)packetWrapper.read(Type.INT);
            chunkZ[i] = (int)packetWrapper.read(Type.INT);
            primaryBitMask[i] = (int)packetWrapper.read(Type.UNSIGNED_SHORT);
        }
        int totalSize = 0;
        for (int j = 0; j < columnCount; ++j) {
            final int size = calcSize(Integer.bitCount(primaryBitMask[j]), skyLightSent, true);
            final CustomByteType customByteType = new CustomByteType(Integer.valueOf(size));
            data[j] = transformChunkData((byte[])packetWrapper.read((Type)customByteType), primaryBitMask[j], skyLightSent, true);
            totalSize += data[j].length;
        }
        packetWrapper.write((Type)Type.SHORT, (Object)(short)columnCount);
        final byte[] buildBuffer = new byte[totalSize];
        int bufferLocation = 0;
        for (int k = 0; k < columnCount; ++k) {
            System.arraycopy(data[k], 0, buildBuffer, bufferLocation, data[k].length);
            bufferLocation += data[k].length;
        }
        final Deflater deflater = new Deflater(4);
        deflater.reset();
        deflater.setInput(buildBuffer);
        deflater.finish();
        final byte[] buffer = new byte[buildBuffer.length + 100];
        final int compressedSize = deflater.deflate(buffer);
        final byte[] finalBuffer = new byte[compressedSize];
        System.arraycopy(buffer, 0, finalBuffer, 0, compressedSize);
        packetWrapper.write(Type.INT, (Object)compressedSize);
        packetWrapper.write(Type.BOOLEAN, (Object)skyLightSent);
        final CustomByteType customByteType2 = new CustomByteType(Integer.valueOf(compressedSize));
        packetWrapper.write((Type)customByteType2, (Object)finalBuffer);
        for (int l = 0; l < columnCount; ++l) {
            packetWrapper.write(Type.INT, (Object)chunkX[l]);
            packetWrapper.write(Type.INT, (Object)chunkZ[l]);
            packetWrapper.write((Type)Type.SHORT, (Object)(short)primaryBitMask[l]);
            packetWrapper.write((Type)Type.SHORT, (Object)0);
        }
    }
}
