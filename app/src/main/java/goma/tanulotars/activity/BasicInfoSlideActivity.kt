package goma.tanulotars.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import goma.tanulotars.BuildConfig
import goma.tanulotars.adapter.BasicInfoPagerAdapter
import goma.tanulotars.adapter.viewPager.transform.DepthPageTransformer
import goma.tanulotars.databinding.ActivityBasicInfoSlideBinding

class BasicInfoSlideActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBasicInfoSlideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasicInfoSlideBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (BuildConfig.DEBUG) {
            Firebase.database.useEmulator("10.0.2.2", 9000)
            Firebase.auth.useEmulator("10.0.2.2", 9099)
            Firebase.storage.useEmulator("10.0.2.2", 9199)
        }

        binding.viewPager.adapter = BasicInfoPagerAdapter(supportFragmentManager)
        binding.viewPager.setPageTransformer(true, DepthPageTransformer())

       // startActivity(Intent(this, MainActivity::class.java))

    }


}