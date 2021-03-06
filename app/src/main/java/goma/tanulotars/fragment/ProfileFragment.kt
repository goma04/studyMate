package goma.tanulotars.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import goma.tanulotars.ImageIdGetter
import goma.tanulotars.R
import goma.tanulotars.activity.EditProfileActivity
import goma.tanulotars.adapter.recyclerView.PostsAdapter
import goma.tanulotars.databinding.FragmentProfileBinding
import goma.tanulotars.firebase.FirebaseUtility
import goma.tanulotars.model.CurrentUser
import goma.tanulotars.model.Post
import goma.tanulotars.model.Relationship
import goma.tanulotars.model.User
import java.util.*


class ProfileFragment() : Fragment(), PostsAdapter.PostClickListener {
    private var user: User = User()
    private lateinit var binding: FragmentProfileBinding
    private lateinit var postsAdapter: PostsAdapter
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val gson = Gson()
        val userJson = requireArguments().getString("userJson")
        user = gson.fromJson(userJson, user::class.java)
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        val res = ImageIdGetter.getImageId(requireContext(), user.profilePictureId)

        user.profilePicture = BitmapFactory.decodeResource(resources, res)

        postsAdapter = PostsAdapter(requireContext(), this)
        binding.rvPosts.adapter = postsAdapter
        binding.rvPosts.layoutManager = LinearLayoutManager(view?.context).apply {
            reverseLayout = true
            stackFromEnd = true
        }

        binding.btnReport.setOnClickListener {
            db.collection("reports")
                .document(user.name + user.id)
                .set(HashMap<String, Objects>())

            Toast.makeText(
                requireContext(),
                "${user.name} jelentve!",
                Toast.LENGTH_SHORT
            ).show()
        }


        loadPosts()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setBindings()
    }


    private fun loadPosts() {

        postsAdapter.clearList()
        db.collection("posts")
            .whereEqualTo("userId", user.id)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    postsAdapter.addPost(document.toObject<Post>())
                }
            }
    }

    private fun setBindings() {

        binding.tvNameProfileFragment.text = user.name
        binding.ivProfilePictureProfileFragment.setImageBitmap(user.profilePicture)
        binding.tvDesc.text = user.introduction
        binding.tvFacebookProfileFragment.text = user.facebook
        binding.tvInstagramProfileFragment.text = user.instagram
        binding.tvOtherContactProfileFragment.text = user.otherContact

        if (user.facebook == "")
            binding.fbContainer.visibility = View.GONE

        if (user.instagram == "")
            binding.instaContainer.visibility = View.GONE

        if (user.otherContact == "")
            binding.otherContainer.visibility = View.GONE

        if (user.introduction == "")
            binding.introContainer.visibility = View.GONE

        var tvSubjectsText = ""
        for (subject in user.subjects) {
            tvSubjectsText += subject.name

            if (user.subjects.last() != subject)
                tvSubjectsText += ", "
        }

        binding.tvSubjectsProfileFragment.text = tvSubjectsText



        if (user.id == CurrentUser.user.id) {
            setCurrentUserProfile()
        } else {
            setOtherUserProfile()
        }

    }

    private fun setCurrentUserProfile() {
        binding.btnSendMessage.text = "Profil szerkeszt??se"
        binding.btnReport.visibility = View.GONE
        binding.btnSendMessage.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }
        binding.tvNamePost.text = "Posztjaim"
    }

    private fun setOtherUserProfile() {


        if (CurrentUser.user.friends.any { it.id == user.id }) {
            binding.btnSendMessage.visibility = View.GONE
            binding.tvStudyMateInfo.visibility = View.VISIBLE

            binding.tvStudyMateInfo.text = "${user.name} a tanul??t??rsad!"
        }

        binding.btnSendMessage.setOnClickListener {
            db.collection("relationships").add(Relationship(CurrentUser.user.id, user.id))

            CurrentUser.user.friendsId += user.id
            user.friendsId += CurrentUser.user.id


            FirebaseUtility.updateOrCreateUser(CurrentUser.user)
            FirebaseUtility.updateOrCreateUser(user)
            Toast.makeText(
                requireContext(),
                "${user.name} hozz??adva a tanul??t??rsakhoz!",
                Toast.LENGTH_SHORT
            ).show()
            binding.btnSendMessage.visibility = View.GONE
        }
    }


    override fun onPostClicked(post: Post) {
        if (user.id == CurrentUser.user.id) {
            AlertDialog.Builder(requireContext()) // set message, title, and icon
                .setTitle("T??rl??s")
                .setMessage("Biztos hogy t??rl??d a posztot?")
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setPositiveButton(
                    "Delete",
                    DialogInterface.OnClickListener { dialog, whichButton -> //your deleting code
                        dialog.dismiss()
                        db.collection("posts").document(post.uid.toString())
                            .delete()
                            .addOnSuccessListener {
                                Toast.makeText(
                                    requireContext(),
                                    "Poszt sikeresen t??r??lve!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                postsAdapter.remove(post)
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    requireContext(),
                                    "Sikertelen m??velet!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                    })
                .setNegativeButton("cancel",
                    DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
                .show()
        }
    }


}