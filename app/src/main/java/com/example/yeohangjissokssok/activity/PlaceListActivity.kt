package com.example.yeohangjissokssok.activity

import PlaceAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.yeohangjissokssok.R
import com.example.yeohangjissokssok.api.RetrofitBuilder
import com.example.yeohangjissokssok.databinding.ActivityPlaceListBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceListActivity : AppCompatActivity() {

    lateinit var placeAdapter: PlaceAdapter

    // 바인딩 객체 선언
    private lateinit var binding: ActivityPlaceListBinding

    val datas = ArrayList<PlaceResponse>()


    var id = 3
    var name = "name"
    var region = "region"
    var address = "address"
    var body = ""

    private lateinit var searchEditText: EditText
    private lateinit var searchBtnInplacelist: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        searchEditText = findViewById(R.id.searchEditText)
        searchBtnInplacelist = findViewById(R.id.searchBtnInplacelist)

        placeAdapter = PlaceAdapter(this.datas)
        binding.rvPlace.adapter = placeAdapter

        // 리사이클러뷰를 처음에는 숨김(GONE) 상태로 설정
        binding.rvPlace.visibility = View.GONE

        // 리사이클러뷰 구분선 지정
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.rvPlace.addItemDecoration(dividerItemDecoration)

        // initRecycler() 함수 호출
        initRecycler()

        initClickEvent()

        searchBtnInplacelist.setOnClickListener {
            val searchText = searchEditText.text.toString()
            if (searchText.isNotEmpty()) {
                fetchPlacesByName(searchText)

                // 검색 버튼 클릭 시 리사이클러뷰를 보이도록 변경
                binding.rvPlace.visibility = View.VISIBLE
            }
        }
    }

    private fun initRecycler() {
        // 데이터 추가
        datas.add(PlaceResponse(1, "낙성대공원", "서울 관악", null))
        datas.add(PlaceResponse(1, "낙성대공원", "서울 관악", null))
        datas.add(PlaceResponse(1, "낙성대공원", "서울 관악", null))

        // 어댑터에 데이터 설정
        placeAdapter.datas = datas

        // RecyclerView를 갱신
        placeAdapter.notifyDataSetChanged()
    }

    private fun initClickEvent() {
        binding.apply{
            goBackBtn.setOnClickListener {
                // 뒤로가기 버튼 클릭 시 이벤트
                startActivity(Intent(this@PlaceListActivity, ResultActivity::class.java))
            }

            // adapter에 클릭리스너 부착
            // 여행지 클릭 시 이벤트
            val intent = Intent(this@PlaceListActivity, ResultActivity::class.java)
            placeAdapter.itemClicklistener = object:PlaceAdapter.OnItemClickListener{
                override fun OnItemClick(position: Int) {
                    // 다음 페이지의 http 통신을 위해 여행지 정보 넘겨줘야 함
                    intent.putExtra("id", placeAdapter.datas[position].id)
                    intent.putExtra("name", placeAdapter.datas[position].name)
                    intent.putExtra("region", placeAdapter.datas[position].region)
                    intent.putExtra("page", "placeList")
                    startActivity(intent)
                }
            }
        }
    }

    private fun fetchPlacesByName(searchText: String) {
        getPlaceByName(searchText) { result ->
            // 데이터를 가져온 후 어댑터에 데이터 설정
            datas.clear() // 기존 데이터를 지우고 새로운 데이터로 대체
            datas.addAll(result)
            placeAdapter.notifyDataSetChanged()
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




