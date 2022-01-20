package goma.tanulotars.fragment.registerFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import goma.tanulotars.databinding.FragmentNameBinding
import goma.tanulotars.model.CurrentUser

class NameFragment() : Fragment() {
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
        CurrentUser.user.name =  binding.editTextTextPersonName.text.toString()
    }


}