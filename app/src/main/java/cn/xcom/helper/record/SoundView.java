package cn.xcom.helper.record;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import cn.xcom.helper.R;

/**
 * Created by hzh on 17/1/17.
 * 需要加入麦克风权限 否则录不到声音 且没有数据
 */

public class SoundView extends RelativeLayout {
    public static final int COUNT_CODE = 111;
    public static final int COMPLETE_CODE = 222;
    private boolean isShowDelete;
    private String filePath;
    private View view;
    private TextView countNumTv;
    private ImageView deleteBtn, animationImg;
    private AnimationDrawable animationDrawable;//播放动画
    private boolean isLocal;//是否为本地资源
    private int soundTime;//音频总长度
    private int countNum;
    private Timer timer;
    private TimerTask timerTask;
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case COUNT_CODE:
                    countNumTv.setText(countNum + "s");
                    break;
                case COMPLETE_CODE:
                    countNumTv.setText(soundTime + "s");
                    break;
            }
        }
    };

    public SoundView(Context context) {
        super(context);
        initView(context);
    }

    public SoundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SoundView);

        isShowDelete = a.getBoolean(R.styleable.SoundView_show_delete, false);
        initView(context);
        a.recycle();
    }

    private void initView(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.view_sound, this, true);
        deleteBtn = (ImageView) view.findViewById(R.id.delete_img);
        animationImg = (ImageView) view.findViewById(R.id.animation_img);
        countNumTv = (TextView) view.findViewById(R.id.num_text);

        if (isShowDelete) {
            deleteBtn.setVisibility(View.VISIBLE);
        } else {
            deleteBtn.setVisibility(View.GONE);
        }
        deleteBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

        animationImg.setImageResource(R.drawable.animation_sound);
        animationDrawable = (AnimationDrawable) animationImg.getDrawable();
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AudioPlayer.isPlaying){
                    stopPlay();
                }else{
                    startPlay();
                }
            }
        });

    }

    public void init(String filePath, int soundTime) {
        this.filePath = filePath;
        this.soundTime = soundTime;
        countNumTv.setText(soundTime + "s");
    }


    private void startPlay() {
        countNum = soundTime;
        timer = new Timer(true);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (countNum > 0) {
                    countNum--;
                    handler.sendEmptyMessage(COUNT_CODE);
                } else {
                    stopPlay();
                }
            }
        };
        AudioPlayer.getInstance().startPlay(filePath);
        animationDrawable.start();
        timer.schedule(timerTask, 1000, 1000);
    }

    private void stopPlay() {
        animationDrawable.stop();
        AudioPlayer.getInstance().stopPlay();
        handler.sendEmptyMessage(COMPLETE_CODE);
        if (timer != null) {
            timer.cancel();
        }
        if (timerTask != null) {
            timerTask.cancel();
        }
    }

    public void delete() {
        if (AudioPlayer.isPlaying) {
            stopPlay();
        }
        view.setVisibility(View.GONE);
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }


    public int getSoundTime() {
        return soundTime;
    }

}
