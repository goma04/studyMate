package goma.tanulotars.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import goma.tanulotars.databinding.ActivityHelpBinding

class HelpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHelpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHelpBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}