// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9;

import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.BossBarStorage;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.BlockPlaceDestroyTracker;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.Cooldown;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.PlayerPosition;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.Levitation;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.EntityTracker;
import us.myles.ViaVersion.api.data.StoredObject;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.Windows;
import de.gerrygames.viarewind.utils.Ticker;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.packets.State;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets.WorldPackets;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets.SpawnPackets;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets.ScoreboardPackets;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets.PlayerPackets;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets.InventoryPackets;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets.EntityPackets;
import us.myles.ViaVersion.api.remapper.ValueTransformer;
import com.google.common.collect.ImmutableSet;
import java.util.Timer;
import us.myles.ViaVersion.api.protocol.Protocol;

public class Protocol1_8TO1_9 extends Protocol
{
    public static final Timer TIMER;
    public static final ImmutableSet<Object> VALID_ATTRIBUTES;
    public static final ValueTransformer<Double, Integer> TO_OLD_INT;
    public static final ValueTransformer<Float, Byte> DEGREES_TO_ANGLE;
    
    protected void registerPackets() {
        EntityPackets.register(this);
        InventoryPackets.register(this);
        PlayerPackets.register(this);
        ScoreboardPackets.register(this);
        SpawnPackets.register(this);
        WorldPackets.register(this);
        this.registerOutgoing(State.PLAY, 31, 0);
        this.registerIncoming(State.PLAY, 11, 0);
    }
    
    public void init(final UserConnection userConnection) {
        Ticker.init();
        userConnection.put((StoredObject)new Windows(userConnection));
        userConnection.put((StoredObject)new EntityTracker(userConnection));
        userConnection.put((StoredObject)new Levitation(userConnection));
        userConnection.put((StoredObject)new PlayerPosition(userConnection));
        userConnection.put((StoredObject)new Cooldown(userConnection));
        userConnection.put((StoredObject)new BlockPlaceDestroyTracker(userConnection));
        userConnection.put((StoredObject)new BossBarStorage(userConnection));
        userConnection.put((StoredObject)new ClientWorld(userConnection));
    }
    
    static {
        TIMER = new Timer("ViaRewind-1_8TO1_9", true);
        final ImmutableSet.Builder<Object> builder = (ImmutableSet.Builder<Object>)ImmutableSet.builder();
        builder.add((Object)"generic.maxHealth");
        builder.add((Object)"generic.followRange");
        builder.add((Object)"generic.knockbackResistance");
        builder.add((Object)"generic.movementSpeed");
        builder.add((Object)"generic.attackDamage");
        builder.add((Object)"horse.jumpStrength");
        builder.add((Object)"zombie.spawnReinforcements");
        VALID_ATTRIBUTES = builder.build();
        TO_OLD_INT = new ValueTransformer<Double, Integer>(Type.INT) {
            public Integer transform(final PacketWrapper wrapper, final Double inputValue) {
                return (int)(inputValue * 32.0);
            }
        };
        DEGREES_TO_ANGLE = new ValueTransformer<Float, Byte>(Type.BYTE) {
            public Byte transform(final PacketWrapper packetWrapper, final Float degrees) throws Exception {
                return (byte)(degrees / 360.0f * 256.0f);
            }
        };
    }
}
