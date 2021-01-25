package m31.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import m31.common.transport.handler.IdExHandler;
import m31.common.transport.kex.KexScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class ServerReqHandler extends ChannelInboundHandlerAdapter implements IdExHandler {

  private final static Logger logger = LoggerFactory.getLogger(ServerReqHandler.class);

  private ServerSession session;

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    session = new ServerSession(ctx.channel());
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    String clientId = session.getClientId();
    if (clientId == null) {
      clientId = IdExHandler.getId((ByteBuf) msg);
      if (clientId == null) {
        return;
      }

      logger.debug("[{}] Received identification: {}", session, clientId);

      session.setClientId(clientId);

      ByteBuf serverId =
          Unpooled.wrappedBuffer((session.getServerId() + "\r\n").getBytes(StandardCharsets.UTF_8));
      ctx.channel().write(serverId);

      byte[] allSchemes = KexScheme.toBytes();
      session.sendKexInit(allSchemes);
    }

    ReferenceCountUtil.release(msg);
  }
}
