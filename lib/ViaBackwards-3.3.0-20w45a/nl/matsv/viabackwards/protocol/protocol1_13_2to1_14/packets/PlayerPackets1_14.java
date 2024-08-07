// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.packets;

import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.Protocol1_13_2To1_14;
import nl.matsv.viabackwards.api.rewriters.Rewriter;

public class PlayerPackets1_14 extends Rewriter<Protocol1_13_2To1_14>
{
    public PlayerPackets1_14(final Protocol1_13_2To1_14 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.SERVER_DIFFICULTY, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.BOOLEAN, Type.NOTHING);
            }
        });
        ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.OPEN_SIGN_EDITOR, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.POSITION1_14, Type.POSITION);
            }
        });
        ((Protocol1_13_2To1_14)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_13.QUERY_BLOCK_NBT, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.POSITION, Type.POSITION1_14);
            }
        });
        ((Protocol1_13_2To1_14)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_13.PLAYER_DIGGING, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.POSITION, Type.POSITION1_14);
                this.map(Type.BYTE);
            }
        });
        ((Protocol1_13_2To1_14)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_13.RECIPE_BOOK_DATA, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int type = (int)wrapper.get((Type)Type.VAR_INT, 0);
                        if (type == 0) {
                            wrapper.passthrough(Type.STRING);
                        }
                        else if (type == 1) {
                            wrapper.passthrough(Type.BOOLEAN);
                            wrapper.passthrough(Type.BOOLEAN);
                            wrapper.passthrough(Type.BOOLEAN);
                            wrapper.passthrough(Type.BOOLEAN);
                            wrapper.write(Type.BOOLEAN, (Object)false);
                            wrapper.write(Type.BOOLEAN, (Object)false);
                            wrapper.write(Type.BOOLEAN, (Object)false);
                            wrapper.write(Type.BOOLEAN, (Object)false);
                        }
                    }
                });
            }
        });
        ((Protocol1_13_2To1_14)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_13.UPDATE_COMMAND_BLOCK, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.POSITION, Type.POSITION1_14);
            }
        });
        ((Protocol1_13_2To1_14)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_13.UPDATE_STRUCTURE_BLOCK, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.POSITION, Type.POSITION1_14);
            }
        });
        ((Protocol1_13_2To1_14)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_13.UPDATE_SIGN, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.POSITION, Type.POSITION1_14);
            }
        });
        ((Protocol1_13_2To1_14)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_13.PLAYER_BLOCK_PLACEMENT, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final Position position = (Position)wrapper.read(Type.POSITION);
                        final int face = (int)wrapper.read((Type)Type.VAR_INT);
                        final int hand = (int)wrapper.read((Type)Type.VAR_INT);
                        final float x = (float)wrapper.read((Type)Type.FLOAT);
                        final float y = (float)wrapper.read((Type)Type.FLOAT);
                        final float z = (float)wrapper.read((Type)Type.FLOAT);
                        wrapper.write((Type)Type.VAR_INT, (Object)hand);
                        wrapper.write(Type.POSITION1_14, (Object)position);
                        wrapper.write((Type)Type.VAR_INT, (Object)face);
                        wrapper.write((Type)Type.FLOAT, (Object)x);
                        wrapper.write((Type)Type.FLOAT, (Object)y);
                        wrapper.write((Type)Type.FLOAT, (Object)z);
                        wrapper.write(Type.BOOLEAN, (Object)false);
                    }
                });
            }
        });
    }
}
