package cn.xcom.helper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.activity.ImageDetailActivity;

/**
 * Created by zhuchongkun on 16/6/16.
 */
public class MyViewPageAdapter extends PagerAdapter {
	private List<ImageView> addList;//存放数据
	private Context mContext;
	private ArrayList<String> mWorkImgs;

	public MyViewPageAdapter(ArrayList<String> workImgs, List<ImageView> addList, Context context) {
		this.addList = addList;
		this.mContext = context;
		this.mWorkImgs = workImgs;

	}

	@Override
	public int getCount() {
		return addList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view==object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		container.addView( addList.get(position));
		addList.get(position).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, ImageDetailActivity.class);
				intent.putStringArrayListExtra("Imgs", mWorkImgs);
				intent.putExtra("position",position);
				intent.putExtra("type", "4");
				mContext.startActivity(intent);
			}
		});
		return addList.get(position);
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(addList.get(position));
	}
}
