package com.example.yeohangjissokssok.activity

import PlaceAdapter
import ReviewAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.yeohangjissokssok.R
import com.example.yeohangjissokssok.R.id.fragment_home
import com.example.yeohangjissokssok.R.id.fragment_recommend
import com.example.yeohangjissokssok.api.RetrofitBuilder
import com.example.yeohangjissokssok.databinding.ActivityResultBinding
import com.example.yeohangjissokssok.fragment.HomeFragment
import com.example.yeohangjissokssok.fragment.RecommendFragment
import com.example.yeohangjissokssok.models.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Console
import java.security.MessageDigest
import java.util.Collections.max
import kotlin.math.round
import kotlin.time.Duration.Companion.seconds

class ResultActivity : AppCompatActivity() {

    lateinit var binding: ActivityResultBinding
    lateinit var adapter: ReviewAdapter
    lateinit var keywordAdapter : ButtonAdapter
    lateinit var mIntent: Intent

    private var selectedButtonIndex: Int = 0
    var placeId: Long = -1
    var saPlaceId: Long = -1
    var keywordId: Long = -1
    var monthId: Long = -1
    var currentMonth: Int = 0
    var category: String = "C001"

    var key1 = ""
    var key2 = ""
    var key3 = ""
    var currentKey = ""

    val datas = ArrayList<ReviewResponse>()
    val reviewDatas = ArrayList<ReviewData>()

    var keywordList = HashMap<String, Int>()
    var keywordArray = ArrayList<Int>(17)

    data class KeywordData(val name: String, val count: Int)
    var keywordDataList: MutableList<KeywordData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

/*        try {
            val information = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            val signatures = information.signingInfo.apkContentsSigners
            for (signature in signatures) {
                val md = MessageDigest.getInstance("SHA").apply {
                    update(signature.toByteArray())
                }
                val HASH_CODE = String(Base64.encode(md.digest(), 0))

                Log.d("hash", "HASH_CODE -> $HASH_CODE")
            }
        } catch (e: Exception) {
            Log.d("error", "Exception -> $e")
        }*/

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
    private fun getPlaceReviews(callback: (List<ReviewResponse>) -> Unit) {
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
                    else{
                        reviewDatas.clear()
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<APIResponseData>, t: Throwable) {
                    Log.d("test", "연결실패")
                }
            })
        }
    }

    // 월 별 리뷰 불러오는 메소드
    private fun getPlaceMonthReviews(id: Long, callback: (List<ReviewResponse>) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            RetrofitBuilder.api.getMonthReviews(id, category).enqueue(object : Callback<APIResponseData> {
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
                    else{
                        reviewDatas.clear()
                        adapter.notifyDataSetChanged()
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

    // 전체 키워드 개수 불러오는 메소드
    private fun getKeywordById(input: Long, callback: (PlaceKeywordResponse) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            RetrofitBuilder.api.getKeywords(input).enqueue(object : Callback<APIResponseData> {
                override fun onResponse(
                    call: Call<APIResponseData>, response: Response<APIResponseData>
                ) {
                    if (response.isSuccessful) {
                        val temp = response.body() as APIResponseData
                        val jsonResult = Gson().toJson(temp.data)
                        val result: PlaceKeywordResponse = GsonBuilder()
                            .create()
                            .fromJson(jsonResult, PlaceKeywordResponse::class.java)
                        callback(result) // 데이터를 가져온 후 콜백으로 전달
                    }
                }

                override fun onFailure(call: Call<APIResponseData>, t: Throwable) {
                    Log.d("test", "연결실패")
                }
            })
        }
    }

    // 월 별 키워드 개수 불러오는 메소드
    private fun getMonthKeyword(month: Int, callback: (PlaceKeywordResponse) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            RetrofitBuilder.api.getMonthKeywords(placeId, month).enqueue(object : Callback<APIResponseData> {
                override fun onResponse(
                    call: Call<APIResponseData>, response: Response<APIResponseData>
                ) {
                    if (response.isSuccessful) {
                        val temp = response.body() as APIResponseData
                        val jsonResult = Gson().toJson(temp.data)
                        val result: PlaceKeywordResponse = GsonBuilder()
                            .create()
                            .fromJson(jsonResult, PlaceKeywordResponse::class.java)
                        callback(result) // 데이터를 가져온 후 콜백으로 전달
                    }
                }

                override fun onFailure(call: Call<APIResponseData>, t: Throwable) {
                    Log.d("test", "연결실패")
                }
            })
        }
    }

    // 키워드 별 리뷰 불러오는 메소드
    private fun getPlaceKeywordReviews(keyword: String, callback: (List<ReviewKeywordResponse>) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            RetrofitBuilder.api.getKeywordReviews(keywordId, keyword).enqueue(object : Callback<APIResponseData> {
                override fun onResponse(
                    call: Call<APIResponseData>, response: Response<APIResponseData>
                ) {
                    if (response.isSuccessful) {
                        val temp = response.body() as APIResponseData
                        val jsonResult = Gson().toJson(temp.data)
                        val result: List<ReviewKeywordResponse> = GsonBuilder()
                            .create()
                            .fromJson(jsonResult, Array<ReviewKeywordResponse>::class.java)
                            .toList()

                        callback(result) // 데이터를 가져온 후 콜백으로 전달
                    }
                    else{
                        reviewDatas.clear()
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<APIResponseData>, t: Throwable) {
                    Log.d("test", "연결실패")
                }
            })
        }
    }

    // 키워드 별 월 별 리뷰 불러오는 메소드
    private fun getPlaceMonthKeywordReviews(id: Long, keyword: String, callback: (List<ReviewKeywordResponse>) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            RetrofitBuilder.api.getMonthKeywordReviews(id, keyword).enqueue(object : Callback<APIResponseData> {
                override fun onResponse(
                    call: Call<APIResponseData>, response: Response<APIResponseData>
                ) {
                    if (response.isSuccessful) {
                        val temp = response.body() as APIResponseData
                        val jsonResult = Gson().toJson(temp.data)
                        val result: List<ReviewKeywordResponse> = GsonBuilder()
                            .create()
                            .fromJson(jsonResult, Array<ReviewKeywordResponse>::class.java)
                            .toList()

                        callback(result) // 데이터를 가져온 후 콜백으로 전달
                    }
                    else{
                        reviewDatas.clear()
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<APIResponseData>, t: Throwable) {
                    Log.d("test", "연결실패")
                }
            })
        }
    }

    // 사진 설정 메소드
    private fun setPlaceImg(url: String){
        Glide.with(this)
            .load(url)      // 이미지 url
            .placeholder(R.drawable.baseline_update_24)  // 로딩 이미지
            .error(R.drawable.baseline_error_24)    // 에러 이미지
            .override(350, 200)
            .centerCrop()
            .into(binding.placeImg)     // 이미지 띄울 위치
    }

    private fun initLayout() {
        setVisibility(false)
        var tempList = arrayListOf<String>()
        keywordAdapter = ButtonAdapter(tempList)
        placeId = intent.getLongExtra("placeId", 0)

        getPlaceById(placeId) {
                result ->
            // map activity로 정보 전달
            mIntent = Intent(this@ResultActivity, MapActivity::class.java)
            mIntent.putExtra("name", result.name)
            mIntent.putExtra("lat", result.latitude)
            mIntent.putExtra("long", result.longitude)
            mIntent.putExtra("placeId", result.id)

            // 클릭한 장소 정보로 화면 설정
            binding.placeName.text = result.name
            binding.placeLocation.text = result.address?.substring(5, result.address.length)
            setPlaceImg(result.photoUrl)
        }

        getPlaceSAResult(placeId) {
                result ->
            saPlaceId = result[0].samonthlysummary_id
            Log.d("SA Result", result.toString())

            binding.apply {
                // "분위기" 카테고리 내용으로 비율 초기화
                setRadioButton(result)

                // 카테고리 클릭 이벤트 설정
                initClickEvent(result)
            }
        }

        getKeywordById(placeId){
                result ->
            keywordId = result.samonthlykeyword_id
            setKeywords(result)
        }

        adapter = ReviewAdapter(this.reviewDatas)
        binding.recyclerView.adapter = adapter

        adapter.datas = reviewDatas

        // recyclerView에 구분자 추가
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )

        // RecyclerView 갱신
        adapter.notifyDataSetChanged()

        binding.rvKeywordlist.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL, false)



    }

    private fun initClickEvent(result: List<SAPlaceResponse>) {
        binding.apply {
            goBackBtn.setOnClickListener {
                // 뒤로가기 버튼 클릭 시 이벤트
                /* val previousPage = intent.getStringExtra("page") as String
                Log.d("back", previousPage)

                if (previousPage == "recommend") {
                    val fragment = RecommendFragment()
                    val fragmentManager = ResultActivity().supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()

                    fragmentTransaction.replace(fragment_recommend, RecommendFragment())
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }
                else if(previousPage == "home") {
                    val fragment =  HomeFragment()
                    val fragmentManager = ResultActivity().supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()

                    fragmentTransaction.replace(fragment_home, HomeFragment())
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }*/
            }

            mapBtn.setOnClickListener {
                // 지도 버튼 클릭 시 이벤트
                startActivity(mIntent)
            }

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
            radioButton5.setOnClickListener {
                //setKeywordRadioButton()
                setRadioButton(result)
            }

//            KeywordRadioButton1.setOnClickListener {
//                setKeywordRadioButton()
//                if (currentMonth > 0){
//                    getMonthKeyword(currentMonth){
//                        result ->
//                        setKeywords(result)
//                        setKeywordMonthlyReviews(monthId)
//                    }
//                }
//                else{
//                    getKeywordById(placeId){
//                        result ->
//                        setKeywords(result)
//                        currentKey = key1
//                        setKeywordReviews(currentKey)
//                    }
//                }
//            }
//            KeywordRadioButton2.setOnClickListener {
//                setKeywordRadioButton()
//                if (currentMonth > 0){
//                    getMonthKeyword(currentMonth){
//                            result ->
//                        setKeywords(result)
//                        setKeywordMonthlyReviews(monthId)
//                    }
//                }
//                else{
//                    getKeywordById(placeId){
//                            result ->
//                        setKeywords(result)
//                        currentKey = key2
//                        setKeywordReviews(currentKey)
//                    }
//                }
//            }
//            KeywordRadioButton3.setOnClickListener {
//                setKeywordRadioButton()
//                if (currentMonth > 0){
//                    getMonthKeyword(currentMonth){
//                            result ->
//                        setKeywords(result)
//                        setKeywordMonthlyReviews(monthId)
//                    }
//                }
//                else{
//                    getKeywordById(placeId){
//                            result ->
//                        setKeywords(result)
//                        currentKey = key3
//                        setKeywordReviews(currentKey)
//
//                    }
//                }
//            }
        }
    }

    private fun initReview(result: List<ReviewResponse>){
        reviewDatas.clear() // 기존 데이터를 지우고 새로운 데이터로 대체
        var size = result.size
        var idx = 0
        while(idx < size){
            val newReview = ReviewData(
                id = result[idx].id,
                date = result[idx].date,
                content = result[idx].content,
                state = result[idx].state,
                keyword = "없음"
            )
            reviewDatas.add(newReview)
            adapter.notifyItemInserted(idx)
            idx++;
        }

        adapter.notifyDataSetChanged()
    }

    private fun initKeywordReview(result: List<ReviewKeywordResponse>){
        reviewDatas.clear() // 기존 데이터를 지우고 새로운 데이터로 대체
        var size = result.size
        var idx = 0
        while(idx < size){
            val newReview = ReviewData(
                id = result[idx].id,
                date = result[idx].date,
                content = result[idx].content,
                state = -1,
                keyword = result[idx].keyword
            )
            reviewDatas.add(newReview)
            adapter.notifyItemInserted(idx)
            idx++;
        }

        adapter.notifyDataSetChanged()
    }

    private fun setReviews(){
        binding.apply{
            getPlaceReviews() { result ->
                initReview(result)
            }
        }
    }

    private fun setMonthlyReviews(id: Long){
        getPlaceMonthReviews(id) { result ->
            initReview(result)
        }
    }

    private fun setKeywordReviews(keyword: String){
        getPlaceKeywordReviews(keyword) {
                result ->
            initKeywordReview(result)
        }
    }

    private fun setKeywordMonthlyReviews(id: Long){
        getPlaceMonthKeywordReviews(id, currentKey){ result ->
            initKeywordReview(result)
        }
    }

    private fun setVisibility(isPurpose: Boolean) {
        binding.apply {
            if (!isPurpose) {
                keywordText.visibility = View.GONE
                rvKeywordlist.visibility = View.GONE
//                KeywordRadioGroup.visibility = View.GONE
//                KeywordRadioButton1.visibility = View.GONE
//                KeywordRadioButton2.visibility = View.GONE
//                KeywordRadioButton3.visibility = View.GONE

                PosText.visibility = View.VISIBLE
                NegText.visibility = View.VISIBLE
                NeuText.visibility = View.VISIBLE

                PosPercent.visibility = View.VISIBLE
                NegPercent.visibility = View.VISIBLE
                NeuPercent.visibility = View.VISIBLE

                PosBar.visibility = View.VISIBLE
                NegBar.visibility = View.VISIBLE
                NeuBar.visibility = View.VISIBLE
            }
            else {
                keywordText.visibility = View.VISIBLE
                rvKeywordlist.visibility = View.VISIBLE
//                KeywordRadioGroup.visibility = View.VISIBLE
//                KeywordRadioButton1.visibility = View.VISIBLE
//                KeywordRadioButton2.visibility = View.VISIBLE
//                KeywordRadioButton3.visibility = View.VISIBLE

                PosText.visibility = View.GONE
                NegText.visibility = View.GONE
                NeuText.visibility = View.GONE

                PosPercent.visibility = View.GONE
                NegPercent.visibility = View.GONE
                NeuPercent.visibility = View.GONE

                PosBar.visibility = View.GONE
                NegBar.visibility = View.GONE
                NeuBar.visibility = View.GONE
            }
        }
    }

    private fun setKeywords(keywordResult: PlaceKeywordResponse){
        //Log.d("keywordResu;t", keywordResult.toString())
        binding.apply {
            keywordList.clear()
            keywordArray.clear()

            keywordList.put("홀로", keywordResult.aloneCount)
            keywordList.put("바다", keywordResult.beachCount)
            keywordList.put("연인", keywordResult.coupleCount)
            keywordList.put("운동", keywordResult.exerciseCount)
            keywordList.put("가족", keywordResult.familyCount)
            keywordList.put("꽃", keywordResult.flowerCount)
            keywordList.put("맛집", keywordResult.foodCount)
            keywordList.put("친구", keywordResult.friendCount)
            keywordList.put("힐링", keywordResult.healingCount)
            keywordList.put("등산", keywordResult.hikingCount)
            keywordList.put("아이", keywordResult.kidsCount)
            keywordList.put("전시", keywordResult.museumCount)
            keywordList.put("야경", keywordResult.nightViewCount)
            keywordList.put("나들이", keywordResult.picnicCount)
            keywordList.put("쇼핑", keywordResult.shoppingCount)
            keywordList.put("관광", keywordResult.tourCount)
            keywordList.put("산책", keywordResult.walkCount)

            var keywords = keywordList.toList().sortedByDescending { it.second }.toMap().toMutableMap()
            for((key, value) in keywords){
                keywordArray.add(value)
                //Log.d("keyword", key + " " + value)
            }

            keywordDataList = keywords.entries
                .sortedByDescending { it.value }
                .filter { it.value != 0 } // 값이 0이 아닌 엔트리만 필터링
                .map { entry ->
                    KeywordData(entry.key, + entry.value)
                }
                .toMutableList()

            var buttonDataList = keywordDataList.map { entry ->
                entry.name + " (" +entry.count + ")"
            }

            keywordAdapter = ButtonAdapter(buttonDataList)

            binding.rvKeywordlist.adapter = keywordAdapter

            var previousSelectedIndex = 0
            var selectedButtonIndex = 0

            if (buttonDataList.isNotEmpty()) {
                //Log.d("button",buttonDataList.size.toString())
                keywordAdapter.toggleItemSelection(selectedButtonIndex)
                keywordAdapter.setOnItemClickListener(object : ButtonAdapter.OnItemClickListener {
                    override fun onItemClick(item: String, position: Int) {
                        //Log.d("setKeywordItemListener", "hi")
                        currentKey = keywordDataList[position].name
                        previousSelectedIndex = selectedButtonIndex

                        // 클릭한 버튼이 이전에 선택한 버튼과 다르다면
                        if (selectedButtonIndex != position) {
                            // 이전에 선택한 버튼의 상태 초기화
                            if (previousSelectedIndex != -1) {
                                keywordAdapter.toggleItemSelection(previousSelectedIndex)
                            }

                            // 현재 선택한 버튼의 인덱스 업데이트
                            selectedButtonIndex = position

                            keywordAdapter.toggleItemSelection(position)
                        }
                        if(currentMonth > 0)
                            setKeywordMonthlyReviews(monthId)
                        else
                            setKeywordReviews(currentKey)
                    }
                })
                currentKey = keywordDataList[0].name
                if (currentMonth > 0)
                    setKeywordMonthlyReviews(monthId)
                else
                    setKeywordReviews(currentKey)
            }

            keywordAdapter.notifyDataSetChanged()

//            for((key, value) in keywordList){
//                if(keywordArray.max() == value) {
//                    key1 = key
//                    KeywordRadioButton1.text = key + " " + keywordArray.max()
//                    keywordArray.remove(value)
//                    keywordList.remove(key)
//                    break
//                }
//            }
//
//            for((key, value) in keywordList){
//                if(keywordArray.max() == value) {
//                    key2 = key
//                    KeywordRadioButton2.text = key + " " + keywordArray.max()
//                    keywordArray.remove(value)
//                    keywordList.remove(key)
//                    break
//                }
//            }
//
//            for((key, value) in keywordList){
//                if(keywordArray.max() == value) {
//                    key3 = key
//                    KeywordRadioButton3.text = key + " " + keywordArray.max()
//                    keywordArray.remove(value)
//                    keywordList.remove(key)
//                    break
//                }
//            }
        }
    }

//    private fun setKeywordRadioButton(){
//        binding.apply {
//            when(KeywordRadioGroup.checkedRadioButtonId){
//                R.id.KeywordRadioButton1 -> {
//                    currentKey = key1
//                    if(currentMonth > 0)
//                        setKeywordMonthlyReviews(monthId)
//                    else
//                        setKeywordReviews(currentKey)
//                }
//                R.id.KeywordRadioButton2 -> {
//                    currentKey = key2
//                    if(currentMonth > 0)
//                        setKeywordMonthlyReviews(monthId)
//                    else
//                        setKeywordReviews(currentKey)
//
//                }
//                R.id.KeywordRadioButton3 -> {
//                    currentKey = key3
//                    if(currentMonth > 0)
//                        setKeywordMonthlyReviews(monthId)
//                    else
//                        setKeywordReviews(currentKey)
//                }
//            }
//        }
//    }


    private fun setRadioButton(result: List<SAPlaceResponse>){
        binding.apply {
            when(radioGroup.checkedRadioButtonId){
                R.id.radioButton1 -> {
                    setVisibility(false)

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
                    NeuPercent.text = (100-round(pos)-round(neg)).toInt().toString() + "%"

                    PosBar.progress = round(pos).toInt()
                    NegBar.progress = round(neg).toInt()
                    NeuBar.progress = (100-round(pos)-round(neg)).toInt()

                    // 리뷰 불러오기
                    if(category != "C001" && monthId == -1.toLong()){
                        category = "C001"
                        setReviews()
                    }
                    else if(monthId != -1.toLong()){
                        category = "C001"
                        setMonthlyReviews(monthId)
                    }
                }

                R.id.radioButton2 -> {
                    setVisibility(false)

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
                    NeuPercent.text = (100-round(pos)-round(neg)).toInt().toString() + "%"

                    PosBar.progress = round(pos).toInt()
                    NegBar.progress = round(neg).toInt()
                    NeuBar.progress = (100-round(pos)-round(neg)).toInt()

                    // 리뷰 불러오기
                    if(category != "C002" && monthId == -1.toLong()){
                        category = "C002"
                        setReviews()
                    }
                    else if(monthId != -1.toLong()){
                        category = "C002"
                        setMonthlyReviews(monthId)
                    }
                }

                R.id.radioButton3 -> {
                    setVisibility(false)

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
                    NeuPercent.text = (100-round(pos)-round(neg)).toInt().toString() + "%"

                    PosBar.progress = round(pos).toInt()
                    NegBar.progress = round(neg).toInt()
                    NeuBar.progress = (100-round(pos)-round(neg)).toInt()

                    // 리뷰 불러오기
                    if(category != "C003" && monthId == -1.toLong()){
                        category = "C003"
                        setReviews()
                    }
                    else if(monthId != -1.toLong()){
                        category = "C003"
                        setMonthlyReviews(monthId)
                    }
                }

                R.id.radioButton4 -> {
                    setVisibility(false)

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
                    NeuPercent.text = (100-round(pos)-round(neg)).toInt().toString() + "%"

                    PosBar.progress = round(pos).toInt()
                    NegBar.progress = round(neg).toInt()
                    NeuBar.progress = (100-round(pos)-round(neg)).toInt()

                    // 리뷰 불러오기
                    if(category != "C004" && monthId == -1.toLong()){
                        category = "C004"
                        setReviews()
                    }
                    else if(monthId != -1.toLong()){
                        category = "C004"
                        setMonthlyReviews(monthId)
                    }
                }

                R.id.radioButton5 -> {
                    setVisibility(true)
                    if(category != "keyword" && monthId == -1.toLong()){
                        category = "keyword"
                        getKeywordById(placeId) {
                                keyResult ->
                            setKeywords(keyResult)
                            setKeywordReviews(currentKey)
                        }
                    }
                    else if(monthId != -1.toLong()){
                        category = "keyword"
                        getMonthKeyword(currentMonth) {
                                keyResult ->
                            setKeywords(keyResult)
                            setKeywordMonthlyReviews(monthId)
                        }
                    }
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
                                monthId = -1
                                currentMonth = 0
                                initClickEvent(result)
                                setRadioButton(result)
                                if(category == "keyword"){
                                    getKeywordById(placeId) {
                                            keyResult ->
                                        setKeywords(keyResult)
                                        //setKeywordRadioButton()
                                        setKeywordReviews(currentKey)
                                    }
                                }
                                else
                                    setReviews()
                            }
                        }

                        // 1월
                        1 -> {
                            getPlaceMonthlySAResult(placeId, 1){
                                    result ->
                                monthId = result[0].samonthlysummary_id
                                currentMonth = 1
                                initClickEvent(result)
                                setRadioButton(result)
                            }
                            if(category == "keyword"){
                                getMonthKeyword(1){
                                        keyResult ->
                                    setKeywords(keyResult)
                                    //setKeywordRadioButton()
                                    setKeywordMonthlyReviews(monthId)
                                }
                            }
                            else
                                setMonthlyReviews(monthId)
                        }

                        // 2월
                        2 -> {
                            getPlaceMonthlySAResult(placeId, 2){
                                    result ->
                                monthId = result[0].samonthlysummary_id
                                currentMonth = 2
                                initClickEvent(result)
                                setRadioButton(result)
                            }

                            if(category == "keyword"){
                                getMonthKeyword(2){
                                        keyResult ->
                                    setKeywords(keyResult)
                                    //setKeywordRadioButton()
                                    setKeywordMonthlyReviews(monthId)
                                }
                            }
                            else
                                setMonthlyReviews(monthId)
                        }

                        // 3월
                        3 -> {
                            getPlaceMonthlySAResult(placeId, 3){
                                    result ->
                                monthId = result[0].samonthlysummary_id
                                currentMonth = 3
                                initClickEvent(result)
                                setRadioButton(result)
                            }
                            if(category == "keyword"){
                                getMonthKeyword(3){
                                        keyResult ->
                                    setKeywords(keyResult)
                                    //setKeywordRadioButton()
                                    setKeywordMonthlyReviews(monthId)
                                }
                            }
                            else
                                setMonthlyReviews(monthId)
                        }

                        // 4월
                        4 -> {
                            getPlaceMonthlySAResult(placeId, 4){
                                    result ->
                                monthId = result[0].samonthlysummary_id
                                currentMonth = 4
                                initClickEvent(result)
                                setRadioButton(result)
                            }
                            if(category == "keyword"){
                                getMonthKeyword(4){
                                        keyResult ->
                                    setKeywords(keyResult)
                                    //setKeywordRadioButton()
                                    setKeywordMonthlyReviews(monthId)
                                }
                            }
                            else
                                setMonthlyReviews(monthId)
                        }

                        // 5월
                        5 -> {
                            getPlaceMonthlySAResult(placeId, 5){
                                    result ->
                                monthId = result[0].samonthlysummary_id
                                currentMonth = 5
                                initClickEvent(result)
                                setRadioButton(result)
                            }
                            if(category == "keyword"){
                                getMonthKeyword(5){
                                        keyResult ->
                                    setKeywords(keyResult)
                                    //setKeywordRadioButton()
                                    setKeywordMonthlyReviews(monthId)
                                }
                            }
                            else
                                setMonthlyReviews(monthId)
                        }

                        // 6월
                        6 -> {
                            getPlaceMonthlySAResult(placeId, 6){
                                    result ->
                                monthId = result[0].samonthlysummary_id
                                currentMonth = 6
                                initClickEvent(result)
                                setRadioButton(result)
                            }
                            if(category == "keyword"){
                                getMonthKeyword(6){
                                        keyResult ->
                                    setKeywords(keyResult)
                                    //setKeywordRadioButton()
                                    setKeywordMonthlyReviews(monthId)
                                }
                            }
                            else
                                setMonthlyReviews(monthId)
                        }

                        // 7월
                        7 -> {
                            getPlaceMonthlySAResult(placeId, 7){
                                    result ->
                                monthId = result[0].samonthlysummary_id
                                currentMonth = 7
                                initClickEvent(result)
                                setRadioButton(result)
                            }
                            if(category == "keyword"){
                                getMonthKeyword(7){
                                        keyResult ->
                                    setKeywords(keyResult)
                                    //setKeywordRadioButton()
                                    setKeywordMonthlyReviews(monthId)
                                }
                            }
                            else
                                setMonthlyReviews(monthId)
                        }

                        // 8월
                        8 -> {
                            getPlaceMonthlySAResult(placeId, 8){
                                    result ->
                                monthId = result[0].samonthlysummary_id
                                currentMonth = 8
                                initClickEvent(result)
                                setRadioButton(result)
                            }
                            if(category == "keyword"){
                                getMonthKeyword(8){
                                        keyResult ->
                                    setKeywords(keyResult)
                                    //setKeywordRadioButton()
                                    setKeywordMonthlyReviews(monthId)
                                }
                            }
                            else
                                setMonthlyReviews(monthId)
                        }

                        // 9월
                        9 -> {
                            getPlaceMonthlySAResult(placeId, 9){
                                    result ->
                                monthId = result[0].samonthlysummary_id
                                currentMonth = 9
                                initClickEvent(result)
                                setRadioButton(result)
                            }
                            if(category == "keyword"){
                                getMonthKeyword(9){
                                        keyResult ->
                                    setKeywords(keyResult)
                                    //setKeywordRadioButton()
                                    setKeywordMonthlyReviews(monthId)
                                }
                            }
                            else
                                setMonthlyReviews(monthId)
                        }

                        // 10월
                        10 -> {
                            getPlaceMonthlySAResult(placeId, 10){
                                    result ->
                                monthId = result[0].samonthlysummary_id
                                currentMonth = 10
                                initClickEvent(result)
                                setRadioButton(result)
                            }
                            if(category == "keyword"){
                                getMonthKeyword(10){
                                        keyResult ->
                                    setKeywords(keyResult)
                                    //setKeywordRadioButton()
                                    setKeywordMonthlyReviews(monthId)
                                }
                            }
                            else
                                setMonthlyReviews(monthId)
                        }

                        // 11월
                        11 -> {
                            getPlaceMonthlySAResult(placeId, 11){
                                    result ->
                                monthId = result[0].samonthlysummary_id
                                currentMonth = 11
                                initClickEvent(result)
                                setRadioButton(result)
                            }
                            if(category == "keyword"){
                                getMonthKeyword(11){
                                        keyResult ->
                                    setKeywords(keyResult)
                                    //setKeywordRadioButton()
                                    setKeywordMonthlyReviews(monthId)
                                }
                            }
                            else
                                setMonthlyReviews(monthId)
                        }

                        // 12월
                        12 -> {
                            getPlaceMonthlySAResult(placeId, 12){
                                    result ->
                                monthId = result[0].samonthlysummary_id
                                currentMonth = 12
                                initClickEvent(result)
                                setRadioButton(result)
                            }
                            if(category == "keyword"){
                                getMonthKeyword(12){
                                        keyResult ->
                                    setKeywords(keyResult)
                                    //setKeywordRadioButton()
                                    setKeywordMonthlyReviews(monthId)
                                }
                            }
                            else
                                setMonthlyReviews(monthId)
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }
}