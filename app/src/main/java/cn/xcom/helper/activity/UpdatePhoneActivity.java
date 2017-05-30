package cn.xcom.helper.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cn.xcom.helper.R;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.LogUtils;
import cn.xcom.helper.utils.RegexUtil;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 16/6/16.
 * 修改绑定手机页
 */
public class UpdatePhoneActivity extends BaseActivity implements View.OnClickListener {
    private String TAG="UpdatePhoneActivity";
    private Context mContext;
    private RelativeLayout rl_back;
    private TextView tv_get_code;
    private EditText et_old_phone,et_new_phone,et_new_phone_confirm,et_code;
    private Button bt_submit;
    private int i=120;
    private static final int CODE_ONE=5;
    private static final int CODE_TWO=6;

    private Handler handler=new Handler(Looper.myLooper()){
        public void handleMessage(Message msg){
            switch (msg.what){
                case CODE_ONE:
                    tv_get_code.setText("重发("+i+")");
                    break;
                case CODE_TWO:
                    tv_get_code.setText("重新发送");
                    tv_get_code.setClickable(true);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_update_phone);
        mContext=this;
        initView();
    }

    private void initView(){
        rl_back= (RelativeLayout) findViewById(R.id.rl_update_phone_back);
        rl_back.setOnClickListener(this);
        et_old_phone= (EditText) findViewById(R.id.et_update_phone);
        et_new_phone= (EditText) findViewById(R.id.et_update_phone_phone);
        et_new_phone_confirm= (EditText) findViewById(R.id.et_update_phone_phone_confirm);
        et_code= (EditText) findViewById(R.id.et_update_phone_verification);
        tv_get_code= (TextView) findViewById(R.id.tv_update_phone_verification_get);
        tv_get_code.setOnClickListener(this);
        bt_submit= (Button) findViewById(R.id.bt_update_phone_submit);
        bt_submit.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_update_phone_back:
                finish();
                break;
            case R.id.tv_update_phone_verification_get:
                checkPhone();
                break;
            case R.id.bt_update_phone_submit:
                Toast.makeText(mContext,"未开发",Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void checkPhone(){
        String phone=et_old_phone.getText().toString().trim();
        if (!RegexUtil.checkMobile(phone)) {
            Toast.makeText(mContext,"请正确输入手机号！",Toast.LENGTH_LONG).show();
            return;
        }
        RequestParams params=new RequestParams();
        params.put("phone",phone);
        HelperAsyncHttpClient.get(NetConstant.NET_CHECK_PHONE,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+response.toString());
                if (response!=null){
                    try {
                        String state=response.getString("status");
                        if (state.equals("error")){
                            Toast.makeText(mContext,"手机号未注册！",Toast.LENGTH_LONG).show();
                        }else if(state.equals("success")){
                            Toast.makeText(mContext,"手机号已被注册！",Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+responseString);
            }
        });
    }
}
