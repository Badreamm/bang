package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.adapter.MyViewPageAdapter;
import cn.xcom.helper.bean.Collection;
import cn.xcom.helper.bean.Front;
import cn.xcom.helper.bean.ShopGoodInfo;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.MyImageLoader;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;

/**
 * 商品详情页
 * 1.我的发布 商品详情
 * 2.商家发布 商品详情
 */
public class ShopActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager vp;
    private ImageView imageView, collect;
    private TextView tvContent, price, adress,buyTv;
    private RelativeLayout backImage;
    private List addViewList;//添加图片的list
    private MyViewPageAdapter viewPageAdapter;
    private Front front;
    private Context context;
    private UserInfo userInfo;
    private List<Collection> addList;
    private Collection collection;
    private int flag = 2;
    private int from = 1;//1我的发布 2商家发布

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        initView();
        Intent intent = getIntent();
        front = (Front) intent.getSerializableExtra("item1");
        from = intent.getIntExtra("from",1);
        tvContent.setText(front.getDescription());
        price.setText("￥" + front.getPrice());
        if(from == 1){
            buyTv.setVisibility(View.GONE);
        }else{
            buyTv.setVisibility(View.VISIBLE);
        }
        ArrayList<String> imgs = new ArrayList<>();
        if (front.getPicturelist().size() > 0) {
            for (int i = 0; i < front.getPicturelist().size(); i++) {
                imgs.add(front.getPicturelist().get(i).getFile());
                imageView = new ImageView(this);
                MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + front.getPicturelist().get(i).getFile(),
                        imageView);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                addViewList.add(imageView);
            }
        } else {
            imgs.add(NetConstant.NET_DISPLAY_IMG);
            imageView = new ImageView(this);
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG, imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            addViewList.add(imageView);
        }
        viewPageAdapter = new MyViewPageAdapter(imgs,addViewList,context);
        vp.setAdapter(viewPageAdapter);
        vp.setCurrentItem(0);
        getCollectedState();
    }

    //初始化控件
    public void initView() {
        context = this;
        userInfo = new UserInfo();
        userInfo.readData(context);
        addList = new ArrayList<>();
        addViewList = new ArrayList();
        vp = (ViewPager) findViewById(R.id.vp);
        tvContent = (TextView) findViewById(R.id.tvContent);
        price = (TextView) findViewById(R.id.price);
        collect = (ImageView) findViewById(R.id.collect);
        collect.setOnClickListener(this);
        backImage = (RelativeLayout) findViewById(R.id.back);
        backImage.setOnClickListener(this);
        buyTv = (TextView) findViewById(R.id.buy);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.collect:
                if (flag == 1) {
                    cancleCollection();
                } else if (flag == 2) {
                    collection();
                }
                break;
        }
    }

    //点击收藏商品
    public void collection() {
        String url = NetConstant.GOOD_COLLECTION;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    String status = jsonObject.getString("status");
                    Log.d("yijingshoucang", status);
                    if (status.equals("success")) {
                        flag = 1;
                        collect.setImageResource(R.mipmap.yijingshoucang);
                        Toast.makeText(context, "收藏成功", Toast.LENGTH_LONG).show();

                    } else {
                        flag = 2;
                        Toast.makeText(context, "收藏失败", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.putValue("userid", userInfo.getUserId());
        Log.d("shoucangid", userInfo.getUserId());
        if (front.getPicturelist().size() == 0) {
            request.putValue("goodsid", "");
        } else {
            request.putValue("goodsid", front.getPicturelist().get(0).getGoodsid());
        }
        //  request.putValue("goodsid",front.getPicturelist().get(0).getGoodsid());
        request.putValue("type", front.getType());
        request.putValue("title", front.getGoodsname());
        request.putValue("description", front.getDescription());
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }

    //取消收藏
    public void cancleCollection() {
        String url = NetConstant.GOOD_CANCLE_COLLECTION;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Log.d("==sou", s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        flag = 2;
                        collect.setImageResource(R.mipmap.shoucang);
                        Toast.makeText(context, "取消收藏", Toast.LENGTH_LONG).show();
                        Log.d("===quxiao", "取消收藏成功");
                    } else {
                        flag = 1;
                        collect.setImageResource(R.mipmap.yijingshoucang);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.putValue("userid", userInfo.getUserId());
        if (front.getPicturelist().size() == 0) {
            request.putValue("goodsid", "");
        } else {
            request.putValue("goodsid", front.getPicturelist().get(0).getGoodsid());
        }

        request.putValue("type", front.getType());
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }

    //判断该商品是否被收藏
    private void getCollectedState() {
        String url = NetConstant.GOOD_IS_COLLECTION;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Log.d("==sou", s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        flag = 1;
                        collect.setImageResource(R.mipmap.yijingshoucang);

                    } else {
                        flag = 2;
                        collect.setImageResource(R.mipmap.shoucang);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.putValue("userid", userInfo.getUserId());
        request.putValue("refid", front.getId());
        request.putValue("type", front.getType());
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }


}
