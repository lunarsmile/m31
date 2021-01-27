package m31.util;

import java.util.Objects;

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
   * Converts an unsigned integer to big-endian (network byte order) byte array.
   *
   * <p>The new array is a 4-byte array represents the given {@code num}</p>
   *
   * <pre>
   *   Bytes.fromInt(0x79347F50) = [(byte) 0x79, (byte) 0x34, (byte) 0x7F, (byte) 0x50]
   * </pre>
   *
   * @param num The unsigned integer in host byte order
   * @return The network byte order byte array of {@code num}
   *
   * @see #toInt(byte[])
   * @see <a href="https://en.wikipedia.org/wiki/Endianness">Endianness</a>
   */
  public static byte[] fromInt(int num) {
    return new byte[] {
        (byte) (num >>> 24),
        (byte) (num >>> 16),
        (byte) (num >>> 8),
        (byte) (num)
    };
  }

  /**
   * Converts a byte buffer, ordered in big-endian (network byte order) to an integer
   *
   * <p>This method invokes {@link #toInt(byte[], int)} internally.</p>
   *
   * <pre>
   *   Bytes.toInt(null) will throws NullPointerException
   *   Bytes.toInt([(byte) 0x01, (byte) 0x02]) will throw IllegalArgumentException
   *
   *   Bytes.toInt([(byte) 0x79, (byte) 0x34, (byte) 0x7F, (byte) 0x50]) = 0x79347F50
   * </pre>
   *
   * @param bytes the byte buffer contains 4 bytes of integer data, in big-endian order.
   * @return The integer represents the given byte buffer, if the parameter - {@code bytes}
   *         is {@code null}, {@link NullPointerException} will be thrown.
   *
   * @see Bytes#fromInt(int)
   * @see Bytes#toInt(byte[], int)
   * @see <a href="https://en.wikipedia.org/wiki/Endianness">Endianness</a>
   */
  public static int toInt(byte[] bytes) {
    return toInt(bytes, 0);
  }

  /**
   * Converts a byte buffer, at a given range and ordered in big-endian (network byte order)
   * to an integer.
   *
   * <p>Starting from {@code offset}, if {@code bytes} has more than 4 bytes, only the first
   * 4 bytes will be used for conversion, if it has less than that,
   * {@link IllegalArgumentException} will be thrown.</p>
   *
   * <pre>
   *   Bytes.toInt([(byte) 0x79,
   *                (byte) 0x12,
   *                (byte) 0x34,
   *                (byte) 0x56,
   *                (byte) 0x78], 1) = 0x12345678
   * </pre>
   *
   * @param bytes   the byte buffer contains 4 bytes of integer data, in big-endian order.
   * @param offset  the offset index in {@code bytes}
   * @return The integer represents the given byte buffer, if the parameter - {@code bytes}
   *         is {@code null}, {@link NullPointerException} will be thrown.
   *
   * @see Bytes#toInt(byte[])
   * @see <a href="https://en.wikipedia.org/wiki/Endianness">Endianness</a>
   */
  @SuppressWarnings({"PointlessArithmeticExpression", "PointlessBitwiseExpression"})
  public static int toInt(byte[] bytes, int offset) {
    Objects.requireNonNull(bytes, "Parameter - bytes, cannot be null");

    if (offset + 4 > bytes.length) {
      throw new IllegalArgumentException("Not enough data to convert to an unsigned integer," +
          " required: 4, actual: " + (bytes.length - offset - 1));
    }

    return (bytes[offset + 0] & 0xFF) << 24 |
           (bytes[offset + 1] & 0xFF) << 16 |
           (bytes[offset + 2] & 0xFF) << 8  |
           (bytes[offset + 3] & 0xFF) << 0;
  }

  /** Private constructor to prevent this class from being explicitly instantiated */
  private Bytes() {}
}
