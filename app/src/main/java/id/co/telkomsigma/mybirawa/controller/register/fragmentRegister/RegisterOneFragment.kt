package id.co.telkomsigma.mybirawa.controller.register.fragmentRegister

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.github.dhaval2404.form_validation.rule.*
import com.github.dhaval2404.form_validation.validation.FormValidator

import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.entity.Register
import id.co.telkomsigma.mybirawa.entity.RegisterPreferenceModel
import id.co.telkomsigma.mybirawa.util.RegisterPreference
import kotlinx.android.synthetic.main.fragment_register_one.*

/**
 * A simple [Fragment] subclass.
 */
class RegisterOneFragment : Fragment(), View.OnClickListener {

    private val TAG = RegisterOneFragment::class.java.simpleName

    private lateinit var mRegisterPreference: RegisterPreference
    private lateinit var modelRegisterSession: RegisterPreferenceModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mRegisterPreference = RegisterPreference(requireActivity())
        modelRegisterSession = mRegisterPreference.getData()

        btn_next_register_one.setOnClickListener(this)

        sessionregister()

    }

    private fun sessionregister() {
        if (modelRegisterSession.nama!!.isNotEmpty()) {
            edt_name_register.setText(modelRegisterSession.nama)
            edt_phone_register.setText(modelRegisterSession.phone)
            edt_email_register.setText(modelRegisterSession.email)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_next_register_one -> {
                if (validationField()) {
                    isValidField()
                } else {
                    validationField()
                }
            }
        }
    }

    private fun isValidField() {
        val name = edt_name_register.text.toString().trim()
        val phone = edt_phone_register.text.toString().trim()
        val email = edt_email_register.text.toString().trim()

        Log.d(TAG, "dataValid")
        savedata(name, phone, email)

    }

    private fun validationField(): Boolean {
        return FormValidator.getInstance()
            .addField(
                textInputLayout_username,
                NonEmptyRule(R.string.ERROR_FIELD_REQUIRED),
                MinLengthRule(5, R.string.ERROR_FIELD_NAME_LENGH_TOO_SHORT),
                MaxLengthRule(50, R.string.ERROR_FIELD_NAME_LENGH_MAX)
            )
            .addField(
                textInputLayout_phone,
                NonEmptyRule(R.string.ERROR_FIELD_REQUIRED),
                MinLengthRule(10, R.string.ERROR_FIELD_PHONE_NUMBER_LENGH_TOO_SHORT),
                MaxLengthRule(13, R.string.ERROR_FIELD_PHONE_NUMBER_LENGH_TOO_MANY)
            )
            .addField(
                textInputLayout_email,
                NonEmptyRule(R.string.ERROR_FIELD_REQUIRED),
                EmailRule(R.string.ERROR_FIELD_EMAIL_IS_NOT_VALID)
            )
            .validate()
    }

    private fun savedata(name: String, phone: String, email: String) {

        val mRegisterTwoFragment = RegisterTwoFragment()
        val data = Register()
        data.nama = name
        data.phone = phone
        data.email = email

        modelRegisterSession.nama = name
        modelRegisterSession.phone = phone
        modelRegisterSession.email = email
        mRegisterPreference.setData(modelRegisterSession)

        val mBundle = Bundle()
        mBundle.putParcelable(RegisterTwoFragment.EXTRA_DATA, data)
        mRegisterTwoFragment.arguments = mBundle

        val mFragmentManager = parentFragmentManager
        mFragmentManager.commit {
            addToBackStack(null)
            replace(
                R.id.frame_container,
                mRegisterTwoFragment,
                RegisterTwoFragment::class.java.simpleName
            )
        }

    }


}
