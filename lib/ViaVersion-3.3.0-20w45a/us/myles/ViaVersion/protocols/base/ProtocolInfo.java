// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.protocols.base;

import us.myles.ViaVersion.api.protocol.ProtocolVersion;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.protocol.ProtocolPipeline;
import java.util.UUID;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.api.data.StoredObject;

public class ProtocolInfo extends StoredObject
{
    private State state;
    private int protocolVersion;
    private int serverProtocolVersion;
    private String username;
    private UUID uuid;
    private ProtocolPipeline pipeline;
    
    public ProtocolInfo(final UserConnection user) {
        super(user);
        this.state = State.HANDSHAKE;
        this.protocolVersion = -1;
        this.serverProtocolVersion = -1;
    }
    
    public State getState() {
        return this.state;
    }
    
    public void setState(final State state) {
        this.state = state;
    }
    
    public int getProtocolVersion() {
        return this.protocolVersion;
    }
    
    public void setProtocolVersion(final int protocolVersion) {
        final ProtocolVersion protocol = ProtocolVersion.getProtocol(protocolVersion);
        this.protocolVersion = protocol.getVersion();
    }
    
    public int getServerProtocolVersion() {
        return this.serverProtocolVersion;
    }
    
    public void setServerProtocolVersion(final int serverProtocolVersion) {
        final ProtocolVersion protocol = ProtocolVersion.getProtocol(serverProtocolVersion);
        this.serverProtocolVersion = protocol.getVersion();
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(final String username) {
        this.username = username;
    }
    
    public UUID getUuid() {
        return this.uuid;
    }
    
    public void setUuid(final UUID uuid) {
        this.uuid = uuid;
    }
    
    public ProtocolPipeline getPipeline() {
        return this.pipeline;
    }
    
    public void setPipeline(final ProtocolPipeline pipeline) {
        this.pipeline = pipeline;
    }
    
    @Override
    public String toString() {
        return "ProtocolInfo{state=" + this.state + ", protocolVersion=" + this.protocolVersion + ", serverProtocolVersion=" + this.serverProtocolVersion + ", username='" + this.username + '\'' + ", uuid=" + this.uuid + '}';
    }
}
