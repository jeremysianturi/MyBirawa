package id.co.telkomsigma.mybirawa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.entity.ProgressTiket
import kotlinx.android.synthetic.main.item_progress_tiket.view.*

class ProgressDetailTiketAdapter :
    RecyclerView.Adapter<ProgressDetailTiketAdapter.UserViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    private val mData = ArrayList<ProgressTiket>()

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<ProgressTiket>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProgressDetailTiketAdapter.UserViewHolder {
        val mView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_progress_tiket, parent, false)
        return UserViewHolder(mView)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(
        holder: ProgressDetailTiketAdapter.UserViewHolder,
        position: Int
    ) {
        holder.bind(mData[position])
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: ProgressTiket) {
            with(itemView) {
                val indicator = data.idStatus
                if (indicator == 0) {
                    Glide.with(itemView.context)
                        .load(R.drawable.indicator_progress_tiket_before)
                        .into(img_indicator)
                } else {
                    Glide.with(itemView.context)
                        .load(R.drawable.indicator_progress_tiket_done)
                        .into(img_indicator)

                }
                tv_tittle_progress_tiket.text = data.statusTiket.toString()
                tv_desc_progress_tiket.text = data.descriptionTiket.toString()
                tv_date_progress_tiket.text = data.date.toString()


//                Glide.with(itemView.context)
//                    .load(model.avatar)
//                    .into(img_avatar)
//                txt_name.text = model.name
                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(data) }
            }
        }

    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ProgressTiket)
    }

}