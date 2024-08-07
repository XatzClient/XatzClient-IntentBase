// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.protocol;

public abstract class SimpleProtocol extends Protocol<DummyPacketTypes, DummyPacketTypes, DummyPacketTypes, DummyPacketTypes>
{
    protected SimpleProtocol() {
    }
    
    public enum DummyPacketTypes implements ClientboundPacketType, ServerboundPacketType
    {
    }
}
