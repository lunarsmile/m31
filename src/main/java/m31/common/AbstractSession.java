package m31.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import m31.arch.M31Constant;
import m31.arch.M31Message;

public abstract class AbstractSession implements Session {

  private static final int DEFAULT_BUFFER_SIZE = 256;

  private final Channel channel;

  public AbstractSession(Channel channel) {
    this.channel = channel;

    sessions.add(this);
  }

  /**
   * Creates a {@link ByteBuf} object - netty data container, the length is 256 bytes.
   *
   * <p>Internally, it calls the {@link #createBuffer(int)} with parameter 256 bytes.</p>
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

  /**
   * Create a {@link ByteBuf} object to represent a M31 message
   *
   * <p>Internally, this method invokes the {@link #createBuffer()} to help create the message
   * buffer, then write the {@code msgId} at position index 5.</p>
   *
   * @return a new {@link ByteBuf} object contains the message ID
   */
  protected ByteBuf createMessage(byte msgId) {
    ByteBuf msg = createBuffer();

    msg.writerIndex(M31Constant.M31_PACKET_HEADER_LENGTH);
    msg.readerIndex(M31Constant.M31_PACKET_HEADER_LENGTH);
    msg.writeByte(msgId);

    return msg;
  }

  /**
   * Sends the key exchange initialization packet to the other side.
   *
   * <p>Key exchange begins by each side sending the following packet:</p>
   * <pre>
   *       byte         M31_MSG_KEXINIT
   *       byte[16]     cookie (random bytes)
   *       name-list    kex_algorithms
   *       name-list    server_host_key_algorithms
   *       name-list    encryption_algorithms_client_to_server
   *       name-list    encryption_algorithms_server_to_client
   *       name-list    mac_algorithms_client_to_server
   *       name-list    mac_algorithms_server_to_client
   *       name-list    compression_algorithms_client_to_server
   *       name-list    compression_algorithms_server_to_client
   *       boolean      first_kex_packet_follows
   *       uint32       0 (reserved for future extension)
   * </pre>
   *
   * @param payload the byte buffer contains the key exchange initialization packet
   */
  public void sendKexInit(byte[] payload) {
    ByteBuf msg = createMessage(M31Message.M31_MSG_KEXINIT);

    msg.writeBytes(payload);

    channel.writeAndFlush(msg);
  }

}
