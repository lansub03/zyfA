package Aio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * 类说明：影响网络操作的处理器
 */
public class AioServerHandler implements Runnable {
    public CountDownLatch latch;
    //进行异步通信的通道
    public AsynchronousServerSocketChannel channel;

    public AioServerHandler( Integer port) {
        try {
            channel = AsynchronousServerSocketChannel.open();
            channel.bind(new InetSocketAddress(port));//绑定端口
            System.out.println("Server is start,port:"+port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);
        //用于接受客户端的连接
        channel.accept(this,new AioAcceptHandler());
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
