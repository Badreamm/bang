package cn.xcom.helper.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import cn.xcom.helper.R;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.MyImageLoader;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class MapBottomPopWindow extends PopupWindow {

    ImageView ivAvatar;
    TextView tvName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.ll_left)
    LinearLayout llLeft;
    @BindView(R.id.btn_chat)
    ImageView btnChat;
    @BindView(R.id.tv_distance_new)
    TextView tvDistanceNew;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_count)
    TextView tvCount;
    LinearLayout linearLayout;
    private Context mContext;
    private View view;
    private String names;
    private String photoUrls;
    private String taddress;
    private String tcount;

    public MapBottomPopWindow(Context context,String name,String photoUrl,String address,String count,View.OnClickListener onClickListener) {
        super(context);
        this.mContext = context;
        this.view = LayoutInflater.from(context).inflate(R.layout.item_authentication_info, null);
        ivAvatar = (ImageView)view.findViewById(R.id.iv_avatar) ;
        tvName = (TextView) view.findViewById(R.id.tv_name) ;
        linearLayout = (LinearLayout)view.findViewById(R.id.bottom_pop_ly);
        linearLayout.setOnClickListener(onClickListener);
        ivAvatar.setOnClickListener(onClickListener);
        this.photoUrls=photoUrl;
        MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + photoUrls, ivAvatar);
        this.names = name;
        tvName.setText(names);
        tvAddress = (TextView)view.findViewById(R.id.tv_address) ;
        this.taddress=address;
        tvAddress.setText(taddress);
        tvCount = (TextView)view.findViewById(R.id.tv_count);
        this.tcount = count;
        tvCount.setText("服务"+tcount+"次");
        this.setOutsideTouchable(true);
         /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
//        this.setAnimationStyle(R.style.take_photo_anim);
    }
}
