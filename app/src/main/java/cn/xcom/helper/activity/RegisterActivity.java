package cn.xcom.helper.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xcom.helper.R;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.HelperConstant;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.GalleryFinalUtil;
import cn.xcom.helper.utils.LogUtils;
import cn.xcom.helper.utils.MyImageLoader;
import cn.xcom.helper.utils.PushImage;
import cn.xcom.helper.utils.PushImageUtil;
import cn.xcom.helper.utils.RegexUtil;
import cn.xcom.helper.utils.StringJoint;
import cn.xcom.helper.utils.ToastUtil;
import cn.xcom.helper.view.CircleImageView;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 16/5/27.
 * 注册页
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener{
    private String TAG="RegisterActivity";
    private RelativeLayout rl_back;
    private CircleImageView iv_head;
    private int gender=1;
    private EditText et_phone,et_verification,et_password,et_register_referral;
    private TextView tv_getVerification,userProtocol;
    private ImageView iv_password;
    private Button bt_submit;
    private RadioGroup rg_gender;
    private RadioButton rb_male,rb_female;
    private Context mContext;
    private CheckBox checkProtocol;
    private UserInfo userInfo;
    private static final int PHOTO_REQUEST_CAMERA=1;//拍照
    private static final int PHOTO_REQUEST_ALBUM=2;//相册
    private static final int PHOTO_REQUEST_CUT=3;//剪裁
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS=4;
    private ImageLoader imageLoader=ImageLoader.getInstance();
    private DisplayImageOptions options;
    private int i=120;
    private static final int CODE_ONE=5;
    private static final int CODE_TWO=6;

    private GalleryFinalUtil galleryFinalUtil;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private ArrayList<PhotoInfo> mPhotoList;
    private List<String> nameList;//添加相册选取完返回的的list
    private String headImg;


    private Handler handler=new Handler(Looper.myLooper()){
            public void handleMessage(Message msg){
                switch (msg.what){
                    case CODE_ONE:
                        tv_getVerification.setText("重发("+i+")");
                        break;
                    case CODE_TWO:
                        tv_getVerification.setText("重新发送");
                        tv_getVerification.setClickable(true);
                        break;
                }
            }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        mContext=this;
        mPhotoList = new ArrayList<>();
        galleryFinalUtil = new GalleryFinalUtil(1);
        nameList = new ArrayList<>();
        initView();
    }
    private void initView(){
        et_register_referral = (EditText) findViewById(R.id.et_register_referral);
        rl_back= (RelativeLayout) findViewById(R.id.rl_register_back);
        rl_back.setOnClickListener(this);
        iv_head= (CircleImageView) findViewById(R.id.iv_register_head);
        iv_head.setOnClickListener(this);
        rg_gender= (RadioGroup) findViewById(R.id.radioGroup_register_gender);
        rb_male= (RadioButton) findViewById(R.id.radioButton_register_man);
        rb_female= (RadioButton) findViewById(R.id.radioButton_register_woman);
        rg_gender.setOnCheckedChangeListener(this);
        userProtocol = (TextView)findViewById(R.id.user_protocol);
        userProtocol.setOnClickListener(this);
        checkProtocol =(CheckBox)findViewById(R.id.check_protocol);
//        et_name= (EditText) findViewById(R.id.et_register_name);
//        et_ID= (EditText) findViewById(R.id.et_register_ID);
//        et_address= (EditText) findViewById(R.id.et_register_address);
        et_phone= (EditText) findViewById(R.id.et_register_phone);
        et_verification= (EditText) findViewById(R.id.et_register_verification);
        et_password= (EditText) findViewById(R.id.et_register_password);
        iv_password= (ImageView) findViewById(R.id.iv_register_password);
        iv_password.setOnClickListener(this);
        tv_getVerification= (TextView) findViewById(R.id.tv_register_verification_get);
        tv_getVerification.setOnClickListener(this);
        bt_submit= (Button) findViewById(R.id.bt_register_submit);
        bt_submit.setOnClickListener(this);
        userInfo=new UserInfo(mContext);
        rb_male.setChecked(true);
        options=new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnLoading(R.mipmap.ic_deafult_head)
                .showImageOnFail(R.mipmap.ic_deafult_head)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayData();

    }
    private void displayData(){
        userInfo.readData(mContext);
        if (!userInfo.getUserImg().equals("")){
           imageLoader.displayImage(NetConstant.NET_DISPLAY_IMG+userInfo.getUserImg(),iv_head,options);
        }
        if (userInfo.getUserGender().equals("0")){
            rb_female.setChecked(true);
        }else   if (userInfo.getUserGender().equals("1")) {
            rb_male.setChecked(true);
        }
//        if (!userInfo.getUserName().equals("")){
//            et_name.setText(userInfo.getUserName());
//        }
//        if (!userInfo.getUserID().equals("")){
//            et_ID.setText(userInfo.getUserID());
//        }
//        if (!userInfo.getUserAddress().equals("")){
//            et_address.setText(userInfo.getUserAddress());
//        }
//        if (!userInfo.getUserPhone().equals("")){
//            et_phone.setText(userInfo.getUserPhone());
//        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveDate();
    }

    private void saveDate(){
        userInfo.setUserGender("" + gender);
//        userInfo.setUserName(et_name.getText().toString().trim());
//        userInfo.setUserID(et_ID.getText().toString().trim());
//        userInfo.setUserAddress(et_address.getText().toString().trim());
        userInfo.setUserPhone(et_phone.getText().toString().trim());
        userInfo.writeData(mContext);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_register_back:
                finish();
                break;
            case R.id.iv_register_head:
                //requestPermission();
                showActionSheet();
                break;
            case R.id.tv_register_verification_get:
                i=120;
                checkPhone();
                break;
            case R.id.iv_register_password:
                //记住光标开始的位置
                int pos = et_password.getSelectionStart();
                if(et_password.getInputType()!= (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)){//隐藏密码
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_password.setImageResource(R.mipmap.ic_close_eyes);
                }else{//显示密码
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_password.setImageResource(R.mipmap.ic_open_eyes);
                }
                et_password.setSelection(pos);
                break;
            case R.id.bt_register_submit:
                toRegister();
                break;
            case R.id.user_protocol:
                startActivity(new Intent(mContext,ProtocolActivity.class));
                break;
        }
    }

    private void showActionSheet() {
        ActionSheet.createBuilder(RegisterActivity.this, getSupportFragmentManager())
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
                                galleryFinalUtil.openAblum(RegisterActivity.this, mPhotoList, REQUEST_CODE_GALLERY, mOnHanlderResultCallback);
                                break;
                            case 1:
                                //获取拍照权限
                                if (galleryFinalUtil.openCamera(RegisterActivity.this, mPhotoList, REQUEST_CODE_CAMERA, mOnHanlderResultCallback)) {
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
                mPhotoList.clear();
                mPhotoList.addAll(resultList);
                MyImageLoader.displayForLocal(mPhotoList.get(0).getPhotoPath(), iv_head);
                //MyImageLoader.display("file:/" + mPhotoList.get(0).getPhotoPath(), iv_head);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getCheckedRadioButtonId()){
            case R.id.radioButton_register_man:
                gender=1;
                break;
            case R.id.radioButton_register_woman:
                gender=0;
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode!=RESULT_CANCELED){
                switch (requestCode) {
                    case PHOTO_REQUEST_CAMERA:// 相册
                        // 判断存储卡是否可以用，可用进行存储
                        String state = Environment.getExternalStorageState();
                        if (state.equals(Environment.MEDIA_MOUNTED)) {
                            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                            File tempFile = new File(path, "51head.jpg");
                            startPhotoZoom(Uri.fromFile(tempFile));
                        } else {
                            Toast.makeText(getApplicationContext(), "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case PHOTO_REQUEST_ALBUM:// 图库
                        startPhotoZoom(data.getData());
                        break;

                    case PHOTO_REQUEST_CUT: // 图片缩放完成后
                        if (data != null) {
                            getImageToView(data);
                        }
                        break;
                }
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showPickDialog(){
        new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT)
                .setNegativeButton("相册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent albumIntent=new Intent();
                        albumIntent.setType("image/*");
                        albumIntent.setAction(Intent.ACTION_PICK);
                        startActivityForResult(albumIntent,PHOTO_REQUEST_ALBUM);
                    }
                }).setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        int permissionCheck = ContextCompat.checkSelfPermission(mContext,
                                Manifest.permission.CAMERA);
                        if(permissionCheck== PackageManager.PERMISSION_GRANTED){
                            Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            String state = Environment.getExternalStorageState();
                            if (state.equals(Environment.MEDIA_MOUNTED)){
                                File path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                                File file=new File(path,"51head.jpg");
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                            }
                            startActivityForResult(cameraIntent,PHOTO_REQUEST_CAMERA);
                        }else if (permissionCheck== PackageManager.PERMISSION_DENIED){
                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext,
                                    Manifest.permission.CAMERA)) {

                                // Show an expanation to the user *asynchronously* -- don't block
                                // this thread waiting for the user's response! After the user
                                // sees the explanation, try again to request the permission.

                            } else {

                                // No explanation needed, we can request the permission.

                                ActivityCompat.requestPermissions((Activity) mContext,
                                        new String[]{Manifest.permission.CAMERA},
                                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                                // app-defined int constant. The callback method gets the
                                // result of the request.
                            }
                        }

            }
        }).show();
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showPickDialog();
            } else {
                //用户勾选了不再询问
                //提示用户手动打开权限
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "访问权限已被禁止", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }*/

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 340);
        intent.putExtra("outputY", 340);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param data
     */
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            storeImageToSDCARD(photo);
        }else{

        }
    }

    /**
     * storeImageToSDCARD 将bitmap存放到sdcard中
     * */
    public void storeImageToSDCARD(Bitmap bm) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "51helper");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            uploadImg(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadImg(File f){
//        http://bang.xiaocool.net/index.php?g=apps&m=index&a=uploadimg
        RequestParams params=new RequestParams();
        try {
            params.put("upfile",f);
        }catch (FileNotFoundException e){

        }
        LogUtils.e(TAG, "--statusCode->" + params.toString());
        HelperAsyncHttpClient.post(NetConstant.NET_UPLOAD_IMG, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG, "--statusCode->" + statusCode + "==>" + response.toString());
                if (response != null) {
                    try {
                        String state = response.getString("status");
                        if (state.equals("success")) {
                            userInfo.setUserImg(response.getString("data"));
                            imageLoader.displayImage(NetConstant.NET_DISPLAY_IMG + userInfo.getUserImg(), iv_head, options);
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
            }
        });


    }
    private void checkPhone(){
//        http://bang.xiaocool.net/index.php?g=apps&m=index&a=checkphone&phone=18653503680
        String phone=et_phone.getText().toString().trim();
        if (!RegexUtil.checkMobile(phone)) {
            Toast.makeText(mContext,"请正确输入手机号！",Toast.LENGTH_LONG).show();
            return;
        }
        RequestParams params=new RequestParams();
        params.put("phone", phone);
        HelperAsyncHttpClient.get(NetConstant.NET_CHECK_PHONE, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG, "--statusCode->" + statusCode + "==>" + response.toString());
                if (response != null) {
                    try {
                        String state = response.getString("status");
                        if (state.equals("error")) {
                            getVerification();
                        } else if (state.equals("success")) {
                            Toast.makeText(mContext, "手机号已被注册！", Toast.LENGTH_LONG).show();
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
            }
        });
    }
    private void getVerification(){
//      http://bang.xiaocool.net/index.php?g=apps&m=index&a=SendMobileCode&phone=18653503680
        tv_getVerification.setClickable(false);
        tv_getVerification.setText("重发("+i+")");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (;i>0;i--){
                    handler.sendEmptyMessage(CODE_ONE);
                    if (i<0){
                        break;
                    }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                }
                handler.sendEmptyMessage(CODE_TWO);
            }
        }).start();
        String phone=et_phone.getText().toString().trim();
        if (!RegexUtil.checkMobile(phone)) {
            Toast.makeText(mContext,"请正确输入手机号！",Toast.LENGTH_LONG).show();
            return;
        }
        RequestParams params=new RequestParams();
        params.put("phone",phone);
        HelperAsyncHttpClient.get(NetConstant.NET_GET_CODE,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+response.toString());
                if (response!=null){
                    try {
                        String state=response.getString("status");
                        if (state.equals("success")){
                           JSONObject jsonObject=response.getJSONObject("data");
                            String code=jsonObject.getString("code");
                            LogUtils.e(TAG,"--code->"+code);
                            Toast.makeText(mContext,"发送成功，请注意接受！",Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+responseString);
            }
        });

    }
    private void toRegister(){
        final String phone=et_phone.getText().toString().trim();
        final String password=et_password.getText().toString().trim();
        final String verification=et_verification.getText().toString().trim();
        if (!RegexUtil.checkMobile(phone)){
            Toast.makeText(mContext,"请正确输入手机号！",Toast.LENGTH_LONG).show();
            return;
        }
        if (verification==null||verification.length()<=0){
            Toast.makeText(mContext,"请输入验证码！",Toast.LENGTH_LONG).show();
            return;
        }
        if (password==null||password.length()<6){
            Toast.makeText(mContext,"密码至少6位！",Toast.LENGTH_LONG).show();
            return;
        }
        if (!checkProtocol.isChecked()){
            Toast.makeText(mContext,"请阅读并同意用户协议！",Toast.LENGTH_LONG).show();
            return;
        }
        if(mPhotoList.size()==0){
            //传入1表示没有图片
            headImg = "";
            RequestParams params=new RequestParams();
            params.put("name","");
            params.put("avatar",headImg);
            params.put("phone",phone);
            params.put("password",password);
            params.put("code",verification);
            params.put("referral",et_register_referral.getText().toString());
            params.put("devicestate", HelperConstant.DEVICE_STATE);
            LogUtils.e(TAG,"--params->"+params.toString());
            HelperAsyncHttpClient.get(NetConstant.NET_REGISTER,params,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+response.toString());
                    if (response!=null){
                        try {
                            String state=response.getString("status");
                            if (state.equals("success")){
                                Toast.makeText(mContext,"注册成功！",Toast.LENGTH_LONG).show();
                                finish();
                            }else if(state.equals("error")){
                                String error=response.getString("data");
                                Toast.makeText(mContext,error,Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+responseString);
                }
            });
        }else{
            new PushImageUtil().setPushIamge(getApplication(), mPhotoList, nameList, new PushImage() {
                @Override
                public void success(boolean state) {
                    //传入2表示有图片
                    String s = StringJoint.arrayJointchar(nameList, ",");
                    userInfo.setUserImg(s);
                    headImg = s;
                    RequestParams params=new RequestParams();
                    params.put("name","");
                    params.put("avatar",headImg);
                    params.put("phone",phone);
                    params.put("password",password);
                    params.put("code",verification);
                    params.put("referral",et_register_referral.getText().toString());
                    params.put("devicestate", HelperConstant.DEVICE_STATE);
                    LogUtils.e(TAG, "--params->" + params.toString());
                    HelperAsyncHttpClient.get(NetConstant.NET_REGISTER,params,new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+response.toString());
                            if (response!=null){
                                try {
                                    String state=response.getString("status");
                                    if (state.equals("success")){
                                        Toast.makeText(mContext,"注册成功！",Toast.LENGTH_LONG).show();
                                        finish();
                                    }else if(state.equals("error")){
                                        String error=response.getString("data");
                                        Toast.makeText(mContext,error,Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+responseString);
                        }
                    });
                }

                @Override
                public void error() {
                    //Toast.makeText(getApplication(), "图片上传失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private int REQUEST_CODE = 1111;

    private void requestPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 第一次请求权限时，用户如果拒绝，下一次请求shouldShowRequestPermissionRationale()返回true
            // 向用户解释为什么需要这个权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(RegisterActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
            } else {
                //申请权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        } else{
            showPickDialog();
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
                    galleryFinalUtil.openCamera(RegisterActivity.this, mPhotoList, REQUEST_CODE_CAMERA, mOnHanlderResultCallback);
                } else {
                    //用户授权拒绝之后，友情提示一下就可以了
                    ToastUtil.showShort(this, "已拒绝进入相机，如想开启请到设置中开启！");
                }

                break;

        }

    }

}