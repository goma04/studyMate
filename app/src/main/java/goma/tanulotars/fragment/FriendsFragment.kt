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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import goma.tanulotars.ImageIdGetter
import goma.tanulotars.activity.ProfileActivity
import goma.tanulotars.adapter.recyclerView.StudentAdapter
import goma.tanulotars.databinding.FragmentFriendsBinding
import goma.tanulotars.firebase.FirebaseUtility
import goma.tanulotars.model.CurrentUser
import goma.tanulotars.model.Relationship
import goma.tanulotars.model.User

class FriendsFragment : Fragment(), StudentAdapter.FriendClickListener {
    private lateinit var binding: FragmentFriendsBinding
    private lateinit var manager: LinearLayoutManager
    private lateinit var db: FirebaseDatabase
    private lateinit var adapter: StudentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendsBinding.inflate(layoutInflater, container, false)
        adapter = StudentAdapter(CurrentUser.user.friends, requireContext(), this)
        binding.rvFriends.adapter = adapter
        binding.rvFriends.layoutManager = LinearLayoutManager(view?.context)

        val db = Firebase.firestore

        //Itt minden megnyitáskor frissítem a Curentuser friendid listáját azért, mert lehet hogy közben valaki hozzáadta.
        //ezt majd úgy kell változtatni, hogy ha valaki addolta őt akkor frissüljön és így nem kell ezt
        val relationships = db.collection("relationships")
        relationships.get()
            .addOnSuccessListener { result ->
                updateFriendsIDFromDatabase(result)
                fetchFriends(db)
            }

        return binding.root
    }

    private fun updateFriendsIDFromDatabase(result: QuerySnapshot) {
        for (document in result) {
            val relationship = document.toObject<Relationship>()
            val id1 = relationship.userIdOne
            val id2 = relationship.userIdTwo

            if (id1 == CurrentUser.user.id && !CurrentUser.user.friendsId.contains(id2)){
                CurrentUser.user.friendsId += id2
                FirebaseUtility.updateOrCreateUser(CurrentUser.user)
            }


            if (id2 == CurrentUser.user.id && !CurrentUser.user.friendsId.contains(id1)){
                CurrentUser.user.friendsId += id1
                FirebaseUtility.updateOrCreateUser(CurrentUser.user)
            }
        }
    }

    private fun fetchFriends(db: FirebaseFirestore) {
        for (friendId in CurrentUser.user.friendsId) {
            val docrefFriend = db.collection("users").document(friendId)

            docrefFriend.get()
                .addOnSuccessListener { document ->
                    if (document != null) {

                        val user = document.toObject<User>()!!
                        adapter.addStudent(user)
                    }
                }
        }
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
