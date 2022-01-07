package goma.tanulotars.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import goma.tanulotars.fragment.HelloFragment
import goma.tanulotars.fragment.IntroductionFragment
import goma.tanulotars.fragment.NameFragment
import goma.tanulotars.fragment.SubjectFragment

class BasicInfoPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {
    companion object {
        private const val NUM_PAGES = 4
    }

    override fun getCount(): Int = NUM_PAGES

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> HelloFragment()
            1 -> NameFragment()
            2 -> SubjectFragment()
            3 -> IntroductionFragment()
            else -> throw IllegalArgumentException("No such page!")
        }
    }
}