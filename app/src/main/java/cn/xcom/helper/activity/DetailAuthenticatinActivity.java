package cn.xcom.helper.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import butterknife.OnClick;
import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.bean.AuthenticationList;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.CommonAdapter;
import cn.xcom.helper.utils.MyImageLoader;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ToastUtil;
import cn.xcom.helper.utils.ViewHolder;
import cn.xcom.helper.view.NoScrollListView;

public class DetailAuthenticatinActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_service_state)
    TextView tvServiceState;
    @BindView(R.id.tv_service_count)
    TextView tvServiceCount;
    @BindView(R.id.tv_paiming)
    TextView tvPaiming;
    @BindView(R.id.gridView_skill)
    GridView gridViewSkill;
    @BindView(R.id.sale_detail_comment)
    NoScrollListView saleDetailComment;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.immediate_Employment)
    TextView immediateEmployment;
    private Context context;
    private AuthenticationList authenticationList;
    UserInfo userInfo;
    private String lat;
    private String lon;
    String userId;
    boolean haveid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail_authenticatin);
        ButterKnife.bind(this);
        context = this;
        userInfo = new UserInfo(context);
        haveid = getIntent().getBooleanExtra("haveid",false);
        if (haveid) {
            HelperApplication.getInstance().convenience ="2";
            userId = getIntent().getStringExtra("userid");
            getSpecificAuthentication(userId);
            ToastUtil.showShort(context,userId);
        } else {
            ToastUtil.showShort(context,"111");
            getData();
        }


    }

    /**
     * 为布局设置值
     */
    private void setData() {
        MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + authenticationList.getPhoto(), ivAvatar);
        tvName.setText(authenticationList.getName());
        tvServiceState.setText(authenticationList.getIsworking().equals("1") ? "服务中" : "未服务");
        if (authenticationList.getServiceCount()==null){
            tvServiceCount.setText("0");
        }else {
            tvServiceCount.setText(authenticationList.getServiceCount());
        }


        if (authenticationList.getRanking() == null) {
            tvPaiming.setText("无排名");
        } else {
            tvPaiming.setText(authenticationList.getRanking() + "");
        }
        tvDistance.setText(authenticationList.getAddress());
        if (authenticationList.getEvaluatelist() != null) {
            saleDetailComment.setAdapter(new CommonAdapter<AuthenticationList.EvaluatelistBean>(context, authenticationList.getEvaluatelist(), R.layout.item_comment_info) {
                @Override
                public void convert(ViewHolder holder, AuthenticationList.EvaluatelistBean evaluatelistBean) {
                    holder.setImageByUrl(R.id.iv_avatar, evaluatelistBean.getPhoto())
                            .setText(R.id.tv_name, evaluatelistBean.getName())
                            .setTimeText(R.id.tv_time, evaluatelistBean.getAdd_time())
                            .setText(R.id.tv_content, evaluatelistBean.getContent());
                    RatingBar ratingBar = holder.getView(R.id.rating_bar);
                    ratingBar.setNumStars(Integer.valueOf(evaluatelistBean.getScore()));
                }
            });
        } else {
            return;
        }
    }


    /**
     * 接受intent传入的值
     */
    private void getData() {

//        userInfo.readData(context);
//        if ((AuthenticationList) getIntent().getSerializableExtra("authentication")!=null){
        authenticationList = (AuthenticationList) getIntent().getSerializableExtra("authentication");

        setData();
        getSkills();

//            new AlertDialog.Builder(this)
//                    .setTitle("提示")
//                    .setMessage("数据加载错误")
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            return;
//                        }
//                    }).show();


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
                        gridViewSkill.setSelector(new ColorDrawable(Color.TRANSPARENT));
                        gridViewSkill.setAdapter(new CommonAdapter<AuthenticationList.SkilllistBean>(context, skills, R.layout.item_skill_tag) {
                            @Override
                            public void convert(ViewHolder holder, AuthenticationList.SkilllistBean skilllistBean) {
                                holder.setText(R.id.tv_item_help_me_skill_tag, skilllistBean.getTypename());
                            }
                        });
                        gridViewSkill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (authenticationList.getId().equals(userInfo.getUserId())) {
                                    ToastUtil.showShort(context, "请不要雇佣自己");
                                } else {
                                    HelperApplication.getInstance().help = true;
                                    Intent intent = new Intent(context, HelpMeActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("authentication", authenticationList);
                                    intent.putExtras(bundle);
                                    intent.putExtra("postion", position + "");
                                    startActivity(intent);
                                }
                            }
                        });
                        String commentStr = jo.getString("commentlist");
                        List<AuthenticationList.EvaluatelistBean> comments = gson.fromJson(commentStr,
                                new TypeToken<ArrayList<AuthenticationList.EvaluatelistBean>>() {
                                }.getType());
                        saleDetailComment.setAdapter(new CommonAdapter<AuthenticationList.EvaluatelistBean>(context, comments, R.layout.item_comment_info) {
                            @Override
                            public void convert(ViewHolder holder, AuthenticationList.EvaluatelistBean evaluatelistBean) {
                                holder.setImageByUrl(R.id.iv_avatar, evaluatelistBean.getPhoto())
                                        .setText(R.id.tv_name, evaluatelistBean.getName())
                                        .setTimeText(R.id.tv_time, evaluatelistBean.getAdd_time())
                                        .setText(R.id.tv_content, evaluatelistBean.getContent());
                                RatingBar ratingBar = holder.getView(R.id.rating_bar);
                                ratingBar.setNumStars(Integer.valueOf(evaluatelistBean.getScore()));
                            }
                        });

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


    @OnClick({R.id.rl_back, R.id.immediate_Employment, R.id.iv_avatar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.immediate_Employment:
                if (authenticationList.getId().equals(userInfo.getUserId())) {
                    ToastUtil.showShort(context, "请不要雇佣自己");
                } else {
                    HelperApplication.getInstance().help = true;
                    Intent intent = new Intent(context, HelpMeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("authentication", authenticationList);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.iv_avatar:
                Intent intent = new Intent(context, AdvImgDetialActivity.class);
                intent.putExtra("path", NetConstant.NET_DISPLAY_IMG + authenticationList.getPhoto());
                intent.putExtra("url", "");
                startActivity(intent);
                break;
        }

    }

    /**
     * 根据用户编号获取具体认证帮服务者
     */
    private void getSpecificAuthentication(final String userId) {

        String url = NetConstant.GET_SKILLS_BY_USERID;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
//                        if (hud != null) {
//                            hud.dismiss();
//                        }
                        String data = "[" + jsonObject.getString("data") + "]";
                        Gson gson = new Gson();
                        List<AuthenticationList> lis = gson.fromJson(data, new TypeToken<List<AuthenticationList>>() {
                        }.getType());
                        authenticationList = (lis.get(0));
                        setData();
                        getSkills();
                    }

                } catch (JSONException e) {
//                    if (hud != null) {
//                        hud.dismiss();
//                    }
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                if (hud != null) {
//                    hud.dismiss();
//                }
            }
        });
        request.putValue("userid", userId);
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);


    }
}
