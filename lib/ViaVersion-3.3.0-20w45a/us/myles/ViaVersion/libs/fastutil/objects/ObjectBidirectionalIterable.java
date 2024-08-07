// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.fastutil.objects;

import java.util.Iterator;

public interface ObjectBidirectionalIterable<K> extends ObjectIterable<K>
{
    ObjectBidirectionalIterator<K> iterator();
}
