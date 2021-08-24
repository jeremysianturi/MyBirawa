package id.co.telkomsigma.mybirawa.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Punchlist(
    var noPunchlist: String? = null,
    var gedung: String? = null,
    var lantai: String? = null,
    var bidangName: String? = null,
    var subBidangName: String? = null,
    var status: String? = null,
    var idStatus: String? = null
) : Parcelable