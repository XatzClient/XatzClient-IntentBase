// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.api.rewriters;

import nl.matsv.viabackwards.api.BackwardsProtocol;

public abstract class Rewriter<T extends BackwardsProtocol>
{
    protected final T protocol;
    
    protected Rewriter(final T protocol) {
        this.protocol = protocol;
    }
    
    public void register() {
        this.registerPackets();
        this.registerRewrites();
    }
    
    protected abstract void registerPackets();
    
    protected void registerRewrites() {
    }
    
    public T getProtocol() {
        return this.protocol;
    }
}
