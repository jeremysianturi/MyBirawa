package id.co.telkomsigma.mybirawa.viewmodel.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.telkomsigma.mybirawa.controller.home.beranda.history.HistoryActivity
import id.co.telkomsigma.mybirawa.entity.HistoryTiket
import id.co.telkomsigma.mybirawa.entity.ListProgressTiket
import id.co.telkomsigma.mybirawa.entity.StatusTiket
import org.json.JSONObject

class HistoryViewModel : ViewModel() {
    val tags = HistoryActivity::class.java.simpleName
    private val listData = MutableLiveData<ArrayList<StatusTiket>>()

    fun setData(
        server: String,
        token: String,
        userId: String,
        bidangId: String,
        roleId: String,
        fmbm: String
    ) {
        val listHistory = ArrayList<StatusTiket>()

        var bidangIdEmty = bidangId
        Log.d(tags, "idBidangViewModel : $bidangIdEmty")
        if (bidangIdEmty == "null") {
            bidangIdEmty = ""
            Log.d(tags, "idBidangViewModelNull : $bidangIdEmty")

        }

        var requestURL = "$server/ticket/list?status=4,5&bidang=$bidangIdEmty&user_id=$userId"
        if (roleId == "3" || roleId == "4") {
            requestURL =
                "$server/ticket/list?status=4,5&bidang=$bidangIdEmty&fmbm=$fmbm&user_id=$userId"
        }

        Log.d(tags, "requestURLHistory : $requestURL")
        // user_id (hardcode) karena cmn user_id = 4 yang ada datanya
        AndroidNetworking.get(requestURL) // status : semua, baru, berjalan, selesai
            .addHeaders(
                "Accept",
                "application/json"
            ) // bidang : Semua Bidang, Sipil, M/E, House Keeping
            .addHeaders("Authorization", "Bearer $token")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.e(tags, "request status ticket : $requestURL")
                    Log.d(tags, "responseHistorysTiket : ${response.toString()}")

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
                                        val phone = objDataTiket.getString("phone")
                                        val dataProgress = ListProgressTiket(
                                            statusIdTicket,
                                            statusNameTicket,
                                            statusDateTicket,
                                            userIdTicket,
                                            userNameTicket,
                                            phone
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
                                    ulasan
                                )
                                listHistory.add(data)
                            }
                        } else {
                            listHistory.clear()
                        }
                        listData.postValue(listHistory)
                    } else {
                        val statusResponse = response?.getBoolean("status")
                        val message = response?.getString("message")

                        listHistory[0].statusResponse = statusResponse.toString()
                        listHistory[0].message = message
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

}