package goma.tanulotars.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import goma.tanulotars.R
import goma.tanulotars.databinding.ActivityProfileBinding
import goma.tanulotars.fragment.ProfileFragment


class ProfileActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)

        val userJson = intent.getStringExtra("userJson")
        val bundle = Bundle()
        bundle.putString("userJson", userJson)


        val profileFragment = ProfileFragment()
        profileFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .add(R.id.fvProfile, profileFragment)
            .commit()


        binding.fvProfile
        setContentView(binding.root)
    }


}