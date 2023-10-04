package com.example.yeohangjissokssok.api

import com.example.yeohangjissokssok.activity.APIResponseData
import com.example.yeohangjissokssok.activity.PlaceRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {
    @GET("places/{placeId}")
    fun getPlace(@Path("placeId") number : Long) : Call<APIResponseData>

    @GET("places/name")
    fun getPlaceByName(@Query("name") name: String) : Call<APIResponseData>

    @GET("sas/place/{placeId}")
    fun getSAPlace(@Path("placeId") placeId : Long) : Call<APIResponseData>

    @GET("sas/{placeId}/{month}")
    fun getSAMonthPlace(@Path("placeId") placeId : Long,
                        @Path("month") month: Int) : Call<APIResponseData>

    @GET("sas/category/{categoryCode}")
    fun getSACategoryPlace(@Path("categoryCode") categoryCode : String) : Call<APIResponseData>

    @GET("sas/category/{categoryCode}/{month}")
    fun getMonthSACategoryPlace(@Path("categoryCode") categoryCode : String,
                                @Path("month") month: Int) : Call<APIResponseData>

    @GET("reviews/{saMonthlySummaryId}/{categoryCode}")
    fun getReviews(@Path("saMonthlySummaryId") saMonthlySummaryId : Long,
                   @Path("categoryCode") categoryCode: String) : Call<APIResponseData>

    @GET("reviews/month/{saMonthlySummaryId}/{categoryCode}")
    fun getMonthReviews(@Path("saMonthlySummaryId") saMonthlySummaryId : Long,
                        @Path("categoryCode") categoryCode: String) : Call<APIResponseData>
}
