package goma.tanulotars.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import goma.tanulotars.databinding.FragmentProfilePictureBinding


class ProfilePictureFragment: Fragment() {
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

    private fun getImageId(context: Context, imageName: String): Int {
        return context.resources
            .getIdentifier("drawable/$imageName", null, context.packageName)
    }

    private fun changeProfilePicture(imageRes: String) {



        val res = getImageId(view!!.context, imageRes)
        binding.ivProfilePicture.setBackgroundResource(res)
      //  binding.ivProfilePicture.setBackgroundResource(R.drawable.profilepicture1)
    }
}