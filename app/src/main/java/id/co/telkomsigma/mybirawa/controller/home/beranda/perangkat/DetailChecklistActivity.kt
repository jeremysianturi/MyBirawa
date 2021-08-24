package id.co.telkomsigma.mybirawa.controller.home.beranda.perangkat

import `in`.galaxyofandroid.spinerdialog.SpinnerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.adapter.DetailPerangkatGedungAdapter
import id.co.telkomsigma.mybirawa.entity.*
import id.co.telkomsigma.mybirawa.util.UserPreference
import id.co.telkomsigma.mybirawa.viewmodel.perangkat.DetailCheckListViewModel
import kotlinx.android.synthetic.main.activity_detail_checklist.*

class DetailChecklistActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    private val tag = DetailChecklistActivity::class.java.simpleName
    private lateinit var dataIntent: Perangkat
    private lateinit var adapter: DetailPerangkatGedungAdapter
    private lateinit var viewModel: DetailCheckListViewModel

    private lateinit var mUserPreference: UserPreference
    private lateinit var session: PreferenceModel

    // dropdown gedung perangkat
    private val listDataGedung = ArrayList<GedungPerangkat>()
    private val listNamaGedung = ArrayList<String>()

    // dropdown lantai perangkat
    private val listDataLantai = ArrayList<LantaiPerangkat>()
    private val listNamaLantai = ArrayList<String>()

    private var gedungName: String? = null
    private var perangkatName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_checklist)

        // user preference/ session
        mUserPreference = UserPreference(this)
        session = mUserPreference.getUser()

        dataIntent = intent.getParcelableExtra(EXTRA_DATA) as Perangkat
        val tittleActionBar = dataIntent.periode

        //int ViewModel
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DetailCheckListViewModel::class.java
        )

        // onclick
        tvDropdownGedung.setOnClickListener(this)
        tvDropdownLantai.setOnClickListener(this)

        //setup Actionbar and navigasi up
        val actionbar = supportActionBar
        actionbar?.title = "Checklist $tittleActionBar"
        actionbar?.setDisplayHomeAsUpEnabled(true)

        //call method
        setupDetailData(dataIntent)
        showRecycleview()
        viewModelMethod()

    }

    private fun viewModelMethod() {
        val url = session.server.toString()
        val token = session.token.toString()
        val fmbm = session.fmbmId
        val subroleBidang = session.subRoleBidang
        val periodeId = dataIntent.idPeriode
        val hari = dataIntent.selisihhari

        //request data to server
        viewModel.setGedung(url, token, fmbm, subroleBidang, periodeId, hari)
        // getData from viewModel
        viewModel.getGedung().observe(this, Observer { gedung ->
            if (gedung.isEmpty()) {
                Log.e(tag, " no data GEDUNG from vieModel")
            } else {
                // clear data
                listDataGedung.clear()
                listNamaGedung.clear()

                // looping data
                for (i in 0 until gedung.size) {
                    val id = gedung[i].gedungId
                    val name = gedung[i].gedungName
                    val address = gedung[i].gedungAddress
                    val lat = gedung[i].gedungLatitude
                    val long = gedung[i].gedungLongitude

                    listNamaGedung.add(name.toString())
                    val mGedung = GedungPerangkat(id, name, address, lat, long)
                    listDataGedung.add(mGedung)
                }
            }
        })


//        viewModel.getData().observe(this, Observer { data ->
//            if (data.isNullOrEmpty()){
//                Log.e(tag, " no data from vieModel")
//            }else{
//                adapter.setData(data)
//            }
//        })

    }

    private fun showRecycleview() {
        adapter = DetailPerangkatGedungAdapter()
        rvlistLantaigedung.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvlistLantaigedung.adapter = adapter

        adapter.setOnItemClickCallback(object : DetailPerangkatGedungAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DetailPerangkat) {
                val mIntent =
                    Intent(this@DetailChecklistActivity, ItemDetailPerangkatActivity::class.java)
                mIntent.putExtra(ItemDetailPerangkatActivity.EXTRA_DATA, data)
                startActivity(mIntent)
            }

        })

    }

    private fun setupDetailData(dataIntent: Perangkat) {

        tvTittleDetailChecklist.text = dataIntent.periode
        val items = "${dataIntent.sudah}/${dataIntent.total}"
        tvItemDetailChecklist.text = items
        val percentage = "${dataIntent.percent}%"
        tvPercentDetailChecklist.text = percentage

        Glide.with(this)
            .load(dataIntent.url)
            .error(R.drawable.ic_sipil)
            .into(imgDetailCheckList)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvDropdownGedung -> {
                val spinnerDialog = SpinnerDialog(
                    this,
                    listNamaGedung,
                    "Select Item : ",
                    R.style.DialogAnimations_SmileWindow
                )
                spinnerDialog.bindOnSpinerListener { s, i ->
                    val name = listDataGedung[i].gedungName
                    val id = listDataGedung[i].gedungId

                    // set name to textView
                    tvDropdownGedung.text = name
                    gedungName = name.toString()
                    // enable tvLantai
                    tvDropdownLantai.isEnabled = true
                    // call method get Lantai
                    getLantai(id)
                }
                spinnerDialog.showSpinerDialog()
            }
            R.id.tvDropdownLantai -> {
                val spinnerDialog = SpinnerDialog(
                    this,
                    listNamaLantai,
                    "Select Item : ",
                    R.style.DialogAnimations_SmileWindow
                )
                spinnerDialog.bindOnSpinerListener { s, i ->
                    val name = listDataLantai[i].lantaiName
                    val id = listDataLantai[i].lantaiid

                    // set name to textView
                    tvDropdownLantai.text = name

                    // call method list Perangkat
                    getListPerangkat(id, name)
                }
                spinnerDialog.showSpinerDialog()
            }
        }
    }

    private fun getListPerangkat(lantaiId: Int?, lantaiName: String?) {
        val url = session.server.toString()
        val token = session.token.toString()
        val fmbm = session.fmbmId
        val subroleBidang = session.subRoleBidang
        val periodeId = dataIntent.idPeriode
        val hari = dataIntent.selisihhari

        // set data to viewmodel
        viewModel.setPerangkat(url, token, fmbm, subroleBidang, periodeId, hari, lantaiId)

        // get data from viewModel
        viewModel.getPerangkat().observe(this, Observer { perangkat ->
            if (perangkat.isNullOrEmpty()) {
                Log.e(tag, " no data Perangkat from vieModel")
            } else {
                adapter.setData(perangkat)
                setupBoxDataPerangkat(lantaiName)
            }
        })
    }

    private fun setupBoxDataPerangkat(lantaiName: String?) {
        tvTittleGedungPerangkat.text = gedungName
        textView18.text = lantaiName
    }

    private fun getLantai(gedungId: Int?) {
        val url = session.server.toString()
        val token = session.token.toString()
        val fmbm = session.fmbmId
        val subroleBidang = session.subRoleBidang
        val periodeId = dataIntent.idPeriode
        val hari = dataIntent.selisihhari

        // set data to viewmodel
        viewModel.setLantai(url, token, fmbm, subroleBidang, periodeId, hari, gedungId)

        // get data Lantai
        viewModel.getLantai().observe(this, Observer { lantai ->
            if (lantai.isNullOrEmpty()) {
                Log.e(tag, " no data Lantai from vieModel")
            } else {
                // clear data
                listDataLantai.clear()
                listNamaLantai.clear()

                // looping data
                for (i in 0 until lantai.size) {
                    val id = lantai[i].lantaiid
                    val name = lantai[i].lantaiName

                    listNamaLantai.add(name.toString())
                    val mLantai = LantaiPerangkat(id, name)
                    listDataLantai.add(mLantai)
                }
            }
        })
    }

}