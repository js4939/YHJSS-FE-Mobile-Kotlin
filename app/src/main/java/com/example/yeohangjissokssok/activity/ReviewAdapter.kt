import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yeohangjissokssok.activity.PlaceResponse
import com.example.yeohangjissokssok.databinding.PlaceRecyclerBinding
import com.example.yeohangjissokssok.databinding.ReviewRecyclerBinding
import com.example.yeohangjissokssok.models.ReviewResponse

class ReviewAdapter(var datas: ArrayList<ReviewResponse>)
    : RecyclerView.Adapter<ReviewAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: ReviewRecyclerBinding) :  RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ReviewRecyclerBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(datas[position].state){
            0 -> holder.binding.reviewState.text = "긍정"
            1 -> holder.binding.reviewState.text = "부정"
            2 -> holder.binding.reviewState.text = "중립"
        }
        holder.binding.reviewContent.text = datas[position].content
    }
}
