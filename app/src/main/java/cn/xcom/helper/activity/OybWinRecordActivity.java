package cn.xcom.helper.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.OybGood;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
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

public class OybWinRecordActivity extends BaseActivity {
    private XRecyclerView xRecyclerView;
    private OrderAdapter adapter;
    private List<OybGood> goodLists;

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
        goodLists = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNewData();
    }

    private void getNewData() {
        String url = NetConstant.GET_OYB_WIN_RECORD;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String state = jsonObject.getString("status");
                    if (state.equals("success")) {
                        JSONArray arr = jsonObject.getJSONArray("data");
                        for(int i = 0;i<arr.length();i++){
                            JSONObject object = arr.getJSONObject(i);
                            if(object.opt("smeta").equals("")){
                                object.put("smeta",null);
                            }
                        }
                        String data = arr.toString();
                        goodLists = new Gson().fromJson(data,new TypeToken<ArrayList<OybGood>>() {
                        }.getType());
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
                ToastUtils.showToast(OybWinRecordActivity.this, "网络连接错误，请检查您的网络");
                xRecyclerView.refreshComplete();
            }
        });
        request.putValue("userid", new UserInfo(OybWinRecordActivity.this).getUserId());
        SingleVolleyRequest.getInstance(OybWinRecordActivity.this).addToRequestQueue(request);
    }


    class OrderAdapter extends RecyclerView.Adapter<ViewHolder> {


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(OybWinRecordActivity.this).inflate(R.layout.item_oyb_win_record, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final OybGood good = goodLists.get(position);
            holder.titleTv.setText(good.getTitle());
            holder.countTv.setText(good.getCount() + "次");
            if (good.getSmeta() != null && good.getSmeta().size() > 0) {
                MyImageLoader.display(NetConstant.NEW_IMG_DISPLAY +
                        good.getSmeta().get(0).getUrl(), holder.imageView);
            } else {
                MyImageLoader.display(NetConstant.NEW_IMG_DISPLAY, holder.imageView);
            }

            holder.allNeedTv.setText(good.getPrice());
            holder.winNumTv.setText(good.getNum());
            holder.annoTimeTv.setText(TimeUtils.newChangeTime(good.getTime()));

            if (good.getIscomment().equals("0")) {
                holder.getPrizeBtn.setVisibility(View.VISIBLE);
            } else {
                holder.getPrizeBtn.setVisibility(View.GONE);
            }
            holder.getPrizeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(OybWinRecordActivity.this,OybPublishCommentActivity.class);
                    intent.putExtra("id",good.getId());
                    startActivity(intent);
                }
            });

            holder.myNumBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(OybWinRecordActivity.this).setTitle("我的号码")
                            .setMessage(good.getPrize_num())
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            });

        }

        @Override
        public int getItemCount() {
            return goodLists.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView titleTv, countTv,allNeedTv;
        private LinearLayout annoFlag;
        private TextView winNumTv;
        private AlwaysMarqueeTextView annoTimeTv;
        private Button myNumBtn, getPrizeBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_iv);
            titleTv = (TextView) itemView.findViewById(R.id.title_tv);
            countTv = (TextView) itemView.findViewById(R.id.count_tv);
            allNeedTv = (TextView) itemView.findViewById(R.id.all_need_tv);
            annoFlag = (LinearLayout) itemView.findViewById(R.id.anno_flag);
            winNumTv = (TextView) itemView.findViewById(R.id.win_num_tv);
            annoTimeTv = (AlwaysMarqueeTextView) itemView.findViewById(R.id.anno_time_tv);
            myNumBtn = (Button) itemView.findViewById(R.id.my_num_btn);
            getPrizeBtn = (Button) itemView.findViewById(R.id.get_prize_btn);
        }
    }

}
