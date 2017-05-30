package cn.xcom.helper.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.adapter.AuthenticationSelectListAdapter;
import cn.xcom.helper.bean.Model;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.LogUtils;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun.
 */
public class AuthenticationListActivity extends  BaseActivity {
    private String TAG="AuthenticationListActivity";
    private Context mContext;
    private RelativeLayout rl_back;
    private TextView tv_select_first,tv_select_second,tv_select_third;
    private ListView lv_first,lv_second,lv_third;
    private AuthenticationSelectListAdapter adapter_first,adapter_second,adapter_third;
    private boolean first_display=false;
    private boolean second_display=false;
    private boolean third_display=false;
    private List<JSONObject> authenticationList=new ArrayList<JSONObject>();
    private List<String> sId=new ArrayList<String>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_authentication_list);
        mContext=this;
        initView();
        getDate();
    }

    private void getDate() {
        RequestParams params=new RequestParams();
        HelperAsyncHttpClient.get(NetConstant.NET_GET_AUTHENTICATION_LIST,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+response.toString());
                if (response!=null){
                    try {
                        String state=response.getString("status");
                        if (state.equals("success")){
                            JSONObject jsonObject=response.getJSONObject("data");

                        }if(state.equals("error")){
                            String data=response.getString("data");
                            Toast.makeText(mContext,data,Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+responseString);
            }
        });
    }

    private void initView() {
        rl_back= (RelativeLayout) findViewById(R.id.rl_authentication_list_back);
        tv_select_first= (TextView) findViewById(R.id.tv_authentication_list_select_first);
        tv_select_second= (TextView) findViewById(R.id.tv_authentication_list_select_second);
        tv_select_third= (TextView) findViewById(R.id.tv_authentication_list_select_third);
        lv_first= (ListView) findViewById(R.id.lv_authentication_list_first);
        lv_second= (ListView) findViewById(R.id.lv_authentication_list_second);
        lv_third= (ListView) findViewById(R.id.lv_authentication_list_third);

        MyOnclickListener mOnclickListener=new MyOnclickListener();
        rl_back.setOnClickListener(mOnclickListener);
        tv_select_first.setOnClickListener(mOnclickListener);
        tv_select_second.setOnClickListener(mOnclickListener);
        tv_select_third.setOnClickListener(mOnclickListener);
        adapter_first=new AuthenticationSelectListAdapter(mContext, Model.AUTHENTICATION_LIST_FIRST);
        adapter_first.setSelectItem(0);
        lv_first.setAdapter(adapter_first);
        lv_first.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter_first.setSelectItem(position);
                Drawable drawable = getResources().getDrawable(
                        R.mipmap.ic_arrow_down_black);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                        drawable.getMinimumHeight());
                tv_select_first.setCompoundDrawables(null, null,
                        drawable, null);
                tv_select_first.setText(Model.AUTHENTICATION_LIST_FIRST[position]);
                lv_first.setVisibility(View.GONE);
                first_display=false;
            }
        });
        adapter_second=new AuthenticationSelectListAdapter(mContext, Model.AUTHENTICATION_LIST_SECOND);
        adapter_second.setSelectItem(0);
        lv_second.setAdapter(adapter_second);
        lv_second.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter_second.setSelectItem(position);
                Drawable drawable = getResources().getDrawable(
                        R.mipmap.ic_arrow_down_black);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                        drawable.getMinimumHeight());
                tv_select_second.setCompoundDrawables(null, null,
                        drawable, null);
                tv_select_second.setText(Model.AUTHENTICATION_LIST_SECOND[position]);
                lv_second.setVisibility(View.GONE);
                second_display=false;
            }
        });
        adapter_third=new AuthenticationSelectListAdapter(mContext, Model.AUTHENTICATION_LIST_THIRD);
        adapter_third.setSelectItem(0);
        lv_third.setAdapter(adapter_third);
        lv_third.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter_third.setSelectItem(position);
                Drawable drawable = getResources().getDrawable(
                        R.mipmap.ic_arrow_down_black);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                        drawable.getMinimumHeight());
                tv_select_third.setCompoundDrawables(null, null,
                        drawable, null);
                tv_select_third.setText(Model.AUTHENTICATION_LIST_THIRD[position]);
                lv_third.setVisibility(View.GONE);
                third_display=false;
            }
        });

    }

    private class MyOnclickListener implements View.OnClickListener {
        public void onClick(View v) {
            int mID = v.getId();

            if (mID == R.id.rl_authentication_list_back) {
               finish();
            }

            if (mID == R.id.tv_authentication_list_select_third) {
                Drawable drawable = null;
                if (!third_display) {
                    drawable = getResources().getDrawable(
                            R.mipmap.ic_arrow_up_black);
                    lv_third.setVisibility(View.VISIBLE);
                    adapter_third.notifyDataSetChanged();
                    third_display = true;
                } else {
                    drawable = getResources().getDrawable(
                            R.mipmap.ic_arrow_down_black);
                    lv_third.setVisibility(View.GONE);
                    third_display = false;
                }
                //这一步必须要做，否则不会显示
                drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                        drawable.getMinimumHeight());
                tv_select_third.setCompoundDrawables(null, null,
                        drawable, null);
            } else {
                Drawable drawable = getResources().getDrawable(
                        R.mipmap.ic_arrow_down_black);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                        drawable.getMinimumHeight());
                tv_select_third.setCompoundDrawables(null, null,
                        drawable, null);
                lv_third.setVisibility(View.GONE);
                third_display = false;

            }
            if (mID == R.id.tv_authentication_list_select_second) {
                Drawable drawable = null;
                if (!second_display) {
                    drawable = getResources().getDrawable(
                            R.mipmap.ic_arrow_up_black);
                    lv_second.setVisibility(View.VISIBLE);
                    adapter_second.notifyDataSetChanged();
                    second_display = true;
                } else {
                    drawable = getResources().getDrawable(
                            R.mipmap.ic_arrow_down_black);
                    lv_second.setVisibility(View.GONE);
                    second_display = false;
                }
                //这一步必须要做，否则不会显示
                drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                        drawable.getMinimumHeight());
                tv_select_second.setCompoundDrawables(null, null,
                        drawable, null);
            } else {
                Drawable drawable = getResources().getDrawable(
                        R.mipmap.ic_arrow_down_black);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                        drawable.getMinimumHeight());
                tv_select_second.setCompoundDrawables(null, null,
                        drawable, null);
                lv_second.setVisibility(View.GONE);
                second_display = false;
            }
            if (mID == R.id.tv_authentication_list_select_first) {
                Drawable drawable = null;
                if (!first_display) {
                    drawable = getResources().getDrawable(
                            R.mipmap.ic_arrow_up_black);
                    lv_first.setVisibility(View.VISIBLE);
                    adapter_first.notifyDataSetChanged();
                    first_display = true;
                } else {
                    drawable = getResources().getDrawable(
                            R.mipmap.ic_arrow_down_black);
                    lv_first.setVisibility(View.GONE);
                    first_display = false;
                }
                //这一步必须要做，否则不会显示
                drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                        drawable.getMinimumHeight());
                tv_select_first.setCompoundDrawables(null, null,
                        drawable, null);
            } else {
                Drawable drawable = getResources().getDrawable(
                        R.mipmap.ic_arrow_down_black);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                        drawable.getMinimumHeight());
                tv_select_first.setCompoundDrawables(null, null,
                        drawable, null);
                lv_first.setVisibility(View.GONE);
                first_display = false;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (third_display == true) {

                Drawable drawable = getResources().getDrawable(
                        R.mipmap.ic_arrow_down_black);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                        drawable.getMinimumHeight());
                tv_select_third.setCompoundDrawables(null, null,
                        drawable, null);
                lv_third.setVisibility(View.GONE);
                third_display = false;
            } else if (second_display == true) {
                Drawable drawable = getResources().getDrawable(
                        R.mipmap.ic_arrow_down_black);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                        drawable.getMinimumHeight());
                tv_select_second.setCompoundDrawables(null, null,
                        drawable, null);
                lv_second.setVisibility(View.GONE);
                second_display = false;
            } else if (first_display == true) {
                Drawable drawable = getResources().getDrawable(
                        R.mipmap.ic_arrow_down_black);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                        drawable.getMinimumHeight());
                tv_select_first.setCompoundDrawables(null, null,
                        drawable, null);
                lv_first.setVisibility(View.GONE);
                first_display = false;
            } else {
                finish();
            }
        }
        return false;
    }
}
