// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.protocol1_13to1_12_2.blockconnections;

import us.myles.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.minecraft.BlockFace;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.data.UserConnection;
import java.util.Set;
import java.util.HashSet;
import us.myles.viaversion.libs.fastutil.ints.Int2IntMap;

public class FlowerConnectionHandler extends ConnectionHandler
{
    private static final Int2IntMap flowers;
    
    static ConnectionData.ConnectorInitAction init() {
        final Set<String> baseFlower = new HashSet<String>();
        baseFlower.add("minecraft:rose_bush");
        baseFlower.add("minecraft:sunflower");
        baseFlower.add("minecraft:peony");
        baseFlower.add("minecraft:tall_grass");
        baseFlower.add("minecraft:large_fern");
        baseFlower.add("minecraft:lilac");
        final FlowerConnectionHandler handler = new FlowerConnectionHandler();
        final Set set;
        final FlowerConnectionHandler value;
        return blockData -> {
            if (set.contains(blockData.getMinecraftKey())) {
                ConnectionData.connectionHandlerMap.put(blockData.getSavedBlockStateId(), value);
                if (blockData.getValue("half").equals("lower")) {
                    blockData.set("half", "upper");
                    FlowerConnectionHandler.flowers.put(blockData.getSavedBlockStateId(), blockData.getBlockStateId());
                }
            }
        };
    }
    
    @Override
    public int connect(final UserConnection user, final Position position, final int blockState) {
        final int blockBelowId = this.getBlockData(user, position.getRelative(BlockFace.BOTTOM));
        final int connectBelow = FlowerConnectionHandler.flowers.get(blockBelowId);
        if (connectBelow != 0) {
            final int blockAboveId = this.getBlockData(user, position.getRelative(BlockFace.TOP));
            if (Via.getConfig().isStemWhenBlockAbove()) {
                if (blockAboveId == 0) {
                    return connectBelow;
                }
            }
            else if (!FlowerConnectionHandler.flowers.containsKey(blockAboveId)) {
                return connectBelow;
            }
        }
        return blockState;
    }
    
    static {
        flowers = new Int2IntOpenHashMap();
    }
}
