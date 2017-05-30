package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xcom.helper.R;
import cn.xcom.helper.bean.TaskInfo;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.CommonAdapter;
import cn.xcom.helper.utils.LogUtils;
import cn.xcom.helper.utils.ToastUtil;
import cn.xcom.helper.utils.ViewHolder;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 16/6/4.
 * 我帮页
 */
public class IHelpActivity extends BaseActivity {
    @BindView(R.id.rl_i_help_back)
    RelativeLayout rlIHelpBack;
    @BindView(R.id.grab_task_list)
    ListView grabTaskList;
    @BindView(R.id.grab_task_srl)
    SwipeRefreshLayout grabTaskSrl;
    private String TAG = "IHelpActivity";
    private Context mContext;
    private List<TaskInfo> taskInfos;
    private CommonAdapter<TaskInfo> adapter;
    private UserInfo userInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_i_help);
        ButterKnife.bind(this);
        mContext = this;
        taskInfos = new ArrayList<>();
        userInfo = new UserInfo(mContext);
        initEvent();
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        grabTaskSrl.setColorSchemeResources(R.color.background_white);
        grabTaskSrl.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorTheme));
        grabTaskSrl.setProgressViewOffset(true, 10, 100);
        grabTaskSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        grabTaskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
    public void onResume() {
        super.onResume();
        userInfo.readData(mContext);
        getData();
    }

    /**
     * 加载数据
     */
    private void getData() {
            RequestParams params=new RequestParams();
            params.put("userid",userInfo.getUserId());
            HelperAsyncHttpClient.get(NetConstant.GETTASKLIST, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    grabTaskSrl.setRefreshing(false);
                    if (response.optString("status").equals("success")) {
                        setAdapter(response);
                    }
                    LogUtils.e(TAG, response.toString());

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    LogUtils.e(TAG, responseString);
                }
            });
    }

    /**
     * 根据请求数据
     * @param response
     */
    private void setAdapter(JSONObject response) {
        taskInfos.clear();
        taskInfos.addAll(getBeanFromJson(response));
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }else{
            adapter = new CommonAdapter<TaskInfo>(mContext,taskInfos,R.layout.item_task_info) {
                @Override
                public void convert(ViewHolder holder, TaskInfo taskInfo) {
                    setItem(holder,taskInfo);
                }
            };
            grabTaskList.setAdapter(adapter);
        }
    }

    /**
     * 为item填充内容
     * @param holder
     * @param taskInfo
     */
    private void setItem(ViewHolder holder, final TaskInfo taskInfo) {
        holder.setText(R.id.tv_name,taskInfo.getName())
                .setText(R.id.tv_task_name,taskInfo.getDescription())
                .setTimeText(R.id.tv_task_time,taskInfo.getTime())
                .setTimeTextWithStr(R.id.tv_expiry_time,taskInfo.getExpirydate(),"前有效")
                .setText(R.id.tv_type_name,taskInfo.getTypename())
                .setText(R.id.tv_address1,taskInfo.getAddress())
                .setText(R.id.tv_address2,taskInfo.getSaddress())
                .setText(R.id.tv_btn_grab,taskInfo.getState().equals("1")?"抢单":"已被抢")
                .setText(R.id.tv_price, taskInfo.getPrice());
        TextView btn_grab = holder.getView(R.id.tv_btn_grab);
        if(taskInfo.getState().equals("1")){
            btn_grab.setClickable(true);
            btn_grab.setText("抢单");
            btn_grab.setTextColor(getResources().getColor(R.color.colorTheme));
            btn_grab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateState(taskInfo.getId());
                }
            });
        }else{
            btn_grab.setClickable(false);
            btn_grab.setText("已被抢");
            btn_grab.setTextColor(getResources().getColor(R.color.holo_red_light));
        }
        //上门详细地址
        if(taskInfo.getLongitude().length()>0&&taskInfo.getLatitude().length()>0){
            holder.getView(R.id.ll_address).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, AddressDetailActivity.class);
                    intent.putExtra("lon",taskInfo.getLongitude());
                    intent.putExtra("lat",taskInfo.getLatitude());
                    startActivity(intent);
                }
            });
        }
        //服务详细地址
        if(taskInfo.getSlongitude().length()>0&&taskInfo.getSlatitude().length()>0){
            holder.getView(R.id.ll_saddress).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, AddressDetailActivity.class);
                    intent.putExtra("lon",taskInfo.getSlongitude());
                    intent.putExtra("lat",taskInfo.getSlatitude());
                    startActivity(intent);
                }
            });
        }
    }

    /**
     * 抢单
     * @param id
     */
    private void updateState(String id) {
        RequestParams params=new RequestParams();
        params.put("userid", userInfo.getUserId());
        params.put("taskid", id);
        HelperAsyncHttpClient.get(NetConstant.GRAB_TASK, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("status").equals("success")) {
                    ToastUtil.showShort(mContext, "抢单");
                    getData();
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
        return new Gson().fromJson(data,new TypeToken<List<TaskInfo>>(){}.getType());
    }

    @OnClick(R.id.rl_i_help_back)
    public void onClick() {
        finish();
    }
}
