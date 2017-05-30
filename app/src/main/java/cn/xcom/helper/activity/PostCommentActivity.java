package cn.xcom.helper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/10/19 0019.
 * 我的发单 我的订单
 * 发表评论页面
 */

public class PostCommentActivity extends BaseActivity implements View.OnClickListener {
    private static final int COMMENT_RESULT_CODE = 112;
    private RatingBar ratingBar;
    private EditText commentEd;
    private RelativeLayout backBtn, publishBtn;
    private String type, receiveType, orderId;
    private UserInfo userInfo;
    private KProgressHUD hud;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comment);
        userInfo = new UserInfo(this);
        Intent intent = getIntent();
        orderId = intent.getStringExtra("order_id");
        type = intent.getStringExtra("type");
        initView();
    }

    private void initView() {
        ratingBar = (RatingBar) findViewById(R.id.rating_bar);
        commentEd = (EditText) findViewById(R.id.ed_comment);
        backBtn = (RelativeLayout) findViewById(R.id.rl_help_me_back);
        backBtn.setOnClickListener(this);
        publishBtn = (RelativeLayout) findViewById(R.id.rl_publish_comment);
        publishBtn.setOnClickListener(this);

    }

    /**
     * userid,orderid(订单编号),type(任务是1,商城是2),
     * receivetype(1=》给发布者／商家评价，2=》给抢单者／购买者评价)
     * content,score
     */
    private void publish() {
        hud = KProgressHUD.create(PostCommentActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
        hud.show();
        String comment = commentEd.getText().toString();
        if (comment.equals("")) {
            Toast.makeText(this, "请输入评价", Toast.LENGTH_SHORT).show();
            return;
        }
        int score = ratingBar.getNumStars();
        if (score < 1) {
            Toast.makeText(this, "最低评分为1分", Toast.LENGTH_SHORT).show();
            return;
        }

        if (type.equals("1")) {
            receiveType = "2";
        } else if (type.equals("2")) {
            receiveType = "1";
        }
        RequestParams params = new RequestParams();
        params.put("userid", userInfo.getUserId());
        params.put("orderid", orderId);
        params.put("type", type);
        params.put("receivetype", receiveType);
        params.put("content", comment);
        params.put("score", score);

        HelperAsyncHttpClient.get(NetConstant.POST_COMMENT, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        String state = response.getString("status");
                        if (state.equals("success")) {
                            Toast.makeText(PostCommentActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                            if(hud!=null){
                                hud.dismiss();
                            }
                            setResult(COMMENT_RESULT_CODE);
                            finish();
                        }else{
                            if(hud!=null){
                                hud.dismiss();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    if(hud!=null){
                        hud.dismiss();
                    }
                }
            }

        });
    }


        @Override
        public void onClick (View v){
            switch (v.getId()) {
                case R.id.rl_help_me_back:
                    finish();
                    break;
                case R.id.rl_publish_comment:
                    publish();
                    break;

            }
        }
    }
