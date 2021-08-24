package id.co.telkomsigma.mybirawa.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PreferenceModel(
    var deviceId: String? = null,
    var firebaseToken: String? = null,
    var name: String? = null,
    var server: String? = null,
    var email: String? = null,
    var password: String? = null,
    var phone: String? = null,
    var rememberLogin: Boolean = false,
    var token: String? = null,
    var tokenType: String? = null,
    var roleId: String? = null,
    var roleName: String? = null,
    var fmbmId: String? = null,
    var fmbmName: String? = null,
    var cityId: String? = null,
    var cityName: String? = null,
    var userId: String? = null,
    var avatar: String? = null,
    var username: String? = null,
    var isLogin: Boolean = false,
//    var userlogin : String? = null,
//    var userpassword : String? = null,
    var subunit: String? = null,
    var versiApp: String? = null,
    var subRoleBidang: String? = null
) : Parcelable