package cn.xcom.helper.utils;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class MsgUtils {
    public static String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/download_img/";

    //得到视频缩略图
    public static Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        }
        catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
        finally {
            try {
                retriever.release();
            }
            catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    //获取视频时长
    public static String getDuration(String path){
        String duration;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return duration;
    }

    //保存图片到本地
    public static void saveFile(Bitmap bm, String imgName) {
        File dirFile = new File(ALBUM_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }

        File myFile = new File(ALBUM_PATH + imgName);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
