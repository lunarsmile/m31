package m31;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import m31.client.ClientReqHandler;
import m31.common.Session;

import java.util.concurrent.CompletableFuture;

public class M31 {

  private EventLoopGroup worker;
  private Bootstrap bs;

  public void start() {
    worker = new NioEventLoopGroup();

    bs = new Bootstrap();
    bs.group(worker)
      .channel(NioSocketChannel.class)
      .handler(new ChannelInitializer<SocketChannel>() {
        @Override
        protected void initChannel(SocketChannel ch) {
          ch.pipeline().addLast(new LoggingHandler(LogLevel.TRACE), new ClientReqHandler());
        }
      });
  }

  public CompletableFuture<Session> connect(String host, int port) {
    CompletableFuture<Session> result = new CompletableFuture<>();

    ChannelFuture cf = bs.connect(host, port);

    Channel channel = cf.channel();
    channel.attr(Session.M31_CONNECT_FUTURE).set(result);

    cf.addListener(f -> {
      Throwable e = f.cause();
      if (e != null) {
        result.completeExceptionally(e);
      } else if (f.isCancelled()) {
        result.cancel(true);
      }
    });

    return result;
  }

  public void stop() {
    if (worker != null) {
      worker.shutdownGracefully();
    }
  }
}
