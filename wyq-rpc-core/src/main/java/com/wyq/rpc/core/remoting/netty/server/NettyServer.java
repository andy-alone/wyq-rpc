package com.wyq.rpc.core.remoting.netty.server;

import com.wyq.rpc.core.util.CustomThreadFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @Author wyq
 * @Date 2022-06-23 0:16
 */
@Slf4j
public class NettyServer {

    Supplier<Integer> cpu = () -> Runtime.getRuntime().availableProcessors();
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;
    private final DefaultEventExecutorGroup serviceHandleGroup;
    {
        workerGroup = new NioEventLoopGroup();
        bossGroup = new NioEventLoopGroup(1);
        serviceHandleGroup = new DefaultEventExecutorGroup(cpu.get() * 2, new CustomThreadFactory());
    }

    public static void main(String[] args) {
        new NettyServer().start();
    }

    @SneakyThrows
    public void start() {
        String host = InetAddress.getLocalHost().getHostAddress();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // TCP默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，减少网络传输。TCP_NODELAY 参数的作用就是控制是否启用 Nagle 算法。
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    // 是否开启 TCP 底层心跳机制
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //表示系统用于临时存放已完成三次握手的请求的队列的最大长度,如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new HeartBeatHandler(0,5,0, TimeUnit.SECONDS));
                            pipeline.addLast(new ChannelInboundHandlerAdapter(){
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    log.info(msg.toString());
                                    ByteBuf byteBuf = ctx.alloc().buffer().writeBytes("server".getBytes());
                                    ctx.writeAndFlush(byteBuf);
                                    super.channelRead(ctx, msg);
                                }
                            });
                        }
                    });
            // 绑定端口，同步等待绑定成功
            ChannelFuture channelFuture = bootstrap.bind(9999).sync();
            log.info("server start...");
            // 等待服务端监听端口关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("异常：", e);
        } finally {
            log.info("关闭资源");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            serviceHandleGroup.shutdownGracefully();
        }
    }

}
