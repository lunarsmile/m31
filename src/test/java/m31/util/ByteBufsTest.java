package m31.util;

import io.netty.buffer.Unpooled;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ByteBufsTest {

  private final Charset utf8 = StandardCharsets.UTF_8;

  @Test
  public void testGetIdSingleLine() {
    String id = "C-2.0-softwareversion\r\n";
    String actual = ByteBufs.parseId(Unpooled.wrappedBuffer(id.getBytes(utf8)));
    assertEquals("C-2.0-softwareversion", actual);

    id = "S-2.0-softwareversion\r\n";
    actual = ByteBufs.parseId(Unpooled.wrappedBuffer(id.getBytes(utf8)));
    assertEquals("S-2.0-softwareversion", actual);
  }

  @Test
  public void testGetIdSingleLineWithoutCR() {
    String id = "C-2.0-softwareversion\n";
    String actual = ByteBufs.parseId(Unpooled.wrappedBuffer(id.getBytes(utf8)));
    assertEquals("C-2.0-softwareversion", actual);

    id = "S-2.0-softwareversion\n";
    actual = ByteBufs.parseId(Unpooled.wrappedBuffer(id.getBytes(utf8)));
    assertEquals("S-2.0-softwareversion", actual);
  }

  @Test
  public void testGetIdContainsNullCharacter() {
    String id = "C-2.0-so" + '\0' + "ftwareversion\r\n";

    IllegalStateException thrown =
        assertThrows(IllegalStateException.class, () -> {
          ByteBufs.parseId(Unpooled.wrappedBuffer(id.getBytes(utf8)));
        });

    assertNotNull(thrown.getMessage());
    assertTrue(thrown.getMessage().contains("character #" + (id.indexOf('\0') + 1)));
  }

  @Test
  public void testGetIdMultipleLines() {
    String id = "1st line\r\nC-2.0-softwareversion\r\n";
    String actual = ByteBufs.parseId(Unpooled.wrappedBuffer(id.getBytes(utf8)));
    assertEquals("C-2.0-softwareversion", actual);

    id = "1st line\r\nS-2.0-softwareversion\r\n";
    actual = ByteBufs.parseId(Unpooled.wrappedBuffer(id.getBytes(utf8)));
    assertEquals("S-2.0-softwareversion", actual);
  }

}