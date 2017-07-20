package cn.xcom.helper.fragment.onyuanbuy;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.adapter.OybGoodAdapter;
import cn.xcom.helper.bean.OybGood;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.TimeUtils;
import cz.msebera.android.httpclient.Header;

/**
 * Created by hzh on 2017/7/8.
 * 一元购商品页面
 */

public class OybGoodsFragment extends Fragment implements Runnable {
    public static final int ALL_GOODS = 111;//所有商品
    public static final int WAIT_GOODS = 122;//待揭晓
    public static final int ANNO_GOODS = 123;//已揭晓

    private int type;
    private XRecyclerView recyclerView;
    private List<OybGood> goodLists;
    private OybGoodAdapter adapter;
    private boolean isRunning;
    private Thread thread;

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
        adapter = new OybGoodAdapter(getContext(), goodLists, type);
        recyclerView.setAdapter(adapter);
    }


    private void getDate() {
        String url = "";
        switch (type) {
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
                                Log.e("oyb", type + ":" + response.toString());
                                String state = response.getString("status");
                                if (state.equals("success")) {
                                    JSONArray arr = response.getJSONArray("data");
                                    for (int i = 0; i < arr.length(); i++) {
                                        JSONObject object = arr.getJSONObject(i);
                                        if (object.opt("smeta").equals("")) {
                                            object.put("smeta", null);
                                        }
                                    }
                                    String data = arr.toString();

                                    List<OybGood> list = new Gson().fromJson(data,
                                            new TypeToken<List<OybGood>>() {
                                            }.getType());
                                    goodLists.clear();
                                    goodLists.addAll(list);
                                    adapter.notifyDataSetChanged();
                                    setTime();
                                    if (type == WAIT_GOODS && isRunning == false && goodLists.size() > 0) {
                                        isRunning = true;
                                        thread = new Thread(OybGoodsFragment.this);
                                        thread.start();
                                    }
                                } else {
                                    isRunning = false;
                                    goodLists.clear();
                                    adapter.notifyDataSetChanged();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        recyclerView.refreshComplete();
                    }
                });
    }

    private void setTime() {
        for (int i = 0; i < goodLists.size(); i++) {
            OybGood good = goodLists.get(i);
            if (type == OybGoodsFragment.WAIT_GOODS) {
                long count = TimeUtils.timeDifferent(good.getTime());
                goodLists.get(i).setCountTime(count);
            }
        }
    }

    @Override
    public void run() {
        while (isRunning && type == WAIT_GOODS) {
            try {
                //线程每秒钟执行一次
                Thread.sleep(1000);
                //遍历商品列表
                for (int i = 0; i < goodLists.size(); i++) {
                    //拿到每件商品的时间差，转化为具体的多少天多少小时多少分多少秒
                    //并保存在商品time这个属性内
                    if (type == OybGoodsFragment.WAIT_GOODS) {
                        long counttime = goodLists.get(i).getCountTime();
                        long days = counttime / (1000 * 60 * 60 * 24);
                        long hours = (counttime - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                        long minutes = (counttime - days * (1000 * 60 * 60 * 24)
                                - hours * (1000 * 60 * 60)) / (1000 * 60);
                        long second = (counttime - days * (1000 * 60 * 60 * 24)
                                - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000;
                        //并保存在商品time这个属性内
                        String finaltime = days + "天" + hours + "时" + minutes + "分" + second + "秒";
                        goodLists.get(i).setShowTime(finaltime);
                        //如果时间差大于1秒钟，将每件商品的时间差减去一秒钟，
                        // 并保存在每件商品的counttime属性内
                        if (counttime > 1000 || second < 0) {
                            goodLists.get(i).setCountTime(counttime - 1000);
                        } else {
                            Message message = new Message();
                            message.what = 2;//更新数据
                            //发送信息给handler
                            handler.sendMessage(message);
                            break;
                        }
                    }
                }
                Message message = new Message();
                message.what = 1;
                //发送信息给handler
                handler.sendMessage(message);

            } catch (Exception e) {
            }
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    adapter.notifyData();
                    break;
                case 2:
                    getDate();
                    break;
            }
            super.handleMessage(msg);
        }
    };


    public static final OybGoodsFragment newInstance(int type) {
        OybGoodsFragment fragment = new OybGoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
            thread = null;
        }
    }
}
