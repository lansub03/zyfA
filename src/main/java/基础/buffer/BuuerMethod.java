package 基础.buffer;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class BuuerMethod {
    public static void main(String[] args) {
        System.out.println("Test get-----------");
        ByteBuffer buffer = ByteBuffer.allocate(32);
        buffer
                .put((byte)1)//下表0
                .put((byte)2)//下标1
                .put((byte)3)//下标2
                .put((byte)4)//下标3
                .put((byte)5)
                .put((byte)6);
        /**
         * 在没有filp的时候，pos=6 lim=32 cap =32
         */
        System.out.println("before flip()"+buffer);
        //转行为读取模式
        buffer.flip();
         //* filp（）之后 pos = 0 lim=6 cap=32
        System.out.println("before get():"+buffer);
        System.out.println("result info:"+(byte)buffer.get());//相对取，导致索引发生变化
        //--------------------------------------------------------------------------------------------------------------------------------------------------
        System.out.println("after get():"+buffer);
        System.out.println("result info:"+(byte)buffer.get(2));//绝对取，索引不会变化
        //--------------------------------------------------------------------------------------------------------------------------------------------------
        System.out.println("after get(index):"+buffer);
        byte[] dst = new byte[10];
        //这个也会使下标发生变化，把0到2的元素添加到dst数组中
        buffer.get(dst,0,2);
        System.out.println("after get(dst , 0 , 2):"+buffer);
        System.out.println("dst:"+ Arrays.toString(dst));

    }
}
