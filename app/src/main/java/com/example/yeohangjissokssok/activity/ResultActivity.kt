package com.example.yeohangjissokssok.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.yeohangjissokssok.R
import com.example.yeohangjissokssok.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClickEvent()
    }

    private fun initClickEvent() {
        binding.apply{
            goBackBtn.setOnClickListener {
                // 뒤로가기 버튼 클릭 시 이벤트
                lateinit var intent: Intent
                var previousPage = intent.getStringExtra("page") as String
                if(previousPage=="placeList"){
                    intent = Intent(this@ResultActivity, PlaceListActivity::class.java)
                }
                else if(previousPage=="recommendList"){
                    intent = Intent(this@ResultActivity, RecommendListActivity::class.java)
                }
                startActivity(intent)
            }

            filterBtn.setOnClickListener {
                // 월별필터 버튼 클릭시 이벤트
                var intent = Intent(this@ResultActivity, ApplyFilterActivity::class.java)
                startActivity(intent)
            }
        }
    }
}