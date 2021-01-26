package m31.server;

import io.netty.channel.Channel;
import m31.common.AbstractSession;

public class ServerSession extends AbstractSession {

  private static final String serverId = "S-0.1";
  private String clientId;

  public ServerSession(Channel channel) {
    super(channel);
  }

  @Override
  public String getServerId() {
    return serverId;
  }

  @Override
  public String getClientId() {
    return this.clientId;
  }

  public void setClientId(String id) {
    this.clientId = id;
  }
}
