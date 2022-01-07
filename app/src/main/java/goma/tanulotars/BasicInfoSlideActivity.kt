package goma.tanulotars

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import goma.tanulotars.adapter.BasicInfoPagerAdapter
import goma.tanulotars.adapter.viewPager.transform.DepthPageTransformer
import goma.tanulotars.databinding.ActivityBasicInfoSlideBinding

class BasicInfoSlideActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBasicInfoSlideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasicInfoSlideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = BasicInfoPagerAdapter(supportFragmentManager)
        binding.viewPager.setPageTransformer(true, DepthPageTransformer())


    }
}