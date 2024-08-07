// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.entities;

import org.jetbrains.annotations.Nullable;

public interface EntityType
{
    int getId();
    
    @Nullable
    EntityType getParent();
    
    String name();
    
    default boolean is(final EntityType... types) {
        for (final EntityType type : types) {
            if (this.is(type)) {
                return true;
            }
        }
        return false;
    }
    
    default boolean is(final EntityType type) {
        return this == type;
    }
    
    default boolean isOrHasParent(final EntityType type) {
        EntityType parent = this;
        while (!parent.equals(type)) {
            parent = parent.getParent();
            if (parent == null) {
                return false;
            }
        }
        return true;
    }
}
