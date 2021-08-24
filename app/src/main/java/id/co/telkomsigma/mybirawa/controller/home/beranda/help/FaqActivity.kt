package id.co.telkomsigma.mybirawa.controller.home.beranda.help

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebViewClient
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import es.dmoral.toasty.Toasty
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.adapter.FaqAdapter
import id.co.telkomsigma.mybirawa.adapter.HistoryTiketAdapter
import id.co.telkomsigma.mybirawa.controller.home.beranda.history.DetailHistoryActivity
import id.co.telkomsigma.mybirawa.entity.PreferenceModel
import id.co.telkomsigma.mybirawa.entity.StatusTiket
import id.co.telkomsigma.mybirawa.util.UserPreference
import id.co.telkomsigma.mybirawa.viewmodel.help.FaqViewModel
import id.co.telkomsigma.mybirawa.viewmodel.history.HistoryViewModel
import kotlinx.android.synthetic.main.activity_faq.*
import kotlinx.android.synthetic.main.activity_history.*

class FaqActivity : AppCompatActivity() {


    private lateinit var mUserPreference: UserPreference
    private lateinit var session: PreferenceModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq)

        // user preference/ session
        mUserPreference = UserPreference(this)
        session = mUserPreference.getUser()

        //setup Actionbar and navigasi up
        val actionbar = supportActionBar
        actionbar?.title = "Faq"
        actionbar?.setDisplayHomeAsUpEnabled(true)

        // call method
        webview()

    }

    private fun webview() {
        webViewFaq.loadUrl("http://mybirawa.gsd.co.id/faq_page")
        webViewFaq.settings.javaScriptEnabled = true
        webViewFaq.webViewClient = WebViewClient()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // Check if the key event was the Back button and if there's history
        if (keyCode == KeyEvent.KEYCODE_BACK && webViewFaq.canGoBack()) {
            webViewFaq.goBack()
            return true
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}