// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.api.entities.storage;

public class EntityObjectData extends EntityData
{
    private final boolean isObject;
    private final int objectData;
    
    public EntityObjectData(final int id, final boolean isObject, final int replacementId, final int objectData) {
        super(id, replacementId);
        this.isObject = isObject;
        this.objectData = objectData;
    }
    
    @Override
    public boolean isObject() {
        return this.isObject;
    }
    
    @Override
    public int getObjectData() {
        return this.objectData;
    }
}
