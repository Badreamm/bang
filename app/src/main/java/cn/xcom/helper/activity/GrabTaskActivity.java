package cn.xcom.helper.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;

import com.kyleduo.switchbutton.SwitchButton;

import cn.xcom.helper.R;

public class GrabTaskActivity extends BaseActivity {
    public static final String TAG = "GrabTaskActivity";
    private Context context;
    private SwipeRefreshLayout srl_task;
    private ListView lv_task;
    private SwitchButton sb_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grab_task);
        context = this;
        initView();
    }

    private void initView() {

    }
}
