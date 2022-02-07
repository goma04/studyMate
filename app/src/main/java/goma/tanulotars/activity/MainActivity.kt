package goma.tanulotars.activity

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import goma.tanulotars.NewFriendDialog
import goma.tanulotars.R
import goma.tanulotars.databinding.ActivityMainBinding
import goma.tanulotars.firebase.FirebaseUtility
import goma.tanulotars.fragment.FriendsFragment
import goma.tanulotars.fragment.PostsFragment
import goma.tanulotars.fragment.ProfileFragment
import goma.tanulotars.interfaces.OnUserLoaded
import goma.tanulotars.model.CurrentUser
import goma.tanulotars.model.Relationship
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
        binding.tvFragmentTitle.text = "Home"
        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            CurrentUser.user = User()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btHelp.setOnClickListener {
            startActivity(Intent(this, HelpActivity::class.java))
        }


        binding.btnMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(Gravity.LEFT);
        }


        checkForFriendsUpdate()
    }

    private fun checkForFriendsUpdate() {
        val db = Firebase.firestore
        //Itt minden megnyitáskor frissítem a Curentuser friendid listáját azért, mert lehet hogy közben valaki hozzáadta.
        //ezt majd úgy kell változtatni, hogy ha valaki addolta őt akkor frissüljön és így nem kell ezt
        val relationships = db.collection("relationships")
        relationships.get()
            .addOnSuccessListener { result ->
                if(updateFriendsIDFromDatabase(result)){
                    val newFragment = NewFriendDialog()
                    newFragment.show(supportFragmentManager,"tag")
                }
            }
    }

    private fun updateFriendsIDFromDatabase(result: QuerySnapshot): Boolean {
        for (document in result) {
            val relationship = document.toObject<Relationship>()
            val id1 = relationship.userIdOne
            val id2 = relationship.userIdTwo

            if (id1 == CurrentUser.user.id && !CurrentUser.user.friendsId.contains(id2)) {
                CurrentUser.user.friendsId += id2
                FirebaseUtility.updateOrCreateUser(CurrentUser.user)
                return true
            }

            if (id2 == CurrentUser.user.id && !CurrentUser.user.friendsId.contains(id1)) {
                CurrentUser.user.friendsId += id1
                FirebaseUtility.updateOrCreateUser(CurrentUser.user)
                return true
            }
        }
        return false
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.nav_home -> {
                binding.tvFragmentTitle.text = "Posztok"
                changeFragment(postsFragment)
            }
            R.id.nav_friends -> {
                binding.tvFragmentTitle.text = "Tanulótársak"
                changeFragment(friendsFragment)
            }
            R.id.nav_profile -> {
                binding.tvFragmentTitle.text = "Profil"
                val gson = Gson()
                val userJson = gson.toJson(CurrentUser.user)
                val bundle = Bundle()
                bundle.putString("userJson", userJson)

                profileFragment.arguments = bundle

                changeFragment(profileFragment)
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {

    }

    private fun changeFragment(newFragment: Fragment) {
        if (currentFragment == newFragment)
            return

        supportFragmentManager.beginTransaction().remove(currentFragment).commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainerView, newFragment)
            .commit()
        currentFragment = newFragment
    }


    fun initProfile(user: User): ProfileFragment {
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