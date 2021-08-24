package id.co.telkomsigma.mybirawa.controller.statusTiket.Rating

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.github.dhaval2404.form_validation.rule.MaxLengthRule
import com.github.dhaval2404.form_validation.rule.MinLengthRule
import com.github.dhaval2404.form_validation.rule.NonEmptyRule
import com.github.dhaval2404.form_validation.validation.FormValidator
import com.google.android.material.bottomsheet.BottomSheetDialog
import es.dmoral.toasty.Toasty
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.controller.home.HomeActivity
import id.co.telkomsigma.mybirawa.entity.PreferenceModel
import id.co.telkomsigma.mybirawa.entity.StatusTiket
import id.co.telkomsigma.mybirawa.util.UserPreference
import kotlinx.android.synthetic.main.activity_detail_tiket_helpdesk.*
import kotlinx.android.synthetic.main.activity_rating_tiket.*
import kotlinx.android.synthetic.main.bottomsheet_information.view.*
import org.json.JSONObject

class RatingTiketActivity : AppCompatActivity(), View.OnClickListener {

    private val tags = RatingTiketActivity::class.java.simpleName

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    private lateinit var mUserPreference: UserPreference
    private lateinit var session: PreferenceModel

    //bottomSheet
    private lateinit var bottomSheetDialog: BottomSheetDialog

    // init variable StatusTicket Model
    private lateinit var dataFromIntent: StatusTiket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating_tiket)

        // recieve data from previous activity
        dataFromIntent = intent.getParcelableExtra(EXTRA_DATA) as StatusTiket

        // declaration bottom sheet dialog
        bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialog)

        // define SharedFreference
        mUserPreference = UserPreference(this)
        session = mUserPreference.getUser()

        tv_rating_name.text = dataFromIntent.userName
        tv_rating_jabatan.text = dataFromIntent.bidangName


        ratingBar.setOnClickListener(this)
        btn_rating_kirim.setOnClickListener(this)
        //setup Actionbar and navigasi up
        val actionbar = supportActionBar
        actionbar?.title = "Rating"
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ratingBar -> {
            }
            R.id.btn_rating_kirim -> {
                if (validationField()) {
                    isValidField()
                }
            }
        }
    }

    private fun isValidField() {
        val indexRating = ratingBar.rating.toInt()
        Log.d(tags, "indexRating : $indexRating")
        if (indexRating == 0) {
            Toasty.error(this, "Rating tidak boleh 0 ", Toasty.LENGTH_SHORT).show()
        } else {
            submitTicketToClosed()

        }
    }

    private fun validationField(): Boolean {
        return FormValidator.getInstance()
            .addField(
                tilEdtUlasanRating,
                NonEmptyRule(R.string.ERROR_FIELD_REQUIRED),
                MinLengthRule(5, "Minimal 5 karakter"),
                MaxLengthRule(200, "Maksimal 200 karakter")
            )
            .validate()
    }

    private fun submitTicketToClosed() {
        showLoading(true)

        val userId = session.userId
        val ticketId = dataFromIntent.ticketId //data dari previous activity
        val token = session.token
        val txtUlasan = edtUlasanRating.text.toString()
        val strRating = ratingBar.rating.toInt()

        val postData = JSONObject()
        postData.put("rating", strRating)
        postData.put("ulasan", txtUlasan)

        Log.d(tags, "postData : $postData")
        val requestURL =
            "${session.server}/ticket/change-status?ticket_id=$ticketId&user_id=$userId"

        Log.d(tags, requestURL)

        AndroidNetworking.post(requestURL)
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .addBodyParameter("rating", strRating.toString())
            .addBodyParameter("ulasan", txtUlasan)
//            .addJSONObjectBody(postData)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d(tags, "Rating : ${response.toString()}")
                    if (response?.getBoolean("status") == true) {
//                        val message = response.getString("message")
                        showLoading(false)
                        popupBottomSheetInformation()
                    } else {
                        showLoading(false)
                        val message = response?.getString("message")
                        Toasty.error(
                            this@RatingTiketActivity,
                            message.toString(),
                            Toasty.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onError(anError: ANError?) {
                    showLoading(false)
                    Toasty.error(
                        this@RatingTiketActivity,
                        "Gagal, Periksa koneksi internet dan coba beberapa saat lagi",
                        Toasty.LENGTH_SHORT
                    ).show()
                    val act = "postRatingByCustomer"
                    errorLog(anError, act)
                }

            })
    }

    private fun popupBottomSheetInformation() {
        Log.e(tags, "method : info")
        val views = layoutInflater.inflate(R.layout.bottomsheet_information, null)
        bottomSheetDialog.setContentView(views)
        views.tvTittleInfo.text = "Tiket Berakhir"
        views.tvContentInfo.text = "Perbaikan yang diminta sedah diselesaikan dengan baik dan benar"
        Glide.with(this)
            .load(R.drawable.illustration_container)
            .into(views.imgInfo)
        views.btnInfo.text = "Oke"
        bottomSheetDialog.show()

        //setOnclick bottomsheet
        views.btnInfo.setOnClickListener {
            bottomSheetDialog.dismiss()
            val mIntent = Intent(this, HomeActivity::class.java)
            startActivity(mIntent)
        }

    }

    private fun errorLog(anError: ANError?, act: String) {
        Log.e(tags, "error $act : $anError")
        Log.e(tags, "error $act : ${anError?.response}")
        Log.e(tags, "error $act : ${anError?.errorBody}")
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            pgBarRating.visibility = View.VISIBLE
        } else {
            pgBarRating.visibility = View.GONE
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
