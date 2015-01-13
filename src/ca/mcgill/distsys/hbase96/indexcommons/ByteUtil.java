package ca.mcgill.distsys.hbase96.indexcommons;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Comparator;

import org.apache.hadoop.io.RawComparator;

import sun.misc.*;

/**
 * This is an extract from org.apache.hadoop.hbase.util.Bytes that only contains what is necessary for the Bytes.COMPARATOR.
 * It has been made Serializable so the TreeMaps that use that comparator can be serialized.
 *
 */
public class ByteUtil implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int SIZEOF_LONG = Long.SIZE / Byte.SIZE;

    public static class ByteArrayComparator implements RawComparator<byte[]>, Serializable {
        private static final long serialVersionUID = 3739538199487703082L;


        /**
         * Constructor
         */
        public ByteArrayComparator() {
            super();
        }

        public int compare(byte[] left, byte[] right) {
            return compareTo(left, right);
        }

        public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
            return LexicographicalComparerHolder.BEST_COMPARER.compareTo(b1, s1, l1, b2, s2, l2);
        }
    }

    public static Comparator<byte[]> BYTES_COMPARATOR = new ByteArrayComparator();

    interface Comparer<T> {
        abstract public int compareTo(T buffer1, int offset1, int length1,
            T buffer2, int offset2, int length2);
      }
    
    public static int compareTo(final byte[] left, final byte[] right) {
        return LexicographicalComparerHolder.BEST_COMPARER.compareTo(left, 0, left.length, right, 0, right.length);
    }
    
    static Comparer<byte[]> lexicographicalComparerJavaImpl() {
      return LexicographicalComparerHolder.PureJavaComparer.INSTANCE;
    }

    /**
     * Provides a lexicographical comparer implementation; either a Java
     * implementation or a faster implementation based on {@link Unsafe}.
     * 
     * <p>
     * Uses reflection to gracefully fall back to the Java implementation if
     * {@code Unsafe} isn't available.
     */
    static class LexicographicalComparerHolder implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        static final String UNSAFE_COMPARER_NAME = LexicographicalComparerHolder.class.getName() + "$UnsafeComparer";

        static final Comparer<byte[]> BEST_COMPARER = getBestComparer();

        /**
         * Returns the Unsafe-using Comparer, or falls back to the pure-Java
         * implementation if unable to do so.
         */
        static Comparer<byte[]> getBestComparer() {
            try {
                Class<?> theClass = Class.forName(UNSAFE_COMPARER_NAME);

                // yes, UnsafeComparer does implement Comparer<byte[]>
                @SuppressWarnings("unchecked")
                Comparer<byte[]> comparer = (Comparer<byte[]>) theClass.getEnumConstants()[0];
                return comparer;
            } catch (Throwable t) { // ensure we really catch *everything*
                return lexicographicalComparerJavaImpl();
            }
        }

        enum PureJavaComparer implements Comparer<byte[]> {
            INSTANCE;

            public int compareTo(byte[] buffer1, int offset1, int length1, byte[] buffer2, int offset2, int length2) {
                // Short circuit equal case
                if (buffer1 == buffer2 && offset1 == offset2 && length1 == length2) {
                    return 0;
                }
                // Bring WritableComparator code local
                int end1 = offset1 + length1;
                int end2 = offset2 + length2;
                for (int i = offset1, j = offset2; i < end1 && j < end2; i++, j++) {
                    int a = (buffer1[i] & 0xff);
                    int b = (buffer2[j] & 0xff);
                    if (a != b) {
                        return a - b;
                    }
                }
                return length1 - length2;
            }
        }

        enum UnsafeComparer implements Comparer<byte[]> {
            INSTANCE;

            static final Unsafe theUnsafe;

            /** The offset to the first element in a byte array. */
            static final int BYTE_ARRAY_BASE_OFFSET;

            static {
                theUnsafe = (Unsafe) AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        try {
                            Field f = Unsafe.class.getDeclaredField("theUnsafe");
                            f.setAccessible(true);
                            return f.get(null);
                        } catch (NoSuchFieldException e) {
                            // It doesn't matter what we throw;
                            // it's swallowed in getBestComparer().
                            throw new Error();
                        } catch (IllegalAccessException e) {
                            throw new Error();
                        }
                    }
                });

                BYTE_ARRAY_BASE_OFFSET = theUnsafe.arrayBaseOffset(byte[].class);

                // sanity check - this should never fail
                if (theUnsafe.arrayIndexScale(byte[].class) != 1) {
                    throw new AssertionError();
                }
            }

            static final boolean littleEndian = ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN);

            /**
             * Returns true if x1 is less than x2, when both values are treated
             * as unsigned.
             */
            static boolean lessThanUnsigned(long x1, long x2) {
                return (x1 + Long.MIN_VALUE) < (x2 + Long.MIN_VALUE);
            }

            /**
             * Lexicographically compare two arrays.
             * 
             * @param buffer1
             *            left operand
             * @param buffer2
             *            right operand
             * @param offset1
             *            Where to start comparing in the left buffer
             * @param offset2
             *            Where to start comparing in the right buffer
             * @param length1
             *            How much to compare from the left buffer
             * @param length2
             *            How much to compare from the right buffer
             * @return 0 if equal, < 0 if left is less than right, etc.
             */
            public int compareTo(byte[] buffer1, int offset1, int length1, byte[] buffer2, int offset2, int length2) {
                // Short circuit equal case
                if (buffer1 == buffer2 && offset1 == offset2 && length1 == length2) {
                    return 0;
                }
                int minLength = Math.min(length1, length2);
                int minWords = minLength / SIZEOF_LONG;
                int offset1Adj = offset1 + BYTE_ARRAY_BASE_OFFSET;
                int offset2Adj = offset2 + BYTE_ARRAY_BASE_OFFSET;

                /*
                 * Compare 8 bytes at a time. Benchmarking shows comparing 8
                 * bytes at a time is no slower than comparing 4 bytes at a time
                 * even on 32-bit. On the other hand, it is substantially faster
                 * on 64-bit.
                 */
                for (int i = 0; i < minWords * SIZEOF_LONG; i += SIZEOF_LONG) {
                    long lw = theUnsafe.getLong(buffer1, offset1Adj + (long) i);
                    long rw = theUnsafe.getLong(buffer2, offset2Adj + (long) i);
                    long diff = lw ^ rw;

                    if (diff != 0) {
                        if (!littleEndian) {
                            return lessThanUnsigned(lw, rw) ? -1 : 1;
                        }

                        // Use binary search
                        int n = 0;
                        int y;
                        int x = (int) diff;
                        if (x == 0) {
                            x = (int) (diff >>> 32);
                            n = 32;
                        }

                        y = x << 16;
                        if (y == 0) {
                            n += 16;
                        } else {
                            x = y;
                        }

                        y = x << 8;
                        if (y == 0) {
                            n += 8;
                        }
                        return (int) (((lw >>> n) & 0xFFL) - ((rw >>> n) & 0xFFL));
                    }
                }

                // The epilogue to cover the last (minLength % 8) elements.
                for (int i = minWords * SIZEOF_LONG; i < minLength; i++) {
                    int a = (buffer1[offset1 + i] & 0xff);
                    int b = (buffer2[offset2 + i] & 0xff);
                    if (a != b) {
                        return a - b;
                    }
                }
                return length1 - length2;
            }
        }
    }
}
