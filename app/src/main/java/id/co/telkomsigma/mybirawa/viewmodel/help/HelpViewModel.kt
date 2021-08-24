package id.co.telkomsigma.mybirawa.viewmodel.help

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.telkomsigma.mybirawa.controller.home.beranda.help.HelpActivity
import id.co.telkomsigma.mybirawa.entity.Help
import org.json.JSONObject

class HelpViewModel : ViewModel() {

    private val TAG = HelpActivity::class.java.simpleName
    val listHelp = MutableLiveData<ArrayList<Help>>()

    fun setHelp(server: String, token: String) {

        val list = ArrayList<Help>()

        val requestUrl = "$server/help"
        AndroidNetworking.get(requestUrl)
            .addHeaders(
                "Accept",
                "application/json"
            ) // bidang : Semua Bidang, Sipil, M/E, House Keeping
            .addHeaders("Authorization", "Bearer $token")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d(TAG, "responsHelp : $response")

                    if (response?.getBoolean("status") == true) {
                        val dataHelp = response.getJSONArray("data")
                        if (dataHelp.length() > 0) {
                            for (i in 0 until dataHelp.length()) {
                                val index = dataHelp.getJSONObject(i)

                                val address = index.getString("deskirpsi")
                                val email = index.getString("email")
                                val kontak = index.getString("kontak")

                                val data = Help(address, email, kontak)
                                list.add(data)
                            }
                            listHelp.postValue(list)
                        }

                    }
                }

                override fun onError(anError: ANError?) {
                    val act = "HelpActivity"
                    errorLog(anError, act)
                }

            })
    }

    fun getHelp(): LiveData<ArrayList<Help>> {
        return listHelp
    }

    private fun errorLog(anError: ANError?, act: String) {
        Log.e(TAG, "error $act : $anError")
        Log.e(TAG, "error $act : ${anError?.response}")
        Log.e(TAG, "error $act : ${anError?.errorBody}")
    }

}