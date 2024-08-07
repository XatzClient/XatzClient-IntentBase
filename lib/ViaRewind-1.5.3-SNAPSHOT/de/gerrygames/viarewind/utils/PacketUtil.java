// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.utils;

import us.myles.ViaVersion.exception.CancelException;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.PacketWrapper;

public class PacketUtil
{
    public static void sendToServer(final PacketWrapper packet, final Class<? extends Protocol> packetProtocol) {
        sendToServer(packet, packetProtocol, true);
    }
    
    public static void sendToServer(final PacketWrapper packet, final Class<? extends Protocol> packetProtocol, final boolean skipCurrentPipeline) {
        sendToServer(packet, packetProtocol, skipCurrentPipeline, false);
    }
    
    public static void sendToServer(final PacketWrapper packet, final Class<? extends Protocol> packetProtocol, final boolean skipCurrentPipeline, final boolean currentThread) {
        try {
            packet.sendToServer((Class)packetProtocol, skipCurrentPipeline, currentThread);
        }
        catch (CancelException ex2) {}
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static boolean sendPacket(final PacketWrapper packet, final Class<? extends Protocol> packetProtocol) {
        return sendPacket(packet, packetProtocol, true);
    }
    
    public static boolean sendPacket(final PacketWrapper packet, final Class<? extends Protocol> packetProtocol, final boolean skipCurrentPipeline) {
        return sendPacket(packet, packetProtocol, true, false);
    }
    
    public static boolean sendPacket(final PacketWrapper packet, final Class<? extends Protocol> packetProtocol, final boolean skipCurrentPipeline, final boolean currentThread) {
        try {
            packet.send((Class)packetProtocol, skipCurrentPipeline, currentThread);
            return true;
        }
        catch (CancelException ex2) {}
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
