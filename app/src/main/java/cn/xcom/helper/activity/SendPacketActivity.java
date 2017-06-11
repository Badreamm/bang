package cn.xcom.helper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ToastUtil;
import cn.xcom.helper.utils.ToastUtils;

/**
 * Created by hzh on 2017/6/11.
 */

public class SendPacketActivity extends BaseActivity {
    private TextView showTv,submitTv,closeTv;
    private EditText moneyEt,numEt;
    private String messagePrice;//便民圈价格，不需要时为0；
    private String mId,tradeNo;//便民圈id
    private UserInfo userInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_packet);
        mId = getIntent().getStringExtra("mid");
        messagePrice = getIntent().getStringExtra("messagePrice");

        userInfo = new UserInfo(this);
        moneyEt = (EditText) findViewById(R.id.et_money);
        numEt = (EditText) findViewById(R.id.et_num);
        showTv = (TextView) findViewById(R.id.tv_show);
        submitTv = (TextView) findViewById(R.id.tv_submit);
        closeTv = (TextView) findViewById(R.id.tv_close);

        moneyEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showTv.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        closeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPay();
            }
        });
    }

    private void goPay() {
        String money = moneyEt.getText().toString();
        String num = numEt.getText().toString();
        if(TextUtils.isEmpty(money) || "0".equals(money)){
            ToastUtils.showToast(this,"金额不能空");
            return;
        }
        if(TextUtils.isEmpty(num) || "0".equals(num)){
            ToastUtils.showToast(this,"份数不能为空");
        }
        if(Integer.valueOf(num) > 60){
            ToastUtils.showToast(this,"最多为60个");
        }

        String url = NetConstant.SEND_PACKET;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("---price", s);
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.optString("status").equals("success")) {
                        //W表示红包后接红包id，Z表示红包和便民圈支付后接便民圈id
                        if (messagePrice == null || messagePrice.equals("0")){//只发红包
                            messagePrice = "0";
                            tradeNo = "W"+object.optString("data");
                        }else{
                            tradeNo = "Z"+mId;
                        }
                        Intent payIntent = new Intent(SendPacketActivity.this, PaymentActivity.class);
                        payIntent.putExtra("paytype", "ConveniencePay");
                        payIntent.putExtra("tradeNo", tradeNo);
                        payIntent.putExtra("body", "便民圈");
                        int price = Integer.valueOf(messagePrice) + Integer.valueOf(moneyEt.getText().toString());
                        //TODO 改为传进来的值 测试只是显示用
                        //        payIntent.putExtra("price",String.valueOf(price));
                        payIntent.putExtra("price","0.01");
                        payIntent.putExtra("type", "3");
                        startActivity(payIntent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplication(), "网络错误，检查您的网络", Toast.LENGTH_SHORT).show();
            }
        });
        request.putValue("mid",mId);
        request.putValue("userid", userInfo.getUserId());
        request.putValue("money",money);
        request.putValue("count",num);
        SingleVolleyRequest.getInstance(getApplication()).addToRequestQueue(request);


    }

}
