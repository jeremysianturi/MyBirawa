package id.co.telkomsigma.mybirawa.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProgressTiket(
    val idStatus: Int? = 0,
    val statusTiket: String? = null,
    val descriptionTiket: String? = null,
    val date: String? = null
) : Parcelable