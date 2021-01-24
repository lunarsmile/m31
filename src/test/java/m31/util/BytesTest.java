package m31.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class BytesTest {

  @Test
  public void testConcat_whenAllParametersAreNull() {
    assertNull(Bytes.concat(null));
    assertNull(Bytes.concat(null, null));
  }

  @Test
  public void testConcat_whenAllParametersAreNotNull() {
    byte[] a = {(byte) 0x01, (byte) 0x02, (byte) 0x06};
    byte[] b = {(byte) 0x05, (byte) 0x08};

    assertArrayEquals(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x06, (byte) 0x05, (byte) 0x08},
        Bytes.concat(a, b));
  }

  @Test
  public void testConcat_whenOneOfArgumentIsNull() {
    byte[] a = {(byte) 0x08, (byte) 0x02};
    byte[] b = {(byte) 0x11};

    assertArrayEquals(new byte[]{(byte) 0x08, (byte) 0x02, (byte) 0x011}, Bytes.concat(a, null, b));
  }

  @Test
  public void testToBytes_shouldReturnsInBigEndian() {
    byte[] expected = {(byte) 0x79, (byte) 0x34, (byte) 0x7F, (byte) 0x50};

    assertArrayEquals(expected, Bytes.toBytes(0x79347F50));
  }

}