package goma.tanulotars.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import goma.tanulotars.adapter.BasicInfoPagerAdapter
import goma.tanulotars.adapter.viewPager.transform.DepthPageTransformer
import goma.tanulotars.databinding.ActivityBasicInfoSlideBinding
import goma.tanulotars.model.User

class BasicInfoSlideActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBasicInfoSlideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasicInfoSlideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val student = User()
        student.introduction = " Sziasztok!ondoltam megosztom veletek a saját tételeim! Kommentbe írjátok le a számát amire szükségetek Sziasztok!ondoltam megosztom veletek a saját tételeim! Kommentbe írjátok le a számát amire szükségetek van és dobjatok hozzá egy Sziasztok!ondoltam megosztom veletek a saját tételeim! Kommentbe írjátok le a számát amire szükségetek van és dobjatok hozzá egy Sziasztok!ondoltam megosztom veletek a saját tételeim! Kommentbe írjátok le a számát amire szükségetek van és dobjatok hozzá egy van és dobjatok hozzá egy e-mail címet is!Sziasztok!  Gondoltam megosztom veletek a saját tételeim! Kommentbe írjátok le a számát amire szükségetek van és dobjatok hozzá egy e-mail címet is!"

        binding.viewPager.adapter = BasicInfoPagerAdapter(supportFragmentManager, student)
        binding.viewPager.setPageTransformer(true, DepthPageTransformer())



    }


}