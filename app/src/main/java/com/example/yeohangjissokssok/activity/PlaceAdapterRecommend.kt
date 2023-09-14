package com.example.yeohangjissokssok.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yeohangjissokssok.databinding.PlaceRecyclerBinding
import com.example.yeohangjissokssok.models.SACategoryResponse

class PlaceAdapterRecommend(var datas: ArrayList<SACategoryResponse>)
    : RecyclerView.Adapter<PlaceAdapterRecommend.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(position:Int)
    }

    var itemClicklistener:OnItemClickListener?=null

    inner class ViewHolder(val binding: PlaceRecyclerBinding) :  RecyclerView.ViewHolder(binding.root) {
        init{
            binding.tvRvPlace.setOnClickListener {
                itemClicklistener?.OnItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceAdapterRecommend.ViewHolder {
        val view = PlaceRecyclerBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvRvPlace.text = datas[position].region + " " + datas[position].name
    }
}