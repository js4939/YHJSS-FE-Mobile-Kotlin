package com.example.yeohangjissokssok.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yeohangjissokssok.databinding.PlaceRecyclerBinding
import com.example.yeohangjissokssok.databinding.SearchRecyclerBinding

class SearchAdapter(var datas: ArrayList<String>)
    : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun OnItemClick(position: Int)
    }

    var itemClicklistener: OnItemClickListener? = null

    inner class ViewHolder(val binding: SearchRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.searchText.setOnClickListener {
                itemClicklistener?.OnItemClick(adapterPosition)
            }
            binding.deleteBtn.setOnClickListener {
                itemClicklistener?.OnItemClick(adapterPosition)
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
        holder.binding.searchText.text = datas[position]
    }
}

