// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_11to1_10.metadata;

import java.util.Iterator;
import java.util.Optional;
import us.myles.ViaVersion.api.type.Type;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_9;
import us.myles.ViaVersion.api.entities.Entity1_11Types;
import us.myles.ViaVersion.protocols.protocol1_11to1_10.EntityIdRewriter;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.data.UserConnection;
import java.util.List;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.storage.EntityTracker;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_11to1_10.storage.EntityTracker1_11;
import us.myles.ViaVersion.protocols.protocol1_11to1_10.Protocol1_11To1_10;
import us.myles.ViaVersion.api.rewriters.MetadataRewriter;

public class MetadataRewriter1_11To1_10 extends MetadataRewriter
{
    public MetadataRewriter1_11To1_10(final Protocol1_11To1_10 protocol) {
        super(protocol, EntityTracker1_11.class);
    }
    
    @Override
    protected void handleMetadata(final int entityId, final EntityType type, final Metadata metadata, final List<Metadata> metadatas, final UserConnection connection) {
        if (metadata.getValue() instanceof Item) {
            EntityIdRewriter.toClientItem((Item)metadata.getValue());
        }
        if (type == null) {
            return;
        }
        if (type.is(Entity1_11Types.EntityType.ELDER_GUARDIAN) || type.is(Entity1_11Types.EntityType.GUARDIAN)) {
            final int oldid = metadata.getId();
            if (oldid == 12) {
                metadata.setMetaType(MetaType1_9.Boolean);
                final boolean val = ((byte)metadata.getValue() & 0x2) == 0x2;
                metadata.setValue(val);
            }
        }
        if (type.isOrHasParent(Entity1_11Types.EntityType.ABSTRACT_SKELETON)) {
            final int oldid = metadata.getId();
            if (oldid == 12) {
                metadatas.remove(metadata);
            }
            if (oldid == 13) {
                metadata.setId(12);
            }
        }
        if (type.isOrHasParent(Entity1_11Types.EntityType.ZOMBIE)) {
            if (type.is(Entity1_11Types.EntityType.ZOMBIE, Entity1_11Types.EntityType.HUSK) && metadata.getId() == 14) {
                metadatas.remove(metadata);
            }
            else if (metadata.getId() == 15) {
                metadata.setId(14);
            }
            else if (metadata.getId() == 14) {
                metadata.setId(15);
            }
        }
        if (type.isOrHasParent(Entity1_11Types.EntityType.ABSTRACT_HORSE)) {
            final int oldid = metadata.getId();
            if (oldid == 14) {
                metadatas.remove(metadata);
            }
            if (oldid == 16) {
                metadata.setId(14);
            }
            if (oldid == 17) {
                metadata.setId(16);
            }
            if (!type.is(Entity1_11Types.EntityType.HORSE)) {
                if (metadata.getId() == 15 || metadata.getId() == 16) {
                    metadatas.remove(metadata);
                }
            }
            if (type.is(Entity1_11Types.EntityType.DONKEY, Entity1_11Types.EntityType.MULE) && metadata.getId() == 13) {
                if (((byte)metadata.getValue() & 0x8) == 0x8) {
                    metadatas.add(new Metadata(15, MetaType1_9.Boolean, true));
                }
                else {
                    metadatas.add(new Metadata(15, MetaType1_9.Boolean, false));
                }
            }
        }
        if (type.is(Entity1_11Types.EntityType.ARMOR_STAND) && Via.getConfig().isHologramPatch()) {
            final Metadata flags = this.getMetaByIndex(11, metadatas);
            final Metadata customName = this.getMetaByIndex(2, metadatas);
            final Metadata customNameVisible = this.getMetaByIndex(3, metadatas);
            if (metadata.getId() == 0 && flags != null && customName != null && customNameVisible != null) {
                final byte data = (byte)metadata.getValue();
                if ((data & 0x20) == 0x20 && ((byte)flags.getValue() & 0x1) == 0x1 && !((String)customName.getValue()).isEmpty() && (boolean)customNameVisible.getValue()) {
                    final EntityTracker1_11 tracker = connection.get(EntityTracker1_11.class);
                    if (!tracker.isHologram(entityId)) {
                        tracker.addHologram(entityId);
                        try {
                            final PacketWrapper wrapper = new PacketWrapper(37, null, connection);
                            wrapper.write(Type.VAR_INT, entityId);
                            wrapper.write(Type.SHORT, (Short)0);
                            wrapper.write(Type.SHORT, (short)(128.0 * (-Via.getConfig().getHologramYOffset() * 32.0)));
                            wrapper.write(Type.SHORT, (Short)0);
                            wrapper.write(Type.BOOLEAN, true);
                            wrapper.send(Protocol1_11To1_10.class);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
    
    @Override
    protected EntityType getTypeFromId(final int type) {
        return Entity1_11Types.getTypeFromId(type, false);
    }
    
    @Override
    protected EntityType getObjectTypeFromId(final int type) {
        return Entity1_11Types.getTypeFromId(type, true);
    }
    
    public static Entity1_11Types.EntityType rewriteEntityType(final int numType, final List<Metadata> metadata) {
        final Optional<Entity1_11Types.EntityType> optType = Entity1_11Types.EntityType.findById(numType);
        if (!optType.isPresent()) {
            Via.getManager().getPlatform().getLogger().severe("Error: could not find Entity type " + numType + " with metadata: " + metadata);
            return null;
        }
        final Entity1_11Types.EntityType type = optType.get();
        try {
            if (type.is(Entity1_11Types.EntityType.GUARDIAN)) {
                final Optional<Metadata> options = getById(metadata, 12);
                if (options.isPresent() && ((byte)options.get().getValue() & 0x4) == 0x4) {
                    return Entity1_11Types.EntityType.ELDER_GUARDIAN;
                }
            }
            if (type.is(Entity1_11Types.EntityType.SKELETON)) {
                final Optional<Metadata> options = getById(metadata, 12);
                if (options.isPresent()) {
                    if ((int)options.get().getValue() == 1) {
                        return Entity1_11Types.EntityType.WITHER_SKELETON;
                    }
                    if ((int)options.get().getValue() == 2) {
                        return Entity1_11Types.EntityType.STRAY;
                    }
                }
            }
            if (type.is(Entity1_11Types.EntityType.ZOMBIE)) {
                final Optional<Metadata> options = getById(metadata, 13);
                if (options.isPresent()) {
                    final int value = (int)options.get().getValue();
                    if (value > 0 && value < 6) {
                        metadata.add(new Metadata(16, MetaType1_9.VarInt, value - 1));
                        return Entity1_11Types.EntityType.ZOMBIE_VILLAGER;
                    }
                    if (value == 6) {
                        return Entity1_11Types.EntityType.HUSK;
                    }
                }
            }
            if (type.is(Entity1_11Types.EntityType.HORSE)) {
                final Optional<Metadata> options = getById(metadata, 14);
                if (options.isPresent()) {
                    if ((int)options.get().getValue() == 0) {
                        return Entity1_11Types.EntityType.HORSE;
                    }
                    if ((int)options.get().getValue() == 1) {
                        return Entity1_11Types.EntityType.DONKEY;
                    }
                    if ((int)options.get().getValue() == 2) {
                        return Entity1_11Types.EntityType.MULE;
                    }
                    if ((int)options.get().getValue() == 3) {
                        return Entity1_11Types.EntityType.ZOMBIE_HORSE;
                    }
                    if ((int)options.get().getValue() == 4) {
                        return Entity1_11Types.EntityType.SKELETON_HORSE;
                    }
                }
            }
        }
        catch (Exception e) {
            if (!Via.getConfig().isSuppressMetadataErrors() || Via.getManager().isDebug()) {
                Via.getPlatform().getLogger().warning("An error occurred with entity type rewriter");
                Via.getPlatform().getLogger().warning("Metadata: " + metadata);
                e.printStackTrace();
            }
        }
        return type;
    }
    
    public static Optional<Metadata> getById(final List<Metadata> metadatas, final int id) {
        for (final Metadata metadata : metadatas) {
            if (metadata.getId() == id) {
                return Optional.of(metadata);
            }
        }
        return Optional.empty();
    }
}
