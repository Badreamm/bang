package cn.xcom.helper.wheel;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.xcom.helper.HelperApplication;

public class WheelBaseActivity extends Activity {
	/**
	 * 所有省
	 */
	protected ProvinceModel[] mProvinceDatas;
	/**
	 * key - 省 value - 市
	 */
	protected Map<String, CityModel[]> mCitisDatasMap = new HashMap<String, CityModel[]>();
	 /**
	 * key - 市 values - 区
	 */
	protected Map<String, DistrictModel[]> mDistrictDatasMap = new HashMap<String, DistrictModel[]>();
	/**
	 * 当前省的名称
	 */
	protected ProvinceModel mCurrentProvice=new ProvinceModel();
	/**
	 * 当前市的名称
	 */
	protected CityModel mCurrentCity=new CityModel();
	/**
	 * 当前区的名称
	 */
	protected DistrictModel mCurrentDistrict=new DistrictModel();

	/**
	 * 解析整个Json对象，完成后释放Json对象的内存
	 */
	protected void initDatas() {
		try {
			JSONArray jsonArray = HelperApplication.mCityJson.getJSONArray("citylist");
			mCurrentProvice.setName(jsonArray.getJSONObject(0).getString("text").trim());
			mCurrentProvice.setId(jsonArray.getJSONObject(0).getString("region_id").trim());
			mCurrentCity.setName(jsonArray.getJSONObject(0).getJSONArray("children").getJSONObject(0).getString("text").trim());
			mCurrentCity.setId(jsonArray.getJSONObject(0).getJSONArray("children").getJSONObject(0).getString("region_id").trim());
			mCurrentDistrict.setName(jsonArray.getJSONObject(0).getJSONArray("children").getJSONObject(0).getJSONArray("children").getJSONObject(0).getString("text").trim());
			mCurrentDistrict.setId(jsonArray.getJSONObject(0).getJSONArray("children").getJSONObject(0).getJSONArray("children").getJSONObject(0).getString("region_id").trim());
			mProvinceDatas = new ProvinceModel[jsonArray.length()];
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonP = jsonArray.getJSONObject(i);// 每个省的json对象
				ProvinceModel provinceModel=new ProvinceModel();
				provinceModel.setName(jsonP.getString("text").trim());
				provinceModel.setId(jsonP.getString("region_id").trim());
				mProvinceDatas[i] = provinceModel;
				JSONArray jsonCs = null;
				try {
					/**
					 * Throws JSONException if the mapping doesn't exist or is
					 * not a JSONArray.
					 */
					jsonCs = jsonP.getJSONArray("children");
				} catch (Exception e1) {
					continue;
				}
				CityModel[] mCitiesDatas = new CityModel[jsonCs.length()];
				for (int j = 0; j < jsonCs.length(); j++) {
					JSONObject jsonCity = jsonCs.getJSONObject(j);
					CityModel cityModel=new CityModel();
					cityModel.setName(jsonCity.getString("text").trim());
					cityModel.setId(jsonCity.getString("region_id").trim());
					mCitiesDatas[j] = cityModel;
					JSONArray jsonAreas = null;
					try {
						/**
						 * Throws JSONException if the mapping doesn't exist or
						 * is not a JSONArray.
						 */
						jsonAreas = jsonCity.getJSONArray("children");
					} catch (Exception e) {
						continue;
					}
					DistrictModel[] mAreasDatas = new DistrictModel[jsonAreas.length()];// 当前市的所有区
					for (int k = 0; k < jsonAreas.length(); k++) {
						JSONObject jsonD = jsonAreas.getJSONObject(k);// 每个省的json对象
						DistrictModel districtModel=new DistrictModel();
						districtModel.setName(jsonD.getString("text").trim());
						districtModel.setId(jsonD.getString("region_id").trim());
						mAreasDatas[k] = districtModel;
					}
					mDistrictDatasMap.put(cityModel.getName(), mAreasDatas);
				}
				mCitisDatasMap.put(provinceModel.getName(), mCitiesDatas);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
