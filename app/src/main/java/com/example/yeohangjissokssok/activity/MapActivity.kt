package com.example.yeohangjissokssok.activity

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.yeohangjissokssok.databinding.ActivityMapBinding
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


class MapActivity: AppCompatActivity() {

    lateinit var binding: ActivityMapBinding
    lateinit var mapView: MapView

    private var lat: Double = 0.0
    private var long: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 클릭한 여행지로 마커 표시
        setMap()
    }

    private fun setMap() {
        var name = intent.getStringExtra("name")
        lat = intent.getDoubleExtra("lat", 0.0)
        long = intent.getDoubleExtra("long", 0.0)

        mapView = MapView(this)
        binding.map.addView(mapView)

        // 여행지의 위도, 경도 설정
        val mapPoint = MapPoint.mapPointWithGeoCoord(lat, long)

        mapView.setMapCenterPoint(mapPoint, true)
        mapView.setZoomLevel(3, true)

        // 여행지에 대한 마커 생성
        val marker = MapPOIItem()
        marker.itemName = name
        marker.mapPoint = mapPoint
        marker.markerType = MapPOIItem.MarkerType.RedPin

        mapView.addPOIItem(marker)
    }
}
