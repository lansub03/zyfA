package Aio.server;

import config.UtilsNumber;

public class AioServer {
    private static AioServerHandler serverHandler;
    //统计客户端个数
    public volatile  static  long clientCount = 0;

    public static void start(){
        if (serverHandler != null)return;
        serverHandler =  new AioServerHandler(UtilsNumber.SERVER_PORT);
        new Thread(serverHandler,"Server").start();
    }

    public static void main(String[] args){
        AioServer.start();
    }
}
