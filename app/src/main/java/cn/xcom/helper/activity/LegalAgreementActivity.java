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
 * 法律协议页
 */
public class LegalAgreementActivity extends BaseActivity implements View.OnClickListener {
    private String TAG="LegalAgreementActivity";
    private Context mContext;
    private RelativeLayout rl_back;
    private TextView tv_content;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_legal_agreement);
        mContext=this;
        initView();

    }
    private void initView(){
        rl_back= (RelativeLayout) findViewById(R.id.rl_legal_agreement_back);
        rl_back.setOnClickListener(this);
        tv_content= (TextView) findViewById(R.id.tv_legal_agreement_content);
        tv_content.setText(ToolUtil.readFromAsset(mContext, "51LegalAgreement.txt"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_legal_agreement_back:
                finish();
                break;
        }
    }
}
