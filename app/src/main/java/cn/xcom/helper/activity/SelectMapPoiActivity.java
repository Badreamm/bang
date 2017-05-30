package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.bean.PoiInformaiton;
import cn.xcom.helper.utils.CommonAdapter;
import cn.xcom.helper.utils.ViewHolder;

public class SelectMapPoiActivity extends BaseActivity implements OnGetGeoCoderResultListener {

    @BindView(R.id.rl_help_me_back)
    RelativeLayout rlHelpMeBack;
    @BindView(R.id.mapview)
    MapView mapview;
    @BindView(R.id.lv_address)
    ListView lvAddress;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_search_clear)
    ImageView ivSearchClear;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.listview_search_result)
    ListView listviewSearchResult;
    private Context context;

    private double lat;
    private double lon;
    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private LatLng mLat;
    BaiduMap mBaiduMap = null;

    private List<PoiInformaiton> poiInformaitons;
    private CommonAdapter<PoiInformaiton> adapter;

    private List<SuggestionResult.SuggestionInfo> suggestionInfos;
    private CommonAdapter<SuggestionResult.SuggestionInfo> suggestionInfoCommonAdapter;
    private LatLng currentPt;
    private Marker marker;
    private boolean isFisrtIn = true;
    private SuggestionSearch mSuggestionSearch;
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select_map_poi);
        ButterKnife.bind(this);
        context = this;
        poiInformaitons = new ArrayList<>();
        suggestionInfos = new ArrayList<>();
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(listener);
        mBaiduMap = mapview.getMap();
        initEvent();
        initListener();
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        getData();
    }

    /**
     * 从上一页面接受经纬度
     */
    private void getData() {
        lat = getIntent().getDoubleExtra("lat", 0);
        lon = getIntent().getDoubleExtra("lon", 0);
        Log.e("hello", lat + lon + "");
        mLat = new LatLng(lat, lon);
        // 反Geo搜索
        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(mLat));
    }


    /**
     * 对地图事件的消息响应
     */
    private void initListener() {
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                mLat = mBaiduMap.getMapStatus().target;
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(mLat));
                Log.e("中心点", mLat.latitude + "");
                if (marker != null) {
                    marker.remove();
                }
                mBaiduMap.clear();
            }
        });
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("siteName", poiInformaitons.get(position).getName());
                intent.putExtra("siteLat", poiInformaitons.get(position).getLat());
                intent.putExtra("siteLon", poiInformaitons.get(position).getLon());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = etSearch.getText().toString();
                Log.e("keyword", keyword);
                if (TextUtils.isEmpty(keyword)) {
                    ivSearchClear.setVisibility(View.GONE);
                    listviewSearchResult.setVisibility(View.GONE);
                } else {
                    ivSearchClear.setVisibility(View.VISIBLE);
                    mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                            .keyword(keyword).city(HelperApplication.getInstance().mDistrict));
                    /*List<City> result = searchCity(mAllCities, keyword);
                    if (result == null || result.size() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        mResultAdapter = new ResultListAdapter(CityPickerActivity.this, result);
                        mResultListView.setAdapter(mResultAdapter);
                        // mResultAdapter.changeData(result);
                    }*/
                }
            }
        });
    }


    OnGetSuggestionResultListener listener = new OnGetSuggestionResultListener() {
        public void onGetSuggestionResult(SuggestionResult res) {
            if (res == null || res.getAllSuggestions() == null
                    || res.getAllSuggestions().size() == 0) {
                return;
                //未找到相关结果
            }
            suggestionInfos = res.getAllSuggestions();
            listviewSearchResult.setVisibility(View.VISIBLE);
            suggestionInfoCommonAdapter = new CommonAdapter<SuggestionResult.SuggestionInfo>(context, suggestionInfos, R.layout.item_search_info) {
                @Override
                public void convert(ViewHolder holder, SuggestionResult.SuggestionInfo suggestionInfo) {
                    holder.setText(R.id.tv_name, suggestionInfo.key);
                }
            };
            listviewSearchResult.setAdapter(suggestionInfoCommonAdapter);
            listviewSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    flag = 1;
                    etSearch.setText("");
                    ivSearchClear.setVisibility(View.GONE);
                    listviewSearchResult.setVisibility(View.GONE);
                    mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                            .location(suggestionInfos.get(position).pt));
                    mLat = suggestionInfos.get(position).pt;

                }
            });
        }
    };


    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null) {
            return;
        }
        mBaiduMap.clear();
        if (isFisrtIn) {
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(mLat));
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(mLat).zoom(18.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
        mBaiduMap.addOverlay(new MarkerOptions().position(mLat)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_dingwei_shou)));
        try {
            if (result.getPoiList() != null) {
                createMarker(result.getLocation(), result.getPoiList().get(0).name);
                setAdapter(result);
            } else {
                createMarker(result.getLocation(), result.getAddressDetail().city + result.getAddressDetail().district + result.getAddressDetail().street);
            }
        } catch (NullPointerException e) {

        }

    }

    /**
     * 构建坐标点
     *
     * @param mPt
     * @param name
     */
    public void createMarker(final LatLng mPt, String name) {
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.ic_dingwei_shou);
        OverlayOptions options = new MarkerOptions()
                .position(mPt)  //设置marker的位置
                .icon(bitmap)  //设置marker图标
                .zIndex(9)  //设置marker所在层级
                .draggable(true);  //设置手势拖拽
        //将marker添加到地图上
        marker = (Marker) (mBaiduMap.addOverlay(options));
        //创建InfoWindow展示的view
        Button button = new Button(context);
        button.setText(name);
        //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
        InfoWindow mInfoWindow = new InfoWindow(button, mPt, -47);
        //显示InfoWindow
        mBaiduMap.showInfoWindow(mInfoWindow);
    }

    /**
     * 为周边设置信息
     *
     * @param result
     */
    private void setAdapter(ReverseGeoCodeResult result) {
        poiInformaitons.clear();
        List<PoiInfo> poiInfos = result.getPoiList();
        for (int i = 0; i < poiInfos.size(); i++) {
            PoiInformaiton poiInformaiton = new PoiInformaiton();
            poiInformaiton.setName(poiInfos.get(i).name);
            poiInformaiton.setAddress(poiInfos.get(i).address);
            poiInformaiton.setLat(poiInfos.get(i).location.latitude);
            poiInformaiton.setLon(poiInfos.get(i).location.longitude);
            poiInformaitons.add(poiInformaiton);
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            adapter = new CommonAdapter<PoiInformaiton>(context, poiInformaitons, R.layout.item_poi_info) {
                @Override
                public void convert(ViewHolder holder, PoiInformaiton poiInformaiton) {
                    holder.setText(R.id.tv_name, poiInformaiton.getName());
                    holder.setText(R.id.tv_address, poiInformaiton.getAddress());
                }
            };
        }
        lvAddress.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        mapview.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mapview.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mapview.onDestroy();
        mSuggestionSearch.destroy();
        super.onDestroy();
    }

    @OnClick({R.id.rl_help_me_back, R.id.iv_search_clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_help_me_back:
                finish();
                break;
            case R.id.iv_search_clear:
                etSearch.setText("");
                ivSearchClear.setVisibility(View.GONE);
                listviewSearchResult.setVisibility(View.GONE);
                break;
        }
    }
}
