package cn.xcom.helper.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.share.sdk.openapi.APAPIFactory;
import com.alipay.share.sdk.openapi.APMediaMessage;
import com.alipay.share.sdk.openapi.APWebPageObject;
import com.alipay.share.sdk.openapi.IAPApi;
import com.alipay.share.sdk.openapi.SendMessageToZFB;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.kaopiz.kprogresshud.KProgressHUD;
//import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
//import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
//import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.WXpay.Constants;
import cn.xcom.helper.adapter.MyViewPageAdapter;
import cn.xcom.helper.bean.Collection;
import cn.xcom.helper.bean.ShopGoodInfoNew;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.CommonAdapter;
import cn.xcom.helper.utils.MyImageLoader;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ToastUtil;
import cn.xcom.helper.utils.ToastUtils;
import cn.xcom.helper.utils.ViewHolder;
import cn.xcom.helper.view.SharePopupWindow;

import static com.tencent.connect.share.QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;

public class SaleDetailActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager vp;
    private ImageView imageView, collect;
    private TextView tvContent, price, tvprice, adress, buy, tv_city_name,tv_title;
    private RelativeLayout backImage;
    private RelativeLayout shopPublish;
    private List addViewList;//添加图片的list
    private MyViewPageAdapter viewPageAdapter;
    //    private Front front;
    private ShopGoodInfoNew shopGoodInfo;
    private Context context;
    private UserInfo userInfo;
    private List<Collection> addList;
    private Collection collection;
    private int flag = 2, wxflag = 1;
    SharePopupWindow takePhotoPopWin;
    private RelativeLayout rl_share;
    IWXAPI msgApi;
    private String goodsId;
    private ListView sale_detail_comment;
    private ImageView iv_phone;
    private CommonAdapter<ShopGoodInfoNew.CommentlistBean> adapter;
    private KProgressHUD hud;
    Resources res;
    Bitmap bitmap;
    String thumbPath;
    private BaseUiListener listener;
    private Tencent mTencent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sale_detail);
        context = this;
        msgApi = WXAPIFactory.createWXAPI(context, Constants.APP_ID, false);
        msgApi.registerApp(Constants.APP_ID);
        initView();
        //得到传过来的数据
        Intent intent = getIntent();
        goodsId = intent.getStringExtra("id");
        addGood();

        mTencent = Tencent.createInstance("1105802480", this.getApplicationContext());
        listener = new BaseUiListener();
        res = getResources();
        bitmap = BitmapFactory.decodeResource(res, R.drawable.ic_logo);
        thumbPath = convertIconToString(bitmap);
    }

    /**
     * 启动百度地图驾车路线规划
     */
    public void startRoutePlanDriving(String taskInfoLatitude, String taskInfoLongitude, String taskInfoAddress, String latitude, String longitude, String address) {
        // 起点坐标
        double mLat1 = Double.parseDouble(taskInfoLatitude);
        double mLon1 = Double.parseDouble(taskInfoLongitude);
        // 终点
        double mLat2 = Double.parseDouble(latitude);
        double mLon2 = Double.parseDouble(longitude);
        LatLng pt1 = new LatLng(mLat1, mLon1);
        LatLng pt2 = new LatLng(mLat2, mLon2);
        // 构建 route搜索参数
        RouteParaOption para = new RouteParaOption()
                .startPoint(pt1)
                .startName(taskInfoAddress)
                .endPoint(pt2)
                .endName(address);
        try {
            BaiduMapRoutePlan.setSupportWebRoute(true);
            BaiduMapRoutePlan.openBaiduMapDrivingRoute(para, context);
        } catch (Exception e) {
            e.printStackTrace();
            showDialog();
        }

    }

    /**
     * 提示未安装百度地图app或app版本过低
     */
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(context);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }

    private void setData() {
        tv_title.setText(shopGoodInfo.getGoodsname());
        tvContent.setText(shopGoodInfo.getDescription());
        price.setText("￥" + shopGoodInfo.getPrice());
        tvprice.setText("￥" + shopGoodInfo.getPrice());
        tv_city_name.setText(shopGoodInfo.getAddress());
        if (!shopGoodInfo.getLongitude().equals("") && !shopGoodInfo.getLatitude().equals("") && !String.valueOf(HelperApplication.getInstance().mLocLat).equals("") && !String.valueOf(HelperApplication.getInstance().mLocLon).equals("")) {
            adress.setText((int) DistanceUtil.getDistance(
                    new LatLng(Double.parseDouble(shopGoodInfo.getLatitude()),
                            Double.parseDouble(shopGoodInfo.getLongitude())),
                    new LatLng(HelperApplication.getInstance().mLocLat,
                            HelperApplication.getInstance().mLocLon)) + "米");
            tv_city_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startRoutePlanDriving(String.valueOf(HelperApplication.getInstance().mLocLat), String.valueOf(HelperApplication.getInstance().mLocLon), HelperApplication.getInstance().mLocAddress
                            , shopGoodInfo.getLatitude(), shopGoodInfo.getLongitude(), shopGoodInfo.getAddress());
                }
            });
        }
        Log.e("========shipeiqiwocao", "" + shopGoodInfo.getPicturelist().size());
        ArrayList<String> imgs = new ArrayList<>();
        if (shopGoodInfo.getPicturelist().size() > 0) {
            for (int i = 0; i < shopGoodInfo.getPicturelist().size(); i++) {
                imgs.add(shopGoodInfo.getPicturelist().get(i).getFile());
                imageView = new ImageView(this);
                MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + shopGoodInfo.getPicturelist().get(i).getFile(),
                        imageView);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                addViewList.add(imageView);
            }
        } else {
            imgs.add(NetConstant.NET_DISPLAY_IMG);
            imageView = new ImageView(this);
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG, imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            addViewList.add(imageView);
        }
        iv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + shopGoodInfo.getPhone()));
                context.startActivity(intent);
            }
        });
        viewPageAdapter = new MyViewPageAdapter(imgs, addViewList, context);
        vp.setAdapter(viewPageAdapter);
        vp.setCurrentItem(0);
        setAdapter();
    }

    /**
     * 设置评论适配器
     */
    private void setAdapter() {
        adapter = new CommonAdapter<ShopGoodInfoNew.CommentlistBean>(context, shopGoodInfo.getCommentlist(), R.layout.item_comment_info) {
            @Override
            public void convert(ViewHolder holder, ShopGoodInfoNew.CommentlistBean commentlistBean) {
                holder.setImageByUrl(R.id.iv_avatar, commentlistBean.getPhoto())
                        .setText(R.id.tv_name, commentlistBean.getName())
                        .setTimeText(R.id.tv_time, commentlistBean.getAdd_time())
                        .setText(R.id.tv_content, commentlistBean.getContent());
                RatingBar ratingBar = holder.getView(R.id.rating_bar);
                ratingBar.setNumStars(Integer.valueOf(commentlistBean.getScore()));
            }
        };
        sale_detail_comment.setAdapter(adapter);
    }

    //初始化控件
    public void initView() {
        userInfo = new UserInfo();
        userInfo.readData(context);
        addViewList = new ArrayList();
        addList = new ArrayList<>();
        tv_title = (TextView) findViewById(R.id.tv_title);
        vp = (ViewPager) findViewById(R.id.vp);
        tvContent = (TextView) findViewById(R.id.tvContent);
        collect = (ImageView) findViewById(R.id.collect);
        price = (TextView) findViewById(R.id.price);
        tvprice = (TextView) findViewById(R.id.tvprice);
        adress = (TextView) findViewById(R.id.adress);
        backImage = (RelativeLayout) findViewById(R.id.back);
        backImage.setOnClickListener(this);
        shopPublish = (RelativeLayout) findViewById(R.id.shopPublish);
        shopPublish.setOnClickListener(this);
        buy = (TextView) findViewById(R.id.buy);
        buy.setOnClickListener(this);
        rl_share = (RelativeLayout) findViewById(R.id.rl_share);
        rl_share.setOnClickListener(this);
        tv_city_name = (TextView) findViewById(R.id.tv_city_name);
        sale_detail_comment = (ListView) findViewById(R.id.sale_detail_comment);
        iv_phone = (ImageView) findViewById(R.id.iv_phone);
    }

    //根据商品的id得到商家的id
    public void addGood() {
        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
        hud.show();
        String url = NetConstant.SHOP_INFO;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(hud!=null){
                    hud.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        String data = jsonObject.getString("data");
                        Log.d("data", data);
                        Gson gson = new Gson();
                        shopGoodInfo = gson.fromJson(data,
                                new TypeToken<ShopGoodInfoNew>() {
                                }.getType());
                        setData();
                        judgeIsCollection();
                        //collectionList();
                        Log.d("data1111", shopGoodInfo.getUserid());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(context, "网络错误，请检查网络");
                if(hud!=null){
                    hud.dismiss();
                }
            }
        });
        request.putValue("id", goodsId);
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.shopPublish:
                if (shopGoodInfo.getUserid() == "") {
                    Intent intent = new Intent(SaleDetailActivity.this, ShopReleaseActivity.class);
                    startActivity(intent);
                    ToastUtil.Toast(context, "网络错误，请检查网络");
                } else {
                    Intent intent = new Intent(SaleDetailActivity.this, ShopReleaseActivity.class);
                    intent.putExtra("userid", shopGoodInfo.getUserid());
                    startActivity(intent);
                }

                break;
            case R.id.collect:
                if (flag == 1) {
                    cancleCollection();
                } else if (flag == 2) {
                    collection();
                }
                break;
            case R.id.buy:
                if (userInfo.getUserId().equals(shopGoodInfo.getUserid())) {
                    Toast.makeText(context, "不能购买自己发布的产品", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(context, BuyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("price", shopGoodInfo);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
            case R.id.rl_share:
                showPopFormBottom(v);
                break;
        }
    }

    public void showPopFormBottom(View view) {
        takePhotoPopWin = new SharePopupWindow(this, onClickListener);
        //SharePopupWindow takePhotoPopWin = new SharePopupWindow(this, onClickListener);
        takePhotoPopWin.showAtLocation(findViewById(R.id.rl_bottom), Gravity.BOTTOM, 0, 0);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.haoyou:
                    ToastUtils.showToast(SaleDetailActivity.this, "微信好友");
                    setting();
                    break;
                case R.id.dongtai:
                    ToastUtils.showToast(SaleDetailActivity.this, "微信朋友圈");
                    history();
                    break;
                case R.id.qq:
                    ToastUtils.showToast(SaleDetailActivity.this, "QQ");
                    shareToQQ();
                    takePhotoPopWin.dismiss();
                    break;
                case R.id.kongjian:
                    ToastUtils.showToast(SaleDetailActivity.this, "QQ空间");
                    shareToQzone();
                    takePhotoPopWin.dismiss();
                    break;
                case R.id.zhifubao:
                    ToastUtils.showToast(SaleDetailActivity.this, "支付宝");
                    toAlipay();
                    takePhotoPopWin.dismiss();
                    break;
            }
        }
    };

    /**
     * 微信分享网页
     */
    private void shareWX() {
        //创建一个WXWebPageObject对象，用于封装要发送的Url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = NetConstant.SHARE_SHOP_H5 + userInfo.getUserId();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "我注册了51bang，发布了商品，来加入吧";
        msg.description = "基于同城个人，商户服务 。商品购买。给个人，商户提供交流与服务平台";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo);
        msg.setThumbImage(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "weiyi";
        req.message = msg;
        req.scene = wxflag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        msgApi.sendReq(req);
    }

    /**
     * 分享到微信好友
     */
    private void setting() {
        //ToastUtils.ToastShort(this, "分享到微信好友");
        wxflag = 0;
        shareWX();
        takePhotoPopWin.dismiss();

    }

    /**
     * 分享到微信朋友圈
     */
    private void history() {
        // ToastUtils.ToastShort(this, "分享到微信朋友圈");
        wxflag = 1;
        shareWX();
        takePhotoPopWin.dismiss();
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
        request.putValue("goodsid", shopGoodInfo.getId());
        request.putValue("type", "3");
        request.putValue("title", shopGoodInfo.getGoodsname());
        request.putValue("description", shopGoodInfo.getDescription());
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }

    //判断该商品是否被收藏
    public void judgeIsCollection() {
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
                    collect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (flag == 1) {
                                cancleCollection();
                            } else if (flag == 2) {
                                collection();
                            }
                        }
                    });
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
        request.putValue("refid", shopGoodInfo.getId());
        request.putValue("type", "3");
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }

    //获取收藏列表
    public void collectionList() {
        String url = NetConstant.HAS_COLLECTION;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String data = jsonObject.getString("data");
                    Log.d("==用户data", data);
                    if (data == null) {
                        collection = null;
                        Log.d("==用户是否收藏", "用户没有收藏");
                    } else {
                        Log.d("==用户是否收藏", "用户有收藏");
                        Gson gson = new Gson();
                        addList = gson.fromJson(data,
                                new TypeToken<ArrayList<Collection>>() {
                                }.getType());
                        for (int i = 0; i < addList.size(); i++) {
                            collection = addList.get(i);
                        }
                        judgeIsCollection();
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
        request.putValue("goodsid", shopGoodInfo.getId());
        request.putValue("type", "3");
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }


    private void toAlipay() {
        //创建工具对象实例，此处的APPID为上文提到的，申请应用生效后，在应用详情页中可以查到的支付宝应用唯一标识
        IAPApi api = APAPIFactory.createZFBApi(getApplicationContext(), "2016083001821606", false);
        APWebPageObject webPageObject = new APWebPageObject();
        webPageObject.webpageUrl = NetConstant.SHARE_SHOP_H5 + userInfo.getUserId();

        //组装分享消息对象
        APMediaMessage mediaMessage = new APMediaMessage();
        mediaMessage.title = "我注册了51bang，发布了商品，来加入吧";
        mediaMessage.description = "基于同城个人，商户服务 。商品购买。给个人，商户提供交流与服务平台";
        mediaMessage.mediaObject = webPageObject;
        mediaMessage.setThumbImage(bitmap);
        //将分享消息对象包装成请求对象
        SendMessageToZFB.Req req = new SendMessageToZFB.Req();
        req.message = mediaMessage;
        req.transaction = "WebShare" + String.valueOf(System.currentTimeMillis());
        //发送请求
        api.sendReq(req);

    }


    private void shareToQQ() {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "我注册了51bang，发布了商品，来加入吧");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "基于同城个人，商户服务 。商品购买。给个人，商户提供交流与服务平台");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, NetConstant.SHARE_SHOP_H5 + userInfo.getUserId());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://www.my51bang.com/uploads/ic_logo.png");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "51帮");
        mTencent.shareToQQ(this, params, listener);
    }

    private void shareToQzone() {
        Bundle params = new Bundle();
        //分享类型
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "我注册了51bang，发布了商品，来加入吧");//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "基于同城个人，商户服务 。商品购买。给个人，商户提供交流与服务平台");//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, NetConstant.SHARE_SHOP_H5 + userInfo.getUserId());//必填
        ArrayList<String> images = new ArrayList<>();
        images.add("http://www.my51bang.com/uploads/ic_logo.png");
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, images);
        mTencent.shareToQzone(SaleDetailActivity.this, params, listener);
    }


    public String convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);

    }

    private class BaseUiListener implements IUiListener {
        @Override
        public void onCancel() {
            Toast.makeText(SaleDetailActivity.this, "取消分享", Toast.LENGTH_SHORT)
                    .show();
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(SaleDetailActivity.this, uiError.errorMessage + "\n" + uiError.errorDetail,
                    Toast.LENGTH_SHORT)
                    .show();
            Log.d("QQshare", uiError.errorMessage + "\n" + uiError.errorDetail);
        }

        @Override
        public void onComplete(Object o) {
//            enableAction(enableActionShareQRCodeActivity.this.action);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HelperApplication.getInstance().saleBack = true;
    }
}
