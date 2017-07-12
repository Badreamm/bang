package cn.xcom.helper.fragment.onyuanbuy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import cn.xcom.helper.adapter.OybGoodAdapter;
import cn.xcom.helper.bean.OybGood;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cz.msebera.android.httpclient.Header;

/**
 * Created by hzh on 2017/7/8.
 * 一元购商品页面
 */

public class OybGoodsFragment extends Fragment {
    public static final int ALL_GOODS = 111;//所有商品
    public static final int WAIT_GOODS = 122;//待揭晓
    public static final int ANNO_GOODS = 123;//已揭晓

    private int type;
    private XRecyclerView recyclerView;
    private List<OybGood> goodLists;
    private OybGoodAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        type = getArguments().getInt("type");
        View view = inflater.inflate(R.layout.fragment_oyb_goods, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDate();
    }

    private void initView(View view) {
        goodLists = new ArrayList<>();
        recyclerView = (XRecyclerView) view.findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getDate();
            }

            @Override
            public void onLoadMore() {
                recyclerView.loadMoreComplete();
            }
        });
        adapter = new OybGoodAdapter(getContext(),goodLists,type);
        recyclerView.setAdapter(adapter);
    }

    private void getDate() {
        String url = "";
        switch (type){
            case ALL_GOODS:
                url = NetConstant.GET_OYB_GOODS;
                break;
            case WAIT_GOODS:
                url = NetConstant.GET_OYB_WAIT;
                break;
            case ANNO_GOODS:
                url = NetConstant.GET_ANNO_GODD;
                break;
        }

        RequestParams requestParams = new RequestParams();

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
                                    List<OybGood> list = new Gson().fromJson(data,
                                            new TypeToken<List<OybGood>>() {
                                            }.getType());
                                    goodLists.clear();
                                    goodLists.addAll(list);
                                    adapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        recyclerView.refreshComplete();
                    }
                });
    }

    public static final OybGoodsFragment newInstance(int type){
        OybGoodsFragment fragment = new OybGoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        fragment.setArguments(bundle);
        return  fragment;
    }



}
