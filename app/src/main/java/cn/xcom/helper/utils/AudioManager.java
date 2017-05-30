package cn.xcom.helper.utils;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Administrator on 2016/9/26 0026.
 */
public class AudioManager  {
    private MediaRecorder mediaRecorder;
    private String mdir;
    private String mCurrent;
    private boolean isPrepared;
    private static AudioManager mInstance;
    private AudioManager(String dir){
        mdir=dir;
    }

    public static AudioManager getmInstance(String dir){
        if (mInstance==null){
            synchronized (AudioManager.class) {
                if (mInstance == null) {
                    mInstance=new AudioManager(dir);
                }

            }

        }
     return mInstance;
    }

    public void prepareAudio(){
        isPrepared=false;
        File dir=new File(mdir);
        if (!dir.exists()){
            dir.mkdir();
        }

        Random random=new Random();
        String fileName ="aaa"+ random.nextInt(10000)+System.currentTimeMillis() + ".amr";
        File file=new File(dir,fileName);
        mCurrent=file.getAbsolutePath();
        mediaRecorder=new MediaRecorder();
        //设置输出文件
        mediaRecorder.setOutputFile(file.getAbsolutePath());
        //设置mediaRecorder的音频源我们的麦克风
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置音频格式
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
        //设置音频的编码
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            //准备结束
            isPrepared=true;
            if (mListener!=null){
               mListener.wellPrepared();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public int getVoiceLevel(){
        return 1;
    }
    public void release(){
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder=null;

    }
    public void cancle(){
         release();
        if (mCurrent!=null){
            File file=new File(mCurrent);
            file.delete();
            mCurrent=null;
        }
    }
    public interface AudioStateListener{
        void wellPrepared();
    }
    public AudioStateListener mListener;

    public  void setOnAudioStateListener(AudioStateListener mListener) {
        this.mListener = mListener;
    }
}
