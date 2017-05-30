package cn.xcom.helper.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.widget.RelativeLayout;

import com.lzy.widget.tab.PagerSlidingTabStrip;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xcom.helper.R;
import cn.xcom.helper.fragment.mytask.TaskAlrCancelFragment;
import cn.xcom.helper.fragment.mytask.TaskInProgressFragment;
import cn.xcom.helper.fragment.mytask.TaskNonBeginFragment;

public class MyTaskActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.task_tab)
    PagerSlidingTabStrip taskTab;
    @BindView(R.id.task_viewpager)
    ViewPager taskViewpager;
    private Context context;
    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_task);
        ButterKnife.bind(this);
        context = this;
        setFragment();
    }

    /**
     * 设置fragment
     */
    private void setFragment() {
        fragments = new ArrayList<>();
        TaskNonBeginFragment fragment1 = new TaskNonBeginFragment();
        TaskInProgressFragment fragment2 = new TaskInProgressFragment();
        TaskAlrCancelFragment fragment3 = new TaskAlrCancelFragment();
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        taskViewpager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        taskTab.setViewPager(taskViewpager);
    }

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }

    /**
     * viewpager适配器
     */
    private class MyAdapter extends FragmentPagerAdapter {

        private String[] titles = {"未开始","进行中","已取消"};

        public MyAdapter(FragmentManager fm) {
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
}
