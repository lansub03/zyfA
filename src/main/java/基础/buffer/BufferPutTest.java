package 基础.buffer;

import java.nio.ByteBuffer;

public class BufferPutTest {
    public static void main(String[] args) {
        System.out.println("----------Test put-------");
        ByteBuffer bb = ByteBuffer.allocate(32);
        //索引变化
        System.out.println("before  put(byte)"+bb.put((byte)'z'));
        //索引不变化
        System.out.println("after put(2 , (byte) 'z')"+bb.put(2,(byte)'z') );
        System.out.println("info:"+new String(bb.array()));

        System.out.println("after put (buffer)" + bb);
        System.out.println(new String(bb.array()));

        System.out.println("---------------Test reset ------------------------");
        ByteBuffer buffer = ByteBuffer.allocate(20);
        buffer.put((byte) 1).put((byte) 1).put((byte) 1).put((byte) 1);
        System.out.println("buffer = "+buffer);
        buffer.mark().reset();
        System.out.println("before reset:"+buffer);
        //todo
    }
}
