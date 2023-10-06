package com.example.yeohangjissokssok.fragment

import PlaceAdapter
import android.content.Intent
import android.os.Build.VERSION_CODES.P
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yeohangjissokssok.R
import com.example.yeohangjissokssok.activity.*
import com.example.yeohangjissokssok.api.RetrofitBuilder
import com.example.yeohangjissokssok.databinding.FragmentRecommendBinding
import com.example.yeohangjissokssok.models.SACategoryResponse
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

    val datas = ArrayList<PlaceResponse>()
    val placeAdapter = PlaceAdapter(datas)

    var placeId = 3
    var region = "region"
    val name = "name"
    val positiveNumber = 1
    val totalNumber = 1
    val proportion = 0.001

    private var selectedImageResource: Int = 0
    private var selectedButtonIndex: Int = -1

    var isMoodClicked = false
    var isTransportClicked = false
    var isCongestionClicked = false
    var isInfraClicked = false
    var isPurposeClicked = false

    val buttonDataList = listOf(
        "힐링", "운동", "전시", "산책", "홀로", "친구", "가족", "커플", "나들이", "효도",
        "꽃", "바다", "데이트", "야경", "맛집", "휴식", "소개팅", "쇼핑", "아이", "등산"
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
                        Log.d("result", result.toString())

                        // 받아온 placeId를 caPlaceIds 리스트에 추가
                        caPlaceIds.clear()
                        for (item in result) {
                            caPlaceIds.add(item.placeId)
                        }

                        // initRecycler 함수를 호출하여 데이터 추가
                        initRecycler()
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

                        // 선택한 버튼의 상태를 토글
                        buttonAdapter.toggleItemSelection(position)

                        // 선택된 아이템(item)에 대한 처리를 추가
                    }
                }
            })
        }

        // 초기 카테고리 설정 (예: "C001")
        val initialCategory = "C001"
        updateRecyclerView(initialCategory)

        // 리사이클러뷰 구분선 지정
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.rvRecommendlist.addItemDecoration(dividerItemDecoration)

        initClickEvent()

        return view
    }

    private fun updateRecyclerView(category: String) {
        // 서버와 통신하여 리사이클러뷰 데이터 업데이트
        getSACategoryPlace(category)
    }

    private fun initRecycler() {
        datas.clear() // 데이터 초기화

        // placeAdapter 초기화
        //placeAdapter = PlaceAdapter(datas)
        binding.rvRecommendlist.adapter = placeAdapter

        for (placeId in caPlaceIds) {
            getPlaceById(placeId) { result ->
                val newPlaceResponse = PlaceResponse(
                    id = result.id,
                    name = result.name,
                    region = result.region,
                    address = result.address,
                    latitude = result.latitude,
                    longitude = result.longitude,
                    photoUrl = result.photoUrl
                )

                datas.add(newPlaceResponse)
                placeAdapter.notifyDataSetChanged()
                //Log.d("carecycler", result.toString())
            }
        }
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
//                        intent.putExtra(
//                            "positiveNumber",
//                            placeAdapter.datas[position].
//                        )
//                        intent.putExtra(
//                            "totalNumber",
//                            placeAdapterRecommend.datas[position].totalNumber
//                        )
//                        intent.putExtra(
//                            "proportion",
//                            placeAdapterRecommend.datas[position].proportion
//                        )
                        intent.putExtra("page", "placeList")
                        startActivity(intent)
                    }
                }
        }
    }
}
