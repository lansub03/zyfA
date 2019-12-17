package bio;

import config.UtilsNumber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class BioClient {
    public static void main(String[] args) throws IOException {
        /**\
         * 1、先new一个Socket，可以空参构造，
         * 参数1：ip String格式
         * 参数2： 端口
         */
        Socket socket = new Socket(UtilsNumber.SERVER_IP, UtilsNumber.SERVER_PORT);
        System.out.println("请求输入消息:");

        //启动读取输入流的信息线程
        ReadMsg readMsg = new ReadMsg(socket);
        readMsg.start();

        PrintWriter pw = null;
        while (true){
            pw = new PrintWriter(socket.getOutputStream());
            //讲内容发送给服务器端
            String readValue = new Scanner(System.in).next();
            System.out.println("键盘输入的内容为："+readValue);
            pw.println(readValue);
            pw.flush();//刷新输出流
        }
    }

    private static class ReadMsg extends Thread{
        Socket socket;
        public ReadMsg(Socket socket) {
            this.socket = socket;
        }
        @Override
        public void run() {
            /**
             * try()用法：
             *  try块退出时，会自动执行br.close()方法，关闭资源。
             */
            try(BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream())))
            {
                String line = null;
                    while (true){
                        line=br.readLine();
                        System.out.printf(line);
                        if (line == null){
                             break;
                        }
                    }


            }catch (Exception e){
                System.out.println();
            }finally {
                clearSocket();//关闭Socket
            }
        }
        //关闭Sockert
        private void clearSocket(){
            if (socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
