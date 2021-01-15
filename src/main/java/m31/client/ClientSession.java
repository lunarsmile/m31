package m31.client;

import m31.common.Session;

public class ClientSession implements Session {

  public ClientSession() {
    sessions.add(this);
  }
}
