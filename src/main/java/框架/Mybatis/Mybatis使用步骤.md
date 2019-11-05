# Mybatis 使用

## 1.导入对应版本的jar包

## 2.编写mybatis的配置xml文件

## 3.我们编写sql的xml文件

# 操作数据库的两种方式

## 1.通过mybatis提供的源生api

## 2.通过接口的方式

### 1.创建个接口，接口内的方法名跟我们的mapper.xml中的id要相同

### 2.mapper.xml中的namespace是我们接口得全限定类名

### 3.配置我们的myabatis的核心配置文件

### 4.通过sqlSession.getMapper来获取到我们的接口
    
### 5.通过接口调用方法获取返回值 并且输出

该标签配置到我们的mybatis的核心配置文件中。作用给我们的类起别名，在我们的mapper.xml种不用写全限定类名

```
    <typeAliases>
        <typeAlias type="com.qf.domain.User" alias="user"></typeAlias>
    </typeAliases>

```



# DEMO：



在idea新建一个maven工程 名字首字母大写外加qf例如：

mhb-qf-mybatis

查询字段在6个的表

1.通过id进行查询

2.查询全部

3.通过id删除

4.通过id修改

5.增加

6.分页查询