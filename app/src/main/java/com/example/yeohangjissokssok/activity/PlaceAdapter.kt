import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yeohangjissokssok.activity.MainData
import com.example.yeohangjissokssok.databinding.PlaceRecyclerBinding

class PlaceAdapter(private val context: Context) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    var datas = mutableListOf<MainData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PlaceRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = PlaceRecyclerBinding.bind(view)

        fun bind(item: MainData) {
            binding.tvRvPlace.text=item.tv_rv_place
            Glide.with(context).load(item.img).into(binding.imgRvPlus)
            //binding.txtName.text = item.tv_rv_place
            //Glide.with(context).load(item.img).into(binding.imgPlus)
        }
    }
}
