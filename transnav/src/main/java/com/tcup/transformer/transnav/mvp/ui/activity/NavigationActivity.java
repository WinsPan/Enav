package com.tcup.transformer.transnav.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.enums.PathPlanningStrategy;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.tcup.transformer.transnav.R;
import com.tcup.transformer.transnav.di.component.DaggerNavigationComponent;
import com.tcup.transformer.transnav.map.util.ToastUtil;
import com.tcup.transformer.transnav.mvp.contract.NavigationContract;
import com.tcup.transformer.transnav.mvp.presenter.NavigationPresenter;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.tcup.transformer.transnav.R.id.ll_itemview;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/17/2019 09:17
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class NavigationActivity extends BaseActivity<NavigationPresenter> implements NavigationContract.View, AMapNaviListener, AMapNaviViewListener, View.OnClickListener {

    private static final String TAG = "NavigationActivity";
    @BindView(R.id.toolbar_title_head)
    TextView mTitle;
    @BindView(R.id.toolbar_back_head)
    RelativeLayout mBack;
    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.rl_tv_navistart)
    TextView tvNavi;
    @BindView(R.id.ll_rl_1way)
    RelativeLayout oneWay;
    @BindView(R.id.rl_tv_time)
    TextView tvTime;
    @BindView(R.id.rl_tv_length)
    TextView tvLength;
    @BindView(R.id.rl_rlv_ways)
    RecyclerView mRecyclerView;
    @BindView(R.id.navi_view)
    MapView mapview;
    private int navigationType = 0;
    private AMap amap;
    private CommonAdapter mAdapter;
    private int currentPosition, lastPosition = -1;
    /**************************************************导航相关************************************** ********************/
    private AMapNavi mAMapNavi;
    /**
     * 起点坐标集合[由于需要确定方向，建议设置多个起点]
     */
    private List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
    /**
     * 途径点坐标集合
     */
    private List<NaviLatLng> wayList = new ArrayList<NaviLatLng>();
    /**
     * 终点坐标集合［建议就一个终点］
     */
    private List<NaviLatLng> endList = new ArrayList<NaviLatLng>();
    /**
     * 保存当前算好的路线
     */
    private SparseArray<RouteOverLay> routeOverlays = new SparseArray<RouteOverLay>();
    private List<AMapNaviPath> ways = new ArrayList<>();
    private boolean calculateSuccess;
    private int routeIndex = 0;
    private int zindex = 0;
    int strategy = 0;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerNavigationComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_navigation; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initViews();
        mapview.onCreate(savedInstanceState);
        initMap();
        mTitle.setText("导航");
        mBack.setOnClickListener(this::onClick);
    }


    private void initViews() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = getAdapter();
        mRecyclerView.setAdapter(mAdapter);
        //tab的字体选择器,默认灰色,选择时白色
        mTabLayout.setTabTextColors(Color.LTGRAY, Color.WHITE);
        //设置tab的下划线颜色,默认是粉红色
        mTabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        mTabLayout.addTab(mTabLayout.newTab().setText("驾车"));
        mTabLayout.addTab(mTabLayout.newTab().setText("步行"));
//        mTabLayout.addTab(mTabLayout.newTab().setText("骑车"));
        tvNavi.setOnClickListener(this);
        String startNavStr = getIntent().getStringExtra("startNavStr");
        String endNavStr = getIntent().getStringExtra("endNavStr");
        if (startNavStr == null || endNavStr == null) {
            ToastUtil.showerror(NavigationActivity.this, 1101);
            finish();
            return;
        }
        NaviLatLng startNav = new NaviLatLng(Double.valueOf(startNavStr.split("-")[0]), Double.valueOf(startNavStr.split("-")[1]));
        NaviLatLng endNav = new NaviLatLng(Double.valueOf(endNavStr.split("-")[0]), Double.valueOf(endNavStr.split("-")[1]));
        startList.add(startNav);
        endList.add(endNav);
        //添加Tab点击事件
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabName = tab.getText().toString();
                if (tabName.equals("驾车")) {
                    navigationType = 0;
                } else if (tabName.equals("步行")) {
                    navigationType = 1;
                } else {
                    navigationType = 2;
                }
                clearRoute();
                planRoute();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    /**
     * 初始化AMap对象
     */
    private void initMap() {
        if (amap == null) {
            amap = mapview.getMap();
            //设置显示定位按钮 并且可以点击
            UiSettings settings = amap.getUiSettings();
            MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
//        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
            myLocationStyle.strokeColor(Color.TRANSPARENT);//设置定位蓝点精度圆圈的边框颜色
            myLocationStyle.radiusFillColor(Color.TRANSPARENT);//设置定位蓝点精度圆圈的填充颜色
            amap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
            // 是否显示定位按钮
            settings.setMyLocationButtonEnabled(true);
            amap.setMyLocationEnabled(true);//显示定位层并且可以触发定位,默认是flase
            mAMapNavi = AMapNavi.getInstance(getApplicationContext());
            mAMapNavi.addAMapNaviListener(this);
            amap.moveCamera(CameraUpdateFactory.zoomTo(15));

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_tv_navistart:
                clickNavigation();
                break;
            case R.id.toolbar_back_head:
                killMyself();
                break;
            default:
                break;
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapview.onResume();
        clearRoute();
        planRoute();//路线规划
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        mapview.onResume();
        clearRoute();
        planRoute();//路线规划
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapview.onPause();
        clearRoute();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapview != null) {
            mapview.onDestroy();
        }
        if (mAMapNavi != null) {
            mAMapNavi.destroy();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

    /**************************************************导航相关************************************** ********************/

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteFailure(int i) {
        calculateSuccess = false;
        ArmsUtils.snackbarText("步行路程过长，建议采用其他出行方式");
    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        if (ints.length == 1) {
            onCalculateRouteSuccessOld();
        } else {
            onCalculateMultipleRoutesSuccessOld(ints);
        }
    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {
    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {
        calculateSuccess = false;
    }

    @Override
    public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {

    }

    @Override
    public void onGpsSignalWeak(boolean b) {

    }

    @Override
    public void onNaviSetting() {

    }

    @Override
    public void onNaviCancel() {

    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }

    @Override
    public void onNaviMapMode(int i) {

    }

    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }

    @Override
    public void onScanViewButtonClick() {

    }

    @Override
    public void onLockMap(boolean b) {

    }

    @Override
    public void onNaviViewLoaded() {

    }

    @Override
    public void onMapTypeChanged(int i) {

    }

    @Override
    public void onNaviViewShowMode(int i) {

    }

    /**********************adapter获取***************************/
    private CommonAdapter getAdapter() {

        return new CommonAdapter<AMapNaviPath>(this, R.layout.item_recycleview_naviways, ways) {
            private float maxWidth = 0;
            Handler handler = new Handler();

            /**
             * 初始化Item样式
             */
            private void initItemBackground(ViewHolder holder) {
                holder.getView(ll_itemview).setBackgroundResource(R.drawable.item_naviway_normal_bg);
                TextView tvTitle = holder.getView(R.id.ll_tv_labels);
                TextView tvTime = holder.getView(R.id.ll_tv_time);
                TextView tvLength = holder.getView(R.id.ll_tv_length);
                tvTitle.setTextColor(getResources().getColor(R.color.item_text_title_color));
                tvLength.setTextColor(getResources().getColor(R.color.item_text_title_color));
                tvTime.setTextColor(getResources().getColor(R.color.black));
                tvTitle.setBackgroundResource(R.drawable.item_naviway_title_normal);
            }

            /**
             * 选中的背景色修改
             */
            private void selectedBackground(ViewHolder holder) {
                holder.getView(ll_itemview).setBackgroundResource(R.drawable.item_naviway_selected_bg);
                TextView tvTitle = holder.getView(R.id.ll_tv_labels);
                TextView tvTime = holder.getView(R.id.ll_tv_time);
                TextView tvLength = holder.getView(R.id.ll_tv_length);
                tvTitle.setTextColor(Color.WHITE);
                tvTime.setTextColor(getResources().getColor(R.color.blue));
                tvLength.setTextColor(getResources().getColor(R.color.blue));
                tvTitle.setBackgroundResource(R.drawable.item_naviway_title_selected);
            }

            /**
             * 清除选中的样式
             */
            private void cleanSelector() {
                if (lastPosition != -1) {
                    View view = mRecyclerView.getChildAt(lastPosition);
                    view.setBackgroundResource(R.drawable.item_naviway_normal_bg);
                    TextView tvTitle = (TextView) view.findViewById(R.id.ll_tv_labels);
                    TextView tvTime = (TextView) view.findViewById(R.id.ll_tv_time);
                    TextView tvLength = (TextView) view.findViewById(R.id.ll_tv_length);
                    tvTitle.setTextColor(getResources().getColor(R.color.item_text_title_color));
                    tvLength.setTextColor(getResources().getColor(R.color.item_text_title_color));
                    tvTime.setTextColor(getResources().getColor(R.color.black));
                    tvTitle.setBackgroundResource(R.drawable.item_naviway_title_normal);
                }

            }

            /**
             * 固定宽度文字自适应大小(小屏幕手机换行效果需要)
             * @param textView
             * @param text
             */
            private void reSizeTextView(TextView textView, String text) {

                Paint paint = textView.getPaint();
                float textWidth = paint.measureText(text);
                float textSizeInDp = textView.getTextSize();

                if (textWidth > maxWidth) {
                    for (; textSizeInDp > 0; textSizeInDp -= 1) {
                        textView.setTextSize(textSizeInDp);
                        paint = textView.getPaint();
                        textWidth = paint.measureText(text);
                        if (textWidth <= maxWidth) {
                            break;
                        }
                    }
                }
                textView.invalidate();
                textView.setText(text);
            }

            @Override
            protected void convert(final ViewHolder holder, final AMapNaviPath aMapNaviPath, final int position) {
                final TextView tvTitle = holder.getView(R.id.ll_tv_labels);
                final TextView tvTime = holder.getView(R.id.ll_tv_time);
                final TextView tvLength = holder.getView(R.id.ll_tv_length);
                if (maxWidth == 0) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            maxWidth = tvTitle.getWidth() - tvTitle.getPaddingLeft() - tvTitle.getPaddingRight();
                        }
                    });
                }
                String title = aMapNaviPath.getLabels();
                int len = title.split(",").length;
                if (len >= 3)
                    title = "推荐";

                final String text = title;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        reSizeTextView(tvTitle, text);
                        reSizeTextView(tvTime, getTime(aMapNaviPath.getAllTime()));
                        reSizeTextView(tvLength, getLength(aMapNaviPath.getAllLength()));
                    }
                });


                holder.getView(ll_itemview).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentPosition = position;
                        //已经选中，再次选中直接返回
                        if (lastPosition == currentPosition) {
                            return;
                        } else {
                            //当前的下标值赋值给当前选择的线路下标值
                            routeIndex = position;
                            changeRoute();
                            selectedBackground(holder);
                            cleanSelector();
                        }
                        lastPosition = position;
                    }

                });
                if (position == 0) {
                    currentPosition = position;
                    if (lastPosition == currentPosition) {
                        return;
                    } else {
                        routeIndex = position;
                        changeRoute();
                        selectedBackground(holder);
                        cleanSelector();
                    }
                    lastPosition = position;
                } else {
                    initItemBackground(holder);
                }
            }
        };
    }

    /**
     * 计算路程
     *
     * @param allLength
     * @return
     */
    private String getLength(int allLength) {
        if (allLength > 1000) {
            int remainder = allLength % 1000;
            String m = remainder > 0 ? remainder + "米" : "";
            return allLength / 1000 + "公里" + m;
        } else {
            return allLength + "米";
        }
    }

    /**
     * 计算时间
     *
     * @param allTime
     * @return
     */
    private String getTime(int allTime) {
        if (allTime > 3600) {//1小时以上
            int minute = allTime % 3600;
            String min = minute / 60 != 0 ? minute / 60 + "分钟" : "";
            return allTime / 3600 + "小时" + min;
        } else {
            int minute = allTime % 3600;
            return minute / 60 + "分钟";
        }
    }

    /**************************路线相关*********************************/
    /**
     * 选择路线
     */
    public void changeRoute() {
        if (!calculateSuccess) {
            Toast.makeText(this, "请先算路", Toast.LENGTH_SHORT).show();
            return;
        }
        /**
         * 计算出来的路径只有一条
         */
        if (routeOverlays.size() == 1) {
            //必须告诉AMapNavi 你最后选择的哪条路
            mAMapNavi.selectRouteId(routeOverlays.keyAt(0));
            return;
        }

        if (routeIndex >= routeOverlays.size())
            routeIndex = 0;
        //根据选中的路线下标值得到路线ID
        int routeID = routeOverlays.keyAt(routeIndex);
        //突出选择的那条路
        for (int i = 0; i < routeOverlays.size(); i++) {
            int key = routeOverlays.keyAt(i);
            routeOverlays.get(key).setTransparency(0.4f);
        }
        routeOverlays.get(routeID).setTransparency(1);
        /**把用户选择的那条路的权值弄高，使路线高亮显示的同时，重合路段不会变的透明**/
        routeOverlays.get(routeID).setZindex(zindex++);

        //必须告诉AMapNavi 你最后选择的哪条路
        mAMapNavi.selectRouteId(routeID);
        routeIndex++;

    }

    /**
     * 绘制路线
     *
     * @param routeId
     * @param path
     */
    private void drawRoutes(int routeId, AMapNaviPath path) {
        calculateSuccess = true;
        amap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(startList.get(0).getLatitude(), startList.get(0).getLongitude()), 12, 0, 0)));
        RouteOverLay routeOverLay = new RouteOverLay(amap, path, this);
        routeOverLay.setTrafficLine(false);
        routeOverLay.setTrafficLightsVisible(false);
        routeOverLay.addToMap();
        routeOverlays.put(routeId, routeOverLay);
    }

    /**
     * 清除当前地图上算好的路线
     */
    private void clearRoute() {
        for (int i = 0; i < routeOverlays.size(); i++) {
            RouteOverLay routeOverlay = routeOverlays.valueAt(i);
            routeOverlay.removeFromMap();
        }
        routeOverlays.clear();
        ways.clear();
    }

    /**
     * 路线规划
     */
    private void planRoute() {
        mRecyclerView.setVisibility(View.GONE);//多条路线规划结果
        oneWay.setVisibility(View.GONE);//一条路线规划结果
        if (startList.size() > 0 && endList.size() > 0) {
            if (navigationType == 0) {//驾车
//                try {
//                    /**
//                     * 方法:
//                     *   int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute);
//                     * 参数:
//                     * @congestion 躲避拥堵
//                     * @avoidhightspeed 不走高速
//                     * @cost 避免收费
//                     * @hightspeed 高速优先
//                     * @multipleroute 多路径
//                     *
//                     * 说明:
//                     *      以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
//                     * 注意:
//                     *      不走高速与高速优先不能同时为true
//                     *      高速优先与避免收费不能同时为true
//                     */
//                    strategy = mAMapNavi.strategyConvert(true, false, false, true, true);
//
//                    LogUtils.debugInfo(TAG, "strategy========" + strategy);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                mAMapNavi.calculateDriveRoute(startList, endList, wayList, PathPlanningStrategy.DRIVING_MULTIPLE_ROUTES_DEFAULT);
            } else if (navigationType == 1) {//步行
                mAMapNavi.calculateWalkRoute(startList.get(0), endList.get(0));
            } else {//骑行
                mAMapNavi.calculateRideRoute(startList.get(0), endList.get(0));
            }
        }
    }

    /**
     * 导航按钮点击事件实现方法
     */
    private void clickNavigation() {
        if (startList.size() == 0) {
            ArmsUtils.snackbarText("未获取到当前位置，不能导航");
        } else if (endList.size() == 0) {
            ArmsUtils.snackbarText("未获取到终点，不能导航");
        } else {
            if (!calculateSuccess) {
                ArmsUtils.snackbarText("请先计算路线");
                return;
            } else {//实时导航
                if (routeIndex > ways.size()) {
                    routeIndex = 0;
                }
                mAMapNavi.selectRouteId(routeOverlays.keyAt(routeIndex));
                Intent gpsintent = new Intent(this, BasicNaviActivity.class);
                startActivity(gpsintent);

            }
        }
    }

    /**
     * 多条路线计算结果回调2
     *
     * @param ints
     */
//    @Override
    private void onCalculateMultipleRoutesSuccessOld(int[] ints) {
        //清空上次计算的路径列表。
        routeOverlays.clear();
        ways.clear();
        HashMap<Integer, AMapNaviPath> paths = mAMapNavi.getNaviPaths();
        for (int i = 0; i < ints.length; i++) {
            AMapNaviPath path = paths.get(ints[i]);
            if (path != null) {
                drawRoutes(ints[i], path);
                ways.add(path);
            }
        }
        if (ways.size() > 0) {
            currentPosition = 0;
            lastPosition = -1;
            mAdapter.notifyDataSetChanged();
            mRecyclerView.setVisibility(View.VISIBLE);
            oneWay.setVisibility(View.GONE);
            tvNavi.setText("开始导航");
        } else if (ways.size() == 1) {
            mRecyclerView.setVisibility(View.GONE);
            oneWay.setVisibility(View.VISIBLE);
            tvTime.setText(getTime(ways.get(0).getAllTime()));
            tvLength.setText(getLength(ways.get(0).getAllLength()));
            tvNavi.setText("开始导航");
        } else {
            mRecyclerView.setVisibility(View.GONE);
            tvNavi.setText("准备导航");
        }
        changeRoute();
    }

    /**
     * 单条路线计算结果回调2
     */
//    @Override
    private void onCalculateRouteSuccessOld() {
        /**
         * 清空上次计算的路径列表。
         */
        routeOverlays.clear();
        ways.clear();
        AMapNaviPath path = mAMapNavi.getNaviPath();
        /**
         * 单路径不需要进行路径选择，直接传入－1即可
         */
        drawRoutes(-1, path);
        mRecyclerView.setVisibility(View.GONE);
        oneWay.setVisibility(View.VISIBLE);
        tvTime.setText(getTime(path.getAllTime()));
        tvLength.setText(getLength(path.getAllLength()));
        tvNavi.setText("开始导航");
    }
}
