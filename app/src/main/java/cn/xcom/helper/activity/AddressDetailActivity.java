package cn.xcom.helper.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.RelativeLayout;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xcom.helper.R;

public class AddressDetailActivity extends BaseActivity {

    @BindView(R.id.rl_help_me_back)
    RelativeLayout rlHelpMeBack;
    @BindView(R.id.mapview)
    MapView mapview;
    private Context context;
    private double lon,lat;
    BaiduMap mBaiduMap = null;
    MyLocationData locData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_address_detail);
        ButterKnife.bind(this);
        context = this;
        mBaiduMap = mapview.getMap();
        getData();
    }

    /**
     * 接受从上以页面传来的经纬度并定位
     */
    private void getData() {
        lon = Double.parseDouble(getIntent().getStringExtra("lon"));
        lat = Double.parseDouble(getIntent().getStringExtra("lat"));
        LatLng latLng = new LatLng(lat,lon);
        mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_dingwei_shou)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng).zoom(18.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    @OnClick(R.id.rl_help_me_back)
    public void onClick() {
        finish();
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
        super.onDestroy();
    }
}
