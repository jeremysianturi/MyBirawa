package id.co.telkomsigma.mybirawa.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Faq(
    val icon: String? = null,
    val tittle: String? = null,
    val description: String? = null
) : Parcelable