package id.co.telkomsigma.mybirawa.util

import android.content.Context
import androidx.core.content.edit
import id.co.telkomsigma.mybirawa.entity.RegisterPreferenceModel

class RegisterPreference(context: Context) {
    companion object {
        private const val PREFS_REGISTER = "register_pref"
        private const val NAMA = "register_nama"
        private const val NOHP = "register_nohp"
        private const val EMAIL = "register_email"
        private const val UNIT_ID = "register_unit_id"
        private const val UNIT_NAME = "register_unit_name"
        private const val SUB_UNIT = "register_sub_unit"
        private const val CITY_ID = "register_city_name"
        private const val CITY_NAME = "register_city_id"
        private const val ADDRESS = "register_alamat"
        private const val PASSWORD = "register_sandi"
        private const val REPASSWORD = "register_resandi"

    }

    private val preferencesRegister =
        context.getSharedPreferences(PREFS_REGISTER, Context.MODE_PRIVATE)

    fun setData(value: RegisterPreferenceModel) {
        preferencesRegister.edit {
            putString(NAMA, value.nama)
            putString(NOHP, value.phone)
            putString(EMAIL, value.email)
            putString(UNIT_ID, value.unitId)
            putString(UNIT_NAME, value.unitName)
            putString(SUB_UNIT, value.suUnit)
            putString(CITY_ID, value.cityId)
            putString(CITY_NAME, value.cityName)
            putString(ADDRESS, value.address)
            putString(PASSWORD, value.password)
            putString(REPASSWORD, value.rePassword)
        }
    }

    fun getData(): RegisterPreferenceModel {
        val model = RegisterPreferenceModel()
        model.nama = preferencesRegister.getString(NAMA, "")
        model.phone = preferencesRegister.getString(NOHP, "")
        model.email = preferencesRegister.getString(EMAIL, "")
        model.unitId = preferencesRegister.getString(UNIT_ID, "")
        model.unitName = preferencesRegister.getString(UNIT_NAME, "")
        model.suUnit = preferencesRegister.getString(SUB_UNIT, "")
        model.cityId = preferencesRegister.getString(CITY_ID, "")
        model.cityName = preferencesRegister.getString(CITY_NAME, "")
        model.address = preferencesRegister.getString(ADDRESS, "")
        model.password = preferencesRegister.getString(PASSWORD, "")
        model.rePassword = preferencesRegister.getString(REPASSWORD, "")
        return model
    }
}