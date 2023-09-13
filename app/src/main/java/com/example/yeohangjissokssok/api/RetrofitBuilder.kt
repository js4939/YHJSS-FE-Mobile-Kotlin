package com.example.yeohangjissokssok.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    var api: RetrofitService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://yeohangjissokssok.fly.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(RetrofitService::class.java)
    }
}