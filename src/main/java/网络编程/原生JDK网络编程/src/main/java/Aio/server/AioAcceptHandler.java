package Aio.server;

import Aio.AioChientReadHandler;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * 类说明：处理用户连接的处理器
 */
public class AioAcceptHandler implements CompletionHandler<AsynchronousSocketChannel,AioServerHandler> {

    @Override
    public void completed(AsynchronousSocketChannel channel, AioServerHandler serverHandler) {
            AioServer.clientCount++;
            System.out.println("连接的客户端数:"+AioServer.clientCount);
            //重新注册监听，让别的客户端也可以连接
            serverHandler.channel.accept(serverHandler,this);
        //异步读
        ByteBuffer readBuff = ByteBuffer.allocate(1024);
        channel.read(readBuff,readBuff,new AioReadHandler(channel));
    }

    @Override
    public void failed(Throwable exc, AioServerHandler serverHandler) {
        exc.printStackTrace();
        serverHandler.latch.countDown();
    }
}
