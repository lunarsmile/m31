package m31;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

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
          ch.pipeline().addLast(new LoggingHandler(LogLevel.TRACE));
        }
      });
  }

  public void stop() {
    if (worker != null) {
      worker.shutdownGracefully();
    }
  }
}
