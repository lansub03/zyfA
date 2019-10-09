package src.test.java.com.shiro.admin.shiro_admin;

import com.shiro.admin.shiro_admin.utils.ShiroPassUtil;
import org.junit.Test;

public class PassTest {
    private static String pass = "123456789";

    @Test
    public void passTest(){
        System.out.println("加密之后的密码："+ ShiroPassUtil.mad5Pass(pass));
    }
}
