package m31.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import m31.common.transport.handler.IdExHandler;
import m31.common.transport.kex.KexScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerReqHandler extends ChannelInboundHandlerAdapter implements IdExHandler {

  private final static Logger logger = LoggerFactory.getLogger(ServerReqHandler.class);

  private ServerSession session;

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    session = new ServerSession(ctx.channel());
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    String id = session.getClientId();
    if (id == null) {
      id = IdExHandler.getId((ByteBuf) msg);
      if (id == null) {
        return;
      }

      logger.debug("[{}] Received identification: {}", session, id);
      session.setClientId(id);

      byte[] kexInit = KexScheme.getBytes();
    }

    ReferenceCountUtil.release(msg);
  }
}
