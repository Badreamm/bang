package cn.xcom.helper.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import cn.xcom.helper.R;
import cn.xcom.helper.fragment.mymessage.IsReleaseFragment;
import cn.xcom.helper.fragment.mymessage.NoReleaseFragment;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class MyMessageActivity extends BaseActivity implements View.OnClickListener {
    RelativeLayout back;
    FrameLayout tabcontent;
    FragmentTabHost tabhost;
    FrameLayout dataRealtabcontent;
    LinearLayout activityNewDetial;
    private LayoutInflater layoutInflater;
    private Class fragmentArray[] = {NoReleaseFragment.class, IsReleaseFragment.class};
    private int colorsArray[] = {R.color.white, R.color.colorPrimary};
    private String mTextviewArray[] = {"待发布", "已发布"};
    private int viewVisbility[] = {View.VISIBLE, View.GONE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_msg);
        initView();
    }

    private void initView() {
        back = (RelativeLayout) findViewById(R.id.rl_msg_back);
        back.setOnClickListener(this);
        tabcontent = (FrameLayout) findViewById(android.R.id.tabcontent);
        tabhost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        dataRealtabcontent = (FrameLayout) findViewById(R.id.data_realtabcontent);
        activityNewDetial = (LinearLayout) findViewById(R.id.activity_my_msg);
        layoutInflater = LayoutInflater.from(this);
        tabhost.setup(this, getSupportFragmentManager(), R.id.data_realtabcontent);
        int count = fragmentArray.length;
        for (int i = 0; i < count; i++) {

            TabHost.TabSpec tabSpec = tabhost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));

            tabhost.addTab(tabSpec, fragmentArray[i], null);

//            tabhost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
        }
    }

    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.fragment_msg_release_item, null);
        TextView textView = (TextView) view.findViewById(R.id.data_item_title);
        textView.setText(mTextviewArray[index]);


//        textView.setTextColor(colorsArray[index]);
//        textView.setBackgroundResource(mImageViewArray[index]);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_msg_back:
                finish();
                break;
        }
    }
}
