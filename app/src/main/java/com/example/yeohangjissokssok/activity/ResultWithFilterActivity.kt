package com.example.yeohangjissokssok.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.yeohangjissokssok.R
import com.example.yeohangjissokssok.databinding.ActivityResultWithFilterBinding

class ResultWithFilterActivity : AppCompatActivity() {

    lateinit var binding: ActivityResultWithFilterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultWithFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

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