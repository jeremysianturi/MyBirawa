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
import id.co.telkomsigma.mybirawa.entity.Perangkat
import id.co.telkomsigma.mybirawa.entity.StatusTiket
import kotlinx.android.synthetic.main.item_history.view.*
import kotlinx.android.synthetic.main.item_history.view.imageView2
import kotlinx.android.synthetic.main.item_history.view.img_photo_enginer
import kotlinx.android.synthetic.main.item_history.view.view_status_color
import kotlinx.android.synthetic.main.item_perangkat.view.*
import java.util.*
import kotlin.collections.ArrayList


class PerangkatAdapter : RecyclerView.Adapter<PerangkatAdapter.UserViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    private val mData = ArrayList<Perangkat>()
    private var mFilter = ArrayList<Perangkat>()

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<Perangkat>) {
        mData.clear()
        mFilter.clear()
        mData.addAll(items)
        mFilter.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PerangkatAdapter.UserViewHolder {
        val mView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_perangkat, parent, false)
        return UserViewHolder(mView)
    }

    override fun getItemCount(): Int = mFilter.size

    override fun onBindViewHolder(holder: PerangkatAdapter.UserViewHolder, position: Int) {
        holder.bind(mFilter[position])
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: Perangkat) {
            with(itemView) {

                val url = data.url
                Glide.with(itemView.context)
                    .load(url)
                    .error(R.drawable.ic_sipil)
                    .into(imgItemPerangkat)

                val tittle = "${data.periode} ${data.sudah}/${data.total}"
                val percent = "${data.percent}%"

                tvItemTittlePerangkat.text = tittle
                tvItemPercentasePerangkat.text = percent

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(data) }
            }
        }

    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Perangkat)
    }

//    override fun getFilter(): Filter {
//        return  object :Filter(){
//            override fun performFiltering(constraint: CharSequence?): FilterResults {
//                val charSearch = constraint.toString()
//                mFilter = if (charSearch.isEmpty()){
//                    mData
//                }else{
//                    val resultList = ArrayList<Perangkat>()
//                    for (row in mFilter) {
//                        if (row.ticketId!!.toLowerCase(Locale.getDefault()).contains(charSearch.toLowerCase(
//                                Locale.getDefault())))
//                            resultList.add(row)
//                    }
//                    resultList
//                }
//                val filterResult : FilterResults = Filter.FilterResults()
//                filterResult.values = mFilter
//
//                return filterResult
//            }
//            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//               mFilter = results!!.values as ArrayList<Perangkat>
//                notifyDataSetChanged()
//            }
//
//        }
//    }


}