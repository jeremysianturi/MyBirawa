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

class BaruStatusTiketAdapter : RecyclerView.Adapter<BaruStatusTiketAdapter.UserViewHolder>() {

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
    ): BaruStatusTiketAdapter.UserViewHolder {
        val mView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_semua_statustiket, parent, false)
        return UserViewHolder(mView)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: BaruStatusTiketAdapter.UserViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: StatusTiket) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(data.historyAvatar)
                    .error(R.drawable.ic_account_circle_black_24dp)
                    .into(img_photo_enginer)

                Glide.with(itemView.context)
                    .load(data.imgURL)
                    .error(R.drawable.ic_account_circle_black_24dp)
                    .into(imageView2)

                var themeColor = R.color.blue_status_tiket
                var themeColorBackground = R.drawable.background_rounded_all_blue_opacity
                if (data.statusId == "1") {
                    view_status_color.setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            themeColor
                        )
                    )
                    tv_status_tiket.setBackgroundResource(themeColorBackground)
                    tv_status_tiket.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            themeColor
                        )
                    )
                } else if (data.statusId == "2") {
                    themeColor = R.color.yellow_status_tiket
                    themeColorBackground = R.drawable.background_rounded_all_yellow_opacity
                    view_status_color.setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            themeColor
                        )
                    )
                    tv_status_tiket.setBackgroundResource(themeColorBackground)
                    tv_status_tiket.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            themeColor
                        )
                    )
                } else {
                    themeColor = R.color.green_status_tiket
                    themeColorBackground = R.drawable.background_rounded_all_green_opacity
                    view_status_color.setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            themeColor
                        )
                    )
                    tv_status_tiket.setBackgroundResource(themeColorBackground)
                    tv_status_tiket.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            themeColor
                        )
                    )
                }

                val noTiket = "Nomor Ticket : ${data.ticketId}"
                tv_no_tiket.text = noTiket
                tv_date.text = data.createdDate

                val desc = "${data.bidangName} ${data.SubBidangName}"
                tv_job_description.text = desc

                tv_status_tiket.text = data.statusName
                tv_enginer_name.text = data.userName

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(data) }
            }
        }

    }

    interface OnItemClickCallback {
        fun onItemClicked(data: StatusTiket)
    }

}