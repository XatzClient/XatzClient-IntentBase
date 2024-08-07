// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_9to1_8.storage;

import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.EntityIdProvider;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.metadata.MetadataRewriter1_9To1_8;
import us.myles.ViaVersion.api.type.types.version.Types1_9;
import java.util.Iterator;
import us.myles.ViaVersion.api.boss.BossStyle;
import us.myles.ViaVersion.api.boss.BossColor;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_9;
import java.util.Collection;
import java.util.ArrayList;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.BossBarProvider;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import us.myles.ViaVersion.api.type.Type;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.minecraft.item.Item;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Sets;
import java.util.concurrent.ConcurrentHashMap;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.entities.Entity1_10Types;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.chat.GameMode;
import us.myles.ViaVersion.api.minecraft.Position;
import java.util.Set;
import us.myles.ViaVersion.api.boss.BossBar;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import us.myles.ViaVersion.api.storage.EntityTracker;

public class EntityTracker1_9 extends EntityTracker
{
    private final Map<Integer, UUID> uuidMap;
    private final Map<Integer, List<Metadata>> metadataBuffer;
    private final Map<Integer, Integer> vehicleMap;
    private final Map<Integer, BossBar> bossBarMap;
    private final Set<Integer> validBlocking;
    private final Set<Integer> knownHolograms;
    private final Set<Position> blockInteractions;
    private boolean blocking;
    private boolean autoTeam;
    private Position currentlyDigging;
    private boolean teamExists;
    private GameMode gameMode;
    private String currentTeam;
    
    public EntityTracker1_9(final UserConnection user) {
        super(user, Entity1_10Types.EntityType.PLAYER);
        this.uuidMap = new ConcurrentHashMap<Integer, UUID>();
        this.metadataBuffer = new ConcurrentHashMap<Integer, List<Metadata>>();
        this.vehicleMap = new ConcurrentHashMap<Integer, Integer>();
        this.bossBarMap = new ConcurrentHashMap<Integer, BossBar>();
        this.validBlocking = (Set<Integer>)Sets.newConcurrentHashSet();
        this.knownHolograms = (Set<Integer>)Sets.newConcurrentHashSet();
        this.blockInteractions = Collections.newSetFromMap((Map<Position, Boolean>)CacheBuilder.newBuilder().maximumSize(10L).expireAfterAccess(250L, TimeUnit.MILLISECONDS).build().asMap());
        this.blocking = false;
        this.autoTeam = false;
        this.currentlyDigging = null;
        this.teamExists = false;
    }
    
    public UUID getEntityUUID(final int id) {
        UUID uuid = this.uuidMap.get(id);
        if (uuid == null) {
            uuid = UUID.randomUUID();
            this.uuidMap.put(id, uuid);
        }
        return uuid;
    }
    
    public void setSecondHand(final Item item) {
        this.setSecondHand(this.getClientEntityId(), item);
    }
    
    public void setSecondHand(final int entityID, final Item item) {
        final PacketWrapper wrapper = new PacketWrapper(60, null, this.getUser());
        wrapper.write(Type.VAR_INT, entityID);
        wrapper.write(Type.VAR_INT, 1);
        wrapper.write(Type.ITEM, item);
        try {
            wrapper.send(Protocol1_9To1_8.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void removeEntity(final int entityId) {
        super.removeEntity(entityId);
        this.vehicleMap.remove(entityId);
        this.uuidMap.remove(entityId);
        this.validBlocking.remove(entityId);
        this.knownHolograms.remove(entityId);
        this.metadataBuffer.remove(entityId);
        final BossBar bar = this.bossBarMap.remove(entityId);
        if (bar != null) {
            bar.hide();
            Via.getManager().getProviders().get(BossBarProvider.class).handleRemove(this.getUser(), bar.getId());
        }
    }
    
    public boolean interactedBlockRecently(final int x, final int y, final int z) {
        return this.blockInteractions.contains(new Position(x, (short)y, z));
    }
    
    public void addBlockInteraction(final Position p) {
        this.blockInteractions.add(p);
    }
    
    public void handleMetadata(final int entityId, final List<Metadata> metadataList) {
        final EntityType type = this.getEntity(entityId);
        if (type == null) {
            return;
        }
        for (final Metadata metadata : new ArrayList<Metadata>(metadataList)) {
            if (type == Entity1_10Types.EntityType.WITHER && metadata.getId() == 10) {
                metadataList.remove(metadata);
            }
            if (type == Entity1_10Types.EntityType.ENDER_DRAGON && metadata.getId() == 11) {
                metadataList.remove(metadata);
            }
            if (type == Entity1_10Types.EntityType.SKELETON && this.getMetaByIndex(metadataList, 12) == null) {
                metadataList.add(new Metadata(12, MetaType1_9.Boolean, true));
            }
            if (type == Entity1_10Types.EntityType.HORSE && metadata.getId() == 16 && (int)metadata.getValue() == Integer.MIN_VALUE) {
                metadata.setValue(0);
            }
            if (type == Entity1_10Types.EntityType.PLAYER) {
                if (metadata.getId() == 0) {
                    final byte data = (byte)metadata.getValue();
                    if (entityId != this.getProvidedEntityId() && Via.getConfig().isShieldBlocking()) {
                        if ((data & 0x10) == 0x10) {
                            if (this.validBlocking.contains(entityId)) {
                                final Item shield = new Item(442, (byte)1, (short)0, null);
                                this.setSecondHand(entityId, shield);
                            }
                            else {
                                this.setSecondHand(entityId, null);
                            }
                        }
                        else {
                            this.setSecondHand(entityId, null);
                        }
                    }
                }
                if (metadata.getId() == 12 && Via.getConfig().isLeftHandedHandling()) {
                    metadataList.add(new Metadata(13, MetaType1_9.Byte, (byte)((((byte)metadata.getValue() & 0x80) == 0x0) ? 1 : 0)));
                }
            }
            if (type == Entity1_10Types.EntityType.ARMOR_STAND && Via.getConfig().isHologramPatch() && metadata.getId() == 0 && this.getMetaByIndex(metadataList, 10) != null) {
                final Metadata meta = this.getMetaByIndex(metadataList, 10);
                final byte data2 = (byte)metadata.getValue();
                if ((data2 & 0x20) == 0x20 && ((byte)meta.getValue() & 0x1) == 0x1 && !((String)this.getMetaByIndex(metadataList, 2).getValue()).isEmpty() && (boolean)this.getMetaByIndex(metadataList, 3).getValue() && !this.knownHolograms.contains(entityId)) {
                    this.knownHolograms.add(entityId);
                    try {
                        final PacketWrapper wrapper = new PacketWrapper(37, null, this.getUser());
                        wrapper.write(Type.VAR_INT, entityId);
                        wrapper.write(Type.SHORT, (Short)0);
                        wrapper.write(Type.SHORT, (short)(128.0 * (Via.getConfig().getHologramYOffset() * 32.0)));
                        wrapper.write(Type.SHORT, (Short)0);
                        wrapper.write(Type.BOOLEAN, true);
                        wrapper.send(Protocol1_9To1_8.class, true, false);
                    }
                    catch (Exception ex) {}
                }
            }
            if (Via.getConfig().isBossbarPatch() && (type == Entity1_10Types.EntityType.ENDER_DRAGON || type == Entity1_10Types.EntityType.WITHER)) {
                if (metadata.getId() == 2) {
                    BossBar bar = this.bossBarMap.get(entityId);
                    String title = (String)metadata.getValue();
                    title = (title.isEmpty() ? ((type == Entity1_10Types.EntityType.ENDER_DRAGON) ? "Ender Dragon" : "Wither") : title);
                    if (bar == null) {
                        bar = Via.getAPI().createBossBar(title, BossColor.PINK, BossStyle.SOLID);
                        this.bossBarMap.put(entityId, bar);
                        bar.addConnection(this.getUser());
                        bar.show();
                        Via.getManager().getProviders().get(BossBarProvider.class).handleAdd(this.getUser(), bar.getId());
                    }
                    else {
                        bar.setTitle(title);
                    }
                }
                else {
                    if (metadata.getId() != 6 || Via.getConfig().isBossbarAntiflicker()) {
                        continue;
                    }
                    BossBar bar = this.bossBarMap.get(entityId);
                    final float maxHealth = (type == Entity1_10Types.EntityType.ENDER_DRAGON) ? 200.0f : 300.0f;
                    final float health = Math.max(0.0f, Math.min((float)metadata.getValue() / maxHealth, 1.0f));
                    if (bar == null) {
                        final String title2 = (type == Entity1_10Types.EntityType.ENDER_DRAGON) ? "Ender Dragon" : "Wither";
                        bar = Via.getAPI().createBossBar(title2, health, BossColor.PINK, BossStyle.SOLID);
                        this.bossBarMap.put(entityId, bar);
                        bar.addConnection(this.getUser());
                        bar.show();
                        Via.getManager().getProviders().get(BossBarProvider.class).handleAdd(this.getUser(), bar.getId());
                    }
                    else {
                        bar.setHealth(health);
                    }
                }
            }
        }
    }
    
    public Metadata getMetaByIndex(final List<Metadata> list, final int index) {
        for (final Metadata meta : list) {
            if (index == meta.getId()) {
                return meta;
            }
        }
        return null;
    }
    
    public void sendTeamPacket(final boolean add, final boolean now) {
        final PacketWrapper wrapper = new PacketWrapper(65, null, this.getUser());
        wrapper.write(Type.STRING, "viaversion");
        if (add) {
            if (!this.teamExists) {
                wrapper.write(Type.BYTE, (Byte)0);
                wrapper.write(Type.STRING, "viaversion");
                wrapper.write(Type.STRING, "§f");
                wrapper.write(Type.STRING, "");
                wrapper.write(Type.BYTE, (Byte)0);
                wrapper.write(Type.STRING, "");
                wrapper.write(Type.STRING, "never");
                wrapper.write(Type.BYTE, (Byte)15);
            }
            else {
                wrapper.write(Type.BYTE, (Byte)3);
            }
            wrapper.write(Type.STRING_ARRAY, new String[] { this.getUser().getProtocolInfo().getUsername() });
        }
        else {
            wrapper.write(Type.BYTE, (Byte)1);
        }
        this.teamExists = add;
        try {
            wrapper.send(Protocol1_9To1_8.class, true, now);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void addMetadataToBuffer(final int entityID, final List<Metadata> metadataList) {
        final List<Metadata> metadata = this.metadataBuffer.get(entityID);
        if (metadata != null) {
            metadata.addAll(metadataList);
        }
        else {
            this.metadataBuffer.put(entityID, metadataList);
        }
    }
    
    public void sendMetadataBuffer(final int entityId) {
        final List<Metadata> metadataList = this.metadataBuffer.get(entityId);
        if (metadataList != null) {
            final PacketWrapper wrapper = new PacketWrapper(57, null, this.getUser());
            wrapper.write(Type.VAR_INT, entityId);
            wrapper.write(Types1_9.METADATA_LIST, metadataList);
            this.getUser().getProtocolInfo().getPipeline().getProtocol(Protocol1_9To1_8.class).get(MetadataRewriter1_9To1_8.class).handleMetadata(entityId, metadataList, this.getUser());
            this.handleMetadata(entityId, metadataList);
            if (!metadataList.isEmpty()) {
                try {
                    wrapper.send(Protocol1_9To1_8.class);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.metadataBuffer.remove(entityId);
        }
    }
    
    public int getProvidedEntityId() {
        try {
            return Via.getManager().getProviders().get(EntityIdProvider.class).getEntityId(this.getUser());
        }
        catch (Exception e) {
            return this.getClientEntityId();
        }
    }
    
    public Map<Integer, UUID> getUuidMap() {
        return this.uuidMap;
    }
    
    public Map<Integer, List<Metadata>> getMetadataBuffer() {
        return this.metadataBuffer;
    }
    
    public Map<Integer, Integer> getVehicleMap() {
        return this.vehicleMap;
    }
    
    public Map<Integer, BossBar> getBossBarMap() {
        return this.bossBarMap;
    }
    
    public Set<Integer> getValidBlocking() {
        return this.validBlocking;
    }
    
    public Set<Integer> getKnownHolograms() {
        return this.knownHolograms;
    }
    
    public Set<Position> getBlockInteractions() {
        return this.blockInteractions;
    }
    
    public boolean isBlocking() {
        return this.blocking;
    }
    
    public void setBlocking(final boolean blocking) {
        this.blocking = blocking;
    }
    
    public boolean isAutoTeam() {
        return this.autoTeam;
    }
    
    public void setAutoTeam(final boolean autoTeam) {
        this.autoTeam = autoTeam;
    }
    
    public Position getCurrentlyDigging() {
        return this.currentlyDigging;
    }
    
    public void setCurrentlyDigging(final Position currentlyDigging) {
        this.currentlyDigging = currentlyDigging;
    }
    
    public boolean isTeamExists() {
        return this.teamExists;
    }
    
    public GameMode getGameMode() {
        return this.gameMode;
    }
    
    public void setGameMode(final GameMode gameMode) {
        this.gameMode = gameMode;
    }
    
    public String getCurrentTeam() {
        return this.currentTeam;
    }
    
    public void setCurrentTeam(final String currentTeam) {
        this.currentTeam = currentTeam;
    }
}
