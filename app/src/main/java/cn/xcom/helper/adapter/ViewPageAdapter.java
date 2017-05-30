package cn.xcom.helper.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.activity.SpaceImageDetailActivity;
import cn.xcom.helper.utils.SaveImageUtils;
import cn.xcom.helper.utils.ToastUtil;

/**
 * Created by zhuchongkun on 16/6/16.
 */
public class ViewPageAdapter extends PagerAdapter {
	private List<ImageView> addList;//存放数据
	Context context;
	public ViewPageAdapter(List<ImageView> addList,Context context) {
		this.addList = addList;
		this.context = context;
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
		addList.get(position).setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setItems(new String[]{context.getResources().getString(R.string.save_picture)}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						addList.get(position).setDrawingCacheEnabled(true);
						Bitmap imageBitmap = addList.get(position).getDrawingCache();
						if (imageBitmap != null) {
							new SaveImageUtils(context, addList.get(position)).execute(imageBitmap);
						}
					}
				});
				builder.show();
				return true;
			}
		});
		return addList.get(position);
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(addList.get(position));
	}
}
