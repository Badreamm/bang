package cn.xcom.helper.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.ChatInfo;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.MyImageLoader;
import cn.xcom.helper.utils.TimeUtils;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/10/21 0021.
 */

public class ChatActivity extends BaseActivity implements View.OnClickListener {
    private TextView titleNameTv;
    private Button sendBtn;
    private ListView listView;
    private EditText contentEdt;
    private List<ChatInfo> chatInfos;
    private Context mContext;
    private UserInfo userInfo;
    private String receiverId;
    private ChatAdapter chatAdapter;
    private String chatName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        receiverId = getIntent().getStringExtra("id");
        chatName = getIntent().getStringExtra("name");
        mContext = this;
        userInfo = new UserInfo(this);
        initView();
        getData();
    }

    private void initView() {
        titleNameTv = (TextView) findViewById(R.id.tv_title_name);
        if (chatName != null && !"".equals(chatName)) {
            titleNameTv.setText(chatName);
        }
        sendBtn = (Button) findViewById(R.id.btn_send);
        sendBtn.setOnClickListener(this);
        contentEdt = (EditText) findViewById(R.id.edt_content);
        listView = (ListView) findViewById(R.id.lv_chat_list);
        chatInfos = new ArrayList<>();
        chatAdapter = new ChatAdapter();
        listView.setAdapter(chatAdapter);

    }

    private void getData() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("send_uid", userInfo.getUserId());
        requestParams.put("receive_uid", receiverId);
        HelperAsyncHttpClient.get(NetConstant.GET_CHAT_DATA, requestParams,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            try {
                                String state = response.getString("status");
                                if (state.equals("success")) {
                                    String data = response.getString("data");
                                    List<ChatInfo> infos = new Gson().fromJson(data,
                                            new TypeToken<List<ChatInfo>>() {
                                            }.getType());
                                    chatInfos.clear();
                                    chatInfos.addAll(infos);
                                    chatAdapter.notifyDataSetChanged();
                                    listView.setSelection(listView.getBottom());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void sendChatData() {
        final String content = contentEdt.getText().toString();
        if (content.equals("")) {
            return;
        } else {
            contentEdt.setText("");
        }
        RequestParams params = new RequestParams();
        params.put("send_uid", userInfo.getUserId());
        params.put("receive_uid", receiverId);
        params.put("content", content);
        HelperAsyncHttpClient.get(NetConstant.SEND_CHAT_DATA, params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            try {
                                Log.e("chat",response.toString());
                                String state = response.getString("status");
                                if (state.equals("success")) {
                                    String data = response.getString("data");
                                    ChatInfo info = new Gson().fromJson(data,
                                            new TypeToken<ChatInfo>() {
                                            }.getType());
                                    info.setSend_face(userInfo.getUserImg());
                                    chatInfos.add(info);
                                    chatAdapter.notifyDataSetChanged();
                                    listView.setSelection(listView.getBottom());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                sendChatData();
                break;
        }
    }


    class ChatAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return chatInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return chatInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Holder holder;
            View sendItemView = LayoutInflater.from(mContext).inflate(R.layout.item_chat_send_message, null);
            View receiveItemView = LayoutInflater.from(mContext).inflate(R.layout.item_chat_receive_message, null);
            ChatInfo chatInfo = chatInfos.get(position);
            int type = getItemViewType(position);
            if (convertView == null) {
                holder = new Holder();
                switch (type) {
                    case 0:
                        convertView = sendItemView;
                        holder.timeTv = (TextView) convertView.findViewById(R.id.tv_time);
                        holder.contentTv = (TextView) convertView.findViewById(R.id.tv_content);
                        holder.senderImg = (ImageView) convertView.findViewById(R.id.iv_sender_icon);
                        break;
                    case 1:
                        convertView = receiveItemView;
                        holder.timeTv = (TextView) convertView.findViewById(R.id.tv_time);
                        holder.contentTv = (TextView) convertView.findViewById(R.id.tv_content);
                        holder.receiverImg = (ImageView) convertView.findViewById(R.id.iv_receiver_icon);
                        break;
                }
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            long time = Long.parseLong(chatInfo.getCreate_time()) * 1000;
            holder.timeTv.setText(TimeUtils.fromateTimeShowByRule(time));
            holder.contentTv.setText(chatInfo.getContent());

            if (type == 0) {
                if (holder.senderImg != null) {
                    if (chatInfo.getSend_face() != null) {
                        MyImageLoader.display(NetConstant.NET_DISPLAY_IMG +
                                chatInfo.getSend_face(), holder.senderImg);
                    } else {
                        MyImageLoader.display(NetConstant.NET_DISPLAY_IMG, holder.senderImg);
                    }
                }
            } else {
                if (holder.receiverImg != null) {
                    if (chatInfo.getReceive_face() != null) {
                        MyImageLoader.display(NetConstant.NET_DISPLAY_IMG +
                                chatInfo.getReceive_face(), holder.receiverImg);
                    } else {
                        MyImageLoader.display(NetConstant.NET_DISPLAY_IMG, holder.receiverImg);
                    }
                }
            }


            return convertView;
        }

        @Override
        public int getItemViewType(int position) {
            ChatInfo chatInfo = chatInfos.get(position);
            if (chatInfo.getSend_uid().equals(userInfo.getUserId())) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        class Holder {
            TextView timeTv, contentTv;
            ImageView senderImg, receiverImg;
        }

    }

    public void onBack(View v) {
        finish();
    }
}
