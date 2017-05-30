package cn.xcom.helper.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import cn.xcom.helper.R;


/**
 * Created by MuMu on 2016/8/14.
 */
public class PicturePickerDialog extends Dialog implements View.OnClickListener {

    private final Button btn_photograph;
    private final Button btn_from_album;
    private final Button btn_cancel;
    private PicturePickerCallBack callBack;

    public PicturePickerDialog(Context context) {
        super(context, R.style.DialogBottomTheme);
        setContentView(R.layout.dialog_picture_picker);

        btn_photograph = (Button) findViewById(R.id.btn_photograph);
        btn_from_album = (Button) findViewById(R.id.btn_from_album);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        btn_photograph.setOnClickListener(this);
        btn_from_album.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    public void show(PicturePickerCallBack callBack){
        this.callBack = callBack;
        super.show();
    }

    @Override
    public void show() {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        cancel();
        return true;
    }

    public interface PicturePickerCallBack {
        void onPhotoClick();
        void onAlbumClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_photograph:
                callBack.onPhotoClick();
                cancel();
                break;
            case R.id.btn_from_album:
                callBack.onAlbumClick();
                cancel();
                break;
            case R.id.btn_cancel:
                cancel();
                break;
        }
    }
}
