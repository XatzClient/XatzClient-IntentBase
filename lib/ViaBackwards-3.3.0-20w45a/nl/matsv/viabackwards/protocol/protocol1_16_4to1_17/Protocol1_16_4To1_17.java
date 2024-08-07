// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_16_4to1_17;

import us.myles.ViaVersion.api.data.MappingData;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.rewriters.StatisticsRewriter;
import us.myles.ViaVersion.api.rewriters.IdRewriteFunction;
import us.myles.ViaVersion.api.rewriters.TagRewriter;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import nl.matsv.viabackwards.api.rewriters.SoundRewriter;
import nl.matsv.viabackwards.api.rewriters.TranslatableRewriter;
import nl.matsv.viabackwards.protocol.protocol1_16_4to1_17.packets.BlockItemPackets1_17;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_17to1_16_4.Protocol1_17To1_16_4;
import nl.matsv.viabackwards.api.data.BackwardsMappings;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.ServerboundPackets1_16_2;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import nl.matsv.viabackwards.api.BackwardsProtocol;

public class Protocol1_16_4To1_17 extends BackwardsProtocol<ClientboundPackets1_16_2, ClientboundPackets1_16_2, ServerboundPackets1_16_2, ServerboundPackets1_16_2>
{
    public static final BackwardsMappings MAPPINGS;
    
    public Protocol1_16_4To1_17() {
        super(ClientboundPackets1_16_2.class, ClientboundPackets1_16_2.class, ServerboundPackets1_16_2.class, ServerboundPackets1_16_2.class);
    }
    
    protected void registerPackets() {
        this.executeAsyncAfterLoaded((Class<? extends Protocol>)Protocol1_17To1_16_4.class, Protocol1_16_4To1_17.MAPPINGS::load);
        new BlockItemPackets1_17(this, null).register();
        final SoundRewriter soundRewriter = new SoundRewriter(this);
        soundRewriter.registerSound((ClientboundPacketType)ClientboundPackets1_16_2.SOUND);
        soundRewriter.registerSound((ClientboundPacketType)ClientboundPackets1_16_2.ENTITY_SOUND);
        soundRewriter.registerNamedSound((ClientboundPacketType)ClientboundPackets1_16_2.NAMED_SOUND);
        soundRewriter.registerStopSound((ClientboundPacketType)ClientboundPackets1_16_2.STOP_SOUND);
        new TagRewriter((Protocol)this, (IdRewriteFunction)null).register((ClientboundPacketType)ClientboundPackets1_16_2.TAGS);
        new StatisticsRewriter((Protocol)this, (IdRewriteFunction)null).register((ClientboundPacketType)ClientboundPackets1_16_2.STATISTICS);
        this.registerOutgoing((ClientboundPacketType)ClientboundPackets1_16_2.RESOURCE_PACK, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    wrapper.passthrough(Type.STRING);
                    wrapper.passthrough(Type.STRING);
                    wrapper.read(Type.BOOLEAN);
                });
            }
        });
    }
    
    @Override
    public BackwardsMappings getMappingData() {
        return Protocol1_16_4To1_17.MAPPINGS;
    }
    
    static {
        MAPPINGS = new BackwardsMappings("1.17", "1.16.2", (Class<? extends Protocol>)Protocol1_17To1_16_4.class, true);
    }
}
