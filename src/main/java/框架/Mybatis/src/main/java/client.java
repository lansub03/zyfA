import domain.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class client {
    //mybatis的原始用法
    @Test
     public void testMabtis() throws IOException {
        //1、加载myabtis配置文件 读取myabtis配置文件
        InputStream resourceAsStream = Resources.getResourceAsStream("SqlMapConfig.xml");

        //2、使用sqlSessionFactoryBuild来创建一个sqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);

        //3、获取到sql session  进行调取api
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //4、根据mapper文件的路径进行调取api
        User user = sqlSession.selectOne("UserTest.selectByid", 1);
        System.out.println("info:"+user);
     }

     //使用接口的方式调用myabtis


}
