package id.co.telkomsigma.mybirawa.controller.home.beranda.penilaianGedung

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import es.dmoral.toasty.Toasty
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.entity.PreferenceModel
import id.co.telkomsigma.mybirawa.util.UserPreference
import kotlinx.android.synthetic.main.activity_penilaian_gedung.*

class PenilaianGedungActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mUserPreference: UserPreference
    private lateinit var session: PreferenceModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penilaian_gedung)

        // declaration SharedPreference
        mUserPreference = UserPreference(this)
        session = mUserPreference.getUser()

        tvDropdowArea.setOnClickListener(this)

        //setup Actionbar and navigasi up
        val actionbar = supportActionBar
        actionbar?.title = "Perfomansi Perangkat"
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvDropdowArea -> {
                Toasty.warning(this, "item masih belum tersedia", Toasty.LENGTH_SHORT).show()
            }
        }
    }
}