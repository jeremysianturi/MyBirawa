package id.co.telkomsigma.mybirawa.controller.home.beranda.perangkat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.adapter.DataChecklistPerangkatAdapter
import id.co.telkomsigma.mybirawa.adapter.TittleChecklistPerangkatAdapter
import id.co.telkomsigma.mybirawa.entity.DetailPerangkat
import id.co.telkomsigma.mybirawa.entity.PreferenceModel
import id.co.telkomsigma.mybirawa.util.UserPreference
import kotlinx.android.synthetic.main.activity_item_detail_perangkat.*

class ItemDetailPerangkatActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    private val tag = ItemDetailPerangkatActivity::class.java.simpleName
    private lateinit var mUserPreference: UserPreference
    private lateinit var session: PreferenceModel

    private lateinit var adapterTittlePerangkat: TittleChecklistPerangkatAdapter
    private lateinit var adapterDataPerangkat: DataChecklistPerangkatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail_perangkat)

        val dataIntent = intent.getParcelableExtra(EXTRA_DATA) as DetailPerangkat
        val titleActionBar = dataIntent.perangkatJenisName.toString()

        mUserPreference = UserPreference(this)
        session = mUserPreference.getUser()

        //setup Actionbar and navigasi up
        val actionbar = supportActionBar
        actionbar?.title = titleActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        // call method
        setupViewPager()
        showRecycleViewFilter()
        showRecycleViewData()
        showRecycleViewData()

    }

    private fun showRecycleViewData() {
        Log.d(tag, "show RecycleView Data")
        // recycle view data
        adapterDataPerangkat = DataChecklistPerangkatAdapter()
        rvDetailPerangkat.setHasFixedSize(true)
        rvDetailPerangkat.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvDetailPerangkat.adapter = adapterDataPerangkat

    }

    private fun showRecycleViewFilter() {
        Log.d(tag, "show RecycleView Filter")

        // recycleview tittle
        adapterTittlePerangkat = TittleChecklistPerangkatAdapter()
        rvPerangkatName.setHasFixedSize(true)
        rvPerangkatName.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvPerangkatName.adapter = adapterTittlePerangkat


    }

    private fun setupViewPager() {

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}