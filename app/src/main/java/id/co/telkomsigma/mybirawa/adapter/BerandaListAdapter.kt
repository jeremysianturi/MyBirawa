package id.co.telkomsigma.mybirawa.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.controller.statusTiket.StatusTiketActivity
import id.co.telkomsigma.mybirawa.entity.BerandaTiket
import id.co.telkomsigma.mybirawa.util.CustomOnItemClickListener
import kotlinx.android.synthetic.main.item_tiket_beranda.view.*

class BerandaListAdapter(private val activity: Activity) :
    RecyclerView.Adapter<BerandaListAdapter.ViewHolder>() {


    private var onItemClickCallback: OnItemClickCallback? = null
    private val mData = ArrayList<BerandaTiket>()

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    fun setData(items: ArrayList<BerandaTiket>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BerandaListAdapter.ViewHolder {
        val mView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_tiket_beranda, parent, false)
        return ViewHolder(mView)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: BerandaTiket) {
            with(itemView) {

                Glide.with(itemView.context)
                    .load(data.image)
                    .placeholder(R.color.grey_line_background_color)
                    .error(R.drawable.ic_test_settings)
                    .into(imgItemIconBeranda)

                val strTittle = data.bidangName
                tvItemTittleBeranda.text = strTittle

                val strBaru = "Baru : ${data.baru}"
                tvItemBaruBeranda.text = strBaru

                val strBerjalan = "Berjalan : ${data.berjalan}"
                tvitemBerjalanBeranda.text = strBerjalan

                val strSelesai = "Selesai : ${data.selesai}"
                tvItemSelesaiBeranda.text = strSelesai

                data.bgColorIcon?.let { lnBackgroundGambar.setBackgroundColor(it) }
                data.bgColor?.let { clBackground.setBackgroundColor(it) }

                cvItemParentBerandaTiket.setOnClickListener(
                    CustomOnItemClickListener(
                        adapterPosition,
                        object : CustomOnItemClickListener.OnItemClickCallback {
                            override fun onItemClicked(view: View, position: Int) {
                                val mIntent = Intent(activity, StatusTiketActivity::class.java)
                                mIntent.putExtra(StatusTiketActivity.EXTRA_DATA, data)
                                mIntent.putExtra(
                                    StatusTiketActivity.EXTRA_POSITION_FILTER_BIDANG,
                                    adapterPosition
                                )
                                activity.startActivity(mIntent)

                            }

                        })
                )
                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(
                        data,
                        data.bidangId?.toInt()
                    )
                }
            }
        }

    }

    interface OnItemClickCallback {
        fun onItemClicked(data: BerandaTiket, adapterPosition: Int?)
    }


}