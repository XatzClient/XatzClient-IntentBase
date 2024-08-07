// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_13to1_13_1.packets;

import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.types.Chunk1_13Type;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import us.myles.ViaVersion.api.rewriters.BlockRewriter;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.protocol.Protocol;

public class WorldPackets1_13_1
{
    public static void register(final Protocol protocol) {
        final BlockRewriter blockRewriter = new BlockRewriter(protocol, Type.POSITION);
        protocol.registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.CHUNK_DATA, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ClientWorld clientWorld = (ClientWorld)wrapper.user().get((Class)ClientWorld.class);
                        final Chunk chunk = (Chunk)wrapper.passthrough((Type)new Chunk1_13Type(clientWorld));
                        for (final ChunkSection section : chunk.getSections()) {
                            if (section != null) {
                                for (int i = 0; i < section.getPaletteSize(); ++i) {
                                    section.setPaletteEntry(i, protocol.getMappingData().getNewBlockStateId(section.getPaletteEntry(i)));
                                }
                            }
                        }
                    }
                });
            }
        });
        blockRewriter.registerBlockAction((ClientboundPacketType)ClientboundPackets1_13.BLOCK_ACTION);
        blockRewriter.registerBlockChange((ClientboundPacketType)ClientboundPackets1_13.BLOCK_CHANGE);
        blockRewriter.registerMultiBlockChange((ClientboundPacketType)ClientboundPackets1_13.MULTI_BLOCK_CHANGE);
        blockRewriter.registerEffect((ClientboundPacketType)ClientboundPackets1_13.EFFECT, 1010, 2001);
    }
}
