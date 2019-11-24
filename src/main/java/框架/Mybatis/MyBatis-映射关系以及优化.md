### 第三阶段 MyBatis-02-动态SQL语句及映射关系

[TOC]

#### 回顾

```
1. MyBatis框架原理
2. MyBatis的基本数据交互方式
3. MyBatis基础配置
4. MyBatis环境搭建
5. MyBatis的基础CRUD操作
```

#### 今天任务

```
1. MyBatis关联映射
2. MyBatis延迟加载

```

#### 教学目标

```
1. 掌握MyBatis关联映射
2. 掌握MyBatis延迟加载
```

## ####一，多参数应用

##### 1、利用MyBatis实现分页查询

###### 1.1、使用map

注意：map的key要和sql中的占位符保持名字一致

mapper：

```xml
<!-- 分页：map传参 -->
     <select id="selectAuthorByPage" resultMap="authorResultMap">
         SELECT * FROM AUTHOR LIMIT #{offset}, #{pagesize}
     </select>
```

接口：

```java
/**
     * 根据分页参数查询
     * @param paramList 分页参数
     * @return 分页后的用户列表
     */
    List<Author> selectAuthorByPage(Map<String, Object> paramList);
```

测试：

```java
@Test
    public void testSelectAuthorByPage(){
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("offset", 0);
        map.put("pagesize", 2);
        
        AuthorMapper authorDao = session.getMapper(AuthorMapper.class);
        List<Author> authorList = authorDao.selectAuthorByPage(map);
        
        for (int i = 0; i < authorList.size(); i++) {
            System.out.println(authorList.get(i));
        }
    }
```

###### 1.2、使用注解

注意：mapper文件中的参数占位符的名字一定要和接口中参数的注解保持一致

**mapper：**

```xml
<!-- 分页：注解传参 -->
    <select id="selectAuthorByPage2" resultMap="authorResultMap">
        SELECT * FROM AUTHOR LIMIT #{offset}, #{pagesize}
    </select>
```

**接口：**

```java
/**
     * 根据分页参数查询
     * @param offset 偏移量
     * @param pagesize 每页条数
     * @return 分页后的用户列表
     */
    List<Author> selectAuthorByPage2(
            @Param(value="offset")int offset, 
            @Param(value="pagesize")int pagesize
    );
```

**测试：**

```java
@Test
    public void testSelectAuthorByPage2(){
        
        AuthorMapper authorDao = session.getMapper(AuthorMapper.class);
        List<Author> authorList = authorDao.selectAuthorByPage2(0, 2);
        
        for (int i = 0; i < authorList.size(); i++) {
            System.out.println(authorList.get(i));
            System.out.println("----------------------");
        }
    }
```



###### 1.3、使用序号

注意：mapper文件中参数占位符的位置编号一定要和接口中参数的顺序保持一致

**mapper：**

```xml
<!-- 分页：序号传参 -->
    <select id="selectAuthorByPage3" resultMap="authorResultMap">
        SELECT * FROM AUTHOR LIMIT #{0}, #{1}
    </select>
```

**接口：**

```java
 /**
     * 根据分页参数查询
     * @param offset 偏移量
     * @param pagesize 每页条数
     * @return 分页后的用户列表
     */
    List<Author> selectAuthorByPage3(int offset, int pagesize);
```

**测试：**

```java
 @Test
    public void testSelectAuthorByPage3(){
        
        AuthorMapper authorDao = session.getMapper(AuthorMapper.class);
        List<Author> authorList = authorDao.selectAuthorByPage3(1, 1);
        
        for (int i = 0; i < authorList.size(); i++) {
            System.out.println(authorList.get(i));
            System.out.println("----------------------");
        }
    }
```



## ####二、性能优化

### #####1、延迟加载

#### ######1.1、 什么是延迟加载

​	resultMap中的association和collection标签具有延迟加载的功能。

​	延迟加载的意思是说，在关联查询时，利用延迟加载，先加载主信息。需要关联信息时再去按需加载关联信息。这样会大大提高数据库性能，因为查询单表要比关联查询多张表速度要快。

#### ######1.2 、设置延迟加载

​	Mybatis默认是不开启延迟加载功能的，我们需要手动开启。

​	需要在SqlMapConfig.xml文件中，在<settings>标签中开启延迟加载功能。

​	lazyLoadingEnabled、aggressiveLazyLoading

| 设置项                | 描述                                                         | 允许值        | 默认值 |
| --------------------- | ------------------------------------------------------------ | ------------- | ------ |
| lazyLoadingEnabled    | 全局性设置懒加载。如果设为‘false’，则所有相关联的都会被初始化加载。 | true \| false | true   |
| aggressiveLazyLoading | 当设置为‘true’的时候，懒加载的对象可能被任何懒属性全部加载。否则，每个属性都按需加载。 | true \| false | true   |

#### ######1.3、使用association进行延迟加载

##### ######1.3.1  需求

​	查询订单并且关联查询用户信息（对用户信息的加载要求是按需加载）

##### ######1.3.2  编写映射文件

​	需要定义两个mapper的方法对应的statement。

1、只查询订单信息

​	SELECT * FROM orders

​	在查询订单的statement中使用association去延迟加载（执行）下边的satatement(关联查询用户信息)

```xml
<!-- 定义OrdersUserLazyLoadingRstMap -->
<resultMap type="com.qf.mybatis.po.Orders" id="OrdersUserLazyLoadingRstMap">
    <id column="id" property="id" />
    <result column="user_id" property="userId" />
    <result column="number" property="number" />
    <result column="createtime" property="createtime" />
    <result column="note" property="note" />
    <!-- 延迟加载用户信息 -->
    <!-- select：指定延迟加载需要执行的statement的id（是根据user_id查询用户信息的statement）
       我们使用UserMapper.xml中的findUserById完成根据用户ID（user_id）查询用户信
       如果findUserById不在本mapper中，前边需要加namespace
    -->
    <!-- column：主信息表中需要关联查询的列，此处是user_id -->
    <association property="user" select="com.qf.mybatis.mapper.UserMapper.findUserById" column="user_id"></association>
</resultMap>
<!-- 查询订单信息，延迟加载关联查询的用户信息 -->
<select id="findOrdersUserLazyLoading" resultMap="OrdersUserLazyLoadingRstMap">
    SELECT * FROM orders
</select>
```

 

2、关联查询用户信息

​           通过上边查询到的订单信息中user_id去关联查询用户信息

​           使用UserMapper.xml中的findUserById

```xml
<select id="findUserById" parameterType="int"
       resultType="com.qf.mybatis.po.User">
    SELECT * FROM user WHERE id = #{id}
</select>
```

 

​	上边先去执行findOrdersUserLazyLoading，当需要去查询用户的时候再去执行findUserById，通过resultMap的定义将延迟加载执行配置起来。

 

##### ######1.3.3  加载映射文件

<!-- 批量加载mapper文件，需要mapper接口文件和mapper映射文件名称相同且在同一个包下 -->

<package name="com.qf.mybatis.mapper”/>

##### ######1.3.4  编写mapper接口

// 查询订单信息，延迟加载关联查询的用户信息

public List<Orders> findOrdersUserLazyLoading();

##### ######1.3.5  编写测试代码

思路：

1、执行上边mapper方法（findOrdersUserLazyLoading），内部去调用com.qf.mybatis.mapper.OrdersMapper中的findOrdersUserLazyLoading只查询orders信息（单表）。

2、在程序中去遍历上一步骤查询出的List<Orders>，当我们调用Orders中的getUser方法时，开始进行延迟加载。

3、执行延迟加载，去调用UserMapper.xml中findUserbyId这个方法获取用户信息。

```java
@Test
public void testFindOrdersUserLazyLoading() {
    // 创建sqlSession
    SqlSession sqlSession = sqlSessionFactory.openSession();
    // 通过SqlSession构造usermapper的代理对象
    OrdersMapper ordersMapper = sqlSession.getMapper(OrdersMapper.class);
    // 调用usermapper的方法
    List<Orders> list = ordersMapper.findOrdersUserLazyLoading();
    for(Orders orders : list){
       System.out.println(orders.getUser());
    }
    // 释放SqlSession
    sqlSession.close();
}
```



#### ######1.4、延迟加载思考

不使用mybatis提供的association及collection中的延迟加载功能，如何实现延迟加载？？

 实现方法如下：

定义两个mapper方法：

1、查询订单列表

2、根据用户id查询用户信息

实现思路：

先去查询第一个mapper方法，获取订单信息列表

在程序中（service），按需去调用第二个mapper方法去查询用户信息。

 总之：

使用延迟加载方法，先去查询简单的sql（最好单表，也可以关联查询），再去按需要加载关联查询的其它信息。

 



## ####二、MyBatis关联映射

### #####1、主键映射

#### ######1.1、主键映射作用

- 当数据插入操作不关心插入后数据的主键（唯一标识），那么建议使用 *不返回自增主键值* 的方式来配置插入语句，这样可以避免额外的SQL开销.

- 当执行插入操作后需要立即获取插入的自增主键值，比如一次操作中保存一对多这种关系的数据，那么就要使用 *插入后获取自增主键值* 的方式配置.

  

  ​	mybatis进行插入操作时，如果表的主键是自增的，针对不同的数据库相应的操作也不同。基本上经常会遇到的就是Oracle Sequece 和 Mysql 自增主键，解释如下。

#### ######1.2、自动递增

​	一对多的那种表结构，在插入多端数据时，需要获取刚刚保存了的一段的主键。那么这个时候，上述的配置就无法满足需要了。为此我们需要使用mybatis提供的`<selectKey />`来单独配置针对自增逐渐的处理。

针对于Mysql这种自己维护主键的数据库，可以直接使用以下配置在插入后获取插入主键，

```Sql
 <sql id='TABLE_NAME'>TEST_USER</sql>
 <insert id="insert" useGeneratedKeys="true" keyProperty="id" parameterType="User">     insert into <include refid="TABLE_NAME" /> ( NAME, AGE )         values ( #{name}, #{age} ) </insert>
```

当然，由于Mysql的自增主键可以通过SQL语句

```
 select LAST_INSERT_ID();
```

来获取的。因此针对Mysql，Mybatis也可配置如下：

```Sql
 <sql id='TABLE_NAME'>TEST_USER</sql>
 <!-- 注意这里需要先查询自增主键值 --> <insert id="insert" parameterType="User">     <selectKey keyProperty="id" resultType="int" order="BEFORE">         SELECT LAST_INSERT_ID()     </selectKey>     insert into <include refid="TABLE_NAME" /> (ID,NAME,AGE)         values ( #{id}, #{name}, #{age} ) </insert>
```

只不过该中配置需要额外的一条查询SQL!

#### ######1.3、非自动递增

如果考虑到插入数据的主键不作为其他表插入数据的外键使用，那么可以考虑使用这种方式。

##### ######1.3.2、Mysql自增主键配置

​	由于mysql数据库中，可以设置表的主键为自增，所以对于Mysql数据库在mybatis配置插入语句时，不指定插入ID字段即可。主键的自增交由Mysql来管理。

```Xml
 <sql id='TABLE_NAME'>TEST_USER</sql>
 <!-- 注意这里的插入SQL中是没有指明ID字段的！ --> <insert id="insert" parameterType="User">     insert into <include refid="TABLE_NAME" /> (NAME,AGE)         values (#{name}, #{age} ) </insert>
```

​	同样，针对Mysql如此配置mybaits，插入完成后`user.id`为空

#### #####2、关联映射

##### ######2.1、关联映射作用

​	在现实的项目中进行数据库建模时，我们要遵循数据库设计范式的要求，会对现实中的业务模型进行拆分，封装在不同的数据表中，表与表之间存在着**一对多**或是**多对多**的对应关系。进而，我们对数据库的增删改查操作的主体，也就从单表变成了多表。那么Mybatis中是如何实现这种多表关系的映射呢？

查询结果集 

​	resultMap 元素是 MyBatis 中最重要最强大的元素。它就是让你远离 90%的需要从结果 集中取出数据的 JDBC 代码的那个东西，而且在一些情形下允许你做一些 JDBC 不支持的事 情。 事实上, 编写相似于对复杂语句联合映射这些等同的代码，也许可以跨过上千行的代码。

​	有朋友会问，之前的示例中我们没有用到结果集，不是也可以正确地将数据表中的数据映射到Java对象的属性中吗？是的。这正是resultMap元素设计的初衷，就是简单语句不需要明确的结果映射，而很多复杂语句确实需要描述它们的关系。

- resultMap元素中，允许有以下直接子元素：

- constructor － 类在实例化时，用来注入结果到构造方法中（本文中暂不讲解）

- id － 作用与result相同，同时可以标识出用这个字段值可以区分其他对象实例。可以理解为数据表中的主键，可以定位数据表中唯一一笔记录

- result － 将数据表中的字段注入到Java对象属性中

- association － 关联，简单的讲，就是“有一个”关系，如“用户”有一个“帐号”

- collection － 集合，顾名思议，就是“有很多”关系，如“客户”有很多“订单”

- discriminator － 使用结果集决定使用哪个个结果映射（暂不涉及）

   每个元素的用法及属性我会在下面结合使用进行讲解。

   我们在数据库中额外创建三张数据表，分别表示销售人员、客户，以及销售和客户多对多的对应关系。每个销售、客户都有一个登录帐号。

```Sql
CREATE TABLE `customer` (
  `customer_id` int(10) NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(200) NOT NULL,
  `user_id` int(10) DEFAULT NULL,
  `is_valid` tinyint(4) NOT NULL DEFAULT '1',
  `created_time` datetime NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`customer_id`),
  KEY `customer_name` (`customer_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8；

CREATE TABLE `salesman` (
  `sales_id` int(10) NOT NULL AUTO_INCREMENT,
  `sales_name` varchar(64) NOT NULL,
  `sales_phone` varchar(32) DEFAULT NULL,
  `sales_fax` varchar(32) DEFAULT NULL,
  `sales_email` varchar(100) DEFAULT NULL,
  `user_id` int(10) DEFAULT NULL,
  `report_to` int(10) DEFAULT '0',
  `is_valid` tinyint(4) NOT NULL DEFAULT '1',
  `created_time` datetime DEFAULT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`sales_id`),
  KEY `sales_name` (`sales_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8；

CREATE TABLE `customer_sales` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `customer_id` int(10) NOT NULL,
  `sales_id` int(10) NOT NULL,
  `created_time` datetime NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `customer_id` (`customer_id`,`sales_id`) USING BTREE,
  KEY `sales_id` (`sales_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8；
```

​	实现销售与登录用户一对一关系

​	这里采用Mybatis的接口式编程。无论是对单表进行映射，还是对多表映射，步骤都是相同的，唯一的不同就在映射文件的编写上。

 	首先，我们需要销售创建一个Java类，其中的userInfo属性对应销售的登录用户信息的。

```Java
public class Sales {
	private int salesId;
	private String salesName;
	private String phone;
	private String fax;
	private String email;
	private int isValid;
	private Timestamp createdTime;
	private Timestamp updateTime;
	private User userInfo;
```

第二步，编写Mybatis映射文件，需要注意的是映射文件的名称空间，要与我们编写的接品的全限定名一致（包名＋接口名）

```Xml
<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.qf.dao.ISalesDao">
    <resultMap id="salesResultMap" type="com.qf.pojo.Sales">
        <id property="salesId" column="sales_id" />
        <result property="salesName" column="sales_name" />
        <result property="phone" column="sales_phone" />
        <result property="fax" column="sales_fax" />
        <result property="email" column="sales_email" />

        <!-- 定义多对一关联信息（每个销售人员对应一个登录帐号） -->
        <association property="userInfo" column="user_id" javaType="User" select="selectUser">
            <id property="userId" column="userId" />
            <result property="userName" column="user_name" />
            <result property="userPassword" column="user_password" />
            <result property="nickName" column="nick_name" />
            <result property="email" column="email" />
            <result property="isValid" column="is_valid" />
            <result property="createdTime" column="created_time" />
            <result property="updateTime" column="update_time" />
        </association>
    </resultMap>
	
    <select id="selectUser" resultType="User">
        SELECT user_id, user_name, user_password, nick_name, email, is_valid, created_time
        FROM sys_user WHERE user_id = #{id}
    </select>
	
    <select id="getById" parameterType="int" resultMap="salesResultMap" >
        SELECT sales_id, sales_name, sales_phone, sales_fax, sales_email, user_id, is_valid, created_time, update_time
        FROM salesman WHERE sales_id=#{id}
    </select>
</mapper>
```

第三步，将映射文件注册到Mybatis中。

```Xml
<mappers>
    <mapper resource="com/qf/mapping/User.xml" />
    <mapper resource="com/qf/mapping/Sales.xml" />
</mappers>
```

第四步，编写接口

```Java
public interface ISalesDao {  
    public Sales getById(int id);  
}  
```

第五步，编写测试用例

```Java
public class SalesDaoTest {

	private Reader reader;
	private SqlSessionFactory sqlSessionFactory;

	@Before
	public void setUp() throws Exception {
		try {
			reader = Resources.getResourceAsReader("mybatis.xml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void getById() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			ISalesDao sd = session.getMapper(ISalesDao.class);
			Sales sales = sd.getById(2);
			assertNotNull(sales);
			System.out.println(sales);
		} finally {
			session.close();
		}
	}

}
```

下面我们就针对第二步，映射文件中的resultMap编写进行详细讲解。

```Xml
<resultMap id="salesResultMap" type="com.qf.pojo.Sales">
    <id property="salesId" column="sales_id" />
    <result property="salesName" column="sales_name" />
    <result property="phone" column="sales_phone" />
    <result property="fax" column="sales_fax" />
    <result property="email" column="sales_email" />
    <result property="isValid" column="is_valid" />
    <result property="createdTime" column="createdTime" />
    <result property="updateTime" column="update_time" />

    <!-- 定义多对一关联信息（每个销售人员对应一个登录帐号） -->
    <association property="userInfo" column="user_id" javaType="User" select="selectUser">
        <id property="userId" column="userId" />
        <result property="userName" column="user_name" />
        <result property="userPassword" column="user_password" />
        <result property="nickName" column="nick_name" />
        <result property="email" column="email" />
        <result property="isValid" column="is_valid" />
        <result property="createdTime" column="created_time" />
        <result property="updateTime" column="update_time" />
    </association>
</resultMap>
```

​	和其他元素一样，我们都需要为其取一个唯一的id，并指定其在Java中对应的类型，由于我没有在Mybatis配置文件中为Sales类指定别名，所以这里使用的是全限定名。

```Xml
<resultMap id="salesResultMap" type="com.qf.pojo.Sales"> 
```

​	使用id和result元素指定数据表中字段与Java类中属性的映射关系，除了我phone、fax和email三行映射代码，其余的全部可以省去不写。为什么？这个就像前面示例中使用到的User类一样，Mybatis会自动帮助我们完成映射工作，不需要我们额外编写代码。那么为什么phone、fax和email这三个字段的映射关系不能省略呢？这是因为我在编写Sales类的时候埋下了伏笔，我故意不按照按驼峰规则对这三个属性进行命名，同时也不与数据表中的字段名相同，为了确保可以正确的将字段映射到属性上，我们必须手工编写映射在代码，明确地告诉Mybatis我们的映射规则。

```Xml
<resultMap id="salesResultMap" type="com.qf.pojo.Sales">
    <result property="phone" column="sales_phone" />
    <result property="fax" column="sales_fax" />
    <result property="email" column="sales_email" />
</resultMap>
```

​	下面重点来了，association元素来帮助我们完成销售与登录用户对应关系的映射。她实现了“有一个”的关系映射，我们需要做的只是告诉Mybatis，这个关系是通过哪一个字段来建立关联的，被关联的对象类型是什么，以及将关联对象映射到哪个属性上面。如果被关联对象的数据结构比较简单，就如本文中的登录用户表这样，那么可以有更简单的写法。

```Xml
<association property="userInfo" column="user_id" javaType="User" select="selectUser" />  
```

我们还需要告诉Mybatis，加载关联的方式。MyBatis 在这方面会有两种不同的方式:

- 嵌套查询:通过执行另外一个 SQL 映射语句来返回预期的复杂类型。
- 嵌套结果:使用嵌套结果映射来处理重复的联合结果的子集。

##### ######2.2、嵌套查询映射

我们在这里先使用嵌套查询来实现。使用属性select指定了关联数据的查询语句。

```Xml
<select id="selectUser" resultType="User">  
    SELECT user_id, user_name, user_password, nick_name, email, is_valid, created_time  
    FROM sys_user WHERE user_id = #{id}  
</select>  
```

当对Sales进行映射的时候，Mybatis会使用这个名为selectUser的查询语句去获取相关联的数据信息。这种方法使用起来很简单。但是简单，不代表最好。对于大型数据集合和列表这种方式将会有性能上的问题，就是我们熟知的 “N+1 查询问题”。概括地讲,N+1 查询问题可以是这样引起的:

- 你执行了一个单独的 SQL 语句来获取结果列表(就是“+1”)。
- 对返回的每条记录,你执行了一个查询语句来为每个加载细节(就是“N”)。

这个问题会导致成百上千的 SQL 语句被执行。这通常不是期望的。

MyBatis 能延迟加载这样的查询就是一个好处,因此你可以分散这些语句同时运行的消耗。然而,如果你加载一个列表,之后迅速迭代来访问嵌套的数据,你会调用所有的延迟加载,这样的行为可能是很糟糕的。

##### ######2.3、嵌套结果映射

下面我们就来讲一下另一种实式方式：嵌套结果。使用这种方式，就可以有效地避免了N+1问题。

```Xml
<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.qf.dao.ISalesDao">
	<resultMap id="salesResultMap" type="com.qf.pojo.Sales">
		<id property="salesId" column="sales_id" />
		<result property="salesName" column="sales_name" />
		<result property="phone" column="sales_phone" />
		<result property="fax" column="sales_fax" />
		<result property="email" column="sales_email" />
		<result property="isValid" column="is_valid" />
		<result property="createdTime" column="created_time" />
		<result property="updateTime" column="update_time" jdbcType="TIMESTAMP" />

		<!-- 定义多对一关联信息（嵌套结果方式） -->
		<association property="userInfo" resultMap="userResult" />
	</resultMap>

	<resultMap id="userResult" type="User">
		<id property="userId" column="user_id" />
		<result property="userName" column="user_name" />
		<result property="userPassword" column="user_password" />
		<result property="nickName" column="nick_name" />
		<result property="email" column="user_email" />
		<result property="isValid" column="user_is_valid" />
		<result property="createdTime" column="user_created_time" />
		<result property="updateTime" column="user_update_time" />
	</resultMap>

	<select id="getById" parameterType="int" resultMap="salesResultMap">
		SELECT
		sales_id, sales_name, sales_phone, sales_fax, sales_email,
		salesman.is_valid, salesman.created_time, salesman.update_time,
		sys_user.user_id as user_id, user_name, user_password, nick_name,
		email as user_email,
		sys_user.is_valid as user_is_valid, sys_user.created_time as
		user_created_time,
		sys_user.update_time as user_update_time
		FROM
		salesman left outer join sys_user using(user_id)
		WHERE sales_id=#{id}
	</select>
</mapper>
```

和嵌套查询相比，使用嵌套结果方式，在映射文件上主要有以下三处修改：

一、修改association元素，无需指定column，另外将resultType改为使用resultMap。为什么？这是因为后面我们会把select语句改为多表关联查询，这样就会有些字段名是冲突的，我们不得不使用别名。这一点对于Mybatis而言，就相当于字段名发生了变化，那么就需要我们手工来维护映射关系。另外，我们也无需指定javaType属性了，因为在resultMap中，已经指定了对应的Java实体类，这里就可以省略了。

```Xml
<association property="userInfo" resultMap="userResult" />
```

  

二、为关联结果集编写映射关系，大家可以看到，好多字段名称已经发生了变化，如is_valid这个字段由于salesman和sys_user表中都存在这个字段，所以我们不得不为其起了一个别名user_is_valid。

```xml
<resultMap id="userResult" type="User">  
        <id property="userId" column="user_id" />  
        <result property="userName" column="user_name" />  
        <result property="userPassword" column="user_password" />  
        <result property="nickName" column="nick_name" />  
        <result property="email" column="user_email" />  
        <result property="isValid" column="user_is_valid" />  
        <result property="createdTime" column="user_created_time" />  
        <result property="updateTime" column="user_update_time" />  
    </resultMap> 
```

三、修改查询语句，由单表查询改表多表关联查询

```xml
<select id="getById" parameterType="int" resultMap="salesResultMap">  
    SELECT sales_id, sales_name, sales_phone, sales_fax, sales_email,  
           salesman.is_valid, salesman.created_time, salesman.update_time,  
           sys_user.user_id as user_id, user_name, user_password, nick_name,  
           email as user_email,  
           sys_user.is_valid as user_is_valid, sys_user.created_time as  
           user_created_time,  
           sys_user.update_time as user_update_time  
    FROM salesman left outer join sys_user using(user_id)  
    WHERE sales_id=#{id}  
</select> 
```

至此，关联映射已讲解完了。还有集合映射没有讲，哇咔咔，内空实在是太多了〜〜〜〜今晚通宵也未必能写得完了。暂时先写到这儿吧，下回再继续讲解如何实现多对多的集合映射。

#### #####3、集合映射

##### ######3.1、集合映射作用

集合映射，实现销售与客户的多对多关系

第一步，在动手编写映射文件之前，我们需要对Sales类增加一个List属性，用以保存销售员对应的客户列表。

```Java
private List<Customer> customers;  
  
public Sales() {  
    super();  
    this.setCustomers(new ArrayList<Customer>());  
}  
  
public List<Customer> getCustomers() {  
    return customers;  
}  
  
protected void setCustomers(List<Customer> customers) {  
    this.customers = customers;  
}  
```

同时增加一个客户类。

```java
public class Customer {   
    private int customerId;  
    private String customerName;   
    private int isValid;   
    private Timestamp createdTime;  
    private Timestamp updateTime;   
    private User userInfo;  

```

第二步，修改映射文件。我们先使用嵌套查询方式来实现为销售加载客户列表。首先在resultMap中增加客户集合映射的定义。

##### ######3.2、嵌套查询映射

```xml
<!-- 定义一对多集合信息（每个销售人员对应多个客户） -->
<collection property="customers" javaType="ArrayList" column="sales_id" ofType="Customer" select="getCustomerForSales" />
```

集合映射的定义与关联映射定义很相似，除了关键字不同外，还多了两个属性JavaType和ofType。

property用于指定在Java实体类是保存集合关系的属性名称

JavaType用于指定在Java实体类中使用什么类型来保存集合数据，多数情况下这个属性可以省略的。

column用于指定数据表中的外键字段名称。

ofType用于指定集合中包含的类型。

select用于指定查询语句。

 然后再定义查询客户的查询语句。

```xml
<select id="getCustomerForSales" resultType="com.qf.pojo.Customer">
    SELECT c.customer_id, c.customer_name, c.user_id, c.is_valid,
    c.created_time, c.update_time
    FROM customer c INNER JOIN customer_sales s USING(customer_id)
    WHERE s.sales_id = #{id}
</select>
```

​	需要注意的是，无论是关联还是集合，在嵌套查询的时候，查询语句的定义都不需要使用parameterType属性定义传入的参数类型，因为通常作为外键的，都是简单数据类型，查询语句会自动使用定义在association或是collection元素上column属性作为传入参数的。

​	运行测试用例，看到如下结果就说明我们的映射文件是正确的了。

##### ######3.3、嵌套结果映射

```xml
<resultMap id="salesResultMap" type="com.qf.pojo.Sales">
    <id property="salesId" column="sales_id" />
    <result property="salesName" column="sales_name" />
    <result property="phone" column="sales_phone" />
    <result property="fax" column="sales_fax" />
    <result property="email" column="sales_email" />
    <result property="isValid" column="is_valid" />
    <result property="createdTime" column="created_time" />
    <result property="updateTime" column="update_time" />

    <!-- 定义多对一关联信息（嵌套结果方式） -->
    <association property="userInfo" resultMap="userResult" />

    <!-- 定义一对多集合信息（每个销售人员对应多个客户） -->
    <!-- <collection property="customers" column="sales_id" select="getCustomerForSales" /> -->

    <collection property="customers" ofType="com.qf.pojo.Customer">
        <id property="customerId" column="customer_id" />
        <result property="customerName" column="customer_name" />
        <result property="isValid" column="is_valid" />
        <result property="createdTime" column="created_time" />
        <result property="updateTime" column="update_time" />
        <!-- 映射客户与登录用户的关联关系，请注意columnPrefix属性 -->
        <association property="userInfo" resultMap="userResult" columnPrefix="cu_" />
    </collection>
</resultMap>
```

这里将客户的映射关系直接写在了销售的resultMap中。上述代码与关联映射十分相似，只是有一点需要朋友们留心，那就是在对客户数据进行映射的时候，**我们使用了association元素的一个新的属性columnPrefix**。这个属性是做什么用的呢？从名字上理解，就是给每个栏位之前加上前缀。Bingo！答对了，那么什么情况下会使用到这个属性呢？后面我们会结合着修改后的查询语句来说明这个属性的使用场景。请耐心的往下看。：）

映射结果修改好了，紧接着我们就要修改查询语句了。

```sql
<select id="getById" parameterType="int" resultMap="salesResultMap">  
    SELECT  
        s.sales_id, s.sales_name, s.sales_phone, s.sales_fax, s.sales_email,  
        s.is_valid, s.created_time, s.update_time,  
        su.user_id as user_id, su.user_name, su.user_password, su.nick_name,  
        su.email as user_email,  
        su.is_valid as user_is_valid,  
        su.created_time as user_created_time,  
        su.update_time as user_update_time,  
        c.customer_id, c.customer_name, c.is_valid as customer_is_valid,  
        c.created_time as customer_created_time,  
        c.update_time as customer_update_time,  
        cu.user_id as cu_user_id, cu.user_name as cu_user_name, cu.user_password as cu_user_password,   
        cu.nick_name as cu_nick_name, cu.email as cu_user_email, cu.is_valid as cu_user_is_valid,  
        cu.created_time as cu_user_created_time, cu.update_time as cu_user_update_time  
    FROM  
        salesman s LEFT OUTER JOIN sys_user su ON s.user_id = su.user_id  
        INNER JOIN customer_sales cs USING(sales_id)  
        LEFT OUTER JOIN customer c USING(customer_id)  
        LEFT OUTER JOIN sys_user cu ON c.user_id = cu.user_id  
    WHERE sales_id=#{id}  
</select>  

```

这个语句乍看起来有些复杂，其实很容易理解。这里用到了四张数据表，销售、客户、客房销售关系表和登录用户表。具体的字段我就不说了，主要说一下这个登录用户表。这张数据表在查询语句中出现了两次，为什么呢？因为销售与登录用户有关联关系，同样地，客户也与登录用户表有关联关系，所以我们需要对用户表进行两次Join操作。

那么问题来了，销售要用户有关联，客户也要与用户有关联，这种映射语句应该如何写呢？难道要对用户表写两次映射？聪明的朋友一定会说，我们可以复用之前写过的用户映射结果集呀！答案是肯定的。我们不妨在这里再次贴出这段代码，一起回忆一下。

```xml
<resultMap id="userResult" type="User">  
    <id property="userId" column="user_id" />  
    <result property="userName" column="user_name" />  
    <result property="userPassword" column="user_password" />  
    <result property="nickName" column="nick_name" />  
    <result property="email" column="user_email" />  
    <result property="isValid" column="user_is_valid" />  
    <result property="createdTime" column="user_created_time" />  
    <result property="updateTime" column="user_update_time" />  
</resultMap>  
```

数据表中的字段与Java实体类中的属性的映射关系是一一对应的，Mybatis会根据我们定义的映射关系，将数据表中字段的映射到Java实体类属性上。

可是我们的查询语句中对用户表进行了两次Join操作，第一次是销售与用户的Join，第二次是客户与用户的Join。而SQL语句是不允许在同一条查询语句中出现相同字段名的（虽然我们有时候会这样写，但是数据库会自动帮我们为重名的字段名起个别名的，比如在字段名后添加数字）。如果我们为第二次Join进来的用户表中的字段使用别名方式，那么就会导致映射的到客户类中的用户信息缺失，因为字段名与我们在映射文件中的定义不一致。如何解决这个问题呢？这时候该columnPrefix属性出场了。

Mybatis也考虑到这种情况的出现，她允许我们在重复出现的字段名前加上一个**统一的字符前缀**，这样就可以有效的避免字段重名，又可以复用之前定义的映射结果集。

在上述的查询语句中，我们为第二次Join进来的用户表中的字段都加上了“cu_”做为区分重名字段的前缀，同时使用columnPrefix属性告诉Mybatis在第二次对用户表映射的时候，将字段名是以“cu_”打头的字段值映射到Java实体类属性当中。这样就可以正确的把客户与用户的关联信息映射到Customer对象当中了。

```
<association property="userInfo" resultMap="userResult" columnPrefix="cu_" />  
```

我们之前在User.xml文件中定义过用户表的映射结果集，现在在Sales.xml中也需要使用到同样的结果集，是否可以直接跨文件引用呢？答案是肯定的了，不然对于同一个映射结果集，我们要多处编写，多处维护，这样不仅工作量大，对日后的维护也带来了一定的麻烦。我们只需要在引用处使用结果集的全限定名就可以了。

```xml
	<resultMap id="salesResultMap" type="com.qf.pojo.Sales">
		<id property="salesId" column="sales_id" />
		<result property="salesName" column="sales_name" />
		<result property="phone" column="sales_phone" />
		<result property="fax" column="sales_fax" />
		<result property="email" column="sales_email" />
		<result property="isValid" column="is_valid" />
		<result property="createdTime" column="created_time" />
		<result property="updateTime" column="update_time" />

		<!-- 定义多对一关联信息（嵌套查询方式） -->
		<!-- <association property="userInfo" column="user_id" javaType="User" 
			select="selectUser" fetchType="lazy"> </association> -->

		<!-- 定义多对一关联信息（嵌套结果方式） -->
		<association property="userInfo" resultMap="com.qf.xml.user.userResult" />

		<!-- 定义一对多集合信息（每个销售人员对应多个客户） -->
		<!-- <collection property="customers" column="sales_id" select="getCustomerForSales" 
			/> -->

		<collection property="customers" ofType="com.qf.pojo.Customer">
			<id property="customerId" column="customer_id" />
			<result property="customerName" column="customer_name" />
			<result property="isValid" column="is_valid" />
			<result property="createdTime" column="created_time" />
			<result property="updateTime" column="update_time" />
			<association property="userInfo" resultMap="com.qf.xml.user.userResult" columnPrefix="cu_" />
		</collection>
	</resultMap>
```



**3、关联Mapper：**

上面我们看到了我们使用了select关联其他的sql语句，而select里面给的就是一个全限定的路径。分别是：

com.qf.mapper.MaleEmployeeMapper.findProstateList

和

com.qf.mapper.FemaleEmployeeMapper.findUterusList

现在让我们看看这两个Mapper是怎么样的：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="comqf.mapper.MaleEmployeeMapper">
    <select id="findProstateList" parameterType="int" resultType="string">
        select prostate from t_healthy_male where emp_id = #{emp_id}
    </select>
</mapper>


<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="comqf.mapper.FemaleEmployeeMapper">
    <select id="findUterusList" parameterType="int" resultType="string">
        select uterus from t_healthy_female where emp_id = #{emp_id}
    </select>
</mapper>


```

显然他们都比较简单，和我们定义的普通Mapper没什么区别。


#### 课前默写

```
1. 掌握MyBatis框架原理
2. 掌握MyBatis的基本数据交互方式
3. 掌握MyBatis基础配置
4. 掌握MyBatis环境搭建
5. 掌握MyBatis的基础CRUD操作
```

#### 作业

```
1. 使用MySchool数据库
2. 建立各实体间的关系
3. 实现各表的CRUD操作
```

