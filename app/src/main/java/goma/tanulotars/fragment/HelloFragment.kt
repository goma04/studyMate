package goma.tanulotars.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import goma.tanulotars.databinding.FragmentHelloBinding


class HelloFragment() : Fragment() {

    private lateinit var binding: FragmentHelloBinding;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHelloBinding.inflate(layoutInflater, container, false)


        return binding.root
    }
}