// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_13to1_12_2;

import com.google.common.collect.Sets;
import java.util.HashMap;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.providers.PaintingProvider;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.providers.BlockEntityProvider;
import us.myles.ViaVersion.api.platform.providers.ViaProviders;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.storage.BlockConnectionStorage;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.blockconnections.providers.BlockConnectionProvider;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.blockconnections.providers.PacketBlockConnectionProvider;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.storage.BlockStorage;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.data.BlockIdData;
import us.myles.ViaVersion.api.rewriters.MetadataRewriter;
import com.google.common.primitives.Ints;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.remapper.ValueTransformer;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.rewriters.SoundRewriter;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.blockconnections.ConnectionData;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.data.RecipeData;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.entities.Entity1_13Types;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.storage.EntityTracker1_13;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.storage.TabCompleteTracker;
import us.myles.ViaVersion.api.remapper.ValueCreator;
import java.util.Iterator;
import java.util.List;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.data.StatisticMappings;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.data.StatisticData;
import java.util.ArrayList;
import us.myles.viaversion.libs.gson.JsonParseException;
import us.myles.ViaVersion.util.GsonUtil;
import us.myles.viaversion.libs.gson.JsonObject;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.packets.InventoryPackets;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.packets.WorldPackets;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.packets.EntityPackets;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.metadata.MetadataRewriter1_13To1_12_2;
import java.util.Set;
import us.myles.viaversion.libs.bungeecordchat.api.ChatColor;
import java.util.Map;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.data.MappingData;
import us.myles.ViaVersion.protocols.protocol1_12_1to1_12.ServerboundPackets1_12_1;
import us.myles.ViaVersion.protocols.protocol1_12_1to1_12.ClientboundPackets1_12_1;
import us.myles.ViaVersion.api.protocol.Protocol;

public class Protocol1_13To1_12_2 extends Protocol<ClientboundPackets1_12_1, ClientboundPackets1_13, ServerboundPackets1_12_1, ServerboundPackets1_13>
{
    public static final MappingData MAPPINGS;
    public static final PacketHandler POS_TO_3_INT;
    private static final PacketHandler SEND_DECLARE_COMMANDS_AND_TAGS;
    protected static final Map<ChatColor, Character> SCOREBOARD_TEAM_NAME_REWRITE;
    private static final Set<ChatColor> FORMATTING_CODES;
    
    public Protocol1_13To1_12_2() {
        super(ClientboundPackets1_12_1.class, ClientboundPackets1_13.class, ServerboundPackets1_12_1.class, ServerboundPackets1_13.class);
    }
    
    @Override
    protected void registerPackets() {
        final MetadataRewriter metadataRewriter = new MetadataRewriter1_13To1_12_2(this);
        EntityPackets.register(this);
        WorldPackets.register(this);
        InventoryPackets.register(this);
        this.registerOutgoing(State.LOGIN, 0, 0, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> ChatRewriter.processTranslate(wrapper.passthrough(Type.COMPONENT)));
            }
        });
        this.registerOutgoing(State.STATUS, 0, 0, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final String response = wrapper.get(Type.STRING, 0);
                        try {
                            final JsonObject json = GsonUtil.getGson().fromJson(response, JsonObject.class);
                            if (json.has("favicon")) {
                                json.addProperty("favicon", json.get("favicon").getAsString().replace("\n", ""));
                            }
                            wrapper.set(Type.STRING, 0, GsonUtil.getGson().toJson(json));
                        }
                        catch (JsonParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_12_1, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_12_1.STATISTICS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int size = wrapper.read((Type<Integer>)Type.VAR_INT);
                        final List<StatisticData> remappedStats = new ArrayList<StatisticData>();
                        for (int i = 0; i < size; ++i) {
                            final String name = wrapper.read(Type.STRING);
                            final String[] split = name.split("\\.");
                            int categoryId = 0;
                            int newId = -1;
                            final int value = wrapper.read((Type<Integer>)Type.VAR_INT);
                            if (split.length == 2) {
                                categoryId = 8;
                                final Integer newIdRaw = StatisticMappings.CUSTOM_STATS.get(name);
                                if (newIdRaw != null) {
                                    newId = newIdRaw;
                                }
                                else {
                                    Via.getPlatform().getLogger().warning("Could not find 1.13 -> 1.12.2 statistic mapping for " + name);
                                }
                            }
                            else {
                                final String s;
                                final String category = s = split[1];
                                switch (s) {
                                    case "mineBlock": {
                                        categoryId = 0;
                                        break;
                                    }
                                    case "craftItem": {
                                        categoryId = 1;
                                        break;
                                    }
                                    case "useItem": {
                                        categoryId = 2;
                                        break;
                                    }
                                    case "breakItem": {
                                        categoryId = 3;
                                        break;
                                    }
                                    case "pickup": {
                                        categoryId = 4;
                                        break;
                                    }
                                    case "drop": {
                                        categoryId = 5;
                                        break;
                                    }
                                    case "killEntity": {
                                        categoryId = 6;
                                        break;
                                    }
                                    case "entityKilledBy": {
                                        categoryId = 7;
                                        break;
                                    }
                                }
                            }
                            if (newId != -1) {
                                remappedStats.add(new StatisticData(categoryId, newId, value));
                            }
                        }
                        wrapper.write(Type.VAR_INT, remappedStats.size());
                        for (final StatisticData stat : remappedStats) {
                            wrapper.write(Type.VAR_INT, stat.getCategoryId());
                            wrapper.write(Type.VAR_INT, stat.getNewId());
                            wrapper.write(Type.VAR_INT, stat.getValue());
                        }
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_12_1, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_12_1.BOSSBAR, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UUID);
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int action = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        if (action == 0 || action == 3) {
                            ChatRewriter.processTranslate(wrapper.passthrough(Type.COMPONENT));
                        }
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_12_1, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_12_1.CHAT_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> ChatRewriter.processTranslate(wrapper.passthrough(Type.COMPONENT)));
            }
        });
        ((Protocol<ClientboundPackets1_12_1, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_12_1.TAB_COMPLETE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.create(new ValueCreator() {
                    @Override
                    public void write(final PacketWrapper wrapper) throws Exception {
                        wrapper.write(Type.VAR_INT, wrapper.user().get(TabCompleteTracker.class).getTransactionId());
                        final String input = wrapper.user().get(TabCompleteTracker.class).getInput();
                        int index;
                        int length;
                        if (input.endsWith(" ") || input.isEmpty()) {
                            index = input.length();
                            length = 0;
                        }
                        else {
                            final int lastSpace = index = input.lastIndexOf(32) + 1;
                            length = input.length() - lastSpace;
                        }
                        wrapper.write(Type.VAR_INT, index);
                        wrapper.write(Type.VAR_INT, length);
                        for (int count = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < count; ++i) {
                            String suggestion = wrapper.read(Type.STRING);
                            if (suggestion.startsWith("/") && index == 0) {
                                suggestion = suggestion.substring(1);
                            }
                            wrapper.write(Type.STRING, suggestion);
                            wrapper.write(Type.BOOLEAN, false);
                        }
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_12_1, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_12_1.OPEN_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.handler(wrapper -> ChatRewriter.processTranslate(wrapper.passthrough(Type.COMPONENT)));
            }
        });
        ((Protocol<ClientboundPackets1_12_1, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_12_1.COOLDOWN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int item = wrapper.read((Type<Integer>)Type.VAR_INT);
                        final int ticks = wrapper.read((Type<Integer>)Type.VAR_INT);
                        wrapper.cancel();
                        if (item == 383) {
                            for (int i = 0; i < 44; ++i) {
                                final Integer newItem = Protocol1_13To1_12_2.this.getMappingData().getItemMappings().get(item << 16 | i);
                                if (newItem == null) {
                                    break;
                                }
                                final PacketWrapper packet = wrapper.create(24);
                                packet.write(Type.VAR_INT, newItem);
                                packet.write(Type.VAR_INT, ticks);
                                packet.send(Protocol1_13To1_12_2.class);
                            }
                        }
                        else {
                            for (int i = 0; i < 16; ++i) {
                                final int newItem2 = Protocol1_13To1_12_2.this.getMappingData().getItemMappings().get(item << 4 | i);
                                if (newItem2 == -1) {
                                    break;
                                }
                                final PacketWrapper packet = wrapper.create(24);
                                packet.write(Type.VAR_INT, newItem2);
                                packet.write(Type.VAR_INT, ticks);
                                packet.send(Protocol1_13To1_12_2.class);
                            }
                        }
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_12_1, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_12_1.DISCONNECT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> ChatRewriter.processTranslate(wrapper.passthrough(Type.COMPONENT)));
            }
        });
        ((Protocol<ClientboundPackets1_12_1, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_12_1.EFFECT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.POSITION);
                this.map(Type.INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int id = wrapper.get(Type.INT, 0);
                        final int data = wrapper.get(Type.INT, 1);
                        if (id == 1010) {
                            wrapper.set(Type.INT, 1, Protocol1_13To1_12_2.this.getMappingData().getItemMappings().get(data << 4));
                        }
                        else if (id == 2001) {
                            final int blockId = data & 0xFFF;
                            final int blockData = data >> 12;
                            wrapper.set(Type.INT, 1, WorldPackets.toNewId(blockId << 4 | blockData));
                        }
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_12_1, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_12_1.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int entityId = wrapper.get(Type.INT, 0);
                        wrapper.user().get(EntityTracker1_13.class).addEntity(entityId, Entity1_13Types.EntityType.PLAYER);
                        final ClientWorld clientChunks = wrapper.user().get(ClientWorld.class);
                        final int dimensionId = wrapper.get(Type.INT, 1);
                        clientChunks.setEnvironment(dimensionId);
                    }
                });
                this.handler(Protocol1_13To1_12_2.SEND_DECLARE_COMMANDS_AND_TAGS);
            }
        });
        ((Protocol<ClientboundPackets1_12_1, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_12_1.CRAFT_RECIPE_RESPONSE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.BYTE);
                this.handler(wrapper -> wrapper.write(Type.STRING, "viaversion:legacy/" + wrapper.read((Type<Object>)Type.VAR_INT)));
            }
        });
        ((Protocol<ClientboundPackets1_12_1, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_12_1.COMBAT_EVENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        if (wrapper.get((Type<Integer>)Type.VAR_INT, 0) == 2) {
                            wrapper.passthrough((Type<Object>)Type.VAR_INT);
                            wrapper.passthrough(Type.INT);
                            ChatRewriter.processTranslate(wrapper.passthrough(Type.COMPONENT));
                        }
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_12_1, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_12_1.MAP_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.BYTE);
                this.map(Type.BOOLEAN);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        for (int iconCount = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < iconCount; ++i) {
                            final byte directionAndType = wrapper.read(Type.BYTE);
                            final int type = (directionAndType & 0xF0) >> 4;
                            wrapper.write(Type.VAR_INT, type);
                            wrapper.passthrough(Type.BYTE);
                            wrapper.passthrough(Type.BYTE);
                            final byte direction = (byte)(directionAndType & 0xF);
                            wrapper.write(Type.BYTE, direction);
                            wrapper.write(Type.OPTIONAL_COMPONENT, null);
                        }
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_12_1, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_12_1.UNLOCK_RECIPES, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.BOOLEAN);
                this.map(Type.BOOLEAN);
                this.create(new ValueCreator() {
                    @Override
                    public void write(final PacketWrapper wrapper) throws Exception {
                        wrapper.write(Type.BOOLEAN, false);
                        wrapper.write(Type.BOOLEAN, false);
                    }
                });
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int action = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        for (int i = 0; i < ((action == 0) ? 2 : 1); ++i) {
                            final int[] ids = wrapper.read(Type.VAR_INT_ARRAY_PRIMITIVE);
                            final String[] stringIds = new String[ids.length];
                            for (int j = 0; j < ids.length; ++j) {
                                stringIds[j] = "viaversion:legacy/" + ids[j];
                            }
                            wrapper.write(Type.STRING_ARRAY, stringIds);
                        }
                        if (action == 0) {
                            wrapper.create(84, new ValueCreator() {
                                @Override
                                public void write(final PacketWrapper wrapper) throws Exception {
                                    wrapper.write(Type.VAR_INT, RecipeData.recipes.size());
                                    for (final Map.Entry<String, RecipeData.Recipe> entry : RecipeData.recipes.entrySet()) {
                                        wrapper.write(Type.STRING, entry.getKey());
                                        wrapper.write(Type.STRING, entry.getValue().getType());
                                        final String type = entry.getValue().getType();
                                        switch (type) {
                                            case "crafting_shapeless": {
                                                wrapper.write(Type.STRING, entry.getValue().getGroup());
                                                wrapper.write(Type.VAR_INT, entry.getValue().getIngredients().length);
                                                for (final Item[] ingredient : entry.getValue().getIngredients()) {
                                                    final Item[] clone = ingredient.clone();
                                                    for (int i = 0; i < clone.length; ++i) {
                                                        if (clone[i] != null) {
                                                            clone[i] = new Item(clone[i]);
                                                        }
                                                    }
                                                    wrapper.write(Type.FLAT_ITEM_ARRAY_VAR_INT, clone);
                                                }
                                                wrapper.write(Type.FLAT_ITEM, new Item(entry.getValue().getResult()));
                                                continue;
                                            }
                                            case "crafting_shaped": {
                                                wrapper.write(Type.VAR_INT, entry.getValue().getWidth());
                                                wrapper.write(Type.VAR_INT, entry.getValue().getHeight());
                                                wrapper.write(Type.STRING, entry.getValue().getGroup());
                                                for (final Item[] ingredient : entry.getValue().getIngredients()) {
                                                    final Item[] clone = ingredient.clone();
                                                    for (int i = 0; i < clone.length; ++i) {
                                                        if (clone[i] != null) {
                                                            clone[i] = new Item(clone[i]);
                                                        }
                                                    }
                                                    wrapper.write(Type.FLAT_ITEM_ARRAY_VAR_INT, clone);
                                                }
                                                wrapper.write(Type.FLAT_ITEM, new Item(entry.getValue().getResult()));
                                                continue;
                                            }
                                            case "smelting": {
                                                wrapper.write(Type.STRING, entry.getValue().getGroup());
                                                final Item[] clone2 = entry.getValue().getIngredient().clone();
                                                for (int j = 0; j < clone2.length; ++j) {
                                                    if (clone2[j] != null) {
                                                        clone2[j] = new Item(clone2[j]);
                                                    }
                                                }
                                                wrapper.write(Type.FLAT_ITEM_ARRAY_VAR_INT, clone2);
                                                wrapper.write(Type.FLAT_ITEM, new Item(entry.getValue().getResult()));
                                                wrapper.write(Type.FLOAT, entry.getValue().getExperience());
                                                wrapper.write(Type.VAR_INT, entry.getValue().getCookingTime());
                                                continue;
                                            }
                                        }
                                    }
                                }
                            }).send(Protocol1_13To1_12_2.class, true, true);
                        }
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_12_1, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_12_1.RESPAWN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ClientWorld clientWorld = wrapper.user().get(ClientWorld.class);
                        final int dimensionId = wrapper.get(Type.INT, 0);
                        clientWorld.setEnvironment(dimensionId);
                        if (Via.getConfig().isServersideBlockConnections()) {
                            ConnectionData.clearBlockStorage(wrapper.user());
                        }
                    }
                });
                this.handler(Protocol1_13To1_12_2.SEND_DECLARE_COMMANDS_AND_TAGS);
            }
        });
        ((Protocol<ClientboundPackets1_12_1, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_12_1.SCOREBOARD_OBJECTIVE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.BYTE);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final byte mode = wrapper.get(Type.BYTE, 0);
                        if (mode == 0 || mode == 2) {
                            final String value = wrapper.read(Type.STRING);
                            wrapper.write(Type.COMPONENT, ChatRewriter.legacyTextToJson(value));
                            final String type = wrapper.read(Type.STRING);
                            wrapper.write(Type.VAR_INT, type.equals("integer") ? 0 : 1);
                        }
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_12_1, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_12_1.TEAMS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.BYTE);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final byte action = wrapper.get(Type.BYTE, 0);
                        if (action == 0 || action == 2) {
                            final String displayName = wrapper.read(Type.STRING);
                            wrapper.write(Type.COMPONENT, ChatRewriter.legacyTextToJson(displayName));
                            final String prefix = wrapper.read(Type.STRING);
                            String suffix = wrapper.read(Type.STRING);
                            wrapper.passthrough(Type.BYTE);
                            wrapper.passthrough(Type.STRING);
                            wrapper.passthrough(Type.STRING);
                            int colour = wrapper.read(Type.BYTE);
                            if (colour == -1) {
                                colour = 21;
                            }
                            if (Via.getConfig().is1_13TeamColourFix()) {
                                final ChatColor lastColor = Protocol1_13To1_12_2.this.getLastColor(prefix);
                                colour = lastColor.ordinal();
                                suffix = lastColor.toString() + suffix;
                            }
                            wrapper.write(Type.VAR_INT, colour);
                            wrapper.write(Type.COMPONENT, ChatRewriter.legacyTextToJson(prefix));
                            wrapper.write(Type.COMPONENT, ChatRewriter.legacyTextToJson(suffix));
                        }
                        if (action == 0 || action == 3 || action == 4) {
                            final String[] names = wrapper.read(Type.STRING_ARRAY);
                            for (int i = 0; i < names.length; ++i) {
                                names[i] = Protocol1_13To1_12_2.this.rewriteTeamMemberName(names[i]);
                            }
                            wrapper.write(Type.STRING_ARRAY, names);
                        }
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_12_1, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_12_1.UPDATE_SCORE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        String displayName = wrapper.read(Type.STRING);
                        displayName = Protocol1_13To1_12_2.this.rewriteTeamMemberName(displayName);
                        wrapper.write(Type.STRING, displayName);
                        final byte action = wrapper.read(Type.BYTE);
                        wrapper.write(Type.BYTE, action);
                        wrapper.passthrough(Type.STRING);
                        if (action != 1) {
                            wrapper.passthrough((Type<Object>)Type.VAR_INT);
                        }
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_12_1, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_12_1.TITLE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int action = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        if (action >= 0 && action <= 2) {
                            ChatRewriter.processTranslate(wrapper.passthrough(Type.COMPONENT));
                        }
                    }
                });
            }
        });
        new SoundRewriter(this).registerSound(ClientboundPackets1_12_1.SOUND);
        ((Protocol<ClientboundPackets1_12_1, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_12_1.TAB_LIST, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        ChatRewriter.processTranslate(wrapper.passthrough(Type.COMPONENT));
                        ChatRewriter.processTranslate(wrapper.passthrough(Type.COMPONENT));
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_12_1, C2, S1, S2>)this).registerOutgoing(ClientboundPackets1_12_1.ADVANCEMENTS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        wrapper.passthrough(Type.BOOLEAN);
                        for (int size = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < size; ++i) {
                            wrapper.passthrough(Type.STRING);
                            if (wrapper.passthrough(Type.BOOLEAN)) {
                                wrapper.passthrough(Type.STRING);
                            }
                            if (wrapper.passthrough(Type.BOOLEAN)) {
                                ChatRewriter.processTranslate(wrapper.passthrough(Type.COMPONENT));
                                ChatRewriter.processTranslate(wrapper.passthrough(Type.COMPONENT));
                                final Item icon = wrapper.read(Type.ITEM);
                                InventoryPackets.toClient(icon);
                                wrapper.write(Type.FLAT_ITEM, icon);
                                wrapper.passthrough((Type<Object>)Type.VAR_INT);
                                final int flags = wrapper.passthrough(Type.INT);
                                if ((flags & 0x1) != 0x0) {
                                    wrapper.passthrough(Type.STRING);
                                }
                                wrapper.passthrough((Type<Object>)Type.FLOAT);
                                wrapper.passthrough((Type<Object>)Type.FLOAT);
                            }
                            wrapper.passthrough(Type.STRING_ARRAY);
                            for (int arrayLength = wrapper.passthrough((Type<Integer>)Type.VAR_INT), array = 0; array < arrayLength; ++array) {
                                wrapper.passthrough(Type.STRING_ARRAY);
                            }
                        }
                    }
                });
            }
        });
        this.cancelIncoming(State.LOGIN, 2);
        ((Protocol<C1, C2, S1, ServerboundPackets1_13>)this).cancelIncoming(ServerboundPackets1_13.QUERY_BLOCK_NBT);
        ((Protocol<C1, C2, S1, ServerboundPackets1_13>)this).registerIncoming(ServerboundPackets1_13.TAB_COMPLETE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        if (Via.getConfig().isDisable1_13AutoComplete()) {
                            wrapper.cancel();
                        }
                        final int tid = wrapper.read((Type<Integer>)Type.VAR_INT);
                        wrapper.user().get(TabCompleteTracker.class).setTransactionId(tid);
                    }
                });
                this.map(Type.STRING, (ValueTransformer<String, Object>)new ValueTransformer<String, String>(Type.STRING) {
                    @Override
                    public String transform(final PacketWrapper wrapper, final String inputValue) {
                        wrapper.user().get(TabCompleteTracker.class).setInput(inputValue);
                        return "/" + inputValue;
                    }
                });
                this.create(new ValueCreator() {
                    @Override
                    public void write(final PacketWrapper wrapper) throws Exception {
                        wrapper.write(Type.BOOLEAN, false);
                        wrapper.write(Type.OPTIONAL_POSITION, null);
                        if (!wrapper.isCancelled() && Via.getConfig().get1_13TabCompleteDelay() > 0) {
                            final TabCompleteTracker tracker = wrapper.user().get(TabCompleteTracker.class);
                            wrapper.cancel();
                            tracker.setTimeToSend(System.currentTimeMillis() + Via.getConfig().get1_13TabCompleteDelay() * 50L);
                            tracker.setLastTabComplete(wrapper.get(Type.STRING, 0));
                        }
                    }
                });
            }
        });
        ((Protocol<C1, C2, ServerboundPackets1_12_1, ServerboundPackets1_13>)this).registerIncoming(ServerboundPackets1_13.EDIT_BOOK, ServerboundPackets1_12_1.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final Item item = wrapper.read(Type.FLAT_ITEM);
                        final boolean isSigning = wrapper.read(Type.BOOLEAN);
                        InventoryPackets.toServer(item);
                        wrapper.write(Type.STRING, isSigning ? "MC|BSign" : "MC|BEdit");
                        wrapper.write(Type.ITEM, item);
                    }
                });
            }
        });
        ((Protocol<C1, C2, S1, ServerboundPackets1_13>)this).cancelIncoming(ServerboundPackets1_13.ENTITY_NBT_REQUEST);
        ((Protocol<C1, C2, ServerboundPackets1_12_1, ServerboundPackets1_13>)this).registerIncoming(ServerboundPackets1_13.PICK_ITEM, ServerboundPackets1_12_1.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.create(new ValueCreator() {
                    @Override
                    public void write(final PacketWrapper wrapper) throws Exception {
                        wrapper.write(Type.STRING, "MC|PickItem");
                    }
                });
            }
        });
        ((Protocol<C1, C2, S1, ServerboundPackets1_13>)this).registerIncoming(ServerboundPackets1_13.CRAFT_RECIPE_REQUEST, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.BYTE);
                final String s;
                final Integer id;
                this.handler(wrapper -> {
                    s = wrapper.read(Type.STRING);
                    if (s.length() < 19 || (id = Ints.tryParse(s.substring(18))) == null) {
                        wrapper.cancel();
                    }
                    else {
                        wrapper.write(Type.VAR_INT, id);
                    }
                });
            }
        });
        ((Protocol<C1, C2, S1, ServerboundPackets1_13>)this).registerIncoming(ServerboundPackets1_13.RECIPE_BOOK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int type = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        if (type == 0) {
                            final String s = wrapper.read(Type.STRING);
                            final Integer id;
                            if (s.length() < 19 || (id = Ints.tryParse(s.substring(18))) == null) {
                                wrapper.cancel();
                                return;
                            }
                            wrapper.write(Type.INT, id);
                        }
                        if (type == 1) {
                            wrapper.passthrough(Type.BOOLEAN);
                            wrapper.passthrough(Type.BOOLEAN);
                            wrapper.read(Type.BOOLEAN);
                            wrapper.read(Type.BOOLEAN);
                        }
                    }
                });
            }
        });
        ((Protocol<C1, C2, ServerboundPackets1_12_1, ServerboundPackets1_13>)this).registerIncoming(ServerboundPackets1_13.RENAME_ITEM, ServerboundPackets1_12_1.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.create(wrapper -> wrapper.write(Type.STRING, "MC|ItemName"));
            }
        });
        ((Protocol<C1, C2, ServerboundPackets1_12_1, ServerboundPackets1_13>)this).registerIncoming(ServerboundPackets1_13.SELECT_TRADE, ServerboundPackets1_12_1.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.create(wrapper -> wrapper.write(Type.STRING, "MC|TrSel"));
                this.map(Type.VAR_INT, Type.INT);
            }
        });
        ((Protocol<C1, C2, ServerboundPackets1_12_1, ServerboundPackets1_13>)this).registerIncoming(ServerboundPackets1_13.SET_BEACON_EFFECT, ServerboundPackets1_12_1.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.create(wrapper -> wrapper.write(Type.STRING, "MC|Beacon"));
                this.map(Type.VAR_INT, Type.INT);
                this.map(Type.VAR_INT, Type.INT);
            }
        });
        ((Protocol<C1, C2, ServerboundPackets1_12_1, ServerboundPackets1_13>)this).registerIncoming(ServerboundPackets1_13.UPDATE_COMMAND_BLOCK, ServerboundPackets1_12_1.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.create(wrapper -> wrapper.write(Type.STRING, "MC|AutoCmd"));
                this.handler(Protocol1_13To1_12_2.POS_TO_3_INT);
                this.map(Type.STRING);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int mode = wrapper.read((Type<Integer>)Type.VAR_INT);
                        final byte flags = wrapper.read(Type.BYTE);
                        final String stringMode = (mode == 0) ? "SEQUENCE" : ((mode == 1) ? "AUTO" : "REDSTONE");
                        wrapper.write(Type.BOOLEAN, (flags & 0x1) != 0x0);
                        wrapper.write(Type.STRING, stringMode);
                        wrapper.write(Type.BOOLEAN, (flags & 0x2) != 0x0);
                        wrapper.write(Type.BOOLEAN, (flags & 0x4) != 0x0);
                    }
                });
            }
        });
        ((Protocol<C1, C2, ServerboundPackets1_12_1, ServerboundPackets1_13>)this).registerIncoming(ServerboundPackets1_13.UPDATE_COMMAND_BLOCK_MINECART, ServerboundPackets1_12_1.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.create(new ValueCreator() {
                    @Override
                    public void write(final PacketWrapper wrapper) throws Exception {
                        wrapper.write(Type.STRING, "MC|AdvCmd");
                        wrapper.write(Type.BYTE, (Byte)1);
                    }
                });
                this.map(Type.VAR_INT, Type.INT);
            }
        });
        ((Protocol<C1, C2, ServerboundPackets1_12_1, ServerboundPackets1_13>)this).registerIncoming(ServerboundPackets1_13.UPDATE_STRUCTURE_BLOCK, ServerboundPackets1_12_1.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.create(wrapper -> wrapper.write(Type.STRING, "MC|Struct"));
                this.handler(Protocol1_13To1_12_2.POS_TO_3_INT);
                this.map((Type<Object>)Type.VAR_INT, (ValueTransformer<Object, Object>)new ValueTransformer<Integer, Byte>(Type.BYTE) {
                    @Override
                    public Byte transform(final PacketWrapper wrapper, final Integer action) throws Exception {
                        return (byte)(action + 1);
                    }
                });
                this.map((Type<Object>)Type.VAR_INT, (ValueTransformer<Object, Object>)new ValueTransformer<Integer, String>(Type.STRING) {
                    @Override
                    public String transform(final PacketWrapper wrapper, final Integer mode) throws Exception {
                        return (mode == 0) ? "SAVE" : ((mode == 1) ? "LOAD" : ((mode == 2) ? "CORNER" : "DATA"));
                    }
                });
                this.map(Type.STRING);
                this.map(Type.BYTE, Type.INT);
                this.map(Type.BYTE, Type.INT);
                this.map(Type.BYTE, Type.INT);
                this.map(Type.BYTE, Type.INT);
                this.map(Type.BYTE, Type.INT);
                this.map(Type.BYTE, Type.INT);
                this.map((Type<Object>)Type.VAR_INT, (ValueTransformer<Object, Object>)new ValueTransformer<Integer, String>(Type.STRING) {
                    @Override
                    public String transform(final PacketWrapper wrapper, final Integer mirror) throws Exception {
                        return (mirror == 0) ? "NONE" : ((mirror == 1) ? "LEFT_RIGHT" : "FRONT_BACK");
                    }
                });
                this.map((Type<Object>)Type.VAR_INT, (ValueTransformer<Object, Object>)new ValueTransformer<Integer, String>(Type.STRING) {
                    @Override
                    public String transform(final PacketWrapper wrapper, final Integer rotation) throws Exception {
                        return (rotation == 0) ? "NONE" : ((rotation == 1) ? "CLOCKWISE_90" : ((rotation == 2) ? "CLOCKWISE_180" : "COUNTERCLOCKWISE_90"));
                    }
                });
                this.map(Type.STRING);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final float integrity = wrapper.read((Type<Float>)Type.FLOAT);
                        final long seed = wrapper.read((Type<Long>)Type.VAR_LONG);
                        final byte flags = wrapper.read(Type.BYTE);
                        wrapper.write(Type.BOOLEAN, (flags & 0x1) != 0x0);
                        wrapper.write(Type.BOOLEAN, (flags & 0x2) != 0x0);
                        wrapper.write(Type.BOOLEAN, (flags & 0x4) != 0x0);
                        wrapper.write(Type.FLOAT, integrity);
                        wrapper.write(Type.VAR_LONG, seed);
                    }
                });
            }
        });
    }
    
    @Override
    protected void onMappingDataLoaded() {
        ConnectionData.init();
        RecipeData.init();
        BlockIdData.init();
    }
    
    @Override
    public void init(final UserConnection userConnection) {
        userConnection.put(new EntityTracker1_13(userConnection));
        userConnection.put(new TabCompleteTracker(userConnection));
        if (!userConnection.has(ClientWorld.class)) {
            userConnection.put(new ClientWorld(userConnection));
        }
        userConnection.put(new BlockStorage(userConnection));
        if (Via.getConfig().isServersideBlockConnections() && Via.getManager().getProviders().get(BlockConnectionProvider.class) instanceof PacketBlockConnectionProvider) {
            userConnection.put(new BlockConnectionStorage(userConnection));
        }
    }
    
    @Override
    protected void register(final ViaProviders providers) {
        providers.register(BlockEntityProvider.class, new BlockEntityProvider());
        providers.register(PaintingProvider.class, new PaintingProvider());
    }
    
    public ChatColor getLastColor(final String input) {
        final int length = input.length();
        for (int index = length - 1; index > -1; --index) {
            final char section = input.charAt(index);
            if (section == '§' && index < length - 1) {
                final char c = input.charAt(index + 1);
                final ChatColor color = ChatColor.getByChar(c);
                if (color != null && !Protocol1_13To1_12_2.FORMATTING_CODES.contains(color)) {
                    return color;
                }
            }
        }
        return ChatColor.RESET;
    }
    
    protected String rewriteTeamMemberName(String name) {
        if (ChatColor.stripColor(name).isEmpty()) {
            final StringBuilder newName = new StringBuilder();
            for (int i = 1; i < name.length(); i += 2) {
                final char colorChar = name.charAt(i);
                Character rewrite = Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.get(ChatColor.getByChar(colorChar));
                if (rewrite == null) {
                    rewrite = colorChar;
                }
                newName.append('§').append(rewrite);
            }
            name = newName.toString();
        }
        return name;
    }
    
    public static int[] toPrimitive(final Integer[] array) {
        final int[] prim = new int[array.length];
        for (int i = 0; i < array.length; ++i) {
            prim[i] = array[i];
        }
        return prim;
    }
    
    @Override
    public MappingData getMappingData() {
        return Protocol1_13To1_12_2.MAPPINGS;
    }
    
    static {
        MAPPINGS = new MappingData();
        final Position position;
        POS_TO_3_INT = (wrapper -> {
            position = wrapper.read(Type.POSITION);
            wrapper.write(Type.INT, position.getX());
            wrapper.write(Type.INT, (int)position.getY());
            wrapper.write(Type.INT, position.getZ());
            return;
        });
        SEND_DECLARE_COMMANDS_AND_TAGS = (w -> {
            w.create(17, new ValueCreator() {
                @Override
                public void write(final PacketWrapper wrapper) {
                    wrapper.write(Type.VAR_INT, 2);
                    wrapper.write(Type.VAR_INT, 0);
                    wrapper.write(Type.VAR_INT, 1);
                    wrapper.write(Type.VAR_INT, 1);
                    wrapper.write(Type.VAR_INT, 22);
                    wrapper.write(Type.VAR_INT, 0);
                    wrapper.write(Type.STRING, "args");
                    wrapper.write(Type.STRING, "brigadier:string");
                    wrapper.write(Type.VAR_INT, 2);
                    wrapper.write(Type.STRING, "minecraft:ask_server");
                    wrapper.write(Type.VAR_INT, 0);
                }
            }).send(Protocol1_13To1_12_2.class);
            w.create(85, new ValueCreator() {
                @Override
                public void write(final PacketWrapper wrapper) throws Exception {
                    wrapper.write(Type.VAR_INT, Protocol1_13To1_12_2.MAPPINGS.getBlockTags().size());
                    for (final Map.Entry tag : Protocol1_13To1_12_2.MAPPINGS.getBlockTags().entrySet()) {
                        wrapper.write(Type.STRING, tag.getKey());
                        wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, Protocol1_13To1_12_2.toPrimitive(tag.getValue()));
                    }
                    wrapper.write(Type.VAR_INT, Protocol1_13To1_12_2.MAPPINGS.getItemTags().size());
                    for (final Map.Entry tag : Protocol1_13To1_12_2.MAPPINGS.getItemTags().entrySet()) {
                        wrapper.write(Type.STRING, tag.getKey());
                        wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, Protocol1_13To1_12_2.toPrimitive(tag.getValue()));
                    }
                    wrapper.write(Type.VAR_INT, Protocol1_13To1_12_2.MAPPINGS.getFluidTags().size());
                    for (final Map.Entry tag : Protocol1_13To1_12_2.MAPPINGS.getFluidTags().entrySet()) {
                        wrapper.write(Type.STRING, tag.getKey());
                        wrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, Protocol1_13To1_12_2.toPrimitive(tag.getValue()));
                    }
                }
            }).send(Protocol1_13To1_12_2.class);
            return;
        });
        SCOREBOARD_TEAM_NAME_REWRITE = new HashMap<ChatColor, Character>();
        FORMATTING_CODES = Sets.newHashSet((Object[])new ChatColor[] { ChatColor.MAGIC, ChatColor.BOLD, ChatColor.STRIKETHROUGH, ChatColor.UNDERLINE, ChatColor.ITALIC, ChatColor.RESET });
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put(ChatColor.BLACK, 'g');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put(ChatColor.DARK_BLUE, 'h');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put(ChatColor.DARK_GREEN, 'i');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put(ChatColor.DARK_AQUA, 'j');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put(ChatColor.DARK_RED, 'p');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put(ChatColor.DARK_PURPLE, 'q');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put(ChatColor.GOLD, 's');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put(ChatColor.GRAY, 't');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put(ChatColor.DARK_GRAY, 'u');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put(ChatColor.BLUE, 'v');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put(ChatColor.GREEN, 'w');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put(ChatColor.AQUA, 'x');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put(ChatColor.RED, 'y');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put(ChatColor.LIGHT_PURPLE, 'z');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put(ChatColor.YELLOW, '!');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put(ChatColor.WHITE, '?');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put(ChatColor.MAGIC, '#');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put(ChatColor.BOLD, '(');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put(ChatColor.STRIKETHROUGH, ')');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put(ChatColor.UNDERLINE, ':');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put(ChatColor.ITALIC, ';');
        Protocol1_13To1_12_2.SCOREBOARD_TEAM_NAME_REWRITE.put(ChatColor.RESET, '/');
    }
}
