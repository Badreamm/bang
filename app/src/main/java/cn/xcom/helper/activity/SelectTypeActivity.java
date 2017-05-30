package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xcom.helper.R;
import cn.xcom.helper.bean.TaskTypeAll;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.CommonAdapter;
import cn.xcom.helper.utils.LogUtils;
import cn.xcom.helper.utils.ViewHolder;
import cz.msebera.android.httpclient.Header;

public class SelectTypeActivity extends BaseActivity {
    @BindView(R.id.rl_help_me_back)
    RelativeLayout rlHelpMeBack;
    @BindView(R.id.lv_task_type)
    ListView lvTaskType;

    private Context context;
    private List<TaskTypeAll> taskTypeAlls;
    private CommonAdapter adapter;
    private List<String> checkedTypeListsFromIntent;
    private ArrayList<String> checkedTypeLists;
    private Map<String,String> checkedTypeMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select_type);
        ButterKnife.bind(this);
        context = this;
        findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData(true);
                finish();
            }
        });
        initData();
    }

    @OnClick(R.id.rl_help_me_back)
    public void onClick() {
        saveData(false);
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getdata();
    }

    private void initData(){
        taskTypeAlls = new ArrayList<>();
        checkedTypeListsFromIntent = getIntent().getStringArrayListExtra("checked");
        checkedTypeLists = new ArrayList<>();
        checkedTypeMap = new Hashtable<>();
    }

    /**
     * 获取数据
     */
    private void getdata() {
        RequestParams params=new RequestParams();
        params.put("id", "0");
        HelperAsyncHttpClient.get(NetConstant.NET_GET_TASKLIST, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e("result", response.toString());
                if (response != null) {
                    try {
                        String state = response.getString("status");
                        if (state.equals("success")) {
                            JSONArray data = response.getJSONArray("data");
                            taskTypeAlls.clear();
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);
                                TaskTypeAll taskTypeAll = new TaskTypeAll();
                                taskTypeAll.setId(object.optString("id"));
                                taskTypeAll.setName(object.optString("name"));
                                JSONArray array = object.optJSONArray("clist");
                                List<TaskTypeAll.ClistBean> list = new ArrayList<TaskTypeAll.ClistBean>();
                                for (int j = 0; j < array.length(); j++) {
                                    JSONObject itemObject = array.optJSONObject(j);
                                    TaskTypeAll.ClistBean clistBean = new TaskTypeAll.ClistBean();
                                    clistBean.setId(itemObject.optString("id"));
                                    clistBean.setName(itemObject.optString("name"));
                                    clistBean.setPhoto(itemObject.optString("photo"));
                                    list.add(clistBean);
                                }
                                taskTypeAll.setClist(list);
                                taskTypeAlls.add(taskTypeAll);
                            }
                            setAdapter();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.e("realut", "--statusCode->" + statusCode + "==>" + responseString);
            }
        });
    }

    /**
     * 设置适配器
     */
    private void setAdapter() {
        adapter = new CommonAdapter<TaskTypeAll>(context, taskTypeAlls, R.layout.item_task_type_info) {
            @Override
            public void convert(final ViewHolder holder, TaskTypeAll taskTypeAll) {
                holder.setText(R.id.tv_task, taskTypeAll.getName());
                GridView gridView = holder.getView(R.id.gv_type);
                gridView.setAdapter(new CommonAdapter<TaskTypeAll.ClistBean>(context, taskTypeAll.getClist(), R.layout.item_task_type) {
                    @Override
                    public void convert(final ViewHolder holder, final TaskTypeAll.ClistBean clistBean) {
                        holder.setText(R.id.tv_name, clistBean.getName());
                        CheckBox checkBox = holder.getView(R.id.cb_checked);
                        for(String s:checkedTypeListsFromIntent){
                            if (clistBean.getId().equals(s)){
                                checkedTypeMap.put(s,s);
                                checkBox.setChecked(true);
                            }
                        }
                        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                String type = clistBean.getId();
                                if (isChecked) {
                                    checkedTypeMap.put(type,type);
                                }else{
                                    checkedTypeMap.remove(type);
                                }
                            }
                        });
                    }
                });

            }
        };
        lvTaskType.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        saveData(false);
        super.onBackPressed();
    }

    /**
     * @param submit 标志位 true发布
     */
    private void saveData(boolean submit){
        for (Map.Entry<String, String> entry : checkedTypeMap.entrySet()) {
            checkedTypeLists.add(entry.getKey());
        }
        Intent data = new Intent();
        data.putStringArrayListExtra("checked", checkedTypeLists);
        data.putExtra("submit",submit);
        setResult(10001, data);
//        checkedTypeLists.clear();
        checkedTypeListsFromIntent.clear();
        checkedTypeMap.clear();
    }
}
