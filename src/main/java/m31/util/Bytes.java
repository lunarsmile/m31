package m31.util;

/**
 * Utility code for dealing with byte array operations.
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
}
