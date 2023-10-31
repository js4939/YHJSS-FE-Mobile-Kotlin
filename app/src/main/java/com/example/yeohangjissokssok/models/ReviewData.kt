package com.example.yeohangjissokssok.models

data class ReviewData(
    val id: Long,
    val date: String,
    val content: String,
    val state: Int = -1,
    val keyword: String = "없음"
)
