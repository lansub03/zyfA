import dao.UserMapper;
import domain.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

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

     //使用接口调用mapper映射文件中的sql
     @Test
     public void testImplMybatis() throws IOException {
         //1、加载myabtis配置文件 读取myabtis配置文件
         InputStream resourceAsStream = Resources.getResourceAsStream("SqlMapConfig.xml");

         //2、使用sqlSessionFactoryBuild来创建一个sqlSessionFactory
         SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);

         //3、获取到sql session  进行调取api
         SqlSession sqlSession = sqlSessionFactory.openSession();

         //4、根据反射获取接口的对象
         UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
         User user = userMapper.selectByid2(1);
         System.out.println("info:"+user);
     }
     //查询全部数据
     @Test
     public void selectAll() throws IOException {
         //1、加载myabtis配置文件 读取myabtis配置文件
         InputStream resourceAsStream = Resources.getResourceAsStream("SqlMapConfig.xml");
         //2、使用sqlSessionFactoryBuild来创建一个sqlSessionFactory
         SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
         //3、获取到sql session  进行调取api
         SqlSession sqlSession = sqlSessionFactory.openSession();
         //4、根据反射获取接口的对象
         UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
         List<User> users = userMapper.selectAllUser();
         for (User u : users){
             System.out.println("info:"+u);
         }
     }
    //增加数据
    @Test
    public void addUser() throws IOException {
        //1、加载myabtis配置文件 读取myabtis配置文件
        InputStream resourceAsStream = Resources.getResourceAsStream("SqlMapConfig.xml");
        //2、使用sqlSessionFactoryBuild来创建一个sqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        //3、获取到sql session  进行调取api
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //4、根据反射获取接口的对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = new User();
        user.setAddress("宁他爹");
        user.setName("善");
        user.setSex("男");
        user.setBirthday(new Date());

        Integer isAdd= userMapper.insertByUser(user);
        sqlSession.commit();//提交数据库事务
        System.out.println("增加的结果为："+isAdd);
    }

    //根据id修改User数据
    @Test
    public void editByUserId() throws IOException {
        //1、加载myabtis配置文件 读取myabtis配置文件
        InputStream resourceAsStream = Resources.getResourceAsStream("SqlMapConfig.xml");
        //2、使用sqlSessionFactoryBuild来创建一个sqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        //3、获取到sql session  进行调取api
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //4、根据反射获取接口的对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        System.out.println("修改之前的结果为："+userMapper.selectByid2(11));

        User user = new User();
        user.setAddress("宁他爹");
        user.setName("善");
        user.setSex("女");
        user.setBirthday(new Date());
        user.setId(11);
        userMapper.editByUserId(user);
        System.out.println("修改之后的结果为："+userMapper.selectByid2(11));
        sqlSession.commit();//提交数据库事务
    }

}
