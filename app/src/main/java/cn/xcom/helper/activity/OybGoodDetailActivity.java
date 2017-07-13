package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.youth.banner.Banner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.bean.OybGood;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.GlideImageLoader;
import cn.xcom.helper.utils.MyImageLoader;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.TimeUtils;
import cn.xcom.helper.utils.ToastUtils;
import cn.xcom.helper.view.DividerItemDecoration;
import me.himanshusoni.quantityview.QuantityView;

/**
 * Created by hzh on 2017/7/8.
 */

public class OybGoodDetailActivity extends BaseActivity {
    private XRecyclerView xRecyclerView;
    private Banner banner;
    private TextView needTv, nowTv, countTv, title;
    private String mark;
    private List<OybGood.Comment> comments;
    private ProgressBar progressBar;
    private QuantityView quantityView;
    private TextView buyTv;
    private OybGood oybGood;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_oyb_good_detail);
        mark = getIntent().getStringExtra("mark");
        initView();
    }

    private void initView() {
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        xRecyclerView = (XRecyclerView) findViewById(R.id.recycler_view);
        View headView = LayoutInflater.from(this).inflate(R.layout.headviewt_oyb_good_detail, null);
        banner = (Banner) headView.findViewById(R.id.banner);
        needTv = (TextView) headView.findViewById(R.id.need_count_tv);
        nowTv = (TextView) headView.findViewById(R.id.now_count_tv);
        countTv = (TextView) headView.findViewById(R.id.left_count_tv);
        title = (TextView) headView.findViewById(R.id.title);
        progressBar = (ProgressBar) headView.findViewById(R.id.progress_bar);
        quantityView = (QuantityView) findViewById(R.id.quantity_view);
        buyTv = (TextView) findViewById(R.id.buy_tv);
        buyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buy();
            }
        });
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
                xRecyclerView.loadMoreComplete();
            }
        });
        xRecyclerView.addHeaderView(headView);
        getNewData();
    }

    private void getNewData() {
        String url = NetConstant.GET_OYB_DETAIL;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String state = jsonObject.getString("status");
                    if (state.equals("success")) {
                        JSONObject j = jsonObject.getJSONObject("data");
                        if(j.opt("smeta").equals("")){
                            j.put("smeta",null);
                        }
                        String data = j.toString();
                        oybGood = new Gson().fromJson(data, new TypeToken<OybGood>() {
                        }.getType());
                        comments = oybGood.getComments();
                        xRecyclerView.setAdapter(new CommentAdapter());
                        title.setText(oybGood.getTitle());
                        needTv.setText("总需人数：" + oybGood.getPrice() + "人次");
                        nowTv.setText("已参与人数：" + oybGood.getCount() + "人次");
                        int letf = Integer.valueOf(oybGood.getPrice()) - Integer.valueOf(oybGood.getCount());
                        quantityView.setMaxQuantity(letf);
                        countTv.setText("剩余：" + letf + "人次");

                        if (!oybGood.getCount().equals("0")) {
                            int count = Integer.valueOf(oybGood.getCount());
                            int all = Integer.valueOf(oybGood.getPrice());
                            DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
                            //可以设置精确几位小数
                            df.setMaximumFractionDigits(1);
                            //模式 例如四舍五入
                            df.setRoundingMode(RoundingMode.HALF_UP);
                            double accuracy_num = count / Double.valueOf(all) * 100;
                            progressBar.setProgress((int) accuracy_num);
                        }
                        List<String> images = new ArrayList<>();
                        if (oybGood.getSmeta() != null && oybGood.getSmeta().size() > 0) {
                            for (int i = 0; i < oybGood.getSmeta().size(); i++) {
                                images.add(NetConstant.NEW_IMG_DISPLAY + oybGood.getSmeta().get(i).getUrl());
                            }
                            //设置图片加载器
                            banner.setImageLoader(new GlideImageLoader());
                            //设置图片集合
                            banner.setImages(images);
                            //banner设置方法全部调用完毕时最后调用
                            banner.start();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                xRecyclerView.refreshComplete();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(OybGoodDetailActivity.this, "网络连接错误，请检查您的网络");
                xRecyclerView.refreshComplete();
            }
        });
        request.putValue("mark", mark);
        SingleVolleyRequest.getInstance(OybGoodDetailActivity.this).addToRequestQueue(request);

    }

    private void buy(){
        int count = quantityView.getQuantity();
        if(count ==0){
            ToastUtils.showToast(OybGoodDetailActivity.this, "请选择商品数量");
            return;
        }
        String url = NetConstant.BUY_OYB_GOOD;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String state = jsonObject.getString("status");
                    if (state.equals("success")) {
                        String orderNumber = jsonObject.optString("data");
                        HelperApplication.getInstance().type="";
                        Intent intent = new Intent(OybGoodDetailActivity.this,PaymentActivity.class);
                        intent.putExtra("price", "0.01");//以后改为count
                        intent.putExtra("tradeNo", orderNumber);
                        intent.putExtra("body","一元购");
                        intent.putExtra("type","3");
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(OybGoodDetailActivity.this, "网络连接错误，请检查您的网络");
            }
        });
        request.putValue("id", oybGood.getId());
        request.putValue("userid",new UserInfo(OybGoodDetailActivity.this).getUserId());
        request.putValue("price",String.valueOf(count));
        SingleVolleyRequest.getInstance(OybGoodDetailActivity.this).addToRequestQueue(request);
    }

    class CommentAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(OybGoodDetailActivity.this).inflate(R.layout.item_oyb_comment, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final OybGood.Comment comment = comments.get(position);
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + comment.getPhoto(), holder.avatarIv);
            holder.nameTv.setText(comment.getName());
            holder.timeTv.setText(TimeUtils.newChangeTime(comment.getCreate_time()));
            holder.contentTv.setText(comment.getContent());
            if (comment.getPic() != null && comment.getPic().size() > 0) {
                holder.gridView.setAdapter(new GridViewAdapter(OybGoodDetailActivity.this, comment.getPic()));
                holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        List<String> addList = new ArrayList<String>();
                        for (int i = 0; i < comment.getPic().size(); i++) {
                            addList.add(NetConstant.NET_DISPLAY_IMG + comment.getPic().get(i).getPictureurl());
                        }
                        Intent intent = new Intent(OybGoodDetailActivity.this, SpaceImageDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("list111", (Serializable) addList);
                        intent.putExtra("position", position);
                        intent.putExtras(bundle);
                        int[] location = new int[2];
                        intent.putExtra("locationX", location[1]);//必须
                        intent.putExtra("locationY", location[0]);//必须
                        intent.putExtra("width", view.getWidth());//必须
                        intent.putExtra("height", view.getHeight());//必须
                        OybGoodDetailActivity.this.startActivity(intent);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView avatarIv;
        private TextView nameTv, timeTv, contentTv;
        private GridView gridView;

        public ViewHolder(View itemView) {
            super(itemView);
            avatarIv = (ImageView) itemView.findViewById(R.id.avatar_iv);
            nameTv = (TextView) itemView.findViewById(R.id.name_tv);
            timeTv = (TextView) itemView.findViewById(R.id.time_tv);
            contentTv = (TextView) itemView.findViewById(R.id.content_tv);
            gridView = (GridView) itemView.findViewById(R.id.grid_view);
        }
    }

    class GridViewAdapter extends BaseAdapter {
        private Context context;
        private List<OybGood.CommentPic> pics;

        public GridViewAdapter(Context context, List<OybGood.CommentPic> pics) {
            this.context = context;
            this.pics = pics;
        }

        @Override
        public int getCount() {
            return pics.size();
        }

        @Override
        public Object getItem(int position) {
            return pics.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GridViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.layout_gridview, null);
                viewHolder = new GridViewHolder();
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.add_image);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (GridViewHolder) convertView.getTag();
            }
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + pics.get(position).getPictureurl(), viewHolder.imageView);
            return convertView;
        }
    }

    class GridViewHolder {
        private ImageView imageView;
    }

}
