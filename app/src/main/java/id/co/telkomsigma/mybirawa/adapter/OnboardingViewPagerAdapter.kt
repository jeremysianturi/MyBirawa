package id.co.telkomsigma.mybirawa.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.controller.onboarding.OnboardingItemFragment

class OnboardingViewPagerAdapter(manager: FragmentManager, private val context: Context) :

    FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    // Returns total number of pages
    override fun getCount(): Int {
        return NUM_ITEMS
    }

    // Returns the fragment to display for that page
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> OnboardingItemFragment.newInstance(
                context.resources.getString(R.string.text_tittle_onboarding_1),
                context.resources.getString(R.string.text_description_onboarding_1),
                R.drawable.onboard_1
//                R.raw.lottie_delivery_boy_bumpy_ride
            )
            1 -> OnboardingItemFragment.newInstance(
                context.resources.getString(R.string.text_tittle_onboarding_2),
                context.resources.getString(R.string.text_description_onboarding_2),
                R.drawable.onboard_2
//                R.raw.lottie_developer
            )
            2 -> OnboardingItemFragment.newInstance(
                context.resources.getString(R.string.text_tittle_onboarding_3),
                context.resources.getString(R.string.text_description_onboarding_3),
                R.drawable.onboard_3
//                R.raw.lottie_girl_with_a_notebook
            )
            else -> null
        }!!
    }

    // Returns the page title for the top indicator
    override fun getPageTitle(position: Int): CharSequence? {
        return "Page $position"
    }

    companion object {
        private const val NUM_ITEMS = 3
    }

}