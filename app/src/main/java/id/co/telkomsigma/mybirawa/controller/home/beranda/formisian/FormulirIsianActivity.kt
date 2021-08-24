package id.co.telkomsigma.mybirawa.controller.home.beranda.formisian

import `in`.galaxyofandroid.spinerdialog.SpinnerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import id.co.telkomsigma.mybirawa.entity.*
import id.co.telkomsigma.mybirawa.util.UserPreference
import id.co.telkomsigma.mybirawa.viewmodel.FormulirIsianViewModel
import kotlinx.android.synthetic.main.activity_formulir_isian.*
import kotlinx.android.synthetic.main.bottomsheet_corfirmation.view.*
import kotlinx.android.synthetic.main.bottomsheet_information.view.*
import org.json.JSONObject

class FormulirIsianActivity : AppCompatActivity(), View.OnClickListener {

    val tag: String = id.co.telkomsigma.mybirawa.controller.register.RegisterActivity::class.java.simpleName
    private lateinit var mUserPreference: UserPreference
    private lateinit var session: PreferenceModel

    private lateinit var viewModel: FormulirIsianViewModel

    //bottomSheet
    private lateinit var bottomSheetDialog: BottomSheetDialog

    // bidang
    private var strBidangId: String? = null
    private lateinit var bidang: Bidang
    private val listBidang = ArrayList<Bidang>()
    private val listBidangName = ArrayList<String>()

    //subBidang
    private var strSubBidangId: String? = null
    private lateinit var subBidang: SubBidang
    private val listSubBidang = ArrayList<SubBidang>()
    private val listSubBidangName = ArrayList<String>()

    //gedung
    private var strGedungId: String? = null
    private lateinit var gedung: Gedung
    private val listGedung = ArrayList<Gedung>()
    private val listGedungName = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulir_isian)

        //biar keyboard ga lgsg popup
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        // declaration SharedPreference
        mUserPreference = UserPreference(this)
        session = mUserPreference.getUser()
        bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialog)

        // set viewModel
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FormulirIsianViewModel::class.java
        )
        viewModel.setDataSubBidang(
            session.server.toString(),
            session.token.toString(),
            strBidangId.toString()
        )
        viewModel.setDataGedung(
            session.server.toString(),
            session.token.toString(),
            session.cityId.toString()
        )
//        Log.e(tag,"city : ${session.cityId}")

        //onclick listerner
        tvGedungFormulir.setOnClickListener(this)
        tvBidangFormulir.setOnClickListener(this)
        tvSubBidangFormulir.setOnClickListener(this)
        btnSubmitFormIsian.setOnClickListener(this)

        // call method
        getViewModel()
        setupDataTextView()

        //setup Actionbar and navigasi up
        val actionbar = supportActionBar
        actionbar?.title = "Formulir Isian"
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupDataTextView() {
        tvFormName.text = session.name.toString()
        tvFormEmail.text = session.email.toString()
        tvFormUnit.text = session.subunit.toString()
        tvFormAddress.text = session.cityName.toString()
        tvFormPhone.text = session.phone.toString()
    }

    private fun getViewModel() {
        // get data SubBidang from viewModel
        viewModel.getDataSubBidang().observe(this, Observer { data ->
            if (data.isNotEmpty()) {
                //clear data arraylist
                listSubBidang.clear()
                listSubBidangName.clear()

                //looping data from viewmodel
                // set data to entity
                for (i in 0 until data.size) {
                    val mSubidangId = data[i].subBidangId
                    val mName = data[i].name
                    val mBidangName = data[i].bidangName
                    val mBidangId = data[i].bidangId

                    subBidang = SubBidang(mSubidangId, mName, mBidangName, mBidangId)
                    listSubBidang.add(subBidang)
                    listSubBidangName.add(mName.toString())
                }
            }
        })
        //get data Gedung from viewModel
        viewModel.getDataGedung().observe(this, Observer { data ->
            if (data.isNotEmpty()) {
                //clear arraylist
                listGedung.clear()
                listGedungName.clear()

                //looping data from viewmodel
                // set data to entity
                for (i in 0 until data.size) {
                    val mId = data[i].id
                    val mName = data[i].name

                    gedung = Gedung(mId, mName)
                    listGedung.add(gedung)
                    listGedungName.add(mName.toString())

                }

            }
        })

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvGedungFormulir -> {
                val spinnerDialog = SpinnerDialog(
                    this,
                    listGedungName,
                    getString(R.string.txt_spinner_dialog_tittle),
                    R.style.DialogAnimations_SmileWindow
                )
                spinnerDialog.bindOnSpinerListener { s, i ->
                    val mName = listGedung[i].name
                    val mId = listGedung[i].id

                    tvGedungFormulir.text = mName
                    strGedungId = mId
                }
                spinnerDialog.showSpinerDialog()
            }
            R.id.tvSubBidangFormulir -> {
                val spinnerDialog = SpinnerDialog(
                    this,
                    listSubBidangName,
                    getString(R.string.txt_spinner_dialog_tittle),
                    R.style.DialogAnimations_SmileWindow
                )
                spinnerDialog.bindOnSpinerListener { s, i ->
                    val mName = listSubBidang[i].name
                    val mId = listSubBidang[i].subBidangId
                    val mBidangId = listSubBidang[i].bidangId
                    val mBidangName = listSubBidang[i].bidangName

                    tvSubBidangFormulir.text = mName
                    strSubBidangId = mId
                    strBidangId = mBidangId
                    tvBidangFormulir.visibility = View.VISIBLE
                    tvBidangFormulir.text = mBidangName
                }
                spinnerDialog.showSpinerDialog()
            }
            R.id.btnSubmitFormIsian -> {
                if (validationField()) {
                    isValidField()
                }
            }
            R.id.btnYesConfirm -> {
                val userId = session.userId.toString()
                Log.e(tag, "userId : $userId")
                val desc = edtReasonFormIsian.text.toString().trim()
                val locationType = "gedung"

                submitForm(
                    session.server.toString(),
                    session.token.toString(),
                    userId,
                    strBidangId,
                    strSubBidangId,
                    strGedungId,
                    locationType,
                    desc
                )
                bottomSheetDialog.dismiss()
            }
            R.id.btnNoConfirm -> {
                bottomSheetDialog.dismiss()
            }
            R.id.btnInfo -> {
                bottomSheetDialog.dismiss()
                finish()
            }
        }

    }

    private fun validationField(): Boolean {
        return FormValidator.getInstance()
            .addField(
                tvGedungFormulir,
                NonEmptyRule(R.string.ERROR_FIELD_REQUIRED)
            )
            .addField(
                tvBidangFormulir,
                NonEmptyRule(R.string.ERROR_FIELD_REQUIRED)
            )
            .addField(
                tvSubBidangFormulir,
                NonEmptyRule(R.string.ERROR_FIELD_REQUIRED)
            )
            .addField(
                edt_reason_tiket,
                NonEmptyRule(R.string.ERROR_FIELD_REQUIRED)
            )
            .validate()

    }

    private fun isValidField() {
        popupBottomSheetConfitmation()
    }

    private fun popupBottomSheetConfitmation() {
        Log.e(tag, "method : confirmation")
        //init bottomSheet
        val views = layoutInflater.inflate(R.layout.bottomsheet_corfirmation, null)
        bottomSheetDialog.setContentView(views)
        views.tvTitttleConfirm.text = "Konfirmasi"
        views.tvContentConfirm.text = "Apakah Anda yakin isi formulir sudah benar?"
        views.btnNoConfirm.text = "Batal"
        views.btnYesConfirm.text = "Ya"
        bottomSheetDialog.show()

        //onclick bottomsheet
        views.btnYesConfirm.setOnClickListener(this)
        views.btnNoConfirm.setOnClickListener(this)

    }

    private fun popupBottomSheetInformation() {
        Log.e(tag, "method : info")
        val views = layoutInflater.inflate(R.layout.bottomsheet_information, null)
        bottomSheetDialog.setContentView(views)
        views.tvTittleInfo.text = "Formulir Terkirim"
        views.tvContentInfo.text = "Laporan Anda akan segera ditindaklanjuti maksimal 20 menit."
        Glide.with(this)
            .load(R.drawable.illustration_container)
            .into(views.imgInfo)
        views.btnInfo.text = "Oke"
        bottomSheetDialog.show()

        //setOnclick bottomsheet
        views.btnInfo.setOnClickListener(this)

    }

    private fun submitForm(
        server: String,
        token: String,
        userId: String,
        strBidangId: String?,
        strSubBidangId: String?,
        strGedungId: String?,
        strLocationType: String,
        desc: String
    ) {

        showLoading(true)

        val dataForm = JSONObject()
        dataForm.put("user_id", userId)
        dataForm.put("bidang", strBidangId)
        dataForm.put("subbidang", strSubBidangId)
        dataForm.put("location_id", strGedungId)
        dataForm.put("location_type", "gedung")
        dataForm.put("desc", desc)

        println("tokenFormIsian : $token ")
        println("objectPost : $dataForm ")

        AndroidNetworking.post("$server/ticket/submit")
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .addJSONObjectBody(dataForm)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d(tag, "responsePostFormIsian : $response")
                    if (response?.getBoolean("status") == true) {
                        val status = response.getBoolean("status")
                        val message = response.getString("message")
                        showLoading(false)
                        popupBottomSheetInformation()
                    } else {
                        showLoading(false)
                        val message = response?.getString("message")
                        Toasty.error(
                            this@FormulirIsianActivity,
                            message.toString(),
                            Toasty.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onError(anError: ANError?) {
                    bottomSheetDialog.dismiss()
                    Toasty.error(
                        this@FormulirIsianActivity,
                        "Periksa Jaringan Anda",
                        Toasty.LENGTH_SHORT
                    ).show()
                    val act = "submitFormIsian"
                    errorLog(anError, act)
                }

            })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar3.visibility = View.VISIBLE
        } else {
            progressBar3.visibility = View.GONE
        }
    }

    private fun errorLog(anError: ANError?, act: String) {
        Log.e(tag, "error $act : $anError")
        Log.e(tag, "error $act : ${anError?.response}")
        Log.e(tag, "error $act : ${anError?.errorBody}")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
