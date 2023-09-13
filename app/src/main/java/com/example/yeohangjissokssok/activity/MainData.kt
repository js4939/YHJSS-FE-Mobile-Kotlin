package com.example.yeohangjissokssok.activity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MainData(
    val tv_rv_place: String,
    val img: Int
) : Parcelable
