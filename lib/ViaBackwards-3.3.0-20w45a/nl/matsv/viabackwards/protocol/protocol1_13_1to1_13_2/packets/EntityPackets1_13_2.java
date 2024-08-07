// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_13_1to1_13_2.packets;

import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import java.util.Iterator;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_13;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_13_2;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.List;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.type.types.version.Types1_13;
import us.myles.ViaVersion.api.type.types.version.Types1_13_2;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import nl.matsv.viabackwards.protocol.protocol1_13_1to1_13_2.Protocol1_13_1To1_13_2;

public class EntityPackets1_13_2
{
    public static void register(final Protocol1_13_1To1_13_2 protocol) {
        protocol.registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.SPAWN_MOB, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.UUID);
                this.map((Type)Type.VAR_INT);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map((Type)Type.SHORT);
                this.map((Type)Type.SHORT);
                this.map((Type)Type.SHORT);
                this.map(Types1_13_2.METADATA_LIST, Types1_13.METADATA_LIST);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        for (final Metadata metadata : (List)wrapper.get(Types1_13.METADATA_LIST, 0)) {
                            if (metadata.getMetaType() == MetaType1_13_2.Slot) {
                                metadata.setMetaType((MetaType)MetaType1_13.Slot);
                            }
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.SPAWN_PLAYER, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Types1_13_2.METADATA_LIST, Types1_13.METADATA_LIST);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        for (final Metadata metadata : (List)wrapper.get(Types1_13.METADATA_LIST, 0)) {
                            if (metadata.getMetaType() == MetaType1_13_2.Slot) {
                                metadata.setMetaType((MetaType)MetaType1_13.Slot);
                            }
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.ENTITY_METADATA, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Types1_13_2.METADATA_LIST, Types1_13.METADATA_LIST);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        for (final Metadata metadata : (List)wrapper.get(Types1_13.METADATA_LIST, 0)) {
                            if (metadata.getMetaType() == MetaType1_13_2.Slot) {
                                metadata.setMetaType((MetaType)MetaType1_13.Slot);
                            }
                        }
                    }
                });
            }
        });
    }
}
