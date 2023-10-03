package com.example.yeohangjissokssok.activity

data class PlaceResponse(
    val id: Long,
    val name: String,
    val region: String,
    val address: String?,
    val latitude: Double,
    val longitude: Double,
    val photoUrl: String
): java.io.Serializable
