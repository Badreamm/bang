package cn.xcom.helper.fragment.mymessage;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.adapter.NoReleaseAdapter;
import cn.xcom.helper.bean.MyMessage;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ToastUtil;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class NoReleaseFragment extends Fragment {

    NoReleaseAdapter noReleaseAdapter;
    private List<MyMessage> addlist;
    private Context context;
    private XRecyclerView xRecyclerView;
    UserInfo user;
    View view;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_isrelease, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        user = new UserInfo();
        user.readData(context);
        getNewDatas();
        initView();
    }

    private void initView() {

        addlist = new ArrayList<>();
        xRecyclerView = (XRecyclerView) view.findViewById(R.id.fragment_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setLayoutManager(linearLayoutManager);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {

            @Override
            public void onRefresh() {
                getNewDatas();
            }

            @Override
            public void onLoadMore() {
                getMoreDatas();
            }
        });
        noReleaseAdapter = new NoReleaseAdapter(addlist, context);
        xRecyclerView.setAdapter(noReleaseAdapter);

    }

    private void getNewDatas() {
        String url = NetConstant.GET_MY_BB_SPOSTLIST;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        List<MyMessage> lists = gson.fromJson(data, new TypeToken<ArrayList<MyMessage>>() {
                        }.getType());
                        addlist.clear();
                        addlist.addAll(lists);
                        noReleaseAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                xRecyclerView.refreshComplete();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                ToastUtil.Toast(context, "网络错误，请检查");
                xRecyclerView.refreshComplete();

            }
        });
        request.putValue("beginid", "0");
        request.putValue("status", "-2");
        request.putValue("userid", user.getUserId());

        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }

    private void getMoreDatas() {
        String url = NetConstant.GET_MY_BB_SPOSTLIST;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        List<MyMessage> lists = gson.fromJson(data, new TypeToken<ArrayList<MyMessage>>() {
                        }.getType());

                        addlist.addAll(lists);
                        noReleaseAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                xRecyclerView.loadMoreComplete();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                ToastUtil.Toast(context, "网络错误，请检查");
                xRecyclerView.loadMoreComplete();

            }
        });
        MyMessage lastMsgV = addlist.get(addlist.size() - 1);
        request.putValue("beginid", lastMsgV.getMid());
        request.putValue("userid", user.getUserId());
        request.putValue("status", "-2");
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public void onPause() {
        super.onPause();

//        ToastUtil.showShort(getActivity(),"bbb");
    }

    @Override
    public void onResume() {
        getNewDatas();
        noReleaseAdapter.notifyDataSetChanged();
//        ToastUtil.showShort(getActivity(),"aaa");
        super.onResume();
    }
}
