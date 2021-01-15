package m31;

import m31.common.Session;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

import static org.junit.Assert.assertNotNull;

public class M31Test {

  @Test
  public void testNormalConnection() throws Exception {
    try (AsynchronousServerSocketChannel srv = AsynchronousServerSocketChannel.open()) {
      srv.bind(new InetSocketAddress("127.0.0.1", 12345));
      srv.accept();

      M31 client = new M31();
      client.start();
      try (Session session = client.connect("127.0.0.1", 12345).get()) {
        assertNotNull(session);
      }
      client.stop();
    }
  }
}