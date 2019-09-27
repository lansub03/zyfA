package 基础.Object类;

import lombok.Data;
import org.junit.Test;

@Data
public class ObjectTestStudent {

    @Test
    public void ObjectToString(){

        Student student = new Student();
        String s = student.getClass().getName();
        System.out.println("s:"+s);
    }
}