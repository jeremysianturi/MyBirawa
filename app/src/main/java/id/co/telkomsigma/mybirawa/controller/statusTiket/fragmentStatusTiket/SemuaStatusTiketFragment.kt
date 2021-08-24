package id.co.telkomsigma.mybirawa.controller.statusTiket.fragmentStatusTiket

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
import es.dmoral.toasty.Toasty

import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.adapter.FilterStatusTiketAdapter
import id.co.telkomsigma.mybirawa.adapter.SemuaStatusTiketAdapter
import id.co.telkomsigma.mybirawa.controller.statusTiket.detailTiket.DetailTiketActivity
import id.co.telkomsigma.mybirawa.controller.statusTiket.detailTiketHelpDesk.DetailTiketHelpdeskActivity
import id.co.telkomsigma.mybirawa.entity.*
import id.co.telkomsigma.mybirawa.util.CustomOnItemClickListener
import id.co.telkomsigma.mybirawa.util.UserPreference
import id.co.telkomsigma.mybirawa.viewmodel.statusTiketViewModel.SemuaStatusTiketViewModel
import kotlinx.android.synthetic.main.semua_status_tiket_fragment.*

class SemuaStatusTiketFragment : Fragment() {

    companion object {
        fun newInstance() = SemuaStatusTiketFragment()
        const val EXTRA_DATA_ID_BIDANG = "extra_data_id_bidang"
        const val EXTRA_POSITION_FILTER_BIDANG = "extra_position_bidang"

    }

    private var idBidangRequest: String? = null

    private lateinit var mUserPreference: UserPreference
    private lateinit var session: PreferenceModel

    private lateinit var adapter: SemuaStatusTiketAdapter
    private lateinit var adapterFilter: FilterStatusTiketAdapter
    private lateinit var viewModel: SemuaStatusTiketViewModel

    private lateinit var mListProgressTiket: ListProgressTiket
    private var mArrayListProgressTicket = ArrayList<ListProgressTiket>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.semua_status_tiket_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        showLoading(true)
        showList()
        showListFilter()

        mUserPreference = UserPreference(requireContext())
        session = mUserPreference.getUser()

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            SemuaStatusTiketViewModel::class.java
        )


        // recieve data from status tiket activity
        val bundle = arguments
        Log.d(tag, "bundle : $bundle")
        if (bundle != null) {
            val data = bundle.getParcelable(EXTRA_DATA_ID_BIDANG) as BerandaTiket?
            idBidangRequest = data?.bidangId.toString()
            Log.d(tag, "idFromBerandaSemua : $idBidangRequest")
            if (idBidangRequest != "null") {
                Log.d(tag, "masuk PAKE ID ")
                getDataViewModelWithID()
            } else {
                Log.d(tag, "masuk GAAAAAAA MAKE  ID ")
                getDataViewModelWithoutId()
            }
        }

        val dataPosition = bundle?.get(EXTRA_POSITION_FILTER_BIDANG)
        Log.d(tag, "dataBidangforPosition : $dataPosition ")

        //filter
        viewModel.setFilterData(
            session.server.toString(),
            session.token.toString(),
            session.userId.toString(),
            session.roleId.toString(),
            session.fmbmId.toString(),
            dataPosition.toString()
        )
        viewModel.getFilterData().observe(viewLifecycleOwner, Observer { model ->
            adapterFilter.setData(model, dataPosition as Int)
        })

    }

    private fun getDataViewModelWithID() {
        if (idBidangRequest != null) {
            // list ticket
            viewModel.setData(
                session.server.toString(),
                session.token.toString(),
                session.userId.toString(),
                idBidangRequest.toString(),
                session.roleId.toString(),
                session.fmbmId.toString()
            )
            viewModel.getData().observe(viewLifecycleOwner, Observer { model ->
                if (model.isEmpty()) {
                    showLoading(false)
                    showAdapter(false)
                } else {
                    if (model[0].statusResponse.equals("true")) {
//                        Log.d(tag,"statusSemuaTiket : $model")
                        showLoading(false)
                        showAdapter(true)
                        adapter.setData(model)
                    } else {
                        showLoading(false)
                    }
                }
            })
        }

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
        viewModel.getData().observe(viewLifecycleOwner, Observer { model ->
            if (model.isEmpty()) {
                showLoading(false)
                showAdapter(false)
            } else {
                if (model[0].statusResponse.equals("true")) {
                    Log.d(tag, "statusSemuaTiket : $model")
                    showLoading(false)
                    showAdapter(true)
                    adapter.setData(model)
                } else {
                    showLoading(false)
                }
            }
        })

    }

    private fun showList() {
        adapter = SemuaStatusTiketAdapter()
        rv_status_tiket.layoutManager = LinearLayoutManager(activity)
        rv_status_tiket.adapter = adapter
        //onclick adapter
        adapter.setOnItemClickCallback(object : SemuaStatusTiketAdapter.OnItemClickCallback {
            override fun onItemClicked(data: StatusTiket) {
                val roleId = session.roleId
                if (roleId == "3" || roleId == "4") {
                    val mIntent = Intent(requireContext(), DetailTiketHelpdeskActivity::class.java)
                    mIntent.putExtra(DetailTiketHelpdeskActivity.EXTRA_DATA, data)
                    startActivity(mIntent)
                } else {
                    val mIntent = Intent(requireContext(), DetailTiketActivity::class.java)
                    mIntent.putExtra(DetailTiketActivity.EXTRA_DATA, data)
                    startActivity(mIntent)
                }
            }

        })
    }

    private fun showListFilter() {
        adapterFilter = FilterStatusTiketAdapter()
        rcFilterStatusTiket.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rcFilterStatusTiket.adapter = adapterFilter
        // onClick Adapter

        adapterFilter.setOnItemClickCallback(object : FilterStatusTiketAdapter.OnItemClickCallback {
            override fun onItemClicked(data: FilterBidang, position: Int) {
                showLoading(true)
                //filter
                viewModel.setFilterData(
                    session.server.toString(),
                    session.token.toString(),
                    session.userId.toString(),
                    session.roleId.toString(),
                    session.fmbmId.toString(),
                    position.toString()
                )
                viewModel.getFilterData().observe(viewLifecycleOwner, Observer { model ->
                    adapterFilter.setData(model, position)

                })

                val idBidang = data.bidangId
                if (idBidang != null) {
                    showLoading(true)
                    viewModel.setData(
                        session.server.toString(),
                        session.token.toString(),
                        session.userId.toString(),
                        idBidang,
                        session.roleId.toString(),
                        session.fmbmId.toString()
                    )
//                    Toasty.info(requireContext(),"idBidang : ${idBidang.toString()} ",Toasty.LENGTH_SHORT).show()
                    viewModel.getData().observe(viewLifecycleOwner, Observer { model ->
                        if (model.isEmpty()) {
                            showLoading(false)
                            adapter.setData(model)

                        } else {
                            showLoading(false)
                        }
                    })

                }

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
            imgNodata.visibility = View.GONE
            rv_status_tiket.visibility = View.VISIBLE
        } else {
            imgNodata.visibility = View.VISIBLE
            rv_status_tiket.visibility = View.GONE
        }
    }


}
