package com.example.yeohangjissokssok.activity

import PlaceAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.yeohangjissokssok.R
import com.example.yeohangjissokssok.databinding.ActivityPlaceListBinding

class PlaceListActivity : AppCompatActivity() {

    lateinit var placeAdapter: PlaceAdapter

    // 바인딩 객체 선언
    private lateinit var binding: ActivityPlaceListBinding

    val datas = mutableListOf<MainData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        placeAdapter = PlaceAdapter(this)
        binding.rvPlace.adapter = placeAdapter

        initRecycler()
    }

    private fun initRecycler() {
        // 데이터 추가
        datas.add(MainData(tv_rv_place = "서울 송파 석촌호수", img = R.drawable.baseline_add_circle_24))
        datas.add(MainData(tv_rv_place = "서울 송파 롯데월드", img = R.drawable.baseline_add_circle_24))
        datas.add(MainData(tv_rv_place = "서울 송파 잠실야구경기장", img = R.drawable.baseline_add_circle_24))

        // 어댑터에 데이터 설정
        placeAdapter.datas = datas

        // RecyclerView를 갱신
        placeAdapter.notifyDataSetChanged()
    }
}

