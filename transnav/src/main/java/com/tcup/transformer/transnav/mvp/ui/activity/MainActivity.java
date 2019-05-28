package com.tcup.transformer.transnav.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.navi.model.NaviLatLng;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.model.ColumnsValue;
import com.litesuits.orm.db.model.ConflictAlgorithm;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tcup.transformer.transnav.R;
import com.tcup.transformer.transnav.app.EventBusTags;
import com.tcup.transformer.transnav.bean.MarketBean;
import com.tcup.transformer.transnav.di.component.DaggerMainComponent;
import com.tcup.transformer.transnav.map.overlay.WindowAdapter;
import com.tcup.transformer.transnav.map.util.ORMUtil;
import com.tcup.transformer.transnav.mvp.contract.MainContract;
import com.tcup.transformer.transnav.mvp.model.entity.SiteListBean;
import com.tcup.transformer.transnav.mvp.presenter.MainPresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/09/2019 09:16
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View, View.OnClickListener, AMap.OnMarkerClickListener,
        AMap.OnInfoWindowClickListener {
    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.nameMain)
    TextView nameMain;
    @BindView(R.id.typeMain)
    TextView typeMain;
    @BindView(R.id.lanlatMain)
    TextView consumeNumMain;
    @BindView(R.id.contentMain)
    TextView lanLatMain;
    @BindView(R.id.bottomMain)
    LinearLayout bottomMain;
    @BindView(R.id.bottomNav)
    LinearLayout bottomNav;
    @Inject
    RxPermissions mRxPermissions;
    AMap aMap = null;
    private UiSettings mUiSettings;
    private MyLocationStyle myLocationStyle;
    private SiteListBean siteListBean;


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMainComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        bottomNav.setOnClickListener(this);
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
    public Activity getActivity() {
        return this;
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
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
    public void initMap() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            mUiSettings = aMap.getUiSettings();
        }
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
//        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.strokeColor(Color.TRANSPARENT);//设置定位蓝点精度圆圈的边框颜色
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);//设置定位蓝点精度圆圈的填充颜色
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        // 绑定marker拖拽事件
        aMap.setOnMarkerDragListener(markerDragListener);
        mUiSettings.setScaleControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottomNav:
                String[] re = lanLatMain.getText().toString().split(":");
                String[] strings = re[1].split(",");
                NaviLatLng startNav = new NaviLatLng(aMap.getMyLocation().getLatitude(), aMap.getMyLocation().getLongitude());
                NaviLatLng endNav = new NaviLatLng(Double.valueOf(strings[1]), Double.valueOf(strings[0]));
                Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
                intent.putExtra("startNav", startNav);
                intent.putExtra("endNav", endNav);
                ArmsUtils.startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        aMap.setMyLocationStyle(myLocationStyle);
        String name = marker.getTitle();
        if (name == null || "".equals(name)) {
            return false;
        }
        jumpPoint(marker);
        if (!marker.getTitle().equals(siteListBean.getSiteAddr()) || siteListBean.getSiteLat() == null) {
            return false;
        }
        if (bottomMain.getVisibility() == View.GONE) {
            bottomMain.setVisibility(View.VISIBLE);
            bottomNav.setVisibility(View.VISIBLE);
        }
        nameMain.setText("变压器信息");
        typeMain.setText("位置名称:" + siteListBean.getSiteAddr());
        lanLatMain.setText("经纬度信息:" + siteListBean.getSiteLng() + "," + siteListBean.getSiteLat());
        consumeNumMain.setText("位置描述:" + siteListBean.getSiteRemark());
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(Double.valueOf(siteListBean.getSiteLat()), Double.valueOf(siteListBean.getSiteLng())), 17, 0, 0)));
        return false;
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
            String name = arg0.getTitle();
            ArrayList<MarketBean> query = ORMUtil.getLiteOrm(MainActivity.this).query(new QueryBuilder<MarketBean>(MarketBean.class)
                    .whereEquals("address", name)
            );
            ColumnsValue cv = new ColumnsValue(new String[]{"latitude", "longitude"}, new Object[]{arg0.getPosition().latitude, arg0.getPosition().longitude});
            ORMUtil.getLiteOrm(MainActivity.this).update(query.get(0), cv, ConflictAlgorithm.None);
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
//        if (aMap != null) {
//            aMap.clear();
//        }
        if (bottomMain.getVisibility() == View.VISIBLE) {
            bottomMain.setVisibility(View.GONE);
            bottomNav.setVisibility(View.GONE);
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_list:
                ArmsUtils.startActivity(new Intent(MainActivity.this, LocationListActivity.class));
                return true;
            case R.id.action_research:
                Toast.makeText(this, "Add Contact option menu clicked!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_luru:
                ArmsUtils.startActivity(new Intent(MainActivity.this, PickListActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initMark() {
        ArrayList<MarketBean> marketBeans = ORMUtil.getLiteOrm(MainActivity.this).query(new QueryBuilder<MarketBean>(MarketBean.class)
                .appendOrderDescBy("createTime"));
        if (marketBeans.size() == 0) {
            String[][] dataList = new String[][]{
                    {"科技园站", "", "华艺科技园东20米左右", "31.850628", "117.209127"},
                    {"科技园站", "", "华艺科技园东20米左右", "31.850522", "117.209427"},
                    {"科技园站", "", "华艺科技园东20米左右", "31.850028", "117.209327"},
                    {"科技园站", "", "华艺科技园东20米左右", "31.850928", "117.219127"},
            };

            for (String[] data : dataList) {
                MarketBean marketBean = new MarketBean();
                marketBean.setLatitude(Double.valueOf(data[3]));
                marketBean.setLongitude(Double.valueOf(data[4]));

                marketBean.setTitle(data[0]);
                marketBean.setAddress(data[0]);
                marketBean.setContent(data[2]);
                marketBean.setCreateTime(new Date());
                ORMUtil.getLiteOrm(MainActivity.this).save(marketBean);
                marketBeans.add(marketBean);
            }
        }

        for (MarketBean marketBean : marketBeans) {
            if (aMap == null) {
                aMap = mMapView.getMap();
                mUiSettings = aMap.getUiSettings();
            }
            aMap.addMarker(new MarkerOptions()
                    .position(new LatLng(marketBean.getLatitude(),//设置纬度
                            marketBean.getLongitude()))//设置经度
                    .title(marketBean.getAddress())//设置标题
                    .snippet(marketBean.getContent())//设置内容
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSiteListBean(SiteListBean siteListBean) {
        this.siteListBean = siteListBean;
        if (siteListBean.getSiteLat() != null && siteListBean.getSiteLng() != null) {
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
            aMap.setMyLocationStyle(myLocationStyle);
            if (aMap == null) {
                aMap = mMapView.getMap();
                mUiSettings = aMap.getUiSettings();
                aMap.clear();
            }
            aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(Double.valueOf(siteListBean.getSiteLat()), Double.valueOf(siteListBean.getSiteLng())), 13, 0, 0)));
            aMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.valueOf(siteListBean.getSiteLat()),//设置纬度
                            Double.valueOf(siteListBean.getSiteLng())))//设置经度
                    .title(siteListBean.getSiteAddr())//设置标题
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
    }

    /**
     * marker点击时跳动一下
     */
    public void jumpPoint(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = aMap.getProjection();
        final LatLng markerLatlng = marker.getPosition();
        Point markerPoint = proj.toScreenLocation(markerLatlng);
        markerPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(markerPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * markerLatlng.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * markerLatlng.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }
}
