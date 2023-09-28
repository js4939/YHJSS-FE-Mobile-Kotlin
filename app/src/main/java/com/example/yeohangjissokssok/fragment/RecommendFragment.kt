package com.example.yeohangjissokssok.fragment

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
import com.example.yeohangjissokssok.R
import com.example.yeohangjissokssok.activity.APIResponseData
import com.example.yeohangjissokssok.activity.PlaceAdapterRecommend
import com.example.yeohangjissokssok.activity.ResultActivity
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RecommendFragment : Fragment() {

    lateinit var placeAdapterRecommend: PlaceAdapterRecommend

    // 바인딩 객체 선언
    private lateinit var binding: FragmentRecommendBinding

    val datas = ArrayList<SACategoryResponse>()

    var placeId = 3
    var region = "region"
    val name = "name"
    val positiveNumber = 1
    val totalNumber = 1
    val proportion = 0.001

    private var selectedImageResource: Int = 0
    var isMoodClicked = false
    var isTransportClicked = false
    var isCongestionClicked = false
    var isInfraClicked = false
    var isPurposeClicked = false


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

        // 초기에 선택된 이미지 리소스 ID를 저장할 변수 초기화
        selectedImageResource = R.drawable.ic_mood_selected

        btnMood.setOnClickListener {
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

            // 키워드 관련 액션 추가 예정
        }

        // 초기 카테고리 설정 (예: "C001")
        val initialCategory = "C001"
        updateRecyclerView(initialCategory)

        placeAdapterRecommend = PlaceAdapterRecommend(this.datas)
        binding.rvRecommendlist.adapter = placeAdapterRecommend

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


    private fun initRecycler(result: List<SACategoryResponse>) {
        // 데이터 추가
        datas.clear() // 데이터 초기화
        datas.addAll(result)

        // RecyclerView 어댑터에 데이터 설정
        placeAdapterRecommend.datas = datas

        // RecyclerView 갱신
        placeAdapterRecommend.notifyDataSetChanged()
    }


    private fun initClickEvent() {
        binding.apply {
            //            goBackBtn.setOnClickListener {
            //                // 뒤로가기 버튼 클릭 시 이벤트
            //                requireActivity().onBackPressed() // 현재 Fragment를 스택에서 제거
            //            }

            // adapter에 클릭리스너 부착
            // 여행지 클릭 시 이벤트
            placeAdapterRecommend.itemClicklistener =
                object : PlaceAdapterRecommend.OnItemClickListener {
                    override fun OnItemClick(position: Int) {
                        // RecommendListFragment를 호스팅하는 Activity에서 다른 Activity로 전환
                        val intent = Intent(requireActivity(), ResultActivity::class.java)
                        intent.putExtra("placeId", placeAdapterRecommend.datas[position].placeId)
                        intent.putExtra("region", placeAdapterRecommend.datas[position].region)
                        intent.putExtra("name", placeAdapterRecommend.datas[position].name)
                        intent.putExtra(
                            "positiveNumber",
                            placeAdapterRecommend.datas[position].positiveNumber
                        )
                        intent.putExtra(
                            "totalNumber",
                            placeAdapterRecommend.datas[position].totalNumber
                        )
                        intent.putExtra(
                            "proportion",
                            placeAdapterRecommend.datas[position].proportion
                        )
                        intent.putExtra("page", "placeList")
                        startActivity(intent)
                    }
                }
        }
    }
}
