# Netty快速入门（第一步）——认识Netty

## 1、什么是Netty？

​	以下为官方解释：

​		Netty项目旨在为可维护的高性能·高可扩展性协议服务器和客户端的快速开发提供异步事件驱动的网络应用程序框架和工具。 

​	Netty是由JBOSS提供第一个Java开源框架

​	Netty提供异步的，事件驱动的网络应用程序和工具，用以快速开发高性能，高科性能的网络服务器和客户端程序

​	换句话说，Netty是一个网络通信框架

## 2、Netty和Tomcat有什么区别

​	Netty和Tomcat最去大的区别在于通信协议，Tomcat是基于Http协议的，他的是指是一个基于Http协议的web容器，但是Netty不一样，他能通过编程自定义各种协议，因为Netty能够通过codec来编码/解码字节流，完成类是redis访问的功能，这就是netty和tomcat最大的不同。

​	有人说netty的性能一定比tomcat性能高，其实不然，tomcat从6.0版本之后，就开始了支持nio模式，后续还要APR模式。一种通过jni调用apache网络库的模式，相比于旧的bio模式，并发性能得到了很大提高，特别是APR模式，而netty是否比tomcat性能更高，则要取决于netty程序作者的技术实力了。

## 3、Maven依赖

```xml
    <dependency>
        <groupId>io.netty</groupId>
        <artifactId>netty-all</artifactId>
        <version>4.1.32.Final</version>
    </dependency>
```

## 4、Netty为什么并发高？

​	Netty是一款基于NIO开发的网络通信框架，对比BIO，他的并发性能你得到了很大提高。

![阻塞IO模型](https://upload-images.jianshu.io/upload_images/1089449-546a563c9822ce16.png?imageMogr2/auto-orient/strip|imageView2/2/w/548/format/webp)

![非阻塞IO模型](https://upload-images.jianshu.io/upload_images/1089449-9eebe781fba495fd.png?imageMogr2/auto-orient/strip|imageView2/2/w/572/format/webp)

  	从以上两图可以看出，NIO单线程能处理连接的数量比BIO要高出很多，而为什么单线程能处理更多的连接呢？原因就是图二中出现的 Selector（选择器）.

​	当一个连接建立之后，他又两个步骤要做，第一步是接受完客户端发送过来的全部数据，第二步是服务端处理完请求业务之后返回response给客户端。NIO和BIO的区别主要是在第一步。

​	在BIO中,等待客户端发送数据这个过程是阻塞的，这样就造成了一个线程只能处理一个请求的情况，而机器能支持的最大线程数是有限的，这就是为什么BIO不能支持高并发的原因。

​	而NIO中，当一个Socket建立好之后，Thread并不会阻塞去接受这个Socket，二十将这个请求交给Selector，Selector会不断的去轮询所有的Socket，一旦有一个Socket建立完成，他会通知Thread，然后Thread处理完数据再返回给客户端，**这个过程是不阻塞的** ，这样就能让一个Thread处理更多的请求。

- BIO，同步阻塞IO，阻塞整个步骤，如果连接少，他的延迟是最低的，因为一个线程只处理一个连接，适用于少连接且延迟低的场景，比如说数据库连接。 
- NIO，同步非阻塞IO，阻塞业务处理但不阻塞数据接收，适用于高并发且处理简单的场景，比如聊天软件。 
- 多路复用IO，他的两个步骤处理是分开的，也就是说，一个连接可能他的数据接收是线程a完成的，数据处理是线程b完成的，他比BIO能处理更多请求。 
- 信号驱动IO，这种IO模型主要用在嵌入式开发。
- 异步IO，他的数据请求和数据处理都是异步的，数据请求一次返回一次，适用于长连接的业务场景。 

## 5、Netty为什么传输快？

​	Netty的传输快其实也是依赖了NIO的一个特性——**零拷贝** 。我们指定Java 的内存有堆内存、栈内存和字符串常量池等等，其实堆内存是占用内存空间最大的一块，也是Java存放POJO的地方，一般我们的数据如果需要从IO读取到堆内存，中间需要经过Socket缓冲区，也就是是一个数据会被拷贝两次才能到达他的终点，如果传递的数据较大，那么就会造成不必要的资源浪费。

​	Netty 针对这种情况，使用了NIO中的另一大特性——零拷贝，当他需要接受数据的时候，他会再堆内存之外开辟一块内存，数据直接从IO读到了那块内存中去，再netty里面通过BytBu可以直接堆这些数据进行操作，从而加快了传输速度。

## 6、Netty中的重要概念

![img](https://upload-images.jianshu.io/upload_images/1089449-afd9e14197e1ef11.png?imageMogr2/auto-orient/strip|imageView2/2/w/751/format/webp)

 

- Channel：表示一个连接，可以理解为每一个请求，就是一个Channel
- ChannelHandler，核心处理业务就在这里，用于处理业务请求
- ChannelPipeline，用于保存出苦力过程需要用到的ChannelHandler和ChannerHandlerContext
- ChannelHandlerContext，用于传输业务数据。 

## 2、Netty组件再了解

![在这里插入图片描述](https://img-blog.csdnimg.cn/20191220151344291.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxNzc5MjQ3MjU3,size_16,color_FFFFFF,t_70)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20191220153353256.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxNzc5MjQ3MjU3,size_16,color_FFFFFF,t_70)

