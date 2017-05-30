package cn.xcom.helper.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.http.multipart.FilePart;
import com.android.internal.http.multipart.Part;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xcom.helper.constant.NetConstant;


/**
 * Created by Administrator on 2016/8/25.
 */
public class PushImageUtil {
    private static final int SUCCESS =11;
    private static final int ERROR = 22;
    private static Context mContext;
    private int imgNums = 0;
    private int key = 0;
    private List<PhotoInfo> photoWithPaths;
    private boolean isOk;
    private PushImage pushIamge;
    private List<String> arrayList=new ArrayList<>();
    private boolean needCompress = true;//是否需要压缩

    public void setPushIamge(Context context,List<PhotoInfo> p,List<String> list,PushImage pushIamge) {
        this.photoWithPaths = p;
        this.arrayList=list;
        mContext = context;
        this.pushIamge = pushIamge;
        if (photoWithPaths.size()>0){
            pushImg(photoWithPaths.get(imgNums).getPhotoPath());
        }else {
            pushIamge.error();
        }
    }

    public void setPushIamge(Context context,List<PhotoInfo> p,List<String> list,PushImage pushIamge,boolean needCompress) {
        this.needCompress = needCompress;
        this.setPushIamge(context,p,list,pushIamge);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        pushIamge.success(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case ERROR:
                    pushIamge.error();
                    break;
            }
        }
    };


    public void pushImg(final String picPath){
        if(needCompress){
            convertBitmap(convertToBitmap(picPath, 720, 1280));
        }else{
            compressImageWithRatio(picPath);
        }
    }
    public void updatePhoto(final File f){

        List<Part> list=new ArrayList<Part>();
        try {
            list.add(new FilePart("upfile",f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String url1= NetConstant.NET_UPLOAD_IMG;
        VolleyRequest request=new VolleyRequest(url1, list.toArray(new Part[list.size()]),new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject obj = new JSONObject(s);
                    if(obj!= null){
                        imgNums ++;
                        if(imgNums < photoWithPaths.size()){
                            pushImg(photoWithPaths.get(imgNums).getPhotoPath());
                        }else{
                            handler.sendEmptyMessage(SUCCESS);
                        }
                    }
                    Log.d("===图片张数",imgNums+"");
                } catch (JSONException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(ERROR);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);

    }



    public void convertBitmap(Bitmap bitmap){
        File appDir = new File(Environment.getExternalStorageDirectory(), "51helper");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        Random random=new Random();
        String fileName ="yyy"+ random.nextInt(10000)+System.currentTimeMillis() + ".jpg";
        arrayList.add(fileName);
        Log.d("5555777", arrayList.size()+"");
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            updatePhoto(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void compressImageWithRatio(String srcPath){
        File appDir = new File(Environment.getExternalStorageDirectory(), "51helper");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        Random random=new Random();
        String fileName ="yyy"+ random.nextInt(10000)+System.currentTimeMillis() + ".jpg";
        arrayList.add(fileName);
        File file = new File(appDir, fileName);
        updatePhoto(ImageCompress.compressPicture(srcPath,file));

    }


    //将图片路径转换为bitmap格式
    public Bitmap convertToBitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            // 缩放
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int)scale;
        return BitmapFactory.decodeFile(path, opts);
        //WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
        //return Bitmap.createScaledBitmap(weak.get(), w, h, true);
    }


}
