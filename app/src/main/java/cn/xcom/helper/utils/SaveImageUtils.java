package cn.xcom.helper.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import cn.xcom.helper.R;

/**
 * Created by Administrator on 2017/4/6 0006.
 */

public class SaveImageUtils extends AsyncTask<Bitmap, Void, String> {
    Context context;
    ImageView mImageView;

    public SaveImageUtils(Context context, ImageView imageView) {
        this.mImageView = imageView;
        this.context = context;
    }

    @Override
    protected String doInBackground(Bitmap... params) {
        String result = context.getResources().getString(R.string.save_picture_failed);
        try {
            String sdcard = Environment.getExternalStorageDirectory().toString();
            File file = new File(sdcard + "/51img");
            if (!file.exists()) {
                file.mkdirs();
            }
            Random random = new Random();
            String imageName = "yyy" + random.nextInt(10000) + System.currentTimeMillis() + ".jpg";
            File imageFile = new File(file.getAbsolutePath(), imageName);
            FileOutputStream outStream = null;
            outStream = new FileOutputStream(imageFile);
            Bitmap image = params[0];
            image.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
            result = context.getResources().getString(R.string.save_picture_success, file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        mImageView.setDrawingCacheEnabled(false);
    }
}