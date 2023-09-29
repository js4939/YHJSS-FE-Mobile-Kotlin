package com.example.yeohangjissokssok.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.yeohangjissokssok.R
import com.example.yeohangjissokssok.activity.PlaceAdapterRecommend
import com.example.yeohangjissokssok.activity.SearchAdapter
import com.example.yeohangjissokssok.databinding.FragmentSearchBinding
import com.example.yeohangjissokssok.databinding.SearchRecyclerBinding
import java.util.zip.Inflater

class SearchFragment : Fragment() {
    lateinit var searchAdapter: SearchAdapter

    // 바인딩 객체 선언
    private lateinit var binding: FragmentSearchBinding
    private lateinit var recyclerViewBinding: SearchRecyclerBinding

    val datas = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        recyclerViewBinding = SearchRecyclerBinding.inflate(inflater, container, false)
        val view = binding.root

        // 데이터 추가
        datas.add("서울")
        datas.add("부산")

        searchAdapter = SearchAdapter(this.datas)
        binding.recyclerView.adapter = searchAdapter

        // RecyclerView 어댑터에 데이터 설정
        searchAdapter.datas = datas

        // RecyclerView 갱신
        searchAdapter.notifyDataSetChanged()

        initClickEvent()

        return view
    }

    private fun initClickEvent() {
        binding.apply {
            goBackBtn.setOnClickListener{
                // 뒤로가기 버튼 클릭 리스너
            }

            searchBtn.setOnClickListener{
                // 찾기 버튼 클릭 리스너
            }

        }

        recyclerViewBinding.apply {
            searchText.setOnClickListener {
                // 리사이클러뷰 data 클릭 리스너
            }

            deleteBtn.setOnClickListener {
                // 리사이클러뷰 삭제 버튼 클릭 리스너
            }
        }
    }
}




