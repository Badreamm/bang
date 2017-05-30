package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.bean.ShopGoodInfoNew;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;

public class BuyActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout back;
    private TextView buy_phone;
    private TextView buy_price,tv_name,tv_good_name,address_choose;
    private Button btnDecrease;
    private EditText etAmount;
    private Button btnIncrease;
    private TextView buy_total;
    private TextView buy_deliver;
    private EditText buy_message;
    private TextView adress;
    private LinearLayout buy_address;
    private RelativeLayout buy_commit;
    private ShopGoodInfoNew shopGoodInfo;
    private Context context;
    private UserInfo userInfo;
    int amount=1;//初始化购买数量
    private TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (etAmount.getText().toString().equals("")||etAmount.getText().equals("0")){
                etAmount.setText("1");
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
            buy_total.setText((Double.parseDouble(etAmount.getText().toString()) *
                    Double.parseDouble(buy_price.getText().toString().substring(1))) + "");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        context = this;
        userInfo = new UserInfo(context);
        userInfo.readData(context);
        initView();
        Intent intent=getIntent();
        shopGoodInfo= (ShopGoodInfoNew) intent.getSerializableExtra("price");
        buy_price.setText("￥" + shopGoodInfo.getPrice());
        buy_total.setText(shopGoodInfo.getPrice());
        buy_deliver.setText(shopGoodInfo.getDelivery());
        tv_name.setText(userInfo.getUserName());
        tv_good_name.setText(shopGoodInfo.getGoodsname());
        buy_phone.setText(userInfo.getUserPhone());
    }

    private void initView() {
        address_choose = (TextView) findViewById(R.id.address_choose);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_good_name = (TextView) findViewById(R.id.tv_good_name);
        back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(this);
        buy_phone = (TextView) findViewById(R.id.buy_phone);
        buy_price = (TextView) findViewById(R.id.buy_price);
        btnDecrease = (Button) findViewById(R.id.btnDecrease);
        etAmount = (EditText) findViewById(R.id.etAmount);
        btnIncrease = (Button) findViewById(R.id.btnIncrease);
        buy_total = (TextView) findViewById(R.id.buy_total);
        buy_deliver = (TextView) findViewById(R.id.buy_deliver);
        buy_message = (EditText) findViewById(R.id.buy_message);
        adress = (TextView) findViewById(R.id.adress);
        buy_address = (LinearLayout) findViewById(R.id.buy_address);
        buy_address.setOnClickListener(this);
        buy_commit = (RelativeLayout) findViewById(R.id.buy_commit);
        buy_commit.setOnClickListener(this);
        btnDecrease.setOnClickListener(this);
        btnIncrease.setOnClickListener(this);
        etAmount.addTextChangedListener(textWatcher);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDecrease:
                if(amount>1){
                    amount= Integer.parseInt(String.valueOf(etAmount.getText()));
                    amount--;
                    etAmount.setText(amount + "");
             buy_total.setText( (Double.parseDouble(etAmount.getText().toString())*
                Double.parseDouble(buy_price.getText().toString().substring(1)))+"");
                }
                break;
            case R.id.btnIncrease:
                amount= Integer.parseInt(String.valueOf(etAmount.getText()));
                amount++;
                etAmount.setText(amount+"");
                break;
            case R.id.buy_commit:
                submit();
//                Intent intent=new Intent(BuyActivity.this,PaymentActivity.class);
//                intent.putExtra("price", buy_total.getText().toString());
//                startActivity(intent);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.buy_address:
                Intent intent1=new Intent(BuyActivity.this,AddressListActivity.class);
                intent1.putExtra("from",1);
                startActivityForResult(intent1, 0x110);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!=RESULT_CANCELED){
            switch (requestCode) {
                case 0X110:
                    if (data != null) {
                        address_choose.setText(data.getStringExtra("address"));
                    }
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void submit() {
        // validate
        String etAmountString = etAmount.getText().toString().trim();
        if (TextUtils.isEmpty(etAmountString)) {
            Toast.makeText(this, "数量不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String message = buy_message.getText().toString().trim();
/*        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "您可以在此留言", Toast.LENGTH_SHORT).show();
            return;
        }*/
        String address = address_choose.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "请选择地址", Toast.LENGTH_SHORT).show();
            return;
        }
        //提交商品订单
        String url = NetConstant.SUBMIT_GOOD_ORDER;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("===特卖订单", s);
                Toast.makeText(getApplication(), "订单生成", Toast.LENGTH_SHORT).show();
                JSONObject object = null;
                try {
                    object = new JSONObject(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String orderNumber = object.optString("data");
                HelperApplication.getInstance().type="";
                Intent intent = new Intent(BuyActivity.this,PaymentActivity.class);
                intent.putExtra("price", buy_total.getText().toString());
                intent.putExtra("tradeNo", orderNumber);
                intent.putExtra("body",shopGoodInfo.getGoodsname());
                intent.putExtra("type","2");
                startActivity(intent);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplication(), "网络错误，检查您的网络", Toast.LENGTH_SHORT).show();
            }
        });
        request.putValue("userid",userInfo.getUserId());
        request.putValue("goodsid",shopGoodInfo.getId());
        request.putValue("goodnum",etAmountString);
        request.putValue("mobile",buy_phone.getText().toString());
        request.putValue("remark",message);
        request.putValue("money",buy_total.getText().toString());
        request.putValue("delivery",buy_deliver.getText().toString());
        request.putValue("address",address);
        SingleVolleyRequest.getInstance(getApplication()).addToRequestQueue(request);

    }
}
