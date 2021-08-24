package id.co.telkomsigma.mybirawa.controller.home.beranda.help

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.entity.PreferenceModel
import id.co.telkomsigma.mybirawa.util.UserPreference
import id.co.telkomsigma.mybirawa.viewmodel.help.HelpViewModel
import kotlinx.android.synthetic.main.activity_help.*


class HelpActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = HelpActivity::class.java.simpleName
    private lateinit var mUserPreference: UserPreference
    private lateinit var session: PreferenceModel

    private lateinit var viewModel: HelpViewModel

    private var phoneNumber: String? = "0"
    private var email: String? = "recipient@example.com"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        // declaration SharedPreference
        mUserPreference = UserPreference(this)
        session = mUserPreference.getUser()

        // init viewModel
        viewModel = ViewModelProvider(this)[HelpViewModel::class.java]
        viewModel.setHelp(session.server.toString(), session.token.toString())

        cv_help_faq.setOnClickListener(this)
        cv_help_languange.setOnClickListener(this)
        btnCallHelp.setOnClickListener(this)
        btnEmailHelp.setOnClickListener(this)

        //setup Actionbar and navigasi up
        val actionbar = supportActionBar
        actionbar?.title = "Bantuan"
        actionbar?.setDisplayHomeAsUpEnabled(true)

        // call method
        setupData()

    }

    private fun setupData() {
        viewModel.getHelp().observe(this, Observer { data ->
            if (data.isNullOrEmpty()) {
                Log.d(TAG, "data help null")
            } else {
                tv_desc_help.text = data[0].alamat
                phoneNumber = data[0].phoneNumber
                email = data[0].email

            }
        })
        tv_help_desc_versi.text = session.versiApp
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cv_help_faq -> {
                val mIntent = Intent(this, FaqActivity::class.java)
                startActivity(mIntent)
            }
            R.id.cv_help_languange -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            R.id.btnCallHelp -> {
                val mIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                startActivity(mIntent)
            }
            R.id.btnEmailHelp -> {
                email()
            }
        }
    }

    private fun email() {
        val i = Intent(Intent.ACTION_SEND)
        i.type = "message/rfc822"
        i.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
//        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email")
//        i.putExtra(Intent.EXTRA_TEXT, "body of email")
        try {
            startActivity(Intent.createChooser(i, "Send mail..."))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                this,
                "There are no email clients installed.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}