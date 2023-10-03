package com.example.yeohangjissokssok.models

data class SAPlaceResponse(
    val samonthlysummary_id: Long,
    val category: String,
    val positive: Int,
    val negative: Int,
    val neutral: Int
): java.io.Serializable


