package m31.common.transport.handler;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public interface IdExHandler {

  /**
   * The maximum length of the string is 255 characters, including the Carriage Return and Line Feed.
   */
  int MAX_IDENTIFICATION_LINE_LENGTH = 255;

  /**
   * Get the remote peer's identification.
   *
   * <p>
   *   This method parse the content of {@link ByteBuf} parameter to see if it match the
   *   identification patterns, which:
   *   <ul>
   *     <li>either starts with "C-" or "S-"</li>
   *     <li>ends with "\r\n"</li>
   *   </ul>
   * </p>
   * <p>
   *   If the buffer contains multiple lines - string holds one or multiple "\r\n", only the first
   *   line that matches the patterns above would be returned, other lines would be ignored.
   * </p>
   * <p>
   *   This method is intended to traversal each character inside the buffer at most once, any more
   *   effort spent more than that should be redundant.
   * </p>
   *
   * @param buf The {@link ByteBuf} buffer where the ID to be parsed from
   * @return The client or server ID as a string
   */
  static String parseId(ByteBuf buf) {
    Objects.requireNonNull(buf, "Parameter cannot be null");

    int rIdx = buf.readerIndex();
    int wIdx = buf.writerIndex();
    if (rIdx == wIdx) {
      return null;
    }

    int line = 1, pos = 0;
    boolean needLf = false;
    boolean validLine = false;

    byte[] data = new byte[MAX_IDENTIFICATION_LINE_LENGTH];

    rIdx--;
    while (rIdx++ < wIdx) {
      byte b = buf.getByte(rIdx);

      if (b == '\0') {
        throw new IllegalStateException("Illegal identification - null character found at" +
            " line #" + line + " character #" + (pos + 1));
      }

      if (b == '\r') {
        needLf = true;

        continue;
      }

      if (b == '\n') {
        line++;

        if (validLine) {
          buf.readerIndex(rIdx + 1);
          buf.discardReadBytes();

          return new String(data, 0, pos, StandardCharsets.UTF_8);
        }

        pos = 0;
        needLf = false;

        continue;
      }

      if (needLf) {
        throw new IllegalStateException("Illegal identification - invalid line ending at" +
            " line #" + line + " character #" + pos + 1);
      }

      if (pos > data.length) {
        throw new IllegalStateException("Illegal identification - line too long at line #" + line +
            " character #" + pos + 1);
      }

      if (pos < 2) {
        data[pos++] = b;
      } else if ((data[0] == 'C' && data[1] == '-') || (data[0] == 'S' && data[1] == '-')) {
        validLine = true;
        data[pos++] = b;
      }
    }

    return null;
  }
}
