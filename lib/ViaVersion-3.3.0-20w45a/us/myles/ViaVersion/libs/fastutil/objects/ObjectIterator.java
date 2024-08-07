// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.fastutil.objects;

import java.util.Iterator;

public interface ObjectIterator<K> extends Iterator<K>
{
    default int skip(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
        }
        int i = n;
        while (i-- != 0 && this.hasNext()) {
            this.next();
        }
        return n - i - 1;
    }
}
