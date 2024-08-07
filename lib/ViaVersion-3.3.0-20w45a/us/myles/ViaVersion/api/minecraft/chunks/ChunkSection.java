// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.minecraft.chunks;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.Nullable;
import us.myles.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;
import us.myles.viaversion.libs.fastutil.ints.IntArrayList;
import us.myles.viaversion.libs.fastutil.ints.Int2IntMap;
import us.myles.viaversion.libs.fastutil.ints.IntList;

public class ChunkSection
{
    public static final int SIZE = 4096;
    public static final int LIGHT_LENGTH = 2048;
    private final IntList palette;
    private final Int2IntMap inversePalette;
    private final int[] blocks;
    private NibbleArray blockLight;
    private NibbleArray skyLight;
    private int nonAirBlocksCount;
    
    public ChunkSection() {
        this.blocks = new int[4096];
        this.blockLight = new NibbleArray(4096);
        this.palette = new IntArrayList();
        (this.inversePalette = new Int2IntOpenHashMap()).defaultReturnValue(-1);
    }
    
    public ChunkSection(final int expectedPaletteLength) {
        this.blocks = new int[4096];
        this.blockLight = new NibbleArray(4096);
        this.palette = new IntArrayList(expectedPaletteLength);
        (this.inversePalette = new Int2IntOpenHashMap(expectedPaletteLength)).defaultReturnValue(-1);
    }
    
    public void setBlock(final int x, final int y, final int z, final int type, final int data) {
        this.setFlatBlock(index(x, y, z), type << 4 | (data & 0xF));
    }
    
    public void setFlatBlock(final int x, final int y, final int z, final int type) {
        this.setFlatBlock(index(x, y, z), type);
    }
    
    public int getBlockId(final int x, final int y, final int z) {
        return this.getFlatBlock(x, y, z) >> 4;
    }
    
    public int getBlockData(final int x, final int y, final int z) {
        return this.getFlatBlock(x, y, z) & 0xF;
    }
    
    public int getFlatBlock(final int x, final int y, final int z) {
        final int index = this.blocks[index(x, y, z)];
        return this.palette.getInt(index);
    }
    
    public int getFlatBlock(final int idx) {
        final int index = this.blocks[idx];
        return this.palette.getInt(index);
    }
    
    public void setBlock(final int idx, final int type, final int data) {
        this.setFlatBlock(idx, type << 4 | (data & 0xF));
    }
    
    public void setPaletteIndex(final int idx, final int index) {
        this.blocks[idx] = index;
    }
    
    public int getPaletteIndex(final int idx) {
        return this.blocks[idx];
    }
    
    public int getPaletteSize() {
        return this.palette.size();
    }
    
    public int getPaletteEntry(final int index) {
        if (index < 0 || index >= this.palette.size()) {
            throw new IndexOutOfBoundsException();
        }
        return this.palette.getInt(index);
    }
    
    public void setPaletteEntry(final int index, final int id) {
        if (index < 0 || index >= this.palette.size()) {
            throw new IndexOutOfBoundsException();
        }
        final int oldId = this.palette.set(index, id);
        if (oldId == id) {
            return;
        }
        this.inversePalette.put(id, index);
        if (this.inversePalette.get(oldId) == index) {
            this.inversePalette.remove(oldId);
            for (int i = 0; i < this.palette.size(); ++i) {
                if (this.palette.getInt(i) == oldId) {
                    this.inversePalette.put(oldId, i);
                    break;
                }
            }
        }
    }
    
    public void replacePaletteEntry(final int oldId, final int newId) {
        final int index = this.inversePalette.remove(oldId);
        if (index == -1) {
            return;
        }
        this.inversePalette.put(newId, index);
        for (int i = 0; i < this.palette.size(); ++i) {
            if (this.palette.getInt(i) == oldId) {
                this.palette.set(i, newId);
            }
        }
    }
    
    public void addPaletteEntry(final int id) {
        this.inversePalette.put(id, this.palette.size());
        this.palette.add(id);
    }
    
    public void clearPalette() {
        this.palette.clear();
        this.inversePalette.clear();
    }
    
    public void setFlatBlock(final int idx, final int id) {
        int index = this.inversePalette.get(id);
        if (index == -1) {
            index = this.palette.size();
            this.palette.add(id);
            this.inversePalette.put(id, index);
        }
        this.blocks[idx] = index;
    }
    
    public void setBlockLight(@Nullable final byte[] data) {
        if (data.length != 2048) {
            throw new IllegalArgumentException("Data length != 2048");
        }
        if (this.blockLight == null) {
            this.blockLight = new NibbleArray(data);
        }
        else {
            this.blockLight.setHandle(data);
        }
    }
    
    public void setSkyLight(@Nullable final byte[] data) {
        if (data.length != 2048) {
            throw new IllegalArgumentException("Data length != 2048");
        }
        if (this.skyLight == null) {
            this.skyLight = new NibbleArray(data);
        }
        else {
            this.skyLight.setHandle(data);
        }
    }
    
    @Nullable
    public byte[] getBlockLight() {
        return (byte[])((this.blockLight == null) ? null : this.blockLight.getHandle());
    }
    
    @Nullable
    public NibbleArray getBlockLightNibbleArray() {
        return this.blockLight;
    }
    
    @Nullable
    public byte[] getSkyLight() {
        return (byte[])((this.skyLight == null) ? null : this.skyLight.getHandle());
    }
    
    @Nullable
    public NibbleArray getSkyLightNibbleArray() {
        return this.skyLight;
    }
    
    public void readBlockLight(final ByteBuf input) {
        if (this.blockLight == null) {
            this.blockLight = new NibbleArray(4096);
        }
        input.readBytes(this.blockLight.getHandle());
    }
    
    public void readSkyLight(final ByteBuf input) {
        if (this.skyLight == null) {
            this.skyLight = new NibbleArray(4096);
        }
        input.readBytes(this.skyLight.getHandle());
    }
    
    public static int index(final int x, final int y, final int z) {
        return y << 8 | z << 4 | x;
    }
    
    public void writeBlockLight(final ByteBuf output) {
        output.writeBytes(this.blockLight.getHandle());
    }
    
    public void writeSkyLight(final ByteBuf output) {
        output.writeBytes(this.skyLight.getHandle());
    }
    
    public boolean hasSkyLight() {
        return this.skyLight != null;
    }
    
    public boolean hasBlockLight() {
        return this.blockLight != null;
    }
    
    public int getNonAirBlocksCount() {
        return this.nonAirBlocksCount;
    }
    
    public void setNonAirBlocksCount(final int nonAirBlocksCount) {
        this.nonAirBlocksCount = nonAirBlocksCount;
    }
}
