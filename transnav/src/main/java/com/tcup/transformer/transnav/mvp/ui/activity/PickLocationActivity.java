package com.tcup.transformer.transnav.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tcup.transformer.transnav.R;
import com.tcup.transformer.transnav.app.EventBusTags;
import com.tcup.transformer.transnav.di.component.DaggerPickLocationComponent;
import com.tcup.transformer.transnav.map.overlay.WindowAdapter;
import com.tcup.transformer.transnav.mvp.contract.PickLocationContract;
import com.tcup.transformer.transnav.mvp.model.entity.SiteListBean;
import com.tcup.transformer.transnav.mvp.presenter.PickLocationPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/24/2019 15:50
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class PickLocationActivity extends BaseActivity<PickLocationPresenter> implements PickLocationContract.View, View.OnClickListener, AMap.OnMarkerClickListener,
        AMap.OnInfoWindowClickListener, AMap.OnMyLocationChangeListener {
    @BindView(R.id.toolbar_pick_title_head)
    TextView mTitle;
    @BindView(R.id.toolbar_pick_back_head)
    RelativeLayout mBack;
    @BindView(R.id.submit_pick_btn)
    Button submitBtn;
    @BindView(R.id.pic_map)
    MapView mMapView;
    @Inject
    RxPermissions mRxPermissions;
    private UiSettings mUiSettings;
    private MyLocationStyle myLocationStyle;
    AMap aMap = null;
    private SiteListBean siteListBean;
    String lonlat;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPickLocationComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_pick_location; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
        mTitle.setText("地图选点");
        mBack.setOnClickListener(this::onClick);
        submitBtn.setOnClickListener(this::onClick);
        lonlat = getIntent().getStringExtra("lonlat");
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_pick_back_head:
                killMyself();
                break;
            case R.id.submit_pick_btn:
                if (siteListBean != null) {
                    EventBus.getDefault().postSticky(siteListBean);
                }
                aMap.clear(true);
                killMyself();
                break;
            default:
                break;
        }
    }

    public void addMark(String lanlat) {
        double lat;
        double lon;
        if (siteListBean != null && siteListBean.getSiteLat() != null && siteListBean.getSiteLng() != null) {
            lat = Double.valueOf(siteListBean.getSiteLat());
            lon = Double.valueOf(siteListBean.getSiteLng());
        } else {
            siteListBean = new SiteListBean();
            lat = Double.valueOf(lanlat.split(",")[1]);
            lon = Double.valueOf(lanlat.split(",")[0]);
        }
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(lat, lon), 17, 0, 0)));
        aMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))//设置经度
//                    .snippet(marketBean.getSiteRemark())//设置内容
                .setFlat(true) // 将Marker设置为贴地显示，可以双指下拉地图查看效果
                .draggable(true) //设置Marker可拖动
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.flag))));
        //设置自定义弹窗
        aMap.setInfoWindowAdapter(new WindowAdapter(this));
        //绑定信息窗点击事件
        aMap.setOnInfoWindowClickListener(this);
        aMap.setOnMarkerClickListener(this);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public void initMap() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            mUiSettings = aMap.getUiSettings();
        }
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        myLocationStyle.strokeColor(Color.TRANSPARENT);//设置定位蓝点精度圆圈的边框颜色
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);//设置定位蓝点精度圆圈的填充颜色
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        // 绑定marker拖拽事件
        aMap.setOnMarkerDragListener(markerDragListener);
        aMap.setOnMyLocationChangeListener(this);
        mUiSettings.setScaleControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        addMark(lonlat);
    }

    // 定义 Marker拖拽的监听
    AMap.OnMarkerDragListener markerDragListener = new AMap.OnMarkerDragListener() {

        // 当marker开始被拖动时回调此方法, 这个marker的位置可以通过getPosition()方法返回。
        // 这个位置可能与拖动的之前的marker位置不一样。
        // marker 被拖动的marker对象。
        @Override
        public void onMarkerDragStart(Marker arg0) {
            // TODO Auto-generated method stub

        }

        // 在marker拖动完成后回调此方法, 这个marker的位置可以通过getPosition()方法返回。
        // 这个位置可能与拖动的之前的marker位置不一样。
        // marker 被拖动的marker对象。
        @Override
        public void onMarkerDragEnd(Marker arg0) {
            siteListBean.setSiteLat(String.valueOf(arg0.getPosition().latitude));
            siteListBean.setSiteLng(String.valueOf(arg0.getPosition().longitude));
        }

        // 在marker拖动过程中回调此方法, 这个marker的位置可以通过getPosition()方法返回。
        // 这个位置可能与拖动的之前的marker位置不一样。
        // marker 被拖动的marker对象。
        @Override
        public void onMarkerDrag(Marker arg0) {
            // TODO Auto-generated method stub

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
//        initMark();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mRxPermissions = null;
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        siteListBean.setSiteLat(String.valueOf(marker.getPosition().latitude));
        siteListBean.setSiteLng(String.valueOf(marker.getPosition().longitude));
        return false;
    }

    @Override
    public void onMyLocationChange(Location location) {
//        addMark(location);
    }
}
