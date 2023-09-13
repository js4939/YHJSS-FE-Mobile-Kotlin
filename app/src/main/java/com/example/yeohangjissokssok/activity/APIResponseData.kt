package com.example.yeohangjissokssok.activity

data class APIResponseData(
    val status: Int,
    val code: String,
    val message: String,
    val data: Any
    ) : java.io.Serializable
