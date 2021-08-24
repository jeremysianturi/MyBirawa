package id.co.telkomsigma.mybirawa.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import id.co.telkomsigma.mybirawa.controller.home.fragmentHome.ProfileFragment

class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var TAB_TITLES = ArrayList<String>()
    private var fragment = ArrayList<Fragment>()

    fun addFragments(
        fragments: Fragment?,
        titles: String?
    ) {
        fragments?.let { fragment.add(it) }
        titles?.let { this.TAB_TITLES.add(it) }
    }


    override fun getItem(position: Int): Fragment {

        return fragment[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return TAB_TITLES[position]
    }

    override fun getCount(): Int {
        return fragment.size
    }
}