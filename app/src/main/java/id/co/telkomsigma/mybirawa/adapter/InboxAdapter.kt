package id.co.telkomsigma.mybirawa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.entity.Inbox

import kotlinx.android.synthetic.main.item_inbox.view.*
import kotlin.collections.ArrayList


class InboxAdapter : RecyclerView.Adapter<InboxAdapter.UserViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    private val mData = ArrayList<Inbox>()
    private var mFilter = ArrayList<Inbox>()

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<Inbox>) {
        mData.clear()
        mFilter.clear()
        mData.addAll(items)
        mFilter.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InboxAdapter.UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.item_inbox, parent, false)
        return UserViewHolder(mView)
    }

    override fun getItemCount(): Int = mFilter.size

    override fun onBindViewHolder(holder: InboxAdapter.UserViewHolder, position: Int) {
        holder.bind(mFilter[position])
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: Inbox) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(data.url)
                    .error(R.drawable.ic_account_circle_black_24dp)
                    .into(img_icon_inbox)

                tv_item_inbox_tittle.text = data.subject
                tv_item_inbox_date.text = data.chageDate
                tv_item_inbox_massage.text = data.message


                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(data) }
            }
        }
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

    interface OnItemClickCallback {
        fun onItemClicked(data: Inbox)
    }

}
