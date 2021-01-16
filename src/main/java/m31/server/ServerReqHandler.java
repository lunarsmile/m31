package m31.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerReqHandler extends ChannelInboundHandlerAdapter {

  private ServerSession session;

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    session = new ServerSession(ctx.channel());
  }

}
