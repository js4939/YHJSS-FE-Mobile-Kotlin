package com.example.yeohangjissokssok.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.yeohangjissokssok.R


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_list)

        val intent = Intent(this, PlaceListActivity::class.java)
        startActivity(intent)

        // 페이지 전환 로그 메시지
        Log.d("PageTransition", "MainActivity -> PlaceListActivity")
    }
}