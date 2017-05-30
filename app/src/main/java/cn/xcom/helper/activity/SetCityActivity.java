package cn.xcom.helper.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

import cn.xcom.helper.R;
import cn.xcom.helper.wheel.CityModel;
import cn.xcom.helper.wheel.DistrictModel;
import cn.xcom.helper.wheel.ProvinceModel;
import cn.xcom.helper.wheel.WheelBaseActivity;
import cn.xcom.helper.wheel.widget.OnWheelChangedListener;
import cn.xcom.helper.wheel.widget.WheelView;
import cn.xcom.helper.wheel.widget.adapter.ArrayWheelAdapter;

/**
 * 设置城市
 */
public class SetCityActivity extends WheelBaseActivity implements OnClickListener, OnWheelChangedListener {
	private RelativeLayout rl_back;
	private Button bt_submit;
	private WheelView mViewProvince, mViewCity, mViewDistrict;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_set_city);
		mContext=this;
		initView();
	}

	/**
	 * 初始化并设置监听
	 */
	private void initView() {
		rl_back = (RelativeLayout) findViewById(R.id.rl_set_city_back);
		bt_submit = (Button) findViewById(R.id.bt_set_city_submit);
		mViewProvince = (WheelView) findViewById(R.id.wheel_set_city_province);
		mViewCity = (WheelView) findViewById(R.id.wheel_set_city_city);
		mViewDistrict = (WheelView) findViewById(R.id.wheel_set_city_district);
		setUpListener();
		initDatas();
	}

	private void setUpListener() {
		// 添加change事件
		mViewProvince.addChangingListener(this);
		// 添加change事件
		mViewCity.addChangingListener(this);
		// 添加change事件
		mViewDistrict.addChangingListener(this);
		rl_back.setOnClickListener(this);
		bt_submit.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<ProvinceModel>(mContext, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_set_city_back:
			finish();// 返回上一层
			break;
		case R.id.bt_set_city_submit:
			Intent intent = new Intent();
			intent.putExtra("city", mCurrentProvice.getName()+mCurrentCity.getName()+mCurrentDistrict.getName());
			Log.e("city",mCurrentProvice.getName());
			intent.putExtra("cityId", mCurrentDistrict.getId());
			setResult(RESULT_OK, intent);
			finish();
			break;
		}
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
			mCurrentDistrict = mDistrictDatasMap.get(mCurrentCity.getName())[newValue];
		}
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCity = mCitisDatasMap.get(mCurrentProvice.getName())[pCurrent];
		DistrictModel[] areas = mDistrictDatasMap.get(mCurrentCity.getName());

		if (areas == null) {

			areas = new DistrictModel[] {};
		}
		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<DistrictModel>(this, areas));
		mViewDistrict.setCurrentItem(0);
		if (areas.length>0) {
			mCurrentDistrict = areas[0];
		}else{
			mCurrentDistrict=new DistrictModel();
		}
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProvice = mProvinceDatas[pCurrent];
		CityModel[] cities = mCitisDatasMap.get(mCurrentProvice.getName());
		if (cities == null) {
			cities = new CityModel[] {};
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<CityModel>(this, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}
}
