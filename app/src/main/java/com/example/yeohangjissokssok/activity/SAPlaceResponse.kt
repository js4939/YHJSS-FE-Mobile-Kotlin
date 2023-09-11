package com.example.yeohangjissokssok.activity

import com.google.gson.annotations.SerializedName

data class SAPlaceResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<SAPlaceData>
){
    data class SAPlaceData(
        @SerializedName("samonthlysummary_id") val samonthlysummary_id: Int,
        @SerializedName("category") val category: String,
        @SerializedName("positive") val positive: Int,
        @SerializedName("negative") val negative: Int,
        @SerializedName("neutral") val neutral: Int
    )
}
