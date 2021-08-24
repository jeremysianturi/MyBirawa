package id.co.telkomsigma.mybirawa.controller.home.fragmentHome

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import es.dmoral.toasty.Toasty
import com.androidnetworking.common.Priority

import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.controller.home.beranda.help.HelpActivity
import id.co.telkomsigma.mybirawa.controller.login.LoginActivity
import id.co.telkomsigma.mybirawa.entity.PreferenceModel
import id.co.telkomsigma.mybirawa.util.UserPreference
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.bottom_sheet_dialog_logout.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment(), View.OnClickListener {

    private val tags = BerandaFragment::class.java.simpleName

    private lateinit var mUserPreference : UserPreference
    private lateinit var session : PreferenceModel

    private lateinit var bottomSheetDialog : BottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // SharedPreference
        mUserPreference = UserPreference(requireActivity())
        session = mUserPreference.getUser()
        //call method
        setupText()
        Log.e(tag,"phone Data : ${session.phone.toString()}")

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // BottomSheetDialog
        bottomSheetDialog = BottomSheetDialog(requireContext(),R.style.BottomSheetDialog)
        val views  = layoutInflater.inflate(R.layout.bottom_sheet_dialog_logout, null)
        bottomSheetDialog.setContentView(views)

        // onclick listener
        btn_profile_logout.setOnClickListener(this)
        btnBantuanProfile.setOnClickListener(this)

        // onclick bottomsheet
        views.btnLogoutYes.setOnClickListener(this)
        views.btnLogoutNo.setOnClickListener(this)

    }

    private fun setupText() {
        tv_profile_email.text = session.email.toString().trim()
        tv_profile_location.text = session.cityName.toString().trim()
        tv_profile_name.text = session.name.toString().trim()
        tv_profile_phone.text = session.phone.toString().trim()
        tv_profile_unit.text = session.subunit.toString().trim()

        val isi = "20"
        val unit : String?  = isi

        Log.d(tags, "test operator : $unit")

        Glide.with(requireContext())
            .load(session.avatar.toString())
            .placeholder(R.drawable.ic_account_circle_black_32dp)
            .into(img_profile)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_profile_logout -> {
                bottomSheetDialog.show()
            }
            R.id.btnBantuanProfile ->{
                val mIntent = Intent(requireContext(),HelpActivity::class.java)
                startActivity(mIntent)
            }
            //onclick bottomsheet
            R.id.btnLogoutYes ->{
                bottomSheetDialog.dismiss()
                logout()
            }
            //onclick bottomsheet
            R.id.btnLogoutNo -> {
                bottomSheetDialog.dismiss()
            }
        }
    }

    private fun logout() {
        //show loading
        showLoading(true)
        //request Url
        val requestUrl  ="${session.server}/auth/logout"

        //setup data JSON Object
        val dataLogout = JSONObject()
        dataLogout.put("device_id",session.deviceId)
        dataLogout.put("user_id",session.userId)

        // request to server
        AndroidNetworking.post(requestUrl)
            .addHeaders("Accept","application/json")
            .addJSONObjectBody(dataLogout)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
//                    Log.d(tags, "responeLogout :  ${response.toString()}")
                    if (response?.getBoolean("status")==false) {

                        //dismiss loading
                        showLoading(false)
                        val message  = response.getString("message")
                        Toasty.error(requireContext(), message,Toasty.LENGTH_SHORT).show()

                    }else{
                        //dismiss loading
                        showLoading(false)
                        // intent to Login Activity
                        session.isLogin = false
                        mUserPreference.setUser(session)
                        val mIntent = Intent(activity,LoginActivity::class.java)
                        mIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        mIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(mIntent)
                        activity?.finish()


                    }

                }

                override fun onError(anError: ANError?) {
                    //dismiss loading
                    showLoading(false)
                    errorLog(anError)
                }

            })

    }

    private fun errorLog(anError: ANError?) {
        Log.e(tags, "$tags -> error : $anError")
        Log.e(tags, "$tags -> error : ${anError?.response}")
        Log.e(tags, "$tags -> error : ${anError?.errorBody}")
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBarLogout.visibility = View.VISIBLE
        } else {
            progressBarLogout.visibility = View.GONE
        }
    }

}
