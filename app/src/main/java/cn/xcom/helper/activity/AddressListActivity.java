package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.Adress;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;

/**
 * Created by zhuchongkun on 16/6/17.
 */
public class AddressListActivity extends BaseActivity implements View.OnClickListener {
    public static final int FROM_ME = 0;//我的地址
    public static final int FROM_BUY = 1;//购入页面
    private String TAG = "AddressListActivity";
    private Context mContext;
    private RelativeLayout rl_back;
    private Button bt_address;
    private ListView lv_address_list;
    private AdressAdapter adressAdapter;
    private List<Adress> adressesList;
    private UserInfo userInfo;
    int position1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == position1) ;
            adressAdapter.notifyDataSetChanged();
        }
    };
    private int from = FROM_ME;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_address_list);
        mContext = this;
        from = getIntent().getIntExtra("from",FROM_ME);
        initView();
        adressList();
    }

    private void initView() {
        userInfo = new UserInfo();
        userInfo.readData(mContext);
        adressesList = new ArrayList<>();
        rl_back = (RelativeLayout) findViewById(R.id.rl_address_list_back);
        rl_back.setOnClickListener(this);
        bt_address = (Button) findViewById(R.id.bt_address_list_add);
        bt_address.setOnClickListener(this);
        lv_address_list = (ListView) findViewById(R.id.lv_address_list);

    }

    //获取地址列表
    public void adressList() {
        String url = NetConstant.GET_ADRESS;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        adressesList = gson.fromJson(data, new TypeToken<ArrayList<Adress>>() {
                        }.getType());
                        adressAdapter = new AdressAdapter(adressesList, mContext);
                        lv_address_list.setAdapter(adressAdapter);
                        adressAdapter.notifyDataSetChanged();
                        /*lv_address_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent();
                                Log.e("address", "yes");
                                intent.putExtra("address", adressesList.get(position).getAddress());
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });*/
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
        request.putValue("userid", userInfo.getUserId());
        SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_address_list_back:
                finish();
                break;
            case R.id.bt_address_list_add:
                startActivityForResult(new Intent(mContext, EditAddressActivity.class), 1);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 9) {
            if (requestCode == 1) {
                adressList();
            }

        }
    }

    class AdressAdapter extends BaseAdapter {
        private List<Adress> list;
        private Context context;

        public AdressAdapter(List<Adress> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.adresslist_layout, null);
                viewHolder.textadress = (TextView) convertView.findViewById(R.id.tv_adress);
                viewHolder.delete = (TextView) convertView.findViewById(R.id.delete_address);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final Adress adress = list.get(position);
            viewHolder.textadress.setText(adress.getAddress().toString());
//

            //删除地址
            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position1 = position;
                    final Message message = Message.obtain();
                    String url = NetConstant.DELETE_ADRESS;
                    StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                Log.d("=====", s);
                                String status = jsonObject.getString("status");
                                if (status.equals("success")) {
                                    Toast.makeText(mContext, "删除成功", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(mContext, "删除失败", Toast.LENGTH_LONG).show();
                                }
                                list.remove(position);
                                message.what = position;
                                handler.sendMessage(message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    });
                    request.putValue("addressid", adress.getId());
                    Log.d("=====adress.getId()", adress.getId());
                    Log.d("userid", userInfo.getUserId());
                    request.putValue("userid", userInfo.getUserId());
                    SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (from == FROM_ME) {
                        return;
                    }
                    Intent intent = new Intent();
                    Log.e("address", "yes");
                    intent.putExtra("address", adressesList.get(position).getAddress());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView textadress;
            TextView delete;
        }
    }
}
