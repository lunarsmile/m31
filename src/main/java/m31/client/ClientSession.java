package m31.client;

import io.netty.channel.Channel;
import m31.common.AbstractSession;

public class ClientSession extends AbstractSession {

  private static final String clientId = "C-0.1";
  private String serverId;

  public ClientSession(Channel channel) {
    super(channel);
  }

  @Override
  public String getClientId() {
    return clientId;
  }

  @Override
  public String getServerId() {
    return serverId;
  }

  public void setServerId(String serverId) {
    this.serverId = serverId;
  }
}
