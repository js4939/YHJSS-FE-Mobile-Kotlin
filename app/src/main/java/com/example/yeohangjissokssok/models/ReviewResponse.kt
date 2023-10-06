package com.example.yeohangjissokssok.models

import com.google.gson.annotations.SerializedName

data class ReviewResponse(
    val id: Long,
    val date: String,
    val content: String,
    val state: Int
    ): java.io.Serializable
