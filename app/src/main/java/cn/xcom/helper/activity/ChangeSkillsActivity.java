package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import cn.xcom.helper.bean.SkillTagInfo;
import cn.xcom.helper.bean.TaskType;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.view.NoScrollGridView;
import cz.msebera.android.httpclient.Header;

public class ChangeSkillsActivity extends BaseActivity {
    @BindView(R.id.rl_authorized_back)
    RelativeLayout rlAuthorizedBack;
    @BindView(R.id.gridView_help_me)
    NoScrollGridView gridViewHelpMe;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    private ArrayList<SkillTagInfo> skillTagInfos;
    private HelpMeSkillAdapter mHelpMeSkillAdapter;
    private List<TaskType> selectList;
    private UserInfo userInfo;
    private KProgressHUD hud;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_skills);
        ButterKnife.bind(this);
        context = this;
        userInfo = new UserInfo(context);
        userInfo.readData(context);
        skillTagInfos = new ArrayList<SkillTagInfo>();
        selectList = new ArrayList<>();
        initData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        refreshAdapter();
    }

    /**
     *
     */
    private void initData() {
        gridViewHelpMe = (NoScrollGridView) findViewById(R.id.gridView_help_me);
        gridViewHelpMe.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mHelpMeSkillAdapter=new HelpMeSkillAdapter(context,skillTagInfos);
        gridViewHelpMe.setAdapter(mHelpMeSkillAdapter);
        gridViewHelpMe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ChangeSkillsActivity.this, SelectTaskTypeOldActivity.class);
                intent.putExtra("id", skillTagInfos.get(position).getSkill_id());
                startActivity(intent);
            }
        });

        getSkill();
    }

    /**
     * 刷新页面技能选中状态
     */
    private void refreshAdapter(){
        selectList = HelperApplication.getInstance().getTaskTypes();
        Log.e("count", String.valueOf(selectList.size()));
        for(int i=0;i<skillTagInfos.size();i++){
            skillTagInfos.get(i).setChecked(check(skillTagInfos.get(i).getSkill_id()));
        }
        mHelpMeSkillAdapter.notifyDataSetChanged();
    }

    private void getSkill(){
        RequestParams params=new RequestParams();
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
                            for (int i = 0; i < data.length(); i++) {
                                SkillTagInfo info = new SkillTagInfo();
                                JSONObject jsonObject = data.getJSONObject(i);
                                info.setSkill_id(jsonObject.getString("id"));
                                info.setSkill_name(jsonObject.getString("name"));
                                skillTagInfos.add(info);
                            }
                            mHelpMeSkillAdapter.notifyDataSetChanged();
                        }
                        getMySkills();
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

    /**
     * 获取我的技能
     */
    private void getMySkills() {
        RequestParams params=new RequestParams();
        params.put("userid", userInfo.getUserId());
        HelperAsyncHttpClient.get(NetConstant.GET_MY_SKILLS, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        String state = response.getString("status");
                        if (state.equals("success")) {
                            JSONArray data = response.optJSONObject("data").optJSONArray("skilllist");
                            selectList.clear();
                            List<TaskType> taskTypes = new ArrayList<TaskType>();
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.optJSONObject(i);
                                TaskType taskType = new TaskType();
                                taskType.setParent(object.optString("parent_typeid"));
                                taskType.setId(object.optString("type"));
                                taskType.setName(object.optString("typename"));
                                taskTypes.add(taskType);
                            }
                            HelperApplication.getInstance().setTaskTypes(taskTypes);
                        }
                        refreshAdapter();
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

    @OnClick({R.id.rl_authorized_back, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_authorized_back:
                finish();
                break;
            case R.id.btn_submit:
                if(selectList.size()>0){
                    changeSkills();
                }
                break;
        }
    }

    /**
     * 提交新技能
     */
    private void changeSkills() {
        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
        hud.show();
        String url=NetConstant.CHANGE_MY_SKILLS;
        StringPostRequest request=new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(hud!=null){
                    hud.dismiss();
                }
                if (s!=null){
                    try {
                        JSONObject object = new JSONObject(s);
                        String state=object.getString("status");
                        if (state.equals("success")){
                            String data=object.getString("data");
                            HelperApplication.getInstance().getTaskTypes().clear();
                            Log.d("发布任务", data);
                            Toast.makeText(getApplication(), "修改成功", Toast.LENGTH_SHORT).show();
                        }else{
                            HelperApplication.getInstance().getTaskTypes().clear();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(hud!=null){
                    hud.dismiss();
                }
                HelperApplication.getInstance().getTaskTypes().clear();
                Toast.makeText(getApplication(),"网络错误，检查您的网络",Toast.LENGTH_SHORT).show();
            }
        });
        request.putValue("userid",userInfo.getUserId());
        request.putValue("type",getSelectString());
        SingleVolleyRequest.getInstance(getApplication()).addToRequestQueue(request);
    }


    /**
     * 根据选择分类得到分类拼接字符串
     * @return
     */
    private String getSelectString() {
        String str = "";
        for(int i=0;i<selectList.size()-1;i++){
            str += selectList.get(i).getId() + "," ;
        }
        str += selectList.get(selectList.size()-1).getId();
        Log.d("---str",str);
        return str;

    }

    /**
     * 判断大分类是否选中
     * @param id
     * @return
     */
    private boolean check(String id){
        for(int i=0;i<selectList.size();i++){
            if(selectList.get(i).getParent().equals(id)){
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HelperApplication.getInstance().getTaskTypes().clear();
    }

    /**
     * 任务分类适配器
     */
    public class HelpMeSkillAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<SkillTagInfo> mSkillTagInfos;

        public HelpMeSkillAdapter(Context context, ArrayList<SkillTagInfo> skillTagInfos) {
            this.mContext = context;
            if (skillTagInfos == null)
                skillTagInfos = new ArrayList<SkillTagInfo>();
            this.mSkillTagInfos = skillTagInfos;
        }

        @Override
        public int getCount() {
            return mSkillTagInfos.size();
        }

        @Override
        public SkillTagInfo getItem(int position) {
            return mSkillTagInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_help_me_skill_tag, null);
                viewHolder.tv_tag = (TextView) convertView.findViewById(R.id.tv_item_help_me_skill_tags);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_tag.setText(mSkillTagInfos.get(position).getSkill_name());
            if (mSkillTagInfos.get(position).isChecked()) {
                viewHolder.tv_tag.setTextColor(mContext.getResources().getColor(R.color.colorTextWhite));
                viewHolder.tv_tag.setBackgroundResource(R.drawable.tv_tag_select);
            } else {
                viewHolder.tv_tag.setTextColor(mContext.getResources().getColor(R.color.colorTheme));
                viewHolder.tv_tag.setBackgroundResource(R.drawable.tv_tag);
            }
            return convertView;
        }

        private class ViewHolder {
            TextView tv_tag;
        }
    }
}
