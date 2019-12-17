package Aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class AioClientWriteHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel clientChannel;
    private CountDownLatch latch;

    public AioClientWriteHandler(AsynchronousSocketChannel clientChannel, CountDownLatch latch) {
        this.clientChannel = clientChannel;
        this.latch = latch;
    }


    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        if (buffer.hasRemaining()){
            clientChannel.write(buffer,buffer,this);
        }else {
            //读取服务端传回的数据
            ByteBuffer readBuff = ByteBuffer.allocate(1024);
            //异步读
            clientChannel.read(readBuff,readBuff,new AioChientReadHandler(clientChannel,latch));
        }
    }
    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        System.out.println("数据发送失败");
        try {
            clientChannel.close();
            latch.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
