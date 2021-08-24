package id.co.telkomsigma.mybirawa.controller.onboarding


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import es.dmoral.toasty.Toasty
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.adapter.OnboardingViewPagerAdapter
import id.co.telkomsigma.mybirawa.controller.login.LoginActivity
import id.co.telkomsigma.mybirawa.controller.register.RegisterActivity
import id.co.telkomsigma.mybirawa.entity.PreferenceModel
import id.co.telkomsigma.mybirawa.entity.RegisterPreferenceModel
import id.co.telkomsigma.mybirawa.util.RegisterPreference
import id.co.telkomsigma.mybirawa.util.UserPreference
import kotlinx.android.synthetic.main.activity_on_boarding.*
import kotlin.system.exitProcess

class OnboardingActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = OnboardingActivity::class.java.simpleName
    private lateinit var mViewPager: ViewPager

    private var doubleBackToExitPressedOnce = false


    private lateinit var mUserPreference: UserPreference
    private lateinit var mPreferenceModel: PreferenceModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        // add User Preference
        mUserPreference = UserPreference(this)
        mPreferenceModel = mUserPreference.getUser()

//        Log.e(TAG,"server_access ${mPreferenceModel.server}" )

        mViewPager = findViewById(R.id.viewPager)
        mViewPager.adapter = OnboardingViewPagerAdapter(supportFragmentManager, this)
        mViewPager.offscreenPageLimit = 1

        btn_login_onboarding.setOnClickListener(this)
        btn_register_onboarding.setOnClickListener(this)


    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login_onboarding -> {
                val mIntentLogin = Intent(this, LoginActivity::class.java)
                startActivity(mIntentLogin)
            }
            R.id.btn_register_onboarding -> {
                val mIntentRegister = Intent(this, RegisterActivity::class.java)
                startActivity(mIntentRegister)
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
            exitProcess(0)
        }
        this.doubleBackToExitPressedOnce = true
//        Toast.makeText(this, "Klik sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()
        Toasty.warning(this, "Klik sekali lagi untuk keluar", Toasty.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

}
