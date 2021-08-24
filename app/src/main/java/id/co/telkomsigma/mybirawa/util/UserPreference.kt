package id.co.telkomsigma.mybirawa.util

import android.content.Context
import androidx.core.content.edit
import id.co.telkomsigma.mybirawa.entity.PreferenceModel

internal class UserPreference(context: Context) {

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val SERVER_URL = "server_url"
        private const val TOKEN = "token"
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
        private const val PHONE_NUMBER = "phone"
        private const val REMEMBER_LOGIN = "remember_login"
        private const val TOKEN_TYPE = "token_type"
        private const val ROLE_ID = "role_id"
        private const val ROLE_NAME = "role_name"
        private const val SUB_ROLE_ID = "sub_role_id"
        private const val SUB_ROLE_NAME = "sub_role_name"
        private const val FM_BM_ID = "fm_bm_id"
        private const val FM_BM_NAME = "fm_bm_name"
        private const val CITY_ID = "area_id"
        private const val CITY_NAME = "area_name"
        private const val USER_ID = "user_id"
        private const val USER_NAME = "user_name"
        private const val AVATAR = "avatar"
        private const val DEVICE_ID = "device_id"
        private const val FIREBASETOKEN = "firebase_token"

        //        private const val USER_LOGIN = "user_login"
//        private const val USER_PASSWORD = "user_password"
        private const val SUB_UNIT = "subunit"
        private const val ISLOGIN = "islogin"
        private const val VERSION_APP = "versi_app"
        private const val BIDANG_SUB_ROLE = "bidang_sub_role"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(value: PreferenceModel) {
        preferences.edit {
            putString(DEVICE_ID, value.deviceId)
            putString(FIREBASETOKEN, value.firebaseToken)
            putString(TOKEN, value.token)
            putString(NAME, value.name)
            putString(EMAIL, value.email)
            putString(PASSWORD, value.password)
            putString(PHONE_NUMBER, value.phone)
            putString(SERVER_URL, value.server)
            putBoolean(REMEMBER_LOGIN, value.rememberLogin)
            putString(TOKEN_TYPE, value.tokenType)
            putString(ROLE_ID, value.roleId)
            putString(ROLE_NAME, value.roleName)
            putString(FM_BM_ID, value.fmbmId)
            putString(FM_BM_NAME, value.fmbmName)
            putString(CITY_ID, value.cityId)
            putString(CITY_NAME, value.cityName)
            putString(USER_ID, value.userId)
            putString(AVATAR, value.avatar)
            putString(USER_NAME, value.username)
//            putString(USER_LOGIN, value.userlogin)
//            putString(USER_PASSWORD, value.userpassword)
            putString(SUB_UNIT, value.subunit)
            putBoolean(ISLOGIN, value.isLogin)
            putString(VERSION_APP, value.versiApp)
            putString(BIDANG_SUB_ROLE, value.subRoleBidang)
        }
    }

    fun getUser(): PreferenceModel {
        val model = PreferenceModel()
        model.token = preferences.getString(TOKEN, "")
        model.name = preferences.getString(NAME, "")
        model.email = preferences.getString(EMAIL, "")
        model.password = preferences.getString(PASSWORD, "")
        model.server = preferences.getString(SERVER_URL, "")
        model.rememberLogin = preferences.getBoolean(REMEMBER_LOGIN, false)
        model.tokenType = preferences.getString(TOKEN_TYPE, "")
        model.roleId = preferences.getString(ROLE_ID, "")
        model.roleName = preferences.getString(ROLE_NAME, "")
        model.fmbmId = preferences.getString(FM_BM_ID, "")
        model.fmbmName = preferences.getString(FM_BM_NAME, "")
        model.cityId = preferences.getString(CITY_ID, "")
        model.cityName = preferences.getString(CITY_NAME, "")
        model.userId = preferences.getString(USER_ID, "")
        model.phone = preferences.getString(PHONE_NUMBER, "")
        model.avatar = preferences.getString(AVATAR, "")
        model.username = preferences.getString(USER_NAME, "")
        model.deviceId = preferences.getString(DEVICE_ID, "")
        model.firebaseToken = preferences.getString(FIREBASETOKEN, "")
//        model.userlogin = preferences.getString(USER_LOGIN,"")
//        model.userpassword = preferences.getString(USER_PASSWORD,"")
        model.subunit = preferences.getString(SUB_UNIT, "")
        model.isLogin = preferences.getBoolean(ISLOGIN, false)
        model.versiApp = preferences.getString(VERSION_APP, "")
        model.subRoleBidang = preferences.getString(BIDANG_SUB_ROLE, "")
        return model
    }
}