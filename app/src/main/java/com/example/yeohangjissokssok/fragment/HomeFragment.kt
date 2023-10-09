package com.example.yeohangjissokssok.fragment

import PlaceAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.yeohangjissokssok.activity.APIResponseData
import com.example.yeohangjissokssok.activity.PlaceResponse
import com.example.yeohangjissokssok.activity.ResultActivity
import com.example.yeohangjissokssok.api.RetrofitBuilder
import com.example.yeohangjissokssok.databinding.FragmentHomeBinding
import com.example.yeohangjissokssok.models.SACategoryResponse
import com.example.yeohangjissokssok.models.SAPlaceResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class HomeFragment : Fragment() {
    //lateinit var placeAdapter : PlaceAdapter

    // 바인딩 객체 선언
    private lateinit var binding: FragmentHomeBinding

    val curDate: LocalDate = LocalDate.now()
    val month = curDate.monthValue

    var caPlaceIds = mutableListOf<Long>()

    val datas = ArrayList<PlaceResponse>()
    var placeAdapter = PlaceAdapter(datas)

    var categorynum = 0

    val name = "name"

    private var totalnum: Int = 0
    private var pos: Double = 0.0

    private fun getMonthSACategoryPlace(input: String) {
        CoroutineScope(Dispatchers.Main).launch {
            RetrofitBuilder.api.getMonthSACategoryPlace(input, month).enqueue(object :
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
                        Log.d("home fragment result", result.toString())

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        Log.d("month", month.toString())

        binding.apply {
            // 현재 날짜 기준 월 설정
            monthText.text = month.toString() + "월 BEST"

            // 클릭 이벤트 핸들러 내에서 getSACategoryPlace를 호출하지 않고
            // 해당 카테고리를 저장하고, 아래에서 한 번에 호출하도록 변경
            lateinit var category:String

            radioButton1.setOnClickListener {
                // 분위기 선택 시
                category = "C001"
                categorynum = 0
                updateRecyclerView(category)
            }

            radioButton2.setOnClickListener {
                // 교통 선택 시
                category = "C002"
                categorynum = 1
                updateRecyclerView(category)
            }

            radioButton3.setOnClickListener {
                // 혼잡도 선택 시
                category = "C003"
                categorynum = 2
                updateRecyclerView(category)
            }

            radioButton4.setOnClickListener {
                // 인프라 선택 시
                category = "C004"
                categorynum = 3
                updateRecyclerView(category)
            }
        }

        // 초기 카테고리 설정 (예: "C001")
        val initialCategory = "C001"
        categorynum = 0
        updateRecyclerView(initialCategory)

        // 리사이클러뷰 구분선 지정
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.rvPlace.addItemDecoration(dividerItemDecoration)

        // 이곳에서 initClickEvent() 호출
        initClickEvent()

        return view
    }

    private fun updateRecyclerView(category: String) {
        // 서버와 통신하여 리사이클러뷰 데이터 업데이트
        getMonthSACategoryPlace(category)
    }

    private fun initRecycler() {
        datas.clear() // 데이터 초기화

        // placeAdapter 초기화
        binding.rvPlace.adapter = placeAdapter

        var currentIndex = 0

        fun addNextPlace() {
            if (currentIndex < caPlaceIds.size) {
                val placeId = caPlaceIds[currentIndex]

                // 데이터 가져오기
                getPlaceSAResult(placeId) { result ->
                    totalnum = result[categorynum].positive + result[categorynum].negative + result[categorynum].neutral
                    pos = result[categorynum].positive.toDouble() / totalnum * 100

                    placeAdapter.setTotalAndPositiveNum(totalnum, pos)

                    // 장소 정보 가져오기
                    getPlaceById(placeId) { placeResult ->
                        val newPlaceResponse = PlaceResponse(
                            id = placeResult.id,
                            name = placeResult.name,
                            region = placeResult.region,
                            address = placeResult.address,
                            latitude = placeResult.latitude,
                            longitude = placeResult.longitude,
                            photoUrl = placeResult.photoUrl
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
             //adapter에 클릭리스너 부착
             //여행지 클릭 시 이벤트
            placeAdapter.itemClicklistener =
                object : PlaceAdapter.OnItemClickListener {
                    override fun OnItemClick(position: Int) {
                        // HomeFragment를 호스팅하는 Activity에서 다른 Activity로 전환
                        val intent = Intent(requireActivity(), ResultActivity::class.java)
                        intent.putExtra("placeId", placeAdapter.datas[position].id)
                        intent.putExtra("region", placeAdapter.datas[position].region)
                        intent.putExtra("name", placeAdapter.datas[position].name)
                        intent.putExtra("page", "home")
                        Log.d("IntentData", "placeId: ${intent.getLongExtra("placeId", -1)}, region: ${intent.getStringExtra("region")}, name: ${intent.getStringExtra("name")}, page: ${intent.getStringExtra("page")}")

                        startActivity(intent)
                    }
                }
        }
    }
}