package id.co.telkomsigma.mybirawa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.entity.Punchlist
import kotlinx.android.synthetic.main.item_content_punchlist.view.*
import kotlinx.android.synthetic.main.item_content_punchlist.view.imageView2
import kotlinx.android.synthetic.main.item_content_punchlist.view.tv_job_description
import kotlinx.android.synthetic.main.item_content_punchlist.view.view_status_color

class PunchlistTeknisiAdapter : RecyclerView.Adapter<PunchlistTeknisiAdapter.PuchlistViewModel>() {

    private var onItemClickCallback: OnItemClickCallback? = null
    private val mData = ArrayList<Punchlist>()

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<Punchlist>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PuchlistViewModel {
        val mView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_content_punchlist, parent, false)
        return PuchlistViewModel(mView)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: PuchlistViewModel, position: Int) {
        holder.bind(mData[position])
    }

    inner class PuchlistViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: Punchlist) {
            with(itemView) {

                Glide.with(itemView.context)
                    .load(R.drawable.ic_settings_black_24dp)
                    .error(R.drawable.ic_error_broken_image_darkgrey)
                    .into(imageView2)

                var themeColor = R.color.red_status_tiket
                var themeColorBackground = R.drawable.background_rounded_all_red_opacity
                when (data.idStatus) {
                    "1" -> {
                        view_status_color.setBackgroundColor(
                            ContextCompat.getColor(
                                itemView.context,
                                themeColor
                            )
                        )
                        tvStatusPunchlist.setBackgroundResource(themeColorBackground)
                        tvStatusPunchlist.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                themeColor
                            )
                        )
                    }
                    "2" -> {
                        themeColor = R.color.yellow_status_tiket
                        themeColorBackground = R.drawable.background_rounded_all_yellow_opacity
                        view_status_color.setBackgroundColor(
                            ContextCompat.getColor(
                                itemView.context,
                                themeColor
                            )
                        )
                        tvStatusPunchlist.setBackgroundResource(themeColorBackground)
                        tvStatusPunchlist.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                themeColor
                            )
                        )
                    }
                    else -> {
                        themeColor = R.color.green_status_tiket
                        themeColorBackground = R.drawable.background_rounded_all_green_opacity
                        view_status_color.setBackgroundColor(
                            ContextCompat.getColor(
                                itemView.context,
                                themeColor
                            )
                        )
                        tvStatusPunchlist.setBackgroundResource(themeColorBackground)
                        tvStatusPunchlist.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                themeColor
                            )
                        )
                    }
                }

                val noTiket = "Nomor Ticket : ${data.noPunchlist}"
                tvNoPunchlist.text = noTiket
                tvGedungLantai.text = "${data.gedung} ${data.lantai}"

                val desc = "${data.bidangName} ${data.subBidangName}"
                tv_job_description.text = desc

                tvStatusPunchlist.text = data.status

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(data) }
            }
        }

    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Punchlist)
    }

}