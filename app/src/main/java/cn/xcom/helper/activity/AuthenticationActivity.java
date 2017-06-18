package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.adapter.AuthenticationListAdapter;
import cn.xcom.helper.bean.AuthenticationList;
import cn.xcom.helper.bean.SkillTagInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.record.AudioPlayer;
import cn.xcom.helper.utils.CommonAdapter;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ToastUtil;
import cn.xcom.helper.utils.ViewHolder;
import cn.xcom.helper.view.DividerItemDecoration;
import cz.msebera.android.httpclient.Header;

/**
 * 认证帮
 */
public class AuthenticationActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_type1)
    TextView tvType1;
    @BindView(R.id.rl_type1)
    RelativeLayout rlType1;
    @BindView(R.id.tv_type2)
    TextView tvType2;
    @BindView(R.id.rl_type2)
    RelativeLayout rlType2;
    @BindView(R.id.tv_type3)
    TextView tvType3;
    @BindView(R.id.rl_type3)
    RelativeLayout rlType3;
    @BindView(R.id.recycler_view)
    XRecyclerView xRecyclerView;
    private Context context;
    private String type, sort, onlineType;
    private AuthenticationListAdapter adapter;
    private List<AuthenticationList> authenticationLists;
    private KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);
        context = this;
        authenticationLists = new ArrayList<>();
        type = "";
        sort = "1";
        onlineType = "0";//0全部 1在线
        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
        hud.show();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setLayoutManager(linearLayoutManager);
        xRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getData();
            }

            @Override
            public void onLoadMore() {
                getMore();
            }
        });
        getData();
    }


    /**
     * 加载数据
     */
    private void getData() {
        String url = NetConstant.GET_AUTHENTICATION_LIST;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (hud != null) {
                    hud.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        setAdapter(jsonObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                xRecyclerView.refreshComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (hud != null) {
                    hud.dismiss();
                }
                ToastUtil.Toast(context, "网络错误，请检查");
                xRecyclerView.refreshComplete();
            }
        });
        request.putValue("cityname", HelperApplication.getInstance().mDistrict);
        request.putValue("latitude", HelperApplication.getInstance().mCurrentLocLat + "");
        request.putValue("longitude", HelperApplication.getInstance().mCurrentLocLon + "");
        request.putValue("sort", sort);
        request.putValue("type", type);
        request.putValue("beginid", "0");
        request.putValue("online",onlineType);
        Log.e("认证帮", NetConstant.GET_AUTHENTICATION_LIST + sort + type);
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }


    private void getMore() {
        String url = NetConstant.GET_AUTHENTICATION_LIST;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (hud != null) {
                    hud.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
//                        if (onlineType.equals("2")) {
//                            authenticationLists.addAll(getBeanByMe(jsonObject));
//                        } else {
//                            authenticationLists.addAll(getBeanFromJson(jsonObject));
//                        }
                        authenticationLists.addAll(getBeanFromJson(jsonObject));
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                xRecyclerView.loadMoreComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (hud != null) {
                    hud.dismiss();
                }
                ToastUtil.Toast(context, "网络错误，请检查");
                xRecyclerView.loadMoreComplete();
            }
        });
        request.putValue("cityname", HelperApplication.getInstance().mDistrict);
        request.putValue("latitude", HelperApplication.getInstance().mCurrentLocLat + "");
        request.putValue("longitude", HelperApplication.getInstance().mCurrentLocLon + "");
        request.putValue("sort", sort);
        request.putValue("type", type);
        request.putValue("online",onlineType);
        AuthenticationList last = authenticationLists.get(authenticationLists.size() - 1);
        request.putValue("beginid", last.getId());
        Log.e("认证帮", NetConstant.GET_AUTHENTICATION_LIST + sort + type);
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }

    /**
     * 字符串转模型集合
     *
     * @param response
     * @return
     */
    private List<AuthenticationList> getBeanFromJson(JSONObject response) {
        String data = "";
        try {
            data = response.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<AuthenticationList>>() {
        }.getType());
    }

    private List<AuthenticationList> getBeanByMe(JSONObject response) {
        List<AuthenticationList> lists = new ArrayList<>();
        try {
            JSONArray array = response.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.optJSONObject(i);
                if (object.optString("isworking").equals("1")) {
                    AuthenticationList authenticationList = new AuthenticationList();
                    authenticationList.setId(object.optString("id"));
                    authenticationList.setName(object.optString("name"));
                    authenticationList.setPhoto(object.optString("photo"));
                    authenticationList.setPhone(object.optString("phone"));
                    authenticationList.setAddress(object.optString("address"));
                    authenticationList.setStatus(object.optString("status"));
                    authenticationList.setUsertype(object.optString("usertype"));
                    authenticationList.setIsworking(object.optString("isworking"));
                    authenticationList.setServiceCount(object.optString("serviceCount"));
                    authenticationList.setLongitude(object.optString("longitude"));
                    authenticationList.setLatitude(object.optString("latitude"));
                    authenticationList.setRanking(object.optString("Ranking"));
                    authenticationList.setDistance(Long.parseLong(object.optString("distance")));
                    authenticationList.setScore(object.optString("score"));
                    //获取技能
                    JSONArray skillArray = object.optJSONArray("skilllist");
                    List<AuthenticationList.SkilllistBean> skilllistBeans = new ArrayList<>();
                    for (int j = 0; j < skillArray.length(); j++) {
                        AuthenticationList.SkilllistBean skilllistBean = new AuthenticationList.SkilllistBean();
                        skilllistBean.setType(skillArray.optJSONObject(j).optString("type"));
                        skilllistBean.setTypename(skillArray.optJSONObject(j).optString("typename"));
                        skilllistBean.setParent_typeid(skillArray.optJSONObject(j).optString("parent_typeid"));
                        skilllistBeans.add(skilllistBean);
                    }
                    authenticationList.setSkilllist(skilllistBeans);
                    //获取评论
                    JSONArray commentArray = object.optJSONArray("evaluatelist");
                    List<AuthenticationList.EvaluatelistBean> evaluatelistBeans = new ArrayList<>();
                    for (int k = 0; k < commentArray.length(); k++) {
                        AuthenticationList.EvaluatelistBean evaluatelistBean = new AuthenticationList.EvaluatelistBean();
                        evaluatelistBean.setId(commentArray.optJSONObject(k).optString("id"));
                        evaluatelistBean.setScore(commentArray.optJSONObject(k).optString("score"));
                        evaluatelistBean.setName(commentArray.optJSONObject(k).optString("name"));
                        evaluatelistBean.setAdd_time(commentArray.optJSONObject(k).optString("add_time"));
                        evaluatelistBean.setContent(commentArray.optJSONObject(k).optString("content"));
                        evaluatelistBean.setPhoto(commentArray.optJSONObject(k).optString("photo"));
                        evaluatelistBean.setUserid(commentArray.optJSONObject(k).optString("userid"));
                        evaluatelistBeans.add(evaluatelistBean);
                    }
                    authenticationList.setEvaluatelist(evaluatelistBeans);
                    lists.add(authenticationList);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lists;
    }

    /**
     * 设置适配器
     *
     * @param jsonObject
     */
    private void setAdapter(JSONObject jsonObject) {
        authenticationLists.clear();
//        if (onlineType.equals("2")) {
//        authenticationLists.addAll(getBeanByMe(jsonObject));
//        } else {
//            authenticationLists.addAll(getBeanFromJson(jsonObject));
//        }
        authenticationLists.addAll(getBeanFromJson(jsonObject));
        /*if (onlineType.equals("2")) {
            for (int i = 0; i < authenticationLists.size(); i++) {
                AuthenticationList authenticationList = new AuthenticationList();
                if (authenticationLists.get(i).getIsworking().equals("1")){
                    authenticationList = authenticationLists.get(i);
                    onlineList.add(authenticationList);
                }
            }
            authenticationLists.clear();
            authenticationLists = onlineList;
        }*/

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            adapter = new AuthenticationListAdapter(context, authenticationLists);
            xRecyclerView.setAdapter(adapter);
        }
    }

    @OnClick({R.id.rl_back, R.id.rl_type1, R.id.rl_type2, R.id.rl_type3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_type1:
                showPopupMenu();
                break;
            case R.id.rl_type2:
                showPopupWindow1();
                break;
            case R.id.rl_type3:
                showPopupWindow2();
                break;
        }
    }

    /**
     * 服务类型弹出框
     */
    private void showPopupWindow1() {
        //自定义布局
        View layout = LayoutInflater.from(context).inflate(R.layout.select_type_mode1, null);
        //初始化popwindow
        final PopupWindow popupWindow = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置弹出位置
        int[] location = new int[2];
        rlType1.getLocationOnScreen(location);
        popupWindow.showAsDropDown(rlType1);
        final TextView tv1 = (TextView) layout.findViewById(R.id.tv1);
        final TextView tv2 = (TextView) layout.findViewById(R.id.tv2);
        final TextView tv3 = (TextView) layout.findViewById(R.id.tv3);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvType2.setText(tv1.getText());
                sort = "1";
                popupWindow.dismiss();
                getData();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvType2.setText(tv2.getText());
                sort = "2";
                popupWindow.dismiss();
                getData();
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvType2.setText(tv3.getText());
                sort = "3";
                popupWindow.dismiss();
                getData();
            }
        });
        // 设置背景颜色变暗
        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        //监听popwindow消失事件，取消遮盖层
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
    }

    /**
     * 是否在线弹出框
     */
    private void showPopupWindow2() {
        //自定义布局
        View layout = LayoutInflater.from(context).inflate(R.layout.select_type_mode2, null);
        //初始化popwindow
        final PopupWindow popupWindow = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置弹出位置
        int[] location = new int[2];
        rlType1.getLocationOnScreen(location);
        popupWindow.showAsDropDown(rlType1);
        final TextView tv1 = (TextView) layout.findViewById(R.id.tv1);
        final TextView tv2 = (TextView) layout.findViewById(R.id.tv2);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvType3.setText(tv1.getText());
                onlineType = "0";
                getData();
                popupWindow.dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvType3.setText(tv2.getText());
                onlineType = "1";
                getData();
                popupWindow.dismiss();
            }
        });
        // 设置背景颜色变暗
        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        //监听popwindow消失事件，取消遮盖层
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
    }

    /**
     * 技能分类弹出框
     */
    private void showPopupMenu() {
        final ArrayList<SkillTagInfo> skillTagInfos = new ArrayList<>();
        RequestParams params = new RequestParams();
        params.put("id", 0);
        HelperAsyncHttpClient.get(NetConstant.NET_GET_TASKLIST, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        String state = response.getString("status");
                        if (state.equals("success")) {
                            JSONArray data = response.getJSONArray("data");
                            skillTagInfos.clear();
                            SkillTagInfo tagInfo = new SkillTagInfo();
                            tagInfo.setSkill_name("全部");
                            tagInfo.setSkill_id("0");
                            skillTagInfos.add(tagInfo);
                            for (int i = 0; i < data.length(); i++) {
                                SkillTagInfo info = new SkillTagInfo();
                                JSONObject jsonObject = data.getJSONObject(i);
                                info.setSkill_id(jsonObject.getString("id"));
                                info.setSkill_name(jsonObject.getString("name"));
                                skillTagInfos.add(info);
                            }
                            //自定义布局
                            View layout = LayoutInflater.from(context).inflate(R.layout.select_type_list, null);
                            //初始化popwindow
                            final PopupWindow popupWindow = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                            popupWindow.setFocusable(true);
                            popupWindow.setOutsideTouchable(true);
                            popupWindow.setBackgroundDrawable(new BitmapDrawable());
                            //设置弹出位置
                            int[] location = new int[2];
                            rlType1.getLocationOnScreen(location);
                            popupWindow.showAsDropDown(rlType1);
                            ListView listView = (ListView) layout.findViewById(R.id.type_list);
                            listView.setAdapter(new CommonAdapter<SkillTagInfo>(context, skillTagInfos, R.layout.item_skill) {
                                @Override
                                public void convert(ViewHolder holder, SkillTagInfo skillTagInfo) {
                                    holder.setText(R.id.tv_type_name, skillTagInfo.getSkill_name());
                                }
                            });
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    tvType1.setText(skillTagInfos.get(position).getSkill_name());
                                    type = skillTagInfos.get(position).getSkill_id();
                                    getData();
                                    popupWindow.dismiss();
                                }
                            });
                            // 设置背景颜色变暗
                            final WindowManager.LayoutParams lp = getWindow().getAttributes();
                            lp.alpha = 0.7f;
                            getWindow().setAttributes(lp);
                            //监听popwindow消失事件，取消遮盖层
                            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                @Override
                                public void onDismiss() {
                                    lp.alpha = 1.0f;
                                    getWindow().setAttributes(lp);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

}
