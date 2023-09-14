package com.example.yeohangjissokssok.models

import com.google.gson.annotations.SerializedName

data class SACategoryResponse(
    val placeId: Long,
    val region: String,
    val name: String,
    val positiveNumber: Long,
    val totalNumber: Long,
    val proportion: Double
): java.io.Serializable
