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
import id.co.telkomsigma.mybirawa.entity.Perangkat
import org.json.JSONObject

class PerangkatViewModel : ViewModel() {
    val tags = PerangkatActivity::class.java.simpleName
    private val listPerangkat = MutableLiveData<ArrayList<Perangkat>>()

    fun setData(url: String, token: String, fmbm: String, bidangRequest: String) {

        val urlRequest = "$url/dashboard_perangkat?fmbm=$fmbm&bidang=$bidangRequest"
        val listData = ArrayList<Perangkat>()

        AndroidNetworking.get(urlRequest)
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d(tags, "perangkatResponse : ${response.toString()}")
                    if (response?.getBoolean("status") == true) {
                        val arrayData = response.getJSONArray("data")
                        for (i in 0 until arrayData.length()) {
                            val objData = arrayData.getJSONObject(i)

                            val tanggal = objData.getString("tanggal")
                            val idPeriode = objData.getInt("id_periode")
                            val selisihHari = objData.getString("selisihhari")
                            val periode = objData.getString("Periode")
                            val belum = objData.getInt("belum")
                            val sudah = objData.getInt("sudah")
                            val total = objData.getInt("total")
                            val percent = objData.getString("percent")
                            val urlImage = objData.getString("url")

                            val data = Perangkat(
                                tanggal,
                                idPeriode,
                                selisihHari,
                                periode,
                                belum,
                                sudah,
                                total,
                                percent,
                                urlImage
                            )
                            listData.add(data)
                        }
                        listPerangkat.postValue(listData)

                    }
                }

                override fun onError(anError: ANError?) {
                    val act = "ListPerangkat"
                    errorLog(anError, act)
                }

            })

    }

    fun getData(): LiveData<ArrayList<Perangkat>> {
        return listPerangkat
    }

    private fun errorLog(anError: ANError?, act: String) {
        Log.e(tags, "error $act : $anError")
        Log.e(tags, "error $act : ${anError?.response}")
        Log.e(tags, "error $act : ${anError?.errorBody}")
    }

}