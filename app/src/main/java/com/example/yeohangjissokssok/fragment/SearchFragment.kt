package com.example.yeohangjissokssok.fragment

import PlaceSearchAdapter
import android.content.Intent
import android.os.Bundle
import android.text.method.TextKeyListener.clear
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.yeohangjissokssok.activity.*
import com.example.yeohangjissokssok.api.RetrofitBuilder
import com.example.yeohangjissokssok.databinding.FragmentSearchBinding
import com.example.yeohangjissokssok.databinding.SearchRecyclerBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {
    lateinit var searchAdapter: SearchAdapter

    // 바인딩 객체 선언
    private lateinit var binding: FragmentSearchBinding
    private lateinit var recyclerViewBinding: SearchRecyclerBinding

    val placeDatas = ArrayList<PlaceResponse>()
    val datas = ArrayList<String>()
    var placeSearchAdapter = PlaceSearchAdapter(placeDatas)

    var name = "name"

    private lateinit var searchEditText: EditText
    private lateinit var searchBtnInplacelist: ImageView

    lateinit var dividerItemDecoration:DividerItemDecoration

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        recyclerViewBinding = SearchRecyclerBinding.inflate(inflater, container, false)
        val view = binding.root

        datas.add("종로")
        datas.add("수원")
        datas.add("서울")

        // 검색 버튼 누르기 전 최근 검색 화면
        searchAdapter = SearchAdapter(this.datas)
        binding.recyclerView.adapter = searchAdapter

        // RecyclerView 어댑터에 데이터 설정
        searchAdapter.datas = datas

        initClickEvent()

        return view
    }

    private fun initClickEvent() {
        searchAdapter.itemClicklistener =
            object : SearchAdapter.OnItemClickListener {
                // 최근 검색 리사이클러뷰 data 클릭 리스너
                override fun OnItemClick(position: Int) {
                    binding.recyclerView.adapter = placeSearchAdapter

                    // RecyclerView를 처음에는 숨김(GONE) 상태로 설정
                    binding.recyclerView.visibility = View.GONE

                    // 리사이클러뷰 구분선 지정
                    dividerItemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
                    binding.recyclerView.addItemDecoration(dividerItemDecoration)

                    var searchText = searchAdapter.datas[position]

                    if (searchText.isNotEmpty()) {
                        fetchPlacesByName(searchText)

                        initPlaceRecycler()

                        binding.searchText.setText(searchText)
                        binding.searchText.setSelection(0, searchText.length)

                        // datas에 해당 검색어가 이미 존재하면 삭제한 후 추가
                        if(datas.contains(searchText))
                            datas.remove(searchText)
                        datas.add(0, searchText)
                        searchAdapter.notifyDataSetChanged()

                        // 리사이클러뷰를 보이도록 변경
                        binding.recyclerView.visibility = View.VISIBLE
                    }
                    else {
                        binding.recyclerView.visibility = View.GONE
                        placeDatas.clear() // 기존 데이터를 지움
                        placeSearchAdapter.notifyDataSetChanged()
                    }

                    // "최근 검색" 글씨 제거
                    binding.recentRecord.visibility = View.GONE
                }

                // 최근 검색 리사이클러뷰 data 삭제 버튼 클릭 리스너
                override fun DeleteClick(pos: Int) {
                    searchAdapter.removeData(pos)
                }
            }

        placeSearchAdapter.itemClicklistener =
            object : PlaceSearchAdapter.OnItemClickListener {
                override fun OnItemClick(position: Int) {
                    // SearchFragment를 호스팅하는 Activity에서 다른 Activity로 전환
                    Log.d("전환", "전환")
                    val intent = Intent(requireActivity(), ResultActivity::class.java)
                    intent.putExtra("placeId", placeSearchAdapter.datas[position].id)
                    intent.putExtra("region", placeSearchAdapter.datas[position].region)
                    intent.putExtra("name", placeSearchAdapter.datas[position].name)
                    intent.putExtra("page", "search")
                    startActivity(intent)
                }
            }

        // 뒤로가기 버튼 클릭 리스너
        binding.apply {
            goBackBtn.setOnClickListener {
                // 기본 검색 화면으로 설정
                binding.searchText.setText("")
                initSearchRecycler()

                // "최근 검색" 글씨 생성
                binding.recentRecord.visibility = View.VISIBLE

                // 리사이클러뷰 구분선 제거
                binding.recyclerView.removeItemDecoration(dividerItemDecoration)

                binding.noneImg.visibility = View.GONE
                binding.noneText.visibility = View.GONE
            }

            // 검색 버튼 클릭 리스너
            searchBtn.setOnClickListener {
                // 첫 data 검색 시 지막 구분선 및 "최근 검색" 추가
                if(datas.isEmpty()) {
                    binding.recentRecord.visibility = View.VISIBLE
                }

                searchEditText = binding.searchText
                searchBtnInplacelist = binding.searchBtn

                binding.recyclerView.adapter = placeSearchAdapter

                // RecyclerView를 처음에는 숨김(GONE) 상태로 설정
                binding.recyclerView.visibility = View.GONE

                // 리사이클러뷰 구분선 지정
                dividerItemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
                binding.recyclerView.addItemDecoration(dividerItemDecoration)

                initPlaceRecycler()

                val searchText = binding.searchText.text.toString()
                if (searchText.isNotEmpty()) {
                    fetchPlacesByName(searchText)

                    initPlaceRecycler()

                    // datas에 해당 검색어가 이미 존재하면 삭제한 후 추가
                    if(datas.contains(searchText))
                        datas.remove(searchText)
                    datas.add(0, searchText)
                    searchAdapter.notifyDataSetChanged()

                    // 검색 버튼 클릭 시 리사이클러뷰를 보이도록 변경
                    binding.recyclerView.visibility = View.VISIBLE
                }
                else {
                    binding.recyclerView.visibility = View.GONE
                    placeDatas.clear() // 기존 데이터를 지움
                    placeSearchAdapter.notifyDataSetChanged()
                }

                // "최근 검색" 글씨 제거
                recentRecord.visibility = View.GONE
            }
        }
    }

    private fun initSearchRecycler() {
        searchAdapter = SearchAdapter(datas)
        binding.recyclerView.adapter = searchAdapter

        // RecyclerView 어댑터에 데이터 설정
        searchAdapter.datas = datas

        // RecyclerView 갱신
        searchAdapter.notifyDataSetChanged()
        initClickEvent()
    }

    private fun initPlaceRecycler() {
        // 어댑터에 데이터 설정
        placeSearchAdapter = PlaceSearchAdapter(placeDatas)

        placeSearchAdapter.datas = placeDatas

        // RecyclerView를 갱신
        placeSearchAdapter.notifyDataSetChanged()
    }

    private fun fetchPlacesByName(searchText: String) {
        // 데이터를 가져온 후 어댑터에 데이터 설정
        placeDatas.clear()       // 기존 데이터를 지움
        placeSearchAdapter.notifyDataSetChanged()
        binding.recyclerView.visibility = View.GONE
        binding.noneImg.visibility = View.VISIBLE
        binding.noneText.visibility = View.VISIBLE

        getPlaceByName(searchText) { result ->
            if(!result.isNullOrEmpty()) {
                // 리사이클러뷰를 보이도록 변경
                binding.recyclerView.visibility = View.VISIBLE
                binding.noneImg.visibility = View.GONE
                binding.noneText.visibility = View.GONE

                placeDatas.clear() // 기존 데이터를 지우고 새로운 데이터로 대체
                placeDatas.addAll(result)
                placeSearchAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun getPlaceByName(input: String, callback: (List<PlaceResponse>) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            RetrofitBuilder.api.getPlaceByName(input).enqueue(object : Callback<APIResponseData> {
                override fun onResponse(
                    call: Call<APIResponseData>, response: Response<APIResponseData>
                ) {
                    if (response.isSuccessful) {
                        val temp = response.body() as APIResponseData
                        val jsonResult = Gson().toJson(temp.data)
                        val result: List<PlaceResponse> = GsonBuilder()
                            .create()
                            .fromJson(jsonResult, Array<PlaceResponse>::class.java)
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
}




