package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.share.sdk.openapi.APAPIFactory;
import com.alipay.share.sdk.openapi.APMediaMessage;
import com.alipay.share.sdk.openapi.APWebPageObject;
import com.alipay.share.sdk.openapi.IAPApi;
import com.alipay.share.sdk.openapi.SendMessageToZFB;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xcom.helper.R;
import cn.xcom.helper.WXpay.Constants;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.HelperConstant;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.MyImageLoader;
import cn.xcom.helper.utils.SPUtils;
import cn.xcom.helper.utils.ToastUtils;
import cn.xcom.helper.view.SharePopupWindow;

import static com.tencent.connect.share.QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;

public class ShareQRCodeActivity extends BaseActivity {
    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.rl_share)
    RelativeLayout rlShare;
    @BindView(R.id.main_view)
    View mainView;
    /*@BindView(R.id.webView)
    WebView webView;*/
    @BindView(R.id.tv_referral)
    TextView tvReferral;
    @BindView(R.id.ll_jizhi)
    LinearLayout llJizhi;
    @BindView(R.id.tv_jizhi)
    TextView tvJizhi;
    private Context context;
    SharePopupWindow takePhotoPopWin;
    IWXAPI msgApi;
    private UserInfo userInfo;
    private int wxflag = 0;
    private String url;
    private BaseUiListener listener;
    private Tencent mTencent;
    Resources res;
    Bitmap bitmap;
    String thumbPath;
    private ImageView iv_qrcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_share_qrcode);
        ButterKnife.bind(this);
        context = this;
        msgApi = WXAPIFactory.createWXAPI(context, Constants.APP_ID, false);
        msgApi.registerApp(Constants.APP_ID);
        userInfo = new UserInfo(context);
        userInfo.readData(context);
        setData();

        mTencent = Tencent.createInstance("1105802480", this.getApplicationContext());
        listener = new BaseUiListener();

        res=getResources();
        bitmap=BitmapFactory.decodeResource(res, R.drawable.ic_logo);
        thumbPath = convertIconToString(bitmap);
    }

    /**
     * 显示数据
     */
    private void setData() {
        iv_qrcode = (ImageView) findViewById(R.id.webView);
        url = NetConstant.MY_QR_CODE + "&uid=" + userInfo.getUserId();
        MyImageLoader.display(url,iv_qrcode);
        tvJizhi.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvJizhi.getPaint().setAntiAlias(true);//抗锯齿
        tvReferral.setText(SPUtils.get(context, HelperConstant.MY_REFERAL, "").toString());
        /*webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });*/

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
                    ToastUtils.showToast(ShareQRCodeActivity.this, "微信好友");
                    setting();
                    break;
                case R.id.dongtai:
                    ToastUtils.showToast(ShareQRCodeActivity.this, "微信朋友圈");
                    history();
                    break;
                case R.id.qq:
                    ToastUtils.showToast(ShareQRCodeActivity.this, "QQ");
                    shareToQQ();
                    takePhotoPopWin.dismiss();
                    break;
                case R.id.kongjian:
                    ToastUtils.showToast(ShareQRCodeActivity.this, "QQ空间");
                    shareToQzone();
                    takePhotoPopWin.dismiss();
                    break;
                case R.id.zhifubao:
                    ToastUtils.showToast(ShareQRCodeActivity.this, "支付宝");
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

    private void toAlipay() {
        //创建工具对象实例，此处的APPID为上文提到的，申请应用生效后，在应用详情页中可以查到的支付宝应用唯一标识
        IAPApi api = APAPIFactory.createZFBApi(getApplicationContext(), "2016083001821606", false);
        APWebPageObject webPageObject = new APWebPageObject();
        webPageObject.webpageUrl = NetConstant.SHARE_QRCODE_H5 + userInfo.getUserId();

        //组装分享消息对象
        APMediaMessage mediaMessage = new APMediaMessage();
        mediaMessage.title = "我注册了51bang，来加入吧";
        mediaMessage.description = "基于同城个人，商户服务 。商品购买。给个人，商户提供交流与服务平台";
        mediaMessage.mediaObject = webPageObject;
        mediaMessage.setThumbImage(bitmap);
        //将分享消息对象包装成请求对象
        SendMessageToZFB.Req req = new SendMessageToZFB.Req();
        req.message = mediaMessage;
        req.transaction = "WebShare"+String.valueOf(System.currentTimeMillis());
        //发送请求
        api.sendReq(req);

    }


    private void shareToQQ() {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "我注册了51bang，来加入吧");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "基于同城个人，商户服务 。商品购买。给个人，商户提供交流与服务平台");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, NetConstant.SHARE_QRCODE_H5 + userInfo.getUserId());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://www.my51bang.com/uploads/ic_logo.png");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "51帮");
        mTencent.shareToQQ(this, params, listener);
    }

    private void shareToQzone() {
        Bundle params = new Bundle();
        //分享类型
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "我注册了51bang，来加入吧");//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "基于同城个人，商户服务 。商品购买。给个人，商户提供交流与服务平台");//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, NetConstant.SHARE_QRCODE_H5 + userInfo.getUserId());//必填
        ArrayList<String> images = new ArrayList<>();
        images.add("http://www.my51bang.com/uploads/ic_logo.png");
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, images);
        mTencent.shareToQzone(ShareQRCodeActivity.this, params, listener);
    }



    @OnClick({R.id.back, R.id.rl_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.rl_share:
                showPopFormBottom(view);
                break;
        }
    }

    @OnClick(R.id.ll_jizhi)
    public void onClick() {
        startActivity(new Intent(context, VIPArticleActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, listener);
    }


    private class BaseUiListener implements IUiListener
    {
        @Override
        public void onCancel() {
            Toast.makeText(ShareQRCodeActivity.this, "取消分享", Toast.LENGTH_SHORT)
                    .show();
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(ShareQRCodeActivity.this, uiError.errorMessage + "\n" +uiError.errorDetail,
                    Toast.LENGTH_SHORT)
                    .show();
            Log.d("QQshare",uiError.errorMessage + "\n" +uiError.errorDetail);
        }

        @Override
        public void onComplete(Object o) {
//            enableAction(enableActionShareQRCodeActivity.this.action);
        }
    }

    public  String convertIconToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);

    }
}
