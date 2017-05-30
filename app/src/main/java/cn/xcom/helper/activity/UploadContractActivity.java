package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import cn.xcom.helper.R;

/**
 * Created by zhuchongkun on 16/6/4.
 * 上传合同页
 */
public class UploadContractActivity extends BaseActivity implements View.OnClickListener {
    private String TAG="UploadContractActivity";
    private Context mContext;
    private RelativeLayout rl_back;
    private ImageView iv_photo;
    private Button bt_skip,bt_upload;
    private String price,tradeNumber;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_upload_contract);
        mContext=this;
        getInfo();
        initView();
    }

    /**
     * 获取上页传来的任务价格和订单号
     */
    private void getInfo() {
        price = getIntent().getStringExtra("price");
        tradeNumber = getIntent().getStringExtra("tradeNo");
    }

    private void initView(){
        rl_back= (RelativeLayout) findViewById(R.id.rl_upload_contract_back);
        rl_back.setOnClickListener(this);
        iv_photo= (ImageView) findViewById(R.id.iv_upload_contract_photo);
        iv_photo.setOnClickListener(this);
        bt_skip= (Button) findViewById(R.id.bt_upload_contract_skip);
        bt_skip.setOnClickListener(this);
        bt_upload= (Button) findViewById(R.id.bt_upload_contract_upload);
        bt_upload.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_upload_contract_back:
                finish();
                break;
            case R.id.iv_upload_contract_photo:
                Toast.makeText(mContext,"未开发",Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_upload_contract_skip:
                Intent intent = new Intent(mContext, PaymentActivity.class);
                intent.putExtra("price",price);
                intent.putExtra("tradeNo",tradeNumber);
                startActivity(intent);
                break;
            case R.id.bt_upload_contract_upload:
                Toast.makeText(mContext,"未开发",Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
