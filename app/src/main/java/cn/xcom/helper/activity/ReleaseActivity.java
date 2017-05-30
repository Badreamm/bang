package cn.xcom.helper.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.adapter.ChoosePhotoListAdapter;
import cn.xcom.helper.adapter.GridViewAdapter;
import cn.xcom.helper.bean.Front;
import cn.xcom.helper.bean.PhotoWithPath;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.GalleryFinalUtil;
import cn.xcom.helper.utils.PicturePickerDialog;
import cn.xcom.helper.utils.PushImage;
import cn.xcom.helper.utils.PushImageUtil;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringJoint;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ToastUtil;
import cn.xcom.helper.utils.WheelView;


public class ReleaseActivity extends BaseActivity implements View.OnClickListener, OnGetGeoCoderResultListener {
    private Context context;
    private RelativeLayout rl_i_help_back, rl_select_address;
    private List<PhotoInfo> addImageList;
    private List<String> nameList;//添加相册选取完返回的的list
    private ArrayList<PhotoWithPath> photoWithPaths;
    private EditText goodName;
    private EditText description;
    private GalleryFinalUtil galleryFinalUtil;
    private ImageView iv_authorized_handheld_ID_card;
    private ImageView tupian, ima_dismiss, id_dismiss11;
    private LinearLayout divide;
    private EditText ed_price;
    private EditText ed_oldprice;
    private GridView gridView;
    private GridViewAdapter gridViewAdapter;
    private LinearLayout location, image_linearLayout;
    private LinearLayout transport;
    private TextView telephone, tv_divide, text_transport, ed_location;
    private LinearLayout release;
    private Button bt_login_release;
    private ChoosePhotoListAdapter choosePhotoListAdapter;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private final int REQUEST_CODE_CROP = 1002;
    private final int REQUEST_CODE_EDIT = 1003;
    private final int PHONE_PHOTO = 1;
    private final int TAKE_PHOTO = 2;
    private final int RESULT_PHOTO = 3;//裁剪后的头像
    private View view_line;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 4;
    String s2 = null;
    private int M;
    private Front front;
    private String type = "";
    private int requestCode1;
    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private KProgressHUD hud;

    private UserInfo info = new UserInfo();

    String a[] = {"同城自取", "送货上门", "凭劵消费"};
    private double mSiteLat;
    private double mSiteLon;
    private String sitename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_release);
        context = this;
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        initView();
        Intent intent = getIntent();
        s2 = intent.getStringExtra("judge").toString();
        Log.d("123s2", s2);
        if (s2 != null && s2.equals("我是order")) {
            M = 1;
            front = (Front) intent.getSerializableExtra("editor");
            goodName.setText(front.getGoodsname());
            Log.d("editor111", front.getGoodsname());
            description.setText(front.getDescription());
            bt_login_release.setText("完成");
        } else {
            M = 2;
        }
    }

    private void initView() {
        info.readData(context);
        addImageList = new ArrayList();
        nameList = new ArrayList<>();
        photoWithPaths = new ArrayList<>();
        galleryFinalUtil = new GalleryFinalUtil(9);
        rl_i_help_back = (RelativeLayout) findViewById(R.id.rl_i_help_back);
        rl_i_help_back.setOnClickListener(this);
        rl_select_address = (RelativeLayout) findViewById(R.id.rl_select_address);
        rl_select_address.setOnClickListener(this);
        goodName = (EditText) findViewById(R.id.goodName);
        description = (EditText) findViewById(R.id.description);
        view_line = findViewById(R.id.view_line);
        // ima_dismiss= (ImageView) findViewById(R.id.ima_dissmis);
        // ima_dismiss.setOnClickListener(this);
        id_dismiss11 = (ImageView) findViewById(R.id.id_dismiss11);
        tv_divide = (TextView) findViewById(R.id.text_divide);
        tv_divide.setOnClickListener(this);
        //iv_authorized_handheld_ID_card = (ImageView) findViewById(R.id.iv_authorized_handheld_ID_card);
        tupian = (ImageView) findViewById(R.id.tupian);
        tupian.setOnClickListener(this);
        text_transport = (TextView) findViewById(R.id.text_transport);
        ed_location = (TextView) findViewById(R.id.ed_location);
        //初始化当前位置信息
        ed_location.setText(HelperApplication.getInstance().mCurrentAddress);
        mSiteLat = HelperApplication.getInstance().mCurrentLocLat;
        mSiteLon = HelperApplication.getInstance().mCurrentLocLon;
        //  delete= (ImageView) findViewById(R.id.delete);
        //   delete.setOnClickListener(this);
        divide = (LinearLayout) findViewById(R.id.divide);
        divide.setOnClickListener(this);

        ed_price = (EditText) findViewById(R.id.ed_price);
        ed_oldprice = (EditText) findViewById(R.id.ed_oldprice);
        location = (LinearLayout) findViewById(R.id.location);
//        image_linearLayout= (LinearLayout) findViewById(R.id.image_linearLayout);
//        image_linearLayout.setOnClickListener(this);
        transport = (LinearLayout) findViewById(R.id.transport);
        transport.setOnClickListener(this);
        telephone = (TextView) findViewById(R.id.telephone);
        telephone.setText(info.getUserPhone());
        release = (LinearLayout) findViewById(R.id.release);
        bt_login_release = (Button) findViewById(R.id.bt_login_release);
        bt_login_release.setOnClickListener(this);
        gridView = (GridView) findViewById(R.id.gridview);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login_release:
                if (M == 1) {
                    submitUpdata();
                } else {
                    submit();
                }
                break;
            case R.id.tupian:
                //showActionSheet();
                showPicturePicker(v);
                break;
            case R.id.rl_i_help_back:
                backImage();
                break;
            case R.id.text_divide:
                requestCode1 = 6;
                Intent intent = new Intent(ReleaseActivity.this, DivideActivity.class);
                startActivityForResult(intent, requestCode1);
                // finish();
                break;
            case R.id.transport:
                popoDialog(a);
                break;
            //选择地址
            case R.id.rl_select_address:
                Intent intent1 = new Intent(context, SelectMapPoiActivity.class);
                intent1.putExtra("lat", HelperApplication.getInstance().mCurrentLocLat);
                intent1.putExtra("lon", HelperApplication.getInstance().mCurrentLocLon);
                startActivityForResult(intent1, 0x110);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode1 == 6) {
            if (resultCode == 6) {
                String s1 = data.getStringExtra("dictionary");
                type = data.getStringExtra("id");
                tv_divide.setText(s1);
            }

        }
        if (requestCode == 0x110) {
            if (data != null) {
                //ed_location.setText(data.getStringExtra("siteName"));
                sitename = data.getStringExtra("siteName");
                mSiteLat = data.getDoubleExtra("siteLat", 0);
                mSiteLon = data.getDoubleExtra("siteLon", 0);
                // 反Geo搜索
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(new LatLng(mSiteLat, mSiteLon)));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //更新发布
    public void submitUpdata() {
        // validate
        final String goodNameString = goodName.getText().toString().trim();
        if (TextUtils.isEmpty(goodNameString)) {
            Toast.makeText(this, "商品名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (goodNameString.length() > 35) {
            ToastUtil.showShort(this, "商品名称不能超过35字");
            return;
        }

        final String descriptionString = description.getText().toString().trim();
        if (TextUtils.isEmpty(descriptionString)) {
            Toast.makeText(this, "商品描述不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        final String price = ed_price.getText().toString().trim();
        if (TextUtils.isEmpty(price)) {
            Toast.makeText(this, "价格不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        final String oldprice = ed_oldprice.getText().toString().trim();
        if (TextUtils.isEmpty(oldprice)) {
            Toast.makeText(this, "原价不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        final String location = ed_location.getText().toString().trim();
        if (TextUtils.isEmpty(oldprice)) {
            Toast.makeText(this, "地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (addImageList.size() == 0) {
            Toast.makeText(this, "请选择图片", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(text_transport.getText().toString().trim())) {
            Toast.makeText(this, "请选择配送方式", Toast.LENGTH_SHORT).show();
            return;
        }

        //先上传图片再发布
        new PushImageUtil().setPushIamge(getApplication(), addImageList, nameList, new PushImage() {
            @Override
            public void success(boolean state) {
                //发布任务
                String url = NetConstant.UPDATA;
                StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d("===更新发布", s);
                        Toast.makeText(getApplication(), "更新成功", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplication(), "网络错误，检查您的网络", Toast.LENGTH_SHORT).show();
                    }
                });
                String s = StringJoint.arrayJointchar(nameList, ",");
                Log.d("2222233", s + "");
                Log.d("2222233", nameList.size() + "");
                request.putValue("id", front.getId());
                request.putValue("picture", s);
                request.putValue("goodsname", goodNameString);
                request.putValue("type", type);
                request.putValue("price", price);
                request.putValue("oprice", oldprice);
                request.putValue("description", descriptionString);
                request.putValue("unit", "个");
                request.putValue("address", location);
                request.putValue("longitude", String.valueOf(mSiteLon));
                request.putValue("latitude", String.valueOf(mSiteLat));
                request.putValue("delivery", text_transport.getText().toString());
                SingleVolleyRequest.getInstance(getApplication()).addToRequestQueue(request);
            }

            @Override
            public void error() {
                ToastUtil.showShort(context, "图片上传失败");
                finish();
            }
        });


    }

    //提交发布
    private void submit() {
        //得到用户的wuserid

        // validate
        final String goodNameString = goodName.getText().toString().trim();
        if (TextUtils.isEmpty(goodNameString)) {
            Toast.makeText(this, "商品名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (goodNameString.length() > 35) {
            ToastUtil.showShort(this, "商品名称不能超过35字");
            return;
        }

        final String descriptionString = description.getText().toString().trim();
        if (TextUtils.isEmpty(descriptionString)) {
            Toast.makeText(this, "商品描述不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        final String price = ed_price.getText().toString().trim();
        if (TextUtils.isEmpty(price)) {
            Toast.makeText(this, "价格不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        final String oldprice = ed_oldprice.getText().toString().trim();
        if (TextUtils.isEmpty(oldprice)) {
            Toast.makeText(this, "原价不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        final String location = ed_location.getText().toString().trim();
        if (TextUtils.isEmpty(oldprice)) {
            Toast.makeText(this, "地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (addImageList.size() == 0) {
            Toast.makeText(this, "请选择图片", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(text_transport.getText().toString().trim())) {
            Toast.makeText(this, "请选择配送方式", Toast.LENGTH_SHORT).show();
            return;
        }
        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
        hud.show();
        //先上传图片再发布
        new PushImageUtil().setPushIamge(getApplication(), addImageList, nameList, new PushImage() {
            @Override
            public void success(boolean state) {
                //发布任务
                String url = NetConstant.RELEASE;
                StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (hud != null) {
                            hud.dismiss();
                        }
                        Log.d("===我的发布", s);
                        Toast.makeText(getApplication(), "发布成功", Toast.LENGTH_SHORT).show();
                        finish();
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
                String s = StringJoint.arrayJointchar(nameList, ",");
                Log.d("2222233", s + "");
                Log.d("2222233", nameList.size() + "");
                request.putValue("userid", info.getUserId());
                request.putValue("picturelist", s);
                request.putValue("goodsname", goodNameString);
                request.putValue("type", type);
                request.putValue("price", price);
                request.putValue("oprice", oldprice);
                request.putValue("description", descriptionString);
                request.putValue("unit", "个");
                request.putValue("address", location);
                request.putValue("longitude", String.valueOf(mSiteLon));
                request.putValue("latitude", String.valueOf(mSiteLat));
                request.putValue("delivery", text_transport.getText().toString());
                request.putValue("UserLatitude", String.valueOf(HelperApplication.getInstance().mLocLat));
                request.putValue("UserLongitude", String.valueOf(HelperApplication.getInstance().mLocLon));
                request.putValue("UserLocation", HelperApplication.getInstance().mLocAddress);
                SingleVolleyRequest.getInstance(getApplication()).addToRequestQueue(request);
            }

            @Override
            public void error() {
                if (hud != null) {
                    hud.dismiss();
                }
                finish();
            }
        });
    }


    public void showPicturePicker(View view) {
        PicturePickerDialog picturePickerDialog = new PicturePickerDialog(this);
        picturePickerDialog.show(new PicturePickerDialog.PicturePickerCallBack() {
            @Override
            public void onPhotoClick() {
                Toast.makeText(context, "拍 照", Toast.LENGTH_SHORT).show();
                //获取拍照权限
                if (galleryFinalUtil.openCamera(ReleaseActivity.this, (ArrayList<PhotoInfo>) addImageList, REQUEST_CODE_CAMERA, mOnHanlderResultCallback)) {
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
                galleryFinalUtil.openAblum(ReleaseActivity.this, (ArrayList<PhotoInfo>) addImageList, REQUEST_CODE_GALLERY, mOnHanlderResultCallback);
                Toast.makeText(context, "相册选择", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 弹出选择框
     */
    private void showActionSheet() {
        ActionSheet.createBuilder(ReleaseActivity.this, getSupportFragmentManager())
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
                                galleryFinalUtil.openAblum(ReleaseActivity.this, (ArrayList<PhotoInfo>) addImageList, REQUEST_CODE_GALLERY, mOnHanlderResultCallback);
                                break;
                            case 1:
                                //获取拍照权限
                                if (galleryFinalUtil.openCamera(ReleaseActivity.this, (ArrayList<PhotoInfo>) addImageList, REQUEST_CODE_CAMERA, mOnHanlderResultCallback)) {
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

    /**
     * 选择图片后 返回的图片数据
     */

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                //  photoWithPaths.clear();
                addImageList.clear();
                addImageList.addAll(resultList);
                //  photoWithPaths.addAll(GetImageUtil.getImgWithPaths(resultList));
                Log.d("======haha", addImageList.size() + "");
                gridViewAdapter = new GridViewAdapter(ReleaseActivity.this, (ArrayList<PhotoInfo>) addImageList);
                gridView.setAdapter(gridViewAdapter);
                view_line.setVisibility(View.VISIBLE);

            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(ReleaseActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    //弹出相册相机对话框
    public void showDialog() {
        new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
                .setNegativeButton("相机", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA);
                        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            String state = Environment.getExternalStorageState();
                            if (state.equals(Environment.MEDIA_MOUNTED)) {
                                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                                File file = new File(path, "51head.jpg");
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                            }
                            startActivityForResult(cameraIntent, PHONE_PHOTO);
                        } else if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                                    Manifest.permission.CAMERA)) {

                                // Show an expanation to the user *asynchronously* -- don't block
                                // this thread waiting for the user's response! After the user
                                // sees the explanation, try again to request the permission.

                            } else {

                                // No explanation needed, we can request the permission.

                                ActivityCompat.requestPermissions((Activity) context,
                                        new String[]{Manifest.permission.CAMERA},
                                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                                // app-defined int constant. The callback method gets the
                                // result of the request.
                            }
                        }
//                        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        File file = FileUitlity.getInstance(getApplicationContext()).makeDir("head_camera");
//                        capter = file.getPath()
//                                + File.separatorChar
//                                + System.currentTimeMillis()
//                                + ".jpg";
//                        camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(capter)));
//                        camera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//                        startActivityForResult(camera, PHONE_PHOTO);
                    }
                }).setPositiveButton("相册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //  takephoto();
            }
        }).show();
    }

    //弹出对话框的方法
    public void popoDialog(String[] a) {
        View outerView = LayoutInflater.from(getBaseContext())
                .inflate(R.layout.wheel_view, null);

        final WheelView wv = (WheelView) outerView
                .findViewById(R.id.wheel_view_wv);

        wv.setOffset(2);// 偏移量
        wv.setItems(Arrays.asList(a));// 实际内容
        wv.setSeletion(0);// 设置默认被选中的项目
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                id_dismiss11.setVisibility(View.INVISIBLE);
                text_transport.setText(item.toString().trim());

            }
        });
        // 展示弹出框
        new android.support.v7.app.AlertDialog.Builder(ReleaseActivity.this)
                .setTitle("请选择类型").setView(outerView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        text_transport.setText(wv.getSeletedItem());
                        id_dismiss11.setVisibility(View.INVISIBLE);
                    }
                }).show();


    }

    /*
     将相册返回的图片的路径转换为bitmap格式；
     */
    public Bitmap convertToBitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            // 缩放
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int) scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
        return Bitmap.createScaledBitmap(weak.get(), w, h, true);
    }

    //点击返回键
    public void backImage() {
        finish();
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        Log.e("hello", result.getAddress());
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(ReleaseActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        String address = result.getAddressDetail().city + result.getAddressDetail().district + sitename;
        ed_location.setText(address);
    }
}
