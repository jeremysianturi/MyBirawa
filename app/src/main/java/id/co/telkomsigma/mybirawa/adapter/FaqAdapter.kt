package id.co.telkomsigma.mybirawa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.entity.Faq
import id.co.telkomsigma.mybirawa.entity.Inbox
import kotlinx.android.synthetic.main.item_faq.view.*

import kotlinx.android.synthetic.main.item_inbox.view.*
import kotlin.collections.ArrayList


class FaqAdapter : RecyclerView.Adapter<FaqAdapter.UserViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    private val mData = ArrayList<Faq>()
    private var mFilter = ArrayList<Faq>()

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<Faq>) {
        mData.clear()
        mFilter.clear()
        mData.addAll(items)
        mFilter.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqAdapter.UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.item_faq, parent, false)
        return UserViewHolder(mView)
    }

    override fun getItemCount(): Int = mFilter.size

    override fun onBindViewHolder(holder: FaqAdapter.UserViewHolder, position: Int) {
        holder.bind(mFilter[position])
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: Faq) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(R.drawable.ic_faq_kerusakan_listrik)
                    .error(R.drawable.ic_faq_kerusakan_listrik)
                    .into(img_icon_item_faq)
//
//                tv_desc_item_faq.text = data.description
//                tv_tittle_item_faq.text = data.tittle

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(data) }
            }
        }

    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Faq)
    }

//    override fun getFilter(): Filter {
//        return  object :Filter(){
//            override fun performFiltering(constraint: CharSequence?): FilterResults {
//                val charSearch = constraint.toString()
//                mFilter = if (charSearch.isEmpty()){
//                    mData
//                }else{
//                    val resultList = ArrayList<StatusTiket>()
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
//               mFilter = results!!.values as ArrayList<Inbox>
//                notifyDataSetChanged()
//            }
//
//        }
//    }


}