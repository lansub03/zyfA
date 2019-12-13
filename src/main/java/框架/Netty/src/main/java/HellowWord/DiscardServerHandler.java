package HellowWord;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * ChannelInboundHandlerAdapter类提供了可以覆盖各种事件的方法，我们只需要继承这个类，并覆盖其中的方法即可
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 覆盖事件处理的方法，每当从客户端接收到新数据时，都会调用此方法
     * @param ctx
     * @param msg
     */
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
/**
 *          //接受什么消息就回复什么消息
 ctx.write(msg); // 调用write(Object)逐字写入收到的消息
 ctx.flush(); // 数据冲洗
 *         /
 */
        try {
            System.out.println("从telent接受到的内容为：" + "\n"+"");
            while (in.isReadable()) {
                System.out.print((char) in.readByte());
                System.out.flush();
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
    /**
     * 处理事件引发的异常，大多数情况下，应该记录捕获的异常并在此关闭其关联的同道
     * @param ctx
     * @param cause
     */
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 当出现异常关闭连接
        cause.printStackTrace();
        ctx.close();
    }
//    Netty ByteBuf 转 String字符串
    public String convertByteBufToString(ByteBuf buf) {
        String str;
        if(buf.hasArray()) { // 处理堆缓冲区
            str = new String(buf.array(), buf.arrayOffset() + buf.readerIndex(), buf.readableBytes());
        } else { // 处理直接缓冲区以及复合缓冲区
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(), bytes);
            str = new String(bytes, 0, buf.readableBytes());
        }
        return str;
    }
}