// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.api.entities.storage;

import org.jetbrains.annotations.Nullable;
import java.util.Iterator;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import java.util.List;

public class MetaStorage
{
    private List<Metadata> metaDataList;
    
    public MetaStorage(final List<Metadata> metaDataList) {
        this.metaDataList = metaDataList;
    }
    
    public boolean has(final Metadata data) {
        return this.metaDataList.contains(data);
    }
    
    public void delete(final Metadata data) {
        this.metaDataList.remove(data);
    }
    
    public void delete(final int index) {
        this.metaDataList.removeIf(meta -> meta.getId() == index);
    }
    
    public void add(final Metadata data) {
        this.metaDataList.add(data);
    }
    
    @Nullable
    public Metadata get(final int index) {
        for (final Metadata meta : this.metaDataList) {
            if (index == meta.getId()) {
                return meta;
            }
        }
        return null;
    }
    
    public Metadata getOrDefault(final int index, final Metadata data) {
        return this.getOrDefault(index, false, data);
    }
    
    public Metadata getOrDefault(final int index, final boolean removeIfExists, final Metadata data) {
        final Metadata existingData = this.get(index);
        if (removeIfExists && existingData != null) {
            this.delete(existingData);
        }
        return (existingData != null) ? existingData : data;
    }
    
    public List<Metadata> getMetaDataList() {
        return this.metaDataList;
    }
    
    public void setMetaDataList(final List<Metadata> metaDataList) {
        this.metaDataList = metaDataList;
    }
    
    @Override
    public String toString() {
        return "MetaStorage{metaDataList=" + this.metaDataList + '}';
    }
}
