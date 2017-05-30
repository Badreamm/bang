package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import cn.xcom.helper.adapter.DivideAdapter;
import cn.xcom.helper.adapter.GroupAdapter;
import cn.xcom.helper.bean.DictionaryList;
import cn.xcom.helper.constant.NetConstant;

import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;

public class DivideActivity extends BaseActivity implements View.OnClickListener {
    private ListView lv;
    private RelativeLayout rl_i_help_back;
    private DivideAdapter divideAdapter;
    private List<DictionaryList>groups;
    private int resultCode=6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_divide);

        lv= (ListView) findViewById(R.id.lv_divide);
        rl_i_help_back= (RelativeLayout) findViewById(R.id.rl_i_help_back);
        rl_i_help_back.setOnClickListener(this);

        String url1= NetConstant.DICTIONARYS_LIST;
        StringPostRequest request=new StringPostRequest(url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("=====显示", "" + s);
                    JSONObject jsonObject=new JSONObject(s);
                    String state = jsonObject.getString("status");
                    if (state.equals("success")){
                        String jsonObject1 = jsonObject.getString("data");
                        Gson gson = new Gson();
                        groups=gson.fromJson(jsonObject1,
                                new TypeToken<ArrayList<DictionaryList>>() {
                                }.getType());
                        divideAdapter = new DivideAdapter(getBaseContext(),groups);
                        lv.setAdapter(divideAdapter);
                        divideAdapter.notifyDataSetChanged();
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
        request.putValue("type", "3");
        SingleVolleyRequest.getInstance(getBaseContext()).addToRequestQueue(request);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s=groups.get(position).getName();
                String id1=groups.get(position).getId();
                Log.d("=====字典的名字", s);
                Intent intent=new Intent(DivideActivity.this, ReleaseActivity.class);
                intent.putExtra("dictionary", s);
                intent.putExtra("id", id1);
               // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                setResult(resultCode, intent);
                finish();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_i_help_back:
                finish();
                break;
        }
    }
}
