package cn.xcom.helper.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
 * Created by zhuchongkun on 16/5/27.
 * 修改密码页
 */
public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener{
    private String TAG="ResetPasswordActivity";
    private Context mContext;
    private RelativeLayout rl_back;
    private EditText et_phone,et_password,et_password_confirm,et_verification;
    private TextView tv_getVerification;
    private ImageView iv_password,iv_password_confirm;
    private Button bt_submit;
    private int i=120;
    private static final int CODE_ONE=5;
    private static final int CODE_TWO=6;
    private Handler handler=new Handler(Looper.myLooper()){
        public void handleMessage(Message msg){
            switch (msg.what){
                case CODE_ONE:
                    tv_getVerification.setText("重发("+i+")");
                    break;
                case CODE_TWO:
                    tv_getVerification.setText("重新发送");
                    tv_getVerification.setClickable(true);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reset_password);
        mContext=this;
        initView();

    }
    private void initView(){
        rl_back= (RelativeLayout) findViewById(R.id.rl_reset_password_back);
        rl_back.setOnClickListener(this);
        et_phone= (EditText) findViewById(R.id.et_reset_password_phone);
        et_verification= (EditText) findViewById(R.id.et_reset_password_verification);
        et_password= (EditText) findViewById(R.id.et_reset_password_password);
        et_password_confirm= (EditText) findViewById(R.id.et_reset_password_password_confirm);
        tv_getVerification= (TextView) findViewById(R.id.tv_reset_password_verification_get);
        tv_getVerification.setOnClickListener(this);
        bt_submit= (Button) findViewById(R.id.bt_reset_password_submit);
        bt_submit.setOnClickListener(this);
        iv_password= (ImageView) findViewById(R.id.iv_reset_password_password);
        iv_password.setOnClickListener(this);
        iv_password_confirm= (ImageView) findViewById(R.id.iv_reset_password_password_confirm);
        iv_password_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_reset_password_back:
                finish();
                break;
            case R.id.tv_reset_password_verification_get:
                i=120;
                checkPhone();
                break;
            case R.id.iv_reset_password_password:
                //记住光标开始的位置
                int pos = et_password.getSelectionStart();
                if(et_password.getInputType()!= (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)){//隐藏密码
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_password.setImageResource(R.mipmap.ic_close_eyes);
                }else{//显示密码
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_password.setImageResource(R.mipmap.ic_open_eyes);
                }
                et_password.setSelection(pos);
                break;
            case R.id.iv_reset_password_password_confirm:
                //记住光标开始的位置
                int pos_confirm = et_password_confirm.getSelectionStart();
                if(et_password_confirm.getInputType()!= (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)){//隐藏密码
                    et_password_confirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_password_confirm.setImageResource(R.mipmap.ic_close_eyes);
                }else{//显示密码
                    et_password_confirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_password_confirm.setImageResource(R.mipmap.ic_open_eyes);
                }
                et_password_confirm.setSelection(pos_confirm);
                break;
            case R.id.bt_reset_password_submit:
                resetPassword();
                break;

        }

    }
    private void checkPhone(){
//        http://bang.xiaocool.net/index.php?g=apps&m=index&a=checkphone&phone=18653503680
        String phone=et_phone.getText().toString().trim();
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
                            Toast.makeText(mContext,"手机号未被注册！",Toast.LENGTH_LONG).show();
                        }else if(state.equals("success")){
                            getVerification();
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
    private void getVerification(){
        tv_getVerification.setClickable(false);
        tv_getVerification.setText("重发("+i+")");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (;i>0;i--){
                    handler.sendEmptyMessage(CODE_ONE);
                    if (i<0){
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(CODE_TWO);
            }
        }).start();
//      http://bang.xiaocool.net/index.php?g=apps&m=index&a=SendMobileCode&phone=18653503680
        String phone=et_phone.getText().toString().trim();
        if (!RegexUtil.checkMobile(phone)) {
            Toast.makeText(mContext,"请正确输入手机号！",Toast.LENGTH_LONG).show();
            return;
        }
        RequestParams params=new RequestParams();
        params.put("phone",phone);
        HelperAsyncHttpClient.get(NetConstant.NET_GET_CODE,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+response.toString());
                if (response!=null){
                    try {
                        String state=response.getString("status");
                        if (state.equals("success")){
                            JSONObject jsonObject=response.getJSONObject("data");
                            String code=jsonObject.getString("code");
                            LogUtils.e(TAG,"--code->"+code);
                            Toast.makeText(mContext,"发送成功，请注意接受！",Toast.LENGTH_LONG).show();
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
    private void resetPassword(){
//        http://bang.xiaocool.net/index.php?g=apps&m=index&a=forgetpwd&phone=18653503680&code=726189&password=1234
        String phone=et_phone.getText().toString().trim();
        String password=et_password.getText().toString().trim();
        String password_confirm=et_password_confirm.getText().toString().trim();
        String verification=et_verification.getText().toString().trim();
        if (!RegexUtil.checkMobile(phone)) {
            Toast.makeText(mContext,"请正确输入手机号！",Toast.LENGTH_LONG).show();
            return;
        }
        if (verification==null||verification.length()<=0){
            Toast.makeText(mContext,"请输入验证码！！",Toast.LENGTH_LONG).show();
            return;
        }
        if (password.length()<6){
            Toast.makeText(mContext,"密码至少6位！",Toast.LENGTH_LONG).show();
            return;
        }
        if(!password.equals(password_confirm)){
            Toast.makeText(mContext,"两次密码输入不一致！",Toast.LENGTH_LONG).show();
            return;
        }
        RequestParams params=new RequestParams();
        params.put("phone",phone);
        params.put("code",verification);
        params.put("password",password);
        HelperAsyncHttpClient.get(NetConstant.NET_RESET_PASSWORD,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+response.toString());
                if (response!=null){
                    try {
                        String state=response.getString("status");
                        if (state.equals("success")){
                            Toast.makeText(mContext,"密码修改成功！",Toast.LENGTH_LONG).show();
                            finish();
                        }else if(state.equals("error")){
                            String error=response.getString("data");
                            Toast.makeText(mContext,error,Toast.LENGTH_LONG).show();
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
