package cn.xcom.helper.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzy.widget.tab.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.adapter.ViewPageAdapter;
import cn.xcom.helper.fragment.onyuanbuy.OybGoodsFragment;
import cn.xcom.helper.fragment.onyuanbuy.OybMyOrderFragment;
import cn.xcom.helper.view.AlwaysMarqueeTextView;

/**
 * Created by hzh on 2017/7/8.
 */

public class OneYuanBuyFragment extends Fragment {
    private PagerSlidingTabStrip slidingTab;
    private ViewPager viewPager;
    private List<Fragment> fragments;
    private AlwaysMarqueeTextView alwaysMarqueeTextView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_yuan_buy,container,false);
        initView(view);
        return view;
    }

    private void initView(View view){
        fragments = new ArrayList<>();
        fragments.add(OybGoodsFragment.newInstance(OybGoodsFragment.ALL_GOODS));
        fragments.add(OybGoodsFragment.newInstance(OybGoodsFragment.WAIT_GOODS));
        fragments.add(OybGoodsFragment.newInstance(OybGoodsFragment.ANNO_GOODS));
        fragments.add(new OybMyOrderFragment());
        slidingTab = (PagerSlidingTabStrip) view.findViewById(R.id.slid_tab);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new FragmentAdapter(getFragmentManager()));
        slidingTab.setViewPager(viewPager);
        alwaysMarqueeTextView = (AlwaysMarqueeTextView) view.findViewById(R.id.item_title);
        alwaysMarqueeTextView.setText("本活动所有开奖结果皆由重庆时时彩获取，按开奖号码位数加1，保证公平性，点击查看");

    }

    private class FragmentAdapter extends FragmentPagerAdapter{
        private String titles[] = {"商品","待揭晓","已揭晓","我的订单"};

        public FragmentAdapter(FragmentManager fm) {
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

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

}
