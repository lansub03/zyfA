package Nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioClientHandle implements Runnable {

    private String host;
    private Integer port;
    private Selector selector;//选择器
    private SocketChannel socketChannel;
    private volatile boolean started;

    public NioClientHandle(String host, Integer port) {
        this.host = host;
        this.port = port;
        try {
            selector = Selector.open();//创建选择器
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);//true为阻塞模式，false为非阻塞模式
            started = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void stop() {
        started = false;
    }

    @Override
    public void run() {
        try {
            doConnect();//发起连接
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        while (started){
            try {
                selector.select();//运行状态下不断轮询
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                SelectionKey key = null;
                while (it.hasNext()){
                    key = it.next();
                    it.remove();
                    try {
                        handleInput(key);
                    } catch (IOException e) {
                        if (key != null){
                            key.cancel();
                            if (key.channel() != null){
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (selector != null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void handleInput(SelectionKey key) throws IOException {
        //先检测key是否是有效的
        if (key.isValid()){
           SocketChannel sc =  (SocketChannel)key.channel();
           if (key.isConnectable()){
               if (sc.finishConnect()){
               }else {System.exit(1);}
           }
           if (key.isReadable()){
               ByteBuffer buffer = ByteBuffer.allocate(1024);
               int readBytes = sc.read(buffer);
               if (readBytes>0){
                   buffer.flip();//都换为写
                   byte[] bytes =  new byte[buffer.remaining()];
                   buffer.get(bytes);
                   String result = new String(bytes,"UTF-8");
                   System.out.println("'accept message:"+result);
               }else if (readBytes <0){
                   key.cancel();
                   sc.close();
               }
           }
        }
    }

    //发送消息
    private void doWrite(SocketChannel channel,String request) throws IOException {
        byte[] bytes = request.getBytes();//将消息编码为字节数组
        ByteBuffer wriBuffer = ByteBuffer.allocate(bytes.length);//根据数组容量创建ByteBuffer
        wriBuffer.put(bytes);//将字节数字添加到缓冲区
        wriBuffer.flip();//转换模式
        channel.write(wriBuffer);//发送缓冲区的字节数组
    }

    private void doConnect() throws IOException {
        if (socketChannel.connect(new InetSocketAddress(host,port))){
            //如果连接成功，那么就什么都不做
        }else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    /**
     * @param msg:发送的内容
     * @throws Exception
     */
    public void sendMsg(String msg) throws Exception {
        //todo
        socketChannel.register(selector,SelectionKey.OP_READ);
        doWrite(socketChannel,msg);
    }
}
