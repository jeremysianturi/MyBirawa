package id.co.telkomsigma.mybirawa.controller.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.commit
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.controller.register.fragmentRegister.RegisterOneFragment

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val mFragmentManager = supportFragmentManager
        val mRegisterOneFragment = RegisterOneFragment()

        val fragment =
            mFragmentManager.findFragmentByTag(RegisterOneFragment::class.java.simpleName)
        if (fragment !is RegisterOneFragment)
            Log.d(
                "MyFlexibleFragment",
                "Fragment Name :" + RegisterOneFragment::class.java.simpleName
            )

        // commit frame layout dengan RegisterOneFragment
        mFragmentManager.commit {
            add(
                R.id.frame_container,
                mRegisterOneFragment,
                RegisterOneFragment::class.java.simpleName
            )
        }

        //setup Actionbar and navigasi up
        val actionbar = supportActionBar
        actionbar?.title = "Register"
    }


}
