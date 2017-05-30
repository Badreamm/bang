package cn.xcom.helper.record;

import android.media.MediaPlayer;

import java.io.File;
import java.io.IOException;

/**
 * Created by hzh on 17/1/18.
 */

public class AudioPlayer {
    public static MediaPlayer mediaPlayer;
    public static boolean isPlaying;
    public static AudioPlayer instance;

    public static AudioPlayer getInstance() {
        if (instance == null) {
            instance = new AudioPlayer();
        }
        return instance;
    }


    public void startPlay(String filePath) {
        if (isPlaying) {
            stopPlay();
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.release();
                }
            });
            isPlaying = true;
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopPlay() {
        isPlaying = false;

        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
            } catch (IllegalStateException e) {
                // TODO 如果当前java状态和jni里面的状态不一致，
                //e.printStackTrace();
                mediaPlayer = null;
                mediaPlayer = new MediaPlayer();
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }

    interface onStopListener {
        void onStop();
    }

    interface onStartListener {
        void onStart();
    }
}
