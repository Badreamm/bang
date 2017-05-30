package cn.xcom.helper.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import cn.xcom.helper.R;

/**
 * Created by zhuchongkun on 16/6/3.
 * 技能列表页
 */
public class SkillListActivity extends BaseActivity implements View.OnClickListener {
    private String TAG="SkillListActivity";
    private Context mContext;
    private RelativeLayout rl_back;
    private Button bt_submit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_skill_list);
        mContext=this;
        initView();
    }

    private void initView(){
        rl_back= (RelativeLayout) findViewById(R.id.rl_skill_list_back);
        rl_back.setOnClickListener(this);
        bt_submit= (Button) findViewById(R.id.bt_skill_list_submit);
        bt_submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_skill_list_back:
                finish();
                break;
            case R.id.bt_skill_list_submit:
                Toast.makeText(mContext,"未开发",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
