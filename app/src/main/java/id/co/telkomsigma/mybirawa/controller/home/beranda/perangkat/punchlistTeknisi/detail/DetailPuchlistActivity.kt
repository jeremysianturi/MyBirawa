package id.co.telkomsigma.mybirawa.controller.home.beranda.perangkat.punchlistTeknisi.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import id.co.telkomsigma.mybirawa.R

class DetailPuchlistActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_puchlist)

        //biar keyboard ga lgsg popup
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        //setup Actionbar and navigasi up
        val actionbar = supportActionBar
        actionbar?.title = "Detail Punchlist"
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}