# 1、什么是Netty？

​	以下为官方解释：

​		Netty项目旨在为可维护的高性能·高可扩展性协议服务器和客户端的快速开发提供异步事件驱动的网络应用程序框架和工具。 

​	换句话说，Netty是一个网络通信框架

# 2、Netty和Tomcat有什么区别

# 2、Maven依赖

```xml
    <dependency>
        <groupId>io.netty</groupId>
        <artifactId>netty-all</artifactId>
        <version>4.1.32.Final</version>
    </dependency>
```

# 3、使用Netty编写一个服务器

我们来编写一个简单的服务器，啥事也不干的那种，丢弃所有接受到的数据。让我们直接从处理程序实现开