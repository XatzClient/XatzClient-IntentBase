// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.minecraft.metadata;

import java.util.Objects;

public class Metadata
{
    private int id;
    private MetaType metaType;
    private Object value;
    
    public Metadata(final int id, final MetaType metaType, final Object value) {
        this.id = id;
        this.metaType = metaType;
        this.value = value;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public MetaType getMetaType() {
        return this.metaType;
    }
    
    public void setMetaType(final MetaType metaType) {
        this.metaType = metaType;
    }
    
    public Object getValue() {
        return this.value;
    }
    
    public <T> T getCastedValue() {
        return (T)this.value;
    }
    
    public void setValue(final Object value) {
        this.value = value;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Metadata metadata = (Metadata)o;
        return this.id == metadata.id && Objects.equals(this.metaType, metadata.metaType) && Objects.equals(this.value, metadata.value);
    }
    
    @Override
    public int hashCode() {
        int result = this.id;
        result = 31 * result + ((this.metaType != null) ? this.metaType.hashCode() : 0);
        result = 31 * result + ((this.value != null) ? this.value.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "Metadata{id=" + this.id + ", metaType=" + this.metaType + ", value=" + this.value + '}';
    }
}
