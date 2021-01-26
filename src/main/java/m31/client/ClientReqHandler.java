package m31.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import m31.common.Session;
import m31.common.transport.handler.IdExHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class ClientReqHandler extends ChannelInboundHandlerAdapter {

  private static final Logger logger = LoggerFactory.getLogger(ClientReqHandler.class);

  private ClientSession session;

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    session = new ClientSession(ctx.channel());
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    ByteBuf clientId =
        Unpooled.wrappedBuffer((session.getClientId() + "\r\n").getBytes(StandardCharsets.UTF_8));
    ctx.writeAndFlush(clientId);
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    String serverId = session.getServerId();
    if (serverId == null) {
      serverId = IdExHandler.parseId((ByteBuf) msg);
      if (serverId == null) {
        return;
      }

      logger.debug("[{}] Received identification: {}", session, serverId);

      session.setServerId(serverId);

      Attribute<CompletableFuture<Session>> attr = ctx.channel().attr(Session.M31_CONNECT_FUTURE);

      CompletableFuture<Session> connectResult = attr.get();
      connectResult.complete(session);
    }
  }
}
