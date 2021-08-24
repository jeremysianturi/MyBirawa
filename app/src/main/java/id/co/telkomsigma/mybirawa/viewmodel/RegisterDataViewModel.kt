package id.co.telkomsigma.mybirawa.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.telkomsigma.mybirawa.controller.register.RegisterActivity
import id.co.telkomsigma.mybirawa.entity.RegisterCity
import id.co.telkomsigma.mybirawa.entity.RegisterSubUnit
import id.co.telkomsigma.mybirawa.entity.RegisterUnit
import org.json.JSONObject

class RegisterDataViewModel : ViewModel() {

    val TAG: String = RegisterActivity::class.java.simpleName

    val listUnit = MutableLiveData<ArrayList<RegisterUnit>>()
    val listUnitString = MutableLiveData<ArrayList<String>>()
    val listSubUnit = MutableLiveData<ArrayList<RegisterSubUnit>>()
    val listCity = MutableLiveData<ArrayList<RegisterCity>>()


    fun setData(url: String, token: String) {
        val listUnitModel = ArrayList<RegisterUnit>()
//        val listSubUnitModel = ArrayList<RegisterSubUnit>()
        val listCityModel = ArrayList<RegisterCity>()

        val listStringUnit = ArrayList<String>()

        listUnitModel.clear()
//        listSubUnitModel.clear()
        listCityModel.clear()
        listStringUnit.clear()


        AndroidNetworking.get("$url/auth/data-parameter")
            .addHeaders("Authorization", "Bearer $token")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response?.get("status") == true) {

                        val arrayData = response.getJSONObject("data")

                        // loop for JSONArray unit
                        val unit = arrayData.getJSONArray("unit")
                        for (x in 0 until unit.length()) {
                            val unitObj = unit.getJSONObject(x)

                            val unitId = unitObj.get("textid")
                            val unitName = unitObj.get("textname")

                            val unitdata = RegisterUnit(unitId.toString(), unitName.toString())
                            listUnitModel.add(unitdata)

                            listStringUnit.add(unitName.toString())

                        }

                        // loop for JSONArray subUnit
//                            val subUnit = arrayData.getJSONArray("city")
//                            for (y in 0 until subUnit.length()){
//                                val subUnitObj = subUnit.getJSONObject(y)
//
//                                val subUnitId = subUnitObj.get("textid")
//                                val subUnitName = subUnitObj.get("textname")
//
//                                val subUnitData = RegisterSubUnit(subUnitId.toString(),subUnitName.toString())
//                                listSubUnitModel.add(subUnitData)
//                            }

                        // loop for JSONArray city
                        val city = arrayData.getJSONArray("city")
                        for (z in 0 until city.length()) {
                            val cityObj = city.getJSONObject(z)

                            val cityId = cityObj.get("textid")
                            val cityName = cityObj.get("textname")

                            val cityData = RegisterCity(cityId.toString(), cityName.toString())
                            listCityModel.add(cityData)
                        }

                        listUnit.postValue(listUnitModel)
//                        listSubUnit.postValue(listSubUnitModel)
                        listCity.postValue(listCityModel)
                        listUnitString.postValue(listStringUnit)

                    }
                }

                override fun onError(anError: ANError?) {
                    errorLog(anError)
                }

            })
    }

    fun getUnitData(): LiveData<ArrayList<RegisterUnit>> {
        return listUnit
    }

    fun getSubUnitData(): LiveData<ArrayList<RegisterSubUnit>> {
        return listSubUnit
    }

    fun getCityData(): LiveData<ArrayList<RegisterCity>> {
        return listCity
    }

    private fun errorLog(anError: ANError?) {
        Log.e(TAG, "error : $anError")
        Log.e(TAG, "error : ${anError?.response}")
        Log.e(TAG, "error : ${anError?.errorBody}")
    }

}