package m31.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import m31.arch.M31Constant;
import m31.arch.M31Message;
import m31.common.Session;

public class ServerSession implements Session {

  private static final int DEFAULT_BUFFER_SIZE = 256;

  private static final String serverId = "S-0.1";
  private String clientId;

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

  /**
   * Create a {@link ByteBuf} object to represent a M31 message
   */
  protected ByteBuf createMessage(byte msgId) {
    ByteBuf msg = createBuffer();

    msg.writerIndex(M31Constant.M31_PACKET_HEADER_LENGTH);
    msg.readerIndex(M31Constant.M31_PACKET_HEADER_LENGTH);
    msg.writeByte(msgId);

    return msg;
  }

  public String getClientId() {
    return this.clientId;
  }

  public void setClientId(String id) {
    this.clientId = id;
  }

  public String getServerId() {
    return serverId;
  }

  public void sendKexInit(byte[] payload) {
    ByteBuf msg = createMessage(M31Message.M31_MSG_KEXINIT);

    msg.writeBytes(payload);

    channel.writeAndFlush(msg);
  }
}
