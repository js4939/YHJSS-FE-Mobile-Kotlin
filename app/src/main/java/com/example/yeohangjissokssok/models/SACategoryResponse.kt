package com.example.yeohangjissokssok.models

import com.google.gson.annotations.SerializedName

data class SACategoryResponse(
    val id: Long,
    val name: String,
    val region: String,
    val positiveNumber: Int,
    val totalNumber: Int,
    val proportion: Int
): java.io.Serializable
