package id.co.telkomsigma.mybirawa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.entity.DetailPerangkat
import kotlinx.android.synthetic.main.item_detail_perangkat_gedung.view.*
import kotlin.collections.ArrayList


class DetailPerangkatGedungAdapter :
    RecyclerView.Adapter<DetailPerangkatGedungAdapter.UserViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    private val mData = ArrayList<DetailPerangkat>()
    private var mFilter = ArrayList<DetailPerangkat>()

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<DetailPerangkat>) {
        mData.clear()
        mFilter.clear()
        mData.addAll(items)
        mFilter.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailPerangkatGedungAdapter.UserViewHolder {
        val mView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detail_perangkat_gedung, parent, false)
        return UserViewHolder(mView)
    }

    override fun getItemCount(): Int = mFilter.size

    override fun onBindViewHolder(
        holder: DetailPerangkatGedungAdapter.UserViewHolder,
        position: Int
    ) {
        holder.bind(mFilter[position])
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: DetailPerangkat) {
            with(itemView) {

                val image = data.urlImage ?: "www.google.com"
                Glide.with(itemView.context)
                    .load(image)
                    .error(R.drawable.ic_error_broken_image_darkgrey)
                    .into(imgDetailPerangkat)

                tvPerangkatName.text = data.perangkatJenisName

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(data) }
            }
        }

    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DetailPerangkat)
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