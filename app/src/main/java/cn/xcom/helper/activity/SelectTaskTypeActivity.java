package cn.xcom.helper.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.bean.TaskType;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.LogUtils;
import cz.msebera.android.httpclient.Header;

public class SelectTaskTypeActivity extends BaseActivity implements View.OnClickListener {

    private Context context;
    private RelativeLayout rl_back;
    private String id;
    private TextView tv_submit;
    private GridView gv_task;
    private List<TaskType> allTaskTypes;
    private TaskAdapter adapter;
    private List<TaskType> selectTaskType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select_task_type);
        context = this;
        selectTaskType = HelperApplication.getInstance().getTaskTypes();
        id = getIntent().getStringExtra("id");
        allTaskTypes = new ArrayList<>();
        getData();
        initView();
    }
    //获取数据
    private void getData() {
        RequestParams params=new RequestParams();
        params.put("id", id);
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
                            allTaskTypes.clear();
                            for (int i = 0; i < data.length(); i++) {
                                TaskType taskType = new TaskType();
                                JSONObject jsonObject = data.getJSONObject(i);
                                taskType.setId(jsonObject.getString("id"));
                                taskType.setName(jsonObject.getString("name"));
                                taskType.setParent(jsonObject.getString("parent"));
                                allTaskTypes.add(taskType);
                            }
                            for (int i = 0; i < allTaskTypes.size(); i++) {
                                allTaskTypes.get(i).setChecked(check(allTaskTypes.get(i).getId()));
                            }
                            adapter = new TaskAdapter(context, allTaskTypes);
                            gv_task.setAdapter(adapter);
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

    private boolean check(String id) {
        boolean checked = false;
        for(int i=0;i<selectTaskType.size();i++){
            if(id.equals(selectTaskType.get(i).getId())){
                return true;
            }
        }
        return false;
    }

    private void initView() {
        rl_back = (RelativeLayout) findViewById(R.id.rl_help_me_back);
        rl_back.setOnClickListener(this);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(this);
        gv_task = (GridView) findViewById(R.id.gv_type);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_help_me_back:
                finish();
                break;
            case R.id.tv_submit:
                //提交,保存当前页面的选择状态到全局变量中
                /*List<TaskType> list = new ArrayList<>();
                for(int i = 0;i<selectTaskType.size();i++){
                    if(!selectTaskType.get(i).getParent().equals(id)){
                        list.add(selectTaskType.get(i));
                    }
                }
                for(int i= 0;i<adapter.getTaskTypes().size();i++){
                    if(adapter.getTaskTypes().get(i).isChecked()){
                        list.add(adapter.getTaskTypes().get(i));
                    }
                }
                HelperApplication.getInstance().setTaskTypes(list);*/
                finish();
                break;
        }
    }

    public class TaskAdapter extends BaseAdapter{
        private Context context;
        private List<TaskType> taskTypes;
        private LayoutInflater inflater;

        public TaskAdapter(Context context, List<TaskType> taskTypes) {
            this.context = context;
            this.taskTypes = taskTypes;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return taskTypes.size();
        }

        @Override
        public Object getItem(int position) {
            return taskTypes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if(convertView == null){
                convertView = inflater.inflate(R.layout.item_task_type,null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_name.setText(taskTypes.get(position).getName());
            holder.cb_type.setChecked(taskTypes.get(position).isChecked());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.cb_type.isChecked()) {
                        holder.cb_type.setChecked(false);
                        HelperApplication.getInstance().getTaskTypes().clear();
                    } else {
                        holder.cb_type.setChecked(true);
                        clearOther(taskTypes.get(position).getId());
                        List<TaskType> list = new ArrayList<TaskType>();
                        list.add(taskTypes.get(position));
                        HelperApplication.getInstance().setTaskTypes(list);
                        notifyDataSetChanged();
                    }
                    taskTypes.get(position).setChecked(holder.cb_type.isChecked());
                }
            });
            holder.cb_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskTypes.get(position).setChecked(holder.cb_type.isChecked());
                    if(holder.cb_type.isChecked()){
                        clearOther(taskTypes.get(position).getId());
                        List<TaskType> list = new ArrayList<TaskType>();
                        list.add(taskTypes.get(position));
                        HelperApplication.getInstance().setTaskTypes(list);
                        notifyDataSetChanged();
                    }else{
                        HelperApplication.getInstance().getTaskTypes().clear();
                    }
                }
            });
            return convertView;
        }

        private void clearOther(String id) {
            for(int i=0;i<taskTypes.size();i++){
                if(!taskTypes.get(i).getId().equals(id)){
                    taskTypes.get(i).setChecked(false);
                }
            }
        }

        public class ViewHolder{
            TextView tv_name;
            CheckBox cb_type;
            public ViewHolder(View view){
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                cb_type = (CheckBox) view.findViewById(R.id.cb_checked);
            }
        }

        public List<TaskType> getTaskTypes() {
            return taskTypes;
        }
    }
}
