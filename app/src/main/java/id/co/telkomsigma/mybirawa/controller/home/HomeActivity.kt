package id.co.telkomsigma.mybirawa.controller.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import es.dmoral.toasty.Toasty
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.controller.home.fragmentHome.BerandaFragment
import id.co.telkomsigma.mybirawa.controller.home.fragmentHome.InboxFragment
import id.co.telkomsigma.mybirawa.controller.home.fragmentHome.ProfileFragment
import id.co.telkomsigma.mybirawa.controller.register.fragmentRegister.RegisterOneFragment
import id.co.telkomsigma.mybirawa.entity.PreferenceModel
import id.co.telkomsigma.mybirawa.util.UserPreference
import kotlinx.android.synthetic.main.activity_main.*


class HomeActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG: String = HomeActivity::class.java.simpleName
    private lateinit var mUserPreference: UserPreference
    private lateinit var session: PreferenceModel
    private var doubleBackToExitPressedOnce = false

    private var mFragment = Fragment()

    private val mFragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)

        mUserPreference = UserPreference(this)
        session = mUserPreference.getUser()

        val deviceOs = android.os.Build.VERSION.SDK_INT

        Log.d(TAG, "deviceOperationSystem : $deviceOs")


//        FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.notification_channel_name))

        ln_profile.setOnClickListener(this)
        ln_beranda.setOnClickListener(this)
        ln_pesan.setOnClickListener(this)

        mFragment = BerandaFragment()

//        val token = FirebaseInstanceId.getInstance().token

        val fragment = mFragmentManager.findFragmentByTag(BerandaFragment::class.java.simpleName)
        if (fragment is BerandaFragment) {
            Log.d("BerandaFragment", "Fragment Name :" + RegisterOneFragment::class.java.simpleName)
            Log.e(TAG, "Bottom_visibility : visible")
        }

        mFragmentManager.commit {
            replace(R.id.frame_container_home, mFragment)
        }

        // method
        requestPermission()

    }

    private fun requestPermission() {
        val PERMISSION_ALL = 1

        val PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INSTALL_PACKAGES,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.REQUEST_INSTALL_PACKAGES,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )

        if (!hasPermissions(this, PERMISSIONS.toString())) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
        }
    }

    private fun hasPermissions(
        context: Context?,
        vararg permissions: String?
    ): Boolean {
        if (context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission!!
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        BerandaFragment()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ln_profile -> {
                mFragment = ProfileFragment()
                //image
                img_profile_home.setImageResource(R.drawable.icon_menu_bar_profile_selector)
                img_beranda_home.setImageResource(R.drawable.icon_menu_bar_beranda)
                img_inbox_home.setImageResource(R.drawable.icon_menu_bar_pesan)
                //text
                tv_profile_home.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.black_text
                    )
                )
                tv_beranda_home.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.grey_text
                    )
                )
                tv_inbox_home.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.grey_text
                    )
                )
                mFragmentManager.commit {
                    replace(
                        R.id.frame_container_home,
                        mFragment,
                        ProfileFragment::class.java.simpleName
                    )
                }

            }
            R.id.ln_beranda -> {
                mFragment = BerandaFragment()
                // setup image
                img_profile_home.setImageResource(R.drawable.icon_menu_bar_profile)
                img_beranda_home.setImageResource(R.drawable.icon_menu_bar_beranda_select)
                img_inbox_home.setImageResource(R.drawable.icon_menu_bar_pesan)
                // setup text color
                tv_profile_home.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.grey_text
                    )
                )
                tv_beranda_home.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.black_text
                    )
                )
                tv_inbox_home.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.grey_text
                    )
                )
                mFragmentManager.commit {
                    replace(
                        R.id.frame_container_home,
                        mFragment,
                        BerandaFragment::class.java.simpleName
                    )
                }
            }
            R.id.ln_pesan -> {
                mFragment = InboxFragment()
                // setup image
                img_profile_home.setImageResource(R.drawable.icon_menu_bar_profile)
                img_beranda_home.setImageResource(R.drawable.icon_menu_bar_beranda)
                img_inbox_home.setImageResource(R.drawable.icon_menu_bar_pesan_select)
                // setup text color
                tv_profile_home.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.grey_text
                    )
                )
                tv_beranda_home.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.grey_text
                    )
                )
                tv_inbox_home.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.black_text
                    )
                )
                mFragmentManager.commit {
                    replace(
                        R.id.frame_container_home,
                        mFragment,
                        InboxFragment::class.java.simpleName
                    )
                }
            }
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
            System.exit(0)
        }
        this.doubleBackToExitPressedOnce = true
//        Toast.makeText(this, "Klik sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()
        Toasty.warning(this, "Klik sekali lagi untuk keluar", Toasty.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

}
