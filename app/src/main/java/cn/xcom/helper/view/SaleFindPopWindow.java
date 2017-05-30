package cn.xcom.helper.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class SaleFindPopWindow extends PopupWindow {
    private Context mContext;
    private View view;
    public ListView lv_find;
//    public FindAdapter findAdapter;
    public TextView tv_find;
    public EditText et_find;
//    public List<History> addAllList=new ArrayList<>();

    public SaleFindPopWindow(Context context, AdapterView.OnItemClickListener itemClickListener, View.OnClickListener onClickListener, TextView.OnEditorActionListener editorActionListener) {
        this.mContext = context;
        this.view = LayoutInflater.from(context).inflate(R.layout.find_list,null);
        lv_find = (ListView) view.findViewById(R.id.list_find);
        tv_find = (TextView) view.findViewById(R.id.tv_find);
        et_find = (EditText) view.findViewById(R.id.search_edt);
        lv_find.setOnItemClickListener(itemClickListener);
        tv_find.setOnClickListener(onClickListener);
        et_find.setOnEditorActionListener(editorActionListener);
//        History history = new History();
//
//        addAllList.add(history);
//        findAdapter = new FindAdapter(mContext, addAllList);
//        lv_find.setAdapter(findAdapter);
//        findAdapter.notifyDataSetChanged();

        // 设置外部可点击
        this.setOutsideTouchable(true);
//


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


