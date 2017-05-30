package cn.xcom.helper.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.ChatListInfo;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.MyImageLoader;
import cn.xcom.helper.wheel.widget.ItemsRange;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 16/6/16.
 * 用户消息页
 */
public class UserMessageActivity extends BaseActivity implements View.OnClickListener {
    private String TAG = "";
    private Context mContext;
    private RelativeLayout rl_back;
    private List<ChatListInfo> chatLists;
    private UserInfo userInfo;
    private ListView listView;
    private ChatListAdapter chatListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_message);
        mContext = this;
        userInfo = new UserInfo(this);
        chatLists = new ArrayList<>();
        initView();

    }

    private void initView() {
        getChatList();
        rl_back = (RelativeLayout) findViewById(R.id.rl_user_message_back);
        rl_back.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.lv_chat_list);
        chatListAdapter = new ChatListAdapter();
        listView.setAdapter(chatListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("id", chatLists.get(position).getChat_uid());
                intent.putExtra("name", chatLists.get(position).getOther_nickname());

                startActivity(intent);
            }
        });

    }

    private void getChatList() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userInfo.getUserId());
        HelperAsyncHttpClient.get(NetConstant.GET_CHAT_LIST, requestParams,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            try {
                                String state = response.getString("status");
                                if (state.equals("success")) {
                                    String data = response.getString("data");
                                    List<ChatListInfo> infos = new Gson().fromJson(data,
                                            new TypeToken<List<ChatListInfo>>() {
                                            }.getType());
                                    chatLists.clear();
                                    chatLists.addAll(infos);
                                    chatListAdapter.notifyDataSetChanged();
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
            case R.id.rl_user_message_back:
                finish();
                break;

        }

    }

    class ChatListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return chatLists.size();
        }

        @Override
        public Object getItem(int position) {
            return chatLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_chat_list, parent, false);
                holder = new ViewHolder();

                holder.nameTv = (TextView) convertView.findViewById(R.id.tv_name);
                holder.contentTv = (TextView) convertView.findViewById(R.id.tv_content);
                holder.timeTv = (TextView) convertView.findViewById(R.id.tv_time);
                holder.iconIv = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.msgCountTv = (TextView) convertView.findViewById(R.id.tv_msgCount);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) (convertView.getTag());
            }
            ChatListInfo info = chatLists.get(position);
            holder.nameTv.setText(info.getOther_nickname());
            holder.timeTv.setText(info.getCreate_time());
            holder.contentTv.setText(info.getLast_content());
            if (Integer.parseInt(info.getNoreadcount()) < 1) {
                holder.msgCountTv.setVisibility(View.GONE);
            } else {
                holder.msgCountTv.setVisibility(View.VISIBLE);
                holder.msgCountTv.setText(info.getNoreadcount());
            }
            if (info.getOther_face() != null) {
                MyImageLoader.display(NetConstant.NET_DISPLAY_IMG +
                        info.getOther_face(), holder.iconIv);
            } else {
                MyImageLoader.display(NetConstant.NET_DISPLAY_IMG, holder.iconIv);
            }
            return convertView;
        }

        class ViewHolder {
            TextView nameTv, contentTv, timeTv, msgCountTv;
            ImageView iconIv;

        }


    }

    @Override
    protected void onResume() {
        getChatList();
        super.onResume();
    }
}
