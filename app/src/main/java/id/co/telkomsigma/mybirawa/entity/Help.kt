package id.co.telkomsigma.mybirawa.entity

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Help(
    var alamat: String? = null,
    var email: String? = null,
    var phoneNumber: String? = null
) : Parcelable