// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.api.rewriters;

import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.packets.State;
import nl.matsv.viabackwards.ViaBackwards;
import us.myles.ViaVersion.api.protocol.Protocol;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import java.util.Iterator;
import us.myles.viaversion.libs.gson.JsonObject;
import java.util.HashMap;
import us.myles.viaversion.libs.gson.JsonElement;
import nl.matsv.viabackwards.api.data.VBMappingDataLoader;
import java.util.Map;
import us.myles.ViaVersion.api.rewriters.ComponentRewriter;

public class TranslatableRewriter extends ComponentRewriter
{
    private static final Map<String, Map<String, String>> TRANSLATABLES;
    protected final Map<String, String> newTranslatables;
    
    public static void loadTranslatables() {
        final JsonObject jsonObject = VBMappingDataLoader.loadData("translation-mappings.json");
        for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            final Map<String, String> versionMappings = new HashMap<String, String>();
            TranslatableRewriter.TRANSLATABLES.put(entry.getKey(), versionMappings);
            for (final Map.Entry<String, JsonElement> translationEntry : entry.getValue().getAsJsonObject().entrySet()) {
                versionMappings.put(translationEntry.getKey(), translationEntry.getValue().getAsString());
            }
        }
    }
    
    public TranslatableRewriter(final BackwardsProtocol protocol) {
        this(protocol, protocol.getClass().getSimpleName().split("To")[1].replace("_", "."));
    }
    
    public TranslatableRewriter(final BackwardsProtocol protocol, final String sectionIdentifier) {
        super((Protocol)protocol);
        final Map<String, String> newTranslatables = TranslatableRewriter.TRANSLATABLES.get(sectionIdentifier);
        if (newTranslatables == null) {
            ViaBackwards.getPlatform().getLogger().warning("Error loading " + sectionIdentifier + " translatables!");
            this.newTranslatables = new HashMap<String, String>();
        }
        else {
            this.newTranslatables = newTranslatables;
        }
    }
    
    public void registerPing() {
        this.protocol.registerOutgoing(State.LOGIN, 0, 0, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> TranslatableRewriter.this.processText((JsonElement)wrapper.passthrough(Type.COMPONENT)));
            }
        });
    }
    
    public void registerDisconnect(final ClientboundPacketType packetType) {
        this.protocol.registerOutgoing(packetType, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> TranslatableRewriter.this.processText((JsonElement)wrapper.passthrough(Type.COMPONENT)));
            }
        });
    }
    
    public void registerChatMessage(final ClientboundPacketType packetType) {
        this.protocol.registerOutgoing(packetType, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> TranslatableRewriter.this.processText((JsonElement)wrapper.passthrough(Type.COMPONENT)));
            }
        });
    }
    
    public void registerLegacyOpenWindow(final ClientboundPacketType packetType) {
        this.protocol.registerOutgoing(packetType, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.handler(wrapper -> TranslatableRewriter.this.processText((JsonElement)wrapper.passthrough(Type.COMPONENT)));
            }
        });
    }
    
    public void registerOpenWindow(final ClientboundPacketType packetType) {
        this.protocol.registerOutgoing(packetType, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map((Type)Type.VAR_INT);
                this.handler(wrapper -> TranslatableRewriter.this.processText((JsonElement)wrapper.passthrough(Type.COMPONENT)));
            }
        });
    }
    
    public void registerTabList(final ClientboundPacketType packetType) {
        this.protocol.registerOutgoing(packetType, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    TranslatableRewriter.this.processText((JsonElement)wrapper.passthrough(Type.COMPONENT));
                    TranslatableRewriter.this.processText((JsonElement)wrapper.passthrough(Type.COMPONENT));
                });
            }
        });
    }
    
    protected void handleTranslate(final JsonObject root, final String translate) {
        final String newTranslate = this.newTranslatables.get(translate);
        if (newTranslate != null) {
            root.addProperty("translate", newTranslate);
        }
    }
    
    static {
        TRANSLATABLES = new HashMap<String, Map<String, String>>();
    }
}
