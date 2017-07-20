package cn.xcom.helper.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.lzy.widget.tab.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.fragment.order.MyPostOrderFragment;

/**
 * Created by zhuchongkun on 16/6/12.
 * 我的发单页
 */
public class BillActivity extends BaseActivity implements View.OnClickListener{
    private Context mContext;
    private RelativeLayout rl_back;
    private PagerSlidingTabStrip pagerTitles;
    private ViewPager viewPager;
    private List<Fragment> fragments;
    private int from;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bill);
        mContext=this;
        from = getIntent().getIntExtra("from",0);
        initView();

    }

    private void initView(){
        rl_back= (RelativeLayout) findViewById(R.id.rl_bill_back);
        rl_back.setOnClickListener(this);
        pagerTitles = (PagerSlidingTabStrip) findViewById(R.id.order_title);
        viewPager = (ViewPager) findViewById(R.id.vp_my_post);
        MyPostOrderFragment fragment1 = new MyPostOrderFragment().newInstance(1);
        MyPostOrderFragment fragment2 = new MyPostOrderFragment().newInstance(2);
        MyPostOrderFragment fragment3 = new MyPostOrderFragment().newInstance(3);
        MyPostOrderFragment fragment4 = new MyPostOrderFragment().newInstance(4);

        fragments = new ArrayList<>();
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);

        PostedOrderAdapter adapter = new PostedOrderAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        pagerTitles.setViewPager(viewPager);
        if(from == 1){
            viewPager.setCurrentItem(1);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_bill_back:
                finish();
                break;
        }
    }


    private class PostedOrderAdapter extends FragmentPagerAdapter{
        private String titles[] = {"未抢单","已被抢","已上门","已完成"};

        public PostedOrderAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
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

    public void changePager(){
        viewPager.setCurrentItem(3);
    }

}
