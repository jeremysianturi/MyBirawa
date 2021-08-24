package id.co.telkomsigma.mybirawa.controller.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.github.dhaval2404.form_validation.rule.EmailRule
import com.github.dhaval2404.form_validation.rule.NonEmptyRule
import com.github.dhaval2404.form_validation.validation.FormValidator
import com.google.android.material.bottomsheet.BottomSheetDialog
import es.dmoral.toasty.Toasty
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.entity.PreferenceModel
import id.co.telkomsigma.mybirawa.util.UserPreference
import kotlinx.android.synthetic.main.activity_forget_password.*
import kotlinx.android.synthetic.main.bottomsheet_forgotpassword.view.*
import org.json.JSONObject

class ForgetPasswordActivity : AppCompatActivity(), View.OnClickListener {

    private val tags = ForgetPasswordActivity::class.java.simpleName
    private lateinit var mUserPreference: UserPreference
    private lateinit var session: PreferenceModel

    private lateinit var bottomSheetDialog: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        mUserPreference = UserPreference(this)
        session = mUserPreference.getUser()

        btn_forget_password.setOnClickListener(this)

        //setup Actionbar and navigasi up
        val actionbar = supportActionBar
        actionbar?.title = "Lupa Kata Sandi"
        actionbar?.setDisplayHomeAsUpEnabled(true)

    }

    private fun validationField(): Boolean {
        return FormValidator.getInstance()
            .addField(
                textInputLayoutForgetPassword,
                NonEmptyRule(R.string.ERROR_FIELD_REQUIRED),
                EmailRule(R.string.ERROR_FIELD_EMAIL_IS_NOT_VALID)
            )
            .validate()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_forget_password -> {
                if (validationField()) {
                    submitForgetPassword()
                }
            }
            R.id.btnYesForgot -> {
                bottomSheetDialog.dismiss()
                val mIntent = Intent(this, LoginActivity::class.java)
                startActivity(mIntent)
            }
        }
    }

    private fun popupDialog() {

        // BottomSheetDialog
        bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
        val views = layoutInflater.inflate(R.layout.bottomsheet_forgotpassword, null)
        bottomSheetDialog.setContentView(views)
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.show()

        views.btnYesForgot.setOnClickListener(this)

    }

    private fun submitForgetPassword() {

        val strEmail = edt_email.text.toString().trim()

        AndroidNetworking.post("${session.server}/auth/forgotpass")
            .addHeaders("Accept", "application/json")
            .addBodyParameter("email", strEmail)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d(tags, "responseForgotPassword : ${response.toString()}")
                    if (response?.getBoolean("status") == true) {
                        popupDialog()
                    } else {
                        val msg = response?.getString("message")
                        Toasty.error(
                            this@ForgetPasswordActivity,
                            msg.toString(),
                            Toasty.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onError(anError: ANError?) {
                    val ctx = "forgotPassword"
                    errorLog(anError, ctx)
                }

            })
    }

    private fun errorLog(anError: ANError?, ctx: String) {
        Log.e(tags, "$tags -> error_$ctx : $anError")
        Log.e(tags, "$tags -> error_$ctx : ${anError?.response}")
        Log.e(tags, "$tags -> error_$ctx : ${anError?.errorBody}")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
