package m31.common;

import io.netty.util.AttributeKey;

import java.io.Closeable;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArraySet;

public interface Session extends Closeable {

  AttributeKey<CompletableFuture<Session>> M31_CONNECT_FUTURE =
      AttributeKey.valueOf(Session.class.getName());

  Set<Session> sessions = new CopyOnWriteArraySet<>();

  @Override
  default void close() throws IOException {
    sessions.remove(this);
  }

  String getClientId();

  String getServerId();
}
