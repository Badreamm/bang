package cn.xcom.helper.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.adapter.CityListAdapter;
import cn.xcom.helper.adapter.ResultListAdapter;
import cn.xcom.helper.bean.City;
import cn.xcom.helper.bean.LocateState;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.db.DBManager;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.Pinyin4jUtils;
import cn.xcom.helper.utils.ToastUtils;
import cn.xcom.helper.view.SideLetterBar;
import cz.msebera.android.httpclient.Header;


/**
 * Created by zhuchongkun.
 */
public class CityPickerActivity extends BaseActivity implements View.OnClickListener, OnGetGeoCoderResultListener {
    public static final int REQUEST_CODE_PICK_CITY = 2333;
    public static final String KEY_PICKED_CITY = "picked_city";

    private ListView mListView;
    private ListView mResultListView;
    private SideLetterBar mLetterBar;
    private EditText searchBox;
    private ImageView clearBtn;
    private ImageView backBtn;
    private ViewGroup emptyView;

    private CityListAdapter mCityAdapter;
    private ResultListAdapter mResultAdapter;
    private List<City> mAllCities;
    private DBManager dbManager;

    //    private AMapLocationClient mLocationClient;
    //定位相关
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private String mLocaddress;
    private double mLocLat,mLocLon;
    private int type; //1是定位，2是切换城市
    private KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        mAllCities = new ArrayList<>();
        hud = KProgressHUD.create(CityPickerActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
        hud.show();
        initView();
    }
    /**
     * 初始化定位参数
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }



    private void initData(JSONObject response) {
        mAllCities.clear();
        initCity(response);
        mCityAdapter = new CityListAdapter(this, mAllCities);
        mListView.setAdapter(mCityAdapter);
        mCityAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(String name) {
                Log.e("name", name);
                back(name);
                type = 2;
                if(mLocaddress.equals(name)){
                    HelperApplication.getInstance().mCurrentLocLat = mLocLat;
                    HelperApplication.getInstance().mCurrentLocLon = mLocLon;
                    HelperApplication.getInstance().status = "1";
                    mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                            .location(new LatLng(mLocLat, mLocLon)));
                    Log.e("定位的经纬度", mLocLat + "," + mLocLon);
                    /*setResult(RESULT_OK);
                    finish();*/
                }else{
                    City city = new City();
                    for(int i=0;i<mAllCities.size();i++){
                        if(mAllCities.get(i).getName().equals(name)){
                            city = mAllCities.get(i);
                        }
                    }
                    HelperApplication.getInstance().mCurrentLocLat = Double.parseDouble(city.getLatitude());
                    HelperApplication.getInstance().mCurrentLocLon = Double.parseDouble(city.getLongitude());
                    HelperApplication.getInstance().status = city.getStatus();
                    Log.e("result",HelperApplication.getInstance().status);
                    mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                            .location(new LatLng(Double.parseDouble(city.getLatitude()), Double.parseDouble(city.getLongitude()))));
//                    setResult(RESULT_OK);
//                    finish();
                }
            }

            @Override
            public void onLocateClick() {
                Log.e("onLocateClick", "重新定位...");
                mCityAdapter.updateLocateState(LocateState.LOCATING, null);
            }
        });

    }

    /**
     * 解析城市列表
     * @param response
     */
    private void initCity(JSONObject response) {
        JSONArray array = response.optJSONArray("data");
        /*City city = new City();
        city.setName(array.optJSONObject(0).optString("name"));
        city.setPinyin(Pinyin4jUtils.getPingYin(array.optJSONObject(0).optString("name")));
        city.setLatitude(array.optJSONObject(0).optString("latitude"));
        city.setLongitude(array.optJSONObject(0).optString("longitude"));
        city.setStatus(array.optJSONObject(0).optString("status"));
        mAllCities.add(city);*/
        for(int i=1;i<array.length();i++){
            JSONArray cityArray = array.optJSONArray(i);
            if (cityArray == null){
                continue;
            }
            for(int j = 0; j<cityArray.length();j++){
                JSONObject object = cityArray.optJSONObject(j);
                City city1 = new City();
                city1.setName(object.optString("name"));
                city1.setLongitude(object.optString("latitude"));
                city1.setLatitude(object.optString("longitude"));
                city1.setPinyin(Pinyin4jUtils.getPingYin(object.optString("name")));
                city1.setStatus(object.optString("status"));
                mAllCities.add(city1);
            }
        }
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listview_all_city);
        TextView overlay = (TextView) findViewById(R.id.tv_letter_overlay);
        mLetterBar = (SideLetterBar) findViewById(R.id.side_letter_bar);
        mLetterBar.setOverlay(overlay);
        mLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                int position = mCityAdapter.getLetterPosition(letter);
                mListView.setSelection(position);
            }
        });

        searchBox = (EditText) findViewById(R.id.et_search);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                if (TextUtils.isEmpty(keyword)) {
                    clearBtn.setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);
                    mResultListView.setVisibility(View.GONE);
                } else {
                    clearBtn.setVisibility(View.VISIBLE);
                    mResultListView.setVisibility(View.VISIBLE);
                    List<City> result = searchCity(mAllCities, keyword);
                    if (result == null || result.size() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                        mResultAdapter = new ResultListAdapter(CityPickerActivity.this, result);
                        mResultListView.setAdapter(mResultAdapter);
                       // mResultAdapter.changeData(result);
                    }
                }
            }
        });

        emptyView = (ViewGroup) findViewById(R.id.empty_view);
        mResultListView = (ListView) findViewById(R.id.listview_search_result);
        mResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                type = 2;
                String name = mResultAdapter.getItem(position).getName();
                City city = new City();
                for(int i=0;i<mAllCities.size();i++){
                    if(mAllCities.get(i).getName().equals(name)){
                        city = mAllCities.get(i);
                    }
                }
                HelperApplication.getInstance().mCurrentLocLat = Double.parseDouble(city.getLatitude());
                HelperApplication.getInstance().mCurrentLocLon = Double.parseDouble(city.getLongitude());
                HelperApplication.getInstance().status = city.getStatus();
                Log.e("result",HelperApplication.getInstance().status);
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(new LatLng(Double.parseDouble(city.getLatitude()), Double.parseDouble(city.getLongitude()))));
            }
        });

        clearBtn = (ImageView) findViewById(R.id.iv_search_clear);
        backBtn = (ImageView) findViewById(R.id.back);

        clearBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    /**
     * 根据关键字搜索相关市区
     * @param mAllCities
     * @param keyword
     * @return
     */
    private List<City> searchCity(List<City> mAllCities, String keyword) {
        List<City> cities = new ArrayList<>();
        for(int i=0;i<mAllCities.size();i++){
            if(mAllCities.get(i).getName().contains(keyword)){
                cities.add(mAllCities.get(i));
            }
        }
        return cities;
    }

    private void back(String city) {
        ToastUtils.showToast(this, "点击的城市：" + city);
//        Intent data = new Intent();
//        data.putExtra(KEY_PICKED_CITY, city);
//        setResult(RESULT_OK, data);
//        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search_clear:
                searchBox.setText("");
                clearBtn.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
                mResultListView.setVisibility(View.GONE);
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mLocationClient.stopLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCityList();
    }
    private void getCityList() {
        RequestParams params=new RequestParams();
        HelperAsyncHttpClient.get(NetConstant.GET_CITY_LIST, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if(hud!=null){
                    hud.dismiss();
                }
                if (response.optString("status").equals("success")) {
                    initData(response);
                    initLocation();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if(hud!=null){
                    hud.dismiss();
                }
            }
        });
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (type == 1){
            mLocaddress = result.getAddressDetail().province+result.getAddressDetail().city+result.getAddressDetail().district;
            mCityAdapter.updateLocateState(LocateState.SUCCESS, mLocaddress);
        }
        if(type == 2){
            Log.e("result_ok","yes");
            HelperApplication.getInstance().mDistrict = result.getAddressDetail().district;
            setResult(RESULT_OK);
            finish();
        }
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.i("BaiduLocationApiDem", sb.toString());
            type = 1;
            mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(new LatLng(location.getLatitude(), location.getLongitude())));
            mLocLat = location.getLatitude();
            mLocLon = location.getLongitude();
            mLocationClient.stop();
        }
    }
}
