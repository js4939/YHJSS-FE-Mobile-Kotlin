package com.example.yeohangjissokssok.fragment

import PlaceAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yeohangjissokssok.R
import com.example.yeohangjissokssok.activity.*
import com.example.yeohangjissokssok.api.RetrofitBuilder
import com.example.yeohangjissokssok.databinding.FragmentRecommendBinding
import com.example.yeohangjissokssok.models.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecommendFragment : Fragment() {

    // 바인딩 객체 선언
    private lateinit var binding: FragmentRecommendBinding

    var caPlaceIds = mutableListOf<Long>()
    var keyPlaceIds = mutableListOf<Long>()

    val datas = ArrayList<PlaceData>()
    var placeAdapter = PlaceAdapter(datas)

    var categorynum = 0
    var currentKeyword = ""

    var keywordList = HashMap<String, Int>()

    val name = "name"

    private var selectedImageResource: Int = 0
    private var selectedButtonIndex: Int = -1

    var isMoodClicked = false
    var isTransportClicked = false
    var isCongestionClicked = false
    var isInfraClicked = false
    var isPurposeClicked = false

    val buttonDataList = listOf(
        "가족", "관광", "꽃", "나들이", "등산", "맛집", "바다", "산책", "쇼핑",
        "아이", "야경", "연인", "운동", "전시", "친구", "홀로", "힐링"
    )

    val buttonAdapter = ButtonAdapter(buttonDataList)

    private fun getSACategoryPlace(input: String) {
        CoroutineScope(Dispatchers.Main).launch {
            RetrofitBuilder.api.getSACategoryPlace(input).enqueue(object :
                Callback<APIResponseData> {
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
                        //Log.d("result", result.toString())

                        // 받아온 placeId를 caPlaceIds 리스트에 추가
                        caPlaceIds.clear()
                        for (item in result) {
                            caPlaceIds.add(item.placeId)
                        }

                        // initRecycler 함수를 호출하여 데이터 추가
                        initRecycler(input)
                    }
                }

                override fun onFailure(call: Call<APIResponseData>, t: Throwable) {
                    Log.d("test", "연결실패")
                }
            })
        }
    }

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
                        //Log.d("saresult", result.toString())
                    }
                }

                override fun onFailure(call: Call<APIResponseData>, t: Throwable) {
                    Log.d("test", "연결실패")
                }
            })
        }
    }

    private fun getPlaceByKeyword(input: String, callback: (List<KeywordListResponse>) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            RetrofitBuilder.api.getPlaceByKeyword(input).enqueue(object : Callback<APIResponseData> {
                override fun onResponse(
                    call: Call<APIResponseData>, response: Response<APIResponseData>
                ) {
                    if (response.isSuccessful) {
                        val temp = response.body() as APIResponseData
                        val jsonResult = Gson().toJson(temp.data)
                        val result: List<KeywordListResponse> = GsonBuilder()
                            .create()
                            .fromJson(jsonResult, Array<KeywordListResponse>::class.java)
                            .toList()
                        callback(result) // 데이터를 가져온 후 콜백으로 전달

                        // 받아온 placeId를 keyPlaceIds 리스트에 추가
                        keyPlaceIds.clear()
                        for (item in result) {
                            keyPlaceIds.add(item.placeId)
                        }
                    }
                    //Log.e("response code", response.code().toString())
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentRecommendBinding.inflate(inflater, container, false)
        val view = binding.root

        val btnMood = view.findViewById<ImageView>(R.id.btn_mood)
        val btnTransport = view.findViewById<ImageView>(R.id.btn_transport)
        val btnCongestion = view.findViewById<ImageView>(R.id.btn_congestion)
        val btnInfra = view.findViewById<ImageView>(R.id.btn_infra)
        val btnPurpose = view.findViewById<ImageView>(R.id.btn_purpose)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_keywordlist)
        recyclerView.adapter = buttonAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // 초기에 선택된 이미지 리소스 ID를 저장할 변수 초기화
        selectedImageResource = R.drawable.ic_mood_selected

        // btnPurpose를 누른 경우에만 keyword_rv가 보이도록 하는 변수
        var isPurposeButtonClicked = false

        btnMood.setOnClickListener {
            if (isPurposeButtonClicked) {
                isPurposeButtonClicked = false
                val keyword_rv = view.findViewById<RecyclerView>(R.id.rv_keywordlist)
                keyword_rv.visibility = View.GONE
            }

            selectedImageResource = R.drawable.ic_mood_selected
            btnMood.setImageResource(selectedImageResource)

            btnTransport.setImageResource(R.drawable.ic_transport)
            btnCongestion.setImageResource(R.drawable.ic_congestion)
            btnInfra.setImageResource(R.drawable.ic_infra)
            btnPurpose.setImageResource(R.drawable.ic_purpose)

            // 클릭 이벤트 핸들러 내에서 getSACategoryPlace를 호출하지 않고
            // 해당 카테고리를 저장하고, 아래에서 한 번에 호출하도록 변경
            val category = "C001"
            categorynum = 0
            updateRecyclerView(category)
        }

        btnTransport.setOnClickListener {
            if (isPurposeButtonClicked) {
                isPurposeButtonClicked = false
                val keyword_rv = view.findViewById<RecyclerView>(R.id.rv_keywordlist)
                keyword_rv.visibility = View.GONE
            }

            selectedImageResource = R.drawable.ic_transport_selected
            btnTransport.setImageResource(selectedImageResource)

            btnMood.setImageResource(R.drawable.ic_mood)
            btnCongestion.setImageResource(R.drawable.ic_congestion)
            btnInfra.setImageResource(R.drawable.ic_infra)
            btnPurpose.setImageResource(R.drawable.ic_purpose)

            val category = "C002"
            categorynum = 1
            updateRecyclerView(category)
        }

        btnCongestion.setOnClickListener {
            if (isPurposeButtonClicked) {
                isPurposeButtonClicked = false
                val keyword_rv = view.findViewById<RecyclerView>(R.id.rv_keywordlist)
                keyword_rv.visibility = View.GONE
            }

            selectedImageResource = R.drawable.ic_congestion_selected
            btnCongestion.setImageResource(selectedImageResource)

            btnMood.setImageResource(R.drawable.ic_mood)
            btnTransport.setImageResource(R.drawable.ic_transport)
            btnInfra.setImageResource(R.drawable.ic_infra)
            btnPurpose.setImageResource(R.drawable.ic_purpose)

            val category = "C003"
            categorynum = 2
            updateRecyclerView(category)
        }

        btnInfra.setOnClickListener {
            if (isPurposeButtonClicked) {
                isPurposeButtonClicked = false
                val keyword_rv = view.findViewById<RecyclerView>(R.id.rv_keywordlist)
                keyword_rv.visibility = View.GONE
            }

            selectedImageResource = R.drawable.ic_infra_selected
            btnInfra.setImageResource(selectedImageResource)

            btnMood.setImageResource(R.drawable.ic_mood)
            btnTransport.setImageResource(R.drawable.ic_transport)
            btnCongestion.setImageResource(R.drawable.ic_congestion)
            btnPurpose.setImageResource(R.drawable.ic_purpose)

            val category = "C004"
            categorynum = 3
            updateRecyclerView(category)
        }

        btnPurpose.setOnClickListener {
            selectedImageResource = R.drawable.ic_purpose_selected
            btnPurpose.setImageResource(selectedImageResource)

            btnMood.setImageResource(R.drawable.ic_mood)
            btnTransport.setImageResource(R.drawable.ic_transport)
            btnCongestion.setImageResource(R.drawable.ic_congestion)
            btnInfra.setImageResource(R.drawable.ic_infra)

            // btnPurpose를 눌렀음을 표시
            isPurposeButtonClicked = true

            // 키워드 관련 액션 추가 예정
            val keyword_rv = view.findViewById<RecyclerView>(R.id.rv_keywordlist)
            keyword_rv.visibility=View.VISIBLE

            // 버튼 상태를 초기화하는 코드 (나머지 버튼은 선택 해제되도록)
            isMoodClicked = false
            isTransportClicked = false
            isCongestionClicked = false
            isInfraClicked = false

            // 나머지 버튼 이미지 업데이트
            btnMood.setImageResource(R.drawable.ic_mood)
            btnTransport.setImageResource(R.drawable.ic_transport)
            btnCongestion.setImageResource(R.drawable.ic_congestion)
            btnInfra.setImageResource(R.drawable.ic_infra)

            buttonAdapter.setOnItemClickListener(object : ButtonAdapter.OnItemClickListener {
                override fun onItemClick(item: String, position: Int) {
                    // 현재 선택한 버튼의 인덱스 저장
                    val previousSelectedIndex = selectedButtonIndex

                    // 클릭한 버튼이 이전에 선택한 버튼과 다르다면
                    if (selectedButtonIndex != position) {
                        // 이전에 선택한 버튼의 상태 초기화
                        if (previousSelectedIndex != -1) {
                            buttonAdapter.toggleItemSelection(previousSelectedIndex)
                        }

                        // 현재 선택한 버튼의 인덱스 업데이트
                        selectedButtonIndex = position

                        val buttonDataList = listOf(
                            "가족", "관광", "꽃", "나들이", "등산", "맛집", "바다", "산책", "쇼핑",
                            "아이", "야경", "연인", "운동", "전시", "친구", "홀로", "힐링"
                        )

                        // 선택한 버튼의 상태를 토글
                        buttonAdapter.toggleItemSelection(position)

                        // 선택된 아이템(item)에 대한 처리를 추가
                        when (selectedButtonIndex){
                            0 -> getPlaceByKeyword("가족"){
                                    result-> Log.d("keyPlaceIds", keyPlaceIds.toString())
                                currentKeyword = "가족"
                                setKeyword(result)
                            }
                            1 ->  getPlaceByKeyword("관광"){
                                    result-> Log.d("keyPlaceIds", keyPlaceIds.toString())
                                currentKeyword = "관광"
                                setKeyword(result)
                            }
                            2 -> getPlaceByKeyword("꽃"){
                                result-> Log.d("keyPlaceIds", keyPlaceIds.toString())
                            currentKeyword = "꽃"
                            setKeyword(result)
                            }
                            3 -> getPlaceByKeyword("나들이"){
                                result-> Log.d("keyPlaceIds", keyPlaceIds.toString())
                            currentKeyword = "나들이"
                            setKeyword(result)
                            }
                            4 -> getPlaceByKeyword("등산"){
                                    result-> Log.d("keyPlaceIds", keyPlaceIds.toString())
                                currentKeyword = "등산"
                                setKeyword(result)
                            }
                            5 -> getPlaceByKeyword("맛집"){
                                    result-> Log.d("keyPlaceIds", keyPlaceIds.toString())
                                currentKeyword = "맛집"
                                setKeyword(result)
                            }
                            6 -> getPlaceByKeyword("바다"){
                                    result-> Log.d("keyPlaceIds", keyPlaceIds.toString())
                                currentKeyword = "바다"
                                setKeyword(result)
                            }
                            7 -> getPlaceByKeyword("산책"){
                                    result-> Log.d("keyPlaceIds", keyPlaceIds.toString())
                                currentKeyword = "산책"
                                setKeyword(result)
                            }
                            8 -> getPlaceByKeyword("쇼핑"){
                                    result-> Log.d("keyPlaceIds", keyPlaceIds.toString())
                                currentKeyword = "쇼핑"
                                setKeyword(result)
                            }
                            9 -> getPlaceByKeyword("아이"){
                                    result-> Log.d("keyPlaceIds", keyPlaceIds.toString())
                                currentKeyword = "아이"
                                setKeyword(result)
                            }
                            10 -> getPlaceByKeyword("야경"){
                                    result-> Log.d("keyPlaceIds", keyPlaceIds.toString())
                                currentKeyword = "야경"
                                setKeyword(result)
                            }
                            11 -> getPlaceByKeyword("연인"){
                                    result-> Log.d("keyPlaceIds", keyPlaceIds.toString())
                                currentKeyword = "연인"
                                setKeyword(result)
                            }
                            12 -> getPlaceByKeyword("운동"){
                                    result-> Log.d("keyPlaceIds", keyPlaceIds.toString())
                                currentKeyword = "운동"
                                setKeyword(result)
                            }
                            13 -> getPlaceByKeyword("전시"){
                                    result-> Log.d("keyPlaceIds", keyPlaceIds.toString())
                                currentKeyword = "전시"
                                setKeyword(result)
                            }
                            14 -> getPlaceByKeyword("친구"){
                                    result-> Log.d("keyPlaceIds", keyPlaceIds.toString())
                                currentKeyword = "친구"
                                setKeyword(result)
                            }
                            15 -> getPlaceByKeyword("홀로"){
                                result-> Log.d("keyPlaceIds", keyPlaceIds.toString())
                            currentKeyword = "홀로"
                            setKeyword(result)
                            }
                            16 -> getPlaceByKeyword("힐링"){
                                    result-> Log.d("keyPlaceIds", keyPlaceIds.toString())
                                currentKeyword = "힐링"
                                setKeyword(result)
                            }
                        }
                    }
                }
            })
        }


        // 초기 카테고리 설정 (예: "C001")
        val initialCategory = "C001"
        categorynum = 0
        updateRecyclerView(initialCategory)

        // 리사이클러뷰 구분선 지정
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.rvRecommendlist.addItemDecoration(dividerItemDecoration)

        initClickEvent()

        return view
    }

    @SuppressLint("SuspiciousIndentation")
    private fun setKeyword(result: List<KeywordListResponse>) {
        datas.clear() // 데이터 초기화

        // placeAdapter 초기화
        binding.rvRecommendlist.adapter = placeAdapter

        var idx = 0
        while (idx < keyPlaceIds.size){
            // 장소 정보 가져오기
            val newPlaceResponse = PlaceData(
                id = result[idx].placeId,
                name = result[idx].name,
                region = result[idx].region,
                address = result[idx].address,
                photoUrl = result[idx].photoUrl,
                pos = 0.0,
                totalNum = 0,
                keywordText = currentKeyword,
                keywordNum = result[idx].keywordCount
            )

                datas.add(newPlaceResponse)
                placeAdapter.notifyItemInserted(datas.size - 1)

               idx++ // 다음 장소를 가져오기 위해 인덱스 증가
        }
    }

    private fun updateRecyclerView(category: String) {
        // 서버와 통신하여 리사이클러뷰 데이터 업데이트
        getSACategoryPlace(category)
    }

    private fun initRecycler(input: String) {
        datas.clear() // 데이터 초기화

        // placeAdapter 초기화
        binding.rvRecommendlist.adapter = placeAdapter

        var currentIndex = 0

        fun addNextPlace() {
            if (currentIndex < caPlaceIds.size) {
                val placeId = caPlaceIds[currentIndex]

                // 데이터 가져오기
                getPlaceSAResult(placeId) { result ->
                    if (input == "C001"){
                        categorynum = 0
                    }
                    else if (input == "C002"){
                        categorynum = 1
                    }
                    else if (input == "C003"){
                        categorynum = 2
                    }
                    else if (input == "C004"){
                        categorynum = 3
                    }

                    var totalnum = result[categorynum].positive + result[categorynum].negative + result[categorynum].neutral

                    // 장소 정보 가져오기
                    getPlaceById(placeId) { placeResult ->
                        val newPlaceResponse = PlaceData(
                            id = placeResult.id,
                            name = placeResult.name,
                            region = placeResult.region,
                            address = placeResult.address,
                            photoUrl = placeResult.photoUrl,
                            pos = result[categorynum].positive.toDouble() / totalnum * 100,
                            totalNum = result[categorynum].positive + result[categorynum].negative + result[categorynum].neutral
                        )

                        datas.add(newPlaceResponse)
                        placeAdapter.notifyItemInserted(datas.size - 1)

                        currentIndex++ // 다음 장소를 가져오기 위해 인덱스 증가
                        addNextPlace() // 다음 장소를 가져오도록 재귀 호출
                    }
                }
            }
        }

        // 첫 번째 장소를 가져오기 위해 호출
        addNextPlace()
    }

    private fun initClickEvent() {
        binding.apply {
            //            goBackBtn.setOnClickListener {
            //                // 뒤로가기 버튼 클릭 시 이벤트
            //                requireActivity().onBackPressed() // 현재 Fragment를 스택에서 제거
            //            }

            // adapter에 클릭리스너 부착
            // 여행지 클릭 시 이벤트
            placeAdapter.itemClicklistener =
                object : PlaceAdapter.OnItemClickListener {
                    override fun OnItemClick(position: Int) {
                        // RecommendListFragment를 호스팅하는 Activity에서 다른 Activity로 전환
                        val intent = Intent(requireActivity(), ResultActivity::class.java)
                        intent.putExtra("placeId", placeAdapter.datas[position].id)
                        intent.putExtra("region", placeAdapter.datas[position].region)
                        intent.putExtra("name", placeAdapter.datas[position].name)
                        intent.putExtra("page", "recommend")
                        startActivity(intent)
                    }
                }
        }
    }
}
