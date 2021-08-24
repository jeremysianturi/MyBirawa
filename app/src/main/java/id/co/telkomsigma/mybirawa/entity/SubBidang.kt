package id.co.telkomsigma.mybirawa.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class SubBidang(
    var subBidangId: String? = null,
    var name: String? = null,
    var bidangName: String? = null,
    var bidangId: String? = null
) : Parcelable