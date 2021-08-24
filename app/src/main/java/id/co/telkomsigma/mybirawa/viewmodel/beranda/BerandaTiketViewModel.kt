package id.co.telkomsigma.mybirawa.viewmodel.beranda

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.telkomsigma.mybirawa.controller.home.fragmentHome.BerandaFragment
import id.co.telkomsigma.mybirawa.entity.BerandaTiket
import id.co.telkomsigma.mybirawa.entity.Inbox
import id.co.telkomsigma.mybirawa.entity.Version
import org.json.JSONObject

class BerandaTiketViewModel : ViewModel() {
    val tag = BerandaFragment::class.java.simpleName
    private val listData = MutableLiveData<ArrayList<BerandaTiket>>()
    private val listVersion = MutableLiveData<ArrayList<Version>>()
    private val listNews = MutableLiveData<ArrayList<Inbox>>()

    fun setData(server: String, token: String, role: String, user_id: String, fmbm: String) {
        val listTiket = ArrayList<BerandaTiket>()
        val listBgColor = intArrayOf(
            Color.parseColor("#F5F6FC"),
            Color.parseColor("#FEF7EA"),
            Color.parseColor("#F3FBEF"),
            Color.parseColor("#FCEFEC"),
            Color.parseColor("#F6F6F8"),
            Color.parseColor("#DFF1F2")
        )

        val listBgColorIcon = intArrayOf(
            Color.parseColor("#b3cdd4ff"),
            Color.parseColor("#b3f5cd7c"),
            Color.parseColor("#c6e9bc"),
            Color.parseColor("#f8c9c4"),
            Color.parseColor("#d1d1e1"),
            Color.parseColor("#d1e1d5")
        )

//        Log.e(tag ,"http://dev-api-birawa.digitalevent.id/api/ticket/ticket-by-bidang")
//        Log.e(tag,"$server/ticket/ticket-by-bidang")

        var requestURL = "$server/ticket/ticket-by-bidang?chusr=$user_id"
        if (role == "3" || role == "4") {
            requestURL = "$server/ticket/ticket-by-fmbm?chusr=$user_id&fmbm=$fmbm"
        }

//        Log.e(tag ,"Request URL beranda: $requestURL")

        AndroidNetworking.get(requestURL)
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d(tag, response.toString())
                    if (response?.getBoolean("status") == true) {
                        val status = response.getBoolean("status")
                        val message = response.getString("message")
                        val arrayData = response.getJSONArray("data")
                        for (i in 0 until arrayData.length()) {

                            val objData = arrayData.getJSONObject(i)

                            val bidangId = objData.getInt("bidang_id")
                            val bidangName = objData.getString("bidang_name")
                            val open = objData.getInt("open")
                            val progress = objData.getInt("progress")
                            val done = objData.getInt("done")
                            val closed = objData.getInt("closed")
                            val color = listBgColor[i]
                            val bgColorIcon = listBgColorIcon[i]
                            val imageUrl = objData.getString("url")
                            Log.e(tag, "color :  $color")

                            val data = BerandaTiket(
                                status,
                                message,
                                bidangId.toString(),
                                bidangName,
                                open,
                                progress,
                                done,
                                closed,
                                imageUrl,
                                color,
                                bgColorIcon
                            )
//                            val data  = BerandaTiket(status,message,bidangId,bidangName,open,progress)
                            listTiket.add(data)
                        }
                        listData.postValue(listTiket)
                    }
                }

                override fun onError(anError: ANError?) {
                    val act = "List_tiket_beranda_byBidang"
                    errorLog(anError, act)
                }

            })

    }

    fun setVersion(server: String, token: String) {
        val requestURL = "$server/auth/version"
        val tokenRequest = token

        val listVer = ArrayList<Version>()

        AndroidNetworking.get(requestURL)
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $tokenRequest")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d(tag, "versionResponse : ${response.toString()}")

                    if (response?.getBoolean("status") == true) {
                        val message = response.getString("message")
                        val data = response.getJSONObject("data")

                        if (data.length() > 0) {
                            val beginDate = data.getString("begda")
                            val endDate = data.getString("endda")
                            val versionNote = data.getString("version_note")
                            val versionCode = data.getString("version_code")
                            val versionNumber = data.getString("version_number")
                            val url = data.getString("version_url")
                            val flag = data.getString("version_flag")
                            val changeDate = data.getString("chgdt")
                            val chusr = data.getInt("chusr")

                            Log.d(tag, "version : $versionNote , $versionCode")

                            val version = Version(versionNote, versionCode, changeDate, url)
                            listVer.add(version)
                        }

                        listVersion.postValue(listVer)
                    }

                }

                override fun onError(anError: ANError?) {
                    val act = "Versi Aplikasi"
                    errorLog(anError, act)
                }

            })

    }

    fun setNews(server: String, token: String, role: String, user_id: String, fmbm: String) {
        val listInbox = ArrayList<Inbox>()

        val requestURL = "$server/inbox"

//        Log.e(tag ,"Request URL beranda: $requestURL")

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
//                    Log.d(tag,"responseNews : ${response.toString()}")
                    if (response?.getBoolean("status") == true) {
                        val arrayData = response.getJSONArray("data")
                        if (arrayData.length() > 0) {
//                            val maxLengh  = if (arrayData.length() > 1){
//                                arrayData.length() - 1
//                            }else{
//                                0
//                            }
//                            Log.d(tag, "arrayLengh : $maxLengh")
//                            for (i in maxLengh until arrayData.length()){

                            // karena data udh di sort paling atas adalah yang terbaru, jd ambil data pertama aja
                            val obj = arrayData.getJSONObject(0)

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
                            Log.d(tag, "dataNews : $data")
                            listInbox.add(data)
//                            }

                        }

                        listNews.postValue(listInbox)
                    }
                }

                override fun onError(anError: ANError?) {
                    val act = "News "
                    errorLog(anError, act)
                }

            })

    }

    fun getVersion(): LiveData<ArrayList<Version>> {
        return listVersion
    }

    fun getData(): LiveData<ArrayList<BerandaTiket>> {
        return listData

    }

    fun getNews(): LiveData<ArrayList<Inbox>> {
        return listNews
    }

    private fun errorLog(anError: ANError?, act: String) {
        Log.e(tag, "error $act : $anError")
        Log.e(tag, "error $act : ${anError?.response}")
        Log.e(tag, "error $act : ${anError?.errorBody}")
    }

}