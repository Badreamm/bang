package cn.xcom.helper.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;

import cn.xcom.helper.R;

/**
 * Created by zhuchongkun on 16/6/4.
 * 定位页
 */
public class LocationActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_location);
    }

    @Override
    public void onClick(View v) {

    }
}
