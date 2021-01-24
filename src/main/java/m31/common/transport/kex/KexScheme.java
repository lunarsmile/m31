package m31.common.transport.kex;

import m31.arch.M31Constant;
import m31.common.NamedObject;
import m31.util.Bytes;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.EnumSet;

public enum KexScheme implements NamedObject {

  KEX             ("KEX",             "ecdh-sha2-nistp521"),
  SERVER_HOST_KEY ("Server Host Key", "ssh-rsa"),
  ENCRYPTION_C2S  ("Encryption C2S",  "aes256-cbc,aes256-ctr"),
  ENCRYPTION_S2C  ("Encryption S2C",  "aes256-cbc,aes256-ctr"),
  MAC_C2S         ("MAC C2S",         "hmac-sha1"),
  MAC_S2C         ("MAC S2C",         "hmac-sha1"),
  COMPRESSION_C2S ("Compression C2S", "none,zlib@openssh.com"),
  COMPRESSION_S2C ("Compression S2C", "none,zlib@openssh.com");

  private static final byte[] FFP = new byte[]{0};                // first factory packet follows
  private static final byte[] RESERVED = new byte[] {0, 0, 0, 0}; // reserved (FFU)

  private final String name;
  private final String scheme;

  KexScheme(String name, String scheme) {
    this.name = name;
    this.scheme = scheme;
  }

  @Override
  public String getName() {
    return name;
  }

  public String getScheme() {
    return scheme;
  }

  /**
   * Construct the key exchange initialization packet.
   */
  public static byte[] getBytes() {
    SecureRandom rand = new SecureRandom();

    byte[] cookie = new byte[M31Constant.MSG_KEX_COOKIE_SIZE];
    rand.nextBytes(cookie);

    byte[] joined = cookie;
    for (KexScheme ks : EnumSet.allOf(KexScheme.class)) {
      String scheme = ks.getScheme();

      byte[] payload = scheme.getBytes(StandardCharsets.UTF_8);

      byte[] header = new byte[4];
      header[0] = (byte) (payload.length >>> 24);
      header[1] = (byte) (payload.length >>> 16);
      header[2] = (byte) (payload.length >>> 8);
      header[3] = (byte) (payload.length);

      joined = Bytes.concat(joined, header, payload);
    }

    return Bytes.concat(joined, FFP, RESERVED);
  }
}
