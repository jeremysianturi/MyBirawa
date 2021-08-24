package id.co.telkomsigma.mybirawa.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Version(
    val versionNote: String? = null,
    val versionCode: String? = null,
    val changeDate: String? = null,
    val url: String? = null

) : Parcelable