package com.shiro.admin.shiro_admin.utils;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * 基于shiro实现密码加密
 */
public class ShiroPassUtil {
    /**
     * shiro中MD5加密参数说明
     * 参数1：加密的方式
     * 参数2：加密的字符串
     * 参数3：加密的中的盐
     * 参数4: 加密的次数
     */
    public static String md5Pass(String pass,String yan , Integer count){
        SimpleHash md5 = new SimpleHash("MD5",pass,yan,count);
        String s = md5.toString();//拿到string字符串
        //以Base64方式进行返回
        return md5.toBase64();
    }

    public static String mad5Pass(String pass){
        return md5Pass(pass,"xuan",1024);
    }

    //生成AES的密钥
    public static String createAESKey(){
        AesCipherService aesCipherService = new AesCipherService();
        aesCipherService.setKeySize(256);
        Key key =  aesCipherService.generateNewKey();
        return new String(Base64.encode(key.getEncoded()));
    }

    //AES加密
    public static String AESEnc (String key ,String msg){
        AesCipherService aesCipherService = new AesCipherService();
        byte[] decode = Base64.decode(key);
        SecretKeySpec keySpec = new SecretKeySpec(decode, "AES");
        String str = aesCipherService.encrypt(msg.getBytes(), keySpec.getEncoded()).toBase64();
        return str;
    }

    //AES解密
    public static String AESDoc (String key ,String msg){
        AesCipherService aesCipherService = new AesCipherService();
        byte[] decode = Base64.decode(key);
        SecretKeySpec keySpec = new SecretKeySpec(decode, "AES");
        String str = new String(aesCipherService.decrypt(Base64.decode(msg), keySpec.getEncoded()).getBytes());
        return str;
    }


}
