// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.packets;

import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.type.Type;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_12to1_11_1.ClientboundPackets1_12;
import nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.data.AdvancementTranslations;
import us.myles.viaversion.libs.gson.JsonObject;
import us.myles.ViaVersion.api.rewriters.ComponentRewriter;
import nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.Protocol1_11_1To1_12;
import nl.matsv.viabackwards.api.rewriters.Rewriter;

public class ChatPackets1_12 extends Rewriter<Protocol1_11_1To1_12>
{
    private final ComponentRewriter componentRewriter;
    
    public ChatPackets1_12(final Protocol1_11_1To1_12 protocol) {
        super(protocol);
        this.componentRewriter = new ComponentRewriter() {
            protected void handleTranslate(final JsonObject object, final String translate) {
                final String text = AdvancementTranslations.get(translate);
                if (text != null) {
                    object.addProperty("translate", text);
                }
            }
        };
    }
    
    @Override
    protected void registerPackets() {
        ((Protocol1_11_1To1_12)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_12.CHAT_MESSAGE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    final JsonElement element = (JsonElement)wrapper.passthrough(Type.COMPONENT);
                    ChatPackets1_12.this.componentRewriter.processText(element);
                });
            }
        });
    }
}
