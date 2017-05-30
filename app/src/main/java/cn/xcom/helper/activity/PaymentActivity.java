package cn.xcom.helper.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
//import com.tencent.mm.sdk.modelpay.PayReq;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.PayNew.OrderInfoUtil2_0;
import cn.xcom.helper.PayNew.PayResult;
import cn.xcom.helper.R;
import cn.xcom.helper.WXpay.Constants;
import cn.xcom.helper.WXpay.MD5;
import cn.xcom.helper.WXpay.Util;
import cn.xcom.helper.bean.OrderHelper;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.fragment.myadvertising.IsReleaseAdvFragment;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ToastUtil;

/**
 * Created by zhuchongkun on 16/6/4.
 * 支付页
 */
public class PaymentActivity extends BaseActivity implements View.OnClickListener {
    private String TAG = "PaymentActivity";
    private Context mContext;
    private RelativeLayout rl_back;
    private LinearLayout cardPay;
    private CheckBox cb_zhifubao, cb_weixin, cb_yinhangka;
    private TextView tv_price;
    private Button bt_submit,bt_cancel;
    private IWXAPI iwxapi;
    private static final int SDK_PAY_FLAG = 1;
    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2016083001821606";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAM2arVc/DULOrVn5VVdkALDETOa1sznxviFaDD/+bZJI+3C7ls1HVZzKNCj7uwKKk02fRnLU70twRMifdhbIwEpqIFQLZW2HXbbif9+74BhetsQiQ/kyzRAhWEqeppNv/KTCtPM5d99S74diQuHHIH0cz4g7Xy9i/9RH8oS315bDAgMBAAECgYEAqpiO/3dXn3kxqRgS0aIuWH1oeX2GKqwE4FOBGpAXhmt8BfwAkm9//8pfISpN7zvgIWXo5Fr9+pA64mQ9bYZA1YDMLxcebn6uRqXZGoa0iZmx0n8/JpTw9L9A0Jt2HBJltrW5vsHqwOkjNL3sPxeeLOnNT9kVRSpp2gRFQuqZPoECQQDvYBpPUOeQ9KC1nouv3ngXOZg0Pw/vJbxaQWwqCAjN/l2m6sBjU6lP2dVB6QSVbD4V6rNABX63PW69uo8V5e1jAkEA2+ImfhcdSM1zS3QnKeyNd0HCKNvXXWXhjnAZ22pHI7tApIexsa/IlbQYNGbL14ZyRD6jq64P2FPwxt4hHUcfIQJAbSBdviz+9GlhXorh2ZJNIyFhjuf05qxIWskae2rgQLCmlzLL9DwuorWG8B4/tbL79tfhUd1vcC/0bVBAbNY+SwJACG2SrCKWrMOzN6EsHx9CDOAoYQiMKLhO/PavBwn70BLNV4Eb/oOOXK6afuexyIEOwC7mdx4k3VXaVMUO3+BqAQJAeMQtg/QEDZJb+frQLOlElYpsUS/J+bASiHHb6j0UTgUYfEtC34oJDd5lX1ZkiaQV5lnGUT8T0da+bzomkZ5xSA==";
    private PayReq req;
    private String price, tradeNumber, tradeNo, body;
    private String type = ""; //1--任务,2--商品 3 便民圈支付 4我的发布支付
    private boolean tag;


    IWXAPI msgApi;
    Map<String, String> resultunifiedorder;
    StringBuffer sb;
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */


                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(PaymentActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//                        if (HelperApplication.getInstance().type.equals("6")) {
//                            HelperApplication.getInstance().advBack = true;
//                            HelperApplication.getInstance().type = "";
//                            finish();
//                            startActivity(new Intent(mContext, MyAdvertsingActivity.class));
//                        } else {
                            if (type.equals("3")) {

                                HelperApplication.getInstance().trendsBack = true;
                                finish();
                            } else if (type.equals("4")) {
                                finish();
                                startActivity(new Intent(mContext, ConvenienceActivity.class));
                            } else if (type.equals("5")) {
                                HelperApplication.getInstance().advBack = true;
                                finish();
//                            startActivity(new Intent(mContext, MyAdvertsingActivity.class));
                            } else if(type.equals("6")){
                                HelperApplication.getInstance().advBack = true;
                                finish();
                                startActivity(new Intent(mContext, MyAdvertsingActivity.class));
                            }else {
                                UpdateTradeState();
                            }

//                        }
                        setResult(113);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PaymentActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 更新任务状态
     */
    private void UpdateTradeState() {
        String url = "";
//        if (type.equals("1")) {
//            url = NetConstant.UPDATETASKPAY;
//        } else if (type.equals("2")) {
//            url = NetConstant.UPDATESHOPPAY;
//        }
//        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                Log.d("更新支付状态", s);
//                finish();
                if (type.equals("2")) {
                    finish();
                    startActivity(new Intent(mContext, MyOrderActivity.class).
                            putExtra("order_type", OrderHelper.BuyerOrder));
                } else if (type.equals("1")) {
                    startActivity(new Intent(mContext, BillActivity.class));
                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                Toast.makeText(getApplication(), "网络错误，检查您的网络", Toast.LENGTH_SHORT).show();
//            }
//        });
//        request.putValue("order_num", tradeNo);
//        request.putValue("paytype", "alipay");
//        SingleVolleyRequest.getInstance(getApplication()).addToRequestQueue(request);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_payment);
        mContext = this;
        sb = new StringBuffer();
        msgApi = WXAPIFactory.createWXAPI(mContext, Constants.APP_ID, false);
        msgApi.registerApp(Constants.APP_ID);
        tag = false;
        getInfo();
        initView();
        checkPayType();
//        if (type != null) {
//            if (type.equals("3") || type.equals("4") || type.equals("5")) {
        cardPay.setVisibility(View.GONE);
        rl_back.setVisibility(View.GONE);
//            }
//        }
    }

    /**
     * 切换支付类型
     */
    private void checkPayType() {
        //1----支付宝
        cb_zhifubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_zhifubao.isChecked()) {
                    cb_weixin.setChecked(false);
                    cb_yinhangka.setChecked(false);
                }
            }
        });
        //2----微信
        cb_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_weixin.isChecked()) {
                    cb_zhifubao.setChecked(false);
                    cb_yinhangka.setChecked(false);
                }
            }
        });
        /*//3----银行卡
        cb_yinhangka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_yinhangka.isChecked()) {
                    cb_zhifubao.setChecked(false);
                    cb_weixin.setChecked(false);
                }
            }
        });*/
    }

    /**
     * 获取上页传来的任务价格和订单号
     */
    private void getInfo() {
        price = getIntent().getStringExtra("price");
        tradeNumber = getIntent().getStringExtra("tradeNo");
        type = getIntent().getStringExtra("type");
        tradeNo = getOutTradeNo(tradeNumber);
        HelperApplication.getInstance().tradeNo = tradeNo;
        HelperApplication.getInstance().payType = type;
        body = getIntent().getStringExtra("body");
    }

    private void initView() {
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_price.setText("¥" + price);
        rl_back = (RelativeLayout) findViewById(R.id.rl_payment_back);
        rl_back.setOnClickListener(this);
        cb_zhifubao = (CheckBox) findViewById(R.id.cb_payment_zhifubao);
        cb_weixin = (CheckBox) findViewById(R.id.cb_payment_weixin);
        cb_yinhangka = (CheckBox) findViewById(R.id.cb_payment_yinhangka);
        cardPay = (LinearLayout) findViewById(R.id.card_pay);
        bt_submit = (Button) findViewById(R.id.bt_payment_submit);
        bt_submit.setOnClickListener(this);
        bt_cancel = (Button) findViewById(R.id.bt_payment_cancel);
        bt_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_payment_back:
                finish();
                break;
            case R.id.bt_payment_submit:
                if (cb_zhifubao.isChecked()) {
                    HelperApplication.getInstance().wxpay = "1";
                    payV2();
                }
                if (cb_weixin.isChecked()) {
                    HelperApplication.getInstance().wxpay = "1";
                    wxpay();
                }
                /*if(cb_yinhangka.isChecked()){
                    WXWebpageObject webpage =new WXWebpageObject();
                    webpage.webpageUrl="www.baidu.com";
                    WXMediaMessage msg =new WXMediaMessage(webpage);
                    msg.title="测试";
                    msg.description="测试";
                    Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.logo_wx);
                    msg.setThumbImage(thumb);
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = "weiyi";
                    req.message = msg;
                    req.scene = SendMessageToWX.Req.WXSceneSession;
                    msgApi.sendReq(req);
                }*/
                break;
            case R.id.bt_payment_cancel:

                if (type.equals("3")) {
                    HelperApplication.getInstance().trendsBack = true;

                    finish();
                } else if (type.equals("4")) {
                    finish();

                    startActivity(new Intent(mContext, ConvenienceActivity.class));
                } else if (type.equals("5")) {
                    HelperApplication.getInstance().advBack = true;

                    finish();
//                            startActivity(new Intent(mContext, MyAdvertsingActivity.class));
                } else if(type.equals("6")){
                    HelperApplication.getInstance().advBack = true;

                    finish();
                    startActivity(new Intent(mContext, MyAdvertsingActivity.class));
                }else {
                    UpdateTradeState();
                }
                finish();
                break;
        }
    }

    private void wxPayNew() {
        /*PayReq req = new PayReq();
        //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
        req.appId			= Constants.APP_ID;
        req.partnerId		= Constants.MCH_ID;
        req.prepayId		= json.getString("prepayid");
        req.nonceStr		= genNonceStr();
        req.timeStamp		= String.valueOf(genTimeStamp());
        req.packageValue	= json.getString("package");
        req.sign			= json.getString("sign");
        req.extData			= "app data"; // optional
        Toast.makeText(PaymentActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        iwxapi.sendReq(req);*/
    }

    /**
     * 支付宝支付业务
     */
    public void payV2() {
        if (TextUtils.isEmpty(APPID) || TextUtils.isEmpty(RSA_PRIVATE)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }
        Log.d("更新支付状态", tradeNo);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, tradeNo, price, body);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        String sign = OrderInfoUtil2_0.getSign(params, RSA_PRIVATE);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PaymentActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public void createTrendNumber() {
        String treadNo = "";
        if (treadNo == null) {
            return;
        }

    }

    /**
     * 生成支付宝订单号
     *
     * @return
     */
    private String getOutTradeNo(String str) {
        String key = "";
        if (tag) {
            return tradeNo;
        } else {
            Date date = new Date();
            key = String.valueOf(date.getTime() / 1000) + "_" + str;
            tag = true;
        }
        return key;
    }


    //微信支付流程
    private void wxpay() {
        GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
        getPrepayId.execute();
    }

    private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {

        private ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(PaymentActivity.this, "提示", "正在获取支付订单");
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            if (dialog != null) {
                dialog.dismiss();
            }
            sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");
//            show.setText(sb.toString());
            Log.e("prepay_id", "-----------------" + sb.toString());

            resultunifiedorder = result;
            sendPayReq();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Map<String, String> doInBackground(Void... params) {

            String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
            String entity = genProductArgs();

            Log.e("orion1", "----" + entity);

            byte[] buf = Util.httpPost(url, entity);

            String content = new String(buf);
            Log.e("orion2", "----" + content);
            Map<String, String> xml = decodeXml(content);

            return xml;
        }
    }

    private String genProductArgs() {
        StringBuffer xml = new StringBuffer();

        try {
            String nonceStr = genNonceStr();

            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
            packageParams.add(new BasicNameValuePair("body", body));
            packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", "http://www.my51bang.com/api/returnwx/example/notify.php"));
            packageParams.add(new BasicNameValuePair("out_trade_no", tradeNo));//商户订单号
            packageParams.add(new BasicNameValuePair("spbill_create_ip", "127.0.0.1"));
            packageParams.add(new BasicNameValuePair("total_fee", String.valueOf((int) (Double.parseDouble(price) * 100))));
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));
            Log.e("paycarnew", "================" + "测试");
            Log.e("genOutTradNo", "================" + genOutTradNo());
            Log.e("genOutTradNo", "================" + "0.01");
            String sign = genPackageSign(packageParams);//生成签名
            packageParams.add(new BasicNameValuePair("sign", sign));
            Log.e("sign", "================" + sign);
            Log.e("packageParams", "================" + packageParams);
//            WXpay_StaticBean.wxpaylist.clear();
//            WXpay_StaticBean.wxpaylist.add(paymoneynew);
//            WXpay_StaticBean.wxpaylist.add(orderdataInfo.getOrder_no());

            String xmlstring = toXml(packageParams);
            Log.e("xmlstring", "================" + xmlstring);
//           return new String(xmlstring.toString().getBytes(), "ISO8859-1");
            return new String(xmlstring.toString().getBytes(), "ISO8859-1");

        } catch (Exception e) {
            return null;
        }
    }

    public Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false) {
                            //实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("orion3", "----" + e.toString());
        }
        return null;

    }

    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private String genOutTradNo() {//获取商户 订单号
        Random random = new Random();
//		return "COATBAE810"; //订单号写死的话只能支付一次，第二次不能生成订单
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    /**
     * 生成签名
     */

    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);


        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
//        Toast.makeText(PayActivity.this, packageSign+"packageSign", Toast.LENGTH_SHORT).show();
        Log.e("orionpackageSign", "----" + packageSign);
        return packageSign;
    }

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");


            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");
        Log.i("orion6666", "----" + sb.toString());
        return sb.toString();
    }

    private void sendPayReq() {

        genPayReq();


    }

    private void genPayReq() {
        PayReq req = new PayReq();
        req.appId = Constants.APP_ID;
        req.partnerId = Constants.MCH_ID;
        req.prepayId = resultunifiedorder.get("prepay_id");
        req.packageValue = "prepay_id=" + resultunifiedorder.get("prepay_id");
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());


        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);

        sb.append("sign\n" + req.sign + "\n\n");

//        show.setText(sb.toString());
        msgApi.registerApp(Constants.APP_ID);
        Log.e("orion4", "----" + signParams.toString());
        msgApi.sendReq(req);
        finish();
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    //    9a2d27ba42496f87f4838b43c3e43969
    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);

        this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes());
        Log.e("orion", "----" + appSign);
        return appSign;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (HelperApplication.getInstance().wxpay.equals("1")) {
            HelperApplication.getInstance().wxpay = "3";
            if (type.equals("3")) {
                HelperApplication.getInstance().trendsBack = true;
                ToastUtil.showShort(mContext,"3");
                finish();
            } else if (type.equals("4")) {
                finish();
                ToastUtil.showShort(mContext,"4");
                startActivity(new Intent(mContext, ConvenienceActivity.class));
            } else if (type.equals("5")) {
                HelperApplication.getInstance().advBack = true;
                ToastUtil.showShort(mContext,"5");
                finish();
//                            startActivity(new Intent(mContext, MyAdvertsingActivity.class));
            } else if(type.equals("6")){
                HelperApplication.getInstance().advBack = true;
                ToastUtil.showShort(mContext,"6");
                finish();
                startActivity(new Intent(mContext, MyAdvertsingActivity.class));
            }else {
                UpdateTradeState();
            }

        }
    }
}
