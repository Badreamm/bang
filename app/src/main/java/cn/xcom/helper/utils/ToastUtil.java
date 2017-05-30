package cn.xcom.helper.utils;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.Toast;

import cn.xcom.helper.R;


/**
 * Created by Administrator on 2016/3/14.
 */
public class ToastUtil {
    public ToastUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    public static boolean isShow = true;

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (isShow)
            Toast.makeText(context, message, duration).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration) {
        if (isShow)
            Toast.makeText(context, message, duration).show();
    }

    /**
     * toast 工具类 自定义toast
     *
     * @param context
     * @param content
     */
    public static void Toast(Context context, String content) {

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        TextView view = new TextView(context);
//        view.setPadding(CommonUtil.dip2px(40), 0
//                , CommonUtil.dip2px(40), 0);
        view.setBackgroundResource(R.color.font_common);
        view.setTextColor(Color.WHITE);
        view.setTextSize(12);
        view.setText(content);
//		toast.setGravity(Gravity.BOTTOM, 10,0);
        toast.setView(view);
        toast.show();
    }


}
