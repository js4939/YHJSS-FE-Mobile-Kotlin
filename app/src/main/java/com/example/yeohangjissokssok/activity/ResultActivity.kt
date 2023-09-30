package com.example.yeohangjissokssok.activity

import PlaceAdapter
import ReviewAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yeohangjissokssok.api.RetrofitBuilder
import com.example.yeohangjissokssok.databinding.ActivityResultBinding
import com.example.yeohangjissokssok.models.ReviewResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultActivity : AppCompatActivity() {

    lateinit var binding: ActivityResultBinding
    lateinit var adapter: ReviewAdapter
    val datas = ArrayList<ReviewResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLayout()
        initClickEvent()
    }


    // 고쳐야될듯듯
//   private fun getReviewsById(input: Long, callback: (List<ReviewResponse>) -> Unit) {
//        CoroutineScope(Dispatchers.Main).launch {
//            RetrofitBuilder.api.getAllReviews(input, "S001").enqueue(object : Callback<APIResponseData> {
//                override fun onResponse(
//                    call: Call<APIResponseData>, response: Response<APIResponseData>
//                ) {
//                    if (response.isSuccessful) {
//                        val temp = response.body() as APIResponseData
//                        val jsonResult = Gson().toJson(temp.data)
//                        val result: List<ReviewResponse> = GsonBuilder()
//                            .create()
//                            .fromJson(jsonResult, Array<ReviewResponse>::class.java)
//                            .toList()
//                        callback(result) // 데이터를 가져온 후 콜백으로 전달
//                    }
//                }
//
//                override fun onFailure(call: Call<APIResponseData>, t: Throwable) {
//                    Log.d("test", "연결실패")
//                }
//
//            })
//        }
//    }

    private fun getContents() {
//        var id = intent?.getStringExtra("id")!!.toLong()
//
//        if(id != null){
//            Log.d("check", "NOT NULL")
//            getReviewsById(id){
//                    result ->
//                datas.clear() // 기존 데이터를 지우고 새로운 데이터로 대체
//                datas.addAll(result)
//                adapter.notifyDataSetChanged()
//            }
//        }
    }

    private fun initLayout() {

        getContents()

        adapter = ReviewAdapter(this.datas)
        binding.recyclerView.adapter = adapter

        datas.add(ReviewResponse(0, "풍경이 예쁘고\n 교통이 편리해요", 1))
        datas.add(ReviewResponse(0, "풍경이 예쁘고\n 교통이 편리해요", 2))
        datas.add(ReviewResponse(0, "풍경이 예쁘고\n 교통이 편리해요", 0))

        adapter.datas = datas

        // recyclerView에 구분자 추가
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )

        // RecyclerView 갱신
        adapter.notifyDataSetChanged()

    }

    private fun initClickEvent() {
        binding.apply{
            goBackBtn.setOnClickListener {
                // 뒤로가기 버튼 클릭 시 이벤트
//                lateinit var intent: Intent
//                var previousPage = intent.getStringExtra("page") as String
//                if(previousPage=="placeList"){
//                    intent = Intent(this@ResultActivity, PlaceListActivity::class.java)
//                }
//                else if(previousPage=="recommendList"){
//                    intent = Intent(this@ResultActivity, RecommendListActivity::class.java)
//                }
//                startActivity(intent)
            }

            filterBtn.setOnClickListener {
                // 월별필터 버튼 클릭시 이벤트
                var intent = Intent(this@ResultActivity, ApplyFilterActivity::class.java)
                startActivity(intent)
            }
        }
    }
}