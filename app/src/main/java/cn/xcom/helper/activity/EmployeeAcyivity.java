package cn.xcom.helper.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RatingBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xcom.helper.R;
import cn.xcom.helper.bean.AuthenticationList;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.CommonAdapter;
import cn.xcom.helper.utils.NoScrollGridView;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ViewHolder;

/**
 * Created by Administrator on 2017/4/15 0015.
 */

public class EmployeeAcyivity extends BaseActivity {
    @BindView(R.id.em_gridView_skill)
    NoScrollGridView emGridViewSkill;
    Context context;
    private AuthenticationList authenticationList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        ButterKnife.bind(this);
        context = this;
        getData();
        getSkills();
    }
    private void getData() {
        authenticationList = (AuthenticationList) getIntent().getSerializableExtra("authentication");
    }

    private void getSkills() {
        Log.d("testid", authenticationList.getId());
        String url = NetConstant.GET_SKILLS_BY_USERID;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        String data = jsonObject.getString("data");
                        JSONObject jo = new JSONObject(data);
                        String skillStr = jo.getString("skilllist");
                        Gson gson = new Gson();
                        List<AuthenticationList.SkilllistBean> skills = gson.fromJson(skillStr,
                                new TypeToken<ArrayList<AuthenticationList.SkilllistBean>>() {
                                }.getType());
                        emGridViewSkill.setSelector(new ColorDrawable(Color.TRANSPARENT));
                        emGridViewSkill.setAdapter(new CommonAdapter<AuthenticationList.SkilllistBean>(context, skills, R.layout.item_skill_tag) {
                            @Override
                            public void convert(ViewHolder holder, AuthenticationList.SkilllistBean skilllistBean) {
                                holder.setText(R.id.tv_item_help_me_skill_tag, skilllistBean.getTypename());
                            }
                        });

                        String commentStr = jo.getString("commentlist");
                        List<AuthenticationList.EvaluatelistBean> comments = gson.fromJson(commentStr,
                                new TypeToken<ArrayList<AuthenticationList.EvaluatelistBean>>() {
                                }.getType());


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.putValue("userid", authenticationList.getId());
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);

    }
}
