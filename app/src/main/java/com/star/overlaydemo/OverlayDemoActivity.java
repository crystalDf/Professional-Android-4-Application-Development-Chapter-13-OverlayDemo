package com.star.overlaydemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

public class OverlayDemoActivity extends AppCompatActivity {

    /**
     * MapView 是地图主控件
     */
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Marker mMarkerA;
    private Marker mMarkerB;
    private Marker mMarkerC;
    private Marker mMarkerD;
    private InfoWindow mInfoWindow;

    // 初始化全局 bitmap 信息，不用时及时 recycle
    private BitmapDescriptor mBitmapDescriptorA;
    private BitmapDescriptor mBitmapDescriptorB;
    private BitmapDescriptor mBitmapDescriptorC;
    private BitmapDescriptor mBitmapDescriptorD;
    private BitmapDescriptor mBitmapDescriptor;
    private BitmapDescriptor mBitmapDescriptorGround;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_overlay_demo);

        mMapView = (MapView) findViewById(R.id.map_view);

        mBaiduMap = mMapView.getMap();

        initOverlay();

        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {

            }

            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(
                        OverlayDemoActivity.this,
                        "拖拽结束，新位置：" + marker.getPosition().latitude + ", "
                                + marker.getPosition().longitude,
                        Toast.LENGTH_LONG).show();
            }

            public void onMarkerDragStart(Marker marker) {

            }
        });

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                Button button = new Button(getApplicationContext());
                button.setBackgroundResource(R.drawable.popup);

                if (marker == mMarkerA || marker == mMarkerD) {
                    button.setText("更改位置");

                    LatLng latLngInfo = marker.getPosition();

                    mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button),
                            latLngInfo, -47, new InfoWindow.OnInfoWindowClickListener() {

                        public void onInfoWindowClick() {
                            LatLng latLngOld = marker.getPosition();
                            LatLng latLngNew = new LatLng(latLngOld.latitude + 0.005,
                                    latLngOld.longitude + 0.005);
                            marker.setPosition(latLngNew);
                            mBaiduMap.hideInfoWindow();
                        }
                    });

                    mBaiduMap.showInfoWindow(mInfoWindow);
                } else if (marker == mMarkerB) {
                    button.setText("更改图标");

                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            marker.setIcon(mBitmapDescriptor);
                            mBaiduMap.hideInfoWindow();
                        }
                    });

                    LatLng latLngInfo = marker.getPosition();

                    mInfoWindow = new InfoWindow(button, latLngInfo, -47);

                    mBaiduMap.showInfoWindow(mInfoWindow);
                } else if (marker == mMarkerC) {
                    button.setText("删除");

                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            marker.remove();
                            mBaiduMap.hideInfoWindow();
                        }
                    });

                    LatLng latLngInfo = marker.getPosition();

                    mInfoWindow = new InfoWindow(button, latLngInfo, -47);

                    mBaiduMap.showInfoWindow(mInfoWindow);
                }
                return true;
            }
        });

    }

    public void initOverlay() {

        mBitmapDescriptorA = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_marka);
        mBitmapDescriptorB = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_markb);
        mBitmapDescriptorC = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_markc);
        mBitmapDescriptorD = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_markd);
        mBitmapDescriptor = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);
        mBitmapDescriptorGround = BitmapDescriptorFactory
                .fromResource(R.drawable.ground_overlay);

        // add marker overlay
        LatLng latLngA = new LatLng(39.963175, 116.400244);
        LatLng latLngB = new LatLng(39.942821, 116.369199);
        LatLng latLngC = new LatLng(39.939723, 116.425541);
        LatLng latLngD = new LatLng(39.906965, 116.401394);

        OverlayOptions overlayOptionsA = new MarkerOptions()
                .position(latLngA)
                .icon(mBitmapDescriptorA)
                .zIndex(9)
                .draggable(true);

        mMarkerA = (Marker) (mBaiduMap.addOverlay(overlayOptionsA));

        OverlayOptions overlayOptionsB = new MarkerOptions()
                .position(latLngB)
                .icon(mBitmapDescriptorB)
                .zIndex(5);

        mMarkerB = (Marker) (mBaiduMap.addOverlay(overlayOptionsB));

        OverlayOptions overlayOptionsC = new MarkerOptions()
                .position(latLngC)
                .icon(mBitmapDescriptorC)
                .perspective(false)
                .anchor(0.5f, 0.5f)
                .rotate(30)
                .zIndex(7);

        mMarkerC = (Marker) (mBaiduMap.addOverlay(overlayOptionsC));

        List<BitmapDescriptor> bitmapDescriptorList = new ArrayList<>();
        bitmapDescriptorList.add(mBitmapDescriptorA);
        bitmapDescriptorList.add(mBitmapDescriptorB);
        bitmapDescriptorList.add(mBitmapDescriptorC);

        OverlayOptions overlayOptionsD = new MarkerOptions()
                .position(latLngD)
                .icons((ArrayList<BitmapDescriptor>) bitmapDescriptorList)
                .zIndex(0)
                .period(10);

        mMarkerD = (Marker) (mBaiduMap.addOverlay(overlayOptionsD));

        // add ground overlay
        LatLng latLngSouthWest = new LatLng(39.92235, 116.380338);
        LatLng latLngNorthEast = new LatLng(39.947246, 116.414977);
        LatLngBounds bounds = new LatLngBounds
                .Builder()
                .include(latLngNorthEast)
                .include(latLngSouthWest)
                .build();

        OverlayOptions overlayOptionsGround = new GroundOverlayOptions()
                .positionFromBounds(bounds)
                .image(mBitmapDescriptorGround)
                .transparency(0.8f);

        mBaiduMap.addOverlay(overlayOptionsGround);

        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
                .newLatLng(bounds.getCenter());

        mBaiduMap.setMapStatus(mapStatusUpdate);

        mapStatusUpdate = MapStatusUpdateFactory.zoomTo(14.0f);

        mBaiduMap.setMapStatus(mapStatusUpdate);

    }

    /**
     * 清除所有Overlay
     *
     * @param view
     */
    public void clearOverlay(View view) {
        mBaiduMap.clear();
    }

    /**
     * 重新添加Overlay
     *
     * @param view
     */
    public void resetOverlay(View view) {
        clearOverlay(null);
        initOverlay();
    }

    @Override
    protected void onResume() {

        super.onResume();

        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        mMapView.onResume();
    }

    @Override
    protected void onPause() {

        super.onPause();

        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        mMapView.onDestroy();

        // 回收 bitmap 资源
        mBitmapDescriptorA.recycle();
        mBitmapDescriptorB.recycle();
        mBitmapDescriptorC.recycle();
        mBitmapDescriptorD.recycle();
        mBitmapDescriptor.recycle();
        mBitmapDescriptorGround.recycle();
    }

}