package id.co.telkomsigma.mybirawa.controller.register.fragmentRegister

import `in`.galaxyofandroid.spinerdialog.SpinnerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.dhaval2404.form_validation.rule.LengthRule
import com.github.dhaval2404.form_validation.rule.MaxLengthRule
import com.github.dhaval2404.form_validation.rule.MinLengthRule
import com.github.dhaval2404.form_validation.rule.NonEmptyRule
import com.github.dhaval2404.form_validation.validation.FormValidator
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.entity.*
import id.co.telkomsigma.mybirawa.util.RegisterPreference
import id.co.telkomsigma.mybirawa.util.UserPreference
import id.co.telkomsigma.mybirawa.viewmodel.RegisterDataViewModel
import kotlinx.android.synthetic.main.fragment_register_two.*


class RegisterTwoFragment : Fragment(), View.OnClickListener {

    private val TAG = RegisterTwoFragment::class.java.simpleName


    private lateinit var name: String
    private lateinit var phone: String
    private lateinit var email: String

    private var unitId: String? = null
    private var unitName: String? = null
    private var subUnitid: String? = null
    private var cityId: String? = null
    private var cityName: String? = null


    private var register: Register? = Register()

    // unit
    private lateinit var registerUnit: RegisterUnit
    private val listRegisterUnitName = ArrayList<String>()
    private val listRegisterUnitModel = ArrayList<RegisterUnit>()

    // subUnit
    private lateinit var registerSubUnit: RegisterSubUnit
    private val listRegisterSubUnitName = ArrayList<String>()
    private val listRegisterSubUnitModel = ArrayList<RegisterSubUnit>()

    // subUnit
    private lateinit var registerCity: RegisterCity
    private val listRegisterCitytName = ArrayList<String>()
    private val listRegisterCitytModel = ArrayList<RegisterCity>()

    private lateinit var mRegisterDataViewModel: RegisterDataViewModel

    private lateinit var mUserPreference: UserPreference
    private lateinit var session: PreferenceModel

    private lateinit var mRegisterPreference: RegisterPreference
    private lateinit var modelRegisterSession: RegisterPreferenceModel

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_two, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            register = arguments?.getParcelable(EXTRA_DATA)
            name = register?.nama.toString()
            phone = register?.phone.toString()
            email = register?.email.toString()
            Log.e(TAG, "data_fragment_one : ${register?.nama}, ${register?.email} ")

        }

        mUserPreference = UserPreference(requireActivity())
        session = mUserPreference.getUser()

        mRegisterPreference = RegisterPreference(requireActivity())
        modelRegisterSession = mRegisterPreference.getData()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // onclick listener
        btn_next_register_two.setOnClickListener(this)
        btn_back_register_two.setOnClickListener(this)
        txt_unit.setOnClickListener(this)
//        txt_subunit.setOnClickListener(this)
        txt_city.setOnClickListener(this)

        dataFragmentOne()
        dataSession()

    }

    private fun dataSession() {
        if (modelRegisterSession.address.isNullOrEmpty()) {
            Log.d(TAG, "data Fragment 2 kosong")
        } else {
            txt_unit.text = modelRegisterSession.unitName
            unitName = modelRegisterSession.unitName
            unitId = modelRegisterSession.unitId
            edtSubUnit.setText(modelRegisterSession.suUnit)
            txt_city.text = modelRegisterSession.cityName
            cityName = modelRegisterSession.cityName
            cityId = modelRegisterSession.cityId
            edt_address_register.setText(modelRegisterSession.address)
        }

    }

    private fun dataFragmentOne() {
        if (modelRegisterSession.nama.isNullOrEmpty()) {
            Log.d(TAG, "data kosong")
        } else {
            name = modelRegisterSession.nama.toString()
            phone = modelRegisterSession.phone.toString()
            email = modelRegisterSession.email.toString()
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mRegisterDataViewModel = ViewModelProvider(this).get(RegisterDataViewModel::class.java)
        mRegisterDataViewModel.setData(session.server.toString(), session.token.toString())

        // ambil data unit dari view model
        mRegisterDataViewModel.getUnitData().observe(viewLifecycleOwner, Observer { model ->
            if (model != null) {
                // clear data agar tidak double
                listRegisterUnitModel.clear()
                listRegisterUnitName.clear()

                // looping data dari viewmodel class
                for (i in 0 until model.size) {
                    val mNama = model[i].name
                    val mId = model[i].id

                    listRegisterUnitName.add(mNama.toString())
                    registerUnit = RegisterUnit(mId, mNama)
                    listRegisterUnitModel.add(registerUnit)
                }

            }

            // ambil data subUnit dari viewmodel
            mRegisterDataViewModel.getSubUnitData()
                .observe(viewLifecycleOwner, Observer { modelSubUnit ->
                    if (modelSubUnit != null) {
                        // clear data agar tidak double
                        listRegisterSubUnitModel.clear()
                        listRegisterSubUnitName.clear()

                        // looping data dari viewmodel class
                        for (j in 0 until modelSubUnit.size) {
                            val name = modelSubUnit[j].name
                            val id = modelSubUnit[j].id

                            listRegisterSubUnitName.add(name.toString())
                            registerSubUnit = RegisterSubUnit(id, name)
                            listRegisterSubUnitModel.add(registerSubUnit)
                        }
                    }
                })
            // ambil data city dari view model
            mRegisterDataViewModel.getCityData().observe(viewLifecycleOwner, Observer { modelCity ->
                if (modelCity != null) {
                    // clear data agar tidak double
                    listRegisterCitytModel.clear()
                    listRegisterCitytName.clear()

                    // looping data dari viewmodel class
                    for (k in 0 until modelCity.size) {
                        val name = modelCity[k].name
                        val id = modelCity[k].id

                        listRegisterCitytName.add(name.toString())
                        registerCity = RegisterCity(id, name)
                        listRegisterCitytModel.add(registerCity)

                    }
                }

            })
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_next_register_two -> {
                if (validationField()) {
                    isValidField()
                }
            }
            R.id.btn_back_register_two -> {
                activity?.onBackPressed()
            }
            R.id.txt_unit -> {
                val spinnerDialog = SpinnerDialog(
                    requireActivity(),
                    listRegisterUnitName,
                    getString(R.string.txt_spinner_dialog_tittle),
                    R.style.DialogAnimations_SmileWindow
                )
                spinnerDialog.bindOnSpinerListener { s, i ->
                    Log.e(
                        tag,
                        "$tag -> cekdata id : ${listRegisterUnitModel[i].id} , nama :${listRegisterUnitModel[i].name}"
                    )
                    val name = listRegisterUnitModel[i].name.toString().trim()
                    val id = listRegisterUnitModel[i].id.toString().trim()


                    txt_unit.text = name
                    unitId = id
                    unitName = name

                }
                spinnerDialog.showSpinerDialog()
            }
            R.id.txt_city -> {
                val spinnerDialog = SpinnerDialog(
                    requireActivity(),
                    listRegisterCitytName,
                    "Select item :",
                    R.style.DialogAnimations_SmileWindow
                )
                spinnerDialog.bindOnSpinerListener { s, i ->
                    val name = listRegisterCitytModel[i].name.toString()
                    val id = listRegisterCitytModel[i].id.toString()

                    txt_city.text = name
                    cityId = id
                    cityName = name

                }
                spinnerDialog.showSpinerDialog()
            }

        }
    }

    private fun isValidField() {

        val unit = unitId.toString().trim()
        val subUnit = edtSubUnit.text.toString().trim()
        val city = cityId.toString().trim()
        val address = edt_address_register.text.toString().trim()

        Log.e(tag, " Valid_Field : $tag =   $unit, $subUnit, $city")

        saveData(unit, subUnit, city, address)
    }

    private fun validationField(): Boolean {
        return FormValidator.getInstance()
            .addField(
                txt_unit,
                NonEmptyRule(R.string.ERROR_FIELD_REQUIRED)
            )
            .addField(
                txt_subunit,
                NonEmptyRule(R.string.ERROR_FIELD_REQUIRED)
            )
            .addField(
                txt_city,
                NonEmptyRule(R.string.ERROR_FIELD_REQUIRED)
            )
            .addField(
                textInputLayout_address,
                NonEmptyRule(R.string.ERROR_FIELD_REQUIRED),
                MinLengthRule(5, R.string.ERROR_FIELD_PASWWORD_LENGH_TOO_SHORT),
                MaxLengthRule(200, R.string.ERROR_FIELD_ADDRESS_LENGH_MAX)
            )
            .validate()

    }

    private fun saveData(unit: String, subunit: String, city: String, address: String) {

        val mRegisterThreeFragment = RegisterThreeFragment()
        val data = Register()
        data.nama = name
        data.phone = phone
        data.email = email
        data.unit = unit
        data.unitName = subunit
        data.city = city
        data.address = address

        modelRegisterSession.unitId = unit
        modelRegisterSession.unitName = unitName
        modelRegisterSession.suUnit = subunit
        modelRegisterSession.cityId = city
        modelRegisterSession.cityName = cityName
        modelRegisterSession.address = address
        mRegisterPreference.setData(modelRegisterSession)

        val mBundle = Bundle()
        mBundle.putParcelable(RegisterThreeFragment.EXTRA_DATA, data)
        mRegisterThreeFragment.arguments = mBundle

        val mFragmentManager = parentFragmentManager
        mFragmentManager.commit {
            addToBackStack(null)
            replace(
                R.id.frame_container,
                mRegisterThreeFragment,
                RegisterThreeFragment::class.java.simpleName
            )
        }
    }

}
