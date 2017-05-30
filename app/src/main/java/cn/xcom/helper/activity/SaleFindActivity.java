package cn.xcom.helper.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.adapter.SaleAdapter;
import cn.xcom.helper.bean.Front;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ToastUtil;
import cn.xcom.helper.utils.ToastUtils;
import cn.xcom.helper.view.DividerItemDecoration;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class SaleFindActivity extends BaseActivity implements View.OnClickListener {
    public ImageView img_back;
    public XRecyclerView lv_find;
    public TextView tv_find;
    public EditText et_find;
    private SaleAdapter saleAdapter;
    private String keyWord;
    private List<Front> addlist = new ArrayList<>();
    private String currentType = "0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_find);
        ButterKnife.bind(this);

        lv_find = (XRecyclerView) findViewById(R.id.list_find);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lv_find.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        lv_find.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        lv_find.setLayoutManager(linearLayoutManager);
        lv_find.addItemDecoration(new DividerItemDecoration(this
                , DividerItemDecoration.VERTICAL_LIST));
        lv_find.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getNewData(currentType, keyWord);

            }

            @Override
            public void onLoadMore() {
                getMore(currentType, keyWord);
            }
        });
        saleAdapter = new SaleAdapter(addlist, SaleFindActivity.this);

        initView();
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void initView() {
        img_back = (ImageView) findViewById(R.id.sale_back);
        img_back.setOnClickListener(this);
        tv_find = (TextView) findViewById(R.id.tv_find);
        tv_find.setOnClickListener(this);
        et_find = (EditText) findViewById(R.id.sfa_search_edt);
        et_find.setOnClickListener(this);
        keyWord = getIntent().getStringExtra("keyword");
        if (!HelperApplication.getInstance().saleBack) {
            getNewData(currentType, keyWord);
        }
        HelperApplication.getInstance().saleBack = false;

    }

    private void getNewData(String type, String keyWord) {
        String url = NetConstant.GOODSLIST;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("=====显示111", "" + s);
                    JSONObject jsonObject = new JSONObject(s);
                    String state = jsonObject.getString("status");
                    if (state.equals("success")) {
                        String jsonObject1 = jsonObject.getString("data");
                        Gson gson = new Gson();
                        addlist.clear();
                        List<Front> fronts = gson.fromJson(jsonObject1,
                                new TypeToken<ArrayList<Front>>() {
                                }.getType());
                        Log.e("========fragment", "" + addlist.size());
                        addlist.addAll(fronts);
                        saleAdapter = new SaleAdapter(addlist, SaleFindActivity.this);
                        lv_find.setAdapter(saleAdapter);
//                        ToastUtils.showToast(mContext, "刷新成功");
                    } else {
                        addlist.clear();
                        saleAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                lv_find.refreshComplete();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(getApplication(), "网络连接错误，请检查您的网络");
                lv_find.refreshComplete();
            }
        });
        request.putValue("city", HelperApplication.getInstance().mDistrict);
        request.putValue("type", type);
        request.putValue("beginid", "0");
        request.putValue("keyword", keyWord);

        SingleVolleyRequest.getInstance(getApplication()).addToRequestQueue(request);
    }


    private void getMore(String type, String keyWord) {
        String url = NetConstant.GOODSLIST;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Log.d("---goodslist", jsonObject.toString());
                    String state = jsonObject.getString("status");
                    if (state.equals("success")) {
                        String jsonObject1 = jsonObject.getString("data");
                        Gson gson = new Gson();
                        List<Front> fronts = gson.fromJson(jsonObject1,
                                new TypeToken<ArrayList<Front>>() {
                                }.getType());
                        addlist.addAll(fronts);

                    } else if (state.equals("error")) {
                        lv_find.noMoreLoading();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                lv_find.loadMoreComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(getApplication(), "网络连接错误，请检查您的网络");
                lv_find.loadMoreComplete();
            }
        });
        request.putValue("city", HelperApplication.getInstance().mDistrict);
        Front front = addlist.get(addlist.size() - 1);
        Log.d("---front", front.getId() + "" + "city" + HelperApplication.getInstance().mDistrict + "type" + type + "keyword");
        request.putValue("type", type);
        request.putValue("beginid", front.getId());
        request.putValue("keyword", keyWord);
        SingleVolleyRequest.getInstance(getApplication()).addToRequestQueue(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sale_back:
                finish();
                break;
            case R.id.tv_find:
                keyWord = et_find.getText().toString();
                if (keyWord.equals("")) {
                    ToastUtil.showShort(SaleFindActivity.this, "请输入要搜索的内容");
                } else {
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SaleFindActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    getNewData(currentType, keyWord);
                }

                break;
            case R.id.sfa_search_edt:

                break;

        }

    }


}
