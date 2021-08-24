package id.co.telkomsigma.mybirawa.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GedungPerangkat(
    var gedungId: Int? = null,
    var gedungName: String? = null,
    var gedungAddress: String? = null,
    var gedungLatitude: String? = null,
    var gedungLongitude: String? = null
) : Parcelable