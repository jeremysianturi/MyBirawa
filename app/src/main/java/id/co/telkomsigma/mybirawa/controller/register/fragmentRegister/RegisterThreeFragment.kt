package id.co.telkomsigma.mybirawa.controller.register.fragmentRegister

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.github.dhaval2404.form_validation.constant.PasswordPattern
import com.github.dhaval2404.form_validation.rule.EqualRule
import com.github.dhaval2404.form_validation.rule.MinLengthRule
import com.github.dhaval2404.form_validation.rule.NonEmptyRule
import com.github.dhaval2404.form_validation.rule.PasswordRule
import com.github.dhaval2404.form_validation.validation.FormValidator
import es.dmoral.toasty.Toasty
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.controller.login.LoginActivity
import id.co.telkomsigma.mybirawa.entity.PreferenceModel
import id.co.telkomsigma.mybirawa.entity.Register
import id.co.telkomsigma.mybirawa.entity.RegisterPreferenceModel
import id.co.telkomsigma.mybirawa.util.RegisterPreference
import id.co.telkomsigma.mybirawa.util.UserPreference
import kotlinx.android.synthetic.main.fragment_register_three.*
import kotlinx.android.synthetic.main.fragment_register_three.progressBar
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 */
class RegisterThreeFragment : Fragment(), View.OnClickListener {

    private val tags = RegisterThreeFragment::class.java.simpleName

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    private lateinit var mUserPreference: UserPreference
    private lateinit var session: PreferenceModel

    private lateinit var mRegisterPreference: RegisterPreference
    private lateinit var modelRegisterSession: RegisterPreferenceModel

    private var register: Register? = Register()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_three, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            register = arguments?.getParcelable(EXTRA_DATA)
            Log.e(tags, "data_fragment_three ${register?.nama}, ${register?.unit}")
        }

        // application session
        mUserPreference = UserPreference(requireActivity())
        session = mUserPreference.getUser()

        // register session
        mRegisterPreference = RegisterPreference(requireActivity())
        modelRegisterSession = mRegisterPreference.getData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_back_register_three.setOnClickListener(this)
        btn_register.setOnClickListener(this)

        dataSession()
    }

    private fun dataSession() {
        if (modelRegisterSession.password.isNullOrEmpty()) {
            Log.d(tags, "data PASSWORD KOSONG")
        } else {
            edt_create_password.setText(modelRegisterSession.password)
            edt_recreate_password.setText(modelRegisterSession.rePassword)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_register -> {
                if (validationField()) {
                    isValidField()
                }
            }
            R.id.btn_back_register_three -> {
                activity?.onBackPressed()
            }
        }
    }

    private fun validationField(): Boolean {
        return FormValidator.getInstance()
            .addField(
                textInputLayout_create_password,
                NonEmptyRule(R.string.ERROR_FIELD_REQUIRED),
                MinLengthRule(6, R.string.ERROR_FIELD_PASWWORD_LENGH_NOT_VALID),
                PasswordRule(
                    PasswordPattern.ALPHA_NUMERIC,
                    R.string.ERROR_FIELD_PASWWORD_PATTERN_IS_NOT_VALID
                )
            )
            .addField(
                textInputLayout_recreate_password,
                NonEmptyRule(R.string.ERROR_FIELD_REQUIRED),
                MinLengthRule(6, R.string.ERROR_FIELD_PASWWORD_LENGH_NOT_VALID),
                EqualRule(
                    edt_create_password.text.toString(),
                    R.string.ERROR_FIELD_PASWWORD_MATCH_NOT_VALID
                ),
                PasswordRule(
                    PasswordPattern.ALPHA_NUMERIC,
                    R.string.ERROR_FIELD_PASWWORD_PATTERN_IS_NOT_VALID
                )
            )
            .validate()
    }

    private fun isValidField() {
        val password = edt_create_password.text.toString().trim()
        val rePassword = edt_recreate_password.text.toString().trim()
        saveData(password, rePassword)
    }

    private fun saveData(password: String, rePassword: String) {
        modelRegisterSession.password = password
        modelRegisterSession.rePassword = rePassword
        mRegisterPreference.setData(modelRegisterSession)
        sendData(password)
    }

    private fun sendData(password: String) {
        showLoading(true)
        val dataRegister = register
        // put data to JSONobject
        val putDataregister = JSONObject()
        putDataregister.put("email", dataRegister?.email)
        putDataregister.put("phone_number", dataRegister?.phone)
        putDataregister.put("name", dataRegister?.nama)
        putDataregister.put("unit_id", dataRegister?.unit)
        putDataregister.put("subunit", dataRegister?.unitName)
        putDataregister.put("city", dataRegister?.city)
        putDataregister.put("address", dataRegister?.address)
        putDataregister.put("password", password)

        Log.e(tag, "data_post : ${dataRegister?.email}")

        AndroidNetworking.post(session.server + "/auth/register")
            .addHeaders("Accept", "application/json")
            .addJSONObjectBody(putDataregister)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.e(tag, "respone : ${response.toString()}")
                    Log.e(tag, "respone : ${response?.getString("message").toString()}")
                    if (response?.getBoolean("status") != false) {
                        val message = response?.getString("message")
                        showLoading(false)

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
                        popUpDialog(message.toString())
                    } else {
                        showLoading(false)
                        val message = response.getString("message")
                        Toasty.error(
                            requireContext(),
                            message.toString().trim(),
                            Toasty.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onError(anError: ANError?) {
                    showLoading(false)
                    Toasty.error(
                        requireContext(),
                        "Error : Coba beberapa saat lagi",
                        Toasty.LENGTH_SHORT
                    ).show()
                    errorLog(anError)
                }

            })
    }

    private fun popUpDialog(massage: String) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.layout_popupdialog_register)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window!!.attributes = lp
        dialog.setTitle("Input Code Here")
        val btnYes =
            dialog.findViewById<View>(R.id.btn_yes) as Button
        val tvText = dialog.findViewById<View>(R.id.txt_subtittle_popup) as TextView
        tvText.text = massage
        val tvTittle = dialog.findViewById<View>(R.id.txt_tittle_popup) as TextView
        tvTittle.text = getString(R.string.str_registrasi_berhasil)
        dialog.show()
        dialog.setCancelable(false)
        btnYes.setOnClickListener {
            dialog.dismiss()
            val mIntent = Intent(activity, LoginActivity::class.java)
            startActivity(mIntent)
        }
    }

    private fun errorLog(anError: ANError?) {
        Log.e(tags, "$tags -> error : $anError")
        Log.e(tags, "$tags -> error : ${anError?.response}")
        Log.e(tags, "$tags -> error : ${anError?.errorBody}")
//        Toasty.error(requireContext(), anError?.errorBody.toString(),Toasty.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

}
