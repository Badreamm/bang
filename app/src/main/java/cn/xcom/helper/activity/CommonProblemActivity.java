package cn.xcom.helper.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.xcom.helper.R;
import cn.xcom.helper.utils.ToolUtil;

/**
 * Created by zhuchongkun on 16/6/12.
 * 常见问题页
 */
public class CommonProblemActivity extends BaseActivity implements View.OnClickListener{
    private String TAG="CommonProblemActivity";
    private Context mContext;
    private RelativeLayout rl_back;
    private TextView tv_content;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_common_problem);
        mContext=this;
        initView();
    }

    private void initView(){
        rl_back= (RelativeLayout) findViewById(R.id.rl_common_problem_back);
        rl_back.setOnClickListener(this);
        tv_content= (TextView) findViewById(R.id.tv_common_problem_content);
        tv_content.setText(ToolUtil.readFromAsset(mContext, "51CommonProblem.txt"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_common_problem_back:
                finish();
                break;
        }
    }
}
