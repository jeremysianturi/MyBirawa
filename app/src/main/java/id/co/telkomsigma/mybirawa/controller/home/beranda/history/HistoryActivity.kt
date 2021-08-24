package id.co.telkomsigma.mybirawa.controller.home.beranda.history

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.adapter.HistoryTiketAdapter
import id.co.telkomsigma.mybirawa.entity.PreferenceModel
import id.co.telkomsigma.mybirawa.entity.StatusTiket
import id.co.telkomsigma.mybirawa.util.UserPreference
import id.co.telkomsigma.mybirawa.viewmodel.history.HistoryViewModel
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {

    private val tags = HistoryActivity::class.java.simpleName
    private lateinit var viewModel: HistoryViewModel
    private lateinit var adapter: HistoryTiketAdapter

    private lateinit var mUserPreference: UserPreference
    private lateinit var session: PreferenceModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        // show loading when start Activity
        showLoading(true)

        // user preference/ session
        mUserPreference = UserPreference(this)
        session = mUserPreference.getUser()

        // set/get data from viewModel
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[HistoryViewModel::class.java]

        getDataViewModelWithoutId()
        viewModel.getData().observe(this, Observer { data ->
            if (data.isEmpty()) {
                showLoading(false)
                showLayoutList(false)
            } else {
                adapter.setData(data)
                showLayoutList(true)
                showLoading(false)
            }
        })

        // swipe refresh layout
        swipeRefresh.setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(
                this,
                R.color.colorPrimaryRed
            )
        )
        swipeRefresh.setColorSchemeColors(Color.WHITE)

        swipeRefresh.setOnRefreshListener {
            // set/get data from viewModel
            getDataViewModelWithoutId()

            // stop refresh
            swipeRefresh.isRefreshing = false
        }


        // show RecycleView
        showList()

        //setup Actionbar and navigasi up
        val actionbar = supportActionBar
        actionbar?.title = "Riwayat"
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)

        // search function
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.actionSearch)?.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }

        })
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.actionSearch) {
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun getDataViewModelWithoutId() {
        // list ticket
        viewModel.setData(
            session.server.toString(),
            session.token.toString(),
            session.userId.toString(),
            "",
            session.roleId.toString(),
            session.fmbmId.toString()
        )
        viewModel.getData().observe(this, Observer { model ->
            if (model.isEmpty()) {
                showLoading(false)
                showLayoutList(false)
            } else {
                if (model[0].statusResponse.equals("true")) {
                    Log.d(tags, "history: ${model[0].statusResponse.equals("true")}")
                    showLoading(false)
                    showLayoutList(true)
                    adapter.setData(model)
                } else {
                    showLoading(false)
                }
            }
        })

    }

    private fun showList() {
        adapter = HistoryTiketAdapter()
        rvHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvHistory.adapter = adapter

        adapter.setOnItemClickCallback(object : HistoryTiketAdapter.OnItemClickCallback {
            override fun onItemClicked(data: StatusTiket) {
                val roleId = session.roleId
                val mIntent = Intent(this@HistoryActivity, DetailHistoryActivity::class.java)
                mIntent.putExtra(DetailHistoryActivity.EXTRA_DATA, data)
                startActivity(mIntent)
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

    private fun showLayoutList(state: Boolean) {
        if (state) {
            swipeRefresh.visibility = View.VISIBLE
            imageView23.visibility = View.GONE
            textView2.visibility = View.GONE
            textView35.visibility = View.GONE
        } else {
            imageView23.visibility = View.VISIBLE
            textView2.visibility = View.VISIBLE
            textView35.visibility = View.VISIBLE
            swipeRefresh.visibility = View.GONE

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}