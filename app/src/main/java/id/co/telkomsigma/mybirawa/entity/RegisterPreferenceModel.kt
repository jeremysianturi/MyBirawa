package id.co.telkomsigma.mybirawa.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RegisterPreferenceModel(
    var nama: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var unitId: String? = null,
    var unitName: String? = null,
    var suUnit: String? = null,
    var cityId: String? = null,
    var cityName: String? = null,
    var address: String? = null,
    var password: String? = null,
    var rePassword: String? = null
) : Parcelable