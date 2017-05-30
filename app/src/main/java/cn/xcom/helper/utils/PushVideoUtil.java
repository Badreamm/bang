package cn.xcom.helper.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.util.Log;

import com.android.internal.http.multipart.FilePart;
import com.android.internal.http.multipart.Part;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.xcom.helper.constant.NetConstant;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class PushVideoUtil {
    private static String videoName;
    private static String picName;
    private PushVideo pushVideo;
    private String videoPath;
    private String fileName, imageName;
    private Context context;
    private File image;
    private static String imgPath;

    public void pushVideo(Context context, String videoPath, PushVideo pushVideo) throws IOException {
        this.pushVideo = pushVideo;
        this.videoPath = videoPath;
        this.context = context;
        image = getImgFirstVideo();
        newVideoFile();
    }

    public static String getVideoName() {
        return videoName;
    }

    public static String getPicName() {
        return picName;
    }

    private File getImgFirstVideo() {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(videoPath);
        Bitmap bitmap = mmr.getFrameAtTime();
        mmr.release();
        return convertBitmap(bitmap);
    }

    public File convertBitmap(Bitmap bitmap) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "hyxvideo");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        Random random = new Random();
        imageName = "yyy" + random.nextInt(10000) + System.currentTimeMillis() + ".jpg";
        Log.d("---imgpath", appDir.getPath() + imageName);
        imgPath = appDir.getPath() + "/" + imageName;
        File file = new File(appDir, imageName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void newVideoFile() throws IOException {
        File appDir = new File(Environment.getExternalStorageDirectory(), "hyxvideo");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        Random random = new Random();
        int length = videoPath.length();
        String str = videoPath.substring(length - 4, length);
        fileName = "yyy" + random.nextInt(10000) + System.currentTimeMillis() + str;
        File file = new File(appDir, fileName);
        File oldFile = new File(videoPath);
        nioTransferCopy(oldFile, file);
//        oldFile.renameTo(file);
        upDateVideo(file);
        Log.d("-----oldvideo", oldFile.getPath() + oldFile.getName());
        Log.d("-----newvideo", file.getPath() + file.getName());
    }

    private void upDateVideo(final File file) {

        new Thread() {

            @Override
            public void run() {
                List<Part> list = new ArrayList<Part>();
                try {
                    list.add(new FilePart("upfile", file));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                String url1 = NetConstant.UPLOAD_RECORD;
                VolleyRequest request = new VolleyRequest(url1, list.toArray(new Part[list.size()]), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        if (JsonResult.JSONparser(context, s)) {
                            try {
                                JSONObject j = new JSONObject(s);
                                videoName = j.getString("data");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            upDateVideoImage(image);

                        } else {
                            pushVideo.error();
                        }
                        Log.d("===  video上传1", s);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        pushVideo.error();
                    }
                });
                SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
            }
        }.start();

    }

    private void upDateVideoImage(final File file) {

        if (file == null) {
            ToastUtil.showShort(context, "获取第一帧图片失败");
            pushVideo.error();
            return;
        }
        new Thread() {

            @Override
            public void run() {
                List<Part> list = new ArrayList<Part>();
                try {
                    list.add(new FilePart("upfile", file));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                String url1 = NetConstant.NET_UPLOAD_IMG;
                VolleyRequest request = new VolleyRequest(url1, list.toArray(new Part[list.size()]), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        if (JsonResult.JSONparser(context, s)) {
                            try {
                                JSONObject j = new JSONObject(s);
                                picName = j.getString("data");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            pushVideo.success(fileName, imageName);
                        } else {
                            pushVideo.error();
                        }
                        Log.d("===  video上传", s);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        pushVideo.error();
                    }
                });
                SingleVolleyRequest.getInstance(context).addToRequestQueue(request);

            }
        }.start();
    }

    //    **
//            * 复制单个文件
//* @param oldPath String 原文件路径 如：c:/fqf.txt
//* @param newPath String 复制后路径 如：f:/fqf.txt
//* @return boolean
//*/
    private static void nioTransferCopy(File source, File target) throws IOException {
        FileChannel in = null;
        FileChannel out = null;
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            inStream = new FileInputStream(source);
            outStream = new FileOutputStream(target);
            in = inStream.getChannel();
            out = outStream.getChannel();
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert inStream != null;
            inStream.close();
            assert in != null;
            in.close();
            outStream.close();
            assert out != null;
            out.close();

        }
    }

}
