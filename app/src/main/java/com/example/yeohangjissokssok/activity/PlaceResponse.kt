package com.example.yeohangjissokssok.activity

import com.google.gson.annotations.SerializedName

data class PlaceResponse(
    val id: Int,
    val name: String,
    val region: String,
    val address: String
): java.io.Serializable
