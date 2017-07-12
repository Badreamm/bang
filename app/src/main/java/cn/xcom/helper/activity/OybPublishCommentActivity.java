package cn.xcom.helper.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.adapter.GridViewAdapter;
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

/**
 * Created by hzh on 2017/7/12.
 */

public class OybPublishCommentActivity extends BaseActivity {
    private TextView publishTv, addressTv;
    private EditText descriptionEt, phoneEt, nameEt;
    private RelativeLayout addressRl;
    private ImageView tupianIv;
    private GalleryFinalUtil galleryFinalUtil;
    private List<PhotoInfo> addImageList;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private GridViewAdapter gridViewAdapter;
    private GridView gridView;
    private List<String> nameList;//添加相册选取完返回的的list
    private KProgressHUD hud;
    private String goodId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oub_publish_comment);
        goodId = getIntent().getStringExtra("id");
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        publishTv = (TextView) findViewById(R.id.publish_tv);
        descriptionEt = (EditText) findViewById(R.id.description_et);
        phoneEt = (EditText) findViewById(R.id.phone_et);
        nameEt = (EditText) findViewById(R.id.name_et);
        addressRl = (RelativeLayout) findViewById(R.id.address_rl);
        addressTv = (TextView) findViewById(R.id.address_tv);
        tupianIv = (ImageView) findViewById(R.id.tupian);
        tupianIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicturePicker();
            }
        });
        gridView = (GridView) findViewById(R.id.gridview);

        galleryFinalUtil = new GalleryFinalUtil(9);
        addImageList = new ArrayList<>();
        nameList = new ArrayList<>();

        publishTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publish();
            }
        });

        addressRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(OybPublishCommentActivity.this,AddressListActivity.class);
                intent1.putExtra("from",1);
                startActivityForResult(intent1, 0x110);
            }
        });
        new AlertDialog.Builder(OybPublishCommentActivity.this).setTitle("提示")
                .setMessage("必须进行晒单评论之后，并填写收货地址后才可领取奖品")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void publish() {
        final String content = descriptionEt.getText().toString();
        final String name = nameEt.getText().toString();
        final String phone = phoneEt.getText().toString();
        String address = addressTv.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.Toast(this, "请输入内容");
            return;
        }
        if (TextUtils.isEmpty(name)) {
            ToastUtil.Toast(this, "请输入姓名");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.Toast(this, "请输入手机号");
            return;
        }
        if (address.equals("请选择收货地址")) {
            ToastUtil.Toast(this, "请选择收货地址");
            return;
        }
        if (addImageList.size() == 0) {
            Toast.makeText(this, "请选择图片", Toast.LENGTH_SHORT).show();
            return;
        }
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
        hud.show();
        //先上传图片再发布
        new PushImageUtil().setPushIamge(getApplication(), addImageList, nameList, new PushImage() {
            @Override
            public void success(boolean state) {
                //发布任务
                String url = NetConstant.PUBLISH_OYB_COMMENT;
                StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (hud != null) {
                            hud.dismiss();
                        }
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
                request.putValue("id",goodId);
                request.putValue("userid", new UserInfo(OybPublishCommentActivity.this).getUserId());
                request.putValue("picurl", s);
                request.putValue("content", content);
                request.putValue("phone", phone);
                request.putValue("name", name);
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

    public void showPicturePicker() {
        PicturePickerDialog picturePickerDialog = new PicturePickerDialog(this);
        picturePickerDialog.show(new PicturePickerDialog.PicturePickerCallBack() {
            @Override
            public void onPhotoClick() {
                Toast.makeText(OybPublishCommentActivity.this, "拍 照", Toast.LENGTH_SHORT).show();
                //获取拍照权限
                if (galleryFinalUtil.openCamera(OybPublishCommentActivity.this, (ArrayList<PhotoInfo>) addImageList, REQUEST_CODE_CAMERA, mOnHanlderResultCallback)) {
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
                galleryFinalUtil.openAblum(OybPublishCommentActivity.this, (ArrayList<PhotoInfo>) addImageList, REQUEST_CODE_GALLERY, mOnHanlderResultCallback);
                Toast.makeText(OybPublishCommentActivity.this, "相册选择", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 选择图片后 返回的图片数据
     */

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                //  photoWithPaths.clear();
                if (reqeustCode == REQUEST_CODE_GALLERY) {
                    addImageList.clear();
                }
                addImageList.addAll(resultList);
                //  photoWithPaths.addAll(GetImageUtil.getImgWithPaths(resultList));
                Log.d("======haha", addImageList.size() + "");
                gridViewAdapter = new GridViewAdapter(OybPublishCommentActivity.this, (ArrayList<PhotoInfo>) addImageList);
                gridView.setAdapter(gridViewAdapter);

            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(OybPublishCommentActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_CANCELED){
            switch (requestCode) {
                case 0X110:
                    if (data != null) {
                        addressTv.setText(data.getStringExtra("address"));
                    }
                    break;
            }
        }
    }
}
