package goma.tanulotars.fragment.registerFragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import goma.tanulotars.databinding.FragmentProfilePictureBinding
import goma.tanulotars.model.CurrentUser


class ProfilePictureFragment() : Fragment() {
    private lateinit var binding: FragmentProfilePictureBinding;
    private lateinit var imageViews: List<ImageView>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfilePictureBinding.inflate(layoutInflater, container, false)
        imageViews = listOf<ImageView>(
            binding.ivProfilePictureAvatar1,
            binding.ivProfilePictureAvatar2,
            binding.ivProfilePictureAvatar3,
            binding.ivProfilePictureAvatar4
        )

        for (i in imageViews) {
            i.visibility = View.INVISIBLE
        }

        binding.ivProfilePictureAvatar1.visibility = View.VISIBLE

        binding.imageButton1.setOnClickListener {
            changeProfilePicture(
                binding.ivProfilePictureAvatar1,
                binding.imageButton1.tag.toString()
            )
        }
        binding.imageButton2.setOnClickListener {
            changeProfilePicture(
                binding.ivProfilePictureAvatar2,
                binding.imageButton2.tag.toString()
            )
        }
        binding.imageButton3.setOnClickListener {
            changeProfilePicture(
                binding.ivProfilePictureAvatar3,
                binding.imageButton3.tag.toString()
            )
        }
        binding.imageButton4.setOnClickListener {
            changeProfilePicture(
                binding.ivProfilePictureAvatar4,
                binding.imageButton4.tag.toString()
            )
        }

        return binding.root
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun changeProfilePicture(iv: ImageView, photoId: String) {
        CurrentUser.user.profilePictureId = photoId
        val res = getImageId(requireView().context, photoId)
        binding.ivProfilePictureAvatar1.setBackgroundResource(res)
        //CurrentUser.user.profilePicture = resources.getDrawable(res, requireContext().theme)
        CurrentUser.user.profilePicture = BitmapFactory.decodeResource(resources, res)
    }

    private fun getImageId(context: Context, imageName: String): Int {
        return context.resources
            .getIdentifier("drawable/$imageName", null, context.packageName)
    }


}