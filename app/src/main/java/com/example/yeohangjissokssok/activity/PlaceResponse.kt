package com.example.yeohangjissokssok.activity

import com.google.gson.annotations.SerializedName

data class PlaceResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<PlaceData>
)  {
    data class PlaceData(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("region") val region: String,
        @SerializedName("address") val address: String
    )
}
