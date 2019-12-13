package HellowWord;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class DiscardServer {

    private int port;

    public DiscardServer(int port) {
        this.port = port;
    }

    public void run() throws Exception{
        // NioEventLoopGroup是一个处理I / O操作的多线程事件循环
        // 使用了多少个线程以及它们如何映射到创建的Channels取决于EventLoopGroup实现，甚至可以通过构造函数进行配置
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            // 设置服务器的帮助程序类
            ServerBootstrap b = new ServerBootstrap();
            b.group(boosGroup,workerGroup) // 将创建好的两个group添加到serverBootStrap中
                    .channel(NioServerSocketChannel.class) // 指定使用 NioServerSocketChannel 来接受传入连接
                    .childHandler(new ChannelInitializer<SocketChannel>() { // childHandler指定处理程序，ChannelInitializer旨在帮助用户配置新的channel
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new DiscardServerHandler()); // 将我们定义好的DiscardServerHandler丢在管道中
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,128) // 这里可以设置特定的channel参数，为了接受传入的连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // 追加操作项，因为我们正在编写TCP/IP服务器，因此我们可以设置套接字选项，如tcpNoDelay和keepAlive

            // 绑定端口号并接收传入的连接
            ChannelFuture f = b.bind(port).sync();
            // 关闭服务器
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            boosGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("你给我起来！...localhost:8080");
        int port = 8080;
        if(args.length > 0 ){
            port = Integer.parseInt(args[0]);
        }

        new DiscardServer(port).run();
    }
}