package 网络编程.Socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GreetingServer extends Thread {
    private ServerSocket serverSocket;

    public GreetingServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        //setSotimeout(10000)是表示如果对方连接状态10秒没有收到数据的话强制断开客户端。
        serverSocket.setSoTimeout(10000);
    }

    @Override
    public void run() {
        while (true){
            try {
                System.out.println("等待连接的端口号为:"+serverSocket.getLocalPort());
                Socket server = serverSocket.accept();
                System.out.println("远程主机地址：" + server.getRemoteSocketAddress());

                //接受发送过来的内容
                DataInputStream in = new DataInputStream(server.getInputStream());
                System.out.println("接受的内容为：:"+in.readUTF());

                //回复信息
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                out.writeUTF("谢谢连接我：" + server.getLocalSocketAddress() + "\nGoodbye!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String [] args) {
        int port = 8082;
        try
        {
            Thread t = new GreetingServer(port);
            t.run();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
