package cn.xcom.helper.fragment.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import cn.xcom.helper.adapter.MyOrderAdapter;
import cn.xcom.helper.bean.OrderHelper;
import cn.xcom.helper.bean.ShopGoodInfo;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.view.DividerItemDecoration;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/10/18 0018.
 * 订单fragment
 */

public class MyOrderFragment extends Fragment {
    public static final int MY_ORDER_REQUEST = 1000;
    private static final int CANCEL_SUCCESS = 101;
    private static final int PAY_SUCCESS = 113;
    private static final int COMMENT_SUCCESS = 112;
    private int orderState;//1全部 2待付款 3待消费 4待评价
    private int orderType;//商户或者买家
    private Context mContext;
    private XRecyclerView mRecyclerView;
    private UserInfo userInfo;
    private List<ShopGoodInfo> shopGoodInfos;
    private MyOrderAdapter myOrderAdapter;
    private boolean firstIn = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderState = getArguments().getInt("state");
        orderType = getArguments().getInt("type");
        Log.d("==order",orderState+"");
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

    @Override
    public void onResume() {
        super.onResume();
        if (!firstIn){
            getOrder();

        }
        firstIn = false;
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
        shopGoodInfos = new ArrayList<>();
        myOrderAdapter = new MyOrderAdapter(mContext, shopGoodInfos, MyOrderFragment.this, orderType);

        mRecyclerView.setAdapter(myOrderAdapter);

    }

    private void getOrder() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", userInfo.getUserId());
        switch (orderState) {
            case 1:
                requestParams.put("state", "1,2,3,4,5,10");
                break;
            case 2:
                requestParams.put("state", "1");
                break;
            case 3:
                requestParams.put("state", "2,3");
                break;
            case 4:
                requestParams.put("state", "4");
                break;
        }
        String url;
        if (orderType == OrderHelper.BuyerOrder) {
            url = NetConstant.BUYER_GET_SHOP_ORDER_LIST;
        } else {
            url = NetConstant.SELLER_GET_SHOPPING_ORDER_LIST;
        }

        HelperAsyncHttpClient.get(url, requestParams,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            try {
                                String state = response.getString("status");
                                if (state.equals("success")) {
                                    String data = response.getString("data");
                                    shopGoodInfos.clear();
                                    List<ShopGoodInfo> list = new Gson().fromJson(data,
                                            new TypeToken<List<ShopGoodInfo>>() {
                                            }.getType());
                                    shopGoodInfos.addAll(list);
                                    myOrderAdapter.notifyDataSetChanged();
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
        if (requestCode == MY_ORDER_REQUEST) {
            switch (resultCode) {
                case CANCEL_SUCCESS:
                    getOrder();
                    break;
                case COMMENT_SUCCESS:
                    getOrder();
                    break;
                case PAY_SUCCESS:
                    getOrder();

            }

        }

    }

    public static final MyOrderFragment newInstance(int orderState, int orderType) {
        MyOrderFragment myOrderFragment = new MyOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("state", orderState);
        bundle.putInt("type", orderType);
        myOrderFragment.setArguments(bundle);
        return myOrderFragment;
    }


}
