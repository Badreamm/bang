package cn.xcom.helper.fragment.Authorized;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import cn.xcom.helper.R;
import cn.xcom.helper.activity.InsureActivity;
import cn.xcom.helper.activity.SelectTypeActivity;
import cn.xcom.helper.activity.SetCityActivity;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.ImageCompress;
import cn.xcom.helper.utils.LogUtils;
import cn.xcom.helper.utils.RegexUtil;
import cn.xcom.helper.utils.StringUtils;
import cz.msebera.android.httpclient.Header;


/**
 * 照片认证fragment
 */
public class PhotoAuthorizedFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "PhotoAuthorizedFragment";
    private Context mContext;
    private LinearLayout chooseCityLl;
    private RelativeLayout rl_back;
    private ImageView iv_handheld_ID_card, iv_ID_card, iv_driving_license;
    private TextView tv_city;
    private EditText et_name, et_ID, et_contact_name, et_contact_phone;
    private Button bt_next;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private static final int PHOTO_REQUEST_CAMERA = 1;//拍照
    private static final int PHOTO_REQUEST_ALBUM = 2;//相册
    private static final int PHOTO_REQUEST_CUT = 3;//剪裁
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 4;
    private static final int GET_SKILL_TYPE = 10;

    private int flag = 0;
    private ProgressDialog dialog;
    private UserInfo userInfo;
    private ArrayList<String> checkedTypeLists;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_authorized, container, false);
        mContext = getActivity();
        initView(view);
        checkedTypeLists = new ArrayList<>();
        return view;
    }


    private void initView(View v) {
        chooseCityLl = (LinearLayout) v.findViewById(R.id.ll_choose_city);
        chooseCityLl.setOnClickListener(this);
        iv_handheld_ID_card = (ImageView) v.findViewById(R.id.iv_authorized_handheld_ID_card);
        iv_handheld_ID_card.setOnClickListener(this);
        iv_ID_card = (ImageView) v.findViewById(R.id.iv_authorized_ID_card);
        iv_ID_card.setOnClickListener(this);
        iv_driving_license = (ImageView) v.findViewById(R.id.iv_authorized_driving_license);
        iv_driving_license.setOnClickListener(this);
        tv_city = (TextView) v.findViewById(R.id.tv_authorized_city);
        et_name = (EditText) v.findViewById(R.id.et_authorized_name);
        et_ID = (EditText) v.findViewById(R.id.et_authorized_ID);
        et_contact_name = (EditText) v.findViewById(R.id.et_authorized_emergency_contact_person_name);
        et_contact_phone = (EditText) v.findViewById(R.id.et_authorized_emergency_contact_person_phone);
        bt_next = (Button) v.findViewById(R.id.bt_authorized_next);
        bt_next.setOnClickListener(this);
        userInfo = new UserInfo(mContext);
        dialog = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
        options = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnLoading(R.mipmap.ic_authorized_photo)
                .showImageOnFail(R.mipmap.ic_authorized_photo)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true).build();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_choose_city:
                Intent intent = new Intent(mContext, SetCityActivity.class);
                startActivityForResult(intent, 4);
                break;

            case R.id.iv_authorized_handheld_ID_card:
                flag = 1;
                showPickDialog();
                break;
            case R.id.iv_authorized_ID_card:
                flag = 2;
                showPickDialog();
                break;
            case R.id.iv_authorized_driving_license:
                flag = 3;
                showPickDialog();
                break;
            case R.id.bt_authorized_next:
                Intent intent1 = new Intent(mContext, SelectTypeActivity.class);
                intent1.putStringArrayListExtra("checked", checkedTypeLists);
                startActivityForResult(intent1, GET_SKILL_TYPE);
                break;
        }
    }

    private void showPickDialog() {
        new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_LIGHT)
                .setNegativeButton("相册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent albumIntent = new Intent();
                        albumIntent.setType("image/*");
                        albumIntent.setAction(Intent.ACTION_PICK);
                        startActivityForResult(albumIntent, PHOTO_REQUEST_ALBUM);
                    }
                }).setPositiveButton("拍照", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                int permissionCheck = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.CAMERA);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        File file = new File(path, "51helper.jpg");
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    }
                    startActivityForResult(cameraIntent, PHOTO_REQUEST_CAMERA);
                } else if (permissionCheck == PackageManager.PERMISSION_DENIED) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case PHOTO_REQUEST_CAMERA:// 相册
                    // 判断存储卡是否可以用，可用进行存储
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        File tempFile = new File(path, "51helper.jpg");
                        compressImgByUri(Uri.fromFile(tempFile));
                    } else {
                        Toast.makeText(mContext, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case PHOTO_REQUEST_ALBUM:// 图库
                    compressImgByUri(data.getData());
                    break;

                case PHOTO_REQUEST_CUT: // 图片缩放完成后
//                    if (data != null) {
//                        getImageToView(data);
//                    }
                    break;
                case 4://选择城市
                    if (data != null) {
                        Log.e("cityResult", data.getStringExtra("city"));
                        tv_city.setText(data.getStringExtra("city"));
                    }
                    break;
                case GET_SKILL_TYPE:
                    if (resultCode == 10001) {
                        checkedTypeLists.clear();
                        checkedTypeLists = data.getStringArrayListExtra("checked");
                        boolean submitFlag = data.getBooleanExtra("submit", false);
                        if (submitFlag) {
                            submit();
                        }

                    }
            }
        }
    }


    private void submit() {
        String city = tv_city.getText().toString();
        if (city.length() == 0) {
            Toast.makeText(mContext, "请选择所在城市", Toast.LENGTH_SHORT).show();
            return;
        }
        String name = et_name.getText().toString();
        if (!RegexUtil.IsChineseOrEnglish(name)) {
            Toast.makeText(mContext, "输入的姓名不合法", Toast.LENGTH_SHORT).show();
            return;
        }
        String idNum = et_ID.getText().toString();
        if (!RegexUtil.checkIdCard(idNum)) {
            Toast.makeText(mContext, "输入的身份证号码不合法", Toast.LENGTH_SHORT).show();
            return;
        }
        String contactName = et_contact_name.getText().toString();
        if (!RegexUtil.IsChineseOrEnglish(contactName)) {
            Toast.makeText(mContext, "输入的姓名不合法", Toast.LENGTH_SHORT).show();
            return;
        }
        String contactPhone = et_contact_phone.getText().toString();
        if (!RegexUtil.checkMobile(contactPhone)) {
            Toast.makeText(mContext, "请正确输入手机号！", Toast.LENGTH_LONG).show();
            return;
        }

        if ("".equals(userInfo.getUserIDCard()) && "".equals(userInfo.getUserHandIDCard())
                && "".equals(userInfo.getUserDrivingLicense())) {
            Toast.makeText(mContext, "请至少上传一张图片！", Toast.LENGTH_LONG).show();
            return;
        }
        String types;
        if (checkedTypeLists.size() == 0) {
            Toast.makeText(mContext, "请至少选择一种职能！", Toast.LENGTH_LONG).show();
            return;
        } else {
            types = StringUtils.listToString(checkedTypeLists, ",");
        }
        Log.e("type",types);

        dialog.setMessage("正在提交认证");
        dialog.show();
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", userInfo.getUserId());
        requestParams.put("city", city);
        requestParams.put("realname", name);
        requestParams.put("idcard", idNum);
        requestParams.put("contactperson", contactName);
        requestParams.put("contactphone", contactPhone);
        requestParams.put("positive_pic", userInfo.getUserIDCard());
        requestParams.put("opposite_pic", userInfo.getUserHandIDCard());
        requestParams.put("driver_pic", userInfo.getUserDrivingLicense());
        requestParams.put("type", types);
        HelperAsyncHttpClient.get(NetConstant.NET_IDENTITY_AUTHENTICATION, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG, "--statusCode->" + statusCode + "==>" + response.toString());
                if (response != null) {
                    try {
                        String state = response.getString("status");
                        if (state.equals("success")) {
                            Toast.makeText(mContext, "提交认证成功！", Toast.LENGTH_LONG).show();
                            getActivity().finish();
                            startActivity(new Intent(mContext, InsureActivity.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                dialog.dismiss();
            }
        });

    }


    private void compressImgByUri(Uri uri){
        try {
            Bitmap bitmap = ImageCompress.getBitmapFormUri(mContext,uri);
            File file = ImageCompress.saveBitmapFile(bitmap);
            uploadImg(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void  uploadImg(File f) {
//        http://bang.xiaocool.net/index.php?g=apps&m=index&a=uploadimg
        dialog.setMessage("正在上传。。。");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        RequestParams params = new RequestParams();
        try {
            params.put("upfile", f);
        } catch (FileNotFoundException e) {

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
                            LogUtils.e(TAG, "--flag->" + flag);
                            if (flag == 1) {
                                userInfo.setUserHandIDCard(response.getString("data"));
                                imageLoader.displayImage(NetConstant.NET_DISPLAY_IMG + userInfo.getUserHandIDCard(), iv_handheld_ID_card, options);
                            } else if (flag == 2) {
                                userInfo.setUserIDCard(response.getString("data"));
                                imageLoader.displayImage(NetConstant.NET_DISPLAY_IMG + userInfo.getUserIDCard(), iv_ID_card, options);
                            } else if (flag == 3) {
                                userInfo.setUserDrivingLicense(response.getString("data"));
                                imageLoader.displayImage(NetConstant.NET_DISPLAY_IMG + userInfo.getUserDrivingLicense(), iv_driving_license, options);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.e(TAG, "--statusCode->" + statusCode + "==>" + responseString);
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("warning", "fragment");
    }
}
