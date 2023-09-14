package com.example.yeohangjissokssok.activity

data class PlaceResponse(
    val id: Long,
    val name: String,
    val region: String,
    val address: String?
): java.io.Serializable
