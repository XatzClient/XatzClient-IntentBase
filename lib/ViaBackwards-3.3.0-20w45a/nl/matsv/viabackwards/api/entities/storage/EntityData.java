// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.api.entities.storage;

import org.jetbrains.annotations.Nullable;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ChatRewriter;

public class EntityData
{
    private final int id;
    private final int replacementId;
    private Object mobName;
    private MetaCreator defaultMeta;
    
    public EntityData(final int id, final int replacementId) {
        this.id = id;
        this.replacementId = replacementId;
    }
    
    public EntityData jsonName(final String name) {
        this.mobName = ChatRewriter.legacyTextToJson(name);
        return this;
    }
    
    public EntityData mobName(final String name) {
        this.mobName = name;
        return this;
    }
    
    public EntityData spawnMetadata(final MetaCreator handler) {
        this.defaultMeta = handler;
        return this;
    }
    
    public boolean hasBaseMeta() {
        return this.defaultMeta != null;
    }
    
    public int getId() {
        return this.id;
    }
    
    @Nullable
    public Object getMobName() {
        return this.mobName;
    }
    
    public int getReplacementId() {
        return this.replacementId;
    }
    
    @Nullable
    public MetaCreator getDefaultMeta() {
        return this.defaultMeta;
    }
    
    public boolean isObject() {
        return false;
    }
    
    public int getObjectData() {
        return -1;
    }
    
    @Override
    public String toString() {
        return "EntityData{id=" + this.id + ", mobName='" + this.mobName + '\'' + ", replacementId=" + this.replacementId + ", defaultMeta=" + this.defaultMeta + '}';
    }
    
    @FunctionalInterface
    public interface MetaCreator
    {
        void createMeta(final MetaStorage p0);
    }
}
