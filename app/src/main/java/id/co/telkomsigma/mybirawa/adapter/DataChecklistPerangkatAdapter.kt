package id.co.telkomsigma.mybirawa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.entity.StatusTiket
import kotlinx.android.synthetic.main.item_history.view.*
import kotlinx.android.synthetic.main.item_semua_statustiket.view.*
import kotlinx.android.synthetic.main.item_semua_statustiket.view.imageView2
import kotlinx.android.synthetic.main.item_semua_statustiket.view.img_photo_enginer
import kotlinx.android.synthetic.main.item_semua_statustiket.view.view_status_color

class DataChecklistPerangkatAdapter :
    RecyclerView.Adapter<DataChecklistPerangkatAdapter.UserViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    private val mData = ArrayList<StatusTiket>()

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<StatusTiket>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataChecklistPerangkatAdapter.UserViewHolder {
        val mView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_data_checklist_perangkat, parent, false)
        return UserViewHolder(mView)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(
        holder: DataChecklistPerangkatAdapter.UserViewHolder,
        position: Int
    ) {
        holder.bind(mData[position])
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: StatusTiket) {
            with(itemView) {


                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(data) }
            }
        }

    }

    interface OnItemClickCallback {
        fun onItemClicked(data: StatusTiket)
    }

}