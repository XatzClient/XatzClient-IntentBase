// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.minecraft.nbt;

import java.util.zip.GZIPOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.OutputStream;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.TagRegistry;
import java.io.BufferedInputStream;
import java.util.zip.GZIPInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;

public final class BinaryTagIO
{
    private BinaryTagIO() {
    }
    
    @NotNull
    public static CompoundTag readPath(@NotNull final Path path) throws IOException {
        return readInputStream(Files.newInputStream(path, new OpenOption[0]));
    }
    
    @NotNull
    public static CompoundTag readInputStream(@NotNull final InputStream input) throws IOException {
        try (final DataInputStream dis = new DataInputStream(input)) {
            return readDataInput(dis);
        }
    }
    
    @NotNull
    public static CompoundTag readCompressedPath(@NotNull final Path path) throws IOException {
        return readCompressedInputStream(Files.newInputStream(path, new OpenOption[0]));
    }
    
    @NotNull
    public static CompoundTag readCompressedInputStream(@NotNull final InputStream input) throws IOException {
        try (final DataInputStream dis = new DataInputStream(new BufferedInputStream(new GZIPInputStream(input)))) {
            return readDataInput(dis);
        }
    }
    
    @NotNull
    public static CompoundTag readDataInput(@NotNull final DataInput input) throws IOException {
        final byte type = input.readByte();
        if (type != TagRegistry.getIdFor(CompoundTag.class)) {
            throw new IOException(String.format("Expected root tag to be a CompoundTag, was %s", type));
        }
        input.skipBytes(input.readUnsignedShort());
        final CompoundTag compoundTag = new CompoundTag("");
        compoundTag.read(input);
        return compoundTag;
    }
    
    public static void writePath(@NotNull final CompoundTag tag, @NotNull final Path path) throws IOException {
        writeOutputStream(tag, Files.newOutputStream(path, new OpenOption[0]));
    }
    
    public static void writeOutputStream(@NotNull final CompoundTag tag, @NotNull final OutputStream output) throws IOException {
        try (final DataOutputStream dos = new DataOutputStream(output)) {
            writeDataOutput(tag, dos);
        }
    }
    
    public static void writeCompressedPath(@NotNull final CompoundTag tag, @NotNull final Path path) throws IOException {
        writeCompressedOutputStream(tag, Files.newOutputStream(path, new OpenOption[0]));
    }
    
    public static void writeCompressedOutputStream(@NotNull final CompoundTag tag, @NotNull final OutputStream output) throws IOException {
        try (final DataOutputStream dos = new DataOutputStream(new GZIPOutputStream(output))) {
            writeDataOutput(tag, dos);
        }
    }
    
    public static void writeDataOutput(@NotNull final CompoundTag tag, @NotNull final DataOutput output) throws IOException {
        output.writeByte(TagRegistry.getIdFor(CompoundTag.class));
        output.writeUTF("");
        tag.write(output);
    }
    
    @NotNull
    public static CompoundTag readString(@NotNull final String input) throws IOException {
        try {
            final CharBuffer buffer = new CharBuffer(input);
            final TagStringReader parser = new TagStringReader(buffer);
            final CompoundTag tag = parser.compound();
            if (buffer.skipWhitespace().hasMore()) {
                throw new IOException("Document had trailing content after first CompoundTag");
            }
            return tag;
        }
        catch (StringTagParseException ex) {
            throw new IOException(ex);
        }
    }
    
    @NotNull
    public static String writeString(@NotNull final CompoundTag tag) throws IOException {
        final StringBuilder sb = new StringBuilder();
        try (final TagStringWriter emit = new TagStringWriter(sb)) {
            emit.writeTag(tag);
        }
        return sb.toString();
    }
}
