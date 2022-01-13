package goma.tanulotars.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import goma.tanulotars.R
import goma.tanulotars.databinding.FragmentProfilePictureBinding
import goma.tanulotars.model.Student


class ProfilePictureFragment(val student: Student): Fragment() {
    private lateinit var binding: FragmentProfilePictureBinding;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfilePictureBinding.inflate(layoutInflater, container, false)
        binding.imageButton1.setOnClickListener{
            changeProfilePicture(binding.imageButton1.tag as String)
        }
        binding.imageButton2.setOnClickListener{
            changeProfilePicture(binding.imageButton2.tag as String)
        }
        binding.imageButton3.setOnClickListener{
            changeProfilePicture(binding.imageButton3.tag as String)
        }
        binding.imageButton4.setOnClickListener{
            changeProfilePicture(binding.imageButton4.tag as String)
        }

        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onResume() {
        super.onResume()
        student.profilePicture = resources.getDrawable(R.drawable.avatar1, context!!.theme)
    }

    private fun getImageId(context: Context, imageName: String): Int {
        return context.resources
            .getIdentifier("drawable/$imageName", null, context.packageName)
    }

    private fun changeProfilePicture(imageRes: String) {
        val res = getImageId(view!!.context, imageRes)
        binding.ivProfilePicture.setBackgroundResource(res)



        student.profilePicture = resources.getDrawable(res, context!!.theme)
    }
}