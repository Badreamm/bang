package cn.xcom.helper.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.adapter.CollectionAdapter;
import cn.xcom.helper.bean.Collection;
import cn.xcom.helper.bean.Front;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;

/**
 * Created by zhuchongkun on 16/6/12.
 * 收藏页
 */
public class CollectionActivity extends BaseActivity implements View.OnClickListener{
    private String TAG="";
    private Context mContext;
    private ListView listView;
    private RelativeLayout rl_back;
    private List<Collection> addList;
    private UserInfo userInfo;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CollectionAdapter collectionAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_collection);
        mContext=this;
        initView();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                collectionList();
            }
        });
        collectionList();
    }

    private void initView(){
        userInfo=new UserInfo();
        userInfo.readData(mContext);
        rl_back= (RelativeLayout) findViewById(R.id.rl_collection_back);
        rl_back.setOnClickListener(this);
        listView= (ListView) findViewById(R.id.collection_listView);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swif);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorTheme);
        //swip.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorTextWhite));
        addList = new ArrayList<>();
        collectionAdapter=new CollectionAdapter(addList,mContext);
        listView.setAdapter(collectionAdapter);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_collection_back:
                finish();
                break;
        }

    }
    public void collectionList(){
        String url= NetConstant.HAS_COLLECTION;
        StringPostRequest request=new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    String data=jsonObject.getString("data");
                    if (data!=null){
                        Gson gson=new Gson();
                        List<Collection> fronts =gson.fromJson(data,
                                new TypeToken<ArrayList<Collection>>(){}.getType());
                        addList.clear();
                        addList.addAll(fronts);
                        collectionAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.putValue("userid", userInfo.getUserId());
        SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);
    }
}
