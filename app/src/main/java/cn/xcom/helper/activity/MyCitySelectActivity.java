package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.bean.City1;
import cn.xcom.helper.bean.MyRegion;
import cn.xcom.helper.utils.CityUtils;
import cn.xcom.helper.utils.ToastUtil;


public class MyCitySelectActivity extends BaseActivity implements OnClickListener, OnGetGeoCoderResultListener {

    private Button btn_back, btn_right;
    private ListView lv_city;
    private ArrayList<MyRegion> regions;
    private CityAdapter adapter;
    private static int PROVINCE = 0x00;
    private static int CITY = 0x01;
    private static int DISTRICT = 0x02;
    private CityUtils util;
    private City1 city;
    private TextView tv_location;
    int last, current;
    private String city1, city2, city3;

    //定位相关
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private double mLocLat, mLocLon;
    private String mLocDistict;

    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    //定位市区
    private String mLocaddress;

    //全局详细地址
    private String mLocateAddress;


    private KProgressHUD hud, locatehud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_activity_city);//三级联动选择页面
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        viewInit();
        hud = KProgressHUD.create(MyCitySelectActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
        hud.show();
        initLocation();
    }


    /**
     * 初始化定位参数
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
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

    /*
     * 初始化
     */
    private void viewInit() {
        tv_location = (TextView) findViewById(R.id.tv_location);
        //点击定位的城市
        tv_location.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperApplication.getInstance().mCurrentLocLat = mLocLat;
                HelperApplication.getInstance().mCurrentLocLon = mLocLon;
                HelperApplication.getInstance().mCurrentAddress = mLocateAddress;
                HelperApplication.getInstance().status = "1";
                Log.e("result_ok", "yes");
                HelperApplication.getInstance().mDistrict = mLocDistict;
                setResult(RESULT_OK);
                finish();
            }
        });
        city = new City1();
        Intent in = getIntent();
        city = in.getParcelableExtra("city");


        if (city == null) {
            city = new City1();
            city.setProvince("");
            city.setCity("");
            city.setDistrict("");
        } else {
            city = new City1();
            city.setProvince("");
            city.setCity("");
            city.setDistrict("");

        }
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_right = (Button) findViewById(R.id.btn_right);
        btn_right.setText("确认");
        btn_right.setVisibility(View.GONE);

        findViewById(R.id.scrollview).setVisibility(View.GONE);

        util = new CityUtils(this, hand);
        util.initProvince();
        lv_city = (ListView) findViewById(R.id.lv_city);

        regions = new ArrayList<>();
        adapter = new CityAdapter(this);
        lv_city.setAdapter(adapter);

    }

    protected void onStart() {
        super.onStart();
        lv_city.setOnItemClickListener(onItemClickListener);
        btn_back.setOnClickListener(this);
        btn_right.setOnClickListener(this);
    }


    Handler hand = new Handler() {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    System.out.println("省份列表what======" + msg.what);
                    regions = (ArrayList<MyRegion>) msg.obj;
                    adapter.clear();
                    adapter.addAll(regions);
                    adapter.update();
                    break;

                case 2:
                    System.out.println("城市列表what======" + msg.what);
                    regions = (ArrayList<MyRegion>) msg.obj;
                    String name = regions.get(0).getName();
                    /*if(name.equals("北京市")||name.equals("上海市")||name.equals("天津市")||name.equals("重庆市")||name.equals("西安市")){

					}else{
						regions.remove(0);
					}*/
                    adapter.clear();
                    adapter.addAll(regions);
                    adapter.update();
                    break;

                case 3:
                    System.out.println("区/县列表what======" + msg.what);
                    regions = (ArrayList<MyRegion>) msg.obj;
                    regions.remove(0);
                    for (int i = 0; i < regions.size(); i++) {
                        if (regions.get(i).getName().equals("其它区")) {
                            regions.remove(i);
                        }
                    }
                    adapter.clear();
                    adapter.addAll(regions);
                    adapter.update();
                    break;
            }
        }

    };

    OnItemClickListener onItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {

            last = current;

            if (current == PROVINCE) {
                String newProvince = regions.get(arg2).getName();
                city1 = newProvince;
                if (!newProvince.equals(city.getProvince())) {
                    city.setProvince(newProvince);

                    city.setRegionId(regions.get(arg2).getId());
                    city.setProvinceCode(regions.get(arg2).getId());
                    city.setCityCode("");
                    city.setDistrictCode("");

                }

                current = 1;
                //点击省份列表中的省份就初始化城市列表
                util.initCities(city.getProvinceCode());
            } else if (current == CITY) {
                String newCity = regions.get(arg2).getName();
                city2 = newCity;
                if (!newCity.equals(city.getCity())) {
                    city.setCity(newCity);
                    city.setRegionId(regions.get(arg2).getId());
                    city.setCityCode(regions.get(arg2).getId());
                    city.setDistrictCode("");
                }
                util.initDistricts(city.getCityCode());
                current = 2;

            } else if (current == DISTRICT) {
                current = 3;
                city.setDistrictCode(regions.get(arg2).getId());
                city.setRegionId(regions.get(arg2).getId());
                city.setDistrict(regions.get(arg2).getName());
                city3 = regions.get(arg2).getName();
                submit();
                /*Intent in = new Intent();
                in.putExtra("city", city);
				setResult(8,in);
				finish();*/
            }

        }
    };

    /**
     * 地理编码
     */
    private void submit() {
        locatehud = KProgressHUD.create(MyCitySelectActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
        locatehud.show();
        Log.e("city", city1 + city2 + city3);
        mSearch.geocode(new GeoCodeOption().city(
                city1).address(city2 + city3));
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            return;
        }
        if (locatehud != null) {
            locatehud.dismiss();
        }
        Log.e("city", result.getLocation().toString());
        HelperApplication.getInstance().mCurrentLocLat = result.getLocation().latitude;
        HelperApplication.getInstance().mCurrentLocLon = result.getLocation().longitude;
        HelperApplication.getInstance().mCurrentAddress = result.getAddress();
        HelperApplication.getInstance().status = "1";
        Log.e("result_ok", "yes");
        HelperApplication.getInstance().mDistrict = city3;
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null) {
            return;
        }
        mLocaddress = result.getAddressDetail().province + result.getAddressDetail().city + result.getAddressDetail().district;
        tv_location.setText(mLocaddress);
        if (result.getPoiList() == null || result.getPoiList().size() == 0) {
            return;
        }
        mLocateAddress = result.getAddressDetail().city + result.getAddressDetail().district + result.getPoiList().get(0).name;
        mLocDistict = result.getAddressDetail().district;
        if (hud != null) {
            hud.dismiss();
        }
    }


    class CityAdapter extends ArrayAdapter<MyRegion> {

        LayoutInflater inflater;

        public CityAdapter(Context con) {
            super(con, 0);
            inflater = LayoutInflater.from(MyCitySelectActivity.this);
        }

        @Override
        public View getView(int arg0, View v, ViewGroup arg2) {
            v = inflater.inflate(R.layout.city_item, null);
            TextView tv_city = (TextView) v.findViewById(R.id.tv_city);
            tv_city.setText(getItem(arg0).getName());
            return v;
        }

        public void update() {
            this.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                if (current == DISTRICT) {
                    current = CITY;
                    util.initCities(city.getProvinceCode());
                } else if (current == CITY) {
                    current = PROVINCE;
                    util.initProvince();
                } else if (current == PROVINCE) {
                    finish();
                }
                break;
            case R.id.btn_right:
                Intent in = new Intent();
                in.putExtra("city", city);
                setResult(8, in);
                finish();
                break;
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
            mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(new LatLng(location.getLatitude(), location.getLongitude())));
            mLocLat = location.getLatitude();
            mLocLon = location.getLongitude();
            mLocationClient.stop();
        }
    }

}
