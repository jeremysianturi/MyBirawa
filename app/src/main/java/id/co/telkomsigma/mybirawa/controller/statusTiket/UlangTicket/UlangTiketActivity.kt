package id.co.telkomsigma.mybirawa.controller.statusTiket.UlangTicket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.github.dhaval2404.form_validation.rule.NonEmptyRule
import com.github.dhaval2404.form_validation.validation.FormValidator
import com.google.android.material.bottomsheet.BottomSheetDialog
import es.dmoral.toasty.Toasty
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.adapter.MyAdapter
import id.co.telkomsigma.mybirawa.controller.home.HomeActivity
import id.co.telkomsigma.mybirawa.entity.ListProgressTiket
import id.co.telkomsigma.mybirawa.entity.PreferenceModel
import id.co.telkomsigma.mybirawa.entity.StatusTiket
import id.co.telkomsigma.mybirawa.util.UserPreference
import kotlinx.android.synthetic.main.activity_detail_tiket_helpdesk.*
import kotlinx.android.synthetic.main.activity_ulang_tiket.*
import kotlinx.android.synthetic.main.activity_ulang_tiket.tvDetailAddress
import kotlinx.android.synthetic.main.activity_ulang_tiket.tvDetailAddress2
import kotlinx.android.synthetic.main.activity_ulang_tiket.tvDetailBidang
import kotlinx.android.synthetic.main.activity_ulang_tiket.tvDetailDescription
import kotlinx.android.synthetic.main.activity_ulang_tiket.tvDetailMasalah
import kotlinx.android.synthetic.main.activity_ulang_tiket.tvDetailNoTiket
import kotlinx.android.synthetic.main.activity_ulang_tiket.tvDetailPic
import kotlinx.android.synthetic.main.activity_ulang_tiket.tvStatusTicket
import kotlinx.android.synthetic.main.bottomsheet_corfirmation.view.*
import kotlinx.android.synthetic.main.bottomsheet_information.view.*
import kotlinx.android.synthetic.main.fragment_register_three.*
import org.json.JSONObject

class UlangTiketActivity : AppCompatActivity(), View.OnClickListener {

    private val tags = UlangTiketActivity::class.java.simpleName

    companion object {
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_DATA_PROGRESS = "extra_data_progress"
    }

    private lateinit var mUserPreference: UserPreference
    private lateinit var session: PreferenceModel

    private lateinit var dataFromIntent: StatusTiket
    private lateinit var listProgress: List<ListProgressTiket>

    //bottomSheet
    private lateinit var bottomSheetDialog: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ulang_tiket)

        // define SharedFreference
        mUserPreference = UserPreference(this)
        session = mUserPreference.getUser()

        // recieve data from previous activity
        dataFromIntent = intent.getParcelableExtra(EXTRA_DATA) as StatusTiket
        // initiation bottomSheeetDialog
        bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialog)

        // call method
        setupDetailData(dataFromIntent)

        // init onclick button
        btn_akhiri.setOnClickListener(this)

        //setup Actionbar and navigasi up
        val actionbar = supportActionBar
        actionbar?.title = "Ulang Tiket"
        actionbar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_akhiri -> {
                if (validationField()) {
                    isValidField()
                }
            }
            R.id.btnYesConfirm -> {
                bottomSheetDialog.dismiss()
                submitPostUlangTicket()
//                popupBottomSheetInformation() // harusnya submit to post method yang di panggil
            }
            R.id.btnNoConfirm -> {
                bottomSheetDialog.dismiss()
            }
            R.id.btnInfo -> {
                bottomSheetDialog.dismiss()
                val mIntent = Intent(this, HomeActivity::class.java)
                startActivity(mIntent)
            }
        }
    }

    private fun submitPostUlangTicket() {
        showLoading(true)
        val userId = session.userId
        val noTicket = dataFromIntent.ticketId
        val reason = edt_reason_tiket.text.toString()

        val token = session.token
        val requestURL = "${session.server}/ticket/reopen?ticket_id=$noTicket&user_id=$userId"

        AndroidNetworking.post(requestURL)
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .addBodyParameter("reason", reason)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d(tags, "UlangTicket : $response ")
                    if (response?.getBoolean("status") == true) {
                        showLoading(false)
                        popupBottomSheetInformation()
                    } else {
                        val message = response?.getString("message")
                        showLoading(false)
                        Toasty.error(
                            this@UlangTiketActivity,
                            message.toString(),
                            Toasty.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onError(anError: ANError?) {
                    showLoading(false)
                    Toasty.error(
                        this@UlangTiketActivity,
                        "Gagal, coba beberapa saat lagi",
                        Toasty.LENGTH_SHORT
                    ).show()
                    val act = "UlangTicket"
                    errorLog(anError, act)
                }

            })

    }

    private fun errorLog(anError: ANError?, act: String) {
        Log.e(tags, "error $act : $anError")
        Log.e(tags, "error $act : ${anError?.response}")
        Log.e(tags, "error $act : ${anError?.errorBody}")
    }


    private fun isValidField() {
        popupBottomSheetConfitmation()
    }

    private fun setupDetailData(data: StatusTiket) {
        // set bacground color
        var themeColor = R.color.blue_status_tiket
        var themeColorBackground = R.drawable.background_rounded_all_blue_opacity
        // change background color based on statusId
        when (data.statusId) {
            "1" -> {
                tvStatusTicket.setBackgroundResource(themeColorBackground)
                tvStatusTicket.setTextColor(ContextCompat.getColor(this, themeColor))
                // setup layout
//                btnProcessTicket.visibility = View.VISIBLE
            }
            "2" -> {
                themeColor = R.color.yellow_status_tiket
                themeColorBackground = R.drawable.background_rounded_all_yellow_opacity
                tvStatusTicket.setBackgroundResource(themeColorBackground)
                tvStatusTicket.setTextColor(ContextCompat.getColor(this, themeColor))
//                // setup layout
//                btnProcessTicket.visibility = View.GONE
//                layoutProgressStatus1.visibility = View.VISIBLE
            }
            else -> {
                themeColor = R.color.green_status_tiket
                themeColorBackground = R.drawable.background_rounded_all_green_opacity
                tvStatusTicket.setBackgroundResource(themeColorBackground)
                tvStatusTicket.setTextColor(ContextCompat.getColor(this, themeColor))
//                // setup layout
//                btnProcessTicket.visibility = View.GONE
//                layoutProgressStatus1.visibility = View.GONE
            }
        }

        // get detail PIC Name
        listProgress = dataFromIntent.historyProgressTicket!!
        // index for
        var lastIndex = listProgress.size

        lastIndex = if (lastIndex > 0) {
            listProgress.size - 1
        } else {
            0
        }
        for (i in 0 until listProgress.size) {
            var isActive = false
            val name = listProgress[i].userName
            val status = listProgress[i].statusName
            val date = listProgress[i].statusDate
//            val tittle = "Ticket $status"

            Log.d(tags, "index : $i + $lastIndex")

            if (lastIndex == i) {
                Log.d(tags, " Masuk lastIndex")
                tvDetailPic.text = name.toString()
                isActive = true
            }
        }


        // set data to XML
        tvDetailNoTiket.text = data.ticketId
        tvStatusTicket.text = data.statusName
        tvDetailAddress.text = data.locationName
        tvDetailAddress2.text = ""
        tvDetailBidang.text = data.bidangName
        tvDetailMasalah.text = data.SubBidangName
        tvDetailDescription.text = data.description
//        tvDetailPic.text = data.historyUsername

    }

    private fun validationField(): Boolean {
        return FormValidator.getInstance()
            .addField(
                edt_reason_tiket,
                NonEmptyRule(R.string.ERROR_FIELD_REQUIRED)
            )
            .validate()
    }

    private fun popupBottomSheetConfitmation() {
        Log.e(tags, "method : confirmation")
        //init bottomSheet
        val views = layoutInflater.inflate(R.layout.bottomsheet_corfirmation, null)
        bottomSheetDialog.setContentView(views)
        views.tvTitttleConfirm.text = "Konfirmasi"
        views.tvContentConfirm.text = "Apakah Anda yakin alasan sudah benar?"
        views.btnNoConfirm.text = "Batal"
        views.btnYesConfirm.text = "Ya"
        bottomSheetDialog.show()

        //onclick bottomsheet
        views.btnYesConfirm.setOnClickListener(this)
        views.btnNoConfirm.setOnClickListener(this)

    }

    private fun popupBottomSheetInformation() {
        Log.e(tags, "method : info")
        val views = layoutInflater.inflate(R.layout.bottomsheet_information, null)
        bottomSheetDialog.setContentView(views)
        views.tvTittleInfo.text = "Pengajuan Terkirim"
        views.tvContentInfo.text = "Laporan Anda akan segera ditindaklanjuti maksimal 20 menit."
        Glide.with(this)
            .load(R.drawable.illustration_container)
            .into(views.imgInfo)
        views.btnInfo.text = "Oke"
        bottomSheetDialog.show()

        //setOnclick bottomsheet
        views.btnInfo.setOnClickListener(this)

    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBareopen.visibility = View.VISIBLE
        } else {
            progressBareopen.visibility = View.GONE
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
