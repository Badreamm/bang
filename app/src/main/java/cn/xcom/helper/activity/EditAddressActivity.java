package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.City1;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;


/**
 * Created by zhuchongkun on 16/6/17.
 */
public class EditAddressActivity extends BaseActivity implements View.OnClickListener {
    private String TAG = "EditAddressActivity";
    private Context mContext;
    private RelativeLayout rl_back, rl_save;
    private EditText et_detail;
    private LinearLayout ll_city, ll_street;
    private TextView tv_city, tv_street;
    private String provinces;
    private City1 city;
    private ArrayList<City1> toCitys;
    private UserInfo userInfo;
    private CheckBox cb_checked;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_address);
        mContext = this;

        initView();


    }

    private void initView() {
        userInfo = new UserInfo();
        userInfo.readData(mContext);
        city = new City1();
        toCitys = new ArrayList<>();
        rl_back = (RelativeLayout) findViewById(R.id.rl_edit_address_back);
        rl_back.setOnClickListener(this);
        cb_checked = (CheckBox) findViewById(R.id.cb_checked);
        rl_save = (RelativeLayout) findViewById(R.id.rl_edit_address_save);
        rl_save.setOnClickListener(this);
        et_detail = (EditText) findViewById(R.id.et_edit_address_detail);
        tv_city = (TextView) findViewById(R.id.tv_edit_address_city);
        //  tv_street= (TextView) findViewById(R.id.tv_edit_address_street);
        ll_city = (LinearLayout) findViewById(R.id.ll_edit_address_city);
        ll_city.setOnClickListener(this);
        // ll_street= (LinearLayout) findViewById(R.id.ll_edit_address_street);
        // ll_street.setOnClickListener(this);
    }

    private void addAdress() {
        String detailAdress = et_detail.getText().toString().trim();
        if (TextUtils.isEmpty(detailAdress)) {
            Toast.makeText(this, "请填写详细地址", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = NetConstant.ADD_ADRESS;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("==返回的地址", s);
                Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
            }
        });
        request.putValue("userid", userInfo.getUserId());
        request.putValue("address", detailAdress);
        request.putValue("longitude", "123.1232");
        request.putValue("latitude", "123.1232");
        //   request.putValue("isdefault","1");
        SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);
        setResult(9);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_edit_address_back:
                finish();
                break;
            case R.id.rl_edit_address_save:
                addAdress();

                break;
            case R.id.ll_edit_address_city:
                Intent in = new Intent(EditAddressActivity.this, CitySelect1Activity.class);
                in.putExtra("city", city);
                startActivityForResult(in, 1);
                break;
//            case R.id.ll_edit_address_street:
//                Toast.makeText(mContext,"未开发",Toast.LENGTH_SHORT).show();
//                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 8) {
            if (requestCode == 1) {
                city = data.getParcelableExtra("city");
                tv_city.setText(city.getProvince() + city.getCity() + city.getDistrict());

            } else if (requestCode == 2) {
                toCitys = data.getParcelableArrayListExtra("toCitys");
                StringBuffer ab = new StringBuffer();
                for (int i = 0; i < toCitys.size(); i++) {
                    if (i == toCitys.size() - 1) {//如果是最后一个城市就不需要逗号
                        ab.append(toCitys.get(i).getCity());
                    } else {
                        StringBuffer a = ab.append(toCitys.get(i).getCity() + "， ");//如果不是最后一个城市就需要逗号
                        tv_city.setText(a);
                    }
                }
            }
        }
    }
}
