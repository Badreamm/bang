package cn.xcom.helper.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xcom.helper.bean.PhotoWithPath;


/**
 * Created by Administrator on 2016/8/25.
 */
public class GetImageUtil {

    private static String filepath = "/sdcard/hongyunschoolimage";

    public static List<PhotoWithPath> getImgWithPaths(List<PhotoInfo> resultList){

        List<PhotoWithPath> photoWithPathList = new ArrayList<>();
        Bitmap bitmap;
        for (PhotoInfo photoInfo : resultList) {
            bitmap = BitmapFactory.decodeFile(photoInfo.getPhotoPath(), getBitmapOption(2));
            photoWithPathList.add(getImageToView(bitmap, photoInfo.getPhotoId()));

        }
        return photoWithPathList;

    }

    private static BitmapFactory.Options getBitmapOption(int inSampleSize) {
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }


    /**
     * 保存图片数据
     */
    private  static PhotoWithPath getImageToView(Bitmap photo, int photoid) {

        PhotoWithPath pwp = new PhotoWithPath();
        if (photo != null) {
            Random random=new Random();
            String picname = "hyx" + random.nextInt(1000) + String.valueOf(new Date().getTime()) + ".jpg";
            String picPath = storeImageToSDCARD(photo, picname, filepath);
            pwp.setPicname(picname);
            pwp.setPicPath(picPath);
            return pwp;
        }
        return pwp;
    }

    /**
     * storeImageToSDCARD 将bitmap存放到sdcard中
     */
    public static String storeImageToSDCARD(Bitmap colorImage, String ImageName, String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        File imagefile = new File(file, ImageName);
        try {
            imagefile.createNewFile();
            FileOutputStream fos = new FileOutputStream(imagefile);
            colorImage.compress(Bitmap.CompressFormat.JPEG, 10, fos);
            fos.flush();
            fos.close();
            return imagefile.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "null";
    }
 }
