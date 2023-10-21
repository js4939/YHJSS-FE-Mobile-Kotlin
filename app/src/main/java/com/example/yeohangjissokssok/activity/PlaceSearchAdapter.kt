import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yeohangjissokssok.R
import com.example.yeohangjissokssok.activity.PlaceResponse
import com.example.yeohangjissokssok.databinding.PlaceSearchRecyclerBinding

class PlaceSearchAdapter(
    var datas: ArrayList<PlaceResponse>
)   : RecyclerView.Adapter<PlaceSearchAdapter.ViewHolder>(){

    interface OnItemClickListener{
        fun OnItemClick(position:Int)
    }

    var itemClicklistener:OnItemClickListener?=null

    inner class ViewHolder(val binding: PlaceSearchRecyclerBinding) :  RecyclerView.ViewHolder(binding.root) {
        init{
            binding.tvRvPlacename.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClicklistener?.OnItemClick(position)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceSearchAdapter.ViewHolder {
        val view = PlaceSearchRecyclerBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageView = holder.binding.imgRvPlace

        // 이미지를 로드 및 설정
        val imageUrl = datas[position].photoUrl
        Glide.with(imageView.context)
            .load(imageUrl)
            .placeholder(R.drawable.baseline_update_24)  // 로딩 이미지
            .error(R.drawable.baseline_error_24)        // 에러 이미지
            .override(200, 200)
            .centerCrop() // 이미지 크기 조절 및 자르기
            .into(imageView)

        holder.binding.tvRvPlacename.text = datas[position].name
        holder.binding.tvRvPlacelocation.text = datas[position].region
        holder.binding.tvRvLocation.text = datas[position].address

    }
}