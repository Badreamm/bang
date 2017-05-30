package cn.xcom.helper.activity;


import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.fragment.Authorized.BindAccountAuthorizedFragment;

/**
 * Created by zhuchongkun on 16/6/3.
 * 身份认证页
 */
public class Authorized2Activity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private String TAG = "AuthorizedActivity";
    private Context mContext;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 4;
    private ViewPager viewPager;
    private List<Fragment> fragments;
    private TextView photoTitleTv,bindBankTitleTv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_authorized);
        mContext = this;
        initView();

    }

    private void initView() {
        /*photoTitleTv = (TextView) findViewById(R.id.tv_title_photo);
        photoTitleTv.setOnClickListener(this);*/
        bindBankTitleTv = (TextView) findViewById(R.id.tv_title_bind);
        bindBankTitleTv.setOnClickListener(this);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        fragments = new ArrayList<>();
        /*fragments.add(new PhotoAuthorizedFragment());*/
        fragments.add(new BindAccountAuthorizedFragment());
        viewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(this);

    }



    public class MyFragmentAdapter extends FragmentPagerAdapter {


        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }


    @Override
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*case R.id.tv_title_photo:
                viewPager.setCurrentItem(0);
                break;*/
            case R.id.tv_title_bind:
                viewPager.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        /*if(position == 0){
            photoTitleTv.setTextColor(getResources().getColor(R.color.colorTheme));
            bindBankTitleTv.setTextColor(getResources().getColor(R.color.text_black));
        }else if(position == 1){
            photoTitleTv.setTextColor(getResources().getColor(R.color.text_black));
            bindBankTitleTv.setTextColor(getResources().getColor(R.color.colorTheme));
        }*/
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public void onBack(View v) {
        finish();
    }



}
