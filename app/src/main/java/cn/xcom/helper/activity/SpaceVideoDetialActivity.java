package cn.xcom.helper.activity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.kaopiz.kprogresshud.KProgressHUD;

import butterknife.ButterKnife;
import cn.xcom.helper.R;
import cn.xcom.helper.record.AudioPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import static cn.xcom.helper.R.*;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class SpaceVideoDetialActivity extends BaseActivity {

    //    private JCVideoPlayerStandard videoView;
    private VideoView videoView;
    ImageView videoback;
    private String videoUri;
    SensorManager sensorManager;
    JCVideoPlayer.JCAutoFullscreenListener sensorEventListener;
    private MediaController mediaController;
    Context context;
    private KProgressHUD hud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_video);

        context = this;

        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
        videoView = (VideoView) findViewById(id.space_videoplayer);

        videoUri = getIntent().getStringExtra("videoUrl");
        videoback = (ImageView) findViewById(id.video_back);
        videoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (!videoUri.equals("")) {
            play(videoUri);
        }
        hud.show();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()

        {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START)
//                            videoView.setBackgroundColor(Color.TRANSPARENT);
                        hud.dismiss();
                        return true;
                    }
                });

            }
        });
    }

    private void play(final String path) {

        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        mediaController.setKeepScreenOn(true);

        videoView.setMediaController(mediaController);
        Uri uri = null;
        uri = Uri.parse(path);
        videoView.setVideoURI(uri);
        videoView.start();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }


}