package id.co.telkomsigma.mybirawa.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LantaiPerangkat(
    var lantaiid: Int? = null,
    var lantaiName: String? = null
) : Parcelable