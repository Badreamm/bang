package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.Packet;
import cn.xcom.helper.bean.PacketRecord;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.MyImageLoader;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.TimeUtils;
import cn.xcom.helper.utils.ToastUtil;

/**
 * Created by hzh on 2017/6/12.
 */

public class PacketDetailActivity extends BaseActivity {
    private ImageView avatarIv;
    private TextView totalMoneyTv, messageTv;
    private ListView recordLv;
    private UserInfo user;
    private String packetId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        packetId = getIntent().getStringExtra("packetid");
        user = new UserInfo(this);
        setContentView(R.layout.activity_packet_detail);
        avatarIv = (ImageView) findViewById(R.id.avatar);
        totalMoneyTv = (TextView) findViewById(R.id.tv_total_money);
        messageTv = (TextView) findViewById(R.id.tv_message);
        recordLv = (ListView) findViewById(R.id.lv_record);
        getPacketState(packetId);
    }

    private void setData(Packet packet){
        MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + packet.getPhoto(), avatarIv);
        if (packet.getIspacket() != null && packet.getIspacket().size() > 0) {
            messageTv.setText("已领取" + packet.getIspacket().size() + "/" + packet.getCount() + ",剩" + packet.getLeft_money() + "/" + packet.getMoney() + "元");
            recordLv.setAdapter(new DetailAdapter(this, packet.getIspacket()));
            for (PacketRecord record : packet.getIspacket()) {
                if(record.getName().equals(user.getUserName())){
                    totalMoneyTv.setText(record.getMoney()+"元");
                    break;
                }
            }
        }
    }

    private void getPacketState(String packetId) {
        String url = NetConstant.GET_PACKET_STATE;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        String date = jsonObject.getString("data");
                        Gson gson = new Gson();
                        Packet packet = gson.fromJson(date, Packet.class);
                        setData(packet);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.Toast(PacketDetailActivity.this, "网络错误，请检查");
            }
        });
        request.putValue("userid", user.getUserId());
        request.putValue("packetid", packetId);
        SingleVolleyRequest.getInstance(PacketDetailActivity.this).addToRequestQueue(request);
    }


    class DetailAdapter extends BaseAdapter {
        private Context context;
        private List<PacketRecord> records;

        public DetailAdapter(Context context, List<PacketRecord> records) {
            this.context = context;
            this.records = records;
        }

        @Override
        public int getCount() {
            return records.size();
        }

        @Override
        public Object getItem(int position) {
            return records.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_packet_detail, parent, false);
            Holder holder;
            if (convertView == null) {
                convertView = view;
                holder = new Holder();
                holder.nameTv = (TextView) convertView.findViewById(R.id.name);
                holder.moneyTv = (TextView) convertView.findViewById(R.id.money);
                holder.timeTv = (TextView) convertView.findViewById(R.id.time);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            PacketRecord packetRecord = records.get(position);
            holder.nameTv.setText(packetRecord.getName());
            holder.moneyTv.setText(packetRecord.getMoney()+"元");
            holder.timeTv.setText(TimeUtils.getDateToString(packetRecord.getCreate_time() * 1000));
            return convertView;
        }

        class Holder {
            TextView nameTv, moneyTv, timeTv;
        }
    }


    public void back(View view) {
        finish();
    }
}
