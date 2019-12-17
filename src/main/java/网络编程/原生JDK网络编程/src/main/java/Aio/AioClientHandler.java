package Aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * 客户端处理类
 *
 */
public class AioClientHandler implements CompletionHandler<Void,AioClientHandler>,Runnable {

    private AsynchronousSocketChannel clientChannel;//异步操作Socket
    private String host;
    private Integer porot;
    private CountDownLatch latch;//防止应用程序退出

    public AioClientHandler(String host, Integer porot) {
        this.host = host;
        this.porot = porot;
        //打开一个客户端通道
        try {
            clientChannel = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接或处理完成以后，会自动执行 completed() 方法
     */
    @Override
    public void completed(Void result, AioClientHandler attachment) {
        System.out.println("已经连接到客户端");
    }

    /**
     * 连接或处理失败时，会执行failed（）方法
     * @param exc
     * @param attachment
     */
    @Override
    public void failed(Throwable exc, AioClientHandler attachment) {
        System.out.println("连接失败");
        exc.printStackTrace();
        latch.countDown();//退出客户端程序
        try {
            clientChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);
        //指定服务器地址和端口
        clientChannel.connect(new InetSocketAddress(host,porot),this,this);
        try {
            latch.await();
            clientChannel.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //向服务器发送消息
    public void sendMsg(String msg){
        //为了把Msg变成可以在网络上传递的格式
        byte[] bytes = msg.getBytes();//变为字节数组
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);//建立一个缓冲区
        writeBuffer.put(bytes);
        writeBuffer.flip();
        clientChannel.write(writeBuffer,writeBuffer,new AioClientWriteHandler(clientChannel,latch));
    }
}
