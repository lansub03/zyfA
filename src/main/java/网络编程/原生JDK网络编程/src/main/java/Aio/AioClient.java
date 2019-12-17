package Aio;

import config.UtilsNumber;

import java.util.Scanner;

/***
 * 类说明：AIO模型的客户端
 */
public class AioClient {
    private static AioClientHandler clientHandler;

    public static void start(){
        //如果不为null，直接return掉
        if (clientHandler != null) return;
        clientHandler = new AioClientHandler(UtilsNumber.SERVER_IP,UtilsNumber.SERVER_PORT);
        new Thread(clientHandler,"Client").start();//启动客户端
    }

    //向服务器发送消息
    public static boolean sendMsg(String msg){
        if (msg.equals("q"))return false;
        clientHandler.sendMsg(msg);
        return true;
    }

    public static void main(String[] args) {
        AioClient.start();
        System.out.println("请求输入消息：");
        Scanner scanner = new Scanner(System.in);
        while (AioClient.sendMsg(scanner.nextLine()));
    }
}
