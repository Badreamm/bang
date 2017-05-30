package cn.xcom.helper.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;


import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import cn.xcom.helper.R;

/**
 * Created by asus on 2016-05-06.
 */
public class MyImageLoader {
    private static DisplayImageOptions options=
            new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.morenimage)
                    .showImageOnFail(R.mipmap.morenimage)
                    .showImageForEmptyUri(R.mipmap.morenimage)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .resetViewBeforeLoading(true)
                    .build();

    public static void display(String uri,ImageView imageView){
        ImageLoader.getInstance().displayImage(uri,imageView,options);
    }
    public static void displayBg(String uri,ImageView imageView){
//        ImageLoader.getInstance().displayImage(uri,imageView,options);
        ImageLoader.getInstance().displayImage(uri, new BgImageViewAware(imageView));
    }
    public static void displayForLocal(String localPath,ImageView imageView){
        ImageLoader.getInstance().displayImage("file://"+localPath,imageView,options);

    }
    public static void imgLoaderInit(Context context){
        ImageLoaderConfiguration config=new ImageLoaderConfiguration.Builder(context)
                               .denyCacheImageMultipleSizesInMemory()
                               .threadPoolSize(3)//线程池大小3
                               .threadPriority(Thread.NORM_PRIORITY)//线程的优先级
                               .memoryCacheSize((int) (Runtime.getRuntime().maxMemory()/8))//是设置内存的缓存大小
                               .diskCacheSize(50*1024*1024)//在硬盘（sd卡）中的缓存大小
                               .diskCacheFileNameGenerator(new Md5FileNameGenerator())//加密方式
                               .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                               .imageDownloader(new BaseImageDownloader(context,50000,50000))
                               .diskCache(new UnlimitedDiskCache(
                                       FileUitlity.getInstance(context).makeDir("imgCache")))
                               .build();
        ImageLoader.getInstance().init(config);
    }
}
