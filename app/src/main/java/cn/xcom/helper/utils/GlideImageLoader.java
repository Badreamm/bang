package cn.xcom.helper.utils;

import android.content.Context;
import android.os.Build;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;

/**
 * Created by hzh on 2017/6/13.
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Glide.with(context).load(path).into(imageView).onLoadFailed(new Exception(), HelperApplication.getInstance().getDrawable(R.drawable.adv));
        }
    }
}
