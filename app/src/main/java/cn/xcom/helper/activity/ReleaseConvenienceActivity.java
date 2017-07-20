package cn.xcom.helper.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Camera;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import android.media.MediaScannerConnection;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.internal.http.multipart.FilePart;
import com.android.internal.http.multipart.Part;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.alertview.AlertView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.adapter.GridViewAdapter;
import cn.xcom.helper.bean.ADDetial;
import cn.xcom.helper.bean.ADInfo;
import cn.xcom.helper.bean.AdvertisingImg;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.record.AudioPlayer;
import cn.xcom.helper.record.RecordActivity;
import cn.xcom.helper.record.SoundView;
import cn.xcom.helper.utils.AudioManager;
import cn.xcom.helper.utils.DateUtil;
import cn.xcom.helper.utils.GalleryFinalUtil;
import cn.xcom.helper.utils.PicturePickerDialog;
import cn.xcom.helper.utils.PushImage;
import cn.xcom.helper.utils.PushImageUtil;
import cn.xcom.helper.utils.PushVideo;
import cn.xcom.helper.utils.PushVideoUtil;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringJoint;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ToastUtil;
import cn.xcom.helper.utils.ToastUtils;
import cn.xcom.helper.utils.ViewFactory;
import cn.xcom.helper.utils.VolleyRequest;
import cn.xcom.helper.view.CycleViewPager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import vn.tungdx.mediapicker.MediaItem;
import vn.tungdx.mediapicker.MediaOptions;
import vn.tungdx.mediapicker.activities.MediaPickerActivity;

import static android.R.attr.thumbnail;
import static cn.xcom.helper.record.AudioPlayer.mediaPlayer;
import static com.baidu.mapapi.BMapManager.getContext;

/**
 * 发布便民圈
 */
public class ReleaseConvenienceActivity extends BaseActivity implements View.OnClickListener {
    public static final int REQUEST_CODE_RECORD_FINISH = 0;
    private static final int SOUND_CODE = 111;
    private static final int REQ_CODE = 10001;
    private static final int REQUEST_VIDEO_CODE = 2;
    private static final int CAMERA_RQ = 8099;
    private static final int CHECK_PERMISSION = 8001;
    private static final int VIDEO = 5;
    private Context context;
    private List<PhotoInfo> addImageList;
    private RelativeLayout back;
    private List<String> nameList;//添加相册选取完返回的的list
    private TextView cnnvenience_release, convenience_phone, myAdv;
    private EditText description;
    private GridView gridview;
    private LinearLayout image_linearLayout;
    private ImageView tupian, voice, video;
    //    private ImageView slideImg;
    private View view_line, view_lines;
    private JCVideoPlayerStandard videoView;
    //    private VideoView videoView;
    private View inflate;
    private Button chooseVideo, playVideo, cancel;
    private Dialog dialog;
    private GalleryFinalUtil galleryFinalUtil;
    private GridViewAdapter gridViewAdapter;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private UserInfo userInfo;//得到用户的userid
    private KProgressHUD hud;
    private String soundPath;
    private SoundView soundView;
    private String descriptionString;
    private String soundName;
    private List<ADDetial> advertisingImgs;
    private String advertisingurl;
    private String payId;
    private String mVideoPath;
    private List<ImageView> views = new ArrayList<ImageView>();
    private List<ADInfo> infos = new ArrayList<ADInfo>();
    private CycleViewPager cycleViewPager;
    private String[] imageUrls = {"http://img.taodiantong.cn/v55183/infoimg/2013-07/130720115322ky.jpg",
            "http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg",
            "http://pic18.nipic.com/20111215/577405_080531548148_2.jpg"
    };
    private List<MediaItem> mMediaSelectedList;
    private MediaController mediaController;
    private AlertView alertView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_convenience);
        initView();
        configImageLoader();
        getImg();
    }

    private void initView() {
        context = this;
        userInfo = new UserInfo();
        userInfo.readData(context);
        addImageList = new ArrayList();
        advertisingImgs = new ArrayList<>();
        nameList = new ArrayList<>();
        if (mVideoPath != null) {
            galleryFinalUtil = new GalleryFinalUtil(8);
        } else {
            galleryFinalUtil = new GalleryFinalUtil(9);
        }

        back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(this);
        view_line = findViewById(R.id.view_line);
        view_lines = findViewById(R.id.view_lines);
        cnnvenience_release = (TextView) findViewById(R.id.cnnvenience_release);
        cnnvenience_release.setOnClickListener(this);
        convenience_phone = (TextView) findViewById(R.id.convenience_phone);
        convenience_phone.setText(userInfo.getUserPhone());
        description = (EditText) findViewById(R.id.description);
        gridview = (GridView) findViewById(R.id.gridview);
        image_linearLayout = (LinearLayout) findViewById(R.id.image_linearLayout);
        tupian = (ImageView) findViewById(R.id.tupian);
        tupian.setOnClickListener(this);
        voice = (ImageView) findViewById(R.id.voice);
        voice.setOnClickListener(this);
        video = (ImageView) findViewById(R.id.video);
        video.setOnClickListener(this);
        videoView = (JCVideoPlayerStandard) findViewById(R.id.JCvideoView);
//        videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setOnClickListener(this);
        myAdv = (TextView) findViewById(R.id.my_adv);
        myAdv.setOnClickListener(this);
        myAdv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        myAdv.getPaint().setColor(getResources().getColor(R.color.msg_red));
        soundView = (SoundView) findViewById(R.id.sound_view);
//        slideImg = (ImageView) findViewById(R.id.slide_pic);
//        slideImg.setOnClickListener(this);
        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);

    }

    /*
    * 获取广告图片
    * */
    private void getImg() {
        String url = NetConstant.GET_SLIDE_LIST_NEW;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("=====显示111", "" + s);
                    JSONObject jsonObject = new JSONObject(s);
                    String state = jsonObject.getString("status");
                    if (state.equals("success")) {
                        String jsonObject1 = jsonObject.getString("data");
                        Gson gson = new Gson();
                        advertisingImgs.clear();
                        List<ADDetial> imgs = gson.fromJson(jsonObject1,
                                new TypeToken<ArrayList<ADDetial>>() {
                                }.getType());
                        advertisingImgs.addAll(imgs);
                        if (imgs.size() > 0) {
                            for (int i = 0; i < imgs.size(); i++) {
//                                infos.get(i).setUrl(NetConstant.NET_DISPLAY_IMG + imgs.get(i).getSlide_pic());
//                                infos.get(i).setContent("http://" + imgs.get(i).getSlide_url());
                                ADInfo info = new ADInfo();
                                info.setUrl(NetConstant.NET_DISPLAY_IMG + advertisingImgs.get(i).getSlide_pic());
                                info.setContent(advertisingImgs.get(i).getSlide_url());
                                infos.add(info);
                            }
                            initialize();
                        }
                    } else {
                        advertisingImgs.clear();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(ReleaseConvenienceActivity.this, "网络连接错误，请检查您的网络");

            }
        });
        request.putValue("typeid", "1");
        request.putValue("cityid", HelperApplication.getInstance().mLocaddresscityid);
        SingleVolleyRequest.getInstance(getContext()).addToRequestQueue(request);
    }

    private void submit() throws IOException {
        descriptionString = description.getText().toString().trim();
        if (TextUtils.isEmpty(descriptionString)) {
            Toast.makeText(this, "描述不能为空", Toast.LENGTH_SHORT).show();
        } else {
            if (addImageList.size() < 1) {
                if (TextUtils.isEmpty(mVideoPath)) {
                    if (TextUtils.isEmpty(soundPath)) {
                        uploadConvenience();
                    } else {
                        uploadSound();
                    }
                } else {
                    uploadVideo();
                }
            } else {
                uploadImgs();
            }
        }

    }

    /**
     * 上传声音
     */
    private void uploadSound() {
        hud.show();
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
                        uploadConvenience();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    hud.dismiss();
                    Toast.makeText(context, "上传录音失败", Toast.LENGTH_SHORT).show();
                }
            });
            SingleVolleyRequest.getInstance(context).addToRequestQueue(request);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
            public void success(boolean state) throws IOException {
                if (!TextUtils.isEmpty(mVideoPath)) {
                    uploadVideo();
                } else if (!TextUtils.isEmpty(soundPath)) {
                    uploadSound();
                } else {
                    uploadConvenience();
                }
            }

            @Override
            public void error() {
                Toast.makeText(getApplication(), "上传图片失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
    * 上传视频
    * */
    private void uploadVideo() throws IOException {
        if (!TextUtils.isEmpty(mVideoPath)) {
            if (!hud.isShowing()) {
                hud.show();
            }
            new PushVideoUtil().pushVideo(getApplication(), mVideoPath, new PushVideo() {
                @Override
                public void success(String videoName, String imageName) {
                    if (!TextUtils.isEmpty(soundPath)) {
                        uploadSound();
                    } else {
                        uploadConvenience();
                    }
                }

                @Override
                public void error() {
                    hud.dismiss();
                    Toast.makeText(getApplication(), "上传视频失败", Toast.LENGTH_SHORT).show();
                    mVideoPath = null;
                    JCVideoPlayer.releaseAllVideos();
                }
            });
        } else {
            uploadConvenience();
        }
    }

    /**
     * 最后上传便民圈
     */
    private void uploadConvenience() {
        if (!hud.isShowing()) {
            hud.show();
        }
        //Toast.makeText(getApplication(), "图片上传成功", Toast.LENGTH_SHORT).show();
        //发布任务
        String url = NetConstant.CONVENIENCE_RELEASE;

        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (hud != null) {
                    hud.dismiss();
                }
                Log.d("---release", s);
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.optString("status").equals("success")) {
                        JSONObject publishresult = object.getJSONObject("data");
                        payId = publishresult.optString("id");
                        HelperApplication.getInstance().trendsBack = true;
                        if (publishresult.optString("status").equals("-2")) {
                            numDialog();
                        } else {
                            packetDailog(false);
                        }
                    } else {
                        Toast.makeText(getApplication(), object.getString("data"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
        request.putValue("phone", convenience_phone.getText().toString());
        request.putValue("type", "1");
        request.putValue("title", convenience_phone.getText().toString());
        request.putValue("content", descriptionString);
        if (addImageList.size() > 0 && mVideoPath != null) {
            String s = StringJoint.arrayJointchar(nameList, ",");
            request.putValue("picurl", s + "," + PushVideoUtil.getPicName());
        } else if (mVideoPath != null && addImageList.size() < 1) {
            request.putValue("picurl", PushVideoUtil.getPicName());
        } else if (mVideoPath == null && addImageList.size() > 0) {
            String s = StringJoint.arrayJointchar(nameList, ",");
            request.putValue("picurl", s);
        } else {
            request.putValue("picurl", "");
        }
        if (!TextUtils.isEmpty(soundName)) {
            request.putValue("sound", soundName);
            request.putValue("soundtime", soundView.getSoundTime() + "");
        } else {
            request.putValue("sound", "");
            request.putValue("soundtime", "");
        }
        if (!TextUtils.isEmpty(PushVideoUtil.getVideoName())) {
            request.putValue("video", PushVideoUtil.getVideoName());
        } else {
            request.putValue("video", "");
        }
        request.putValue("latitude", String.valueOf(HelperApplication.getInstance().mCurrentLocLat));
        request.putValue("longitude", String.valueOf(HelperApplication.getInstance().mCurrentLocLon));
        request.putValue("address", HelperApplication.getInstance().detailAdress);
        SingleVolleyRequest.getInstance(getApplication()).addToRequestQueue(request);
    }


    public void showPicturePicker(View view) {
        PicturePickerDialog picturePickerDialog = new PicturePickerDialog(this);
        picturePickerDialog.show(new PicturePickerDialog.PicturePickerCallBack() {
            @Override
            public void onPhotoClick() {

                Toast.makeText(context, "拍 照", Toast.LENGTH_SHORT).show();
                //获取拍照权限
                if (galleryFinalUtil.openCamera(ReleaseConvenienceActivity.this, (ArrayList<PhotoInfo>) addImageList, REQUEST_CODE_CAMERA, mOnHanlderResultCallback)) {
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
                galleryFinalUtil.openAblum(ReleaseConvenienceActivity.this, (ArrayList<PhotoInfo>) addImageList, REQUEST_CODE_GALLERY, mOnHanlderResultCallback);
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
                if (reqeustCode == REQUEST_CODE_GALLERY) {
                    addImageList.clear();
                }
                addImageList.addAll(resultList);
                Log.d("======haha", addImageList.size() + "");
                gridViewAdapter = new GridViewAdapter(ReleaseConvenienceActivity.this, (ArrayList<PhotoInfo>) addImageList);
                gridview.setAdapter(gridViewAdapter);
                view_line.setVisibility(View.VISIBLE);

            } else {
                view_line.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(ReleaseConvenienceActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tupian:
                showPicturePicker(v);
                break;
            case R.id.cnnvenience_release:
                try {
                    submit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.voice:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                } else {
                    startActivityForResult(new Intent(this, RecordActivity.class), SOUND_CODE);
                }
                break;
            case R.id.video:
                show();

                break;

//            case R.id.btn_play_video:
//                if (ContextCompat.checkSelfPermission(ReleaseConvenienceActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//                        && ContextCompat.checkSelfPermission(ReleaseConvenienceActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//                        && ContextCompat.checkSelfPermission(ReleaseConvenienceActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
//                        && ContextCompat.checkSelfPermission(ReleaseConvenienceActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
//
//                } else {
//                    ActivityCompat.requestPermissions(ReleaseConvenienceActivity.this, new String[]{
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                            Manifest.permission.READ_EXTERNAL_STORAGE,
//                            Manifest.permission.CAMERA,
//                            Manifest.permission.RECORD_AUDIO}, CHECK_PERMISSION);
//                }

//                break;
            case R.id.btn_chose_video:
                MediaOptions.Builder builder = new MediaOptions.Builder();
                MediaOptions options = null;
                options = builder.selectVideo().setMaxVideoDuration(15 * 1000)
                        .setShowWarningBeforeRecordVideo(true).build();

                if (options != null) {

                    MediaPickerActivity.open(this, REQUEST_VIDEO_CODE, options);
                }
                dialog.dismiss();
                break;
            case R.id.video_btn_cancel:
                dialog.dismiss();
                break;
            case R.id.my_adv:
                finish();
                HelperApplication.getInstance().type = "6";
                HelperApplication.getInstance().conAdv = true;
                startActivity(new Intent(context, ReleaseAdvertisingActivity.class));
                break;
        }
    }

    public void show() {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(this).inflate(R.layout.video_dialog, null);
        chooseVideo = (Button) inflate.findViewById(R.id.btn_chose_video);
        cancel = (Button) inflate.findViewById(R.id.video_btn_cancel);
        chooseVideo.setOnClickListener(this);
        cancel.setOnClickListener(this);
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //不是第一次录音
                case SOUND_CODE:
                    if (!TextUtils.isEmpty(soundPath)) {
                        soundView.delete();
                    }
                    soundPath = data.getStringExtra("path");
                    int time = data.getIntExtra("time", 0);
                    soundView.setVisibility(View.VISIBLE);
                    soundView.init(soundPath, time);
                    break;
                case CAMERA_RQ:
                    try {
                        String filePath = data.getStringExtra("videoUrl");
                        Log.e("lzf_video", filePath);
                        if (filePath != null && !filePath.equals("")) {
                            if (filePath.startsWith("file://")) {
                                filePath = data.getStringExtra("videoUrl").substring(7, filePath.length());
                                description.setText("视频保存在：" + filePath);
                            }
                        }
                    } catch (Exception ex) {

                    }
                    break;
                case REQUEST_VIDEO_CODE:
                    dialog.dismiss();


                    mMediaSelectedList = MediaPickerActivity
                            .getMediaItemSelected(data);
                    if (mMediaSelectedList != null) {
                        for (MediaItem mediaItem : mMediaSelectedList) {
                            mVideoPath = null;
                            mVideoPath = mediaItem.getPathOrigin(context);
                            if (mVideoPath != null) {
//                                int length = mVideoPath.length();
//                                String str = mVideoPath.substring(length-4, length);
//                                if (str.equals(".mp4")){
                                videoView.setVisibility(View.VISIBLE);
                                videoView.setUp(mVideoPath, JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
//                                }else {
//                                    ToastUtil.showShort(context,"请选择MP4格式视频");
//                                }
                            }

//                            videoView.setVideoURI(mediaItem.getUriOrigin());
//                            mediaController = new MediaController(this);
//                            mediaController.setAnchorView(videoView);
//                            mediaController.setKeepScreenOn(true);
//
//                            videoView.setMediaController(mediaController);
                        }
                    } else {
                        Log.e("===", "Error to get media, NULL");
                    }
                    break;

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                startActivityForResult(new Intent(this, RecordActivity.class), SOUND_CODE);
            } else {
                // Permission Denied
            }
        }
        if (requestCode == CHECK_PERMISSION
                && grantResults.length == 4
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED
                && grantResults[3] == PackageManager.PERMISSION_GRANTED) {

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

    private void numDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReleaseConvenienceActivity.this);
        builder.setMessage("您当天的免费发布次数已用完，是否购买更多");
        builder.setTitle("系统提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                packetDailog(true);
            }

        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                startActivity(new Intent(ReleaseConvenienceActivity.this, MyMessageActivity.class));
            }
        });
        builder.show();
    }

    //弹出是否发红包dialog
    private void packetDailog(final boolean needBuyMore) {
        String posiStr = "", nagvStr = "", neuStr = "";
        if (needBuyMore) {
            posiStr = "去发红包和发布此便民圈";
            nagvStr = "只发布便民圈，不发红包";
            neuStr = "不发布此条便民圈";
        } else {
            posiStr = "去发红包";
            nagvStr = "只发布便民圈，不发红包";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("系统提示");
        builder.setMessage("便民圈可以发红包啦！发红包能让更多的人关注您的便民圈，而且在全国展示呦！是否要发红包？");
        builder.setCancelable(false);
        builder.setPositiveButton(posiStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //红包+便民圈
                if (needBuyMore) {
                    getMessagePrice(true);
                } else {
                    //发红包页面
                    Intent intent = new Intent(ReleaseConvenienceActivity.this, SendPacketActivity.class);
                    intent.putExtra("mid", payId);
                    intent.putExtra("messagePrice", "0");//此条件下不需要支付便民圈费用
                    startActivity(intent);
                    finish();
                }
            }
        });
        builder.setNegativeButton(nagvStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (needBuyMore) {
                    //获取便民圈单价
                    getMessagePrice(false);
                } else {
                    Toast.makeText(ReleaseConvenienceActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        if (!TextUtils.isEmpty(neuStr)) {
            builder.setNeutralButton(neuStr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    startActivity(new Intent(ReleaseConvenienceActivity.this, MyMessageActivity.class));
                }
            });
        }

        builder.show();
    }

    //获取便民圈价格
    private void getMessagePrice(final boolean needPacket) {
        String url = NetConstant.GET_MESSAGE_PRICE;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.d("---price", s);
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.optString("status").equals("success")) {
                        String messagePrice = object.optString("data");
                        if (needPacket) {
                            //发红包页面
                            Intent intent = new Intent(ReleaseConvenienceActivity.this, SendPacketActivity.class);
                            intent.putExtra("mid", payId);
                            intent.putExtra("messagePrice", messagePrice);//此条件下需要支付便民圈费用
                            startActivity(intent);
                        } else {
                            Intent payIntent = new Intent(ReleaseConvenienceActivity.this, PaymentActivity.class);
                            payIntent.putExtra("paytype", "ConveniencePay");
                            payIntent.putExtra("body", "便民圈");
                            payIntent.putExtra("tradeNo", "M" + payId);
                            payIntent.putExtra("price", messagePrice);
                            payIntent.putExtra("type", "3");
                            startActivity(payIntent);
                        }
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplication(), "网络错误，检查您的网络", Toast.LENGTH_SHORT).show();
            }
        });

        request.putValue("userid", userInfo.getUserId());
        SingleVolleyRequest.getInstance(getApplication()).addToRequestQueue(request);
    }

    private void play(final String path) {

        if (!TextUtils.isEmpty(mVideoPath)) {
            videoView.setVisibility(View.VISIBLE);
        }
//
//        boolean setUp = videoView.setUp(path, JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
//        if (setUp) {
//            Glide.with(ReleaseConvenienceActivity.this).load(NetConstant.NET_DISPLAY_IMG + PushVideoUtil.getPicName()).into(videoView.thumbImageView);
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (AudioPlayer.isPlaying) {
            AudioPlayer.getInstance().stopPlay();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new ContextWrapper(newBase) {
            @Override
            public Object getSystemService(String name) {
                if (Context.AUDIO_SERVICE.equals(name))
                    return getApplicationContext().getSystemService(name);
                return super.getSystemService(name);
            }
        });
    }
//
//    @Override
//    public void onBackPressed() {
//        if (JCVideoPlayer.backPress()) {
//            return;
//        }
//        super.onBackPressed();
//    }

    @Override
    protected void onPause() {
//        mVideoPath= null;
        if (AudioPlayer.isPlaying) {
            AudioPlayer.getInstance().stopPlay();
        }
        super.onPause();
    }

    @SuppressLint("NewApi")
    private void initialize() {

        cycleViewPager = (CycleViewPager) getFragmentManager()
                .findFragmentById(R.id.fragment_cycle_viewpager_content);

//        for (int i = 0; i < imageUrls.length; i++) {
//            ADInfo info = new ADInfo();
////            info.setUrl(NetConstant.NET_HOST +advertisingImgs.get(i).getSlide_pic());
////            info.setContent("http://"+advertisingImgs.get(i).getSlide_url() );
//            info.setUrl(imageUrls[i]);
//            info.setContent("图片-->" + i);
//            infos.add(info);
//        }

        // 将最后一个ImageView添加进来
        views.add(ViewFactory.getImageView(this, infos.get(infos.size() - 1).getUrl()));
        for (int i = 0; i < infos.size(); i++) {
            views.add(ViewFactory.getImageView(this, infos.get(i).getUrl()));

        }
        if (infos.size() < 3) {
            for (int j = 0; j < 3 - infos.size(); j++) {
                Log.d("==infosize", infos.size() + "");
                views.add(ViewFactory.getImageViewforid(this, R.drawable.adv));
            }
        }
        // 将第一个ImageView添加进来
        views.add(ViewFactory.getImageView(this, infos.get(0).getUrl()));

        // 设置循环，在调用setData方法前调用
        cycleViewPager.setCycle(true);
        // 在加载数据前设置是否循环
        cycleViewPager.setData(views, infos, mAdCycleViewListener);
        //设置轮播
        cycleViewPager.setWheel(true);
        // 设置轮播时间，默认5000ms
        cycleViewPager.setTime(2000);
        //设置圆点指示图标组居中显示，默认靠右
        cycleViewPager.setIndicatorCenter();
    }

    private CycleViewPager.ImageCycleViewListener mAdCycleViewListener = new CycleViewPager.ImageCycleViewListener() {

        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
            if (cycleViewPager.isCycle()) {
                position = position - 1;

                Intent intent = new Intent(context, AdvImgDetialActivity.class);
                intent.putExtra("path", info.getUrl());
                intent.putExtra("url", info.getContent());
                startActivity(intent);
//                Toast.makeText(ReleaseConvenienceActivity.this,
//                        "position-->" + info.getContent()+info.getUrl(), Toast.LENGTH_SHORT)
//                        .show();

            }

        }

    };

    /**
     * 配置ImageLoder
     */
    private void configImageLoader() {
        // 初始化ImageLoader
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.icon_stub) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }


}
