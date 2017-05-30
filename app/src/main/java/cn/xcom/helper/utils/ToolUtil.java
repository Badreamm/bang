package cn.xcom.helper.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhuchongkun on 16/6/28.
 */
public class ToolUtil {
    private static final String TAG="ToolUtil";

    /**
     * 检测网络是否可用
     * @param context
     * @return
     */
    public static boolean isNetWorkConnected(Context context){
        if (context!=null){
            ConnectivityManager manager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo=manager.getActiveNetworkInfo();
            if (mNetworkInfo!=null){
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 检测Sdcard是否存在
     * @return
     */
    public static boolean isExitsSdcard(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 读取asset文件
     * @param context
     * @param textName
     * @return
     */
    public static String readFromAsset(Context context,String textName){
        try {
            StringBuffer stringBuffer=new StringBuffer();
            InputStream inputStream=context.getAssets().open(textName);
            int size=inputStream.available();
            int len=-1;
            byte[] buf=new byte[size];
            while ((len=inputStream.read(buf))!=-1){
                stringBuffer.append(new String(buf,0,len,"utf-8"));
            }
            inputStream.close();
            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }
    }

}
