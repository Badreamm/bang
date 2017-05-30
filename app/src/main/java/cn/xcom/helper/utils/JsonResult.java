package cn.xcom.helper.utils;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class JsonResult {

    private String result;



    public static <T> List<T> JsonParser(String result) {
        String data = "";
        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<T>>() {
        }.getType());
    }

    public JsonResult() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断返回成功失败
     *
     * @param context
     * @param result
     * @return
     */
    public static Boolean JSONparser(Context context, String result) {
        Boolean flag = true;
        try {
            JSONObject json = new JSONObject(result);
            if (json.getString("status").equals("success")) {
                flag = true;
            } else if (json.getString("status").equals("error")) {
                flag = false;
                ToastUtil.Toast(context, json.getString("data"));
            }

        } catch (JSONException e) {
            flag = false;
        }
        return flag;
    }


}
