package com.example.yeohangjissokssok.api

import com.example.yeohangjissokssok.activity.APIResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitService {
    @GET("place/{placeId}")
    fun getPlace(@Path("placeId") number : Long) : Call<APIResponseData>
}
