package HellowNetty.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class EchoClient {
    private final int port;//远程端口
    private final String host;//远程主机地址

    /**
     *
     * @param port 远程端口
     * @param host //远程地址
     */
    public EchoClient(int port, String host) {
        this.port = port;
        this.host = host;
    }

    /**
     * 启动客户端方法
     */
    public void start() throws InterruptedException {
        //创建一个线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //客户端启动必备
            Bootstrap b = new Bootstrap();
            //把线程组交给启动类
            b.group(group)
                    //指明使用NIO进行网络通讯
                    .channel(NioSocketChannel.class)
                    //配置远程服务器地址和端口
                    .remoteAddress(new InetSocketAddress(host,port))
                    //设置处理器，处理器必须为ChannelHandler的实例
                    .handler(new EchoClientHandler());
            //加上sync()后，连接到远程节点，阻塞等待直到连接完成
            ChannelFuture f = b.connect().sync();
            //阻塞，直到channel关闭
            f.channel().closeFuture().sync();
        } finally {
            //释放资源，关闭连接
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new EchoClient(9999,"127.0.0.1").start();

    }
}
