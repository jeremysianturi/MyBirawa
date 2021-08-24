package id.co.telkomsigma.mybirawa.viewmodel.perangkat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.telkomsigma.mybirawa.controller.home.beranda.perangkat.PerangkatActivity
import id.co.telkomsigma.mybirawa.entity.DetailPerangkat
import id.co.telkomsigma.mybirawa.entity.GedungPerangkat
import id.co.telkomsigma.mybirawa.entity.LantaiPerangkat
import org.json.JSONObject

class DetailCheckListViewModel : ViewModel() {
    val tags = PerangkatActivity::class.java.simpleName

    private val listDetailPerangkat = MutableLiveData<ArrayList<DetailPerangkat>>()
    private val listGedungPerangkat = MutableLiveData<ArrayList<GedungPerangkat>>()
    private val listLantaiPerangkat = MutableLiveData<ArrayList<LantaiPerangkat>>()

    fun setGedung(
        url: String,
        token: String,
        fmbm: String?,
        subroleBidang: String?,
        periodeId: Int?,
        hari: String?
    ) {

        val urlReq =
            "$url/dashboard_perangkat/getgedung?fmbm=$fmbm&bidang=$subroleBidang&period_id=$periodeId&hari=$hari"

        val listGedung = ArrayList<GedungPerangkat>()

        AndroidNetworking.get(urlReq)
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d(tags, "responseGedungPerangkat : $response")

                    if (response?.getBoolean("status") == true) {
                        val jsonArray = response.getJSONArray("data")
                        if (jsonArray.length() > 0) {
                            for (i in 0 until jsonArray.length()) {
                                val index = jsonArray.getJSONObject(i)

                                val gedungId = index.get("gedung_id") as Int
                                val gedungName = index.getString("gedung_name")
                                val gedungAddress = index.getString("gedung_address")
                                val gedungLat = index.getString("gedung_lat")
                                val gedungLong = index.getString("gedung_long")

                                val gedung = GedungPerangkat(
                                    gedungId,
                                    gedungName,
                                    gedungAddress,
                                    gedungLat,
                                    gedungLong
                                )
                                listGedung.add(gedung)
                            }
                            listGedungPerangkat.postValue(listGedung)
                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    val act = "GedungPerangkat"
                    errorLog(anError, act)
                }

            })
    }

    fun setLantai(
        url: String,
        token: String,
        fmbm: String?,
        subroleBidang: String?,
        periodeId: Int?,
        hari: String?,
        gedungId: Int?
    ) {

        val urlReq =
            "$url/dashboard_perangkat/getlantai?fmbm=$fmbm&bidang=$subroleBidang&period_id=$periodeId&hari=$hari&gedung_id=$gedungId"

        val listLantai = ArrayList<LantaiPerangkat>()

        AndroidNetworking.get(urlReq)
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d(tags, "responseLantaiPerangkat : $response")
                    if (response?.getBoolean("status") == true) {
                        val jsonArray = response.getJSONArray("data")
                        if (jsonArray.length() > 0) {
                            for (i in 0 until jsonArray.length()) {
                                val index = jsonArray.getJSONObject(i)
                                val lantaiId = index.get("lantai_id") as Int
                                val lantainame = index.getString("lantai_name")

                                val lantai = LantaiPerangkat(lantaiId, lantainame)
                                listLantai.add(lantai)
                            }
                            listLantaiPerangkat.postValue(listLantai)
                        }
                    }

                }

                override fun onError(anError: ANError?) {
                    val act = "LantaiPerangkat"
                    errorLog(anError, act)
                }

            })
    }

    fun setPerangkat(
        url: String,
        token: String,
        fmbm: String?,
        subroleBidang: String?,
        periodeId: Int?,
        hari: String?,
        lantaiId: Int?
    ) {
        val urlReq =
            "$url/dashboard_perangkat/getperangkat?fmbm=$fmbm&bidang=$subroleBidang&period_id=$periodeId&hari=$hari&lantai_id=$lantaiId"

        Log.d(tags, "urlPerangkat : $urlReq")
        val perangkat = ArrayList<DetailPerangkat>()

        AndroidNetworking.get(urlReq)
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d(tags, "responsePerangkat : $response")
                    if (response?.getBoolean("status") == true) {
                        val jsonArray = response.getJSONArray("data")
                        if (jsonArray.length() > 0) {
                            for (i in 0 until jsonArray.length()) {
                                val index = jsonArray.getJSONObject(i)

                                val jenisId = index.get("perangkat_jenis_id") as Int?
                                val jenisName = index.getString("perangkat_jenis_name")
                                val jenisBelum = index.get("perangkat_jenis_belum") as Int?
                                val imageUrl = index.getString("logo_jp")

                                val jenisPerangkat =
                                    DetailPerangkat(jenisId, jenisName, jenisBelum, imageUrl)
                                perangkat.add(jenisPerangkat)
                            }
                            listDetailPerangkat.postValue(perangkat)
                        }
                    }

                }

                override fun onError(anError: ANError?) {
                    val act = "LantaiPerangkat"
                    errorLog(anError, act)
                }

            })


    }

    fun getGedung(): LiveData<ArrayList<GedungPerangkat>> {
        return listGedungPerangkat
    }

    fun getLantai(): LiveData<ArrayList<LantaiPerangkat>> {
        return listLantaiPerangkat
    }

    fun getPerangkat(): LiveData<ArrayList<DetailPerangkat>> {
        return listDetailPerangkat
    }

    private fun errorLog(anError: ANError?, act: String) {
        Log.e(tags, "error $act : $anError")
        Log.e(tags, "error $act : ${anError?.response}")
        Log.e(tags, "error $act : ${anError?.errorBody}")
    }

}