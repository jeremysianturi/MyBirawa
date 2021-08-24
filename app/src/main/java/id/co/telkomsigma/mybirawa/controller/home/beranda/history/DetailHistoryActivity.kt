package id.co.telkomsigma.mybirawa.controller.home.beranda.history

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.adapter.MyAdapter
import id.co.telkomsigma.mybirawa.controller.statusTiket.detailTiket.DetailTiketActivity
import id.co.telkomsigma.mybirawa.entity.ListProgressTiket
import id.co.telkomsigma.mybirawa.entity.StatusTiket
import kotlinx.android.synthetic.main.activity_detail_history.*

class DetailHistoryActivity : AppCompatActivity(), View.OnClickListener {

    private val tags = DetailHistoryActivity::class.java.simpleName

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    private var mListProgressTiket: ListProgressTiket? = null
    private lateinit var listProgress: List<ListProgressTiket>
    private lateinit var mMyAdapter: MyAdapter
    private lateinit var dataFromIntent: StatusTiket

    private var phoneNumbers: String? = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_history)

        dataFromIntent = intent.getParcelableExtra(DetailTiketActivity.EXTRA_DATA) as StatusTiket

        if (dataFromIntent.historyProgressTicket.isNullOrEmpty()) {
            Log.d(tags, "null data array")
        } else {
            showProgressTicket(dataFromIntent)
        }

        // method
        setupDetailData(dataFromIntent)

        // onclickListener
        imgCallHistory.setOnClickListener(this)

        //setup Actionbar and navigasi up
        val actionbar = supportActionBar
        actionbar?.title = "Detail Tiket"
        actionbar?.setDisplayHomeAsUpEnabled(true)

    }

    private fun showProgressTicket(dataFromIntent: StatusTiket) {
        listProgress = dataFromIntent.historyProgressTicket!!
        val listItem = ArrayList<MyAdapter.MyItem>()
        // index for
        var lastIndex = listProgress.size

        lastIndex = if (lastIndex > 0) {
            listProgress.size - 1
        } else {
            0
        }

        Log.d(tags, "lastIndex : $lastIndex")
        for (i in listProgress.indices) {
            var isActive = false
            val name = listProgress[i].userName
            val status = listProgress[i].statusName
            val date = listProgress[i].statusDate
            val statusid = listProgress[i].statusId
            val phone = listProgress[i].noTlp
//            val tittle = "Ticket $status"

            Log.d(tags, "index : $i + $lastIndex")

            if (lastIndex == i) {
                Log.d(tags, " Masuk lastIndex")
                tvDetailPic.text = name.toString()
                phoneNumbers = phone.toString()
                isActive = true
            }
//            val items = MyAdapter.MyItem(isActive,date.toString(),tittle,name.toString())
            val items =
                MyAdapter.MyItem(isActive, status.toString(), date.toString(), name.toString())
            listItem.add(items)
            mListProgressTiket = ListProgressTiket(status, name)
        }
        mMyAdapter = MyAdapter(listItem)
        sequenceLayout2.setAdapter(mMyAdapter)
    }

    private fun setupDetailData(data: StatusTiket) {
        var themeColor = R.color.blue_status_tiket
        var themeColorBackground = R.drawable.background_rounded_all_blue_opacity

        Log.d(tags, "statusIdDetailTicket : ${data.statusId}")
        when (data.statusId) {
            "1" -> {
                tvStatusTicket.setBackgroundResource(themeColorBackground)
                tvStatusTicket.setTextColor(ContextCompat.getColor(this, themeColor))
//                layoutPicDetailTicket.visibility = View.GONE
            }
            "2" -> {
                themeColor = R.color.yellow_status_tiket
                themeColorBackground = R.drawable.background_rounded_all_yellow_opacity
                tvStatusTicket.setBackgroundResource(themeColorBackground)
                tvStatusTicket.setTextColor(ContextCompat.getColor(this, themeColor))
            }
            "3" -> {
                themeColor = R.color.green_status_tiket
                themeColorBackground = R.drawable.background_rounded_all_green_opacity
                tvStatusTicket.setBackgroundResource(themeColorBackground)
                tvStatusTicket.setTextColor(ContextCompat.getColor(this, themeColor))
                //setup Layout
//                layoutDone.visibility = View.VISIBLE
            }
            "4" -> {
                themeColor = R.color.red_status_tiket
                themeColorBackground = R.drawable.background_rounded_all_red_opacity
                tvStatusTicket.setBackgroundResource(themeColorBackground)
                tvStatusTicket.setTextColor(ContextCompat.getColor(this, themeColor))
            }
            else -> {
                themeColor = R.color.red_status_tiket
                themeColorBackground = R.drawable.background_rounded_all_red_opacity
                tvStatusTicket.setBackgroundResource(themeColorBackground)
                tvStatusTicket.setTextColor(ContextCompat.getColor(this, themeColor))
                tvTittleRating.visibility = View.GONE
                ratingBar2.visibility = View.GONE
                tvUlasan.visibility = View.GONE
                //setup Layout
//                layoutDone.visibility = View.VISIBLE
            }
        }

        tvDetailNoTiket.text = data.ticketId
        tvStatusTicket.text = data.statusName
        tvDetailAddress.text = data.locationName
        tvDetailAddress2.text = data.locationName
        tvDetailBidang.text = data.bidangName
        tvDetailMasalah.text = data.SubBidangName
        tvDetailDescription.text = data.description

        Glide.with(this)
            .load(data.evidence)
            .error(R.drawable.onboard_2)
            .into(imageView24)

        var rating = 0f
        if (data.rating != "null") {
            rating = data.rating!!.toFloat()
        }

        Log.e(tags, "rating : $rating")
        tvDescriptionHasilPekerjaan.text = data.report

        ratingBar2.rating = rating

        if (data.ulasan != "null") {
            tvUlasan.text = data.ulasan.toString()
        } else {
            tvUlasan.text = ""
        }

//        tvDetailPic.text = data.historyUsername
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgCallHistory -> {
                val phoneNumber = phoneNumbers
                val mIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                startActivity(mIntent)
            }
        }
    }

}