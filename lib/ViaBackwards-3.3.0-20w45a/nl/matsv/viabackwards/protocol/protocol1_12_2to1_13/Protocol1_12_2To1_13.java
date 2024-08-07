// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13;

import us.myles.ViaVersion.api.data.MappingData;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.storage.PlayerPositionStorage1_13;
import nl.matsv.viabackwards.ViaBackwards;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.storage.TabCompleteStorage;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.storage.BackwardsBlockStorage;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.packets.SoundPackets1_13;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.packets.PlayerPacket1_13;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.packets.EntityPackets1_13;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.viaversion.libs.gson.JsonObject;
import nl.matsv.viabackwards.api.rewriters.TranslatableRewriter;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.platform.providers.Provider;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.providers.BackwardsBlockEntityProvider;
import us.myles.ViaVersion.api.Via;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.data.PaintingMapping;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.packets.BlockItemPackets1_13;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.data.BackwardsMappings;
import us.myles.ViaVersion.protocols.protocol1_12_1to1_12.ServerboundPackets1_12_1;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import us.myles.ViaVersion.protocols.protocol1_12_1to1_12.ClientboundPackets1_12_1;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import nl.matsv.viabackwards.api.BackwardsProtocol;

public class Protocol1_12_2To1_13 extends BackwardsProtocol<ClientboundPackets1_13, ClientboundPackets1_12_1, ServerboundPackets1_13, ServerboundPackets1_12_1>
{
    public static final BackwardsMappings MAPPINGS;
    private BlockItemPackets1_13 blockItemPackets;
    
    public Protocol1_12_2To1_13() {
        super(ClientboundPackets1_13.class, ClientboundPackets1_12_1.class, ServerboundPackets1_13.class, ServerboundPackets1_12_1.class);
    }
    
    protected void registerPackets() {
        this.executeAsyncAfterLoaded((Class<? extends Protocol>)Protocol1_13To1_12_2.class, () -> {
            Protocol1_12_2To1_13.MAPPINGS.load();
            PaintingMapping.init();
            Via.getManager().getProviders().register((Class)BackwardsBlockEntityProvider.class, (Provider)new BackwardsBlockEntityProvider());
            return;
        });
        final TranslatableRewriter translatableRewriter = new TranslatableRewriter(this) {
            @Override
            protected void handleTranslate(final JsonObject root, final String translate) {
                String newTranslate = this.newTranslatables.get(translate);
                if (newTranslate != null || (newTranslate = Protocol1_12_2To1_13.this.getMappingData().getTranslateMappings().get(translate)) != null) {
                    root.addProperty("translate", newTranslate);
                }
            }
        };
        translatableRewriter.registerPing();
        translatableRewriter.registerBossBar((ClientboundPacketType)ClientboundPackets1_13.BOSSBAR);
        translatableRewriter.registerChatMessage((ClientboundPacketType)ClientboundPackets1_13.CHAT_MESSAGE);
        translatableRewriter.registerLegacyOpenWindow((ClientboundPacketType)ClientboundPackets1_13.OPEN_WINDOW);
        translatableRewriter.registerDisconnect((ClientboundPacketType)ClientboundPackets1_13.DISCONNECT);
        translatableRewriter.registerCombatEvent((ClientboundPacketType)ClientboundPackets1_13.COMBAT_EVENT);
        translatableRewriter.registerTitle((ClientboundPacketType)ClientboundPackets1_13.TITLE);
        translatableRewriter.registerTabList((ClientboundPacketType)ClientboundPackets1_13.TAB_LIST);
        (this.blockItemPackets = new BlockItemPackets1_13(this)).register();
        new EntityPackets1_13(this).register();
        new PlayerPacket1_13(this).register();
        new SoundPackets1_13(this).register();
        this.cancelOutgoing((ClientboundPacketType)ClientboundPackets1_13.DECLARE_COMMANDS);
        this.cancelOutgoing((ClientboundPacketType)ClientboundPackets1_13.NBT_QUERY);
        this.cancelOutgoing((ClientboundPacketType)ClientboundPackets1_13.CRAFT_RECIPE_RESPONSE);
        this.cancelOutgoing((ClientboundPacketType)ClientboundPackets1_13.UNLOCK_RECIPES);
        this.cancelOutgoing((ClientboundPacketType)ClientboundPackets1_13.ADVANCEMENTS);
        this.cancelOutgoing((ClientboundPacketType)ClientboundPackets1_13.DECLARE_RECIPES);
        this.cancelOutgoing((ClientboundPacketType)ClientboundPackets1_13.TAGS);
        this.cancelIncoming((ServerboundPacketType)ServerboundPackets1_12_1.CRAFT_RECIPE_REQUEST);
        this.cancelIncoming((ServerboundPacketType)ServerboundPackets1_12_1.RECIPE_BOOK_DATA);
    }
    
    public void init(final UserConnection user) {
        if (!user.has((Class)ClientWorld.class)) {
            user.put((StoredObject)new ClientWorld(user));
        }
        if (!user.has((Class)EntityTracker.class)) {
            user.put((StoredObject)new EntityTracker(user));
        }
        ((EntityTracker)user.get((Class)EntityTracker.class)).initProtocol(this);
        if (!user.has((Class)BackwardsBlockStorage.class)) {
            user.put((StoredObject)new BackwardsBlockStorage(user));
        }
        if (!user.has((Class)TabCompleteStorage.class)) {
            user.put((StoredObject)new TabCompleteStorage(user));
        }
        if (ViaBackwards.getConfig().isFix1_13FacePlayer() && !user.has((Class)PlayerPositionStorage1_13.class)) {
            user.put((StoredObject)new PlayerPositionStorage1_13(user));
        }
    }
    
    public BlockItemPackets1_13 getBlockItemPackets() {
        return this.blockItemPackets;
    }
    
    @Override
    public BackwardsMappings getMappingData() {
        return Protocol1_12_2To1_13.MAPPINGS;
    }
    
    static {
        MAPPINGS = new BackwardsMappings();
    }
}
