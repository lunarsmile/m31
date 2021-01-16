package m31.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import m31.common.Session;

public class ServerSession implements Session {

  private static final int DEFAULT_BUFFER_SIZE = 256;

  private final Channel channel;

  public ServerSession(Channel channel) {
    this.channel = channel;
  }

  /**
   * Creates a {@link ByteBuf} object - netty data container, the length is 256 bytes.
   *
   * <p>
   *   Internally, it calls the {@link #createBuffer(int)} with parameter 256 bytes.
   * </p>
   *
   * @return a newly created {@link ByteBuf} object
   *
   * @see #createBuffer(int)
   */
  public ByteBuf createBuffer() {
    return createBuffer(DEFAULT_BUFFER_SIZE);
  }

  /**
   * Creates a {@link ByteBuf} object - netty data container, with the given size.
   *
   * @param size size of the buffer to create
   * @return a newly created {@link ByteBuf} object
   *
   * @see #createBuffer()
   */
  public ByteBuf createBuffer(int size) {
    return channel.alloc().buffer(size);
  }
}
