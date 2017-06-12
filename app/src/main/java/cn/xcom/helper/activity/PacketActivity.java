package cn.xcom.helper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.Packet;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ToastUtil;

/**
 * Created by hzh on 2017/6/12.
 */

public class PacketActivity extends BaseActivity {
    private ImageView chaiIv,closeIv;
    private String packetId;
    private UserInfo userInfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packet);
        packetId = getIntent().getStringExtra("packetid");
        userInfo = new UserInfo(this);

        chaiIv = (ImageView) findViewById(R.id.iv_chai);
        closeIv = (ImageView) findViewById(R.id.iv_close);

        chaiIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPacket();
            }
        });

        closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getPacket(){
        String url = NetConstant.TOUCH_PACKET;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        String date = jsonObject.getString("data");
                        Gson gson = new Gson();
                        Packet packet = gson.fromJson(date,Packet.class);
                        Intent intent = new Intent(PacketActivity.this, PacketDetailActivity.class);
                        intent.putExtra("packet",packet);
                        intent.putExtra("from","touch");
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.Toast(PacketActivity.this, "网络错误，请检查");
            }
        });
        request.putValue("userid", userInfo.getUserId());
        request.putValue("packetid", packetId);
        SingleVolleyRequest.getInstance(this).addToRequestQueue(request);
    }
}
