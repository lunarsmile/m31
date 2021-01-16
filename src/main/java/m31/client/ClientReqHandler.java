package m31.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import m31.common.Session;

import java.util.concurrent.CompletableFuture;

public class ClientReqHandler extends ChannelInboundHandlerAdapter {

  private ClientSession session;

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    super.handlerAdded(ctx);

    session = new ClientSession();
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    super.channelActive(ctx);

    Attribute<CompletableFuture<Session>> attr = ctx.channel().attr(Session.SSH_CONNECT_FUTURE);

    CompletableFuture<Session> connectResult = attr.get();
    connectResult.complete(session);
  }
}
