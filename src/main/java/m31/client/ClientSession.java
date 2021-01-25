package m31.client;

import m31.common.Session;

public class ClientSession implements Session {

  private static final String clientId = "C-0.1";
  private String serverId;

  public ClientSession() {
    sessions.add(this);
  }

  public String getClientId() {
    return clientId;
  }

  public String getServerId() {
    return serverId;
  }

  public void setServerId(String serverId) {
    this.serverId = serverId;
  }
}
