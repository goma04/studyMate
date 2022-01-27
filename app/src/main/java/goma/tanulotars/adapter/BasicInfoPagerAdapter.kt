package goma.tanulotars.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.gson.Gson
import goma.tanulotars.fragment.ProfileFragment
import goma.tanulotars.fragment.registerFragment.*
import goma.tanulotars.model.CurrentUser

class BasicInfoPagerAdapter(manager: FragmentManager) :
    FragmentStatePagerAdapter(manager) {
    companion object {
        private const val NUM_PAGES = 6
    }

    override fun getCount(): Int = NUM_PAGES

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> HelloFragment()
            1 -> NameFragment()
            2 -> ProfilePictureFragment()
            3 -> SubjectFragment()
            4 -> IntroductionFragment()
            5 -> {
                val gson = Gson()
                val bundle = Bundle()

                bundle.putString("userJson", gson.toJson(CurrentUser.user))

                val profileFragment = ProfileFragment()
                profileFragment.arguments = bundle

                return profileFragment
            }
            else -> throw IllegalArgumentException("No such page!")
        }
    }


}