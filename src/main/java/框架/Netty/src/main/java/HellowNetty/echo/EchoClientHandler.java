package HellowNetty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    /**
     * 客户端读取到数据以后，触发此方法
     * @param channelHandlerContext
     * @param msg : 数据存放的地方
     * @throws Exception
     */
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf msg) throws Exception {
        System.out.println("Client accetp:"+msg.toString(CharsetUtil.UTF_8));
    }

    /**
     * 客户端端被通知channer活跃以后，或者说通道建立之后，触发此函数
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //往服务器写数据
        ctx.writeAndFlush(Unpooled.copiedBuffer("hellow Netty",CharsetUtil.UTF_8));
    }

    /**
     * 异常梳理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
