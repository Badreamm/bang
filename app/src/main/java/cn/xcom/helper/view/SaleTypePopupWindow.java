package cn.xcom.helper.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.adapter.GroupAdapter;
import cn.xcom.helper.bean.DictionaryList;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;

/**
 * Created by Administrator on 2016/8/11 0011.
 */
public class SaleTypePopupWindow extends PopupWindow {
    private Context mContext;
    private View view;
    public ListView lv_group;
    public GroupAdapter groupAdapter;
    public List <DictionaryList>addAllList=new ArrayList<>();


    public SaleTypePopupWindow(final Context mContext, AdapterView.OnItemClickListener itemClickListener) {
        this.mContext = mContext;

        this.view = LayoutInflater.from(mContext).inflate(R.layout.group_list, null);

        lv_group = (ListView) view.findViewById(R.id.lvGroup);

        // 设置按钮监听
        lv_group.setOnItemClickListener(itemClickListener);
        DictionaryList dictionaryList=new DictionaryList();
        dictionaryList.setId("1000");
        dictionaryList.setName("全部分类");
        addAllList.add(dictionaryList);

        String url1= NetConstant.DICTIONARYS_LIST;
        StringPostRequest request=new StringPostRequest(url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("=====显示", "" + s);
                    JSONObject jsonObject=new JSONObject(s);
                    String state = jsonObject.getString("status");
                    if (state.equals("success")){
                        String jsonObject1 = jsonObject.getString("data");
                        Gson gson = new Gson();
                        List<DictionaryList> groups=gson.fromJson(jsonObject1,
                                new TypeToken<ArrayList<DictionaryList>>() {
                                }.getType());
                        addAllList.addAll(groups);
                        Log.d("=====显示后面",""+addAllList.size());
                        groupAdapter = new GroupAdapter(mContext, addAllList);
                        lv_group.setAdapter(groupAdapter);
                        groupAdapter.notifyDataSetChanged();
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
        request.putValue("type","3");
        SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);

        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.lvGroup).getTop();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y > height) {
                        dismiss();
                    }
                }
                return true;
            }
        });


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
        //this.setAnimationStyle(R.style.take_photo_anim);
    }
}
