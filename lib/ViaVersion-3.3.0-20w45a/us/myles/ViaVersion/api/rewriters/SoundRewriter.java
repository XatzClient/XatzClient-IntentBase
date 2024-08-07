// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.rewriters;

import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.protocol.Protocol;

public class SoundRewriter
{
    protected final Protocol protocol;
    protected final IdRewriteFunction idRewriter;
    
    public SoundRewriter(final Protocol protocol) {
        this.protocol = protocol;
        this.idRewriter = (id -> protocol.getMappingData().getSoundMappings().getNewId(id));
    }
    
    public SoundRewriter(final Protocol protocol, final IdRewriteFunction idRewriter) {
        this.protocol = protocol;
        this.idRewriter = idRewriter;
    }
    
    public void registerSound(final ClientboundPacketType packetType) {
        this.protocol.registerOutgoing(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                final int soundId;
                final int mappedId;
                this.handler(wrapper -> {
                    soundId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    mappedId = SoundRewriter.this.idRewriter.rewrite(soundId);
                    if (mappedId == -1) {
                        wrapper.cancel();
                    }
                    else if (soundId != mappedId) {
                        wrapper.set(Type.VAR_INT, 0, mappedId);
                    }
                });
            }
        });
    }
}
