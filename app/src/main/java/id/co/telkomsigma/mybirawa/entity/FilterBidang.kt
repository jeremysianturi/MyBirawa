package id.co.telkomsigma.mybirawa.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterBidang(
    var bidangId: String? = null,
    var bidangName: String? = null,
    var backgoundRed: Boolean? = false
) : Parcelable
