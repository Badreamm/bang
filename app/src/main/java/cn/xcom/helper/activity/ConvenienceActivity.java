package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.adapter.ConvenienceAdapter;
import cn.xcom.helper.bean.Convenience;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.record.AudioPlayer;
import cn.xcom.helper.utils.DateUtil;
import cn.xcom.helper.utils.PermissionUtil;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ToastUtil;

public class ConvenienceActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout back;
    private TextView cnnvenience_release, cnnvenience_msg, message_count;
    private EditText et_research;
    private Button convenience_deliver;
    private Button convenienc_traffic;
    private List<Convenience> addlist;
    private ConvenienceAdapter convenienceAdapter;
    private Context context;
    private XRecyclerView xRecyclerView;
    private KProgressHUD hud;
    private String city;
    String msgCount;
    UserInfo user;
    String keyWord = "";
    private CheckBox stateCb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convenience);
        city = getIntent().getStringExtra("city");
        if(TextUtils.isEmpty(city)){
            city = HelperApplication.getInstance().mDistrict;
        }
        initView();
        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
        hud.show();
        getNewDatas("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (HelperApplication.getInstance().trendsBack) {
            getNewDatas("");
        }
        getMessage();

    }

    @Override
    protected void onPause() {
        super.onPause();
        HelperApplication.getInstance().trendsBack = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HelperApplication.getInstance().trendsBack = false;
        if (AudioPlayer.isPlaying) {
            AudioPlayer.getInstance().stopPlay();
        }
    }

    private void initView() {
        context = this;
        user = new UserInfo();
        user.readData(context);
        addlist = new ArrayList<>();
        addlist.clear();
        back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(this);
        cnnvenience_release = (TextView) findViewById(R.id.cnnvenience_release);
        cnnvenience_release.setOnClickListener(this);
        convenience_deliver = (Button) findViewById(R.id.convenience_deliver);
        convenience_deliver.setOnClickListener(this);
        convenienc_traffic = (Button) findViewById(R.id.convenience_traffic);
        convenienc_traffic.setOnClickListener(this);
        cnnvenience_msg = (TextView) findViewById(R.id.cnnvenience_msg);
        cnnvenience_msg.setOnClickListener(this);
        message_count = (TextView) findViewById(R.id.cnnvenience_msg_count);
        et_research = (EditText) findViewById(R.id.et_search);
        et_research.addTextChangedListener(textWatcher);
        et_research.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                getNewDatas(et_research.getText().toString());
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow
                        (ConvenienceActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return true;
            }
        });
        xRecyclerView = (XRecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setLayoutManager(linearLayoutManager);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {

            @Override
            public void onRefresh() {
                getNewDatas(keyWord);
            }

            @Override
            public void onLoadMore() {

                getMoreDatas(keyWord);
            }
        });
        convenienceAdapter = new ConvenienceAdapter(addlist, context);
        xRecyclerView.setAdapter(convenienceAdapter);
        getMessage();
        stateCb = (CheckBox) findViewById(R.id.cb_state);
        stateCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePacketReceiveState();
            }
        });
        getPacketReceiveState();
    }

    /*
    * 获得未读消息数量
    * */
    private void getMessage() {
        String url = NetConstant.XC_GET_CHAT_NOREAD_COUNT;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        String data = jsonObject.getString("data");
                        msgCount = data;
                        if (Integer.parseInt(msgCount) > 0) {
                            message_count.setVisibility(View.VISIBLE);
                            message_count.setText(msgCount);
                        } else {

                        }

                    } else {
                        message_count.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.Toast(context, "网络错误，请检查");
            }
        });
        request.putValue("uid", user.getUserId());
        Log.d("---userid", user.getUserId());
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            keyWord = et_research.getText().toString();
            getNewDatas(et_research.getText().toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            //发布便民消息
            case R.id.cnnvenience_release:
                /*if (SPUtils.get(context, HelperConstant.IS_HAD_AUTHENTICATION, "").equals("1")) {
                    goPublish();
                } else {
                    goAuthorized();
                }*/
                if (!"".equals(HelperApplication.getInstance().mDistrict) &&
                        PermissionUtil.gPSIsOPen(this)) {
                    goPublish();
                } else {
                    ToastUtil.showShort(this, "请开启定位");
                }
                break;
            case R.id.convenience_deliver:
                startActivity(new Intent(ConvenienceActivity.this, DeliverActivity.class));
                break;
            case R.id.convenience_traffic:
                startActivity(new Intent(ConvenienceActivity.this, TrafficActivity.class));
                break;
            case R.id.cnnvenience_msg:
                startActivity(new Intent(ConvenienceActivity.this, UserMessageActivity.class));
                break;
        }
    }

    private void goPublish() {
        startActivity(new Intent(ConvenienceActivity.this, ReleaseConvenienceActivity.class));
    }

    private void goAuthorized() {
        Intent intent = new Intent(ConvenienceActivity.this, BindAccountAuthorizedActivity.class);
        startActivity(intent);
    }

    private void getNewDatas(String keyWord) {
        String url = NetConstant.CONVENIENCE;
        Log.d("---url", url);
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
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        List<Convenience> lists = gson.fromJson(data, new TypeToken<ArrayList<Convenience>>() {
                        }.getType());
                        addlist.clear();
                        addlist.addAll(lists);
                        convenienceAdapter.notifyDataSetChanged();

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
        request.putValue("userid", user.getUserId());
        request.putValue("beginid", "0");
        request.putValue("type", "1");
        request.putValue("keyword", keyWord);
        request.putValue("city", city);
        Log.e("获取便民圈", HelperApplication.getInstance().mDistrict);
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }

    private void getMoreDatas(String keyWord) {
        String url = NetConstant.CONVENIENCE;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {

                        String data = jsonObject.getString("data");
                        Log.e("获取便民圈", data);
                        Gson gson = new Gson();
                        List<Convenience> lists = gson.fromJson(data, new TypeToken<ArrayList<Convenience>>() {
                        }.getType());
                        addlist.addAll(lists);
                        convenienceAdapter.notifyDataSetChanged();

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
        Convenience lastConV = addlist.get(addlist.size() - 1);
        request.putValue("userid", user.getUserId());
        request.putValue("beginid", lastConV.getMid());
        request.putValue("type", "1");
        request.putValue("city", city);
        request.putValue("keyword", keyWord);
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);

    }

    /*
    * 点击屏幕外隐藏键盘
    * */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (DateUtil.isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    private void getPacketReceiveState() {
        String url = NetConstant.GET_PACKET_RECEIVER_STATE;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        String data = jsonObject.getString("data");
                        if("1".equals(data)){
                            stateCb.setChecked(true);
                        }else{
                            stateCb.setChecked(false);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.Toast(context, "网络错误，请检查");
            }

        });
        request.putValue("userid", user.getUserId());
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);

    }

    private void changePacketReceiveState() {
        String url = NetConstant.CHANGE_PACKET_RECEIVER_STATE;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        String data = jsonObject.getString("data");
                        if("1".equals(data)){
                            stateCb.setChecked(true);
                        }else{
                            stateCb.setChecked(false);
                        }
                    }else{
                        ToastUtil.Toast(context, "切换状态失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.Toast(context, "网络错误，请检查");
            }
        });
        request.putValue("userid", user.getUserId());
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }

}
