package id.co.telkomsigma.mybirawa.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Perangkat(
    val tanggal: String? = null,
    val idPeriode: Int? = null,
    val selisihhari: String? = null,
    val periode: String? = null,
    val belum: Int? = null,
    val sudah: Int? = null,
    val total: Int? = null,
    val percent: String? = null,
    val url: String? = null

) : Parcelable
