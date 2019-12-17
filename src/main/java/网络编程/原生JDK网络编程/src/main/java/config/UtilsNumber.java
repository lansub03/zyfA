package config;

import java.util.Date;

public class UtilsNumber {
    public final static String SERVER_IP = "192.168.1.168";//端口
    public final static Integer SERVER_PORT = 6666;//端口

    public static String responseCode(String value){
        return "Hello,"+ value + ",Now is" + new Date(System.currentTimeMillis()).toString();
    }
}
