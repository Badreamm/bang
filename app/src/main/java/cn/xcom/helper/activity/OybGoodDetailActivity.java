package cn.xcom.helper.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.youth.banner.Banner;

import cn.xcom.helper.R;

/**
 * Created by hzh on 2017/7/8.
 */

public class OybGoodDetailActivity extends BaseActivity {
    private XRecyclerView recyclerView;
    private Banner banner;
    private TextView needTv,nowTv,countTv;
    private String mark;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_oyb_good_detail);
        mark = getIntent().getStringExtra("mark");
    }

    private void initView(){
        recyclerView = (XRecyclerView) findViewById(R.id.recycler_view);
        View headView = LayoutInflater.from(this).inflate(R.layout.headviewt_oyb_good_detail,null);
        banner = (Banner) headView.findViewById(R.id.banner);
        needTv = (TextView) headView.findViewById(R.id.need_count_tv);
        nowTv = (TextView) headView.findViewById(R.id.now_count_tv);
        countTv = (TextView) headView.findViewById(R.id.left_count_tv);
    }
}
