package cn.xcom.helper.fragment.order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.activity.MyPostOrderDetailActivity;
import cn.xcom.helper.adapter.OrderRecyclerViewAdapter;
import cn.xcom.helper.bean.TaskItemInfo;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.view.DividerItemDecoration;
import cz.msebera.android.httpclient.Header;


/**
 * Created by Administrator on 2016/10/14 0014.
 * 我的发单fragment
 */

public class MyPostOrderFragment extends Fragment {
    public static final int POST_ORDER_REQUEST_CODE = 10002;
    public static final int CANCEL_ORDER_RESULT_CODE = 111;
    public static final int COMMENT_RESULT_CODE = 112;
    public static final int PAY_RESULT_CODE = 113;

    private Context mContext;
    private XRecyclerView mRecyclerView;
    private UserInfo userInfo;
    private int orderType;
    private List<TaskItemInfo> taskItemInfos;
    private OrderRecyclerViewAdapter adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderType = getArguments().getInt("order_type");
        mContext = getContext();
        userInfo = new UserInfo(mContext);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        initView(view);
        getOrder();
        return view;
    }


    private void initView(View v) {
        mRecyclerView = (XRecyclerView) v.findViewById(R.id.rv_order);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getOrder();
            }

            @Override
            public void onLoadMore() {
                mRecyclerView.loadMoreComplete();
            }
        });
        taskItemInfos = new ArrayList<>();
        adapter = new OrderRecyclerViewAdapter(mContext, taskItemInfos,MyPostOrderFragment.this,orderType);

        mRecyclerView.setAdapter(adapter);


    }

    private void getOrder() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", userInfo.getUserId());
        switch (orderType) {
            case 1:
                requestParams.put("state", "1,-1");
                break;
            case 2:
                requestParams.put("state", "2");
                break;
            case 3:
                requestParams.put("state", "3,4");
                break;
            case 4:
                requestParams.put("state", "5,6,7,10");
                break;
        }
        HelperAsyncHttpClient.get(NetConstant.GET_TASK_LIST_BY_USERID, requestParams,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            try {
                                String state = response.getString("status");
                                if (state.equals("success")) {
                                    String data = response.getString("data");
                                    taskItemInfos.clear();
                                    List<TaskItemInfo> list = new Gson().fromJson(data,
                                            new TypeToken<List<TaskItemInfo>>() {
                                            }.getType());
                                    taskItemInfos.addAll(list);
                                    adapter.notifyDataSetChanged();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == POST_ORDER_REQUEST_CODE){
            switch (resultCode){
                case CANCEL_ORDER_RESULT_CODE:
                    getOrder();
                    break;
                case COMMENT_RESULT_CODE:
                    getOrder();
                    break;
                case PAY_RESULT_CODE:
                    getOrder();
            }

        }
    }

    public static final MyPostOrderFragment newInstance(int orderType) {
        MyPostOrderFragment myPostOrderFragment = new MyPostOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("order_type", orderType);
        myPostOrderFragment.setArguments(bundle);
        return myPostOrderFragment;
    }

    @Override
    public void onResume() {
        getOrder();
        super.onResume();
    }
}
