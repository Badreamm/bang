package cn.xcom.helper.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import cn.xcom.helper.R;

/**
 * ImageView创建工厂
 */
public class ViewFactory {

    /**
     * 获取ImageView视图的同时加载显示url
     *
     * @return
     */
    public static ImageView getImageView(Context context, String url) {
        ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(
                R.layout.view_banner, null);
        ImageLoader.getInstance().displayImage(url, imageView);
        imageView.setClickable(true);
        return imageView;
    }
    public static ImageView getImageViewforid(Context context, int id) {
        ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(
                R.layout.view_banner, null);
        imageView.setClickable(false);
//        ImageLoader.getInstance().displayImage(url, imageView);
        imageView.setImageResource(id);
        return imageView;
    }
}
