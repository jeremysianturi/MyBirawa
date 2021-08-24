package id.co.telkomsigma.mybirawa.controller.splashscreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.animation.AnimationUtils
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.controller.home.HomeActivity
import id.co.telkomsigma.mybirawa.controller.onboarding.OnboardingActivity
import id.co.telkomsigma.mybirawa.entity.PreferenceModel
import id.co.telkomsigma.mybirawa.util.UserPreference
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {

    private lateinit var mUserPreference: UserPreference
    private lateinit var session: PreferenceModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // set default night mode "NO:
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        mUserPreference = UserPreference(this)
        session = mUserPreference.getUser()

        // animation
        val topanim =
            android.view.animation.AnimationUtils.loadAnimation(this, R.anim.top_animation)
        val bottompanim =
            android.view.animation.AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        // set animation
        txt_splashscreen.animation = topanim
        img_splashscreen.animation = bottompanim

//        // set default night mode "NO:
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        session.server = "http://dev-api-birawa.digitalevent.id/api" // development
//        session.server = "https://api-birawa.digitalevent.id/api" // production
        mUserPreference.setUser(session)

        val splashTread: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(2000)
                } catch (e: InterruptedException) {
                    // do nothing
                } finally {
                    if (session.isLogin) {
                        val i = Intent(this@SplashScreen, HomeActivity::class.java)
//                        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//                        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(i)
//                        finish()
                    } else {
                        val i = Intent(this@SplashScreen, OnboardingActivity::class.java)
                        startActivity(i)
//                        finish()
                    }
                }

            }

        }

        splashTread.start()

    }
}
