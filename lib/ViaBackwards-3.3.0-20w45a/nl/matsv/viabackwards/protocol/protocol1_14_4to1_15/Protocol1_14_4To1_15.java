// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_14_4to1_15;

import us.myles.ViaVersion.api.data.MappingData;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import us.myles.ViaVersion.api.data.StoredObject;
import nl.matsv.viabackwards.protocol.protocol1_14_4to1_15.data.ImmediateRespawn;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.rewriters.StatisticsRewriter;
import us.myles.ViaVersion.api.rewriters.TagRewriter;
import nl.matsv.viabackwards.protocol.protocol1_14_4to1_15.data.EntityTypeMapping;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import nl.matsv.viabackwards.api.rewriters.SoundRewriter;
import nl.matsv.viabackwards.protocol.protocol1_14_4to1_15.packets.EntityPackets1_15;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import nl.matsv.viabackwards.api.rewriters.TranslatableRewriter;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.Protocol1_15To1_14_4;
import nl.matsv.viabackwards.protocol.protocol1_14_4to1_15.packets.BlockItemPackets1_15;
import nl.matsv.viabackwards.api.data.BackwardsMappings;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import nl.matsv.viabackwards.api.BackwardsProtocol;

public class Protocol1_14_4To1_15 extends BackwardsProtocol<ClientboundPackets1_15, ClientboundPackets1_14, ServerboundPackets1_14, ServerboundPackets1_14>
{
    public static final BackwardsMappings MAPPINGS;
    private BlockItemPackets1_15 blockItemPackets;
    
    public Protocol1_14_4To1_15() {
        super(ClientboundPackets1_15.class, ClientboundPackets1_14.class, ServerboundPackets1_14.class, ServerboundPackets1_14.class);
    }
    
    protected void registerPackets() {
        this.executeAsyncAfterLoaded((Class<? extends Protocol>)Protocol1_15To1_14_4.class, Protocol1_14_4To1_15.MAPPINGS::load);
        final TranslatableRewriter translatableRewriter = new TranslatableRewriter(this);
        translatableRewriter.registerBossBar((ClientboundPacketType)ClientboundPackets1_15.BOSSBAR);
        translatableRewriter.registerChatMessage((ClientboundPacketType)ClientboundPackets1_15.CHAT_MESSAGE);
        translatableRewriter.registerCombatEvent((ClientboundPacketType)ClientboundPackets1_15.COMBAT_EVENT);
        translatableRewriter.registerDisconnect((ClientboundPacketType)ClientboundPackets1_15.DISCONNECT);
        translatableRewriter.registerOpenWindow((ClientboundPacketType)ClientboundPackets1_15.OPEN_WINDOW);
        translatableRewriter.registerTabList((ClientboundPacketType)ClientboundPackets1_15.TAB_LIST);
        translatableRewriter.registerTitle((ClientboundPacketType)ClientboundPackets1_15.TITLE);
        translatableRewriter.registerPing();
        (this.blockItemPackets = new BlockItemPackets1_15(this, translatableRewriter)).register();
        new EntityPackets1_15(this).register();
        final SoundRewriter soundRewriter = new SoundRewriter(this);
        soundRewriter.registerSound((ClientboundPacketType)ClientboundPackets1_15.SOUND);
        soundRewriter.registerSound((ClientboundPacketType)ClientboundPackets1_15.ENTITY_SOUND);
        soundRewriter.registerNamedSound((ClientboundPacketType)ClientboundPackets1_15.NAMED_SOUND);
        soundRewriter.registerStopSound((ClientboundPacketType)ClientboundPackets1_15.STOP_SOUND);
        this.registerOutgoing((ClientboundPacketType)ClientboundPackets1_15.EXPLOSION, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.handler(wrapper -> {
                    final PacketWrapper soundPacket = wrapper.create(81);
                    soundPacket.write((Type)Type.VAR_INT, (Object)243);
                    soundPacket.write((Type)Type.VAR_INT, (Object)4);
                    soundPacket.write(Type.INT, (Object)this.toEffectCoordinate((float)wrapper.get((Type)Type.FLOAT, 0)));
                    soundPacket.write(Type.INT, (Object)this.toEffectCoordinate((float)wrapper.get((Type)Type.FLOAT, 1)));
                    soundPacket.write(Type.INT, (Object)this.toEffectCoordinate((float)wrapper.get((Type)Type.FLOAT, 2)));
                    soundPacket.write((Type)Type.FLOAT, (Object)4.0f);
                    soundPacket.write((Type)Type.FLOAT, (Object)1.0f);
                    soundPacket.send((Class)Protocol1_14_4To1_15.class);
                });
            }
            
            private int toEffectCoordinate(final float coordinate) {
                return (int)(coordinate * 8.0f);
            }
        });
        new TagRewriter((Protocol)this, EntityTypeMapping::getOldEntityId).register((ClientboundPacketType)ClientboundPackets1_15.TAGS);
        new StatisticsRewriter((Protocol)this, EntityTypeMapping::getOldEntityId).register((ClientboundPacketType)ClientboundPackets1_15.STATISTICS);
    }
    
    public void init(final UserConnection user) {
        if (!user.has((Class)ImmediateRespawn.class)) {
            user.put((StoredObject)new ImmediateRespawn(user));
        }
        if (!user.has((Class)EntityTracker.class)) {
            user.put((StoredObject)new EntityTracker(user));
        }
        ((EntityTracker)user.get((Class)EntityTracker.class)).initProtocol(this);
    }
    
    public BlockItemPackets1_15 getBlockItemPackets() {
        return this.blockItemPackets;
    }
    
    @Override
    public BackwardsMappings getMappingData() {
        return Protocol1_14_4To1_15.MAPPINGS;
    }
    
    static {
        MAPPINGS = new BackwardsMappings("1.15", "1.14", (Class<? extends Protocol>)Protocol1_15To1_14_4.class, true);
    }
}
