package id.co.telkomsigma.mybirawa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.entity.HistoryTiket
import id.co.telkomsigma.mybirawa.entity.StatusTiket
import kotlinx.android.synthetic.main.item_history.view.*
import kotlinx.android.synthetic.main.item_history.view.imageView2
import kotlinx.android.synthetic.main.item_history.view.img_photo_enginer
import kotlinx.android.synthetic.main.item_history.view.view_status_color
import java.util.*
import kotlin.collections.ArrayList


class HistoryTiketAdapter : RecyclerView.Adapter<HistoryTiketAdapter.UserViewHolder>(), Filterable {

    private var onItemClickCallback: OnItemClickCallback? = null

    private val mData = ArrayList<StatusTiket>()
    private var mFilter = ArrayList<StatusTiket>()

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<StatusTiket>) {
        mData.clear()
        mFilter.clear()
        mData.addAll(items)
        mFilter.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryTiketAdapter.UserViewHolder {
        val mView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return UserViewHolder(mView)
    }

    override fun getItemCount(): Int = mFilter.size

    override fun onBindViewHolder(holder: HistoryTiketAdapter.UserViewHolder, position: Int) {
        holder.bind(mFilter[position])
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
                when (data.statusId) {
                    "1" -> {
                        view_status_color.setBackgroundColor(
                            ContextCompat.getColor(
                                itemView.context,
                                themeColor
                            )
                        )
                        tvStatusHistory.setBackgroundResource(themeColorBackground)
                        tvStatusHistory.setTextColor(
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
                        tvStatusHistory.setBackgroundResource(themeColorBackground)
                        tvStatusHistory.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                themeColor
                            )
                        )
                    }
                    "3" -> {
                        themeColor = R.color.green_status_tiket
                        themeColorBackground = R.drawable.background_rounded_all_green_opacity
                        view_status_color.setBackgroundColor(
                            ContextCompat.getColor(
                                itemView.context,
                                themeColor
                            )
                        )
                        tvStatusHistory.setBackgroundResource(themeColorBackground)
                        tvStatusHistory.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                themeColor
                            )
                        )
                    }
                    else -> {
                        themeColor = R.color.red_status_tiket
                        themeColorBackground = R.drawable.background_rounded_all_red_opacity
                        view_status_color.setBackgroundColor(
                            ContextCompat.getColor(
                                itemView.context,
                                themeColor
                            )
                        )
                        tvStatusHistory.setBackgroundResource(themeColorBackground)
                        tvStatusHistory.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                themeColor
                            )
                        )
                    }
                }

                val noTiket = "Nomor Ticket : ${data.ticketId}"
                tvNumberTiketHistory.text = noTiket
                tvDateHistory.text = data.createdDate

                val desc = "${data.bidangName} ${data.SubBidangName}"
                tvDescription.text = desc

                tvStatusHistory.text = data.statusName
                tvEnginerName.text = data.userName


                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(data) }
            }
        }

    }

    interface OnItemClickCallback {
        fun onItemClicked(data: StatusTiket)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                mFilter = if (charSearch.isEmpty()) {
                    mData
                } else {
                    val resultList = ArrayList<StatusTiket>()
                    for (row in mFilter) {
                        if (row.ticketId!!.toLowerCase(Locale.getDefault()).contains(
                                charSearch.toLowerCase(
                                    Locale.getDefault()
                                )
                            )
                        )
                            resultList.add(row)
                    }
                    resultList
                }
                val filterResult: FilterResults = Filter.FilterResults()
                filterResult.values = mFilter

                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                mFilter = results!!.values as ArrayList<StatusTiket>
                notifyDataSetChanged()
            }

        }
    }


}