package cn.xcom.helper.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import cn.xcom.helper.R;
import cn.xcom.helper.adapter.CouponAdapter;

/**
 * Created by zhuchongkun on 16/6/8.
 * 优惠券页
 */
public class CouponActivity extends BaseActivity implements View.OnClickListener{
    private String TAG="CouponActivity";
    private Context mContext;
    private RelativeLayout rl_back;
    private XRecyclerView mRecyclerView;
    private CouponAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_coupon);
        mContext=this;
        initView();
    }

    private void initView(){
        rl_back= (RelativeLayout) findViewById(R.id.rl_coupon_back);
        rl_back.setOnClickListener(this);
        mRecyclerView= (XRecyclerView) findViewById(R.id.lv_coupon);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
//        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {

            }
        });
        mAdapter = new CouponAdapter();

        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setRefreshing(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_coupon_back:
                finish();
                break;
        }
    }
}
