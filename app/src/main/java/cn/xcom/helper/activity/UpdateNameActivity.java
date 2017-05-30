package cn.xcom.helper.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.LogUtils;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun.
 */
public class UpdateNameActivity extends BaseActivity implements View.OnClickListener {
    private String TAG="UpdateNameActivity";
    private RelativeLayout rl_back;
    private TextView tv_save;
    private EditText et_name;
    private Context mContext;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_update_name);
        mContext=this;
        initView();
    }

    private void initView() {
        rl_back= (RelativeLayout) findViewById(R.id.rl_update_name_back);
        rl_back.setOnClickListener(this);
        tv_save= (TextView) findViewById(R.id.tv_update_name_save);
        tv_save.setOnClickListener(this);
        et_name= (EditText) findViewById(R.id.et_update_name);
        userInfo=new UserInfo(mContext);
        if (userInfo!=null&&userInfo.getUserName()!=null){
            et_name.setText(userInfo.getUserName());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_update_name_back:
                finish();
                break;
            case R.id.tv_update_name_save:
                toUpdateName();
                break;
        }

    }
    private void toUpdateName() {
        final String name= et_name.getText().toString();
        RequestParams params =new RequestParams();
        params.put("userid",userInfo.getUserId());
        params.put("nicename",name);
        HelperAsyncHttpClient.get(NetConstant.NET_UPDATE_NAME,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+response.toString());
                if (response!=null){
                    try {
                        String state=response.getString("status");
                        if (state.equals("success")){
                            userInfo.setUserName(name);
                            userInfo.writeData(mContext);
                            Toast.makeText(mContext,"修改成功！",Toast.LENGTH_SHORT);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
