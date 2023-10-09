import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yeohangjissokssok.R
import com.example.yeohangjissokssok.activity.PlaceResponse
import com.example.yeohangjissokssok.databinding.PlaceRecyclerBinding
import com.example.yeohangjissokssok.models.SAPlaceResponse
import kotlin.math.round

class PlaceAdapter(
    var datas: ArrayList<PlaceResponse>
)
    : RecyclerView.Adapter<PlaceAdapter.ViewHolder>(){

    // totalnum과 positivenum을 저장할 변수
    private var totalnum: Int = 0
    private var pos: Double = 0.0

    interface OnItemClickListener{
        fun OnItemClick(position:Int)
    }

    var itemClicklistener:OnItemClickListener?=null

    inner class ViewHolder(val binding: PlaceRecyclerBinding) :  RecyclerView.ViewHolder(binding.root) {
        init{
            binding.tvRvPlacename.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClicklistener?.OnItemClick(position)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceAdapter.ViewHolder {
        val view = PlaceRecyclerBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(view)
    }

    fun setTotalAndPositiveNum(total: Int, positive: Double) {
        totalnum = total
        pos = positive
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
        holder.binding.PosBarRv.progress = round(pos).toInt()
        holder.binding.PosPercentRv.text = round(pos).toInt().toString() + "% (" + totalnum + ")"

    }

}