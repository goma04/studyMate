package goma.tanulotars.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

import goma.tanulotars.fragment.*
import goma.tanulotars.model.User

class BasicInfoPagerAdapter(manager: FragmentManager, val user: User) :
    FragmentStatePagerAdapter(manager) {
    companion object {
        private const val NUM_PAGES = 6
    }

    override fun getCount(): Int = NUM_PAGES

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> HelloFragment()
            1 -> NameFragment(user)
            2 -> ProfilePictureFragment(user)
            3 -> SubjectFragment(user)
            4 -> IntroductionFragment(user)
            5 -> SummaryFragment(user)
            else -> throw IllegalArgumentException("No such page!")
        }
    }


}