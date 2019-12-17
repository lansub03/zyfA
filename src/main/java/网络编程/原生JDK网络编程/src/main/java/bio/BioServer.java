package bio;

import config.UtilsNumber;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BioServer {
    private static ServerSocket serverSocket;
    //线程池
    private static ExecutorService executorService = Executors.newFixedThreadPool(5);

    private static void start() throws IOException {
        try {
            serverSocket = new ServerSocket(UtilsNumber.SERVER_PORT);//设置监听的端口
            System.out.println("服务器已启动，端口号为："+ UtilsNumber.SERVER_PORT);
            //开始接受Client发送过来的信息
            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("有新的客户端连接-------");
                //作为任务投入到线程中
                executorService.execute(new BioServerHandle(socket));
            }
        }finally {
            if (serverSocket != null){
                serverSocket.close();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        start();
    }
}
