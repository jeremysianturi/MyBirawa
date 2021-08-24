package id.co.telkomsigma.mybirawa.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailPerangkat(
    var perangkatJenisId: Int? = null,
    var perangkatJenisName: String? = null,
    var perangkatJenisBelum: Int? = null,
    var urlImage: String? = null

) : Parcelable
