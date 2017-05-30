package cn.xcom.helper.utils;

import android.os.Build;

/**
 * Created by Administrator on 2016/10/21 0021.
 */
public class GetUniqueNumber {
    private static GetUniqueNumber instance = null;
    private GetUniqueNumber(){

    }
    public static GetUniqueNumber getInstance(){
        if(instance == null){
            synchronized (GetUniqueNumber.class){
                if(instance == null){
                    instance = new GetUniqueNumber();
                }
            }
        }
        return instance;
    }

    public String getNumber(){
        String m_szDevIDShort = "35" +
                Build.BOARD.length()%10+ Build.BRAND.length()%10 +

                Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +

                Build.DISPLAY.length()%10 + Build.HOST.length()%10 +

                Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +

                Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +

                Build.TAGS.length()%10 + Build.TYPE.length()%10 +

                Build.USER.length()%10 ; //13 位
        return m_szDevIDShort;
    }
}

