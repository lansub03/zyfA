package 基础.buffer;

import java.nio.ByteBuffer;

/**
 * 1、写入数据到buffer
 * 2、调用filp：从写模式切换到读模式,更改position和limit两个游标
 * 3、从buffer读数据
 * 4、情况buffer
 */
public class AllocateBuffer {
    public static void main(String[] args) {
        System.out.println("------------测试 分配-------------");
        System.out.println("分配之间堆内存为："+ Runtime.getRuntime().freeMemory());

        //堆上分配（一般推荐堆内存）
        /*
        ByteBuffer buffer = ByteBuffer.allocate(1024000);
        System.out.println("buffer = "+buffer);
        System.out.println("分配之后堆内存为:"+Runtime.getRuntime().freeMemory());
        */

        //用直接内存（比堆内存分配、回收慢）
        ByteBuffer directBuffer = ByteBuffer.allocateDirect(1024000);
        System.out.println("directBuffer = "+directBuffer);
        System.out.println("在直接内存中分配之后，堆内存为："+Runtime.getRuntime().freeMemory());

        System.out.println("-----------------test wrap----------");
        byte[] bytes = new byte[32];
        //todo
//        System.out.println(buffer);
        //todo
//        System.out.println(buffer);
    }
}
