package m31.util;

/**
 * Static utility methods pertain to primitive byte array operations.
 */
public final class Bytes {

  /**
   * Concatenates all the given byte arrays into a new byte array.
   *
   * <p>
   *   The new array contains all of the elements in the given order in {@code arrays}.
   *   When an byte array is returned, it's always a new one.
   * </p>
   *
   * <pre>
   *   Bytes.concat(null)        = null
   *   Bytes.concat(null, null)  = null
   *   Bytes.concat(arr1, null)  = cloned copy of arr1
   *   Bytes.concat(null, arr2)  = cloned copy of arr2
   *   Bytes.concat([0], [1, 2]) = [0, 1, 2]
   * </pre>
   *
   * @param arrays The byte arrays to be merged into the new byte array
   * @return The new byte array contains all the values of the given arrays, {@code null} if
   *         all the given arrays are {@code null}.
   */
  public static byte[] concat(byte[] ... arrays) {
    if (arrays == null) {
      return null;
    }

    int size = 0;

    for (byte[] arr : arrays) {
      if (arr != null) {
        size += arr.length;
      }
    }

    if (size == 0) {
      return null;
    }

    byte[] res = new byte[size];
    int off = 0;
    for (byte[] arr : arrays) {
      if (arr != null) {
        System.arraycopy(arr, 0, res, off, arr.length);
        off += arr.length;
      }
    }

    return res;
  }

  /**
   * Convert an unsigned integer to big-endian (network byte order) byte array
   *
   * <p>
   *   The new array is a 4-byte array represents the given {@code num}
   * </p>
   *
   * <pre>
   *   Bytes.toBytes(0x79347F50) = {(byte) 0x79, (byte) 0x34, (byte) 0x7F, (byte) 0x50}
   * </pre>
   *
   * @param num The unsigned integer in host byte order
   * @return    The network byte order byte array of {@code num}
   *
   * @see <a href="https://en.wikipedia.org/wiki/Endianness">Endianness</a>
   */
  public static byte[] toBytes(long num) {
    return new byte[] {
        (byte) (num >>> 24),
        (byte) (num >>> 16),
        (byte) (num >>> 8),
        (byte) (num)
    };
  }
}
