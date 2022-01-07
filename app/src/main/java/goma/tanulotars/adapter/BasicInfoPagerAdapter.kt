package goma.tanulotars.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import goma.tanulotars.fragment.*

class BasicInfoPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {
    companion object {
        private const val NUM_PAGES = 5
    }

    override fun getCount(): Int = NUM_PAGES

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> HelloFragment()
            1 -> NameFragment()
            2 -> ProfilePictureFragment()
            3 -> SubjectFragment()
            4 -> IntroductionFragment()
            else -> throw IllegalArgumentException("No such page!")
        }
    }
}