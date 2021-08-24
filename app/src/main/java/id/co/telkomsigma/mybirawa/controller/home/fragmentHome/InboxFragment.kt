package id.co.telkomsigma.mybirawa.controller.home.fragmentHome

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.adapter.InboxAdapter
import id.co.telkomsigma.mybirawa.controller.inbox.DetailInboxActivity
import id.co.telkomsigma.mybirawa.entity.Inbox
import id.co.telkomsigma.mybirawa.entity.PreferenceModel
import id.co.telkomsigma.mybirawa.util.UserPreference
import id.co.telkomsigma.mybirawa.viewmodel.beranda.InboxViewModel
import kotlinx.android.synthetic.main.inbox_fragment.*

class InboxFragment : Fragment() {

    companion object {
        fun newInstance() = InboxFragment()
    }

    private val tags = InboxFragment::class.java.simpleName
    private lateinit var viewModel: InboxViewModel

    private lateinit var mUserPreference: UserPreference
    private lateinit var session: PreferenceModel
    private lateinit var adapter: InboxAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.inbox_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // declaration SharedPreference
        mUserPreference = UserPreference(requireActivity())
        session = mUserPreference.getUser()

        showLoading(true)
        // init view model
        viewModel = ViewModelProvider(this).get(InboxViewModel::class.java)

        // call method
        getInbox()
        showInbox()

    }

    private fun getInbox() {

        val baseUrl = session.server.toString()
        val token = session.token.toString()
        val roleId = session.roleId.toString()
        val userId = session.userId.toString()
        val fmbm = session.fmbmId.toString()

        // request to server
        viewModel.setData(baseUrl, token, roleId, userId, fmbm)

        // get data from server
        viewModel.getData().observe(viewLifecycleOwner, Observer { data ->
            if (data.isNullOrEmpty()) {
                Log.e(tags, "inbox Empty")
                showAdapter(false)
                showLoading(false)
            } else {
                showAdapter(true)
                adapter.setData(data)
                showLoading(false)
                Log.e(tags, "inbox Not Empty")
            }
        })
    }

    private fun showInbox() {
        adapter = InboxAdapter()
        rvInbox.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvInbox.setHasFixedSize(true)
        rvInbox.adapter = adapter

        adapter.setOnItemClickCallback(object : InboxAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Inbox) {
                val mIntent = Intent(requireActivity(), DetailInboxActivity::class.java)
                mIntent.putExtra(DetailInboxActivity.EXTRA_DATA, data)
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

    private fun showAdapter(state: Boolean) {
        if (state) {
            layoutAdapter.visibility = View.VISIBLE
            layout_no_inbox.visibility = View.GONE
        } else {
            layout_no_inbox.visibility = View.VISIBLE
            layoutAdapter.visibility = View.GONE

        }
    }


}
