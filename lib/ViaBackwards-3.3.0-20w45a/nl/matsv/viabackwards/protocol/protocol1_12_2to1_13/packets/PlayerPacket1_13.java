// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.packets;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;
import us.myles.ViaVersion.protocols.protocol1_12_1to1_12.ServerboundPackets1_12_1;
import nl.matsv.viabackwards.utils.ChatUtil;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ChatRewriter;
import us.myles.viaversion.libs.gson.JsonElement;
import java.util.UUID;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.storage.TabCompleteStorage;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.data.ParticleMapping;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import java.util.List;
import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;
import nl.matsv.viabackwards.ViaBackwards;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.packets.InventoryPackets;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.ValueCreator;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.packets.State;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.Protocol1_12_2To1_13;
import nl.matsv.viabackwards.api.rewriters.Rewriter;

public class PlayerPacket1_13 extends Rewriter<Protocol1_12_2To1_13>
{
    public PlayerPacket1_13(final Protocol1_12_2To1_13 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing(State.LOGIN, 4, -1, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.cancel();
                        packetWrapper.create(2, (ValueCreator)new ValueCreator() {
                            public void write(final PacketWrapper newWrapper) throws Exception {
                                newWrapper.write((Type)Type.VAR_INT, packetWrapper.read((Type)Type.VAR_INT));
                                newWrapper.write(Type.BOOLEAN, (Object)false);
                            }
                        }).sendToServer((Class)Protocol1_12_2To1_13.class);
                    }
                });
            }
        });
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.PLUGIN_MESSAGE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final String channel = (String)wrapper.read(Type.STRING);
                        if (channel.equals("minecraft:trader_list")) {
                            wrapper.write(Type.STRING, (Object)"MC|TrList");
                            wrapper.passthrough(Type.INT);
                            for (int size = (short)wrapper.passthrough(Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                                final Item input = (Item)wrapper.read(Type.FLAT_ITEM);
                                wrapper.write(Type.ITEM, (Object)PlayerPacket1_13.this.getProtocol().getBlockItemPackets().handleItemToClient(input));
                                final Item output = (Item)wrapper.read(Type.FLAT_ITEM);
                                wrapper.write(Type.ITEM, (Object)PlayerPacket1_13.this.getProtocol().getBlockItemPackets().handleItemToClient(output));
                                final boolean secondItem = (boolean)wrapper.passthrough(Type.BOOLEAN);
                                if (secondItem) {
                                    final Item second = (Item)wrapper.read(Type.FLAT_ITEM);
                                    wrapper.write(Type.ITEM, (Object)PlayerPacket1_13.this.getProtocol().getBlockItemPackets().handleItemToClient(second));
                                }
                                wrapper.passthrough(Type.BOOLEAN);
                                wrapper.passthrough(Type.INT);
                                wrapper.passthrough(Type.INT);
                            }
                        }
                        else {
                            final String oldChannel = InventoryPackets.getOldPluginChannelId(channel);
                            if (oldChannel == null) {
                                if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
                                    ViaBackwards.getPlatform().getLogger().warning("Ignoring outgoing plugin message with channel: " + channel);
                                }
                                wrapper.cancel();
                                return;
                            }
                            wrapper.write(Type.STRING, (Object)oldChannel);
                            if (oldChannel.equals("REGISTER") || oldChannel.equals("UNREGISTER")) {
                                final String[] channels = new String((byte[])wrapper.read(Type.REMAINING_BYTES), StandardCharsets.UTF_8).split("\u0000");
                                final List<String> rewrittenChannels = new ArrayList<String>();
                                for (final String s : channels) {
                                    final String rewritten = InventoryPackets.getOldPluginChannelId(s);
                                    if (rewritten != null) {
                                        rewrittenChannels.add(rewritten);
                                    }
                                    else if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
                                        ViaBackwards.getPlatform().getLogger().warning("Ignoring plugin channel in outgoing REGISTER: " + s);
                                    }
                                }
                                wrapper.write(Type.REMAINING_BYTES, (Object)Joiner.on('\0').join((Iterable)rewrittenChannels).getBytes(StandardCharsets.UTF_8));
                            }
                        }
                    }
                });
            }
        });
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.SPAWN_PARTICLE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map((Type)Type.FLOAT);
                this.map(Type.INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ParticleMapping.ParticleData old = ParticleMapping.getMapping((int)wrapper.get(Type.INT, 0));
                        wrapper.set(Type.INT, 0, (Object)old.getHistoryId());
                        final int[] data = old.rewriteData((Protocol1_12_2To1_13)PlayerPacket1_13.this.protocol, wrapper);
                        if (data != null) {
                            if (old.getHandler().isBlockHandler() && data[0] == 0) {
                                wrapper.cancel();
                                return;
                            }
                            for (final int i : data) {
                                wrapper.write((Type)Type.VAR_INT, (Object)i);
                            }
                        }
                    }
                });
            }
        });
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.PLAYER_INFO, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final TabCompleteStorage storage = (TabCompleteStorage)packetWrapper.user().get((Class)TabCompleteStorage.class);
                        final int action = (int)packetWrapper.passthrough((Type)Type.VAR_INT);
                        for (int nPlayers = (int)packetWrapper.passthrough((Type)Type.VAR_INT), i = 0; i < nPlayers; ++i) {
                            final UUID uuid = (UUID)packetWrapper.passthrough(Type.UUID);
                            if (action == 0) {
                                final String name = (String)packetWrapper.passthrough(Type.STRING);
                                storage.usernames.put(uuid, name);
                                for (int nProperties = (int)packetWrapper.passthrough((Type)Type.VAR_INT), j = 0; j < nProperties; ++j) {
                                    packetWrapper.passthrough(Type.STRING);
                                    packetWrapper.passthrough(Type.STRING);
                                    if (packetWrapper.passthrough(Type.BOOLEAN)) {
                                        packetWrapper.passthrough(Type.STRING);
                                    }
                                }
                                packetWrapper.passthrough((Type)Type.VAR_INT);
                                packetWrapper.passthrough((Type)Type.VAR_INT);
                                if (packetWrapper.passthrough(Type.BOOLEAN)) {
                                    packetWrapper.passthrough(Type.COMPONENT);
                                }
                            }
                            else if (action == 1) {
                                packetWrapper.passthrough((Type)Type.VAR_INT);
                            }
                            else if (action == 2) {
                                packetWrapper.passthrough((Type)Type.VAR_INT);
                            }
                            else if (action == 3) {
                                if (packetWrapper.passthrough(Type.BOOLEAN)) {
                                    packetWrapper.passthrough(Type.COMPONENT);
                                }
                            }
                            else if (action == 4) {
                                storage.usernames.remove(uuid);
                            }
                        }
                    }
                });
            }
        });
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.SCOREBOARD_OBJECTIVE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.BYTE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final byte mode = (byte)wrapper.get(Type.BYTE, 0);
                        if (mode == 0 || mode == 2) {
                            String value = ((JsonElement)wrapper.read(Type.COMPONENT)).toString();
                            value = ChatRewriter.jsonTextToLegacy(value);
                            if (value.length() > 32) {
                                value = value.substring(0, 32);
                            }
                            wrapper.write(Type.STRING, (Object)value);
                            final int type = (int)wrapper.read((Type)Type.VAR_INT);
                            wrapper.write(Type.STRING, (Object)((type == 1) ? "hearts" : "integer"));
                        }
                    }
                });
            }
        });
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.TEAMS, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.BYTE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final byte action = (byte)wrapper.get(Type.BYTE, 0);
                        if (action == 0 || action == 2) {
                            String displayName = (String)wrapper.read(Type.STRING);
                            displayName = ChatRewriter.jsonTextToLegacy(displayName);
                            displayName = ChatUtil.removeUnusedColor(displayName, 'f');
                            if (displayName.length() > 32) {
                                displayName = displayName.substring(0, 32);
                            }
                            wrapper.write(Type.STRING, (Object)displayName);
                            final byte flags = (byte)wrapper.read(Type.BYTE);
                            final String nameTagVisibility = (String)wrapper.read(Type.STRING);
                            final String collisionRule = (String)wrapper.read(Type.STRING);
                            int colour = (int)wrapper.read((Type)Type.VAR_INT);
                            if (colour == 21) {
                                colour = -1;
                            }
                            final JsonElement prefixComponent = (JsonElement)wrapper.read(Type.COMPONENT);
                            final JsonElement suffixComponent = (JsonElement)wrapper.read(Type.COMPONENT);
                            String prefix = (prefixComponent == null || prefixComponent.isJsonNull()) ? "" : ChatRewriter.jsonTextToLegacy(prefixComponent.toString());
                            if (ViaBackwards.getConfig().addTeamColorTo1_13Prefix()) {
                                prefix = prefix + "§" + ((colour > -1 && colour <= 15) ? Integer.toHexString(colour) : "r");
                            }
                            prefix = ChatUtil.removeUnusedColor(prefix, 'f', true);
                            if (prefix.length() > 16) {
                                prefix = prefix.substring(0, 16);
                            }
                            if (prefix.endsWith("§")) {
                                prefix = prefix.substring(0, prefix.length() - 1);
                            }
                            String suffix = (suffixComponent == null || suffixComponent.isJsonNull()) ? "" : ChatRewriter.jsonTextToLegacy(suffixComponent.toString());
                            suffix = ChatUtil.removeUnusedColor(suffix, '\0');
                            if (suffix.length() > 16) {
                                suffix = suffix.substring(0, 16);
                            }
                            if (suffix.endsWith("§")) {
                                suffix = suffix.substring(0, suffix.length() - 1);
                            }
                            wrapper.write(Type.STRING, (Object)prefix);
                            wrapper.write(Type.STRING, (Object)suffix);
                            wrapper.write(Type.BYTE, (Object)flags);
                            wrapper.write(Type.STRING, (Object)nameTagVisibility);
                            wrapper.write(Type.STRING, (Object)collisionRule);
                            wrapper.write(Type.BYTE, (Object)(byte)colour);
                        }
                        if (action == 0 || action == 3 || action == 4) {
                            wrapper.passthrough(Type.STRING_ARRAY);
                        }
                    }
                });
            }
        });
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.TAB_COMPLETE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final TabCompleteStorage storage = (TabCompleteStorage)wrapper.user().get((Class)TabCompleteStorage.class);
                        if (storage.lastRequest == null) {
                            wrapper.cancel();
                            return;
                        }
                        if (storage.lastId != (int)wrapper.read((Type)Type.VAR_INT)) {
                            wrapper.cancel();
                        }
                        final int start = (int)wrapper.read((Type)Type.VAR_INT);
                        final int length = (int)wrapper.read((Type)Type.VAR_INT);
                        final int lastRequestPartIndex = storage.lastRequest.lastIndexOf(32) + 1;
                        if (lastRequestPartIndex != start) {
                            wrapper.cancel();
                        }
                        if (length != storage.lastRequest.length() - lastRequestPartIndex) {
                            wrapper.cancel();
                        }
                        for (int count = (int)wrapper.passthrough((Type)Type.VAR_INT), i = 0; i < count; ++i) {
                            final String match = (String)wrapper.read(Type.STRING);
                            wrapper.write(Type.STRING, (Object)(((start == 0 && !storage.lastAssumeCommand) ? "/" : "") + match));
                            if (wrapper.read(Type.BOOLEAN)) {
                                wrapper.read(Type.STRING);
                            }
                        }
                    }
                });
            }
        });
        ((Protocol1_12_2To1_13)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_12_1.TAB_COMPLETE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    final TabCompleteStorage storage = (TabCompleteStorage)wrapper.user().get((Class)TabCompleteStorage.class);
                    final int id = ThreadLocalRandom.current().nextInt();
                    wrapper.write((Type)Type.VAR_INT, (Object)id);
                    String command = (String)wrapper.read(Type.STRING);
                    final boolean assumeCommand = (boolean)wrapper.read(Type.BOOLEAN);
                    wrapper.read(Type.OPTIONAL_POSITION);
                    if (!assumeCommand) {
                        if (command.startsWith("/")) {
                            command = command.substring(1);
                        }
                        else {
                            wrapper.cancel();
                            final PacketWrapper response = wrapper.create(14);
                            final List<String> usernames = new ArrayList<String>();
                            for (final String value : storage.usernames.values()) {
                                if (value.toLowerCase().startsWith(command.substring(command.lastIndexOf(32) + 1).toLowerCase())) {
                                    usernames.add(value);
                                }
                            }
                            response.write((Type)Type.VAR_INT, (Object)usernames.size());
                            for (final String value : usernames) {
                                response.write(Type.STRING, (Object)value);
                            }
                            response.send((Class)((Protocol1_12_2To1_13)PlayerPacket1_13.this.protocol).getClass());
                        }
                    }
                    wrapper.write(Type.STRING, (Object)command);
                    storage.lastId = id;
                    storage.lastAssumeCommand = assumeCommand;
                    storage.lastRequest = command;
                });
            }
        });
        ((Protocol1_12_2To1_13)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_12_1.PLUGIN_MESSAGE, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler(wrapper -> {
                    final String s2;
                    final String channel = s2 = (String)wrapper.read(Type.STRING);
                    switch (s2) {
                        case "MC|BSign":
                        case "MC|BEdit": {
                            wrapper.setId(11);
                            final Item book = (Item)wrapper.read(Type.ITEM);
                            wrapper.write(Type.FLAT_ITEM, (Object)PlayerPacket1_13.this.getProtocol().getBlockItemPackets().handleItemToServer(book));
                            final boolean signing = channel.equals("MC|BSign");
                            wrapper.write(Type.BOOLEAN, (Object)signing);
                            break;
                        }
                        case "MC|ItemName": {
                            wrapper.setId(28);
                            break;
                        }
                        case "MC|AdvCmd": {
                            final byte type = (byte)wrapper.read(Type.BYTE);
                            if (type == 0) {
                                wrapper.setId(34);
                                wrapper.cancel();
                                ViaBackwards.getPlatform().getLogger().warning("Client send MC|AdvCmd custom payload to update command block, weird!");
                                break;
                            }
                            if (type == 1) {
                                wrapper.setId(35);
                                wrapper.write((Type)Type.VAR_INT, wrapper.read(Type.INT));
                                wrapper.passthrough(Type.STRING);
                                wrapper.passthrough(Type.BOOLEAN);
                                break;
                            }
                            wrapper.cancel();
                            break;
                        }
                        case "MC|AutoCmd": {
                            wrapper.setId(34);
                            final int x = (int)wrapper.read(Type.INT);
                            final int y = (int)wrapper.read(Type.INT);
                            final int z = (int)wrapper.read(Type.INT);
                            wrapper.write(Type.POSITION, (Object)new Position(x, (short)y, z));
                            wrapper.passthrough(Type.STRING);
                            byte flags = 0;
                            if (wrapper.read(Type.BOOLEAN)) {
                                flags |= 0x1;
                            }
                            final String mode = (String)wrapper.read(Type.STRING);
                            final int modeId = mode.equals("SEQUENCE") ? 0 : (mode.equals("AUTO") ? 1 : 2);
                            wrapper.write((Type)Type.VAR_INT, (Object)modeId);
                            if (wrapper.read(Type.BOOLEAN)) {
                                flags |= 0x2;
                            }
                            if (wrapper.read(Type.BOOLEAN)) {
                                flags |= 0x4;
                            }
                            wrapper.write(Type.BYTE, (Object)flags);
                            break;
                        }
                        case "MC|Struct": {
                            wrapper.setId(37);
                            final int x = (int)wrapper.read(Type.INT);
                            final int y = (int)wrapper.read(Type.INT);
                            final int z = (int)wrapper.read(Type.INT);
                            wrapper.write(Type.POSITION, (Object)new Position(x, (short)y, z));
                            wrapper.write((Type)Type.VAR_INT, (Object)((byte)wrapper.read(Type.BYTE) - 1));
                            final String mode2 = (String)wrapper.read(Type.STRING);
                            final int modeId2 = mode2.equals("SAVE") ? 0 : (mode2.equals("LOAD") ? 1 : (mode2.equals("CORNER") ? 2 : 3));
                            wrapper.write((Type)Type.VAR_INT, (Object)modeId2);
                            wrapper.passthrough(Type.STRING);
                            wrapper.write(Type.BYTE, (Object)((Integer)wrapper.read(Type.INT)).byteValue());
                            wrapper.write(Type.BYTE, (Object)((Integer)wrapper.read(Type.INT)).byteValue());
                            wrapper.write(Type.BYTE, (Object)((Integer)wrapper.read(Type.INT)).byteValue());
                            wrapper.write(Type.BYTE, (Object)((Integer)wrapper.read(Type.INT)).byteValue());
                            wrapper.write(Type.BYTE, (Object)((Integer)wrapper.read(Type.INT)).byteValue());
                            wrapper.write(Type.BYTE, (Object)((Integer)wrapper.read(Type.INT)).byteValue());
                            final String mirror = (String)wrapper.read(Type.STRING);
                            final int mirrorId = mode2.equals("NONE") ? 0 : (mode2.equals("LEFT_RIGHT") ? 1 : 2);
                            final String rotation = (String)wrapper.read(Type.STRING);
                            final int rotationId = mode2.equals("NONE") ? 0 : (mode2.equals("CLOCKWISE_90") ? 1 : (mode2.equals("CLOCKWISE_180") ? 2 : 3));
                            wrapper.passthrough(Type.STRING);
                            byte flags2 = 0;
                            if (wrapper.read(Type.BOOLEAN)) {
                                flags2 |= 0x1;
                            }
                            if (wrapper.read(Type.BOOLEAN)) {
                                flags2 |= 0x2;
                            }
                            if (wrapper.read(Type.BOOLEAN)) {
                                flags2 |= 0x4;
                            }
                            wrapper.passthrough((Type)Type.FLOAT);
                            wrapper.passthrough((Type)Type.VAR_LONG);
                            wrapper.write(Type.BYTE, (Object)flags2);
                            break;
                        }
                        case "MC|Beacon": {
                            wrapper.setId(32);
                            wrapper.write((Type)Type.VAR_INT, wrapper.read(Type.INT));
                            wrapper.write((Type)Type.VAR_INT, wrapper.read(Type.INT));
                            break;
                        }
                        case "MC|TrSel": {
                            wrapper.setId(31);
                            wrapper.write((Type)Type.VAR_INT, wrapper.read(Type.INT));
                            break;
                        }
                        case "MC|PickItem": {
                            wrapper.setId(21);
                            break;
                        }
                        default: {
                            final String newChannel = InventoryPackets.getNewPluginChannelId(channel);
                            if (newChannel == null) {
                                if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
                                    ViaBackwards.getPlatform().getLogger().warning("Ignoring incoming plugin message with channel: " + channel);
                                }
                                wrapper.cancel();
                                return;
                            }
                            wrapper.write(Type.STRING, (Object)newChannel);
                            if (!newChannel.equals("minecraft:register") && !newChannel.equals("minecraft:unregister")) {
                                break;
                            }
                            final String[] channels = new String((byte[])wrapper.read(Type.REMAINING_BYTES), StandardCharsets.UTF_8).split("\u0000");
                            final List<String> rewrittenChannels = new ArrayList<String>();
                            for (final String s : channels) {
                                final String rewritten = InventoryPackets.getNewPluginChannelId(s);
                                if (rewritten != null) {
                                    rewrittenChannels.add(rewritten);
                                }
                                else if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
                                    ViaBackwards.getPlatform().getLogger().warning("Ignoring plugin channel in incoming REGISTER: " + s);
                                }
                            }
                            if (!rewrittenChannels.isEmpty()) {
                                wrapper.write(Type.REMAINING_BYTES, (Object)Joiner.on('\0').join((Iterable)rewrittenChannels).getBytes(StandardCharsets.UTF_8));
                                break;
                            }
                            wrapper.cancel();
                        }
                    }
                });
            }
        });
        ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.STATISTICS, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        int newSize;
                        final int size = newSize = (int)wrapper.get((Type)Type.VAR_INT, 0);
                        for (int i = 0; i < size; ++i) {
                            final int categoryId = (int)wrapper.read((Type)Type.VAR_INT);
                            final int statisticId = (int)wrapper.read((Type)Type.VAR_INT);
                            String name = "";
                            switch (categoryId) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                case 5:
                                case 6:
                                case 7: {
                                    wrapper.read((Type)Type.VAR_INT);
                                    --newSize;
                                    continue;
                                }
                                case 8: {
                                    name = (String)((Protocol1_12_2To1_13)PlayerPacket1_13.this.protocol).getMappingData().getStatisticMappings().get(statisticId);
                                    if (name == null) {
                                        wrapper.read((Type)Type.VAR_INT);
                                        --newSize;
                                        continue;
                                    }
                                    break;
                                }
                            }
                            wrapper.write(Type.STRING, (Object)name);
                            wrapper.passthrough((Type)Type.VAR_INT);
                        }
                        if (newSize != size) {
                            wrapper.set((Type)Type.VAR_INT, 0, (Object)newSize);
                        }
                    }
                });
            }
        });
    }
}
