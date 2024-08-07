// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import us.myles.viaversion.libs.bungeecordchat.api.ChatColor;
import java.util.Optional;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;
import io.netty.buffer.ByteBuf;
import us.myles.ViaVersion.protocols.base.ProtocolInfo;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.Scoreboard;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.api.protocol.Protocol;

public class ScoreboardPackets
{
    public static void register(final Protocol protocol) {
        protocol.registerOutgoing(State.PLAY, 59, 59, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        String name = (String)packetWrapper.passthrough(Type.STRING);
                        if (name.length() > 16) {
                            packetWrapper.set(Type.STRING, 0, (Object)(name = name.substring(0, 16)));
                        }
                        final byte mode = (byte)packetWrapper.read(Type.BYTE);
                        final Scoreboard scoreboard = (Scoreboard)packetWrapper.user().get((Class)Scoreboard.class);
                        if (mode == 0) {
                            if (scoreboard.objectiveExists(name)) {
                                packetWrapper.cancel();
                                return;
                            }
                            scoreboard.addObjective(name);
                        }
                        else if (mode == 1) {
                            if (!scoreboard.objectiveExists(name)) {
                                packetWrapper.cancel();
                                return;
                            }
                            if (scoreboard.getColorIndependentSidebar() != null) {
                                final String username = ((ProtocolInfo)packetWrapper.user().get((Class)ProtocolInfo.class)).getUsername();
                                final Optional<Byte> color = scoreboard.getPlayerTeamColor(username);
                                if (color.isPresent()) {
                                    final String sidebar = scoreboard.getColorDependentSidebar().get(color.get());
                                    if (name.equals(sidebar)) {
                                        final PacketWrapper sidebarPacket = new PacketWrapper(61, (ByteBuf)null, packetWrapper.user());
                                        sidebarPacket.write(Type.BYTE, (Object)1);
                                        sidebarPacket.write(Type.STRING, (Object)scoreboard.getColorIndependentSidebar());
                                        PacketUtil.sendPacket(sidebarPacket, Protocol1_7_6_10TO1_8.class);
                                    }
                                }
                            }
                            scoreboard.removeObjective(name);
                        }
                        else if (mode == 2 && !scoreboard.objectiveExists(name)) {
                            packetWrapper.cancel();
                            return;
                        }
                        if (mode == 0 || mode == 2) {
                            final String displayName = (String)packetWrapper.passthrough(Type.STRING);
                            if (displayName.length() > 32) {
                                packetWrapper.set(Type.STRING, 1, (Object)displayName.substring(0, 32));
                            }
                            packetWrapper.read(Type.STRING);
                        }
                        else {
                            packetWrapper.write(Type.STRING, (Object)"");
                        }
                        packetWrapper.write(Type.BYTE, (Object)mode);
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 60, 60, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final Scoreboard scoreboard = (Scoreboard)packetWrapper.user().get((Class)Scoreboard.class);
                        String name = (String)packetWrapper.passthrough(Type.STRING);
                        final byte mode = (byte)packetWrapper.passthrough(Type.BYTE);
                        if (mode == 1) {
                            name = scoreboard.removeTeamForScore(name);
                        }
                        else {
                            name = scoreboard.sendTeamForScore(name);
                        }
                        if (name.length() > 16) {
                            name = ChatColor.stripColor(name);
                            if (name.length() > 16) {
                                name = name.substring(0, 16);
                            }
                        }
                        packetWrapper.set(Type.STRING, 0, (Object)name);
                        String objective = (String)packetWrapper.read(Type.STRING);
                        if (objective.length() > 16) {
                            objective = objective.substring(0, 16);
                        }
                        if (mode != 1) {
                            final int score = (int)packetWrapper.read((Type)Type.VAR_INT);
                            packetWrapper.write(Type.STRING, (Object)objective);
                            packetWrapper.write(Type.INT, (Object)score);
                        }
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 61, 61, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.BYTE);
                this.map(Type.STRING);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        byte position = (byte)packetWrapper.get(Type.BYTE, 0);
                        final String name = (String)packetWrapper.get(Type.STRING, 0);
                        final Scoreboard scoreboard = (Scoreboard)packetWrapper.user().get((Class)Scoreboard.class);
                        if (position > 2) {
                            final byte receiverTeamColor = (byte)(position - 3);
                            scoreboard.getColorDependentSidebar().put(receiverTeamColor, name);
                            final String username = ((ProtocolInfo)packetWrapper.user().get((Class)ProtocolInfo.class)).getUsername();
                            final Optional<Byte> color = scoreboard.getPlayerTeamColor(username);
                            if (color.isPresent() && color.get() == receiverTeamColor) {
                                position = 1;
                            }
                            else {
                                position = -1;
                            }
                        }
                        else if (position == 1) {
                            scoreboard.setColorIndependentSidebar(name);
                            final String username2 = ((ProtocolInfo)packetWrapper.user().get((Class)ProtocolInfo.class)).getUsername();
                            final Optional<Byte> color2 = scoreboard.getPlayerTeamColor(username2);
                            if (color2.isPresent() && scoreboard.getColorDependentSidebar().containsKey(color2.get())) {
                                position = -1;
                            }
                        }
                        if (position == -1) {
                            packetWrapper.cancel();
                            return;
                        }
                        packetWrapper.set(Type.BYTE, 0, (Object)position);
                    }
                });
            }
        });
        protocol.registerOutgoing(State.PLAY, 62, 62, (PacketRemapper)new PacketRemapper() {
            public void registerMap() {
                this.map(Type.STRING);
                this.handler((PacketHandler)new PacketHandler() {
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final String team = (String)packetWrapper.get(Type.STRING, 0);
                        if (team == null) {
                            packetWrapper.cancel();
                            return;
                        }
                        final byte mode = (byte)packetWrapper.passthrough(Type.BYTE);
                        final Scoreboard scoreboard = (Scoreboard)packetWrapper.user().get((Class)Scoreboard.class);
                        if (mode != 0 && !scoreboard.teamExists(team)) {
                            packetWrapper.cancel();
                            return;
                        }
                        if (mode == 0 && scoreboard.teamExists(team)) {
                            scoreboard.removeTeam(team);
                            final PacketWrapper remove = new PacketWrapper(62, (ByteBuf)null, packetWrapper.user());
                            remove.write(Type.STRING, (Object)team);
                            remove.write(Type.BYTE, (Object)1);
                            PacketUtil.sendPacket(remove, Protocol1_7_6_10TO1_8.class, true, true);
                        }
                        if (mode == 0) {
                            scoreboard.addTeam(team);
                        }
                        else if (mode == 1) {
                            scoreboard.removeTeam(team);
                        }
                        if (mode == 0 || mode == 2) {
                            packetWrapper.passthrough(Type.STRING);
                            packetWrapper.passthrough(Type.STRING);
                            packetWrapper.passthrough(Type.STRING);
                            packetWrapper.passthrough(Type.BYTE);
                            packetWrapper.read(Type.STRING);
                            final byte color = (byte)packetWrapper.read(Type.BYTE);
                            if (mode == 2 && scoreboard.getTeamColor(team).get() != color) {
                                final String username = ((ProtocolInfo)packetWrapper.user().get((Class)ProtocolInfo.class)).getUsername();
                                final String sidebar = scoreboard.getColorDependentSidebar().get(color);
                                final PacketWrapper sidebarPacket = packetWrapper.create(61);
                                sidebarPacket.write(Type.BYTE, (Object)1);
                                sidebarPacket.write(Type.STRING, (Object)((sidebar == null) ? "" : sidebar));
                                PacketUtil.sendPacket(sidebarPacket, Protocol1_7_6_10TO1_8.class);
                            }
                            scoreboard.setTeamColor(team, color);
                        }
                        if (mode == 0 || mode == 3 || mode == 4) {
                            final byte color = scoreboard.getTeamColor(team).get();
                            final String[] entries = (String[])packetWrapper.read(Type.STRING_ARRAY);
                            final List<String> entryList = new ArrayList<String>();
                            for (int i = 0; i < entries.length; ++i) {
                                final String entry = entries[i];
                                final String username2 = ((ProtocolInfo)packetWrapper.user().get((Class)ProtocolInfo.class)).getUsername();
                                if (mode == 4) {
                                    if (!scoreboard.isPlayerInTeam(entry, team)) {
                                        continue;
                                    }
                                    scoreboard.removePlayerFromTeam(entry, team);
                                    if (entry.equals(username2)) {
                                        final PacketWrapper sidebarPacket2 = packetWrapper.create(61);
                                        sidebarPacket2.write(Type.BYTE, (Object)1);
                                        sidebarPacket2.write(Type.STRING, (Object)((scoreboard.getColorIndependentSidebar() == null) ? "" : scoreboard.getColorIndependentSidebar()));
                                        PacketUtil.sendPacket(sidebarPacket2, Protocol1_7_6_10TO1_8.class);
                                    }
                                }
                                else {
                                    scoreboard.addPlayerToTeam(entry, team);
                                    if (entry.equals(username2) && scoreboard.getColorDependentSidebar().containsKey(color)) {
                                        final PacketWrapper displayObjective = packetWrapper.create(61);
                                        displayObjective.write(Type.BYTE, (Object)1);
                                        displayObjective.write(Type.STRING, (Object)scoreboard.getColorDependentSidebar().get(color));
                                        PacketUtil.sendPacket(displayObjective, Protocol1_7_6_10TO1_8.class);
                                    }
                                }
                                entryList.add(entry);
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
}
