// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.api.entities.meta;

import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import org.jetbrains.annotations.Nullable;
import us.myles.ViaVersion.api.entities.EntityType;

public class MetaHandlerSettings
{
    private EntityType filterType;
    private boolean filterFamily;
    private int filterIndex;
    private MetaHandler handler;
    
    public MetaHandlerSettings() {
        this.filterIndex = -1;
    }
    
    public MetaHandlerSettings filter(final EntityType type) {
        return this.filter(type, this.filterFamily, this.filterIndex);
    }
    
    public MetaHandlerSettings filter(final EntityType type, final boolean filterFamily) {
        return this.filter(type, filterFamily, this.filterIndex);
    }
    
    public MetaHandlerSettings filter(final int index) {
        return this.filter(this.filterType, this.filterFamily, index);
    }
    
    public MetaHandlerSettings filter(final EntityType type, final int index) {
        return this.filter(type, this.filterFamily, index);
    }
    
    public MetaHandlerSettings filter(final EntityType type, final boolean filterFamily, final int index) {
        this.filterType = type;
        this.filterFamily = filterFamily;
        this.filterIndex = index;
        return this;
    }
    
    public void handle(@Nullable final MetaHandler handler) {
        this.handler = handler;
    }
    
    public void handleIndexChange(final int newIndex) {
        final Metadata data;
        this.handle(e -> {
            data = e.getData();
            data.setId(newIndex);
            return data;
        });
    }
    
    public void removed() {
        this.handle(e -> {
            throw RemovedValueException.EX;
        });
    }
    
    public boolean hasHandler() {
        return this.handler != null;
    }
    
    public boolean hasType() {
        return this.filterType != null;
    }
    
    public boolean hasIndex() {
        return this.filterIndex > -1;
    }
    
    public boolean isFilterFamily() {
        return this.filterFamily;
    }
    
    public boolean isGucci(final EntityType type, final Metadata metadata) {
        if (!this.hasHandler()) {
            return false;
        }
        if (this.hasType()) {
            if (this.filterFamily) {
                if (type.isOrHasParent(this.filterType)) {
                    return !this.hasIndex() || metadata.getId() == this.filterIndex;
                }
            }
            else if (this.filterType.is(type)) {
                return !this.hasIndex() || metadata.getId() == this.filterIndex;
            }
            return false;
        }
        return !this.hasIndex() || metadata.getId() == this.filterIndex;
    }
    
    public EntityType getFilterType() {
        return this.filterType;
    }
    
    public int getFilterIndex() {
        return this.filterIndex;
    }
    
    @Nullable
    public MetaHandler getHandler() {
        return this.handler;
    }
    
    @Override
    public String toString() {
        return "MetaHandlerSettings{filterType=" + this.filterType + ", filterFamily=" + this.filterFamily + ", filterIndex=" + this.filterIndex + ", handler=" + this.handler + '}';
    }
}
