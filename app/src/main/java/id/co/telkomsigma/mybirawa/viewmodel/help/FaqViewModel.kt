package id.co.telkomsigma.mybirawa.viewmodel.help

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.telkomsigma.mybirawa.controller.home.beranda.formisian.FormulirIsianActivity
import id.co.telkomsigma.mybirawa.controller.home.beranda.help.FaqActivity
import id.co.telkomsigma.mybirawa.entity.Bidang
import id.co.telkomsigma.mybirawa.entity.Faq
import id.co.telkomsigma.mybirawa.entity.Gedung
import id.co.telkomsigma.mybirawa.entity.SubBidang
import org.json.JSONObject

class FaqViewModel : ViewModel() {
    private val tag = FaqActivity::class.java.simpleName

    private val listFaq = MutableLiveData<ArrayList<Faq>>()

    fun setFaq(url: String, token: String) {

        //declaration new array list
        val arrayFaq = ArrayList<Faq>()
        arrayFaq.clear()

        val tittle = "Jenis kerusakan instalasi listrik?"
        val content = "List Secondary Text Goes Here"

        val data = Faq("", tittle, content)
        arrayFaq.add(data)

        listFaq.postValue(arrayFaq)

//        AndroidNetworking.get("$url/ticket/data-gedung?id=$idKota")
//            .addHeaders("Authorization","Bearer $token")
//            .setPriority(Priority.MEDIUM)
//            .build()
//            .getAsJSONObject(object : JSONObjectRequestListener{
//                override fun onResponse(response: JSONObject?) {
////                    Log.d(tag,response.toString())
//                    if (response?.getBoolean("status")==true){
//                        val arrayData = response.getJSONArray("data")
//                        for (i in 0 until arrayData.length()){
//                            val itemObject = arrayData.getJSONObject(i)
//
//                            val gedungId = itemObject.getInt("subbindang_id")
//                            val gedungName = itemObject.getString("role_name")
//
//
//                            val strGedungId = gedungId.toString()
//
//                            val dataJSON = Gedung(strGedungId, gedungName)
//                            arrayGedung.add(dataJSON)
//                        }
//                        listGedung.postValue(arrayGedung)
//                    }
//                }
//
//                override fun onError(anError: ANError?) {
//                    val act = "FormIsian SubBidang"
//                    errorLog(anError,act)
//                }
//
//            })

    }

    fun getDataFaq(): LiveData<ArrayList<Faq>> {
        return listFaq
    }

    private fun errorLog(anError: ANError?, act: String) {
        Log.e(tag, "error $act : $anError")
        Log.e(tag, "error $act : ${anError?.response}")
        Log.e(tag, "error $act : ${anError?.errorBody}")
    }

}