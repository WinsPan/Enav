package com.tcup.transformer.transnav.map.overlay;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.tcup.transformer.transnav.R;
import com.tcup.transformer.transnav.bean.MarketBean;
import com.tcup.transformer.transnav.map.util.ORMUtil;

import java.util.ArrayList;

public class WindowAdapter implements AMap.InfoWindowAdapter, AMap.OnMarkerClickListener,
        AMap.OnInfoWindowClickListener {

    private Context context;
    private static final String TAG = "WindowAdapter";

    public WindowAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        if (marker == null || "".equals(marker.getTitle()) || marker.getTitle() == null) {
            return null;
        }
        String name = marker.getTitle();
        ArrayList<MarketBean> query = ORMUtil.getLiteOrm(context).query(new QueryBuilder<MarketBean>(MarketBean.class)
                .whereEquals("address", name).whereAppendOr().whereEquals("address", marker.getSnippet())
        );
        if (query == null || query.size() < 1) {
            return null;
        }
        //关联布局
        View view = LayoutInflater.from(context).inflate(R.layout.layout_info_item, null);
        //标题
        TextView title = (TextView) view.findViewById(R.id.info_title);
        //纬度
        TextView latitude = (TextView) view.findViewById(R.id.info_latitude);
        //经度
        TextView longitude = (TextView) view.findViewById(R.id.info_longitude);

        title.setText(marker.getTitle());
//        marker.getPosition().latitude, marker.getPosition().longitude
        latitude.setText(query.get(0).getLatitude() + "");
        longitude.setText(query.get(0).getLongitude() + "");
        Log.e(TAG, "getInfoWindow1: " + marker.getTitle());
        Log.e(TAG, "getInfoWindow: " + marker.getSnippet());
        Log.e(TAG, "getInfoWindow: " + marker.getPosition().latitude);
        Log.e(TAG, "getInfoWindow: " + marker.getPosition().longitude);
        return view;
    }

    //如果用自定义的布局，不用管这个方法,返回null即可
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    // marker 对象被点击时回调的接口
    // 返回 true 则表示接口已响应事件，否则返回false
    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.e(TAG, "Marker被点击了");
        return false;
    }

    //绑定信息窗点击事件
    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.e(TAG, "InfoWindow被点击了");
        Poi start = new Poi(marker.getSnippet(), new LatLng(marker.getPosition().latitude, marker.getPosition().longitude), "");
        Poi end = new Poi(marker.getSnippet(), new LatLng(31.804556, 117.227231), "B000A83M61");
        AmapNaviPage.getInstance().showRouteActivity(context, new AmapNaviParams(start, null, end, AmapNaviType.WALK), null);
    }
}