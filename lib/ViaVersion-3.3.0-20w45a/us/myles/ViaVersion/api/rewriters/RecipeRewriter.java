// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.rewriters;

import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.PacketWrapper;
import java.util.HashMap;
import java.util.Map;
import us.myles.ViaVersion.api.protocol.Protocol;

public abstract class RecipeRewriter
{
    protected final Protocol protocol;
    protected final ItemRewriter.RewriteFunction rewriter;
    protected final Map<String, RecipeConsumer> recipeHandlers;
    
    protected RecipeRewriter(final Protocol protocol, final ItemRewriter.RewriteFunction rewriter) {
        this.recipeHandlers = new HashMap<String, RecipeConsumer>();
        this.protocol = protocol;
        this.rewriter = rewriter;
    }
    
    public void handle(final PacketWrapper wrapper, final String type) throws Exception {
        final RecipeConsumer handler = this.recipeHandlers.get(type);
        if (handler != null) {
            handler.accept(wrapper);
        }
    }
    
    public void registerDefaultHandler(final ClientboundPacketType packetType) {
        this.protocol.registerOutgoing(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                int size;
                int i;
                String type;
                String id;
                this.handler(wrapper -> {
                    for (size = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < size; ++i) {
                        type = wrapper.passthrough(Type.STRING).replace("minecraft:", "");
                        id = wrapper.passthrough(Type.STRING);
                        RecipeRewriter.this.handle(wrapper, type);
                    }
                });
            }
        });
    }
    
    @FunctionalInterface
    public interface RecipeConsumer
    {
        void accept(final PacketWrapper p0) throws Exception;
    }
}
