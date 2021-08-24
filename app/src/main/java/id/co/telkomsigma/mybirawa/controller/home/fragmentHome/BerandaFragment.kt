package id.co.telkomsigma.mybirawa.controller.home.fragmentHome

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsClient.getPackageName
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import es.dmoral.toasty.Toasty
import id.co.telkomsigma.mybirawa.BuildConfig
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.adapter.BerandaListAdapter
import id.co.telkomsigma.mybirawa.controller.home.beranda.formisian.FormulirIsianActivity
import id.co.telkomsigma.mybirawa.controller.home.beranda.history.HistoryActivity
import id.co.telkomsigma.mybirawa.controller.inbox.DetailInboxActivity
import id.co.telkomsigma.mybirawa.controller.statusTiket.StatusTiketActivity
import id.co.telkomsigma.mybirawa.entity.BerandaTiket
import id.co.telkomsigma.mybirawa.entity.Inbox
import id.co.telkomsigma.mybirawa.entity.PreferenceModel
import id.co.telkomsigma.mybirawa.util.UserPreference
import id.co.telkomsigma.mybirawa.viewmodel.beranda.BerandaTiketViewModel
import kotlinx.android.synthetic.main.bottomsheet_corfirmation.view.*
import kotlinx.android.synthetic.main.fragment_beranda.*
import java.io.File

/**
 * A simple [Fragment] subclass.
 */
class BerandaFragment : Fragment(), View.OnClickListener {

    private val TAG = BerandaFragment::class.java.simpleName
    private lateinit var mUserPreference: UserPreference
    private lateinit var session: PreferenceModel

    private lateinit var viewModel: BerandaTiketViewModel
    private lateinit var adapter: BerandaListAdapter

    private lateinit var inbox: Inbox
    private var fmbmCheck: String? = null

    //pop up bottomSheet
    private lateinit var bottomSheetDialog: BottomSheetDialog

    // auto update using google playcore
    private lateinit var appUpdateManager: AppUpdateManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_beranda, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // onclick listener
        tv_beranda_lihat_tiket.setOnClickListener(this)
        lytBerandaPengaduan.setOnClickListener(this)
        lytBerandaInformation.setOnClickListener(this)
        lytBerandaRiwayat.setOnClickListener(this)
        tvLihatNews.setOnClickListener(this)
        lytBerandaReservasi.setOnClickListener(this)
        ln_news.setOnClickListener(this)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // declaration SharedPreference
        mUserPreference = UserPreference(requireActivity())
        session = mUserPreference.getUser()

        // show loading
        showLoading(true)

        // init popup bottom sheet
        bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)

        //setup bottom navigation icon and text
        val roleId = session.roleId
        if (roleId == "3" || roleId == "4") {
            icInformation.setImageResource(R.drawable.ic_beranda_perangkat_pic)
            tvInformation.text = "Perangkat"
            icGedung.setImageResource(R.drawable.ic_beranda_gedung_pic)
            tvGedung.text = "Gedung"
        }

        // get data from viewModel class
        viewModel = ViewModelProvider(this).get(BerandaTiketViewModel::class.java)
        viewModel.setData(
            session.server.toString(),
            session.token.toString(),
            session.roleId.toString(),
            session.userId.toString(),
            session.fmbmId.toString()
        )
        viewModel.getData().observe(viewLifecycleOwner, Observer { data ->
            if (data.isEmpty()) {
                Log.d(tag, "Data List Bidang Kosong")
                showLoading(false)
            } else {
                adapter.setData(data)
                showLoading(false)
            }
        })
        // call method
        showRecycleView()
        checkVersion()
        showNews()
        checkVersionPlaystore()

    }

    private fun checkVersionPlaystore() {
        appUpdateManager = AppUpdateManagerFactory.create(requireActivity())
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && it.isUpdateTypeAllowed(
                    AppUpdateType.IMMEDIATE
                )
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    it,
                    AppUpdateType.IMMEDIATE,
                    requireActivity(),
                    999
                )
            } else {
                //do something in here if update not available
                Log.d(TAG, "No Update Available ")
            }
        }

    }

    override fun onResume() {
        super.onResume()
        appUpdateManager.appUpdateInfo
            .addOnSuccessListener {
                if (it.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    appUpdateManager.startUpdateFlowForResult(
                        it,
                        AppUpdateType.IMMEDIATE,
                        requireActivity(),
                        999
                    )
                }
            }
        super.onResume()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 999 && resultCode == Activity.RESULT_OK) {
            // do something in here if in-app updates success
            Toasty.success(requireContext(), "Success update", Toasty.LENGTH_SHORT).show()
        } else {
            //do something in here if in-app updates failure
            Toasty.error(requireContext(), "Update Gagal", Toasty.LENGTH_SHORT).show()
        }

    }

    private fun showNews() {

        val baseUrl = session.server.toString()
        val token = session.token.toString()
        val roleId = session.roleId.toString()
        val userId = session.userId.toString()
        val fmbm = session.fmbmId.toString()

        // request data to view model
        viewModel.setNews(baseUrl, token, roleId, userId, fmbm)

        // get data from View Model
        viewModel.getNews().observe(viewLifecycleOwner, Observer { news ->

            if (news.isNullOrEmpty()) {
                newsTitle.text = getString(R.string.txt_tidak_ada_berita)
                newsContent.text = getString(R.string.txt_tidak_ada_berita)
            } else {
                fmbmCheck = news[0].fmbmId
                Glide.with(requireActivity())
                    .load(news[0].url)
                    .error(R.drawable.ic_sipil)
                    .into(newsIcon)

//            tittle news beranda
                if (news[0].subject.equals("null")) {
                    newsTitle.text = getString(R.string.txt_tidak_ada_berita)

                } else {
                    newsTitle.text = news[0].subject
//                set data to entity class
                    inbox = Inbox(
                        news[0].fmbmId,
                        news[0].bidangId,
                        news[0].ticketId,
                        news[0].otych,
                        news[0].userId,
                        news[0].subject,
                        news[0].message,
                        news[0].chageDate,
                        news[0].chusr,
                        news[0].url
                    )
                }

//            content news beranda
                if (news[0].message.equals("null")) {
                    newsContent.text = getString(R.string.txt_tidak_ada_berita)
                } else {
                    newsContent.text = news[0].message
                }


            }
        })
    }

    private fun showRecycleView() {
        adapter = BerandaListAdapter(requireActivity())
        rvListBeranda.layoutManager = LinearLayoutManager(activity)
        rvListBeranda.adapter = adapter
        // onClick adapter
        adapter.setOnItemClickCallback(object : BerandaListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: BerandaTiket, adapterPosition: Int?) {
                val mIntent = Intent(requireContext(), StatusTiketActivity::class.java)
                mIntent.putExtra(StatusTiketActivity.EXTRA_DATA, data)
                mIntent.putExtra(StatusTiketActivity.EXTRA_POSITION_FILTER_BIDANG, adapterPosition)
                startActivity(mIntent)
            }

        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_beranda_lihat_tiket -> {
                val mIntent = Intent(requireContext(), StatusTiketActivity::class.java)
                startActivity(mIntent)
            }
            R.id.lytBerandaPengaduan -> {
                val mIntent = Intent(requireContext(), FormulirIsianActivity::class.java)
                startActivity(mIntent)
            }
            R.id.lytBerandaInformation -> {
                Toasty.info(requireContext(), "Fitur belum tersedia", Toasty.LENGTH_SHORT).show()
//                val mIntent =Intent(requireContext(),PerangkatActivity::class.java)
//                startActivity(mIntent)
            }
            R.id.lytBerandaReservasi -> {
                Toasty.info(requireContext(), "Fitur belum tersedia", Toasty.LENGTH_SHORT).show()
//                val mIntent = Intent(requireContext(),PenilaianGedungActivity::class.java)
//                startActivity(mIntent)
            }
            R.id.lytBerandaRiwayat -> {
                val mIntent = Intent(requireContext(), HistoryActivity::class.java)
                startActivity(mIntent)
            }
            R.id.ln_news -> {
                if (fmbmCheck == null) {
                    Toasty.info(requireContext(), "Tidak ada berita", Toasty.LENGTH_SHORT).show()
                } else {
                    val mIntent = Intent(requireActivity(), DetailInboxActivity::class.java)
                    mIntent.putExtra(DetailInboxActivity.EXTRA_DATA, inbox)
                    startActivity(mIntent)
                }
            }
        }
    }

    private fun checkVersion() {
        // set and get data from view model
        viewModel.setVersion(session.server.toString(), session.token.toString())

        viewModel.getVersion().observe(viewLifecycleOwner, Observer { version ->
            if (version.isEmpty()) {
                Log.e(tag, "NO DATA FROM API VERSION")
            } else {
                val versionNote = version[0].versionNote
                val versionCode = version[0].versionCode!!.toInt()
                val versionChangeDate = version[0].changeDate
                val versionURL = version[0].url

                val applicationCode = BuildConfig.VERSION_CODE
//                val applicationCode = 1000
                session.versiApp = version[0].versionCode
                mUserPreference.setUser(session)

//                if (applicationCode < versionCode){
//                    Log.e(tag, "Update Aplikasi ")
////                    val testUrl = "http://ulamapro.digitalevent.id/download/NAU.apk"
//                    popupVersion(versionNote, versionChangeDate, versionURL)
////                    appUpdate.start()
//                }else {
//                    Log.e(tag, "Aplikasi Terupdate")
//                }
            }
        })


    }

    // popup auto update
    private fun popupVersion(
        title: String?,
        versionChangeDate: String?,
        url: String?
    ) {
        Log.e(tag, "method : confirmation")
        //init bottomSheet
        val views = layoutInflater.inflate(R.layout.bottomsheet_corfirmation, null)
        bottomSheetDialog.setContentView(views)
        bottomSheetDialog.setCancelable(false)
        views.tvTitttleConfirm.text = "Update Baru "
        views.tvContentConfirm.text = "Note : ${title.toString()}"
        views.btnNoConfirm.text = "batal"
        views.btnYesConfirm.text = "Unduh"
        bottomSheetDialog.show()

        //onclick bottomsheet
        views.btnYesConfirm.setOnClickListener {
            bottomSheetDialog.dismiss()
            if (Build.VERSION.SDK_INT >= 23) {
                if (context?.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    //Log.v(TAG,"Permission is granted");
                    //apkDownloader(url,title);
                    println("masuk apk")
//                    apkDownloader(url, title)
                    checkVersionPlaystore()
                } else {
                    println("masuk permission")
                    // ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            (context as Activity),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    ) {
                        showDialogOK("The Permissions are required for this application",
                            DialogInterface.OnClickListener { dialog, which ->
                                when (which) {
                                    DialogInterface.BUTTON_POSITIVE ->                                                           // proceed with logic by disabling the related features or quit the app.
//                                        apkDownloader(url, title)
                                        checkVersionPlaystore()
                                    DialogInterface.BUTTON_NEGATIVE -> {
                                    }
                                }
                            })
                    }
                }
            } else {
                println("langsung update")
//                apkDownloader(url, title)
                checkVersionPlaystore()

            }
        }

        views.btnNoConfirm.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

    }

    // dialog auto update
    private fun showDialogOK(
        message: String,
        okListener: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(context)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("SETTINGS", okListener)
            .create()
            .show()
    }

    // apk download auto update
    private fun apkDownloader(
        url: String?,
        title: String?
    ) {
        val pd =
            ProgressDialog.show(context, "Please Wait..", "Downloading data", false, false)
        var destination =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/"
        println("PathDestination : $destination")
        // System.out.println();
//        String fileName = url.split("/")[url.split("/").length-1];
        val fileName = "mybirawa.apk"
        println(fileName)
        destination += fileName
        val uri = Uri.parse("file://$destination")
        println("UriDestination : $uri")

        //Delete update file if exists
        val file = File(destination)
        if (file.exists()) {
            file.delete() //file.delete() - test this, I think sometimes it doesnt work
        }
        println(url)

        //get url of app on server
        //String url = url;

        //set downloadmanager
        val request = DownloadManager.Request(Uri.parse(url))
        request.setDescription(title)
        request.setTitle(title)

        //set destination
        request.setDestinationUri(uri)

        // get download service and enqueue file
        val manager =
            requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = manager.enqueue(request)
        println("masuk1 ")
        //set BroadcastReceiver to install app when .apk is downloaded
        val onComplete: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(ctxt: Context, intent: Intent) {
                pd.dismiss()
                val install = Intent(Intent.ACTION_VIEW)
                install.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                install.setDataAndType(uri, "application/vnd.android.package-archive")
                manager.getMimeTypeForDownloadedFile(downloadId)
                context!!.startActivity(install)
                context!!.unregisterReceiver(this)
                //(Activity)context.;
            }
        }
        //register receiver for when .apk download is compete
        requireContext().registerReceiver(
            onComplete,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }


}
