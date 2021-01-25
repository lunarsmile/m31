package m31.arch;

public final class M31Constant {

  public static final int M31_PACKET_LENGTH        = 4; // a 32-bit of integer
  public static final int M31_PADDING_LENGTH       = 1; // a 8-bit of byte
  public static final int M31_PACKET_HEADER_LENGTH = M31_PACKET_LENGTH + M31_PADDING_LENGTH;
  public static final int MSG_KEX_COOKIE_SIZE      = 16;
}
