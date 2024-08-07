// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_14_2to1_14_3;

import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.rewriters.RecipeRewriter;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.data.RecipeRewriter1_14;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import nl.matsv.viabackwards.api.BackwardsProtocol;

public class Protocol1_14_2To1_14_3 extends BackwardsProtocol<ClientboundPackets1_14, ClientboundPackets1_14, ServerboundPackets1_14, ServerboundPackets1_14>
{
    public Protocol1_14_2To1_14_3() {
        super(ClientboundPackets1_14.class, ClientboundPackets1_14.class, ServerboundPackets1_14.class, ServerboundPackets1_14.class);
    }
    
    protected void registerPackets() {
        this.registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.TRADE_LIST, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        wrapper.passthrough((Type)Type.VAR_INT);
                        for (int size = (short)wrapper.passthrough(Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                            wrapper.passthrough(Type.FLAT_VAR_INT_ITEM);
                            wrapper.passthrough(Type.FLAT_VAR_INT_ITEM);
                            if (wrapper.passthrough(Type.BOOLEAN)) {
                                wrapper.passthrough(Type.FLAT_VAR_INT_ITEM);
                            }
                            wrapper.passthrough(Type.BOOLEAN);
                            wrapper.passthrough(Type.INT);
                            wrapper.passthrough(Type.INT);
                            wrapper.passthrough(Type.INT);
                            wrapper.passthrough(Type.INT);
                            wrapper.passthrough((Type)Type.FLOAT);
                        }
                        wrapper.passthrough((Type)Type.VAR_INT);
                        wrapper.passthrough((Type)Type.VAR_INT);
                        wrapper.passthrough(Type.BOOLEAN);
                        wrapper.read(Type.BOOLEAN);
                    }
                });
            }
        });
        final RecipeRewriter recipeHandler = (RecipeRewriter)new RecipeRewriter1_14((Protocol)this, item -> {});
        this.registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.DECLARE_RECIPES, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    final int size = (int)wrapper.passthrough((Type)Type.VAR_INT);
                    int deleted = 0;
                    for (int i = 0; i < size; ++i) {
                        final String fullType = (String)wrapper.read(Type.STRING);
                        final String type = fullType.replace("minecraft:", "");
                        final String id = (String)wrapper.read(Type.STRING);
                        if (type.equals("crafting_special_repairitem")) {
                            ++deleted;
                        }
                        else {
                            wrapper.write(Type.STRING, (Object)fullType);
                            wrapper.write(Type.STRING, (Object)id);
                            recipeHandler.handle(wrapper, type);
                        }
                    }
                    wrapper.set((Type)Type.VAR_INT, 0, (Object)(size - deleted));
                });
            }
        });
    }
}
