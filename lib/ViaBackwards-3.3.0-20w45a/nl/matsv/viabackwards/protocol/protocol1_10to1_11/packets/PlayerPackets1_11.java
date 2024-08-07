// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_10to1_11.packets;

import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ServerboundPackets1_9_3;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.viaversion.libs.bungeecordchat.api.chat.BaseComponent;
import us.myles.viaversion.libs.gson.JsonObject;
import us.myles.viaversion.libs.bungeecordchat.api.chat.TextComponent;
import us.myles.viaversion.libs.bungeecordchat.chat.ComponentSerializer;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import nl.matsv.viabackwards.protocol.protocol1_10to1_11.Protocol1_10To1_11;
import us.myles.ViaVersion.api.remapper.ValueTransformer;

public class PlayerPackets1_11
{
    private static final ValueTransformer<Short, Float> TO_NEW_FLOAT;
    
    public void register(final Protocol1_10To1_11 protocol) {
        protocol.registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.TITLE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.handler(wrapper -> {
                    final int action = (int)wrapper.get((Type)Type.VAR_INT, 0);
                    if (action == 2) {
                        JsonElement message = (JsonElement)wrapper.read(Type.COMPONENT);
                        wrapper.clearPacket();
                        wrapper.setId(ClientboundPackets1_9_3.CHAT_MESSAGE.ordinal());
                        final BaseComponent[] parsed = ComponentSerializer.parse(message.toString());
                        final String legacy = TextComponent.toLegacyText(parsed);
                        message = (JsonElement)new JsonObject();
                        message.getAsJsonObject().addProperty("text", legacy);
                        wrapper.write(Type.COMPONENT, (Object)message);
                        wrapper.write(Type.BYTE, (Object)2);
                    }
                    else if (action > 2) {
                        wrapper.set((Type)Type.VAR_INT, 0, (Object)(action - 1));
                    }
                });
            }
        });
        protocol.registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.COLLECT_ITEM, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map((Type)Type.VAR_INT);
                this.handler(wrapper -> {
                    final Integer n = (Integer)wrapper.read((Type)Type.VAR_INT);
                });
            }
        });
        protocol.registerIncoming((ServerboundPacketType)ServerboundPackets1_9_3.PLAYER_BLOCK_PLACEMENT, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.POSITION);
                this.map((Type)Type.VAR_INT);
                this.map((Type)Type.VAR_INT);
                this.map(Type.UNSIGNED_BYTE, PlayerPackets1_11.TO_NEW_FLOAT);
                this.map(Type.UNSIGNED_BYTE, PlayerPackets1_11.TO_NEW_FLOAT);
                this.map(Type.UNSIGNED_BYTE, PlayerPackets1_11.TO_NEW_FLOAT);
            }
        });
    }
    
    static {
        TO_NEW_FLOAT = new ValueTransformer<Short, Float>((Type)Type.FLOAT) {
            public Float transform(final PacketWrapper wrapper, final Short inputValue) throws Exception {
                return inputValue / 15.0f;
            }
        };
    }
}
