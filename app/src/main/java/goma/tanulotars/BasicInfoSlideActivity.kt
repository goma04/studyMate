package goma.tanulotars

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import goma.tanulotars.adapter.BasicInfoPagerAdapter
import goma.tanulotars.adapter.viewPager.transform.DepthPageTransformer
import goma.tanulotars.databinding.ActivityBasicInfoSlideBinding
import goma.tanulotars.model.Student

class BasicInfoSlideActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBasicInfoSlideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasicInfoSlideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val student = Student()

        binding.viewPager.adapter = BasicInfoPagerAdapter(supportFragmentManager, student)
        binding.viewPager.setPageTransformer(true, DepthPageTransformer())



    }


}