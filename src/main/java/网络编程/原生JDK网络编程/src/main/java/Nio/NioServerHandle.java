package Nio;

        import config.UtilsNumber;

        import java.io.IOException;
        import java.net.InetSocketAddress;
        import java.nio.ByteBuffer;
        import java.nio.channels.SelectionKey;
        import java.nio.channels.Selector;
        import java.nio.channels.ServerSocketChannel;
        import java.nio.channels.SocketChannel;
        import java.util.Iterator;
        import java.util.Set;

/**
 * 类说明：nio通信服务端服务器
 */
public class NioServerHandle implements Runnable {
    private Selector selector;//选择器
    private ServerSocketChannel serverChannel;
    //todo
    private volatile boolean started;

    /**
     * @param port:指定要监听的端口
     */
    public NioServerHandle(Integer port) {
        try {
            selector = Selector.open();//创建选择器
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);//true为阻塞模式，false为非阻塞模式
            serverChannel.socket().bind(new InetSocketAddress(UtilsNumber.SERVER_PORT));//选择监听的端口
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            started = true;
            System.out.println("服务器已启动，端口号："+UtilsNumber.SERVER_PORT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void stop() {
        started=false;
    }

    @Override
    public void run() {
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
                    } catch (Exception e) {
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
    }

    public void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()){
            //处理新接入的请求消息
            if (key.isAcceptable()){
                ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
                SocketChannel sc = ssc.accept();
                System.out.println("socket channel 建立连接");
                sc.configureBlocking(false);//设置为非阻塞模式
                sc.register(selector,SelectionKey.OP_READ);
            }
            //读消息
            if (key.isReadable()){
                System.out.println("-----------socket channel数据准备完成");
            }
            SocketChannel sc = (SocketChannel)key.channel();
            //创建ByteBuffer，并开辟一个1M的缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            //读取请求码流，返回读取到的字节数
            int readBytes = sc.read(buffer);
            //d读取到字节，对字节进行编解码
            if (readBytes > 0){
                //将缓冲区当前的limit设置为position=0
                //用于后修对缓冲区的读取操作
                buffer.flip();
                //根据缓冲区可读字节数创建字节数组
                byte[] bytes = new byte[buffer.remaining()];
                //将缓冲区可读字节数复制到新建的数组中
                buffer.get(bytes);
                String message = new String(bytes,"UTF-8");
                System.out.println("服务器接受到消息："+message);
                //处理消息
                String result = UtilsNumber.responseCode(message);
                //发送应答消息
                doWrite(sc,result);
            }
            //链路已经关闭，释放资源
            else if (readBytes < 0){
                key.cancel();;
                sc.close();
            }
        }
    }
    //发送消息
    private void doWrite(SocketChannel channel,String request) throws IOException {
        //将消息编码为字节数组
        byte[] bytes = request.getBytes();
        //根据数组容量创建ByteBuffer
        ByteBuffer wriBuffer = ByteBuffer.allocate(bytes.length);
        //将字节数字添加到缓冲区
        wriBuffer.put(bytes);
        //转换模式
        wriBuffer.flip();
        //发送缓冲区的字节数组
        channel.write(wriBuffer);
    }
}
