package id.co.telkomsigma.mybirawa.viewmodel.statusTiketViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.telkomsigma.mybirawa.controller.statusTiket.fragmentStatusTiket.BaruStatusTiketFragment
import id.co.telkomsigma.mybirawa.entity.BerandaTiket
import id.co.telkomsigma.mybirawa.entity.FilterBidang
import id.co.telkomsigma.mybirawa.entity.ListProgressTiket
import id.co.telkomsigma.mybirawa.entity.StatusTiket
import org.json.JSONObject

class BaruStatusTiketViewModel : ViewModel() {

    private val tags = BaruStatusTiketFragment::class.java.simpleName

    private val listData = MutableLiveData<ArrayList<StatusTiket>>()

    private val listFlterData = MutableLiveData<ArrayList<FilterBidang>>()

    fun setFilterData(
        server: String,
        token: String,
        user_id: String,
        role: String,
        fmbm: String,
        dataBidangIntent: String
    ) {

        val listFilter = ArrayList<FilterBidang>()

        var requestURL = "$server/ticket/ticket-by-bidang?chusr=$user_id"
        if (role == "3" || role == "4") {
            requestURL = "$server/ticket/ticket-by-fmbm?chusr=$user_id&fmbm=$fmbm"
        }

//        AndroidNetworking.get("$server/ticket/ticket-by-bidang")
        AndroidNetworking.get(requestURL)
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
//                    Log.d(tags,response.toString())
                    if (response?.getBoolean("status") == true) {
                        val status = response.getBoolean("status")
                        val message = response.getString("message")
                        val arrayData = response.getJSONArray("data")
                        val bidangIds = 0
                        val bidangNames = "All"
                        var bgRed = false
                        val dataBidangIN = dataBidangIntent.toInt()
                        if (dataBidangIN == 0) {
                            bgRed = true
                        }
                        val data = FilterBidang(bidangIds.toString(), bidangNames, bgRed)
                        listFilter.add(data)

                        for (i in 0 until arrayData.length()) {

                            val objData = arrayData.getJSONObject(i)

                            val bidangId = objData.getInt("bidang_id")
                            val bidangName = objData.getString("bidang_name")
                            var bgRedFor = false

                            val dataBidangINt = dataBidangIntent.toInt()
                            if (dataBidangINt == bidangId) {
                                bgRedFor = true
                            }
                            val data2 = FilterBidang(bidangId.toString(), bidangName, bgRedFor)
                            listFilter.add(data2)
                        }
                        listFlterData.postValue(listFilter)
                    }
                }

                override fun onError(anError: ANError?) {
                    val act = "List_tiket_beranda_byBidang"
                    errorLog(anError, act)
                }

            })

    }


    fun setData(
        server: String,
        token: String,
        userId: String,
        bidangId: String,
        roleId: String,
        fmbm: String
    ) {
        val listStatusTiket = ArrayList<StatusTiket>()

        var bidangIdEmty = bidangId
        Log.d(tags, "idBidangViewModel : $bidangIdEmty")
        if (bidangIdEmty == "null") {
            bidangIdEmty = ""
            Log.d(tags, "idBidangViewModelNull : $bidangIdEmty")

        }
        var requestURL = "$server/ticket/list?status=1&bidang=$bidangId&user_id=$userId"
        if (roleId == "3" || roleId == "4") {
            requestURL = "$server/ticket/list?status=1&bidang=$bidangId&fmbm=$fmbm&user_id=$userId"
        }

        Log.d(tags, "URL_BARU : $requestURL")

        // user_id (hardcode) karena cmn user_id = 4 yang ada datanya
        AndroidNetworking.get(requestURL) // status : semua, baru, berjalan, selesai
            .addHeaders(
                "Accept",
                "application/json"
            )                                   // bidang : Semua Bidang, Sipil, M/E, House Keeping
            .addHeaders("Authorization", "Bearer $token")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
//                    Log.e(tags,"responseBaruStatusTiket : ${response.toString()}")
                    if (response?.getBoolean("status") == true) {
                        val statusResponse = response.getBoolean("status")
                        val message: String = response.getString("message")
                        val arrayData = response.getJSONArray("data")

                        if (arrayData.length() > 0) {
                            for (i in 0 until arrayData.length()) {
                                val objData = arrayData.getJSONObject(i)

                                val user_id = objData.getInt("user_id")
                                val user_name = objData.getString("user_name")
                                val ticket_id = objData.getString("ticket_id")
                                val bidang_id = objData.getInt("bidang_id")
                                val bidang_name = objData.getString("bidang_name")
                                val subbidang_id = objData.getInt("subbidang_id")
                                val subbidang_name = objData.getString("subbidang_name")
                                val location_type = objData.getString("location_type")
                                val location_id = objData.getInt("location_id")
                                val location_name = objData.getString("location_name")
                                val status_id = objData.get("status_id")
                                val reopenIndex = objData.getInt("reopen")
                                val status_name = objData.getString("status_name")
                                val description = objData.getString("description")
                                val created_at = objData.getString("created_at")
                                val imgURL = objData.getString("url")
                                val evidence = objData.getString("evidence")
                                val report = objData.getString("report")
                                val rating = objData.getString("rating")
                                val ulasan = objData.getString("ulasan")

                                // history status tiket variable
                                var historyStatusId: String? = "Belum ada data"
                                var historyStatusName: String? = "Belum ada data"
                                var historyStatusDate: String? = "Belum ada data"
                                var historyUserId: String? = "Belum ada data"
                                var historyUsername: String? = "Belum ada data"
                                var historyAvatar: String? = "Belum ada data"
                                val progressTicket = ArrayList<ListProgressTiket>()

                                // array historyTicket
                                val historyTicket = objData.getJSONArray("history_ticket")
                                if (historyTicket.length() > 0) {
                                    for (j in 0 until historyTicket.length()) {
                                        val objDataTiket = historyTicket.getJSONObject(j)

                                        val statusIdTicket =
                                            objDataTiket.getInt("status_id").toString()
                                        val statusNameTicket = objDataTiket.getString("status_name")
                                        val statusDateTicket = objDataTiket.getString("status_date")
                                        val userIdTicket = objDataTiket.getInt("user_id").toString()
                                        val userNameTicket = objDataTiket.getString("user_name")
                                        val userNoTelp = objDataTiket.get("phone").toString()
                                        val dataProgress = ListProgressTiket(
                                            statusIdTicket,
                                            statusNameTicket,
                                            statusDateTicket,
                                            userIdTicket,
                                            userNameTicket,
                                            userNoTelp
                                        )
                                        progressTicket.add(dataProgress)

                                    }
                                }

                                if (historyTicket.length() > 0) {
                                    historyStatusId =
                                        historyTicket.getJSONObject(0).getInt("status_id")
                                            .toString()
                                    historyStatusName =
                                        historyTicket.getJSONObject(0).getString("status_name")
                                    historyStatusDate =
                                        historyTicket.getJSONObject(0).getString("status_date")
                                    historyUserId =
                                        historyTicket.getJSONObject(0).getInt("user_id").toString()
                                    historyUsername =
                                        historyTicket.getJSONObject(0).getString("user_name")
                                    historyAvatar = historyTicket.getJSONObject(0).getString("url")
                                }

                                Log.d(tags, "statusHistory : $historyStatusName ")

                                val data = StatusTiket(
                                    statusResponse.toString(),
                                    message,
                                    user_id.toString(),
                                    user_name,
                                    ticket_id,
                                    bidang_id.toString(),
                                    bidang_name,
                                    subbidang_id.toString(),
                                    subbidang_name,
                                    location_type,
                                    location_id.toString(),
                                    location_name,
                                    status_id.toString(),
                                    status_name,
                                    description,
                                    created_at,
                                    imgURL,
                                    historyStatusId,
                                    historyStatusName,
                                    historyStatusDate,
                                    historyUserId,
                                    historyUsername,
                                    progressTicket,
                                    historyAvatar,
                                    evidence,
                                    report,
                                    rating,
                                    ulasan,
                                    reopenIndex
                                )
                                listStatusTiket.add(data)
                            }
                        } else {
                            listStatusTiket.clear()
                        }
                        listData.postValue(listStatusTiket)
                    } else {
                        val statusResponse = response?.getBoolean("status")
                        val message = response?.getString("message")

                        listStatusTiket[0].statusResponse = statusResponse.toString()
                        listStatusTiket[0].message = message
                    }
                }

                override fun onError(anError: ANError?) {
                    val act = "listStatusTiket"
                    errorLog(anError, act)
                }

            })

    }

    private fun errorLog(anError: ANError?, act: String) {
        Log.e(tags, "error $act : $anError")
        Log.e(tags, "error $act : ${anError?.response}")
        Log.e(tags, "error $act : ${anError?.errorBody}")
    }

    fun getData(): LiveData<ArrayList<StatusTiket>> {
        return listData
    }

    fun getFilterData(): LiveData<ArrayList<FilterBidang>> {
        return listFlterData
    }


}
