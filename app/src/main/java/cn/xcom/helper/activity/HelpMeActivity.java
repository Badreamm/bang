package cn.xcom.helper.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.http.multipart.FilePart;
import com.android.internal.http.multipart.Part;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baoyz.actionsheet.ActionSheet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.feezu.liuli.timeselector.TimeSelector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.adapter.LocalImgGridAdapter;
import cn.xcom.helper.bean.AuthenticationList;
import cn.xcom.helper.bean.SkillTagInfo;
import cn.xcom.helper.bean.TaskType;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.record.AudioPlayer;
import cn.xcom.helper.record.RecordActivity;
import cn.xcom.helper.record.SoundView;
import cn.xcom.helper.utils.CommonAdapter;
import cn.xcom.helper.utils.GalleryFinalUtil;
import cn.xcom.helper.utils.LogUtils;
import cn.xcom.helper.utils.PushImage;
import cn.xcom.helper.utils.PushImageUtil;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringJoint;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ToastUtil;
import cn.xcom.helper.utils.ViewHolder;
import cn.xcom.helper.utils.VolleyRequest;
import cn.xcom.helper.view.NoScrollGridView;
import cn.xcom.helper.view.WheelView;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 16/6/4.
 * 帮我页
 */
public class HelpMeActivity extends BaseActivity implements View.OnClickListener, OnGetGeoCoderResultListener {
    private String TAG = "HelpMeActivity";
    private static final int SOUND_CODE = 111;
    private Context mContext;
    private RelativeLayout rl_back;
    private NoScrollGridView gv_skill;
    private KProgressHUD hud, submit_hub;
    private ArrayList<SkillTagInfo> skillTagInfos;
    private HelpMeSkillAdapter mHelpMeSkillAdapter;
    private EditText et_content, et_phone, et_site_location, et_service_location, et_wages, et_validity_period;
    private TextView tv_time_unit, tv_service_charge;
    private ImageView iv_site_location, iv_service_location, iv_validity_period;
    private Button bt_submit;
    private SkillTagInfo selectTag;
    private UserInfo userInfo;
    private List<TaskType> selectList;
    private LinearLayout ll_time;
    private ScrollView bottom;
    private String begintime = "";
    private CommonAdapter skilladp;
    private double lat;
    private double lon;
    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private LatLng mLat;

    private RelativeLayout rl_photo, rl_voice, rl_grid_photo;
    private NoScrollGridView addsnPicGrid;
    private GalleryFinalUtil galleryFinalUtil;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private ArrayList<PhotoInfo> mPhotoList;
    private LocalImgGridAdapter localImgGridAdapter;
    private List<String> nameList;//添加相册选取完返回的的list

    private double mSiteLat, mSiteLon, mServiceLat, mServiewLon;
    private String mSiteName, mServiceName;
    private int type = 1;
    private String description;

    private SoundView soundView;
    private String soundPath;
    private String soundName;
    private String skill;
    private AuthenticationList authenticationList;
    private String typeId;
    private String postions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_help_me);
        mContext = this;
        userInfo = new UserInfo(mContext);
        userInfo.readData(mContext);
        mPhotoList = new ArrayList<>();
        galleryFinalUtil = new GalleryFinalUtil(9);
        nameList = new ArrayList<>();
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        hud = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
        hud.show();
        getData();
        initView();

    }

    /**
     * 从上一页面接受经纬度
     */
    private void getData() {
        lat = getIntent().getDoubleExtra("lat", 0);
        lon = getIntent().getDoubleExtra("lon", 0);
        postions = getIntent().getStringExtra("postion");
        if (lat == 0 || lon == 0) {
            lon = HelperApplication.getInstance().mCurrentLocLon;
            lat = HelperApplication.getInstance().mCurrentLocLat;
        }
        mServiceLat = lat;
        mSiteLat = lat;
        mServiewLon = lon;
        mSiteLon = lon;
        Log.e("hello", lat + lon + "");
        mLat = new LatLng(lat, lon);
        // 反Geo搜索
        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(mLat));
        authenticationList = (AuthenticationList) getIntent().getSerializableExtra("authentication");
    }

    private void initView() {
        addsnPicGrid = (NoScrollGridView) findViewById(R.id.addsn_pic_grid);
        rl_grid_photo = (RelativeLayout) findViewById(R.id.rl_grid_photo);
        rl_photo = (RelativeLayout) findViewById(R.id.rl_photo);
        rl_photo.setOnClickListener(this);
        rl_voice = (RelativeLayout) findViewById(R.id.rl_voice);
        rl_voice.setOnClickListener(this);
        bottom = (ScrollView) findViewById(R.id.bottom);
        ll_time = (LinearLayout) findViewById(R.id.ll_time);
        ll_time.setOnClickListener(this);
        rl_back = (RelativeLayout) findViewById(R.id.rl_help_me_back);
        rl_back.setOnClickListener(this);
        et_content = (EditText) findViewById(R.id.et_help_me_content);
        et_phone = (EditText) findViewById(R.id.et_help_me_phone);
        et_phone.setText(userInfo.getUserPhone());
        et_site_location = (EditText) findViewById(R.id.et_help_me_site_location);
        et_service_location = (EditText) findViewById(R.id.et_help_me_service_location);
        et_wages = (EditText) findViewById(R.id.et_help_me_wages);
        //监听单价的变化
        et_wages.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_service_charge.setText("¥" + et_wages.getText());
            }
        });
        et_validity_period = (EditText) findViewById(R.id.et_help_me_validity_period);
        tv_time_unit = (TextView) findViewById(R.id.tv_help_me_time_unit);
        tv_time_unit.setOnClickListener(this);
        tv_service_charge = (TextView) findViewById(R.id.tv_help_me_service_charge);
        iv_site_location = (ImageView) findViewById(R.id.iv_help_me_site_location);
        iv_site_location.setOnClickListener(this);
        iv_service_location = (ImageView) findViewById(R.id.iv_help_me_service_location);
        iv_service_location.setOnClickListener(this);
        iv_validity_period = (ImageView) findViewById(R.id.iv_help_me_validity_period);
        iv_validity_period.setOnClickListener(this);
        bt_submit = (Button) findViewById(R.id.bt_help_me_submit);
        bt_submit.setOnClickListener(this);
        selectTag = new SkillTagInfo();
        skillTagInfos = new ArrayList<SkillTagInfo>();
        gv_skill = (NoScrollGridView) findViewById(R.id.gridView_help_me);
        if (!HelperApplication.getInstance().help) {
            gv_skill.setSelector(new ColorDrawable(Color.TRANSPARENT));
            mHelpMeSkillAdapter = new HelpMeSkillAdapter(mContext, skillTagInfos);
            gv_skill.setAdapter(mHelpMeSkillAdapter);
            gv_skill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(HelpMeActivity.this, SelectTaskTypeActivity.class);
                    intent.putExtra("id", skillTagInfos.get(position).getSkill_id());
                    startActivity(intent);
                }
            });

            getSkill();
        } else {
            getSkills();
        }
        soundView = (SoundView) findViewById(R.id.sound_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!HelperApplication.getInstance().help) {
            selectList = HelperApplication.getInstance().getTaskTypes();
            Log.e("count", String.valueOf(selectList.size()));
            for (int i = 0; i < skillTagInfos.size(); i++) {
                skillTagInfos.get(i).setChecked(check(skillTagInfos.get(i).getSkill_id()));
                if (skillTagInfos.get(i).isChecked()) {
                    description = skillTagInfos.get(i).getSkill_name();
                }
            }
            mHelpMeSkillAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 判断大分类是否选中
     *
     * @param id
     * @return
     */
    private boolean check(String id) {
        for (int i = 0; i < selectList.size(); i++) {
            if (selectList.get(i).getParent().equals(id)) {
                return true;
            }
        }

        return false;
    }

    private void getSkills() {
        Log.d("testid", authenticationList.getId());
        String url = NetConstant.GET_SKILLS_BY_USERID;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        if (hud != null) {
                            hud.dismiss();
                        }
                        String data = jsonObject.getString("data");
                        JSONObject jo = new JSONObject(data);
                        String skillStr = jo.getString("skilllist");
                        Gson gson = new Gson();
                        final List<AuthenticationList.SkilllistBean> skills = gson.fromJson(skillStr,
                                new TypeToken<ArrayList<AuthenticationList.SkilllistBean>>() {
                                }.getType());
                        gv_skill.setSelector(new ColorDrawable(Color.TRANSPARENT));
                        skilladp = new CommonAdapter<AuthenticationList.SkilllistBean>(mContext, skills, R.layout.item_skill_tag) {
                            @Override
                            public void convert(ViewHolder holder, AuthenticationList.SkilllistBean skilllistBean) {
                                holder.setText(R.id.tv_item_help_me_skill_tag, skilllistBean.getTypename());
                                if (postions != null) {
                                    skilladp.canselect = true;
                                    skilladp.selectIndex = Integer.parseInt(postions);
                                    skill = skills.get(Integer.parseInt(postions)).getTypename();
                                    typeId = skills.get(Integer.parseInt(postions)).getParent_typeid();
                                    postions = null;
                                }
                            }
                        };
                        gv_skill.setAdapter(skilladp);
                        gv_skill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                TextView view1 = (TextView) view.findViewById(R.id.tv_item_help_me_skill_tag);

                                if (view.isClickable()) {
                                    view1.setTextColor(getResources().getColor(R.color.colorTextWhite));
                                } else {
                                    view1.setTextColor(getResources().getColor(R.color.colorTheme));
                                }
                                skilladp.canselect = true;
                                skill = skills.get(position).getTypename();
                                typeId = skills.get(position).getParent_typeid();
                                skilladp.selectIndex = position;
                                skilladp.notifyDataSetChanged();

                            }
                        });


                        String commentStr = jo.getString("commentlist");
                        List<AuthenticationList.EvaluatelistBean> comments = gson.fromJson(commentStr,
                                new TypeToken<ArrayList<AuthenticationList.EvaluatelistBean>>() {
                                }.getType());


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
        request.putValue("userid", authenticationList.getId());
        SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);

    }

    private void getSkill() {
        RequestParams params = new RequestParams();
        params.put("id", 0);
        HelperAsyncHttpClient.get(NetConstant.NET_GET_TASKLIST, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG, "--statusCode->" + statusCode + "==>" + response.toString());
                if (hud != null) {
                    hud.dismiss();
                }
                if (response != null) {
                    try {
                        String state = response.getString("status");
                        if (state.equals("success")) {
                            JSONArray data = response.getJSONArray("data");
                            skillTagInfos.clear();
                            for (int i = 0; i < data.length(); i++) {
                                SkillTagInfo info = new SkillTagInfo();
                                JSONObject jsonObject = data.getJSONObject(i);
                                info.setSkill_id(jsonObject.getString("id"));
                                info.setSkill_name(jsonObject.getString("name"));
                                skillTagInfos.add(info);
                            }
                            mHelpMeSkillAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.e(TAG, "--statusCode->" + statusCode + "==>" + responseString);
                if (hud != null) {
                    hud.dismiss();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_help_me_back:
                finish();
                break;
            //选择上门地址
            case R.id.iv_help_me_site_location:
                Intent intent1 = new Intent(mContext, SelectMapPoiActivity.class);
                intent1.putExtra("lat", lat);
                intent1.putExtra("lon", lon);
                startActivityForResult(intent1, 1);
                break;
            //选择服务地址
            case R.id.iv_help_me_service_location:
                Intent intent2 = new Intent(mContext, SelectMapPoiActivity.class);
                intent2.putExtra("lat", lat);
                intent2.putExtra("lon", lon);
                startActivityForResult(intent2, 2);
                break;
            case R.id.tv_help_me_time_unit:
                showTextPicker();
                break;
            //选择有效时间
            case R.id.iv_help_me_validity_period:
                //showTimePicker();
                Date date = new Date();
                String startStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
                TimeSelector timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        et_validity_period.setText(time);
                        begintime = dataOne(time + ":00");
                    }
                }, startStr, "2020-12-31 23:59");
                timeSelector.show();
                break;
            case R.id.bt_help_me_submit:
                submit();
                break;
            //选择图片
            case R.id.rl_photo:
                showActionSheet();
                break;
            //语音
            case R.id.rl_voice:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                } else {
                    startActivityForResult(new Intent(this, RecordActivity.class), SOUND_CODE);
                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 1:
                    if (data != null) {
                        type = 2;
                        mSiteName = data.getStringExtra("siteName");
                        mSiteLat = data.getDoubleExtra("siteLat", 0);
                        mSiteLon = data.getDoubleExtra("siteLon", 0);
                        // 反Geo搜索
                        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                                .location(new LatLng(mSiteLat, mSiteLon)));
                        //et_site_location.setText(mSiteName);
                    }
                    break;
                case 2:
                    if (data != null) {
                        type = 3;
                        mServiceName = data.getStringExtra("siteName");
                        mServiceLat = data.getDoubleExtra("siteLat", 0);
                        mServiewLon = data.getDoubleExtra("siteLon", 0);
                        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                                .location(new LatLng(mServiceLat, mServiewLon)));
                        //et_service_location.setText(mServiceName);
                    }
                    break;
                case SOUND_CODE: {
                    if (!TextUtils.isEmpty(soundPath)) {
                        soundView.delete();
                    }
                    soundPath = data.getStringExtra("path");
                    int time = data.getIntExtra("time", 0);
                    soundView.setVisibility(View.VISIBLE);
                    soundView.init(soundPath, time);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 选择图片后 返回的图片数据
     */

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                if (reqeustCode == REQUEST_CODE_GALLERY) {
                    mPhotoList.clear();
                }
                mPhotoList.addAll(resultList);
                rl_grid_photo.setVisibility(View.VISIBLE);
                localImgGridAdapter = new LocalImgGridAdapter(mPhotoList, HelpMeActivity.this);
                addsnPicGrid.setAdapter(localImgGridAdapter);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(HelpMeActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            rl_grid_photo.setVisibility(View.GONE);
        }
    };

    /**
     * 弹出选择框
     */
    private void showActionSheet() {
        ActionSheet.createBuilder(HelpMeActivity.this, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("打开相册", "拍照")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {

                        switch (index) {
                            case 0:
                                galleryFinalUtil.openAblum(HelpMeActivity.this, mPhotoList, REQUEST_CODE_GALLERY, mOnHanlderResultCallback);
                                break;
                            case 1:
                                //获取拍照权限
                                if (galleryFinalUtil.openCamera(HelpMeActivity.this, mPhotoList, REQUEST_CODE_CAMERA, mOnHanlderResultCallback)) {
                                    return;
                                } else {
                                    String[] perms = {"android.permission.CAMERA"};
                                    int permsRequestCode = 200;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(perms, permsRequestCode);
                                    }
                                }
                                break;

                            default:
                                break;
                        }
                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HelperApplication.getInstance().getTaskTypes().clear();
        HelperApplication.getInstance().help = false;
        if (AudioPlayer.isPlaying) {
            AudioPlayer.getInstance().stopPlay();
        }
    }

    /**
     * 上传任务
     */
    private void submit() {
        if (!HelperApplication.getInstance().help) {
            if (selectList.size() == 0) {
                ToastUtil.showShort(mContext, "请选择分类");
                return;
            }
        } else {
            if (skill == null) {
                ToastUtil.showShort(mContext, "请选择分类");
                return;
            }
        }
        /*if (et_content.getText().toString().length() == 0) {
            ToastUtil.showShort(mContext, "请输入任务描述");
            return;
        }*/
        if (et_phone.getText().toString().length() == 0) {
            ToastUtil.showShort(mContext, "请输入联系方式");
            return;
        }
        if (et_wages.getText().toString().length() == 0) {
            ToastUtil.showShort(mContext, "请输入工资");
            return;
        }
        if (begintime.length() == 0) {
            ToastUtil.showShort(mContext, "请选择有效时间");
            return;
        }
        if(TextUtils.isEmpty(et_site_location.getText().toString())){
            ToastUtil.showShort(mContext, "请填写上门地址");
            return;
        }
        if(TextUtils.isEmpty(et_service_location.getText().toString())){
            ToastUtil.showShort(mContext, "请填写服务地址");
            return;
        }

        submit_hub = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
        submit_hub.show();

        if (!TextUtils.isEmpty(soundPath)) {
            uploadSound();
        } else {
            uploadImgs();
        }

    }

    /**
     * 上传声音
     */
    private void uploadSound() {
        String url = NetConstant.UPLOAD_RECORD;
        File f = new File(soundPath);
        List<Part> list = new ArrayList<>();
        try {
            FilePart filePart = new FilePart("upfile", f);
            list.add(filePart);
            VolleyRequest request = new VolleyRequest(url, list.toArray(new Part[list.size()]), new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    Log.d("uplaod_sound", s);
                    try {
                        JSONObject j = new JSONObject(s);
                        if (j.getString("status").equals("success")) {
                            soundName = j.getString("data");
                        }
                        uploadImgs();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    submit_hub.dismiss();
                    Toast.makeText(mContext, "上传录音失败", Toast.LENGTH_SHORT).show();
                }
            });
            SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void uploadImgs() {
        if (mPhotoList.size() == 0) {
            //传入1表示没有图片
            submitTask(1);
        } else {
            //先上传图片再发布
            new PushImageUtil().setPushIamge(getApplication(), mPhotoList, nameList, new PushImage() {
                @Override
                public void success(boolean state) {
                    //传入2表示有图片
                    Toast.makeText(getApplication(), "图片上传成功", Toast.LENGTH_SHORT).show();
                    submitTask(2);
                }

                @Override
                public void error() {
                    Toast.makeText(getApplication(), "图片上传失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    /**
     * 上传任务
     *
     * @param i 1表示没有图片，2表示有图片
     */
    private void submitTask(int i) {

        String url;
        if (HelperApplication.getInstance().help) {
            url = NetConstant.PUBLISH_HIRE_TASK;
        } else {
            url = NetConstant.PUBLISHTASK;
        }
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (submit_hub != null) {
                    submit_hub.dismiss();
                }
                if (s != null) {
                    try {
                        JSONObject object = new JSONObject(s);
                        String state = object.getString("status");
                        if (state.equals("success")) {
                            String data = object.getString("data");
                            HelperApplication.getInstance().getTaskTypes().clear();
                            Log.d("发布任务", data);
                            // Toast.makeText(getApplication(), "发布成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(mContext, PaymentActivity.class);
                            intent.putExtra("price", et_wages.getText().toString());
                            intent.putExtra("tradeNo", data);
                            intent.putExtra("body", "任务费用");
                            intent.putExtra("type", "1");
                            startActivity(intent);
                            finish();
                        } else {
                            HelperApplication.getInstance().getTaskTypes().clear();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (submit_hub != null) {
                    submit_hub.dismiss();
                }
                HelperApplication.getInstance().getTaskTypes().clear();
                Toast.makeText(getApplication(), "网络错误，检查您的网络", Toast.LENGTH_SHORT).show();
            }
        });
        //用户选择图片后上传图片字符串
        if (i == 2) {
            String s = StringJoint.arrayJointchar(nameList, ",");
            request.putValue("picurl", s);
        }
        if (!TextUtils.isEmpty(soundPath)) {
            request.putValue("sound", soundName);
            request.putValue("soundtime", soundView.getSoundTime() + "");
        } else {
//            request.putValue("sound", soundName);
//            request.putValue("soundtime", soundView.getSoundTime() + "");
        }
        request.putValue("userid", userInfo.getUserId());
        request.putValue("title", et_content.getText().toString());
        request.putValue("description", skill);
        request.putValue("expirydate", begintime);
        request.putValue("price", et_wages.getText().toString());
        if (HelperApplication.getInstance().help) {
            request.putValue("type", typeId);
        } else {
            request.putValue("type", getSelectString());
        }

        request.putValue("address", et_site_location.getText().toString());
        request.putValue("longitude", mSiteLon + "");
        request.putValue("latitude", mSiteLat + "");
        request.putValue("saddress", et_service_location.getText().toString());
        request.putValue("slongitude", mServiewLon + "");
        request.putValue("slatitude", mServiceLat + "");
        if (HelperApplication.getInstance().help) {
            request.putValue("employeeid", authenticationList.getId());

        }
        Log.d("---request", "picurl" + StringJoint.arrayJointchar(nameList, ",") + "\n" + "userid" + userInfo.getUserId() + "\n" + "title" + et_content.getText().toString()
                + "\n" + "description" + "," + skill + "\n" + "expirydate" + "," + begintime + "\n" + "price" + "," + et_wages.getText().toString() + "\n"
                + "type" + "," + skill + "\n" + "address" + "," + et_site_location.getText().toString() + "\n" + "longitude" + "," + mSiteLon + "\n" + "latitude" + "," + mSiteLat + "\n"
                + "saddress" + "," + et_service_location.getText().toString() + "\n" + "slongitude" + "," + mServiewLon + "\n" + "slatitude" + "," + mServiceLat + "\n"+"tupeid"+typeId);
        SingleVolleyRequest.getInstance(getApplication()).addToRequestQueue(request);
    }

    /**
     * 根据选择分类得到分类拼接字符串
     *
     * @return
     */
    private String getSelectString() {
        String str = "";
        for (int i = 0; i < selectList.size() - 1; i++) {
            str += selectList.get(i).getId() + ",";
        }
        str += selectList.get(selectList.size() - 1).getId();
        return str;
    }

    /**
     * 弹出计费方式选择框
     */
    private void showTextPicker() {
        View layout = LayoutInflater.from(this).inflate(R.layout.popwindow_select_unit, null);
        final PopupWindow popupWindow = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(bottom, Gravity.BOTTOM, 0, 0);

        // 设置背景颜色变暗
        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        //监听popwindow消失事件，取消遮盖层
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        final WheelView wv = (WheelView) layout.findViewById(R.id.wheelView);
        String[] PLANETS = new String[]{"按小时计费", "按天计费", "按月计费", "按趟计费", "按件计费", "按重量计费"};
        wv.setOffset(2);
        wv.setItems(Arrays.asList(PLANETS));
        wv.setSeletion(3);
        TextView tv_quxiao = (TextView) layout.findViewById(R.id.tv_quxiao);
        TextView tv_queding = (TextView) layout.findViewById(R.id.tv_queding);
        tv_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_time_unit.setText(wv.getSeletedItem());
                popupWindow.dismiss();
            }
        });
        tv_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 弹出时间选择对话框
     */
    private void showTimePicker() {
        Calendar cal = Calendar.getInstance();
        Date myData = new Date();
        cal.setTime(myData);

        //获取系统的时间
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        final int hour = cal.get(Calendar.HOUR_OF_DAY);
        final int minute = cal.get(Calendar.MINUTE);
        final int second = cal.get(Calendar.SECOND);
        DatePickerDialog dlg = new DatePickerDialog(HelpMeActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.e("MONTH", "monthOfYear" + monthOfYear);
                monthOfYear += 1;//monthOfYear 从0开始

                String data = year + "-" + monthOfYear + "-" + dayOfMonth;
                et_validity_period.setText(data);
                //时分秒用0代替
                String data_new = dataOne(data + "-" + hour + "-" + minute + "-" + second);
                Log.e("--444444---", data_new);
                begintime = data_new;

            }
        }, year, month, day);
        dlg.show();
    }

    /**
     * 时间转时间戳
     *
     * @param time
     * @return
     */
    public String dataOne(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        Log.e("hello", result.getAddress());
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(HelpMeActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        String address = "中国"+result.getAddressDetail().province+result.getAddressDetail().city + result.getAddressDetail().district;
        try {
            String endName = "";
            if (result.getPoiList() != null) {
                endName = result.getPoiList().get(0).name;
            } else {
                endName = result.getAddressDetail().street;
            }
            if (type == 1) {
                et_service_location.setText(address + endName);
                et_site_location.setText(address + endName);
            } else if (type == 2) {
                et_site_location.setText(address + endName);
            } else if (type == 3) {
                et_service_location.setText(address + endName);
            }
        } catch (NullPointerException e) {
            if (type == 1) {
                et_service_location.setText(HelperApplication.getInstance().detailAdress);
                et_site_location.setText(HelperApplication.getInstance().detailAdress);
                mSiteLat = HelperApplication.getInstance().mCurrentLocLat;
                mSiteLon = HelperApplication.getInstance().mCurrentLocLon;
                mServiceLat = HelperApplication.getInstance().mCurrentLocLat;
                mServiewLon = HelperApplication.getInstance().mCurrentLocLon;
            }
        }


    }

    /**
     * 任务分类适配器
     */
    public class HelpMeSkillAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<SkillTagInfo> mSkillTagInfos;

        public HelpMeSkillAdapter(Context context, ArrayList<SkillTagInfo> skillTagInfos) {
            this.mContext = context;
            if (skillTagInfos == null)
                skillTagInfos = new ArrayList<SkillTagInfo>();
            this.mSkillTagInfos = skillTagInfos;
        }

        @Override
        public int getCount() {
            return mSkillTagInfos.size();
        }

        @Override
        public SkillTagInfo getItem(int position) {
            return mSkillTagInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_help_me_skill_tag, null);
                viewHolder.tv_tag = (TextView) convertView.findViewById(R.id.tv_item_help_me_skill_tags);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_tag.setText(mSkillTagInfos.get(position).getSkill_name());
            if (mSkillTagInfos.get(position).isChecked()) {
                skill = HelperApplication.getInstance().getTaskTypes().get(0).getName();
//                ToastUtil.showShort(mContext,HelperApplication.getInstance().getTaskTypes().get(0).getName());
                viewHolder.tv_tag.setTextColor(mContext.getResources().getColor(R.color.colorTextWhite));
                viewHolder.tv_tag.setBackgroundResource(R.drawable.tv_tag_select);
            } else {
                viewHolder.tv_tag.setTextColor(mContext.getResources().getColor(R.color.colorTheme));
                viewHolder.tv_tag.setBackgroundResource(R.drawable.tv_tag);
            }
            return convertView;
        }

        private class ViewHolder {
            TextView tv_tag;
        }
    }

    /**
     * 授权权限
     *
     * @param permsRequestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
        switch (permsRequestCode) {
            case 200:
                boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (cameraAccepted) {
                    //授权成功之后，调用系统相机进行拍照操作等
                    galleryFinalUtil.openCamera(HelpMeActivity.this, mPhotoList, REQUEST_CODE_CAMERA, mOnHanlderResultCallback);
                } else {
                    //用户授权拒绝之后，友情提示一下就可以了
                    ToastUtil.showShort(this, "已拒绝进入相机，如想开启请到设置中开启！");
                }
                break;
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    startActivityForResult(new Intent(this, RecordActivity.class), SOUND_CODE);
                } else {
                    // Permission Denied
                }
            }
        }

    }

}
