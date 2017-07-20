package cn.xcom.helper.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.lzy.widget.tab.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.fragment.onyuanbuy.OybGoodsFragment;
import cn.xcom.helper.fragment.onyuanbuy.OybMyOrderFragment;
import cn.xcom.helper.view.AutoScrollTextView;

/**
 * Created by hzh on 2017/7/8.
 */

public class OneYuanBuyFragment extends Fragment {
    private PagerSlidingTabStrip slidingTab;
    private ViewPager viewPager;
    private List<Fragment> fragments;
    private AutoScrollTextView alwaysMarqueeTextView;
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
        alwaysMarqueeTextView = (AutoScrollTextView) view.findViewById(R.id.item_title);
        alwaysMarqueeTextView.init(getActivity().getWindowManager());
        alwaysMarqueeTextView.startScroll();
        alwaysMarqueeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://buy.cqcp.net/gamedraw/ssc/open.shtml");
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });
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
