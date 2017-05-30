package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.adapter.ShopReleaseAdapter;
import cn.xcom.helper.bean.Front;
import cn.xcom.helper.bean.ShopGoodInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ToastUtils;
import cn.xcom.helper.wheel.widget.TosAdapterView;

public class ShopReleaseActivity extends BaseActivity implements View.OnClickListener{
    private ListView listView;
    private RelativeLayout relativeLayout;
    private String userid="";
    private List<ShopGoodInfo> shop_list;
    private Context context;
    private List<Front>choose_list;
    private ShopReleaseAdapter shopReleaseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_release);
        initView();

        chooseGood();

    }
    public  void initView(){
        context=this;
        shop_list=new ArrayList<>();
        choose_list=new ArrayList<>();
        relativeLayout= (RelativeLayout) findViewById(R.id.back);
        relativeLayout.setOnClickListener(this);
        listView= (ListView) findViewById(R.id.shop_list);
        Intent intent=getIntent();
        userid=intent.getStringExtra("userid");

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();

        }
    }

    //得到商家id从而得到商家发布的商品
    public void chooseGood(){
        String url= NetConstant.SHOP_GOOD;
        StringPostRequest request=new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("dataxiamain", userid+"==");
                    Log.d("data","hahahaha");
                    Log.d("datas",s);
                    JSONObject jsonObject=new JSONObject(s);
                    String status=jsonObject.getString("status");
                    if (status.equals("success")){
                        String data=jsonObject.getString("data");
                        Log.d("datadata",data);
                        Gson gson=new Gson();
                        choose_list=gson.fromJson(data,
                                new TypeToken<ArrayList<Front>>() {
                                }.getType());
                        Log.d("data",choose_list.size()+"");
                        shopReleaseAdapter=new ShopReleaseAdapter(choose_list,context);
                        listView.setAdapter(shopReleaseAdapter);
                        shopReleaseAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(context,"网络错误，请检查网络");
            }
        });
        request.putValue("userid", userid);
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }

}
