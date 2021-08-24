package id.co.telkomsigma.mybirawa.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BerandaTiket(
    var status: Boolean? = false,
    var message: String? = null,
    var bidangId: String? = null,
    var bidangName: String? = null,
    var baru: Int? = null,
    var berjalan: Int? = null,
    var selesai: Int? = null,
    var tutup: Int? = null,
    var image: String? = null,
    var bgColor: Int? = null,
    var bgColorIcon: Int? = null
) : Parcelable