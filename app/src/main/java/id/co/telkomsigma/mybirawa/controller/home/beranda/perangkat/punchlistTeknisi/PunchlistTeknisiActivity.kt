package id.co.telkomsigma.mybirawa.controller.home.beranda.perangkat.punchlistTeknisi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.adapter.PunchlistFilterAdapter
import id.co.telkomsigma.mybirawa.adapter.PunchlistTeknisiAdapter
import id.co.telkomsigma.mybirawa.entity.PreferenceModel
import id.co.telkomsigma.mybirawa.util.UserPreference
import id.co.telkomsigma.mybirawa.viewmodel.punchlist.PunchlistViewModel
import kotlinx.android.synthetic.main.content_punchlist.*

class PunchlistTeknisiActivity : AppCompatActivity() {

    private val tags = PunchlistTeknisiActivity::class.java.simpleName
    private lateinit var mUserPreference: UserPreference
    private lateinit var session: PreferenceModel

    private lateinit var viewModel: PunchlistViewModel
    private lateinit var adapterFilter: PunchlistFilterAdapter

    private lateinit var adapterContent: PunchlistTeknisiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_punchlist_teknisi)

        // declaration SharedPreference
        mUserPreference = UserPreference(this)
        session = mUserPreference.getUser()

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[PunchlistViewModel::class.java]
        viewModel.setFilterPunclist()
        viewModel.getPunchlIst().observe(this, Observer { filter ->

            if (filter.isNullOrEmpty()) {
                Log.e(tags, "no data FILTER PUNCHLIST")
            } else {
                adapterFilter.setData(filter, 1)
            }
        })

        viewModel.setContentPunchlist()
        viewModel.getContentPunchlist().observe(this, Observer { content ->
            if (content.isNullOrEmpty()) {
                Log.e(tags, "no data CONTENT PUNCHLIST")
            } else {
                adapterContent.setData(content)
            }
        })
        //call method
        showRecycleView()

        //setup Actionbar and navigasi up
        val actionbar = supportActionBar
        actionbar?.title = "Punchlist"
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun showRecycleView() {
        adapterFilter = PunchlistFilterAdapter()
        rvFilterPunchlist.setHasFixedSize(true)
        rvFilterPunchlist.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvFilterPunchlist.adapter = adapterFilter

        adapterContent = PunchlistTeknisiAdapter()
        rvContentPunchlist.setHasFixedSize(true)
        rvContentPunchlist.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvContentPunchlist.adapter = adapterContent

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}