// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.api.entities.meta;

import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import nl.matsv.viabackwards.api.entities.storage.MetaStorage;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import us.myles.ViaVersion.api.data.UserConnection;

public class MetaHandlerEvent
{
    private final UserConnection user;
    private final EntityTracker.StoredEntity entity;
    private final int index;
    private final Metadata data;
    private final MetaStorage storage;
    private List<Metadata> extraData;
    
    public MetaHandlerEvent(final UserConnection user, final EntityTracker.StoredEntity entity, final int index, final Metadata data, final MetaStorage storage) {
        this.user = user;
        this.entity = entity;
        this.index = index;
        this.data = data;
        this.storage = storage;
    }
    
    public boolean hasData() {
        return this.data != null;
    }
    
    public Metadata getMetaByIndex(final int index) {
        for (final Metadata meta : this.storage.getMetaDataList()) {
            if (index == meta.getId()) {
                return meta;
            }
        }
        return null;
    }
    
    public void clearExtraData() {
        this.extraData = null;
    }
    
    public void createMeta(final Metadata metadata) {
        ((this.extraData != null) ? this.extraData : (this.extraData = new ArrayList<Metadata>())).add(metadata);
    }
    
    public UserConnection getUser() {
        return this.user;
    }
    
    public EntityTracker.StoredEntity getEntity() {
        return this.entity;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public Metadata getData() {
        return this.data;
    }
    
    public MetaStorage getStorage() {
        return this.storage;
    }
    
    @Nullable
    public List<Metadata> getExtraData() {
        return this.extraData;
    }
}
