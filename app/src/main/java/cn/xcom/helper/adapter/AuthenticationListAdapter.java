package cn.xcom.helper.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import cn.xcom.helper.R;
import cn.xcom.helper.activity.ChatActivity;
import cn.xcom.helper.activity.DetailAuthenticatinActivity;
import cn.xcom.helper.bean.AuthenticationList;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.record.SoundView;
import cn.xcom.helper.utils.MyImageLoader;

/**
 * Created by zhuchongkun on 16/8/31.
 */
public class AuthenticationListAdapter extends RecyclerView.Adapter<AuthenticationListAdapter.ViewHolder> {
    private List<AuthenticationList> authList;
    private Context context;

    public AuthenticationListAdapter(Context context, List<AuthenticationList> authList) {
        this.authList = authList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_authentication_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AuthenticationList a = authList.get(position);
        holder.tvName.setText(a.getName());
        holder.tvAddress.setText(a.getAddress());
        holder.tvDistanceNew.setText(a.getDistance() == 10000000000L
                ? "1000km+" : a.getDistance() / 1000.0 + "km");
        holder.tvCount.setText(a.getServiceCount() + "次");
        if ("".equals(a.getPhoto())) {
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG, holder.ivAvatar);
        } else {
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG +
                    a.getPhoto(), holder.ivAvatar);
        }

        holder.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("id", a.getId());
                intent.putExtra("name", a.getName());
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailAuthenticatinActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("authentication", a);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return authList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvName;
        TextView tvTime;
        LinearLayout llLeft;
        ImageView btnChat;
        TextView tvDistanceNew;
        TextView tvAddress;
        TextView tvType;
        TextView tvCount;
        SoundView soundView;

        public ViewHolder(View view) {
            super(view);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            llLeft = (LinearLayout) view.findViewById(R.id.ll_left);
            btnChat = (ImageView) view.findViewById(R.id.btn_chat);
            tvDistanceNew = (TextView) view.findViewById(R.id.tv_distance_new);
            tvAddress = (TextView) view.findViewById(R.id.tv_address);
            tvType = (TextView) view.findViewById(R.id.tv_type);
            tvCount = (TextView) view.findViewById(R.id.tv_count);
            soundView = (SoundView) view.findViewById(R.id.sound_view);
        }

    }
}
