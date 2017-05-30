package cn.xcom.helper.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zhuchongkun.
 */
public class Md5 {
    public static String getMd5(String s){

        try {
            MessageDigest bmd5=MessageDigest.getInstance("MD5");
            bmd5.update(s.getBytes());
            int i;
            StringBuffer buffer=new StringBuffer();
            byte[] b=bmd5.digest();
            for (int offset=0;offset<b.length;offset++){
                i=b[offset];
                if (i<0)
                    i +=256;
                if (i<16)
                    buffer.append("0");
                buffer.append(Integer.toHexString(i));
            }
            return buffer.toString();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return "";
    }
    public final static String getMessageDigest(byte[] buffer){
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        try {
            MessageDigest mdTemp=MessageDigest.getInstance("MD5");
            mdTemp.update(buffer);
            byte[] md=mdTemp.digest();
            int j=md.length;
            char str[]=new char[j*2];
            int k=0;
            for (int i=0;i<j;i++){
                byte byte0=md[i];
                str[k++]=hexDigits[byte0>>>4&0xf];
                str[k++]=hexDigits[byte0&0xf];
            }
            return new String(str);
        }catch (Exception e){
            return null;
        }
    }
}
