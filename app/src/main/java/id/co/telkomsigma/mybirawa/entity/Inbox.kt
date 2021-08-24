package id.co.telkomsigma.mybirawa.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Inbox(
    var fmbmId: String? = null,
    var bidangId: String? = null,
    var ticketId: String? = null,
    var otych: String? = null,
    var userId: String? = null,
    var subject: String? = null,
    var message: String? = null,
    var chageDate: String? = null,
    var chusr: String? = null,
    var url: String? = null
) : Parcelable