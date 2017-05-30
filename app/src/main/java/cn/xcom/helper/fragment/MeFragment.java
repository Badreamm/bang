package cn.xcom.helper.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONException;
import org.json.JSONObject;

import cn.xcom.helper.R;
import cn.xcom.helper.activity.AddressListActivity;
import cn.xcom.helper.activity.BillActivity;
import cn.xcom.helper.activity.CollectionActivity;
import cn.xcom.helper.activity.EditPersonalActivity;
import cn.xcom.helper.activity.InsureActivity;
import cn.xcom.helper.activity.MoreServiceActivity;
import cn.xcom.helper.activity.MyAdvertsingActivity;
import cn.xcom.helper.activity.MyMessageActivity;
import cn.xcom.helper.activity.MyOrderActivity;
import cn.xcom.helper.activity.OrderActivity;
import cn.xcom.helper.activity.OrderTakingActivity;
import cn.xcom.helper.activity.ShareQRCodeActivity;
import cn.xcom.helper.activity.SignActivity;
import cn.xcom.helper.activity.UserMessageActivity;
import cn.xcom.helper.activity.VerifyShoppingCodeActivity;
import cn.xcom.helper.activity.WalletActivity;
import cn.xcom.helper.bean.OrderHelper;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.HelperConstant;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.LogUtils;
import cn.xcom.helper.utils.SPUtils;
import cn.xcom.helper.view.CircleImageView;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 16/5/27.
 * 主页面——我
 */
public class MeFragment extends Fragment implements View.OnClickListener {
    private String TAG = "MeFragment";
    private Context mContext;
    private CircleImageView iv_head;
    private ImageView iv_gender;
    private TextView tv_name, tv_phone, tv_realName, tv_wallet, tv_sign, tv_message,
            tv_bill, tv_coupon, tv_order, tv_collection, tv_shoppingCart, tv_share2, tv_shopBuy, tv_orderTaking,
            tv_insure, tv_moreService, tv_adress, tv_fragment_me_real_baoxian, tv_fragment_me_real_name,tv_fragment_me_my_msg,
            tv_fragment_me_advertsing;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private UserInfo userInfo;
    private LinearLayout ll_serviceConsulting;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        initView();
        getInsurance();
        getNameAuthentication(userInfo.getUserId());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        getInsurance();
        getNameAuthentication(userInfo.getUserId());
    }

    /**
     * 获取实名认证
     */
    private void getNameAuthentication(final String userid) {
        RequestParams params = new RequestParams();
        params.put("userid", userid);
        HelperAsyncHttpClient.get(NetConstant.Check_Had_Authentication, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("认证", response.toString());
                if (response.optString("status").equals("success")) {
                    SPUtils.put(mContext, HelperConstant.IS_HAD_AUTHENTICATION, "1");
                    tv_fragment_me_real_name.setText("实名认证");
                    tv_fragment_me_real_name.setTextColor(mContext.getResources().getColor(R.color.white));
                } else {
                    SPUtils.put(mContext, HelperConstant.IS_HAD_AUTHENTICATION, "0");
                    tv_fragment_me_real_name.setText("未实名认证");
                    tv_fragment_me_real_name.setTextColor(mContext.getResources().getColor(R.color.holo_red_light));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("认证", responseString);
            }
        });
    }

    /**
     * 获取保险认证
     */
    private void getInsurance() {
        RequestParams params = new RequestParams();
        params.put("userid", userInfo.getUserId());
        HelperAsyncHttpClient.get(NetConstant.Check_Insurance, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("认证", response.toString());
                String data = response.optString("data");
                if (response.optString("status").equals("success")) {
                    SPUtils.put(mContext, HelperConstant.IS_INSURANCE, data);
                }
                switch (data) {
                    case "1":
                        tv_fragment_me_real_baoxian.setText("保险认证");
                        tv_fragment_me_real_baoxian.setTextColor(mContext.getResources().getColor(R.color.white));
                        break;
                    default:
                        tv_fragment_me_real_baoxian.setText("未保险认证");
                        tv_fragment_me_real_baoxian.setTextColor(mContext.getResources().getColor(R.color.holo_red_light));
                        break;
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("认证", responseString);
            }
        });
    }

    private void initView() {
        iv_head = (CircleImageView) getView().findViewById(R.id.iv_fragment_me_head);
        iv_head.setOnClickListener(this);
        tv_fragment_me_real_baoxian = (TextView) getView().findViewById(R.id.tv_fragment_me_real_baoxian);
        tv_fragment_me_real_name = (TextView) getView().findViewById(R.id.tv_fragment_me_real_name);
        iv_gender = (ImageView) getView().findViewById(R.id.iv_fragment_me_gender);
        tv_name = (TextView) getView().findViewById(R.id.tv_fragment_me_name);
        tv_phone = (TextView) getView().findViewById(R.id.tv_fragment_me_phone);
        tv_realName = (TextView) getView().findViewById(R.id.tv_fragment_me_real_name);
        tv_realName.setOnClickListener(this);
        tv_wallet = (TextView) getView().findViewById(R.id.tv_fragment_me_wallet);
        tv_wallet.setOnClickListener(this);
        tv_sign = (TextView) getView().findViewById(R.id.tv_fragment_me_sign);
        tv_sign.setOnClickListener(this);
        tv_message = (TextView) getView().findViewById(R.id.tv_fragment_me_message);
        tv_message.setOnClickListener(this);
        tv_bill = (TextView) getView().findViewById(R.id.tv_fragment_me_bill);
        tv_bill.setOnClickListener(this);
        tv_coupon = (TextView) getView().findViewById(R.id.tv_fragment_me_coupon);
        tv_coupon.setOnClickListener(this);
        tv_order = (TextView) getView().findViewById(R.id.tv_fragment_me_order);
        tv_order.setOnClickListener(this);
        tv_collection = (TextView) getView().findViewById(R.id.tv_fragment_me_collection);
        tv_collection.setOnClickListener(this);
        tv_shoppingCart = (TextView) getView().findViewById(R.id.tv_fragment_me_shopping_cart);
        tv_shoppingCart.setOnClickListener(this);
        tv_share2 = (TextView) getView().findViewById(R.id.tv_fragment_me_share2);
        tv_share2.setOnClickListener(this);
        tv_shopBuy = (TextView) getView().findViewById(R.id.tv_fragment_me_shopBuy);
        tv_shopBuy.setOnClickListener(this);
        tv_orderTaking = (TextView) getView().findViewById(R.id.tv_fragment_me_order_taking);
        tv_orderTaking.setOnClickListener(this);
        tv_insure = (TextView) getView().findViewById(R.id.tv_fragment_me_insure);
        tv_insure.setOnClickListener(this);
        ll_serviceConsulting = (LinearLayout) getView().findViewById(R.id.ll_fragment_me_service_consulting);
        ll_serviceConsulting.setOnClickListener(this);
        tv_moreService = (TextView) getView().findViewById(R.id.tv_fragment_me_more_service);
        tv_moreService.setOnClickListener(this);
        tv_adress = (TextView) getView().findViewById(R.id.tv_fragment_me_adress);
        tv_adress.setOnClickListener(this);
        tv_fragment_me_my_msg=(TextView)getView().findViewById(R.id.tv_fragment_me_myMsg) ;
        tv_fragment_me_my_msg.setOnClickListener(this);
        tv_fragment_me_advertsing=(TextView)getView().findViewById(R.id.tv_fragment_me_myAdvertising) ;
        tv_fragment_me_advertsing.setOnClickListener(this);
        userInfo = new UserInfo(mContext);
        options = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnLoading(R.mipmap.ic_deafult_head)
                .showImageOnFail(R.mipmap.ic_deafult_head)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true).build();
        displayDate();

    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void displayDate() {
        if (userInfo != null) {
            imageLoader.displayImage(NetConstant.NET_DISPLAY_IMG + userInfo.getUserImg(), iv_head, options);
            if (userInfo.getUserGender().equals("0")) {
                iv_gender.setImageResource(R.mipmap.ic_me_gender_woman);
            } else if (userInfo.getUserGender().equals("1")) {
                iv_gender.setImageResource(R.mipmap.ic_me_gender_man);
            }
            tv_name.setText(userInfo.getUserName());
            tv_phone.setText(userInfo.getUserPhone());
        }

    }

    private void getData() {
        RequestParams params = new RequestParams();
        params.put("userid", userInfo.getUserId());
        HelperAsyncHttpClient.get(NetConstant.NET_GET_USER_INFO, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG, "--statusCode->" + statusCode + "==>" + response.toString());
                if (response != null) {
                    try {
                        String state = response.getString("status");
                        if (state.equals("success")) {
                            JSONObject jsonObject = response.getJSONObject("data");
                            userInfo.setUserId(jsonObject.getString("id"));
                            userInfo.setUserName(jsonObject.getString("name"));
                            userInfo.setUserImg(jsonObject.getString("photo"));
                            userInfo.setUserAddress(jsonObject.getString("address"));
                            userInfo.setUserID(jsonObject.getString("idcard"));
                            userInfo.setUserPhone(jsonObject.getString("phone"));
                            userInfo.setUserGender(jsonObject.getString("sex"));
                            userInfo.writeData(mContext);
                            displayDate();
                        }
                        if (state.equals("error")) {
                            String data = response.getString("data");
                            Toast.makeText(mContext, data, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_fragment_me_head:
                startActivity(new Intent(mContext, EditPersonalActivity.class));
                break;
            case R.id.tv_fragment_me_real_name:

                break;
            case R.id.tv_fragment_me_wallet:
                startActivity(new Intent(mContext, WalletActivity.class));
                break;
            case R.id.tv_fragment_me_sign:
                startActivity(new Intent(mContext, SignActivity.class));
                break;
            case R.id.tv_fragment_me_message:
//                startActivity(new Intent(mContext, MessageActivity.class));
                startActivity(new Intent(mContext, UserMessageActivity.class));
                break;
            case R.id.tv_fragment_me_bill:
                startActivity(new Intent(mContext, BillActivity.class));
                break;
            case R.id.tv_fragment_me_coupon:
//                startActivity(new Intent(mContext, CouponActivity.class));
                startActivity(new Intent(mContext, MyOrderActivity.class).
                        putExtra("order_type", OrderHelper.BuyerOrder));
                break;
            case R.id.tv_fragment_me_order:
                startActivity(new Intent(mContext, OrderActivity.class));
                break;
            case R.id.tv_fragment_me_collection:
                startActivity(new Intent(mContext, CollectionActivity.class));
                break;
            case R.id.tv_fragment_me_shopping_cart:
//                startActivity(new Intent(mContext, ShoppingCartActivity.class));
                startActivity(new Intent(mContext, VerifyShoppingCodeActivity.class));
                break;
            case R.id.tv_fragment_me_adress:
                startActivity(new Intent(mContext, AddressListActivity.class));
                break;
            case R.id.tv_fragment_me_shopBuy:
                startActivity(new Intent(mContext, MyOrderActivity.class).
                        putExtra("order_type", OrderHelper.SellerOrder));
                break;
            case R.id.tv_fragment_me_order_taking:
                startActivity(new Intent(mContext, OrderTakingActivity.class));
                break;
            case R.id.tv_fragment_me_insure:
                startActivity(new Intent(mContext, InsureActivity.class));
                break;
            case R.id.ll_fragment_me_service_consulting:
                //用intent启动拨打电话
                String number = getResources().getString(R.string.tv_fragment_me_customer_number);
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
                startActivity(intent);
                break;
            case R.id.tv_fragment_me_more_service:
                startActivity(new Intent(mContext, MoreServiceActivity.class));
                break;
            case R.id.tv_fragment_me_share2:
                startActivity(new Intent(mContext, ShareQRCodeActivity.class));
                break;
            case R.id.tv_fragment_me_myMsg:
                startActivity(new Intent(mContext, MyMessageActivity.class));
                break;
            case R.id.tv_fragment_me_myAdvertising:
                startActivity(new Intent(mContext, MyAdvertsingActivity.class));
                break;
        }

    }
}
