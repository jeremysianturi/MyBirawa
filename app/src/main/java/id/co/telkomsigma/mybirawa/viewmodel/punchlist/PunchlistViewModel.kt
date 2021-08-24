package id.co.telkomsigma.mybirawa.viewmodel.punchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.co.telkomsigma.mybirawa.controller.home.beranda.perangkat.punchlistTeknisi.PunchlistTeknisiActivity
import id.co.telkomsigma.mybirawa.entity.FilterBidang
import id.co.telkomsigma.mybirawa.entity.Punchlist

class PunchlistViewModel : ViewModel() {

    val tag = PunchlistTeknisiActivity::class.java.simpleName
    private val listFilterPuncnlist = MutableLiveData<ArrayList<FilterBidang>>()
    private val listContentPunchlist = MutableLiveData<ArrayList<Punchlist>>()

    fun setFilterPunclist() {

        val listFilter = ArrayList<FilterBidang>()

        val mData = FilterBidang("1", "Semua", true)
        val mData1 = FilterBidang("2", "Belum", false)
        val mData2 = FilterBidang("3", "Selesai", false)
        val mData3 = FilterBidang("4", "Tertunda", false)

        listFilter.add(mData)
        listFilter.add(mData1)
        listFilter.add(mData2)
        listFilter.add(mData3)

        listFilterPuncnlist.postValue(listFilter)

    }

    fun setContentPunchlist() {
        val listPunchlist = ArrayList<Punchlist>()

        val mData1 = Punchlist(
            "PL-0000001",
            "Gedung Menara Multimedia",
            "Lantai 1",
            "Sipil",
            "Plafon Bocor",
            "Belum",
            "1"
        )
        val mData2 = Punchlist(
            "PL-0000002",
            "Gedung Menara Multimedia",
            "Lantai 2",
            "ME",
            "AC RUSAK",
            "Trtunda",
            "2"
        )

        listPunchlist.add(mData1)
        listPunchlist.add(mData2)

        listContentPunchlist.postValue(listPunchlist)
    }

    fun getContentPunchlist(): LiveData<ArrayList<Punchlist>> {
        return listContentPunchlist
    }

    fun getPunchlIst(): LiveData<ArrayList<FilterBidang>> {
        return listFilterPuncnlist
    }
}