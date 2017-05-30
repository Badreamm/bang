package cn.xcom.helper.utils;


import android.util.Log;

import cn.xcom.helper.constant.HelperConstant;

/**
 * Created by zhuchongkun on 16/5/27.
 */
public class LogUtils{
    public static void i(String tag,String message){
        if (HelperConstant.isLog){
            Log.i(tag,message);
        }
    }
    public static void d(String tag,String message){
        if (HelperConstant.isLog){
            Log.d(tag,message);
        }

    }
    public static void e(String tag,String message){
        if (HelperConstant.isLog){
            Log.e(tag,message);
        }
    }
    public static void v(String tag,String message){
        if (HelperConstant.isLog){
            Log.v(tag,message);
        }
    }
    public static void w(String tag,String message){
        if (HelperConstant.isLog){
            Log.w(tag,message);
        }
    }


}
