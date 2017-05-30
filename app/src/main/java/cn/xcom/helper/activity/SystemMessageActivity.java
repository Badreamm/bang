package cn.xcom.helper.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.xcom.helper.R;

/**
 * Created by zhuchongkun on 16/6/16.
 * 系统消息页
 */
public class SystemMessageActivity extends BaseActivity implements View.OnClickListener{
    private String TAG="SystemMessageActivity";
    private Context mContext;
    private RelativeLayout rl_back;
    private TextView tv_content;
    private Button bt_update;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_system_message);
        mContext=this;
        initView();
    }

    private void initView(){
        rl_back= (RelativeLayout) findViewById(R.id.rl_system_message_back);
        rl_back.setOnClickListener(this);
        tv_content= (TextView) findViewById(R.id.tv_system_message_content);
        bt_update= (Button) findViewById(R.id.bt_system_message_order);
        bt_update.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_system_message_back:
                finish();
                break;
            case R.id.bt_system_message_order:
                Toast.makeText(mContext,"未开发",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
