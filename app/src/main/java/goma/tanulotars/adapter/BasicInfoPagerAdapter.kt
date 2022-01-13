package goma.tanulotars.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

import goma.tanulotars.fragment.*
import goma.tanulotars.model.Student

class BasicInfoPagerAdapter(manager: FragmentManager, val student: Student) :
    FragmentStatePagerAdapter(manager) {
    companion object {
        private const val NUM_PAGES = 6
    }

    override fun getCount(): Int = NUM_PAGES

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> HelloFragment()
            1 -> NameFragment(student)
            2 -> ProfilePictureFragment(student)
            3 -> SubjectFragment(student)
            4 -> IntroductionFragment(student)
            5 -> SummaryFragment(student)
            else -> throw IllegalArgumentException("No such page!")
        }
    }


}