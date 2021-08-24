package id.co.telkomsigma.mybirawa.controller.inbox

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import androidx.core.text.htmlEncode
import com.bumptech.glide.Glide
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.entity.Inbox
import kotlinx.android.synthetic.main.activity_detail_inbox.*

class DetailInboxActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_inbox)

        // get data from Intent
        val dataFromIntent = intent.getParcelableExtra(EXTRA_DATA) as Inbox

        //setup Actionbar and navigasi up
        val actionbar = supportActionBar
        actionbar?.title = "Rincian Pesan"
        actionbar?.setDisplayHomeAsUpEnabled(true)

        // call methodp
        setupDetailInbox(dataFromIntent)
    }

    private fun setupDetailInbox(data: Inbox) {
        Glide.with(this)
            .load(data.url)
            .error(R.drawable.ic_detail_inbox)
            .into(imgDetailInbox)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv_isi_detail_inbox.text = Html.fromHtml(data.message, Html.FROM_HTML_MODE_COMPACT)
        } else {
            tv_isi_detail_inbox.text = Html.fromHtml(data.message)
        }
        tv_tittle_detail_inbox.text = data.subject
        tv_date_detail_inbox.text = data.chageDate
//        tv_isi_detail_inbox.text = data.message
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}