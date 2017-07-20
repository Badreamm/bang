package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.share.sdk.openapi.APAPIFactory;
import com.alipay.share.sdk.openapi.APMediaMessage;
import com.alipay.share.sdk.openapi.APWebPageObject;
import com.alipay.share.sdk.openapi.IAPApi;
import com.alipay.share.sdk.openapi.SendMessageToZFB;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.youth.banner.Banner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.WXpay.Constants;
import cn.xcom.helper.bean.OybGood;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.GlideImageLoader;
import cn.xcom.helper.utils.MyImageLoader;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.TimeUtils;
import cn.xcom.helper.utils.ToastUtils;
import cn.xcom.helper.view.DividerItemDecoration;
import cn.xcom.helper.view.QuantityView;
import cn.xcom.helper.view.SharePopupWindow;

import static com.tencent.connect.share.QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;

/**
 * Created by hzh on 2017/7/8.
 */

public class OybGoodDetailActivity extends BaseActivity {
    private XRecyclerView xRecyclerView;
    private Banner banner;
    private TextView needTv, nowTv, countTv, title;
    private String mark;
    private List<OybGood.Comment> comments;
    private ProgressBar progressBar;
    private QuantityView quantityView;
    private TextView buyTv;
    private OybGood oybGood;
    private LinearLayout shareTv;
    SharePopupWindow takePhotoPopWin;
    private UserInfo userInfo;
    private int wxflag = 0;
    IWXAPI msgApi;
    Bitmap bitmap;
    Resources res;
    private Tencent mTencent;
    private BaseUiListener listener;
    String thumbPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_oyb_good_detail);
        mark = getIntent().getStringExtra("mark");
        initView();
        userInfo = new UserInfo(this);
        msgApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        msgApi.registerApp(Constants.APP_ID);
        res = getResources();
        bitmap = BitmapFactory.decodeResource(res, R.drawable.ic_logo);
        mTencent = Tencent.createInstance("1105802480", this.getApplicationContext());
        listener = new BaseUiListener();

        res = getResources();
        bitmap = BitmapFactory.decodeResource(res, R.drawable.ic_logo);
        thumbPath = convertIconToString(bitmap);
    }

    private void initView() {
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        xRecyclerView = (XRecyclerView) findViewById(R.id.recycler_view);
        View headView = LayoutInflater.from(this).inflate(R.layout.headviewt_oyb_good_detail, null);
        shareTv = (LinearLayout) headView.findViewById(R.id.share_tv);
        shareTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopFormBottom(v);
            }
        });
        banner = (Banner) headView.findViewById(R.id.banner);
        needTv = (TextView) headView.findViewById(R.id.need_count_tv);
        nowTv = (TextView) headView.findViewById(R.id.now_count_tv);
        countTv = (TextView) headView.findViewById(R.id.left_count_tv);
        title = (TextView) headView.findViewById(R.id.title);
        progressBar = (ProgressBar) headView.findViewById(R.id.progress_bar);
        quantityView = (QuantityView) findViewById(R.id.quantity_view);
        buyTv = (TextView) findViewById(R.id.buy_tv);
        buyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buy();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setLayoutManager(linearLayoutManager);
        xRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getNewData();
            }

            @Override
            public void onLoadMore() {
                xRecyclerView.loadMoreComplete();
            }
        });
        xRecyclerView.addHeaderView(headView);
        getNewData();
    }

    private void getNewData() {
        String url = NetConstant.GET_OYB_DETAIL;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String state = jsonObject.getString("status");
                    if (state.equals("success")) {
                        JSONObject j = jsonObject.getJSONObject("data");
                        if (j.opt("smeta").equals("")) {
                            j.put("smeta", null);
                        }
                        String data = j.toString();
                        oybGood = new Gson().fromJson(data, new TypeToken<OybGood>() {
                        }.getType());
                        comments = oybGood.getComments();
                        xRecyclerView.setAdapter(new CommentAdapter());
                        title.setText(oybGood.getTitle());
                        needTv.setText("总需人数：" + oybGood.getPrice() + "人次");
                        nowTv.setText("已参与人数：" + oybGood.getCount() + "人次");
                        int letf = Integer.valueOf(oybGood.getPrice()) - Integer.valueOf(oybGood.getCount());
                        quantityView.setMaxQuantity(letf);
                        countTv.setText("剩余：" + letf + "人次");

                        if (!oybGood.getCount().equals("0")) {
                            int count = Integer.valueOf(oybGood.getCount());
                            int all = Integer.valueOf(oybGood.getPrice());
                            DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
                            //可以设置精确几位小数
                            df.setMaximumFractionDigits(1);
                            //模式 例如四舍五入
                            df.setRoundingMode(RoundingMode.HALF_UP);
                            double accuracy_num = count / Double.valueOf(all) * 100;
                            progressBar.setProgress((int) accuracy_num);
                        }
                        List<String> images = new ArrayList<>();
                        if (oybGood.getSmeta() != null && oybGood.getSmeta().size() > 0) {
                            for (int i = 0; i < oybGood.getSmeta().size(); i++) {
                                images.add(NetConstant.NEW_IMG_DISPLAY + oybGood.getSmeta().get(i).getUrl());
                            }
                            //设置图片加载器
                            banner.setImageLoader(new GlideImageLoader());
                            //设置图片集合
                            banner.setImages(images);
                            //banner设置方法全部调用完毕时最后调用
                            banner.start();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                xRecyclerView.refreshComplete();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(OybGoodDetailActivity.this, "网络连接错误，请检查您的网络");
                xRecyclerView.refreshComplete();
            }
        });
        request.putValue("mark", mark);
        SingleVolleyRequest.getInstance(OybGoodDetailActivity.this).addToRequestQueue(request);

    }

    private void buy() {
        int count = quantityView.getQuantity();
        if (count == 0) {
            ToastUtils.showToast(OybGoodDetailActivity.this, "请选择商品数量");
            return;
        }
        String url = NetConstant.BUY_OYB_GOOD;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String state = jsonObject.getString("status");
                    if (state.equals("success")) {
                        String orderNumber = jsonObject.optString("data");
                        HelperApplication.getInstance().type = "";
                        Intent intent = new Intent(OybGoodDetailActivity.this, PaymentActivity.class);
                        intent.putExtra("price", "0.01");//以后改为count
                        intent.putExtra("tradeNo", orderNumber);
                        intent.putExtra("body", "一元购");
                        intent.putExtra("type", "7");
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.showToast(OybGoodDetailActivity.this, "提交订单失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(OybGoodDetailActivity.this, "网络连接错误，请检查您的网络");
            }
        });
        request.putValue("id", oybGood.getId());
        request.putValue("userid", new UserInfo(OybGoodDetailActivity.this).getUserId());
        request.putValue("price", String.valueOf(count));
        SingleVolleyRequest.getInstance(OybGoodDetailActivity.this).addToRequestQueue(request);
    }


    class CommentAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(OybGoodDetailActivity.this).inflate(R.layout.item_oyb_comment, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final OybGood.Comment comment = comments.get(position);
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + comment.getPhoto(), holder.avatarIv);
            holder.nameTv.setText(comment.getName());
            holder.timeTv.setText(TimeUtils.newChangeTime(comment.getCreate_time()));
            holder.contentTv.setText(comment.getContent());
            if (comment.getPic() != null && comment.getPic().size() > 0) {
                holder.gridView.setAdapter(new GridViewAdapter(OybGoodDetailActivity.this, comment.getPic()));
                holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        List<String> addList = new ArrayList<String>();
                        for (int i = 0; i < comment.getPic().size(); i++) {
                            addList.add(NetConstant.NET_DISPLAY_IMG + comment.getPic().get(i).getPictureurl());
                        }
                        Intent intent = new Intent(OybGoodDetailActivity.this, SpaceImageDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("list111", (Serializable) addList);
                        intent.putExtra("position", position);
                        intent.putExtras(bundle);
                        int[] location = new int[2];
                        intent.putExtra("locationX", location[1]);//必须
                        intent.putExtra("locationY", location[0]);//必须
                        intent.putExtra("width", view.getWidth());//必须
                        intent.putExtra("height", view.getHeight());//必须
                        OybGoodDetailActivity.this.startActivity(intent);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView avatarIv;
        private TextView nameTv, timeTv, contentTv;
        private GridView gridView;

        public ViewHolder(View itemView) {
            super(itemView);
            avatarIv = (ImageView) itemView.findViewById(R.id.avatar_iv);
            nameTv = (TextView) itemView.findViewById(R.id.name_tv);
            timeTv = (TextView) itemView.findViewById(R.id.time_tv);
            contentTv = (TextView) itemView.findViewById(R.id.content_tv);
            gridView = (GridView) itemView.findViewById(R.id.grid_view);
        }
    }

    class GridViewAdapter extends BaseAdapter {
        private Context context;
        private List<OybGood.CommentPic> pics;

        public GridViewAdapter(Context context, List<OybGood.CommentPic> pics) {
            this.context = context;
            this.pics = pics;
        }

        @Override
        public int getCount() {
            return pics.size();
        }

        @Override
        public Object getItem(int position) {
            return pics.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GridViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.layout_gridview, null);
                viewHolder = new GridViewHolder();
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.add_image);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (GridViewHolder) convertView.getTag();
            }
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + pics.get(position).getPictureurl(), viewHolder.imageView);
            return convertView;
        }
    }

    class GridViewHolder {
        private ImageView imageView;
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
                    ToastUtils.showToast(OybGoodDetailActivity.this, "微信好友");
                    setting();
                    break;
                case R.id.dongtai:
                    ToastUtils.showToast(OybGoodDetailActivity.this, "微信朋友圈");
                    history();
                    break;
                case R.id.qq:
                    ToastUtils.showToast(OybGoodDetailActivity.this, "QQ");
                    shareToQQ();
                    takePhotoPopWin.dismiss();
                    break;
                case R.id.kongjian:
                    ToastUtils.showToast(OybGoodDetailActivity.this, "QQ空间");
                    shareToQzone();
                    takePhotoPopWin.dismiss();
                    break;
                case R.id.zhifubao:
                    ToastUtils.showToast(OybGoodDetailActivity.this, "支付宝");
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
        webpage.webpageUrl = NetConstant.SHARE_QRCODE_H5 + userInfo.getUserId();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "我参与了51帮夺宝活动，快来一起参加吧，分享有惊喜呦！";
        msg.description = "基于同城个人，商户服务，夺宝活动，商品购买，给个人，商户提供交流与服务平台";
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

    private void toAlipay() {
        //创建工具对象实例，此处的APPID为上文提到的，申请应用生效后，在应用详情页中可以查到的支付宝应用唯一标识
        IAPApi api = APAPIFactory.createZFBApi(getApplicationContext(), "2016083001821606", false);
        APWebPageObject webPageObject = new APWebPageObject();
        webPageObject.webpageUrl = NetConstant.SHARE_QRCODE_H5 + userInfo.getUserId();

        //组装分享消息对象
        APMediaMessage mediaMessage = new APMediaMessage();
        mediaMessage.title = "我参与了51帮夺宝活动，快来一起参加吧，分享有惊喜呦！";
        mediaMessage.description = "基于同城个人，商户服务，夺宝活动，商品购买，给个人，商户提供交流与服务平台";
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
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "我参与了51帮夺宝活动，快来一起参加吧，分享有惊喜呦！");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "基于同城个人，商户服务，夺宝活动，商品购买，给个人，商户提供交流与服务平台");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, NetConstant.SHARE_QRCODE_H5 + userInfo.getUserId());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://www.my51bang.com/uploads/ic_logo.png");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "51帮");
        mTencent.shareToQQ(this, params, listener);
    }

    private void shareToQzone() {
        Bundle params = new Bundle();
        //分享类型
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "我参与了51帮夺宝活动，快来一起参加吧，分享有惊喜呦！");//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "基于同城个人，商户服务，夺宝活动，商品购买，给个人，商户提供交流与服务平台");//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, NetConstant.SHARE_QRCODE_H5 + userInfo.getUserId());//必填
        ArrayList<String> images = new ArrayList<>();
        images.add("http://www.my51bang.com/uploads/ic_logo.png");
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, images);
        mTencent.shareToQzone(OybGoodDetailActivity.this, params, listener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, listener);
    }


    private class BaseUiListener implements IUiListener {
        @Override
        public void onCancel() {
            Toast.makeText(OybGoodDetailActivity.this, "取消分享", Toast.LENGTH_SHORT)
                    .show();
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(OybGoodDetailActivity.this, uiError.errorMessage + "\n" + uiError.errorDetail,
                    Toast.LENGTH_SHORT)
                    .show();
            Log.d("QQshare", uiError.errorMessage + "\n" + uiError.errorDetail);
        }

        @Override
        public void onComplete(Object o) {
//            enableAction(enableActionShareQRCodeActivity.this.action);
        }
    }

    public String convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);

    }
}
