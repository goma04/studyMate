package goma.tanulotars.fragment

import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import goma.tanulotars.ImageIdGetter
import goma.tanulotars.activity.ProfileActivity
import goma.tanulotars.adapter.recyclerView.FriendAdapter
import goma.tanulotars.databinding.FragmentFriendsBinding
import goma.tanulotars.model.CurrentUser
import goma.tanulotars.model.User

class FriendsFragment : Fragment(), FriendAdapter.FriendClickListener {
    private lateinit var binding: FragmentFriendsBinding
    private lateinit var manager: LinearLayoutManager
    private lateinit var db: FirebaseDatabase
    private lateinit var adapter: FriendAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendsBinding.inflate(layoutInflater, container, false)
        adapter = FriendAdapter(CurrentUser.user.friends, requireContext(), this)
        binding.rvFriends.adapter = adapter
        binding.rvFriends.layoutManager = LinearLayoutManager(view?.context)

        return binding.root
    }

    override fun onFriendClicked(friend: User) {
        val db = Firebase.firestore
        val docRef = db.collection("users").document(friend.id)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")
                    val user = document.toObject<User>()!!

                    val res = ImageIdGetter.getImageId(requireContext(), user.profilePictureId)
                    user.profilePicture = BitmapFactory.decodeResource(resources, res)

                    val gson = Gson()
                    val intent = Intent(context, ProfileActivity()::class.java)

                    intent.putExtra("userJson", gson.toJson(user))
                    startActivity(intent)
                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }
    }

}
