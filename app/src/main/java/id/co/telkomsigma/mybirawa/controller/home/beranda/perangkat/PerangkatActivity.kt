package id.co.telkomsigma.mybirawa.controller.home.beranda.perangkat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.adapter.PerangkatAdapter
import id.co.telkomsigma.mybirawa.controller.home.beranda.perangkat.punchlistTeknisi.PunchlistTeknisiActivity
import id.co.telkomsigma.mybirawa.controller.home.beranda.perangkat.punchlistTeknisi.detail.DetailPuchlistActivity
import id.co.telkomsigma.mybirawa.entity.Perangkat
import id.co.telkomsigma.mybirawa.entity.PreferenceModel
import id.co.telkomsigma.mybirawa.util.UserPreference
import id.co.telkomsigma.mybirawa.viewmodel.perangkat.PerangkatViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_perangkat.*
import kotlinx.android.synthetic.main.activity_perangkat.progressBar

class PerangkatActivity : AppCompatActivity() {

    private val tag = PerangkatActivity::class.java.simpleName
    private lateinit var mUserPreference: UserPreference
    private lateinit var session: PreferenceModel

    private lateinit var viewModel: PerangkatViewModel
    private lateinit var adapter: PerangkatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perangkat)

        //biar keyboard ga lgsg popup
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        // declaration SharedPreference
        mUserPreference = UserPreference(this)
        session = mUserPreference.getUser()

        //setup Actionbar and navigasi up
        val actionbar = supportActionBar
        actionbar?.title = "Perfomansi Perangkat"
        actionbar?.setDisplayHomeAsUpEnabled(true)

        // call method
        showRecycleView()
        getDataFromViewModel()
    }

    private fun showRecycleView() {
        adapter = PerangkatAdapter()
        rvPerangkat.setHasFixedSize(true)
        rvPerangkat.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvPerangkat.adapter = adapter

        adapter.setOnItemClickCallback(object : PerangkatAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Perangkat) {
                if (data.idPeriode == 0) {
                    val mIntent =
                        Intent(this@PerangkatActivity, PunchlistTeknisiActivity::class.java)
                    startActivity(mIntent)
                } else {
                    val mIntent =
                        Intent(this@PerangkatActivity, DetailChecklistActivity::class.java)
                    mIntent.putExtra(DetailChecklistActivity.EXTRA_DATA, data)
                    startActivity(mIntent)
                }
            }

        })
    }

    private fun getDataFromViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(PerangkatViewModel::class.java)
        //set data to ViewModel Class\
        showLoading(true)
        val url = session.server.toString()
        val token = session.token.toString()
        val fmbm = session.fmbmId.toString()
        val bidangRequest = session.subRoleBidang.toString()

        viewModel.setData(url, token, fmbm, bidangRequest)

        // get data from ViewModel
        viewModel.getData().observe(this, Observer { data ->
            if (data.isNullOrEmpty()) {
                showLoading(false)
                Log.e(tag, "no data from Perangkat ViewModel")
            } else {
                showLoading(false)
                Log.d(tag, "ada data")
                adapter.setData(data)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}