package bio;

import config.UtilsNumber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BioServerHandle implements Runnable {
    private Socket socket;
    public BioServerHandle(Socket socket){
        this.socket = socket;
    };

    public void run() {
        try(BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true)
        ) {
            String message;
            String result ;
            while ( (message = in.readLine()) != null){
                System.out.println("server appept msgValue:"+ message);
                result = UtilsNumber.responseCode(message);
                out.println(result);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭socket
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
