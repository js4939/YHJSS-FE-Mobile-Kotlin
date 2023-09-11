package com.example.yeohangjissokssok.activity

import com.google.gson.annotations.SerializedName

data class SACategoryResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<SACategoryData>
)   {
    data class SACategoryData(
        @SerializedName("placeId") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("region") val region: String,
        @SerializedName("positiveNumber") val positiveNumber: Int,
        @SerializedName("totalNumber") val totalNumber: Int,
        @SerializedName("proportion") val proportion: Int
    )
}
