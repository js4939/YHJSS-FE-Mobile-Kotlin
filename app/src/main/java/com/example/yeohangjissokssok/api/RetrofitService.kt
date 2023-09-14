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

    @GET("sa/place/{placeId}")
    fun getSAPlace(@Path("placeId") placeId : Long) : Call<APIResponseData>

    @GET("sa/{placeId}/{month}")
    fun getSAMonthPlace(@Path("placeId") placeId : Long,
                        @Path("month") month: Int) : Call<APIResponseData>

    @GET("sa/category/{categoryCode}")
    fun getSACategoryPlace(@Path("categoryCode") categoryCode : String) : Call<APIResponseData>

}
