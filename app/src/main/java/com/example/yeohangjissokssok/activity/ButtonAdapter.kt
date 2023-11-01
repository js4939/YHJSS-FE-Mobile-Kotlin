package com.example.yeohangjissokssok.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.yeohangjissokssok.R

class ButtonAdapter(private val buttonDataList: List<String>) :
    RecyclerView.Adapter<ButtonAdapter.ButtonViewHolder>() {

    // 버튼 클릭 이벤트 리스너 인터페이스 정의
    interface OnItemClickListener {
        fun onItemClick(item: String, position: Int)
    }

    // 리스너 프로퍼티 선언
    private var listener: OnItemClickListener? = null

    // 각 버튼의 선택 상태를 저장하는 배열
    private val selectedStates = BooleanArray(buttonDataList.size) { false }

    // 리스너 등록 메서드
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    // ViewHolder 클래스 정의
    inner class ButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.rv_btn)

        init {
            // 버튼 클릭 시 아이템을 리스너에 전달
            button.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(buttonDataList[position], position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.keyword_recycler, parent, false)
        return ButtonViewHolder(view)
    }

    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        val buttonText = buttonDataList[position]
        holder.button.text = buttonText

        // 버튼의 선택 상태에 따라 배경색 설정
        holder.button.isSelected = selectedStates[position]
    }

    override fun getItemCount(): Int {
        return buttonDataList.size
    }

    // 버튼의 선택 상태를 토글하는 함수
    fun toggleItemSelection(position: Int) {
        selectedStates[position] = !selectedStates[position]
        notifyItemChanged(position)
    }

}
