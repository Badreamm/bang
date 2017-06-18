package cn.xcom.helper.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.adapter.GridViewAdapter;
import cn.xcom.helper.bean.AdvertisingImg;
import cn.xcom.helper.bean.AppInfo;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.model.CityBean;
import cn.xcom.helper.utils.DateUtil;
import cn.xcom.helper.utils.GalleryFinalUtil;
import cn.xcom.helper.utils.PicturePickerDialog;
import cn.xcom.helper.utils.PushImage;
import cn.xcom.helper.utils.PushImageUtil;
import cn.xcom.helper.utils.PushVideoUtil;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringJoint;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ToastUtil;
import cn.xcom.helper.utils.ToastUtils;

import static com.baidu.mapapi.BMapManager.getContext;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

public class ReleaseAdvertisingActivity extends BaseActivity implements View.OnClickListener {
    Button one_adv, two_adv, three_adv, advAddImg;
    LinearLayout advOnePlay, advTwoPlay, advThreePlay, advNetAddressLy, advThemeLy;
    ImageView advImg;
    RelativeLayout back;
    EditText etAdvNetAddress, etAdvTheme;
    TextView one_start, one_end, two_start, two_end, three_start, three_end, messagePrice, advRelease;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private GalleryFinalUtil galleryFinalUtil;
    private List<PhotoInfo> addImageList;
    private List<String> nameList;//添加相册选取完返回的的list
    Context context;
    String photopath;
    String typeId = "1";
    private UserInfo userInfo;
    private String price;
    private String startTime;
    private String endTime;
    private String city;
    private List<String> dayList;
    private List<LinearLayout> LlList;
    private KProgressHUD hud;
    private String payId;
    java.text.DecimalFormat df;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_advertising);
        city = HelperApplication.getInstance().detailCityAdress;
        userInfo = new UserInfo(this);
        df = new java.text.DecimalFormat("#0.00");
        try {
            initView();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            initData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void initView() throws ParseException {
        context = this;
        galleryFinalUtil = new GalleryFinalUtil(1);
        addImageList = new ArrayList<>();
        dayList = new ArrayList<>();
        LlList = new ArrayList<>();
        nameList = new ArrayList<>();
        one_start = (TextView) findViewById(R.id.adv_one_play_start);
        one_end = (TextView) findViewById(R.id.adv_one_play_end);
        two_start = (TextView) findViewById(R.id.adv_two_play_start);
        two_end = (TextView) findViewById(R.id.adv_two_play_end);
        three_start = (TextView) findViewById(R.id.adv_three_play_start);
        three_end = (TextView) findViewById(R.id.adv_three_play_end);
        one_adv = (Button) findViewById(R.id.one_adv);
        one_adv.setOnClickListener(this);
        two_adv = (Button) findViewById(R.id.two_adv);
        two_adv.setOnClickListener(this);
        three_adv = (Button) findViewById(R.id.three_adv);
        three_adv.setOnClickListener(this);
        advOnePlay = (LinearLayout) findViewById(R.id.adv_play_one);
        advOnePlay.setOnClickListener(this);
        advTwoPlay = (LinearLayout) findViewById(R.id.adv_play_two);
        advTwoPlay.setOnClickListener(this);
        advThreePlay = (LinearLayout) findViewById(R.id.adv_play_three);
        advThreePlay.setOnClickListener(this);
        advAddImg = (Button) findViewById(R.id.adv_add_img);
        advAddImg.setOnClickListener(this);
        advImg = (ImageView) findViewById(R.id.adv_img);
        back = (RelativeLayout) findViewById(R.id.rl_advertising_back);
        back.setOnClickListener(this);
        advNetAddressLy = (LinearLayout) findViewById(R.id.adv_net_address_ly);
        advThemeLy = (LinearLayout) findViewById(R.id.adv_theme_ly);
        etAdvNetAddress = (EditText) findViewById(R.id.et_adv_netaddress);
        etAdvTheme = (EditText) findViewById(R.id.et_adv_theme);
        messagePrice = (TextView) findViewById(R.id.message_price);
        advRelease = (TextView) findViewById(R.id.my_advertising_release);
        advRelease.setOnClickListener(this);
        LlList.add(advOnePlay);
        LlList.add(advTwoPlay);
        LlList.add(advThreePlay);
        checkAdIsCanPublish();
        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
    }

    private void initData() throws ParseException {

        one_start.setText(DateUtil.getCurrentDate());
        one_end.setText(DateUtil.getOneEnd());
        two_start.setText(DateUtil.getTwoStart());
        two_end.setText(DateUtil.getTwoEnd());
        three_start.setText(DateUtil.getThreeStart());
        three_end.setText(DateUtil.getThreeEnd());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_advertising_back:
                finish();
                break;
            case R.id.one_adv:
                typeId = "1";
                one_adv.setBackgroundResource(R.color.colorPrimary);
                two_adv.setBackgroundResource(R.color.gray_normal);
                three_adv.setBackgroundResource(R.color.gray_normal);
                messagePrice.setText("0.00");
                try {
                    checkAdIsCanPublish();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

//                if (startTime == null) {
//                } else {
//                    GetMessagePrice(startTime, endTime);
//                }
                break;
            case R.id.two_adv:
                two_adv.setBackgroundResource(R.color.colorPrimary);
                one_adv.setBackgroundResource(R.color.gray_normal);
                three_adv.setBackgroundResource(R.color.gray_normal);
                messagePrice.setText("0.00");
                typeId = "2";
                try {
                    checkAdIsCanPublish();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

//                if (startTime == null) {
//                } else {
//                    GetMessagePrice(startTime, endTime);
//                }
                break;
            case R.id.three_adv:
                typeId = "3";
                messagePrice.setText("0.00");
                try {
                    checkAdIsCanPublish();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                three_adv.setBackgroundResource(R.color.colorPrimary);
                two_adv.setBackgroundResource(R.color.gray_normal);
                one_adv.setBackgroundResource(R.color.gray_normal);

//                if (startTime == null) {
//                } else {
//                    GetMessagePrice(startTime, endTime);
//                }
                break;
            case R.id.adv_play_one:

                advOnePlay.setBackgroundResource(R.drawable.adv_bg_p);
                if (advTwoPlay.isClickable()) {
                    advTwoPlay.setBackgroundResource(R.drawable.adv_bg);
                }
                if (advThreePlay.isClickable()) {
                    advThreePlay.setBackgroundResource(R.drawable.adv_bg);
                }
                try {
                    GetMessagePrice(DateUtil.getCurrentDate(), DateUtil.getOneEnd());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.adv_play_two:

                advTwoPlay.setBackgroundResource(R.drawable.adv_bg_p);
                if (advOnePlay.isClickable()) {
                    advOnePlay.setBackgroundResource(R.drawable.adv_bg);
                }
                if (advThreePlay.isClickable()) {
                    advThreePlay.setBackgroundResource(R.drawable.adv_bg);
                }
                try {
                    GetMessagePrice(DateUtil.getTwoStart(), DateUtil.getTwoEnd());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.adv_play_three:

                advThreePlay.setBackgroundResource(R.drawable.adv_bg_p);
                if (advOnePlay.isClickable()) {
                    advOnePlay.setBackgroundResource(R.drawable.adv_bg);
                }
                if (advTwoPlay.isClickable()) {
                    advTwoPlay.setBackgroundResource(R.drawable.adv_bg);
                }
                try {
                    GetMessagePrice(DateUtil.getThreeStart(), DateUtil.getThreeEnd());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.adv_add_img:
                showPicturePicker(v);
                break;
            case R.id.my_advertising_release:
                if (startTime == null) {
                    ToastUtil.showShort(context, "请选择广告时间段");
                } else {
                    sumbit();
                }
                break;
        }
    }


    public void showPicturePicker(View view) {
        PicturePickerDialog picturePickerDialog = new PicturePickerDialog(this);
        picturePickerDialog.show(new PicturePickerDialog.PicturePickerCallBack() {
            @Override
            public void onPhotoClick() {

                Toast.makeText(context, "拍 照", Toast.LENGTH_SHORT).show();
                //获取拍照权限
                if (galleryFinalUtil.openCamera(ReleaseAdvertisingActivity.this, (ArrayList<PhotoInfo>) addImageList, REQUEST_CODE_CAMERA, mOnHanlderResultCallback)) {
                    return;
                } else {
                    String[] perms = {"android.permission.CAMERA"};
                    int permsRequestCode = 200;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(perms, permsRequestCode);
                    }
                }
            }

            @Override
            public void onAlbumClick() {
                galleryFinalUtil.openAblum(ReleaseAdvertisingActivity.this, (ArrayList<PhotoInfo>) addImageList, REQUEST_CODE_GALLERY, mOnHanlderResultCallback);
                Toast.makeText(context, "相册选择", Toast.LENGTH_SHORT).show();

            }

        });
    }

    /**
     * 选择图片后 返回的图片数据放在list里面
     */
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                advImg.setVisibility(View.VISIBLE);
                advNetAddressLy.setVisibility(View.VISIBLE);
                advThemeLy.setVisibility(View.VISIBLE);
                addImageList.clear();
                addImageList.addAll(resultList);
                photopath = addImageList.get(0).getPhotoPath();
                Glide
                        .with(context)
                        .load(photopath)
                        .into(advImg);
//                view_line.setVisibility(View.VISIBLE);

            } else {
                advImg.setVisibility(View.GONE);
                advNetAddressLy.setVisibility(View.GONE);
                advThemeLy.setVisibility(View.GONE);
//                view_line.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(ReleaseAdvertisingActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    /*
    * 获取天数
    * */
    private int getDay(String start, String end) throws ParseException {
        Date startDate = DateUtil.getStringToDates(start);
        Date endDate = DateUtil.getStringToDates(end);
        SimpleDateFormat sf = new SimpleDateFormat("dd");
        int starti = Integer.parseInt(sf.format(startDate));
        int endi = Integer.parseInt(sf.format(endDate));
        return endi - starti + 1;
    }

    //检测广告位是否可用
    private void checkAdIsCanPublish() throws ParseException {
        if (hud != null) {
            hud.show();
        }
        String day1 = DateUtil.getStringToDate(DateUtil.getOneEnd()) + "";
        String day2 = DateUtil.getStringToDate(DateUtil.getTwoEnd()) + "";
        String day3 = DateUtil.getStringToDate(DateUtil.getThreeEnd()) + "";
        String day1f = day1.substring(0, day1.length() - 3) + ",";
        String day2f = day2.substring(0, day2.length() - 3) + ",";
        String day3f = day3.substring(0, day3.length() - 3) + "";
        dayList.clear();
        dayList.add(day1f.replace(",", ""));
        dayList.add(day2f.replace(",", ""));
        dayList.add(day3f);
        String url = NetConstant.CHECK_AD_IS_CAN_PUBLISH;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("=====checkAd", "" + s);
                    JSONObject jsonObject = new JSONObject(s);
                    String state = jsonObject.getString("status");
                    if (state.equals("success")) {
                        hud.dismiss();
                        JSONArray ja = jsonObject.getJSONArray("data");//取出“asks”对应的值，因为asks对应的值
                        //中含有方括号，所以这是JsonArray型数据，需要取出进一步解析；JsonArray可以与数组
                        //进行比较理解，我个人是这样的，供参考
                        for (int i = 0; i < ja.length(); i++) {
                            JSONArray ja1 = ja.getJSONArray(i);//获取ja中的第一个元素，因为这个元素
                            //也是jsonArray,所以可以再进一步解析
                            String d1 = ja1.getString(0);
                            String d2 = ja1.getString(1);//
                            Log.d("jsonarray" + i, d1 + d2);
                            for (int j = 0; j < ja.length(); j++) {
                                if (d1.equals(dayList.get(j))) {
                                    Log.d("=====daylist", dayList.get(j));
                                    if (d2.equals("1")) {
                                        LlList.get(j).setBackgroundResource(R.color.gray_normal);
                                        LlList.get(j).setClickable(true);
//                                       LlList.get(j).setFocusable(true);
                                    } else {
                                        LlList.get(j).setBackgroundResource(R.color.gray_pressed);
//                                        LlList.get(j).setOnClickListener(null);
                                        LlList.get(j).setClickable(false);
                                    }
                                }
                            }
                        }

                    } else {
                        hud.dismiss();
                        ToastUtil.showShort(context, "检测失败，请检查您的网络");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hud.dismiss();
                ToastUtils.showToast(ReleaseAdvertisingActivity.this, "网络连接错误，请检查您的网络");

            }
        });
        request.putValue("userid", userInfo.getUserId());
        request.putValue("type", typeId);
        request.putValue("cityid", HelperApplication.getInstance().mLocaddresscityid);
        Log.d("---cityid", HelperApplication.getInstance().mLocaddresscityid);
        request.putValue("day", day1f + day2f + day3f);
        Log.d("---day", day1 + day2 + day3);
        SingleVolleyRequest.getInstance(getContext()).addToRequestQueue(request);
    }

    /*
    * 获取广告位单价
    * */
    private void GetMessagePrice(final String start, final String end) {
        if (hud != null) {
            hud.show();
        }
        String url = NetConstant.GET_MESSAGE_PRICE;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("=====显示111", "" + s);
                    JSONObject jsonObject = new JSONObject(s);
                    String state = jsonObject.getString("status");
                    if (state.equals("success")) {
                        hud.dismiss();
                        String jsonObject1 = jsonObject.getString("data");
                        price = jsonObject1;
                        startTime = start;
                        endTime = end;
                        try {
                            messagePrice.setText(df.format((getDay(start, end) * Double.parseDouble(price))) + "");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    } else {
                        hud.dismiss();
                        ToastUtil.showShort(context, "获取单价失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(ReleaseAdvertisingActivity.this, "网络连接错误，请检查您的网络");
                hud.dismiss();
            }
        });
        request.putValue("userid", userInfo.getUserId());
        request.putValue("type", typeId);
        Log.d("=====", userInfo.getUserId() + typeId);
        SingleVolleyRequest.getInstance(getContext()).addToRequestQueue(request);
    }

    /**
     * 上传图片
     */
    private void uploadImgs() {
        if (!hud.isShowing()) {
            hud.show();
        }
        //先上传图片再发布
        new PushImageUtil().setPushIamge(getApplication(), addImageList, nameList, new PushImage() {
            @Override
            public void success(boolean state) {
                hud.dismiss();
                Toast.makeText(getApplication(), "上传图片成功", Toast.LENGTH_SHORT).show();
                uploadAdv();
            }

            @Override
            public void error() {
                hud.dismiss();
                Toast.makeText(getApplication(), "上传图片失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
    * 发布广告
    * */
    private void uploadAdv() {
        if (!hud.isShowing()) {
            hud.show();
        }
        //Toast.makeText(getApplication(), "图片上传成功", Toast.LENGTH_SHORT).show();
        //发布任务
        String url = NetConstant.PUBLISH_AD;

        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.d("---release", s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String state = jsonObject.getString("status");
                    if (state.equals("success")) {
                        ToastUtil.showShort(context, "上传成功");
                        hud.dismiss();
                        String jsonObject1 = jsonObject.getString("data");
                        payId = jsonObject1;
                        dialog();
                    } else {
                        hud.dismiss();
                        ToastUtil.showShort(context, "上传失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    hud.dismiss();
                    Toast.makeText(getApplication(), "解析错误", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (hud != null) {
                    hud.dismiss();
                }
                Toast.makeText(getApplication(), "网络错误，检查您的网络", Toast.LENGTH_SHORT).show();
            }
        });

        request.putValue("userid", userInfo.getUserId());
        request.putValue("photo", nameList.get(0));
        Log.d("======photo", nameList.get(0));
        if (etAdvNetAddress.getText() == null) {
            request.putValue("url", "");
        } else {
            request.putValue("url", etAdvNetAddress.getText().toString());
        }
        String begintime = DateUtil.getStringToDate(startTime) + "";
        String endtime = DateUtil.getStringToDate(endTime) + "";
        request.putValue("begintime", begintime.substring(0, begintime.length() - 3));
        request.putValue("endtime",endtime.substring(0, endtime.length() - 3));

        if (messagePrice.getText() == null) {
            request.putValue("price", "");
        } else {
            request.putValue("price", messagePrice.getText().toString());
        }

        request.putValue("type", typeId);
        if (etAdvTheme.getText() == null) {
            request.putValue("slide_name", "");
        } else {
            request.putValue("slide_name", etAdvTheme.getText().toString());
        }
        request.putValue("cityid", HelperApplication.getInstance().mLocaddresscityid);
        SingleVolleyRequest.getInstance(getApplication()).addToRequestQueue(request);
    }

    /*
    * 发布
    * */
    private void sumbit() {

        if (addImageList.size() < 1) {
            ToastUtil.showShort(context, "请上传图片");
        } else {
            uploadImgs();
        }


    }

    /*
  *点击输入框外隐藏小键盘
  * */
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (DateUtil.isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReleaseAdvertisingActivity.this);
        builder.setMessage("是否要购买广告发布业务");
        builder.setTitle("系统提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Intent payIntent = new Intent(ReleaseAdvertisingActivity.this, PaymentActivity.class);
                payIntent.putExtra("paytype", "AdvPay");
                payIntent.putExtra("tradeNo", payId);
                payIntent.putExtra("body", "广告发布");
                payIntent.putExtra("price", messagePrice.getText().toString());
                if (HelperApplication.getInstance().conAdv){
                    payIntent.putExtra("type", "6");
                    HelperApplication.getInstance().conAdv=false;
                }else {
                    payIntent.putExtra("type", "5");
                }

                startActivity(payIntent);
            }

        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                startActivity(new Intent(ReleaseAdvertisingActivity.this, MyAdvertsingActivity.class));
            }
        });
        builder.show();
    }
}
