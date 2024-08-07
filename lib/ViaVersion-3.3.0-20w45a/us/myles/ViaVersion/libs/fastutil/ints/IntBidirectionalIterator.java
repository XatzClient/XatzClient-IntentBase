// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.fastutil.ints;

import us.myles.viaversion.libs.fastutil.objects.ObjectBidirectionalIterator;

public interface IntBidirectionalIterator extends IntIterator, ObjectBidirectionalIterator<Integer>
{
    int previousInt();
    
    @Deprecated
    default Integer previous() {
        return this.previousInt();
    }
    
    default int back(final int n) {
        int i = n;
        while (i-- != 0 && this.hasPrevious()) {
            this.previousInt();
        }
        return n - i - 1;
    }
    
    default int skip(final int n) {
        return super.skip(n);
    }
}
