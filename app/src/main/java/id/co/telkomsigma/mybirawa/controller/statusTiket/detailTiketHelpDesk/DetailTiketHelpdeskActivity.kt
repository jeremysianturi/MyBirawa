package id.co.telkomsigma.mybirawa.controller.statusTiket.detailTiketHelpDesk

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.github.dhaval2404.form_validation.rule.NonEmptyRule
import com.github.dhaval2404.form_validation.validation.FormValidator
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import es.dmoral.toasty.Toasty
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.adapter.MyAdapter
import id.co.telkomsigma.mybirawa.controller.home.HomeActivity
import id.co.telkomsigma.mybirawa.controller.statusTiket.detailTiket.DetailTiketActivity
import id.co.telkomsigma.mybirawa.entity.ListProgressTiket
import id.co.telkomsigma.mybirawa.entity.PreferenceModel
import id.co.telkomsigma.mybirawa.entity.StatusTiket
import id.co.telkomsigma.mybirawa.util.UserPreference
import id.co.telkomsigma.mybirawa.util.setLocalImage
import kotlinx.android.synthetic.main.activity_detail_tiket_helpdesk.*
import kotlinx.android.synthetic.main.activity_detail_tiket_helpdesk.tvDetailAddress
import kotlinx.android.synthetic.main.activity_detail_tiket_helpdesk.tvDetailAddress2
import kotlinx.android.synthetic.main.activity_detail_tiket_helpdesk.tvDetailBidang
import kotlinx.android.synthetic.main.activity_detail_tiket_helpdesk.tvDetailDescription
import kotlinx.android.synthetic.main.activity_detail_tiket_helpdesk.tvDetailMasalah
import kotlinx.android.synthetic.main.activity_detail_tiket_helpdesk.tvDetailNoTiket
import kotlinx.android.synthetic.main.activity_detail_tiket_helpdesk.tvDetailPic
import kotlinx.android.synthetic.main.activity_detail_tiket_helpdesk.tvStatusTicket
import kotlinx.android.synthetic.main.activity_ulang_tiket.*
import kotlinx.android.synthetic.main.bottomsheet_corfirmation.view.*
import kotlinx.android.synthetic.main.bottomsheet_information.view.*
import org.json.JSONObject
import java.io.File

class DetailTiketHelpdeskActivity : AppCompatActivity(), View.OnClickListener {

    private val tags = DetailTiketHelpdeskActivity::class.java.simpleName

    companion object {
        private const val PROFILE_IMAGE_REQ_CODE = 101
        const val EXTRA_DATA = "extra_data"
//        const val EXTRA_DATA_PROGRESS = "extra_data_progress"
    }

    private var uploadFoto: Int? = 0

    //bottomSheet
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private lateinit var mUserPreference: UserPreference
    private lateinit var session: PreferenceModel


    private var mListProgressTiket: ListProgressTiket? = null
    private lateinit var listProgress: List<ListProgressTiket>
    private var mProfileFile: File? = null
    private lateinit var mMyAdapter: MyAdapter

    var ticketIdPost: String? = null
    private lateinit var phoneNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tiket_helpdesk)

        // define SharedFreference
        mUserPreference = UserPreference(this)
        session = mUserPreference.getUser()

        uploadFoto = 0

        bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialog)

        //recieve data from previous activity
        val dataFromIntent =
            intent.getParcelableExtra(DetailTiketActivity.EXTRA_DATA) as StatusTiket

        if (dataFromIntent.historyProgressTicket.isNullOrEmpty()) {
            Log.d(tags, "null data array")
        } else {
            ticketIdPost = dataFromIntent.ticketId
            showProgressTicket(dataFromIntent)
        }

        // onclick listener
        layout_picker_photo_helpdesk.setOnClickListener(this)
        btn_ganti_foto_helpdesk.setOnClickListener(this)
        btnProcessTicket.setOnClickListener(this)
        btnLaporanSelesai.setOnClickListener(this)
        imgCallHelpdesk.setOnClickListener(this)

        // call method
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

        for (i in 0 until listProgress.size) {
            var isActive = false
            val name = listProgress[i].userName
            val status = listProgress[i].statusName
            val date = listProgress[i].statusDate
            val phoneNum = listProgress[i].noTlp
//            val tittle = "Ticket $status"

            Log.d(tags, "index : $i + $lastIndex")

            if (lastIndex == i) {
                Log.d(tags, " Masuk_lastIndex $phoneNum")
                tvDetailPic.text = name.toString()
                phoneNumber = phoneNum.toString()
                isActive = true
            }
//            val items = MyAdapter.MyItem(isActive,date.toString(),tittle,name.toString())
            val items =
                MyAdapter.MyItem(isActive, status.toString(), date.toString(), name.toString())
            listItem.add(items)
            mListProgressTiket = ListProgressTiket(status, name)
        }
        mMyAdapter = MyAdapter(listItem)
        sequenceLayout.setAdapter(mMyAdapter)
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
                btnProcessTicket.visibility = View.VISIBLE
            }
            "2" -> {
                themeColor = R.color.yellow_status_tiket
                themeColorBackground = R.drawable.background_rounded_all_yellow_opacity
                tvStatusTicket.setBackgroundResource(themeColorBackground)
                tvStatusTicket.setTextColor(ContextCompat.getColor(this, themeColor))
                // setup layout
                btnProcessTicket.visibility = View.GONE
                layoutProgressStatus1.visibility = View.VISIBLE
            }
            else -> {
                themeColor = R.color.green_status_tiket
                themeColorBackground = R.drawable.background_rounded_all_green_opacity
                tvStatusTicket.setBackgroundResource(themeColorBackground)
                tvStatusTicket.setTextColor(ContextCompat.getColor(this, themeColor))
                // setup layout
                btnProcessTicket.visibility = View.GONE
                layoutProgressStatus1.visibility = View.GONE
                layoutDone.visibility = View.VISIBLE
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

        //done
        tvDescriptionHasilPekerjaan.text = data.report
        Glide.with(this)
            .load(data.evidence)
            .error(R.drawable.illustration_container_tidak_ada_jaringan)
            .into(imgDetailTicker)

//        tvDetailPic.text = data.historyUsername

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.layout_picker_photo_helpdesk -> {
                pickPhoto()
            }
            R.id.btn_ganti_foto_helpdesk -> {
                pickPhoto()
            }
            R.id.btnProcessTicket -> {
                popupBottomSheetConfitmation()
            }
            R.id.btnLaporanSelesai -> {
                if (validationFieldLaporanSeselsai()) {
                    isvalidationFieldLaporanSelesai()
                }
            }
            R.id.imgCallHelpdesk -> {
                val number = phoneNumber
                Log.e(tags, "number_Telp : $number")
                val mIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
                startActivity(mIntent)
            }
            R.id.btnInfo -> {
                bottomSheetDialog.dismiss()
                val mIntent = Intent(this, HomeActivity::class.java)
                startActivity(mIntent)
            }
            R.id.btnNoConfirm -> {
                bottomSheetDialog.dismiss()
            }
            R.id.btnYesConfirm -> {
                bottomSheetDialog.dismiss()
                submitProcessTicket()
            }
        }
    }

    private fun validationFieldLaporanSeselsai(): Boolean {
        return FormValidator.getInstance()
            .addField(
                edt_helpdesk,
                NonEmptyRule(R.string.ERROR_FIELD_REQUIRED)
            )
            .validate()
    }

    private fun isvalidationFieldLaporanSelesai() {

        if (uploadFoto == 0) {
            Toasty.error(this, "Foto tidak boleh kosong. ", Toast.LENGTH_SHORT).show()
        } else {
            showLoading(true)
            submitLaporanSelesai()
        }
    }

    private fun submitLaporanSelesai() {
        btnLaporanSelesai.isEnabled = false
        Log.d(tags, "masuk post laporan selesai")

        val token = session.token
        val userId = session.userId
        val txtReport = edt_helpdesk.text.toString()

        val requestURL =
            "${session.server}/ticket/change-status?ticket_id=${ticketIdPost.toString()}&user_id=$userId"
        AndroidNetworking.upload(requestURL)
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .addMultipartFile("evidence", mProfileFile)
            .addMultipartParameter("report", txtReport)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d(tags, "LaporanSelesai : ${response.toString()}")
                    if (response?.getBoolean("status") == true) {
                        val message = response.getString("message")
                        showLoading(false)
                        Toasty.success(
                            this@DetailTiketHelpdeskActivity,
                            message,
                            Toasty.LENGTH_SHORT
                        ).show()
                        btnLaporanSelesai.isEnabled = true
                        val mIntent =
                            Intent(this@DetailTiketHelpdeskActivity, HomeActivity::class.java)
                        startActivity(mIntent)
                    } else {
                        showLoading(false)
                        btnLaporanSelesai.isEnabled = true
                        val message = response?.getString("message")
                        Toasty.error(
                            this@DetailTiketHelpdeskActivity,
                            message.toString(),
                            Toasty.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onError(anError: ANError?) {
                    showLoading(false)
                    btnLaporanSelesai.isEnabled = true
                    Toasty.error(
                        this@DetailTiketHelpdeskActivity,
                        "Gagal, Periksa koneksi internet dan coba beberapa saat lagi",
                        Toasty.LENGTH_SHORT
                    ).show()
                    val act = "postLaporanSelesai"
                    errorLog(anError, act)
                }

            })
    }

    private fun submitProcessTicket() {
        showLoading(true)
        val token = session.token
        val userId = session.userId

        AndroidNetworking.post("${session.server}/ticket/change-status?ticket_id=${ticketIdPost.toString()}&user_id=$userId")
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d(tags, "responseSubmitProgressTicket : ${response.toString()}")
                    if (response?.getBoolean("status") == true) {
                        val message = response.getString("message")
                        val status = response.getBoolean("status")
                        showLoading(false)
                        popupBottomSheetInformation()
                    } else {
                        val message = response?.getString("message")
                        showLoading(false)
                        Toasty.error(
                            this@DetailTiketHelpdeskActivity,
                            message.toString(),
                            Toasty.LENGTH_SHORT
                        ).show()
                    }

                }

                override fun onError(anError: ANError?) {
                    showLoading(false)
                    Toasty.error(
                        this@DetailTiketHelpdeskActivity,
                        "Gagal, Periksa koneksi internet dan coba beberapa saat lagi",
                        Toasty.LENGTH_SHORT
                    ).show()
                    val act = "detailPIC"
                    errorLog(anError, act)
                }

            })
    }

    private fun pickPhoto() {
        ImagePicker.with(this)
            .setImageProviderInterceptor { imageProvider -> // Intercept ImageProvider
                Log.d("ImagePicker", "Selected ImageProvider: " + imageProvider.name)
            }
            .maxResultSize(1080, 1920)
            .compress(1024)
            .start(PROFILE_IMAGE_REQ_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                Log.e("ImagePickerDetail", "Path:${ImagePicker.getFilePath(data)}")
                // setting visibility layout
                layout_picker_photo_helpdesk.visibility = View.GONE
                tv_tiitile_uploadphoto.visibility = View.GONE
                img_result_photo.visibility = View.VISIBLE
                tv_name_image_helpdesk.visibility = View.VISIBLE
                btn_ganti_foto_helpdesk.visibility = View.VISIBLE

                val file = ImagePicker.getFile(data)!!
                when (requestCode) {
                    PROFILE_IMAGE_REQ_CODE -> {
                        mProfileFile = file
                        img_result_photo.setLocalImage(file, false)
                        tv_name_image_helpdesk.text = ImagePicker.getFilePath(data)
                        uploadFoto = 1
                    }
                }
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun popupBottomSheetInformation() {
        Log.e(tags, "method : info")
        val views = layoutInflater.inflate(R.layout.bottomsheet_information, null)
        bottomSheetDialog.setContentView(views)
        views.tvTittleInfo.text = "Tiket Diproses"
        views.tvContentInfo.text =
            "Segera lakukan perbaikan yang diminta dan selesaikan dengan baik dan benar"
        Glide.with(this)
            .load(R.drawable.illustration_container)
            .into(views.imgInfo)
        views.btnInfo.text = "Oke"
        bottomSheetDialog.show()

        //setOnclick bottomsheet
        views.btnInfo.setOnClickListener(this)

    }

    private fun popupBottomSheetConfitmation() {
        Log.e(tags, "method : confirmation")
        //init bottomSheet
        val views = layoutInflater.inflate(R.layout.bottomsheet_corfirmation, null)
        bottomSheetDialog.setContentView(views)
        views.tvTitttleConfirm.text = "Konfirmasi"
        views.tvContentConfirm.text = "Apakah Anda yakin untuk memproses tiket?"
        views.btnNoConfirm.text = "Batal"
        views.btnYesConfirm.text = "Ya"
        bottomSheetDialog.show()

        //onclick bottomsheet
        views.btnYesConfirm.setOnClickListener(this)
        views.btnNoConfirm.setOnClickListener(this)

    }

    private fun errorLog(anError: ANError?, act: String) {
        Log.e(tags, "error $act : $anError")
        Log.e(tags, "error $act : ${anError?.response}")
        Log.e(tags, "error $act : ${anError?.errorBody}")
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBarDetailTicketHelpdesk.visibility = View.VISIBLE
        } else {
            progressBarDetailTicketHelpdesk.visibility = View.GONE
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}