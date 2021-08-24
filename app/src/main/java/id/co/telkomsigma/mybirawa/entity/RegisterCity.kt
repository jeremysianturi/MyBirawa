package id.co.telkomsigma.mybirawa.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RegisterCity(
    var id: String? = null,
    var name: String? = null

) : Parcelable