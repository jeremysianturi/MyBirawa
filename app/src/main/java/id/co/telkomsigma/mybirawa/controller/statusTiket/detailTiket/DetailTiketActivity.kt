package id.co.telkomsigma.mybirawa.controller.statusTiket.detailTiket

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import es.dmoral.toasty.Toasty
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.adapter.MyAdapter
import id.co.telkomsigma.mybirawa.controller.statusTiket.Rating.RatingTiketActivity
import id.co.telkomsigma.mybirawa.controller.statusTiket.UlangTicket.UlangTiketActivity
import id.co.telkomsigma.mybirawa.entity.ListProgressTiket
import id.co.telkomsigma.mybirawa.entity.StatusTiket
import kotlinx.android.synthetic.main.activity_detail_tiket.*

import java.io.File

class DetailTiketActivity : AppCompatActivity(), View.OnClickListener {

    private val tags = DetailTiketActivity::class.java.simpleName

    companion object {
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_DATA_PROGRESS = "extra_data_progress"
    }

    private var mListProgressTiket: ListProgressTiket? = null
    private lateinit var listProgress: List<ListProgressTiket>
    private var mProfileFile: File? = null
    private lateinit var mMyAdapter: MyAdapter
    private lateinit var dataFromIntent: StatusTiket

    private lateinit var phoneNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tiket)

        dataFromIntent = intent.getParcelableExtra(EXTRA_DATA) as StatusTiket

        if (dataFromIntent.historyProgressTicket.isNullOrEmpty()) {
            Log.d(tags, "null data array")
        } else {
            showProgressTicket(dataFromIntent)
        }

        Log.d(tags, "reopen : ${dataFromIntent.reopen}")
        // onclick listener
        btn_ulang.setOnClickListener(this)
        btn_akhiri.setOnClickListener(this)
        imgCall.setOnClickListener(this)

        // method
        setupDetailData(dataFromIntent)
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
                phoneNumber = phone.toString()
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
                layoutPicDetailTicket.visibility = View.GONE
            }
            "2" -> {
                themeColor = R.color.yellow_status_tiket
                themeColorBackground = R.drawable.background_rounded_all_yellow_opacity
                tvStatusTicket.setBackgroundResource(themeColorBackground)
                tvStatusTicket.setTextColor(ContextCompat.getColor(this, themeColor))
            }
            else -> {
                themeColor = R.color.green_status_tiket
                themeColorBackground = R.drawable.background_rounded_all_green_opacity
                tvStatusTicket.setBackgroundResource(themeColorBackground)
                tvStatusTicket.setTextColor(ContextCompat.getColor(this, themeColor))
                //setup Layout
                layoutDone.visibility = View.VISIBLE
            }
        }

        tvDetailNoTiket.text = data.ticketId
        tvStatusTicket.text = data.statusName
        tvDetailAddress.text = data.locationName
        tvDetailAddress2.text = data.locationName
        tvDetailBidang.text = data.bidangName
        tvDetailMasalah.text = data.SubBidangName
        tvDetailDescription.text = data.description
//        tvDetailPic.text = data.historyUsername

        //done
        tvDescriptionHasilPekerjaan.text = data.report
        Glide.with(this)
            .load(data.evidence)
            .error(R.drawable.illustration_container_tidak_ada_jaringan)
            .into(imgDetailTicker)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_ulang -> {
                if (dataFromIntent.reopen == 1) {
                    Toasty.info(
                        this,
                        "Anda sudah melakukan reopen pada ticket ini, \nReopen tiket hanya dapat di lakukan 1 kali ",
                        Toasty.LENGTH_SHORT
                    ).show()
                } else {
                    val description = "Apakah anda yakin untuk mengulangi tiket ini?"
                    popUpDialogUlang(description)
                }

            }
            R.id.btn_akhiri -> {
                val description = "Apakah anda yakin untuk mengakhiri tiket ini?"
                popUpDialogAkhir(description)
            }

            R.id.imgCall -> {
//                val phoneNumber  = "085279699642"
                Log.d(
                    tags, "" +
                            " : $phoneNumber"
                )
                val mIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                startActivity(mIntent)
            }
        }
    }

    private fun popUpDialogAkhir(description: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.layout_popupdialog_detail_tiket)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.BOTTOM
        dialog.window!!.attributes = lp
        dialog.setTitle("Input Code Here")

        val btnYes = dialog.findViewById<View>(R.id.btn_ya) as Button
        val btnNo = dialog.findViewById<View>(R.id.btn_batal) as Button
        val tvTittle = dialog.findViewById<View>(R.id.txt_tittle_popup) as TextView
        tvTittle.text = getString(R.string.txt_konfirmasi)
        val tvText = dialog.findViewById<View>(R.id.txt_subtittle_popup) as TextView
        tvText.text = description

        dialog.show()

        btnYes.setOnClickListener {
            dialog.dismiss()
            val mIntent = Intent(this, RatingTiketActivity::class.java)
            mIntent.putExtra(RatingTiketActivity.EXTRA_DATA, dataFromIntent)
            startActivity(mIntent)
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun popUpDialogUlang(description: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.layout_popupdialog_detail_tiket)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.BOTTOM
        dialog.window!!.attributes = lp
        dialog.setTitle("Input Code Here")

        val btnYes = dialog.findViewById<View>(R.id.btn_ya) as Button
        val btnNo = dialog.findViewById<View>(R.id.btn_batal) as Button

        val tvTittle = dialog.findViewById<View>(R.id.txt_tittle_popup) as TextView
        tvTittle.text = getString(R.string.txt_konfirmasi)
        val tvText = dialog.findViewById<View>(R.id.txt_subtittle_popup) as TextView
        tvText.text = description

        dialog.show()
        btnYes.setOnClickListener {
            dialog.dismiss()
            val mIntent = Intent(this, UlangTiketActivity::class.java)
            mIntent.putExtra(UlangTiketActivity.EXTRA_DATA, dataFromIntent)
            startActivity(mIntent)
        }
        btnNo.setOnClickListener {
            dialog.dismiss()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}