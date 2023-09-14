package com.example.yeohangjissokssok.activity

import PlaceAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.yeohangjissokssok.R
import com.example.yeohangjissokssok.api.RetrofitBuilder
import com.example.yeohangjissokssok.databinding.ActivityRecommendListBinding
import com.example.yeohangjissokssok.models.SACategoryResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecommendListActivity : AppCompatActivity() {

    lateinit var placeAdapterRecommend: PlaceAdapterRecommend

    // 바인딩 객체 선언
    private lateinit var binding: ActivityRecommendListBinding

    val datas = ArrayList<SACategoryResponse>()

    var placeId = 3
    var region = "region"
    val name = "name"
    val positiveNumber = 1
    val totalNumber = 1
    val proportion = 0.001


    private fun getSACategoryPlace(input : String){
        CoroutineScope(Dispatchers.Main).launch{
            RetrofitBuilder.api.getSACategoryPlace(input).enqueue(object : Callback<APIResponseData>{
                override fun onResponse(
                    call: Call<APIResponseData>,
                    response: Response<APIResponseData>
                ) {
                    if (response.isSuccessful) {
                        val temp = response.body() as APIResponseData
                        val jsonResult = Gson().toJson(temp.data)
                        val result: List<SACategoryResponse> = GsonBuilder()
                            .create()
                            .fromJson(jsonResult, Array<SACategoryResponse>::class.java)
                            .toList()
                        Log.d("result", result.toString())

                        // initRecycler 함수를 호출하여 데이터 추가
                        initRecycler(result)
                    }
                }

                override fun onFailure(call: Call<APIResponseData>, t: Throwable) {
                    Log.d("test", "연결실패")
                }

            })
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend_list)

        binding = ActivityRecommendListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        placeAdapterRecommend = PlaceAdapterRecommend(this.datas)
        binding.rvRecommendlist.adapter=placeAdapterRecommend

        // 리사이클러뷰 구분선 지정
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.rvRecommendlist.addItemDecoration(dividerItemDecoration)

        initClickEvent()

        // 분위기-C001, 교통-C002, 혼잡도-C003, 인프라-C004
        getSACategoryPlace("C001")

    }

    private fun initRecycler(result: List<SACategoryResponse>) {

        // 데이터 추가
        datas.addAll(result)

        //datas.add(SACategoryResponse(1, "서울 관악", "낙성대공원", 1,1,0.00001))
        //datas.add(SACategoryResponse(1, "서울 관악", "낙성대공원", 1,1,0.00001))
        //datas.add(SACategoryResponse(1, "서울 관악", "낙성대공원", 1,1,0.00001))

        // 어댑터에 데이터 설정
        placeAdapterRecommend.datas = datas

        // RecyclerView를 갱신
        placeAdapterRecommend.notifyDataSetChanged()
    }

    private fun initClickEvent() {
        binding.apply{
            goBackBtn.setOnClickListener {
                // 뒤로가기 버튼 클릭 시 이벤트
                startActivity(Intent(this@RecommendListActivity, ResultActivity::class.java))
            }

            // adapter에 클릭리스너 부착
            // 여행지 클릭 시 이벤트
            val intent = Intent(this@RecommendListActivity, ResultActivity::class.java)
            placeAdapterRecommend.itemClicklistener = object:PlaceAdapterRecommend.OnItemClickListener{
                override fun OnItemClick(position: Int) {
                    // 다음 페이지의 http 통신을 위해 여행지 정보 넘겨줘야 함
                    intent.putExtra("placeId", placeAdapterRecommend.datas[position].placeId)
                    intent.putExtra("region", placeAdapterRecommend.datas[position].region)
                    intent.putExtra("name", placeAdapterRecommend.datas[position].name)
                    intent.putExtra("positiveNumber", placeAdapterRecommend.datas[position].positiveNumber)
                    intent.putExtra("totalNumber", placeAdapterRecommend.datas[position].totalNumber)
                    intent.putExtra("proportion", placeAdapterRecommend.datas[position].proportion)
                    intent.putExtra("page", "placeList")
                    startActivity(intent)
                }
            }
        }
    }
}