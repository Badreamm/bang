package cn.xcom.helper.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.DateUtil;
import cn.xcom.helper.utils.GalleryFinalUtil;
import cn.xcom.helper.utils.PicturePickerDialog;
import cn.xcom.helper.utils.PushImage;
import cn.xcom.helper.utils.PushImageUtil;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ToastUtil;

/**
 * Created by Administrator on 2017/4/13 0013.
 */

public class AdvertisingEditActivity extends BaseActivity {
    @BindView(R.id.rl_advedit_back)
    RelativeLayout rlAdveditBack;
    @BindView(R.id.my_advedit_update)
    TextView myAdveditUpdate;
    @BindView(R.id.adv_edit_img)
    Button advEditImg;
    @BindView(R.id.edit_img)
    ImageView editImg;
    @BindView(R.id.edit_adv_netaddress)
    EditText editAdvNetaddress;
    @BindView(R.id.edit_adv_theme)
    EditText editAdvTheme;
    String id;
    String img;
    String url;
    String theme;
    private KProgressHUD hud;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private GalleryFinalUtil galleryFinalUtil;
    private List<PhotoInfo> addImageList;
    private List<String> nameList;//添加相册选取完返回的的list
    Context context;
    private UserInfo userInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv_edit);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        img = getIntent().getStringExtra("img");
        url = getIntent().getStringExtra("url");
        theme = getIntent().getStringExtra("theme");
        editAdvNetaddress.setText(id);
        initView();
        Glide
                .with(context)
                .load(NetConstant.NET_DISPLAY_IMG + img)
                .into(editImg);

    }

    private void initView() {
        context = this;
        userInfo = new UserInfo(this);
        editAdvNetaddress.setText(url);
        editAdvTheme.setText(theme);
        galleryFinalUtil = new GalleryFinalUtil(1);
        addImageList = new ArrayList<>();
        nameList = new ArrayList<>();
        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
    }

    @OnClick({R.id.rl_advedit_back, R.id.my_advedit_update, R.id.adv_edit_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_advedit_back:
                finish();
                break;
            case R.id.my_advedit_update:
                if (addImageList.size() < 1) {
                    ToastUtil.showShort(context, "请上传图片");
                } else {
                    uploadImgs();
                }

                break;
            case R.id.adv_edit_img:
                showPicturePicker(view);
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
                if (galleryFinalUtil.openCamera(AdvertisingEditActivity.this, (ArrayList<PhotoInfo>) addImageList, REQUEST_CODE_CAMERA, mOnHanlderResultCallback)) {
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
                galleryFinalUtil.openAblum(AdvertisingEditActivity.this, (ArrayList<PhotoInfo>) addImageList, REQUEST_CODE_GALLERY, mOnHanlderResultCallback);
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

                addImageList.clear();
                addImageList.addAll(resultList);
                String photopath = addImageList.get(0).getPhotoPath();
                Glide
                        .with(context)
                        .load(photopath)
                        .into(editImg);
//                view_line.setVisibility(View.VISIBLE);

            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(AdvertisingEditActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

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
        String url = NetConstant.UPDATE_AD;

        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.d("---release", s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String state = jsonObject.getString("status");
                    if (state.equals("success")) {
                        ToastUtil.showShort(context, "修改成功");
                        finish();
                        hud.dismiss();
                        String jsonObject1 = jsonObject.getString("data");

                    } else {
                        hud.dismiss();
                        ToastUtil.showShort(context, "修改失败");
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
        request.putValue("id", id);
        request.putValue("userid", userInfo.getUserId());
        request.putValue("photo", nameList.get(0));
        Log.d("======photo", nameList.get(0));
        if (editAdvNetaddress.getText() == null) {
            request.putValue("url", "");
        } else {
            request.putValue("url", editAdvNetaddress.getText().toString());
        }

        if (editAdvTheme.getText() == null) {
            request.putValue("slide_name", "");
        } else {
            request.putValue("slide_name", editAdvTheme.getText().toString());
        }

        SingleVolleyRequest.getInstance(getApplication()).addToRequestQueue(request);
    }

}
