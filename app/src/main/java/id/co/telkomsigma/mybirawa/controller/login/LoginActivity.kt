package id.co.telkomsigma.mybirawa.controller.login

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.github.dhaval2404.form_validation.rule.EmailRule
import com.github.dhaval2404.form_validation.rule.NonEmptyRule
import com.github.dhaval2404.form_validation.validation.FormValidator
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import es.dmoral.toasty.Toasty
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.controller.home.HomeActivity
import id.co.telkomsigma.mybirawa.controller.register.RegisterActivity
import id.co.telkomsigma.mybirawa.entity.PreferenceModel
import id.co.telkomsigma.mybirawa.entity.RegisterPreferenceModel
import id.co.telkomsigma.mybirawa.util.Animatoo
import id.co.telkomsigma.mybirawa.util.RegisterPreference
import id.co.telkomsigma.mybirawa.util.UserPreference
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.progressBar
import kotlinx.android.synthetic.main.fragment_beranda.*
import org.json.JSONObject
import java.util.*
import kotlin.system.exitProcess


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private val tags = LoginActivity::class.java.simpleName

    companion object {
        const val REQUEST_CODE = 1001
    }

    //session
    private lateinit var mUserPreference: UserPreference
    private lateinit var session: PreferenceModel

    // register session
    private lateinit var mRegisterPreference: RegisterPreference
    private lateinit var modelRegisterSession: RegisterPreferenceModel

    // deviceId
    private lateinit var telephonyManager: TelephonyManager
    private var doubleBackToExitPressedOnce = false
    private var firebaseToken: String? = null
    private var uuid: String? = null

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //biar keyboard ga lgsg popup
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        // define SharedFreference
        mUserPreference = UserPreference(this)
        session = mUserPreference.getUser()

        // animation
        val topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        val bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)
        val rightAnim = AnimationUtils.loadAnimation(this, R.anim.right_animation)
        val leftAnim = AnimationUtils.loadAnimation(this, R.anim.left_animation)

        // hooks
        imageView4.animation = topAnim
        txt_tittle_login.animation = topAnim
        img_logo_login.animation = leftAnim


//        if (session.username != null){
//            Log.d(tags,"username tidak kosong")
//            edt_email_login.setText(session.userlogin)
//            edt_password_login.setText(session.userpassword)
//        }else{
//            Log.d(tags,"username kosong")
//        }

        if (session.rememberLogin) {
            edt_email_login.setText(session.username)
            edt_password_login.setText(session.password)
            checkBox_remember_login.isChecked = true
        } else {
            edt_email_login.setText("")
            edt_password_login.setText("")
            checkBox_remember_login.isChecked = false
        }

        // onClick
        btn_login.setOnClickListener(this)
        txt_lupa_sandi.setOnClickListener(this)
        txt_register.setOnClickListener(this)
        checkBox_remember_login.setOnClickListener(this)

        // call method
        requestPermission()

        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

//        Log.d(tags,"session data : ${session.firebaseToken} , ${session.deviceId}")

        uuid = if (session.deviceId.isNullOrEmpty()) {
            Log.d(tags, " masuk method tanpa data device id")
            UUID.randomUUID().toString()
        } else {
            Log.d(tags, " ada data device id")
            session.deviceId
        }

        if (session.firebaseToken.isNullOrEmpty()) {
            Log.d(tags, " masuk method tanpa data device id")
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(tags, "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result?.token
                    firebaseToken = token


                    // Log and toast
                    val msg = getString(R.string.msg_token_fmt, token)
                    Log.d(tags, msg)
//                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                })

        } else {
            Log.d(tags, " ada data TOKEN FIREBASE ")
            firebaseToken = session.firebaseToken
        }

//        Log.d(tags,"UUID : $uuid ,firebaseTOKEN : $firebaseToken")

//        call method
        clearRegisterSession()


    }

    private fun clearRegisterSession() {
        // register session
        mRegisterPreference = RegisterPreference(this)
        modelRegisterSession = mRegisterPreference.getData()

        modelRegisterSession.nama = ""
        modelRegisterSession.phone = ""
        modelRegisterSession.email = ""
        modelRegisterSession.unitId = ""
        modelRegisterSession.unitName = ""
        modelRegisterSession.suUnit = ""
        modelRegisterSession.cityId = ""
        modelRegisterSession.cityName = ""
        modelRegisterSession.address = ""
        modelRegisterSession.password = ""
        modelRegisterSession.rePassword = ""
        mRegisterPreference.setData(modelRegisterSession)
    }

    override fun onResume() {
        super.onResume()
        clearRegisterSession()
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txt_lupa_sandi -> {
                val mIntentForgetPassword =
                    Intent(this@LoginActivity, ForgetPasswordActivity::class.java)
                startActivity(mIntentForgetPassword)
            }
            R.id.txt_register -> {
                val mIntentRegister = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(mIntentRegister)
            }
            R.id.checkBox_remember_login -> {
//                if (checkBox_remember_login.isChecked){
//                    savePassword = true
//                }else{
//                    savePassword = false
//                }
//                savePassword = checkBox_remember_login.isChecked
            }
            R.id.btn_login -> {
                if (validationField()) {
                    isValidField()
                }
            }
        }
    }

    private fun requestPermission() {
        val PERMISSION_ALL = 1

        val PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INSTALL_PACKAGES,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.REQUEST_INSTALL_PACKAGES,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )

        if (!hasPermissions(this, PERMISSIONS.toString())) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
        }
    }

    private fun hasPermissions(
        context: Context?,
        vararg permissions: String?
    ): Boolean {
        if (context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission!!
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    private fun validationField(): Boolean {
        return FormValidator.getInstance()
            .addField(
                textInputLayout,
                NonEmptyRule(R.string.ERROR_FIELD_REQUIRED),
                EmailRule(R.string.ERROR_FIELD_EMAIL_IS_NOT_VALID)
            )
            .addField(
                textInputLayout2,
                NonEmptyRule(R.string.ERROR_FIELD_REQUIRED)
//                PasswordRule(PasswordPattern.ALPHA_NUMERIC,R.string.ERROR_FIELD_PASWWORD_PATTERN_IS_NOT_VALID),
//                MinLengthRule(6,R.string.ERROR_FIELD_PASWWORD_LENGH_NOT_VALID)
            )
            .validate()
    }

    private fun isValidField() {
        val username = edt_email_login.text.toString()
        val password = edt_password_login.text.toString()

        saveData(username, password)
    }

    private fun saveData(username: String, password: String) {

//        Log.e(tags, "UUID POST : $uuid")
        showLoading(true)
        val dataLogin = JSONObject()
        dataLogin.put("application_id", "1") // default
        dataLogin.put("username", username)
        dataLogin.put("password", password)
        dataLogin.put("platform", "2") // '1' = ios , '2' = android
        dataLogin.put("device_id", uuid) // imei
        dataLogin.put("device_token", firebaseToken) // token fcm

//        Log.d(tags,"submitData : $dataLogin")

//        Log.i(tags,"ServerURL ${session.server}/auth/login")
        AndroidNetworking.post("${session.server}/auth/login")
            .addHeaders("Accept", "application/json")
            .addJSONObjectBody(dataLogin)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
//                    Log.d(tags, "responeLogin :  ${response.toString()}")

                    if (response?.getBoolean("status") == false) {
                        //dismiss loading
                        showLoading(false)

                        val message = response.getString("message")
                        Toasty.error(this@LoginActivity, message, Toasty.LENGTH_SHORT).show()

                    } else {

                        val tokenType = response?.getString("token_type")
//                        val expiresIn = response?.getString("expires_in")
                        val userName = response?.getString("user_name")
                        val userId = response?.getInt("user_id")
                        val roleId =
                            response?.getJSONArray("roles")?.getJSONObject(0)?.getInt("id")
                        val roleName =
                            response?.getJSONArray("roles")?.getJSONObject(0)?.getString("name")
                        val cityId =
                            response?.getJSONArray("city")?.getJSONObject(0)?.getInt("id")
                        val cityName =
                            response?.getJSONArray("city")?.getJSONObject(0)?.getString("name")
//                        val activeSeasons = response?.getString("active_sessions")
                        val token = response?.getString("access_token")
                        val name = response?.getString("name")
                        val phone = response?.getString("phone")
                        val avatar = response?.getString("avatar")
                        val email = response?.getString("email")
                        val subUnit = response?.getString("subunit")
                        val subRole =
                            response?.getJSONArray("subrole")?.getJSONObject(0)?.getString("id")

                        val fmbmObject = response?.getJSONArray("fmbm")
                        var fmbm_id = ""
                        var fmbm_name = ""
                        if (fmbmObject != null) {
                            if (fmbmObject.length() > 0) {
                                fmbm_id =
                                    response.getJSONArray("fmbm").getJSONObject(0)?.getString("id")
                                        .toString()
                                fmbm_name =
                                    response.getJSONArray("fmbm").getJSONObject(0)
                                        ?.getString("name").toString()
                            }
                        }

                        session.token = token
                        session.name = name
                        session.tokenType = tokenType
                        session.roleId = roleId.toString()
                        session.roleName = roleName
                        session.fmbmId = fmbm_id
                        session.fmbmName = fmbm_name
                        session.cityId = cityId.toString()
                        session.userId = userId.toString()
                        session.avatar = avatar.toString()
                        session.username = userName.toString()
                        session.password = edt_password_login.text.toString()
                        session.cityName = cityName.toString()
                        session.phone = phone.toString()
                        session.email = email.toString()
                        session.deviceId = uuid.toString()
                        session.firebaseToken = firebaseToken
                        session.subunit = subUnit.toString()
                        session.rememberLogin = checkBox_remember_login.isChecked
                        session.subRoleBidang = subRole
                        session.isLogin = true

                        mUserPreference.setUser(session)

                        //dismiss loading
                        showLoading(false)

                        val mIntent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(mIntent)

                    }

                }

                override fun onError(anError: ANError?) {
                    //dismiss loading
                    showLoading(false)
                    Toasty.error(
                        this@LoginActivity,
                        "Login gagal, dapatkan bantuan atau coba beberapa saat lagi",
                        Toasty.LENGTH_SHORT
                    ).show()
                    errorLog(anError)
                }

            })

    }

    private fun errorLog(anError: ANError?) {
        Log.e(tags, "$tags -> error : $anError")
        Log.e(tags, "$tags -> error : ${anError?.response}")
        Log.e(tags, "$tags -> error : ${anError?.errorBody}")
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
            exitProcess(0)
        }
        this.doubleBackToExitPressedOnce = true
//        Toast.makeText(this, "Klik sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()
        Toasty.warning(this, "Klik sekali lagi untuk keluar", Toasty.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

}
