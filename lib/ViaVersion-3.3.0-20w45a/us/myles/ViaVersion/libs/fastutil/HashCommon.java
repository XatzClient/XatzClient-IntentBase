// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.fastutil;

public class HashCommon
{
    private static final int INT_PHI = -1640531527;
    private static final int INV_INT_PHI = 340573321;
    private static final long LONG_PHI = -7046029254386353131L;
    private static final long INV_LONG_PHI = -1018231460777725123L;
    
    protected HashCommon() {
    }
    
    public static int murmurHash3(int x) {
        x ^= x >>> 16;
        x *= -2048144789;
        x ^= x >>> 13;
        x *= -1028477387;
        x ^= x >>> 16;
        return x;
    }
    
    public static long murmurHash3(long x) {
        x ^= x >>> 33;
        x *= -49064778989728563L;
        x ^= x >>> 33;
        x *= -4265267296055464877L;
        x ^= x >>> 33;
        return x;
    }
    
    public static int mix(final int x) {
        final int h = x * -1640531527;
        return h ^ h >>> 16;
    }
    
    public static int invMix(final int x) {
        return (x ^ x >>> 16) * 340573321;
    }
    
    public static long mix(final long x) {
        long h = x * -7046029254386353131L;
        h ^= h >>> 32;
        return h ^ h >>> 16;
    }
    
    public static long invMix(long x) {
        x ^= x >>> 32;
        x ^= x >>> 16;
        return (x ^ x >>> 32) * -1018231460777725123L;
    }
    
    public static int float2int(final float f) {
        return Float.floatToRawIntBits(f);
    }
    
    public static int double2int(final double d) {
        final long l = Double.doubleToRawLongBits(d);
        return (int)(l ^ l >>> 32);
    }
    
    public static int long2int(final long l) {
        return (int)(l ^ l >>> 32);
    }
    
    public static int nextPowerOfTwo(int x) {
        if (x == 0) {
            return 1;
        }
        x = (--x | x >> 1);
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        return (x | x >> 16) + 1;
    }
    
    public static long nextPowerOfTwo(long x) {
        if (x == 0L) {
            return 1L;
        }
        --x;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        return (x | x >> 32) + 1L;
    }
    
    public static int maxFill(final int n, final float f) {
        return Math.min((int)Math.ceil(n * f), n - 1);
    }
    
    public static long maxFill(final long n, final float f) {
        return Math.min((long)Math.ceil(n * f), n - 1L);
    }
    
    public static int arraySize(final int expected, final float f) {
        final long s = Math.max(2L, nextPowerOfTwo((long)Math.ceil(expected / f)));
        if (s > 1073741824L) {
            throw new IllegalArgumentException("Too large (" + expected + " expected elements with load factor " + f + ")");
        }
        return (int)s;
    }
    
    public static long bigArraySize(final long expected, final float f) {
        return nextPowerOfTwo((long)Math.ceil(expected / f));
    }
}
