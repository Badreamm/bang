package cn.xcom.helper.activity;
/*
  * 加载便民圈的图片，并且点击放大
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.adapter.ViewPageAdapter;
import cn.xcom.helper.utils.SaveImageUtils;
import cn.xcom.helper.utils.SmoothImageView;
import cn.xcom.helper.utils.ToastUtil;

public class SpaceImageDetailActivity extends BaseActivity {
    private int mLocationX;
    private int mLocationY;
    private int mWidth;
    private int mHeight;
    private ImageView imageView1;
    private SmoothImageView imageView;
    private List convenience;
    private Context context;
    private List addViewList;//添加图片的list
    private ViewPager viewPager;
    private ViewPageAdapter viewPageAdapter;
    private RelativeLayout rl_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_space_image_detail);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //getSupportActionBar().hide();
        context=this;
        addViewList=new ArrayList();
        convenience=new ArrayList<>();
        Intent intent=getIntent();
        int ID=getIntent().getIntExtra("position", 0);
        viewPager= (ViewPager) findViewById(R.id.view_pager);
        convenience= (List) intent.getSerializableExtra("list111");
        mLocationX = getIntent().getIntExtra("locationX", 0);
        mLocationY = getIntent().getIntExtra("locationY", 0);
        mWidth = getIntent().getIntExtra("width", 0);
        mHeight = getIntent().getIntExtra("height", 0);
        imageView = new SmoothImageView(this);
        imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
        imageView.transformIn();
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            for (int i=0;i<convenience.size();i++){
                imageView1=new ImageView(context);
                imageView.buildDrawingCache();
                Bitmap bmap = imageView.getDrawingCache();
                imageView1.setImageBitmap(bmap);
                ImageLoader.getInstance().displayImage((String) convenience.get(i), imageView1);
                imageView1.setScaleType(ImageView.ScaleType.FIT_CENTER);
                addViewList.add(imageView1);
            }

        viewPageAdapter=new ViewPageAdapter(addViewList,context);
        viewPager.setAdapter(viewPageAdapter);
        viewPager.setCurrentItem(ID);


    }

}
