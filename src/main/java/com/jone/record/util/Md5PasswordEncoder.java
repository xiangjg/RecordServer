package com.jone.record.util;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5PasswordEncoder {
    public static String encrypt(String value, String key) throws NoSuchAlgorithmException {
        MessageDigest message = MessageDigest.getInstance("MD5");
        message.update(key.getBytes());
        message.update(value.getBytes());
        byte[] data = message.digest();
        return new Base64().encodeToString(data);//new sun.misc.BASE64Encoder().encode(data);
    }

    public static void main(String[] args){
        try {
            System.out.println(encrypt("123456","admin"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
