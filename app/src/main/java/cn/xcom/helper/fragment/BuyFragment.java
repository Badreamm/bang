package cn.xcom.helper.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.kyleduo.switchbutton.SwitchButton;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.activity.AuthorizedActivity;
import cn.xcom.helper.activity.ChangeSkillsActivity;
import cn.xcom.helper.activity.MyTaskActivity;
import cn.xcom.helper.activity.TaskDetailActivity;
import cn.xcom.helper.bean.TaskInfo;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.HelperConstant;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.LogUtils;
import cn.xcom.helper.utils.MyImageLoader;
import cn.xcom.helper.utils.RoundImageView;
import cn.xcom.helper.utils.SPUtils;
import cn.xcom.helper.utils.ToastUtil;
import cn.xcom.helper.utils.ViewHolder;
import cn.xcom.helper.view.DividerItemDecoration;
import cn.xcom.helper.view.MenuPopupWindow;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 16/5/27.
 * 主页面——抢单
 */
public class BuyFragment extends Fragment implements View.OnClickListener {
    private String TAG = "BuyFragment";
    private Context mContext;
    private Button bt_authorized;
    private UserInfo userInfo;
    private SwitchButton sb;
    private RelativeLayout ll_task;
    private SwitchButton sb_change;
    private boolean isChecked = true;
    private List<TaskInfo> taskInfos;
    private BuyRecycleAdapter adapter;
    private MenuPopupWindow menuPopupWindow;
    // 定位相关
    LocationClient mLocClient;
    public BDLocationListener myListener = new MyLocationListener();
    private String district;
    private boolean isFirstIn = true;
    private KProgressHUD hud;
    private XRecyclerView xRecyclerView;

    int flag = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        Log.e("state", SPUtils.get(getActivity(), HelperConstant.IS_HAD_AUTHENTICATION, "").toString());
        /*if(SPUtils.get(getActivity(), HelperConstant.IS_HAD_AUTHENTICATION,"").equals("1")){
            view = inflater.inflate(R.layout.activity_grab_task,container,false);
        }else{
            view = inflater.inflate( R.layout.fragment_buy,container,false);
        }*/
        view = inflater.inflate(R.layout.activity_grab_task, container, false);
        return view;
    }

    /**
     * 开始定位
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        taskInfos = new ArrayList<>();
        mLocClient = new LocationClient(mContext);     //声明LocationClient类
        mLocClient.registerLocationListener(myListener);    //注册监听函数
        initLocation();
        initView();
    }

    private void initView() {
        userInfo = new UserInfo(mContext);
        /*if(SPUtils.get(getActivity(), HelperConstant.IS_HAD_AUTHENTICATION,"").equals("1")){
            ll_task = (RelativeLayout) getView().findViewById(R.id.ll_task);
            ll_task.setOnClickListener(this);
            srl_task = (SwipeRefreshLayout) getView().findViewById(R.id.grab_task_srl);
            lv_task = (ListView) getView().findViewById(R.id.grab_task_list);
            sb_change = (SwitchButton) getView().findViewById(R.id.sb_fragment_buy);
            srl_task.setColorSchemeResources(R.color.background_white);
            srl_task.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorTheme));
            srl_task.setProgressViewOffset(true, 10, 100);
            srl_task.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getData(HelperApplication.getInstance().mDistrict);
                }
            });
            lv_task.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext, TaskDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("taskInfo",taskInfos.get(position));
                    intent.putExtras(bundle);
                    //抢单列表
                    intent.putExtra("type","1");
                    mContext.startActivity(intent);
                }
            });
        }else{
            bt_authorized= (Button) getView().findViewById(R.id.bt_fragment_buy_authorized);
            bt_authorized.setOnClickListener(this);
            sb= (SwitchButton) getView().findViewById(R.id.sb_fragment_buy);
        }*/
        ll_task = (RelativeLayout) getView().findViewById(R.id.ll_task);
        ll_task.setOnClickListener(this);
        sb_change = (SwitchButton) getView().findViewById(R.id.sb_fragment_buy);
        xRecyclerView = (XRecyclerView) getView().findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setLayoutManager(linearLayoutManager);
        xRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()
                , DividerItemDecoration.VERTICAL_LIST));
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getData(HelperApplication.getInstance().mDistrict);
            }

            @Override
            public void onLoadMore() {
                getMore(HelperApplication.getInstance().mDistrict);
            }
        });

        adapter = new BuyRecycleAdapter(getContext(), taskInfos);
        xRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        userInfo.readData(mContext);
        if (!isFirstIn) {
            getData(HelperApplication.getInstance().mDistrict);
        }
        //getIdentity();
    }

    /**
     * 显示选择菜单
     */
    private void showPopupMenu() {
        menuPopupWindow = new MenuPopupWindow(mContext, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.add_qun:
                        startActivity(new Intent(mContext, MyTaskActivity.class));
                        menuPopupWindow.dismiss();
                        break;
                    case R.id.tong_bu:
                        startActivity(new Intent(mContext, ChangeSkillsActivity.class));
                        menuPopupWindow.dismiss();
                        break;
                }
            }
        });
        //设置弹出位置
        int[] location = new int[2];
        ll_task.getLocationOnScreen(location);
        menuPopupWindow.showAsDropDown(ll_task);

    }

    /**
     * 加载数据
     */
    private void getData(String district) {
        getWorkingState();
        RequestParams params = new RequestParams();
        params.put("city", district);
        Log.e("city", HelperApplication.getInstance().mDistrict);
        //params.put("city","芝罘区");
        params.put("userid", userInfo.getUserId());
        params.put("beginid", "0");

        HelperAsyncHttpClient.get(NetConstant.GETTASKLIST, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("status").equals("success")) {
                    taskInfos.clear();
                    taskInfos.addAll(getBeanFromJson(response));
                    adapter.notifyDataSetChanged();
                }
                LogUtils.e(TAG, response.toString());
                xRecyclerView.refreshComplete();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.e(TAG, responseString);
                xRecyclerView.refreshComplete();
            }
        });
    }

    private void getMore(String district) {
        Log.e("zcq","getmore");
        getWorkingState();
        RequestParams params = new RequestParams();
        //params.put("city","芝罘区");
        params.put("city", district);//district
        params.put("userid", userInfo.getUserId());
        TaskInfo taskInfo = taskInfos.get(taskInfos.size() - 1);
        params.put("beginid", taskInfo.getId());
        Log.e("city", district);//HelperApplication.getInstance().mDistrict
        Log.e("userid", userInfo.getUserId());
        Log.e("beginid", taskInfo.getId());
        HelperAsyncHttpClient.get(NetConstant.GETTASKLIST, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("status").equals("success")) {
                    taskInfos.addAll(getBeanFromJson(response));
                    adapter.notifyDataSetChanged();
                }
                xRecyclerView.loadMoreComplete();
                LogUtils.e(TAG, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                xRecyclerView.loadMoreComplete();
                LogUtils.e(TAG, responseString);
            }
        });
    }

    /**
     * 获取当前用户开工收工状态
     */
    private void getWorkingState() {
        RequestParams params = new RequestParams();
        params.put("userid", userInfo.getUserId());
        HelperAsyncHttpClient.get(NetConstant.GET_WORKING_STATE, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                String state = response.optString("data");
                if (sb_change == null) {
                    return;
                }
                if (state.equals("0")) {
                    sb_change.setChecked(false);
                } else if (state.equals("1")) {
                    sb_change.setChecked(true);
                }
                sb_change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!SPUtils.get(mContext, HelperConstant.IS_INSURANCE, "").equals("1")) {
                            popDialog(mContext, "提示", "您未通过认证，不能开工，是否跳转到认证页面进行验证？");
                        } else {
                            if (sb_change.isChecked()) {
                                changeWorkingState("1");
                            } else {
                                changeWorkingState("0");
                            }
                        }
                    }
                });

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.e(TAG, responseString);
            }
        });
    }

    /**
     * 切换工作状态
     */
    private void changeWorkingState(String state) {
        hud = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
        hud.show();
        RequestParams params = new RequestParams();
        params.put("userid", userInfo.getUserId());
        params.put("address", HelperApplication.getInstance().mLocAddress);
        params.put("longitude", HelperApplication.getInstance().mLocLon);
        params.put("latitude", HelperApplication.getInstance().mLocLat);
        params.put("isworking", state);
        HelperAsyncHttpClient.get(NetConstant.CHANGE_WORKING_STATE, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (hud != null) {
                    hud.dismiss();
                }
                super.onSuccess(statusCode, headers, response);
                Log.e(TAG, String.valueOf(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (hud != null) {
                    hud.dismiss();
                }
                LogUtils.e(TAG, responseString);
            }
        });
    }


    /**
     * 弹框
     *
     * @param context
     * @param title
     * @param message
     */
    private void popDialog(final Context context, String title, String message) {
        AlertView mAlertView = new AlertView(title, message, "取消", new String[]{"确定"}, null, context, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    startActivity(new Intent(mContext, AuthorizedActivity.class));
                }
            }
        }).setCancelable(true).setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {

            }
        });
        mAlertView.show();
    }

    /**
     * 启动百度地图驾车路线规划
     */
    public void startRoutePlanDriving(String taskInfoLatitude, String taskInfoLongitude, String taskInfoAddress, String latitude, String longitude, String address) {
        // 起点坐标
        double mLat1 = Double.parseDouble(taskInfoLatitude);
        double mLon1 = Double.parseDouble(taskInfoLongitude);
        // 终点
        double mLat2 = Double.parseDouble(latitude);
        double mLon2 = Double.parseDouble(longitude);
        LatLng pt1 = new LatLng(mLat1, mLon1);
        LatLng pt2 = new LatLng(mLat2, mLon2);
        // 构建 route搜索参数
        RouteParaOption para = new RouteParaOption()
                .startPoint(pt1)
                .startName(taskInfoAddress)
                .endPoint(pt2)
                .endName(address);
        try {
            BaiduMapRoutePlan.setSupportWebRoute(true);
            BaiduMapRoutePlan.openBaiduMapDrivingRoute(para, mContext);
        } catch (Exception e) {
            e.printStackTrace();
            showDialog();
        }

    }

    /**
     * 提示未安装百度地图app或app版本过低
     */
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(mContext);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }

    /**
     * 启动百度地图驾车路线规划
     *
     * @param taskInfoLatitude
     * @param taskInfoLongitude
     * @param taskInfoAddress
     * @param latitude
     * @param longitude
     * @param address
     */
    public void startRoutePlan(String taskInfoLatitude, String taskInfoLongitude, String taskInfoAddress, String latitude, String longitude, String address) {
        // 起点坐标
        double mLat1 = Double.parseDouble(taskInfoLatitude);
        double mLon1 = Double.parseDouble(taskInfoLongitude);
        // 终点
        double mLat2 = Double.parseDouble(latitude);
        double mLon2 = Double.parseDouble(longitude);
        LatLng pt1 = new LatLng(mLat1, mLon1);
        LatLng pt2 = new LatLng(mLat2, mLon2);
        BaiduMapNavigation.setSupportWebNavi(true);
        // 构建 导航参数
        NaviParaOption para = new NaviParaOption()
                .startPoint(pt1).endPoint(pt2)
                .startName(taskInfoAddress).endName(address);
        try {
            // 调起百度地图骑行导航
            BaiduMapNavigation.openBaiduMapBikeNavi(para, mContext);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            //showDialog();
        }

    }

    /**
     * 抢单
     *
     * @param id
     */
    private void updateState(String id) {
        RequestParams params = new RequestParams();
        params.put("userid", userInfo.getUserId());
        params.put("taskid", id);
        HelperAsyncHttpClient.get(NetConstant.GRAB_TASK, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("status").equals("success")) {
                    ToastUtil.showShort(mContext, "抢单");
                    getData(HelperApplication.getInstance().mDistrict);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    /**
     * 字符串转模型集合
     *
     * @param response
     * @return
     */
    private List<TaskInfo> getBeanFromJson(JSONObject response) {
        String data = "";
        try {
            data = response.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<TaskInfo>>() {
        }.getType());
    }

    private void getIdentity() {
        RequestParams params = new RequestParams();
        params.put("userid", userInfo.getUserId());
        HelperAsyncHttpClient.get(NetConstant.NET_GET_IDENTITY, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG, "--statusCode->" + statusCode + "==>" + response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.e(TAG, "--statusCode->" + statusCode + "==>" + responseString);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.bt_fragment_buy_authorized:
                startActivity(new Intent(mContext, AuthorizedActivity.class));
                break;*/
            case R.id.ll_task:
                showPopupMenu();
        }
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location != null) {
                district = location.getDistrict();
                mLocClient.stop();
                if (isFirstIn) {
                    isFirstIn = false;
                    getData(district);
                }
            }
        }
    }


    /**
     * 适配器
     */
    public class BuyRecycleAdapter extends RecyclerView.Adapter<BuyRecycleAdapter.ViewHolder> {
        private Context context;
        private List<TaskInfo> taskInfos;

        public BuyRecycleAdapter(Context context, List<TaskInfo> taskInfos) {
            this.context = context;
            this.taskInfos = taskInfos;
        }


        @Override
        public BuyRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_task_info, parent, false);
            return new BuyRecycleAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(BuyRecycleAdapter.ViewHolder holder, final int position) {
            final TaskInfo taskInfo = taskInfos.get(position);
            holder.tvName.setText(taskInfo.getName());
            holder.tvTaskName.setText(taskInfo.getTitle());
            Date date = new Date();
            date.setTime(Long.parseLong(taskInfo.getTime()) * 1000);
            holder.tvTaskTime.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm").format(date));
            holder.tvExpiryTime.setText(setTimeTextWithStr(taskInfo.getExpirydate(), "前有效"));
            holder.tvTypeName.setText(taskInfo.getDescription());
            holder.tvAddress1.setText(taskInfo.getAddress());
            holder.tvAddress2.setText(taskInfo.getSaddress());
//            holder.tvBtnGrab.setText((taskInfo.getState().equals("1")&&(taskInfo.getIshire().equals("1"))) ? "抢单" : "已被抢");
            Log.d("===ishire",taskInfo.getIshire());
            Log.d("===istate",taskInfo.getState());
            if ("".equals(taskInfo.getPhoto())) {
                MyImageLoader.display(NetConstant.NET_DISPLAY_IMG, holder.ivAvatar);
            } else {
                MyImageLoader.display(NetConstant.NET_DISPLAY_IMG +
                        taskInfo.getPhoto(), holder.ivAvatar);
            }
            holder.tvPrice.setText(taskInfo.getPrice());

            if (!taskInfo.getLongitude().equals("") && !taskInfo.getLatitude().equals("") && !taskInfo.getSlatitude().equals("") && !taskInfo.getSlongitude().equals("")) {
                holder.tvDistance.setText((int) DistanceUtil.getDistance(
                        new LatLng(Double.parseDouble(taskInfo.getLatitude()),
                                Double.parseDouble(taskInfo.getLongitude())),
                        new LatLng(Double.parseDouble(taskInfo.getSlatitude()),
                                Double.parseDouble(taskInfo.getSlongitude()))) + "米");
            }
            if (taskInfo.getState().equals("1")&&(!taskInfo.getIshire().equals("1"))) {
                if (Long.parseLong(taskInfo.getExpirydate()) * 1000 < new Date().getTime()) {
                    holder.tvBtnGrab.setClickable(false);
                    holder.tvBtnGrab.setText("过期");
                    holder.tvBtnGrab.setTextColor(context.getResources().getColor(R.color.holo_red_light));
                } else {
                    holder.tvBtnGrab.setClickable(true);
                    holder.tvBtnGrab.setText("抢单");
                    holder.tvBtnGrab.setTextColor(context.getResources().getColor(R.color.colorTheme));
                    holder.tvBtnGrab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!SPUtils.get(context, HelperConstant.IS_HAD_AUTHENTICATION, "").equals("1")) {
                                //popDialog(mContext,"提示","您未通过认证，不能抢单，是否跳转到保险认证页面进行验证？");
                                context.startActivity(new Intent(context, AuthorizedActivity.class));
                            } else {
                                if (taskInfo.getUserid().equals(userInfo.getUserId())) {
                                    ToastUtil.showShort(mContext, "不能抢自己发布的任务");
                                } else {
                                    updateState(taskInfo.getId());
                                }
                            }
                        }
                    });
                }
            } else {
                holder.tvBtnGrab.setClickable(false);
                holder.tvBtnGrab.setText("已被抢");
                holder.tvBtnGrab.setTextColor(getResources().getColor(R.color.holo_red_light));
            }

            if (taskInfo.getLongitude().length() > 0 && taskInfo.getLatitude().length() > 0
                    && taskInfo.getSlongitude().length() > 0 && taskInfo.getSlatitude().length() > 0) {
                holder.llMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startRoutePlanDriving(taskInfo.getLatitude(), taskInfo.getLongitude(), taskInfo.getAddress()
                                , taskInfo.getSlatitude(), taskInfo.getSlongitude(), taskInfo.getSaddress());
                    }
                });
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, TaskDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("taskInfo", taskInfos.get(position));
                    intent.putExtras(bundle);
                    //抢单列表
                    intent.putExtra("type", "1");
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return taskInfos.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.iv_avatar)
            RoundImageView ivAvatar;
            @BindView(R.id.tv_task_time)
            TextView tvTaskTime;
            @BindView(R.id.tv_name)
            TextView tvName;
            @BindView(R.id.tv_type_name)
            TextView tvTypeName;
            @BindView(R.id.ll_ll)
            LinearLayout llLl;
            @BindView(R.id.tv_expiry_time)
            TextView tvExpiryTime;
            @BindView(R.id.tv_task_name)
            TextView tvTaskName;
            @BindView(R.id.tv_address1)
            TextView tvAddress1;
            @BindView(R.id.ll_address)
            LinearLayout llAddress;
            @BindView(R.id.tv_address2)
            TextView tvAddress2;
            @BindView(R.id.ll_saddress)
            LinearLayout llSaddress;
            @BindView(R.id.ll_map)
            LinearLayout llMap;
            @BindView(R.id.tv_price)
            TextView tvPrice;
            @BindView(R.id.tv_left)
            TextView tvLeft;
            @BindView(R.id.tv_distance)
            TextView tvDistance;
            @BindView(R.id.tv_btn_grab)
            TextView tvBtnGrab;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }

        public String setTimeTextWithStr(String text, String str) {
            Date date = new Date();
            date.setTime(Long.parseLong(text) * 1000);
            return new SimpleDateFormat("MM-dd  HH:mm").format(date) + str;
        }
    }
}
