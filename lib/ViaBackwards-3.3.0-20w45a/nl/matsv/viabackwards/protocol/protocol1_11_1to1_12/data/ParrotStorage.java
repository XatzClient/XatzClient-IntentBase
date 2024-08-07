// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.data;

import nl.matsv.viabackwards.api.entities.storage.EntityStorage;

public class ParrotStorage implements EntityStorage
{
    private boolean tamed;
    private boolean sitting;
    
    public ParrotStorage() {
        this.tamed = true;
        this.sitting = true;
    }
    
    public boolean isTamed() {
        return this.tamed;
    }
    
    public void setTamed(final boolean tamed) {
        this.tamed = tamed;
    }
    
    public boolean isSitting() {
        return this.sitting;
    }
    
    public void setSitting(final boolean sitting) {
        this.sitting = sitting;
    }
}
