package cn.xcom.helper.record;

import android.os.Environment;

import com.czt.mp3recorder.MP3Recorder;

import java.io.File;
import java.io.IOException;


public class AudioRecorder implements RecordStrategy {

    private String fileName;
    private String fileFolder = Environment.getExternalStorageDirectory()
            .getPath() + "/Record";
    private MP3Recorder mp3Recorder;
    private boolean isRecording = false;

    @Override
    public void ready() {
        File file = new File(fileFolder);
        if (!file.exists()) {
            file.mkdir();
        }
        fileName = getFileName();
        mp3Recorder = new MP3Recorder(new File(fileFolder, fileName));

    }

    // 以当前时间作为文件名
    private String getFileName() {
        String str = "sound" + System.currentTimeMillis() + ".mp3";
        return str;
    }

    @Override
    public void start() {
        if (!isRecording) {
            try {
                mp3Recorder.start();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            isRecording = true;
        }

    }

    @Override
    public void stop() {
        if (isRecording) {
            mp3Recorder.stop();
            isRecording = false;
        }

    }

    @Override
    public void deleteOldFile() {
        File file = new File(fileFolder + "/" + fileName);
//        file.deleteOnExit();
        if(file.exists()){
            file.delete();
        }
    }

    @Override
    public double getAmplitude() {
        if (!isRecording) {
            return 0;
        }
        return mp3Recorder.getVolume();
    }

    @Override
    public String getFilePath() {
        return fileFolder + "/" + fileName;
    }


}
