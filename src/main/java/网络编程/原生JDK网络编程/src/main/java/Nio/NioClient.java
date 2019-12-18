package Nio;

import config.UtilsNumber;

import java.util.Scanner;

/**
 * 类说明：nio通信客户端
 */
public class NioClient {
    private static NioClientHandle nioclientHandle;

    public static  void start(){
        if (nioclientHandle != null)nioclientHandle.stop();

        nioclientHandle = new NioClientHandle(UtilsNumber.SERVER_IP,UtilsNumber.SERVER_PORT);
        new Thread(nioclientHandle,"Client").start();
    }

    //向服务器发送消息
    public static boolean sendMsg(String msg) throws Exception {
        nioclientHandle.sendMsg(msg);
        return true;
    }
    public static void main(String[] args) throws Exception {
        start();
        Scanner scanner = new Scanner(System.in);
        while (NioClient.sendMsg(scanner.next()));

    }}
