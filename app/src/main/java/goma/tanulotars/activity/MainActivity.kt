package goma.tanulotars.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import goma.tanulotars.R
import goma.tanulotars.databinding.ActivityMainBinding
import goma.tanulotars.fragment.FriendsFragment
import goma.tanulotars.fragment.PostsFragment
import goma.tanulotars.fragment.ProfileFragment
import goma.tanulotars.interfaces.OnUserLoaded
import goma.tanulotars.model.CurrentUser
import goma.tanulotars.model.User


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    OnUserLoaded {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val postsFragment = PostsFragment()
    private val friendsFragment = FriendsFragment()
    private var profileFragment: ProfileFragment = initProfile(CurrentUser.user)
    private var currentFragment: Fragment = Fragment()
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeFragment(postsFragment)
        binding.navView.setNavigationItemSelectedListener(this)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.nav_home -> {
                changeFragment(postsFragment)
            }
            R.id.nav_friends -> {
                changeFragment(friendsFragment)
            }
            R.id.nav_profile -> {
                val gson = Gson()
                val userJson = gson.toJson(CurrentUser.user)
                val bundle = Bundle()
                bundle.putString("userJson", userJson)

                profileFragment.arguments = bundle

                changeFragment(profileFragment)
            }
            R.id.nav_logout -> {
                FirebaseAuth.getInstance().signOut()
                CurrentUser.user = User()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {

    }

    private fun changeFragment(newFragment: Fragment) {
        if(currentFragment == newFragment)
            return

        supportFragmentManager.beginTransaction().remove(currentFragment).commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainerView, newFragment)
            .commit()
        currentFragment = newFragment
    }



    fun initProfile(user: User): ProfileFragment{
        profileFragment = ProfileFragment()

        val gson = Gson()
        val userJson = gson.toJson(CurrentUser.user)
        val bundle = Bundle()
        bundle.putString("userJson", userJson)

        profileFragment.arguments = bundle
        return profileFragment
    }

    //A bejelentkezett user adatai később erkeznek meg, mint az activity indulása, ezért a loginactivity elsüt egy eseményt amikor kész
    override fun onUserLoaded(user: User) {
        CurrentUser.user = user
        profileFragment = ProfileFragment()

        val gson = Gson()
        val userJson = gson.toJson(CurrentUser.user)
        val bundle = Bundle()
        bundle.putString("userJson", userJson)

        profileFragment.arguments = bundle
    }


}