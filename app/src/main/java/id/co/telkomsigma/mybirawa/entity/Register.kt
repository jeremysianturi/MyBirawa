package id.co.telkomsigma.mybirawa.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Register(
    var nama: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var unit: String? = null,
    var unitName: String? = null,
    var city: String? = null,
    var address: String? = null,
    var password: String? = null,
    var rePassword: String? = null

) : Parcelable