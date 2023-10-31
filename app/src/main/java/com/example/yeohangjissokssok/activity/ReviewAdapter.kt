import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yeohangjissokssok.activity.PlaceResponse
import com.example.yeohangjissokssok.databinding.PlaceRecyclerBinding
import com.example.yeohangjissokssok.databinding.ReviewRecyclerBinding
import com.example.yeohangjissokssok.models.ReviewData
import com.example.yeohangjissokssok.models.ReviewResponse
import kotlinx.coroutines.NonDisposableHandle.parent

class ReviewAdapter(var datas: ArrayList<ReviewData>)
    : RecyclerView.Adapter<ReviewAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: ReviewRecyclerBinding) :  RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ReviewRecyclerBinding.inflate(LayoutInflater.from(parent.context))
        val layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        view.root.layoutParams = layoutParams
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(datas[position].state){
            0 -> holder.binding.reviewState.text = "긍정"
            1 -> holder.binding.reviewState.text = "부정"
            3 -> holder.binding.reviewState.text = "중립"
        }

        when(datas[position].keyword){
            "홀로" -> holder.binding.reviewState.text = "홀로"
            "바다" -> holder.binding.reviewState.text = "바다"
            "연인" -> holder.binding.reviewState.text = "연인"
            "운동" -> holder.binding.reviewState.text = "운동"
            "가족" -> holder.binding.reviewState.text = "가족"
            "꽃" -> holder.binding.reviewState.text = "꽃"
            "맛집" -> holder.binding.reviewState.text = "맛집"
            "친구" -> holder.binding.reviewState.text = "친구"
            "힐링" -> holder.binding.reviewState.text = "힐링"
            "등산" -> holder.binding.reviewState.text = "등산"
            "아이" -> holder.binding.reviewState.text = "아이"
            "전시" -> holder.binding.reviewState.text = "전시"
            "야경" -> holder.binding.reviewState.text = "야경"
            "나들이" -> holder.binding.reviewState.text = "나들이"
            "쇼핑" -> holder.binding.reviewState.text = "쇼핑"
            "관광" -> holder.binding.reviewState.text = "관광"
            "산책" -> holder.binding.reviewState.text = "산책"
        }

        holder.binding.reviewDate.text = datas[position].date
        holder.binding.reviewContent.text = datas[position].content
    }

}
