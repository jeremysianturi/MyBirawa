package id.co.telkomsigma.mybirawa.viewmodel.beranda

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.telkomsigma.mybirawa.controller.home.fragmentHome.InboxFragment
import id.co.telkomsigma.mybirawa.entity.Inbox
import org.json.JSONObject

class InboxViewModel : ViewModel() {
    val tag = InboxFragment::class.java.simpleName
    private val listData = MutableLiveData<ArrayList<Inbox>>()


    fun setData(server: String, token: String, role: String, user_id: String, fmbm: String) {
        val listInbox = ArrayList<Inbox>()

        val requestURL = "$server/inbox"

//        Log.e(tag ,"Request URL Inbox: $requestURL")
//        Log.d(tag, "params : userId = $user_id , roles : $role , fmbm : $fmbm ")


        AndroidNetworking.post(requestURL)
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .addBodyParameter("user_id", user_id)
            .addBodyParameter("roles", role)
            .addBodyParameter("fmbm", fmbm)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
//                    Log.d(tag,"responseInbox : ${response.toString()}")
                    if (response?.getBoolean("status") == true) {
                        val arrayData = response.getJSONArray("data")
                        if (arrayData.length() > 0) {

                            for (i in 0 until arrayData.length()) {
                                val obj = arrayData.getJSONObject(i)

                                val fmbmId = obj.getInt("fmbm").toString()
                                val bidangId = obj.getInt("bidang").toString()
                                val ticketId = obj.getString("ticket_id")
                                val otych = obj.getString("otych")
                                val userId = obj.getInt("user_id").toString()
                                val subject = obj.getString("subject")
                                val massage = obj.getString("massage")
                                val changeDate = obj.getString("chgdt")
                                val chusr = obj.getInt("chusr").toString()
                                val url = obj.getString("url")

                                val data = Inbox(
                                    fmbmId,
                                    bidangId,
                                    ticketId,
                                    otych,
                                    userId,
                                    subject,
                                    massage,
                                    changeDate,
                                    chusr,
                                    url
                                )
                                listInbox.add(data)
                            }

                        }

                        listData.postValue(listInbox)
                    }
                }

                override fun onError(anError: ANError?) {
                    val act = "Inbox "
                    errorLog(anError, act)
                }

            })

    }


    fun getData(): LiveData<ArrayList<Inbox>> {
        return listData

    }

    private fun errorLog(anError: ANError?, act: String) {
        Log.e(tag, "error $act : $anError")
        Log.e(tag, "error $act : ${anError?.response}")
        Log.e(tag, "error $act : ${anError?.errorBody}")
    }

}