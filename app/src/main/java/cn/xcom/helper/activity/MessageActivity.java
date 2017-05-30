package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import cn.xcom.helper.R;
import cn.xcom.helper.adapter.MessageAdapter;

/**
 * Created by zhuchongkun on 16/6/12.
 * 消息页
 */
public class MessageActivity extends BaseActivity implements View.OnClickListener{
    private String TAG="MessageActivity";
    private Context mContext;
    private RelativeLayout rl_back;
    private XRecyclerView mRecyclerView;
    private MessageAdapter mAdapter;
    private TextView tv_system,tv_user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_message);
        mContext=this;
        initView();
    }

    private void initView(){
        //
        tv_system= (TextView) findViewById(R.id.tv_message_system);
        tv_system.setOnClickListener(this);
        tv_user= (TextView) findViewById(R.id.tv_message_user);
        tv_user.setOnClickListener(this);
        //
        rl_back= (RelativeLayout) findViewById(R.id.rl_message_back);
        rl_back.setOnClickListener(this);
        mRecyclerView= (XRecyclerView) findViewById(R.id.lv_message);
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
        mAdapter=new MessageAdapter();
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_message_back:
                finish();
                break;
            case R.id.tv_message_system:
                startActivity(new Intent(mContext,SystemMessageActivity.class));
                break;
            case R.id.tv_message_user:
                startActivity(new Intent(mContext,UserMessageActivity.class));
                break;
        }
    }
}
