package com.example.yeohangjissokssok.models

data class KeywordListResponse(
    val placeId: Long,
    val region: String,
    val name: String,
    val address: String,
    val photoUrl: String,
    val keywordCount: Int
): java.io.Serializable