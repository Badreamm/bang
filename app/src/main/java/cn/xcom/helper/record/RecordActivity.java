package cn.xcom.helper.record;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import cn.xcom.helper.R;
import cn.xcom.helper.activity.BaseActivity;

/**
 * Created by hzh on 17/1/17.
 */

public class RecordActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_sound);
        RecordButton recordButton = (RecordButton) findViewById(R.id.record_button);
        recordButton.setAudioRecord(new AudioRecorder());
        recordButton.setRecordListener(new RecordButton.RecordListener() {
            @Override
            public void recordEnd(final String filePath) {
                final MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(filePath);
                    mediaPlayer.prepare();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            int time = mediaPlayer.getDuration() / 1000;
                            mediaPlayer.release();
                            Intent intent = new Intent();
                            intent.putExtra("path", filePath);
                            intent.putExtra("time", time);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(RecordActivity.this, "录音储存异常", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
