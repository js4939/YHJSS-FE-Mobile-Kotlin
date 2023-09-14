package com.example.yeohangjissokssok.api

import com.example.yeohangjissokssok.activity.APIResponseData
import com.example.yeohangjissokssok.activity.PlaceRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {
    @GET("place/{placeId}")
    fun getPlace(@Path("placeId") number : Long) : Call<APIResponseData>

    @GET("place/name")
    fun getPlaceByName(@Query("name") name: String) : Call<APIResponseData>
}
