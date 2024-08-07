// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_13_1to1_13_2;

import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import nl.matsv.viabackwards.protocol.protocol1_13_1to1_13_2.packets.EntityPackets1_13_2;
import nl.matsv.viabackwards.protocol.protocol1_13_1to1_13_2.packets.WorldPackets1_13_2;
import nl.matsv.viabackwards.protocol.protocol1_13_1to1_13_2.packets.InventoryPackets1_13_2;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import nl.matsv.viabackwards.api.BackwardsProtocol;

public class Protocol1_13_1To1_13_2 extends BackwardsProtocol<ClientboundPackets1_13, ClientboundPackets1_13, ServerboundPackets1_13, ServerboundPackets1_13>
{
    public Protocol1_13_1To1_13_2() {
        super(ClientboundPackets1_13.class, ClientboundPackets1_13.class, ServerboundPackets1_13.class, ServerboundPackets1_13.class);
    }
    
    protected void registerPackets() {
        InventoryPackets1_13_2.register(this);
        WorldPackets1_13_2.register(this);
        EntityPackets1_13_2.register(this);
        this.registerIncoming((ServerboundPacketType)ServerboundPackets1_13.EDIT_BOOK, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.FLAT_ITEM, Type.FLAT_VAR_INT_ITEM);
            }
        });
        this.registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.ADVANCEMENTS, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        wrapper.passthrough(Type.BOOLEAN);
                        for (int size = (int)wrapper.passthrough((Type)Type.VAR_INT), i = 0; i < size; ++i) {
                            wrapper.passthrough(Type.STRING);
                            if (wrapper.passthrough(Type.BOOLEAN)) {
                                wrapper.passthrough(Type.STRING);
                            }
                            if (wrapper.passthrough(Type.BOOLEAN)) {
                                wrapper.passthrough(Type.COMPONENT);
                                wrapper.passthrough(Type.COMPONENT);
                                final Item icon = (Item)wrapper.read(Type.FLAT_VAR_INT_ITEM);
                                wrapper.write(Type.FLAT_ITEM, (Object)icon);
                                wrapper.passthrough((Type)Type.VAR_INT);
                                final int flags = (int)wrapper.passthrough(Type.INT);
                                if ((flags & 0x1) != 0x0) {
                                    wrapper.passthrough(Type.STRING);
                                }
                                wrapper.passthrough((Type)Type.FLOAT);
                                wrapper.passthrough((Type)Type.FLOAT);
                            }
                            wrapper.passthrough(Type.STRING_ARRAY);
                            for (int arrayLength = (int)wrapper.passthrough((Type)Type.VAR_INT), array = 0; array < arrayLength; ++array) {
                                wrapper.passthrough(Type.STRING_ARRAY);
                            }
                        }
                    }
                });
            }
        });
    }
}
