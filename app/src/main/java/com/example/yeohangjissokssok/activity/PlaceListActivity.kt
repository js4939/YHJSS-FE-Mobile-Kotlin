package com.example.yeohangjissokssok.activity

import PlaceAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.yeohangjissokssok.R
import com.example.yeohangjissokssok.api.RetrofitBuilder
import com.example.yeohangjissokssok.databinding.ActivityPlaceListBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class PlaceListActivity : AppCompatActivity() {

    lateinit var placeAdapter: PlaceAdapter

    // 바인딩 객체 선언
    private lateinit var binding: ActivityPlaceListBinding

    val datas = mutableListOf<MainData>()

    var id = 3
    var name = "name"
    var region = "region"
    var address = "address"
    var body = ""

    private fun http() {
        CoroutineScope(Dispatchers.Main).launch {
            RetrofitBuilder.api.getPlace(1).enqueue(object : Callback<APIResponseData> {
                override fun onResponse(
                    call: Call<APIResponseData>, response: Response<APIResponseData>
                ) {
                    if (response.isSuccessful) {
                        val temp = response.body() as APIResponseData
                        val type: Type = object : TypeToken<PlaceResponse>() {}.type
                        val jsonResult = Gson().toJson(temp.data)
                        val result = Gson().fromJson(jsonResult, type) as PlaceResponse
                        Log.d("result", result.toString())
                    }
                }

                override fun onFailure(call: Call<APIResponseData>, t: Throwable) {
                    Log.d("test", "연결실패")
                }

            })
        }
        data class APIResponseData(
            val status: Int,
            val code: String,
            val message: String,
            val data: Any
        ) : java.io.Serializable

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        placeAdapter = PlaceAdapter(this)
        binding.rvPlace.adapter = placeAdapter

        initRecycler()
        http()
    }

    private fun initRecycler() {
        // 데이터 추가
        datas.add(MainData(tv_rv_place = "서울 송파 석촌호수", img = R.drawable.baseline_add_circle_24))
        datas.add(MainData(tv_rv_place = "서울 송파 롯데월드", img = R.drawable.baseline_add_circle_24))
        datas.add(MainData(tv_rv_place = "서울 송파 잠실야구경기장", img = R.drawable.baseline_add_circle_24))

        // 어댑터에 데이터 설정
        placeAdapter.datas = datas

        // RecyclerView를 갱신
        placeAdapter.notifyDataSetChanged()
    }
}


