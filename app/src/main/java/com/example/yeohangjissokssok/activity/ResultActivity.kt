package com.example.yeohangjissokssok.activity

import PlaceAdapter
import ReviewAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.yeohangjissokssok.R
import com.example.yeohangjissokssok.api.RetrofitBuilder
import com.example.yeohangjissokssok.databinding.ActivityResultBinding
import com.example.yeohangjissokssok.models.ReviewResponse
import com.example.yeohangjissokssok.models.SAPlaceResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.round

class ResultActivity : AppCompatActivity() {

    lateinit var binding: ActivityResultBinding
    lateinit var adapter: ReviewAdapter

    var placeId: Long = 0
    var saPlaceId: Long = 0

    val datas = ArrayList<ReviewResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLayout()
        initSpinner()
    }

    // 장소 정보 불러오는 메소드
    private fun getPlaceById(input: Long, callback: (PlaceResponse) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            RetrofitBuilder.api.getPlace(input).enqueue(object : Callback<APIResponseData> {
                override fun onResponse(
                    call: Call<APIResponseData>, response: Response<APIResponseData>
                ) {
                    if (response.isSuccessful) {
                        val temp = response.body() as APIResponseData
                        val jsonResult = Gson().toJson(temp.data)
                        val result: PlaceResponse = GsonBuilder()
                            .create()
                            .fromJson(jsonResult, PlaceResponse::class.java)
                        callback(result) // 데이터를 가져온 후 콜백으로 전달
                    }
                }

                override fun onFailure(call: Call<APIResponseData>, t: Throwable) {
                    Log.d("test", "연결실패")
                }
            })
        }
    }

    // 카테고리 별 리뷰 불러오는 메소드
    private fun getPlaceReviews(category: String, callback: (List<ReviewResponse>) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            RetrofitBuilder.api.getReviews(saPlaceId, category).enqueue(object : Callback<APIResponseData> {
                override fun onResponse(
                    call: Call<APIResponseData>, response: Response<APIResponseData>
                ) {
                    if (response.isSuccessful) {
                        val temp = response.body() as APIResponseData
                        val jsonResult = Gson().toJson(temp.data)
                        val result: List<ReviewResponse> = GsonBuilder()
                            .create()
                            .fromJson(jsonResult, Array<ReviewResponse>::class.java)
                            .toList()
                        callback(result) // 데이터를 가져온 후 콜백으로 전달
                    }
                }

                override fun onFailure(call: Call<APIResponseData>, t: Throwable) {
                    Log.d("test", "연결실패")
                }
            })
        }
    }

    // 월 별 리뷰 불러오는 메소드
    private fun getPlaceMonthReviews(category: String, callback: (List<ReviewResponse>) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            RetrofitBuilder.api.getMonthReviews(saPlaceId, category).enqueue(object : Callback<APIResponseData> {
                override fun onResponse(
                    call: Call<APIResponseData>, response: Response<APIResponseData>
                ) {
                    if (response.isSuccessful) {
                        val temp = response.body() as APIResponseData
                        val jsonResult = Gson().toJson(temp.data)
                        val result: List<ReviewResponse> = GsonBuilder()
                            .create()
                            .fromJson(jsonResult, Array<ReviewResponse>::class.java)
                            .toList()
                        callback(result) // 데이터를 가져온 후 콜백으로 전달
                    }
                }

                override fun onFailure(call: Call<APIResponseData>, t: Throwable) {
                    Log.d("test", "연결실패")
                }
            })
        }
    }

    // 카테고리 별 감성분석 결과 불러오는 메소드
    private fun getPlaceSAResult(id: Long, callback: (List<SAPlaceResponse>) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            RetrofitBuilder.api.getSAPlace(id).enqueue(object : Callback<APIResponseData> {
                override fun onResponse(
                    call: Call<APIResponseData>, response: Response<APIResponseData>
                ) {
                    if (response.isSuccessful) {
                        val temp = response.body() as APIResponseData
                        val jsonResult = Gson().toJson(temp.data)
                        val result: List<SAPlaceResponse> = GsonBuilder()
                            .create()
                            .fromJson(jsonResult, Array<SAPlaceResponse>::class.java)
                            .toList()
                        callback(result) // 데이터를 가져온 후 콜백으로 전달
                    }
                }

                override fun onFailure(call: Call<APIResponseData>, t: Throwable) {
                    Log.d("test", "연결실패")
                }
            })
        }
    }

    // 월 별 감성분석 결과 불러오는 메소드
    private fun getPlaceMonthlySAResult(id: Long, month: Int, callback: (List<SAPlaceResponse>) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            RetrofitBuilder.api.getSAMonthPlace(id, month).enqueue(object : Callback<APIResponseData> {
                override fun onResponse(
                    call: Call<APIResponseData>, response: Response<APIResponseData>
                ) {
                    if (response.isSuccessful) {
                        val temp = response.body() as APIResponseData
                        val jsonResult = Gson().toJson(temp.data)
                        val result: List<SAPlaceResponse> = GsonBuilder()
                            .create()
                            .fromJson(jsonResult, Array<SAPlaceResponse>::class.java)
                            .toList()
                        callback(result) // 데이터를 가져온 후 콜백으로 전달
                    }
                }

                override fun onFailure(call: Call<APIResponseData>, t: Throwable) {
                    Log.d("test", "연결실패")
                }
            })
        }
    }

    private fun setContents() {
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

    // 사진 설정 메소드
    private fun setPlaceImg(url: String){
        Glide.with(this)
            .load(url)      // 이미지 url
            .placeholder(R.drawable.baseline_update_24)  // 로딩 이미지
            .error(R.drawable.baseline_error_24)    // 에러 이미지
            .into(binding.placeImg)     // 이미지 띄울 위치
    }

    private fun initLayout() {
        placeId = intent.getLongExtra("placeId", 0)

        getPlaceById(placeId) {
            result ->
                // 클릭한 장소 정보로 화면 설정
                binding.placeName.text = result.name
                setPlaceImg(result.photoUrl)
        }

        getPlaceSAResult(placeId) {
            result ->
            saPlaceId = result[0].samonthlysummary_id

            binding.apply {
                // "분위기" 카테고리 내용으로 비율 초기화
                setRadioButton(result)

                // 카테고리 클릭 이벤트 설정
                initClickEvent(result)
            }
        }

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

    private fun initClickEvent(result: List<SAPlaceResponse>) {
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

//            mapBtn.setOnClickListener {
//                // 지도 버튼 클릭 시 이벤트
//            }

            radioButton1.setOnClickListener {
                setRadioButton(result)
            }
            radioButton2.setOnClickListener {
                setRadioButton(result)
            }
            radioButton3.setOnClickListener {
                setRadioButton(result)
            }
            radioButton4.setOnClickListener {
                setRadioButton(result)
            }
        }
    }

    private fun setReviews(category: String){
        binding.apply{
            getPlaceReviews(category){
                result ->
                Log.d("reviews", result.toString())

                    datas.clear() // 기존 데이터를 지우고 새로운 데이터로 대체
                    datas.addAll(result)
                    if(result == null){

                    }
                    adapter.notifyDataSetChanged()
            }
        }
    }

    private fun setRadioButton(result: List<SAPlaceResponse>){
        binding.apply {
            when(radioGroup.checkedRadioButtonId){
                R.id.radioButton1 -> {
                    // 분위기 선택 시
                    var reviewCount = result[0].positive + result[0].negative + result[0].neutral
                    var pos = 0.0
                    var neg = 0.0
                    var neu = 0.0

                    if(result[0].positive != 0)
                        pos = result[0].positive.toDouble()/reviewCount * 100
                    if(result[0].negative != 0)
                        neg = result[0].negative.toDouble()/reviewCount * 100
                    if(result[0].neutral != 0)
                        neu = result[0].neutral.toDouble()/reviewCount * 100

                    PosPercent.text = round(pos).toInt().toString() + "%"
                    NegPercent.text = round(neg).toInt().toString() + "%"
                    NeuPercent.text = round(neu).toInt().toString() + "%"

                    // 리뷰 불러오기
                    setReviews("C001")
                }

                R.id.radioButton2 -> {
                    // 교통 선택 시
                    var reviewCount = result[1].positive + result[1].negative + result[1].neutral
                    var pos = 0.0
                    var neg = 0.0
                    var neu = 0.0

                    if(result[1].positive != 0)
                        pos = result[1].positive.toDouble()/reviewCount * 100
                    if(result[1].negative != 0)
                        neg = result[1].negative.toDouble()/reviewCount * 100
                    if(result[1].neutral != 0)
                        neu = result[1].neutral.toDouble()/reviewCount * 100

                    PosPercent.text = round(pos).toInt().toString() + "%"
                    NegPercent.text = round(neg).toInt().toString() + "%"
                    NeuPercent.text = round(neu).toInt().toString() + "%"

                    // 리뷰 불러오기
                    setReviews("C002")
                }

                R.id.radioButton3 -> {
                    // 혼잡도 선택 시
                    var reviewCount = result[2].positive + result[2].negative + result[2].neutral
                    var pos = 0.0
                    var neg = 0.0
                    var neu = 0.0

                    if(result[2].positive != 0)
                        pos = result[2].positive.toDouble()/reviewCount * 100
                    if(result[2].negative != 0)
                        neg = result[2].negative.toDouble()/reviewCount * 100
                    if(result[2].neutral != 0)
                        neu = result[2].neutral.toDouble()/reviewCount * 100

                    PosPercent.text = round(pos).toInt().toString() + "%"
                    NegPercent.text = round(neg).toInt().toString() + "%"
                    NeuPercent.text = round(neu).toInt().toString() + "%"

                    // 리뷰 불러오기
                    setReviews("C003")
                }

                R.id.radioButton4 -> {
                    // 인프라 선택 시
                    var reviewCount = result[3].positive + result[3].negative + result[3].neutral
                    var pos = 0.0
                    var neg = 0.0
                    var neu = 0.0

                    if(result[3].positive != 0)
                        pos = result[3].positive.toDouble()/reviewCount * 100
                    if(result[3].negative != 0)
                        neg = result[3].negative.toDouble()/reviewCount * 100
                    if(result[3].neutral != 0)
                        neu = result[3].neutral.toDouble()/reviewCount * 100

                    PosPercent.text = round(pos).toInt().toString() + "%"
                    NegPercent.text = round(neg).toInt().toString() + "%"
                    NeuPercent.text = round(neu).toInt().toString() + "%"

                    // 리뷰 불러오기
                    setReviews("C004")
                }
            }
        }
    }

    // 스피너 초기화 메소드
    private fun initSpinner() {
        // 스피너 객체 생성
        val spinner: Spinner = binding.filterBtn

        // 어댑터 적용
        /*spinner.adapter = ArrayAdapter.createFromResource(this,
            R.array.monthList, R.layout.row_spinner)*/
        spinner.adapter = ArrayAdapter.createFromResource(this,
            R.array.monthList, android.R.layout.simple_spinner_dropdown_item)

        // 아이템 선택 리스너
        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.apply {
                    when(position){
                        // 전체
                        0 -> {
                            getPlaceSAResult(placeId){
                                    result ->
                                setRadioButton(result)
                            }
                        }

                        // 1월
                        1 -> {
                            getPlaceMonthlySAResult(placeId, 1){
                                    result ->
                                setRadioButton(result)
                            }
                        }

                        // 2월
                        2 -> {
                            getPlaceMonthlySAResult(placeId, 2){
                                    result ->
                                setRadioButton(result)
                            }
                        }

                        // 3월
                        3 -> {
                            getPlaceMonthlySAResult(placeId, 3){
                                    result ->
                                setRadioButton(result)
                            }
                        }

                        // 4월
                        4 -> {
                            getPlaceMonthlySAResult(placeId, 4){
                                    result ->
                                setRadioButton(result)
                            }
                        }

                        // 5월
                        5 -> {
                            getPlaceMonthlySAResult(placeId, 5){
                                    result ->
                                setRadioButton(result)
                            }
                        }

                        // 6월
                        6 -> {
                            getPlaceMonthlySAResult(placeId, 6){
                                    result ->
                                setRadioButton(result)
                            }
                        }

                        // 7월
                        7 -> {
                            getPlaceMonthlySAResult(placeId, 7){
                                    result ->
                                setRadioButton(result)
                            }
                        }

                        // 8월
                        8 -> {
                            getPlaceMonthlySAResult(placeId, 8){
                                    result ->
                                setRadioButton(result)
                            }
                        }

                        // 9월
                        9 -> {
                            getPlaceMonthlySAResult(placeId, 9){
                                    result ->
                                setRadioButton(result)
                            }
                        }

                        // 10월
                        10 -> {
                            getPlaceMonthlySAResult(placeId, 10){
                                    result ->
                                setRadioButton(result)
                            }
                        }

                        // 11월
                        11 -> {
                            getPlaceMonthlySAResult(placeId, 12){
                                    result ->
                                setRadioButton(result)
                            }
                        }

                        // 12월
                        12 -> {
                            getPlaceMonthlySAResult(placeId, 12){
                                    result ->
                                setRadioButton(result)
                            }
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }
}