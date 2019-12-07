# Mybatis

## 框架介绍

​	Mybatis 是一款优秀的持久层框架，它支持定制化sql，存储过程以及高级映射。Mybatis避免了几乎所有的JDBC代码和手动设计参数以及获取结果集，Mybatis可以使用简单的 XML 或注解来配置和映射原生信息，将接口和Java的POJO，映射成数据库中的记录

## 框架特点

​	简单易学：本身就很小而且简单，没有任何第三方依赖，最简单安装只要两个jar文件+配置几个sql映射文件易于学习，易于学习，通过文档和源代码，可以比较完全的掌握它的设计思路和实现。

​	灵活:mybatis不会对应用程序或者数据库的现有设计强加任何影响。sql语句卸载xml里，便于统一管理和优化，通过sql基本上可以实现我们不使用数据访问框架可以实现所有的功能，或许更多。

​	解除sql与代码之间的耦合性：通过DAL层，讲业务逻辑和数据访问逻辑分析，使系统的设计更加清晰，更易维护，更易单元测试，sql 和 代码的分离，提高了可维护性。

​	提供映射标签：支持对象与数据库的orm字段关系映射。

​	提供对象关系映射标签，支持对象关系组建维护

​	提供xml标签，支持编写动态sql

### Mbatis使用步骤	

​	sql：我们新建一个表来测试我们的用法 

```sql
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(10) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '名字',
  `address` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '信息',
  `birthday` date NULL DEFAULT NULL COMMENT '生日时间',
  `sex` varchar(2) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '性别',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

```



### 	1、导入j对应版本jar包（在POM.XML文件中）

```xml
    <dependencies>

        <!--mysql的jar包-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.46</version>
        </dependency>

        <!--myabtis 的jar包-->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.2</version>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.10</version>
            <scope>compile</scope>
        </dependency>

    </dependencies>

```

​	

### 	2、配置Mbatis 的xml文件

​	在resources资源文件夹下新建一个xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--配置myabtis的数据源-->
    <environments default="">
        <environment id="devlopment">
            <!--事务管理-->
            <transactionManager type="JDBC"></transactionManager>
            <!--数据库相关信息-->
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"></property>
                <property name="driver" value="com.mysql.jdbc.Driver"></property>
                <property name="url" value="jdbc:mysql://localhost:3306/test?characterEncoding=utf-8"></property>
                <property name="username" value="root"></property>
                <property name="password" value="root"></property>
            </dataSource>
        </environment>
    </environments>

    <!--指定mapper文件,这里可以指定多个mapper映射文件-->
    <mappers>
        <mapper resource="User.xml"></mapper>
    </mappers>

</configuration>
```



### 	3、我们编写sql 的xml文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >


<mapper namespace="dao.UserMapper">

    <!-- 增加myabtis的映射
    id  对应的就是我们数据库的主键，如果有多个，我们这里需要配置相同的id标签
    column   数据库的字段名称
    property 对应的是实体的属性名
    -->
    <resultMap id="BaseResultMap" type="domain.User" >
        <id column="id" property="id" />
        <result column="name" property="name" />
        <result column="address" property="address" />
        <result column="birthday" property="birthday" />
        <result column="sex" property="sex" />
    </resultMap>

    <!--根据id查询User
        id:唯一id
        parameterType:传入的参数类型
        resultType：查询以后返回的类型
    -->
    <select id="selectByid2" parameterType="java.lang.Integer" resultMap="BaseResultMap">
        SELECT * from user where id = #{id}
    </select>

    <select id="selectAllUser" resultType="user">
        select * from user
    </select>

    <insert id="insertByUser" parameterType="domain.User">
        INSERT into user(name,sex,address) VALUES(#{name},#{sex},#{address})
    </insert>

    <update id="editByUserId" parameterType="domain.User">
        UPDATE user
        <set>
            <if test ='null != name'>name = #{name},</if>
            <if test ='null != address'>address = #{address},</if>
            <if test ='null != birthday'>birthday = #{birthday},</if>
            <if test ='null != sex'>sex = #{sex}</if>
        </set>
            WHERE id = #{id}
    </update>

</mapper>
```

### 4、测试代码

```java
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

```