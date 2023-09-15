package com.example.yeohangjissokssok.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.yeohangjissokssok.R
import kotlin.math.log

class ApplyFilterActivity : AppCompatActivity() {

    private val monthButtons = arrayOf(
        R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
        R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8,
        R.id.btn9, R.id.btn10, R.id.btn11, R.id.btn12
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_filter)

        // 월별 버튼에 클릭 리스너 설정
        for (btnId in monthButtons) {
            val button = findViewById<Button>(btnId)
            button.setOnClickListener { onMonthButtonClick(button.text.toString()) }
        }
    }

    private fun onMonthButtonClick(monthText: String) {
        // 선택된 월을 다음 화면으로 전달하고 페이지 변경
        val intent = Intent(this, ResultWithFilterActivity::class.java)

        // 서버 통신 위해 "월" 문자는 전달하지 않음
        val substring = monthText.substring(0, monthText.length - 1)
        intent.putExtra("selectedMonth", substring)
        startActivity(intent)

        Log.d("select month ", substring)
    }
}
