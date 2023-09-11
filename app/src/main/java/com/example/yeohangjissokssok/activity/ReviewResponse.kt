package com.example.yeohangjissokssok.activity

import com.google.gson.annotations.SerializedName

data class ReviewResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<ReviewData>
    ){
    data class ReviewData(
        @SerializedName("id") val id: Int,
        @SerializedName("content") val content: String,
        @SerializedName("state") val state: Int
    )
}
