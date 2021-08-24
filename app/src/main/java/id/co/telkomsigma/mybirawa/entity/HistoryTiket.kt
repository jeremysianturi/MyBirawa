package id.co.telkomsigma.mybirawa.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HistoryTiket(
    var statusResponse: String? = null,
    var message: String? = null,
    var userId: String? = null,
    var userName: String? = null,
    var ticketId: String? = null,
    var bidangId: String? = null,
    var bidangName: String? = null,
    var subBidangId: String? = null,
    var SubBidangName: String? = null,
    var locationType: String? = null,
    var locationId: String? = null,
    var locationName: String? = null,
    var statusId: String? = null,
    var statusName: String? = null,
    var description: String? = null,
    var createdDate: String? = null,
    var imgURL: String? = null,
    var historyStatusId: String? = null,
    var historyStatusName: String? = null,
    var historyStatusDate: String? = null,
    var historyUserId: String? = null,
    var historyUsername: String? = null,
    var historyProgressTicket: List<ListProgressTiket>? = null
) : Parcelable