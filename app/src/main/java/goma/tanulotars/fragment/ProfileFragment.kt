package goma.tanulotars.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import goma.tanulotars.databinding.FragmentProfileBinding
import goma.tanulotars.firebase.FirebaseUtility
import goma.tanulotars.model.CurrentUser
import goma.tanulotars.model.User

class ProfileFragment() : Fragment() {
    private var user: User = User()
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val gson = Gson()
        val userJson = requireArguments().getString("userJson")
        user = gson.fromJson(userJson, user::class.java)
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        setBindings()



        var tvSubjectsText = ""
        for (subject in user.subjects) {
            tvSubjectsText += subject.name

            if (user.subjects.last() != subject)
                tvSubjectsText += ", "
        }


        binding.tvSubjectsProfileFragment.text = tvSubjectsText

        return binding.root
    }

    private fun setBindings() {

        binding.tvNameProfileFragment.text = user.name
        binding.ivProfilePictureProfileFragment.setImageBitmap(user.profilePicture)
        binding.tvDesc.text = user.introduction
        binding.tvMatesNumberProfileFragment.text = user.friends.size.toString()
        binding.tvSubjectCountProfileFragment.text = user.subjects.size.toString()

        if(user.id != CurrentUser.user.id){
            binding.btnSendMessage.setOnClickListener {
                user
                CurrentUser.user.friends += user
                FirebaseUtility.updateOrCreateUser(CurrentUser.user)
            }
        }else{
            binding.btnSendMessage.visibility = View.GONE
        }

    }

    override fun onResume() {
        super.onResume()
        setBindings()
    }
}