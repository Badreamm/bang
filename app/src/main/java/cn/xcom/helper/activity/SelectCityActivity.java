package cn.xcom.helper.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;

import cn.xcom.helper.R;

/**
 * Created by zhuchongkun on 16/6/4.
 * 选择城市页
 */
public class SelectCityActivity extends BaseActivity implements View.OnClickListener{
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select_city);
    }

    @Override
    public void onClick(View v) {

    }
}
