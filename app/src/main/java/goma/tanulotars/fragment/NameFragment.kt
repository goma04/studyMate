package goma.tanulotars.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import goma.tanulotars.databinding.FragmentNameBinding
import goma.tanulotars.model.User

class NameFragment(val user: User) : Fragment() {
    private lateinit var binding: FragmentNameBinding;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNameBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        user.name = binding.editTextTextPersonName.text.toString()
    }


}