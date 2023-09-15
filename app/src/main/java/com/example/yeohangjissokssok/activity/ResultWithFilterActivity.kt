package com.example.yeohangjissokssok.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.yeohangjissokssok.R
import com.example.yeohangjissokssok.databinding.ActivityResultWithFilterBinding

class ResultWithFilterActivity : AppCompatActivity() {

    lateinit var binding: ActivityResultWithFilterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultWithFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent로 monthText 값 추출
        val selectedMonth = intent.getStringExtra("selectedMonth")
        //Log.d("select month ", selectedMonth.toString())

        val selectedTextTextView = findViewById<TextView>(R.id.selectedFilter)
        selectedTextTextView.text = "$selectedMonth"+"월"



        initClickEvent()
    }

    private fun initClickEvent() {
        binding.goBackBtn.setOnClickListener {
            // 뒤로가기 버튼 클릭 시 이벤트
            var intent = Intent(this, ApplyFilterActivity::class.java)
            startActivity(intent)
        }
    }

}