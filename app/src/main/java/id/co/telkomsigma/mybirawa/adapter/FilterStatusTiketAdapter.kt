package id.co.telkomsigma.mybirawa.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.entity.BerandaTiket
import id.co.telkomsigma.mybirawa.entity.FilterBidang
import kotlinx.android.synthetic.main.item_filter_status_tiket.view.*

class FilterStatusTiketAdapter : RecyclerView.Adapter<FilterStatusTiketAdapter.UserViewHolder>() {

//    companion object{
//        var lastPosition = 0
//    }

    private var lastPosition = 0
    private var onItemClickCallback: OnItemClickCallback? = null

    private val mData = ArrayList<FilterBidang>()
    private var posBIdang: Int = 0

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<FilterBidang>, postion: Int) {
        mData.clear()
        mData.addAll(items)
        this.posBIdang = postion
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FilterStatusTiketAdapter.UserViewHolder {
        val mView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_filter_status_tiket, parent, false)
        return UserViewHolder(mView)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: FilterStatusTiketAdapter.UserViewHolder, position: Int) {
        holder.bind(mData[position], posBIdang)

    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: FilterBidang, posBIdang: Int) {
            with(itemView) {

                tvFilterItem.text = data.bidangName
                if (data.backgoundRed == true) {
                    tvFilterItem.setBackgroundResource(R.drawable.background_rounded_border_redline)
                } else {
                    tvFilterItem.setBackgroundResource(R.drawable.background_rounded_border_greyline)
                }

                itemView.setOnClickListener {
                    tvFilterItem.setBackgroundResource(R.drawable.background_rounded_border_redline)
                    onItemClickCallback?.onItemClicked(data, data.bidangId!!.toInt())

                }

            }
        }

    }

    interface OnItemClickCallback {
        fun onItemClicked(data: FilterBidang, position: Int)
    }

}