package com.example.yeohangjissokssok.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yeohangjissokssok.databinding.PlaceRecyclerBinding
import com.example.yeohangjissokssok.databinding.SearchRecyclerBinding

class SearchAdapter(var datas: ArrayList<String>)
    : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    fun removeData(pos:Int){
        // items에서 data 삭제 후, 화면 갱신 요청
        datas.removeAt(pos)
        notifyItemRemoved(pos)
    }

    fun moveData(oldPos:Int, newPos:Int){
        var temp = datas[oldPos]
        datas[oldPos] = datas[newPos]
        datas[newPos] = temp
        notifyItemMoved(oldPos, newPos)
    }

    interface OnItemClickListener {
        fun OnItemClick(pos: Int)
        fun DeleteClick(pos: Int)
    }

    var itemClicklistener: OnItemClickListener?=null

    inner class ViewHolder(val binding: SearchRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {
            init {
                binding.recentSearchText.setOnClickListener {
                    itemClicklistener?.OnItemClick(bindingAdapterPosition)
                }
                binding.deleteBtn.setOnClickListener {
                    itemClicklistener?.DeleteClick(bindingAdapterPosition)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SearchRecyclerBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.recentSearchText.text = datas[position]
    }
}

