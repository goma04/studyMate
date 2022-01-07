package goma.tanulotars.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import goma.tanulotars.databinding.FragmentIntroductionBinding

class IntroductionFragment: Fragment() {
    private lateinit var binding: FragmentIntroductionBinding;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntroductionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}