package id.co.telkomsigma.mybirawa.controller.statusTiket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.adapter.SectionsPagerAdapter
import id.co.telkomsigma.mybirawa.controller.statusTiket.fragmentStatusTiket.BaruStatusTiketFragment
import id.co.telkomsigma.mybirawa.controller.statusTiket.fragmentStatusTiket.BerjalanStatusTiketFragment
import id.co.telkomsigma.mybirawa.controller.statusTiket.fragmentStatusTiket.SelesaiStatusTiketFragment
import id.co.telkomsigma.mybirawa.controller.statusTiket.fragmentStatusTiket.SemuaStatusTiketFragment
import id.co.telkomsigma.mybirawa.entity.BerandaTiket
import kotlinx.android.synthetic.main.activity_status_tiket.*

class StatusTiketActivity : AppCompatActivity() {

    private val tags = StatusTiketActivity::class.java.simpleName
    companion object{
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_POSITION_FILTER_BIDANG = "extra_position_bidang"
    }

    private var itemPosition : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_tiket)

        // get data intent
        val detailFilterData = intent.getParcelableExtra<BerandaTiket>(EXTRA_DATA)

        itemPosition = intent.getIntExtra(EXTRA_POSITION_FILTER_BIDANG,0)

        Log.d(tags, "itemPosition :  $itemPosition")

        // call method
        setupViewPager(detailFilterData)

        //setup Actionbar and navigasi up
        val actionbar = supportActionBar
        actionbar?.title = "Status Tiket"
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupViewPager(data: BerandaTiket?) {

        // all fragment
        val semuaFragment = SemuaStatusTiketFragment()
        val bundelDataSemua = Bundle()
        bundelDataSemua.putParcelable(SemuaStatusTiketFragment.EXTRA_DATA_ID_BIDANG,data)
        bundelDataSemua.putInt(SemuaStatusTiketFragment.EXTRA_POSITION_FILTER_BIDANG,itemPosition)
        semuaFragment.arguments = bundelDataSemua

        // open Fragment
        val baruFragment = BaruStatusTiketFragment()
        val bundelDataBaru = Bundle()
        bundelDataBaru.putParcelable(BaruStatusTiketFragment.EXTRA_DATA_ID_BIDANG,data)
        bundelDataBaru.putInt(BaruStatusTiketFragment.EXTRA_POSITION_FILTER_BIDANG,itemPosition)
        baruFragment.arguments = bundelDataBaru

        // progress Fragment
        val berjalanFragment = BerjalanStatusTiketFragment()
        val bundelDataBejalan = Bundle()
        bundelDataBejalan.putParcelable(BerjalanStatusTiketFragment.EXTRA_DATA_ID_BIDANG,data)
        bundelDataBejalan.putInt(BerjalanStatusTiketFragment.EXTRA_POSITION_FILTER_BIDANG,itemPosition)
        berjalanFragment.arguments = bundelDataBejalan

        // Done Fragment
        val selesaiFragment = SelesaiStatusTiketFragment()
        val bundelDataSelesai = Bundle()
        bundelDataSelesai.putParcelable(SelesaiStatusTiketFragment.EXTRA_DATA_ID_BIDANG,data)
        bundelDataSelesai.putInt(SelesaiStatusTiketFragment.EXTRA_POSITION_FILTER_BIDANG,itemPosition)
        selesaiFragment.arguments = bundelDataSelesai

        // add title and fragment to adapter (SectionsPagerAdapter)
        val sectionsPagerAdapter = SectionsPagerAdapter(this,supportFragmentManager)
        sectionsPagerAdapter.addFragments(semuaFragment,"All")
        sectionsPagerAdapter.addFragments(baruFragment,"Open")
        sectionsPagerAdapter.addFragments(berjalanFragment,"Progress")
        sectionsPagerAdapter.addFragments(selesaiFragment,"Done")

        // set view pager to adapter
        view_pager.adapter = sectionsPagerAdapter
        tabLayout.setupWithViewPager(view_pager,true)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
