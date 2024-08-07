// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_14to1_13_2.packets;

import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ListTag;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import us.myles.ViaVersion.api.protocol.Protocol;

public class PlayerPackets
{
    public static void register(final Protocol protocol) {
        protocol.registerOutgoing(ClientboundPackets1_13.OPEN_SIGN_EDITOR, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION, Type.POSITION1_14);
            }
        });
        protocol.registerIncoming(ServerboundPackets1_14.QUERY_BLOCK_NBT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.POSITION1_14, Type.POSITION);
            }
        });
        protocol.registerIncoming(ServerboundPackets1_14.EDIT_BOOK, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final Item item = wrapper.passthrough(Type.FLAT_VAR_INT_ITEM);
                        InventoryPackets.toServer(item);
                        if (Via.getConfig().isTruncate1_14Books()) {
                            if (item == null) {
                                return;
                            }
                            final CompoundTag tag = item.getTag();
                            if (tag == null) {
                                return;
                            }
                            final Tag pages = tag.get("pages");
                            if (!(pages instanceof ListTag)) {
                                return;
                            }
                            final ListTag listTag = (ListTag)pages;
                            if (listTag.size() <= 50) {
                                return;
                            }
                            listTag.setValue(listTag.getValue().subList(0, 50));
                        }
                    }
                });
            }
        });
        protocol.registerIncoming(ServerboundPackets1_14.PLAYER_DIGGING, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.POSITION1_14, Type.POSITION);
                this.map(Type.BYTE);
            }
        });
        protocol.registerIncoming(ServerboundPackets1_14.RECIPE_BOOK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int type = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        if (type == 0) {
                            wrapper.passthrough(Type.STRING);
                        }
                        else if (type == 1) {
                            wrapper.passthrough(Type.BOOLEAN);
                            wrapper.passthrough(Type.BOOLEAN);
                            wrapper.passthrough(Type.BOOLEAN);
                            wrapper.passthrough(Type.BOOLEAN);
                            wrapper.read(Type.BOOLEAN);
                            wrapper.read(Type.BOOLEAN);
                            wrapper.read(Type.BOOLEAN);
                            wrapper.read(Type.BOOLEAN);
                        }
                    }
                });
            }
        });
        protocol.registerIncoming(ServerboundPackets1_14.UPDATE_COMMAND_BLOCK, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION1_14, Type.POSITION);
            }
        });
        protocol.registerIncoming(ServerboundPackets1_14.UPDATE_STRUCTURE_BLOCK, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION1_14, Type.POSITION);
            }
        });
        protocol.registerIncoming(ServerboundPackets1_14.UPDATE_SIGN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION1_14, Type.POSITION);
            }
        });
        protocol.registerIncoming(ServerboundPackets1_14.PLAYER_BLOCK_PLACEMENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int hand = wrapper.read((Type<Integer>)Type.VAR_INT);
                        final Position position = wrapper.read(Type.POSITION1_14);
                        final int face = wrapper.read((Type<Integer>)Type.VAR_INT);
                        final float x = wrapper.read((Type<Float>)Type.FLOAT);
                        final float y = wrapper.read((Type<Float>)Type.FLOAT);
                        final float z = wrapper.read((Type<Float>)Type.FLOAT);
                        wrapper.read(Type.BOOLEAN);
                        wrapper.write(Type.POSITION, position);
                        wrapper.write(Type.VAR_INT, face);
                        wrapper.write(Type.VAR_INT, hand);
                        wrapper.write(Type.FLOAT, x);
                        wrapper.write(Type.FLOAT, y);
                        wrapper.write(Type.FLOAT, z);
                    }
                });
            }
        });
    }
}
