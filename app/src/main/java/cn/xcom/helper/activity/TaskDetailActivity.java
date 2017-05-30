package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xcom.helper.R;
import cn.xcom.helper.adapter.ImgGridAdapter;
import cn.xcom.helper.bean.TaskInfo;
import cn.xcom.helper.bean.TaskItemInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.record.AudioPlayer;
import cn.xcom.helper.record.SoundView;
import cn.xcom.helper.utils.MyImageLoader;
import cn.xcom.helper.utils.NoScrollGridView;
import de.hdodenhof.circleimageview.CircleImageView;

public class TaskDetailActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.rl_dianhua)
    RelativeLayout rlDianhua;
    @BindView(R.id.rl_xiaoxi)
    RelativeLayout rlXiaoxi;
    @BindView(R.id.tv_tradeNo)
    TextView tvTradeNo;
    @BindView(R.id.tv_tradeName)
    TextView tvTradeName;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.item_sn_gridpic)
    NoScrollGridView itemSnGridpic;
    @BindView(R.id.sound_view)
    SoundView soundView;

    private Context context;
    private TaskInfo taskInfo;
    private TaskItemInfo taskItemInfo;
    private String type;
    private int time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_task_detail);
        ButterKnife.bind(this);
        context = this;
        taskInfo = (TaskInfo) getIntent().getSerializableExtra("taskInfo");
        taskItemInfo = (TaskItemInfo) getIntent().getSerializableExtra("taskItemInfo");
        type = getIntent().getStringExtra("type");
        setData();
    }

    /**
     * 为view设置数据
     */
    private void setData() {
        time = 0;
        if (type.equals("1")) {
            tvName.setText(taskInfo.getName());
            tvTradeNo.setText(taskInfo.getOrder_num());
            tvTradeName.setText(taskInfo.getTitle());
            tvPrice.setText(taskInfo.getPrice());
            tvAddress.setText(taskInfo.getAddress());
            Date date = new Date();
            date.setTime(Long.parseLong(taskInfo.getExpirydate()) * 1000);
            tvTime.setText(new SimpleDateFormat("MM月dd日  HH:mm").format(date));
            //获取图片字符串数组
            ArrayList<String> images = new ArrayList<>();
            for (int i=0;i<taskInfo.getFiles().size();i++){
                images.add(taskInfo.getFiles().get(i).getFile());
            }
            if(taskInfo.getFiles().size()>0){
                itemSnGridpic.setVisibility(View.VISIBLE);
                itemSnGridpic.setAdapter(new ImgGridAdapter(images, context));
            }
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + taskInfo.getPhoto(),ivAvatar);
            if(!TextUtils.isEmpty(taskInfo.getSoundtime())){
                time =Integer.valueOf(taskInfo.getSoundtime());
            }
            if(!TextUtils.isEmpty(taskInfo.getSound())){
                soundView.setVisibility(View.VISIBLE);
                soundView.init(NetConstant.NET_DISPLAY_IMG+taskInfo.getSound(),time);
            }
        } else if (type.equals("2")) {
            tvName.setText(taskItemInfo.getName());
            tvTradeNo.setText(taskItemInfo.getOrder_num());
            tvTradeName.setText(taskItemInfo.getTitle());
            tvPrice.setText(taskItemInfo.getPrice());
            tvAddress.setText(taskItemInfo.getAddress());
            Date date = new Date();
            date.setTime(Long.parseLong(taskItemInfo.getExpirydate()) * 1000);
            tvTime.setText(new SimpleDateFormat("MM月dd日  HH:mm").format(date));
            //获取图片字符串数组
            ArrayList<String> images = new ArrayList<>();
            for (int i=0;i<taskItemInfo.getFiles().size();i++){
                images.add(taskItemInfo.getFiles().get(i).getFile());
            }
            if(taskItemInfo.getFiles().size()>0){
                itemSnGridpic.setVisibility(View.VISIBLE);
                itemSnGridpic.setAdapter(new ImgGridAdapter(images, context));
            }
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + taskItemInfo.getPhoto(),ivAvatar);
            if(!TextUtils.isEmpty(taskItemInfo.getSoundtime())){
                time =Integer.valueOf(taskItemInfo.getSoundtime());
            }
            if(!TextUtils.isEmpty(taskItemInfo.getSound())){
                soundView.setVisibility(View.VISIBLE);
                soundView.init(NetConstant.NET_DISPLAY_IMG+taskItemInfo.getSound(),time);
            }
        }

    }

    @OnClick({R.id.rl_back, R.id.rl_dianhua, R.id.rl_xiaoxi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_dianhua:
                if (type.equals("1")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" +taskInfo.getPhone()));
                    context.startActivity(intent);
                }else if(type.equals("2")){
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" +taskItemInfo.getPhone()));
                    context.startActivity(intent);
                }
                break;
            case R.id.rl_xiaoxi:
                if (type.equals("1")) {
                    Intent intent = new Intent(context,ChatActivity.class);
                    intent.putExtra("id",taskInfo.getUserid());
                    intent.putExtra("name",taskInfo.getName());
                    context.startActivity(intent);
                }else if(type.equals("2")){
                    Intent intent = new Intent(context,ChatActivity.class);
                    intent.putExtra("id",taskItemInfo.getUserid());
                    intent.putExtra("name",taskItemInfo.getName());
                    context.startActivity(intent);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(AudioPlayer.isPlaying){
            AudioPlayer.getInstance().stopPlay();
        }
    }
}
