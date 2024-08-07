// 
// Decompiled by Procyon v0.5.36
// 

package nl.matsv.viabackwards.api.entities.meta;

import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;

public interface MetaHandler
{
    Metadata handle(final MetaHandlerEvent p0) throws RemovedValueException;
}
