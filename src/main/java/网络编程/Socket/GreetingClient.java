package 网络编程.Socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class GreetingClient {

    public static void main(String[] args) {
         String serverNmae = "localhost";
         int port = 8082;

        System.out.println("连接到主机：" + serverNmae + " ，端口号：" + port);
        try {
            Socket client = new Socket(serverNmae, port);//创建Socket对象
            System.out.println("远程主机地址"+client.getRemoteSocketAddress());//获取远程Socket信息
            OutputStream outToServer  = client.getOutputStream();//获取输出流
            DataOutputStream out = new DataOutputStream(outToServer);

            out.writeUTF("你好我叫轩轩");//写入信息
            InputStream inFromServer = client.getInputStream();//获取输入流
            DataInputStream in = new DataInputStream(inFromServer);
            System.out.println("服务器响应： " + in.readUTF());
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
