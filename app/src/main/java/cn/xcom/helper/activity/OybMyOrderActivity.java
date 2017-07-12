package cn.xcom.helper.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.bean.Front;
import cn.xcom.helper.bean.OybGood;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.JsonResult;
import cn.xcom.helper.utils.MyImageLoader;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.TimeUtils;
import cn.xcom.helper.utils.ToastUtils;
import cn.xcom.helper.view.AlwaysMarqueeTextView;
import cn.xcom.helper.view.DividerItemDecoration;

/**
 * Created by hzh on 2017/7/11.
 */

public class OybMyOrderActivity extends BaseActivity {
    private XRecyclerView xRecyclerView;
    private JSONArray resultArray;
    private OrderAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oyb_my_order);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        xRecyclerView = (XRecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setLayoutManager(linearLayoutManager);
        xRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getNewData();
            }

            @Override
            public void onLoadMore() {

            }
        });
        resultArray = new JSONArray();
    }
    @Override
    protected void onResume() {
        super.onResume();
        getNewData();
    }

    private void getNewData() {
        String url = NetConstant.GET_OYB_MY_ORDER;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String state = jsonObject.getString("status");
                    if (state.equals("success")) {
                        resultArray = jsonObject.getJSONArray("data");
                        adapter = new OrderAdapter();
                        xRecyclerView.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                xRecyclerView.refreshComplete();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(OybMyOrderActivity.this, "网络连接错误，请检查您的网络");
                xRecyclerView.refreshComplete();
            }
        });
        request.putValue("userid", new UserInfo(OybMyOrderActivity.this).getUserId());
        SingleVolleyRequest.getInstance(OybMyOrderActivity.this).addToRequestQueue(request);
    }


    class OrderAdapter extends RecyclerView.Adapter<ViewHolder> {


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(OybMyOrderActivity.this).inflate(R.layout.item_oyb_my_order, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            JSONObject resultJs = null;
            try {
                resultJs = resultArray.getJSONObject(position);
                String js = resultJs.getString("goods");
                final OybGood good = new Gson().fromJson(js, new TypeToken<OybGood>() {
                }.getType());
                holder.titleTv.setText(good.getTitle());
                holder.countTv.setText(resultJs.optString("buy_count") + "次");
                if (good.getSmeta() != null && good.getSmeta().size() > 0) {
                    MyImageLoader.display(NetConstant.NEW_IMG_DISPLAY +
                            good.getSmeta().get(0).getUrl(), holder.imageView);
                } else {
                    MyImageLoader.display(NetConstant.NEW_IMG_DISPLAY, holder.imageView);
                }

                if (good.getStatus().equals("0")) {//未完成 进行中
                    holder.buyingFlag.setVisibility(View.VISIBLE);
                    holder.annoFlag.setVisibility(View.GONE);
                    int allNeed = Integer.valueOf(good.getPrice());//总价
                    int nowCount = Integer.valueOf(resultJs.optString("count"));
                    holder.yicanyuTv.setText(nowCount + "人");
                    holder.shengyu_tv.setText((allNeed - nowCount) + "人");

                    DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
                    //可以设置精确几位小数
                    df.setMaximumFractionDigits(1);
                    //模式 例如四舍五入
                    df.setRoundingMode(RoundingMode.HALF_UP);
                    double accuracy_num = nowCount / Double.valueOf(allNeed) * 100;
                    String result = df.format(accuracy_num);
                    holder.progressTv.setText(result + "%");
                    holder.progressBar.setProgress((int) accuracy_num);

                    holder.buyMoreBtn.setVisibility(View.VISIBLE);
                    holder.imageFlag.setImageDrawable(getResources().getDrawable(R.drawable.jinxingzhong));
                    holder.textFlagTv.setText("进行中");
                    holder.textFlagTv.setTextColor(getResources().getColor(R.color.msg_red));
                } else if (good.getStatus().equals("1")) {//已完成
                    holder.buyingFlag.setVisibility(View.GONE);
                    holder.annoFlag.setVisibility(View.VISIBLE);
                    holder.winUserTv.setText(good.getName());
                    holder.winNumTv.setText(good.getNum());
                    holder.annoTimeTv.setText(TimeUtils.newChangeTime(good.getTime()));

                    holder.buyMoreBtn.setVisibility(View.GONE);
                    holder.imageFlag.setImageDrawable(getResources().getDrawable(R.drawable.yijieshu));
                    holder.textFlagTv.setText("已完成");
                    holder.textFlagTv.setTextColor(getResources().getColor(R.color.gray));
                    if (good.getPhone() != null && good.getPhone().equals(new UserInfo(OybMyOrderActivity.this).getUserPhone())) {
                        holder.getPrizeBtn.setVisibility(View.VISIBLE);
                    } else {
                        holder.getPrizeBtn.setVisibility(View.GONE);
                    }
                } else {
                    holder.buyingFlag.setVisibility(View.GONE);
                    holder.annoFlag.setVisibility(View.GONE);

                    holder.buyMoreBtn.setVisibility(View.GONE);
                    holder.imageFlag.setImageDrawable(getResources().getDrawable(R.drawable.naozhong));
                    holder.textFlagTv.setText("待揭晓");
                    holder.textFlagTv.setTextColor(getResources().getColor(R.color.orange));
                }
                holder.getPrizeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(OybMyOrderActivity.this,OybPublishCommentActivity.class);
                        intent.putExtra("id",good.getId());
                        startActivity(intent);
                    }
                });

                holder.myNumBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(OybMyOrderActivity.this).setTitle("我的号码")
                                .setMessage(good.getPrize_num())
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return resultArray.length();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView titleTv, countTv;
        private LinearLayout buyingFlag;
        private TextView progressTv;
        private ProgressBar progressBar;
        private TextView yicanyuTv, shengyu_tv;
        private LinearLayout annoFlag;
        private TextView winUserTv, winNumTv;
        private AlwaysMarqueeTextView annoTimeTv;
        private ImageView imageFlag;
        private TextView textFlagTv;
        private Button buyMoreBtn, myNumBtn, getPrizeBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_iv);
            titleTv = (TextView) itemView.findViewById(R.id.title_tv);
            countTv = (TextView) itemView.findViewById(R.id.count_tv);
            buyingFlag = (LinearLayout) itemView.findViewById(R.id.buying_flag);
            progressTv = (TextView) itemView.findViewById(R.id.progress_tv);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            yicanyuTv = (TextView) itemView.findViewById(R.id.yicanyu_tv);
            shengyu_tv = (TextView) itemView.findViewById(R.id.shengyu_tv);
            annoFlag = (LinearLayout) itemView.findViewById(R.id.anno_flag);
            winUserTv = (TextView) itemView.findViewById(R.id.win_user_tv);
            winNumTv = (TextView) itemView.findViewById(R.id.win_num_tv);
            annoTimeTv = (AlwaysMarqueeTextView) itemView.findViewById(R.id.anno_time_tv);
            imageFlag = (ImageView) itemView.findViewById(R.id.image_flag);
            textFlagTv = (TextView) itemView.findViewById(R.id.text_flag_tv);
            buyMoreBtn = (Button) itemView.findViewById(R.id.buy_more_btn);
            myNumBtn = (Button) itemView.findViewById(R.id.my_num_btn);
            getPrizeBtn = (Button) itemView.findViewById(R.id.get_prize_btn);
        }
    }

}
