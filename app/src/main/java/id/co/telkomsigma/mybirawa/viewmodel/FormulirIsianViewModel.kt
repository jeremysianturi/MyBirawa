package id.co.telkomsigma.mybirawa.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.telkomsigma.mybirawa.controller.home.beranda.formisian.FormulirIsianActivity
import id.co.telkomsigma.mybirawa.entity.Bidang
import id.co.telkomsigma.mybirawa.entity.Gedung
import id.co.telkomsigma.mybirawa.entity.SubBidang
import org.json.JSONObject

class FormulirIsianViewModel : ViewModel() {
    private val tag = FormulirIsianActivity::class.java.simpleName

    val listGedung = MutableLiveData<ArrayList<Gedung>>()
    val listSubBidang = MutableLiveData<ArrayList<SubBidang>>()

    fun setDataSubBidang(url: String, token: String, idBidang: String) {

        //declaration new array list
        val arraySubBidang = ArrayList<SubBidang>()
        arraySubBidang.clear()

        val requestURL = "$url/ticket/data-subbidang"

        AndroidNetworking.get(requestURL)
            .addHeaders("Authorization", "Bearer $token")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
//                    Log.d(tag,"responseMASALAH : $response")
                    if (response?.getBoolean("status") == true) {
                        val arrayData = response.getJSONArray("data")
                        for (i in 0 until arrayData.length()) {
                            val itemObject = arrayData.getJSONObject(i)

                            val subBidangId = itemObject.getInt("subbindang_id").toString()
                            val subBidangName = itemObject.getString("role_name")
                            val bidangId = itemObject.getInt("bindang_id").toString()
                            val bidangName = itemObject.getString("bidang")

                            val dataJSON =
                                SubBidang(subBidangId, subBidangName, bidangName, bidangId)
                            arraySubBidang.add(dataJSON)
                        }
                        listSubBidang.postValue(arraySubBidang)
                    }
                }

                override fun onError(anError: ANError?) {
                    val act = "FormIsian SubBidang"
                    errorLog(anError, act)
                }

            })

    }

    fun setDataGedung(
        url: String,
        token: String,
        idKota: String
    ) {

        //declaration new array list
        val arrayGedung = ArrayList<Gedung>()
        arrayGedung.clear()

        AndroidNetworking.get("$url/ticket/data-gedung?id=$idKota")
            .addHeaders("Authorization", "Bearer $token")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
//                    Log.d(tag,response.toString())
                    if (response?.getBoolean("status") == true) {
                        val arrayData = response.getJSONArray("data")
                        for (i in 0 until arrayData.length()) {
                            val itemObject = arrayData.getJSONObject(i)

                            val gedungId = itemObject.getInt("subbindang_id")
                            val gedungName = itemObject.getString("role_name")


                            val strGedungId = gedungId.toString()

                            val dataJSON = Gedung(strGedungId, gedungName)
                            arrayGedung.add(dataJSON)
                        }
                        listGedung.postValue(arrayGedung)
                    }
                }

                override fun onError(anError: ANError?) {
                    val act = "FormIsian SubBidang"
                    errorLog(anError, act)
                }

            })

    }

    fun getDataSubBidang(): LiveData<ArrayList<SubBidang>> {
        return listSubBidang
    }

    fun getDataGedung(): LiveData<ArrayList<Gedung>> {
        return listGedung
    }

    private fun errorLog(anError: ANError?, act: String) {
        Log.e(tag, "error $act : $anError")
        Log.e(tag, "error $act : ${anError?.response}")
        Log.e(tag, "error $act : ${anError?.errorBody}")
    }

}