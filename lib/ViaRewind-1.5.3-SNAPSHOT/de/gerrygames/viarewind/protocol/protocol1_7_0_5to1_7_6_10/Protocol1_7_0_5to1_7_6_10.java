// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_0_5to1_7_6_10;

import us.myles.ViaVersion.api.data.UserConnection;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Types1_7_6_10;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.api.remapper.ValueTransformer;
import us.myles.ViaVersion.api.protocol.Protocol;

public class Protocol1_7_0_5to1_7_6_10 extends Protocol
{
    public static final ValueTransformer<String, String> REMOVE_DASHES;
    
    protected void registerPackets() {
        this.registerOutgoing(State.LOGIN, 2, 2, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.STRING, (ValueTransformer)Protocol1_7_0_5to1_7_6_10.REMOVE_DASHES);
                this.map(Type.STRING);
            }
        });
        this.registerOutgoing(State.PLAY, 12, 12, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map((Type)Type.VAR_INT);
                this.map(Type.STRING, (ValueTransformer)Protocol1_7_0_5to1_7_6_10.REMOVE_DASHES);
                this.map(Type.STRING);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        for (int size = (int)packetWrapper.read((Type)Type.VAR_INT), i = 0; i < size * 3; ++i) {
                            packetWrapper.read(Type.STRING);
                        }
                    }
                });
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map((Type)Type.SHORT);
                this.map((Type)Types1_7_6_10.METADATA_LIST);
            }
        });
        this.registerOutgoing(State.PLAY, 62, 62, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.BYTE);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final byte mode = (byte)packetWrapper.get(Type.BYTE, 0);
                        if (mode == 0 || mode == 2) {
                            packetWrapper.passthrough(Type.STRING);
                            packetWrapper.passthrough(Type.STRING);
                            packetWrapper.passthrough(Type.STRING);
                            packetWrapper.passthrough(Type.BYTE);
                        }
                        if (mode == 0 || mode == 3 || mode == 4) {
                            final int size = (short)packetWrapper.read((Type)Type.SHORT);
                            final List<String> entryList = new ArrayList<String>();
                            for (int i = 0; i < size; ++i) {
                                String entry = (String)packetWrapper.read(Type.STRING);
                                if (entry != null) {
                                    if (entry.length() > 16) {
                                        entry = entry.substring(0, 16);
                                    }
                                    if (!entryList.contains(entry)) {
                                        entryList.add(entry);
                                    }
                                }
                            }
                            packetWrapper.write((Type)Type.SHORT, (Object)(short)entryList.size());
                            final Iterator<String> iterator = entryList.iterator();
                            while (iterator.hasNext()) {
                                final String entry = iterator.next();
                                packetWrapper.write(Type.STRING, (Object)entry);
                            }
                        }
                    }
                });
            }
        });
    }
    
    public void init(final UserConnection userConnection) {
    }
    
    static {
        REMOVE_DASHES = new ValueTransformer<String, String>(Type.STRING) {
            public String transform(final PacketWrapper packetWrapper, final String s) {
                return s.replace("-", "");
            }
        };
    }
}
