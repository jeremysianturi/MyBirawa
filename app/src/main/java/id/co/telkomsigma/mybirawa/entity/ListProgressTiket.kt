package id.co.telkomsigma.mybirawa.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.stream.Stream

@Parcelize
data class ListProgressTiket(
    var statusId: String? = null,
    var statusName: String? = null,
    var statusDate: String? = null,
    var userId: String? = null,
    var userName: String? = null,
    var noTlp: String? = null

) : Parcelable