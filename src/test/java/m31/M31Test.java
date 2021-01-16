package m31;

import m31.common.Session;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class M31Test {

  @Test
  public void testNormalConnection() {
    try(M31d m31d = new M31d()) {
      m31d.open(12345);

      M31 client = new M31();
      client.start();
      try (Session session = client.connect("127.0.0.1", 12345).get(1, TimeUnit.SECONDS)) {
        assertNotNull(session);
      } catch (Exception e) {
        fail("Unable to connect");
      }
      client.stop();
    }
  }
}