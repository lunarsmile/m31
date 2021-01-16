package m31;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import m31.server.ServerReqHandler;

import java.io.Closeable;

public class M31d implements Closeable {

  private final EventLoopGroup boss   = new NioEventLoopGroup(1);
  private final EventLoopGroup worker = new NioEventLoopGroup();

  public void open(int port) {
    ServerBootstrap sbs = new ServerBootstrap();
    LoggingHandler loggingHandler = new LoggingHandler(LogLevel.TRACE);

    sbs.group(boss, worker)
       .channel(NioServerSocketChannel.class)
       .handler(loggingHandler)
       .childHandler(new ChannelInitializer<SocketChannel>() {
         @Override
         protected void initChannel(SocketChannel ch) {
           ch.pipeline().addLast(loggingHandler, new ServerReqHandler());
         }
       }).bind(port);
  }

  @Override
  public void close() {
    boss.shutdownGracefully();
    worker.shutdownGracefully();
  }
}
