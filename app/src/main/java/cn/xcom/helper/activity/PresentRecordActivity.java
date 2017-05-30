package cn.xcom.helper.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.xcom.helper.R;
import cn.xcom.helper.adapter.PresentRecordAdapter;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.LogUtils;
import cn.xcom.helper.view.DividerItemDecoration;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 16/6/12.
 * 提现记录页
 */
public class PresentRecordActivity extends BaseActivity implements View.OnClickListener {
    private String TAG = "PresentRecordActivity";
    private Context mContext;
    private RelativeLayout rl_back;
    private XRecyclerView mRecyclerView;
    private PresentRecordAdapter mAdapter;
    private UserInfo userInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_present_record);
        mContext = this;
        initView();
    }

    private void initView() {
        rl_back = (RelativeLayout) findViewById(R.id.rl_present_record_back);
        rl_back.setOnClickListener(this);
        mRecyclerView = (XRecyclerView) findViewById(R.id.lv_present_record);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getPresentRecordList();
            }

            @Override
            public void onLoadMore() {
                mRecyclerView.loadMoreComplete();
            }
        });
        userInfo = new UserInfo(mContext);
        getPresentRecordList();

    }

    private void getPresentRecordList() {
        final RequestParams requestParams = new RequestParams();
        requestParams.put("userid", userInfo.getUserId());
        HelperAsyncHttpClient.get(NetConstant.NET_GET_WITHDRAW_LOG, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG, "--statusCode->" + statusCode + "==>" + response.toString());
                if (response != null) {
                    try {
                        String state = response.getString("status");
                        if (state.equals("success")) {
                            JSONArray jsonArray = response.getJSONArray("data");
                            mAdapter = new PresentRecordAdapter(jsonArray);
                            mRecyclerView.setAdapter(mAdapter);
                        } else if (state.equals("error")) {
                            String date = response.getString("data");
                            Toast.makeText(mContext, date, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mRecyclerView.refreshComplete();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_present_record_back:
                finish();
                break;
        }

    }


}
