package cn.xcom.helper.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.lzy.widget.tab.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.fragment.packet.PacketRecordFragment;

/**
 * Created by hzh on 2017/6/3.
 */

public class PacketRecordActivity extends BaseActivity {
    private ViewPager viewPager;
    private PagerSlidingTabStrip tabTitle;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packet_record);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabTitle = (PagerSlidingTabStrip) findViewById(R.id.tab_title);
        PacketRecordFragment fragment = PacketRecordFragment.newInstance(PacketRecordFragment.INCOME);
        PacketRecordFragment fragment1 = PacketRecordFragment.newInstance(PacketRecordFragment.SENDOUT);
        fragments = new ArrayList<>();
        fragments.add(fragment);
        fragments.add(fragment1);
        RecordFragmentAdapter fragmentAdapter = new RecordFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);
        tabTitle.setViewPager(viewPager);

    }

    public class RecordFragmentAdapter extends FragmentPagerAdapter {
        private String[] titles = {"我收到的红包", "我发出的红包"};

        public RecordFragmentAdapter(FragmentManager fm) {
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


    public void onBack(View view) {
        finish();
    }
}
