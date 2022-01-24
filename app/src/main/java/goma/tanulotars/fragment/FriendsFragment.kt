package goma.tanulotars.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import goma.tanulotars.adapter.recyclerView.FriendAdapter
import goma.tanulotars.databinding.FragmentFriendsBinding
import goma.tanulotars.model.CurrentUser
import goma.tanulotars.model.User

class FriendsFragment: Fragment(), FriendAdapter.FriendClickListener {
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

        val friends = mutableListOf<User>()
        val db = Firebase.firestore
        val docRef = db.collection("users").document(CurrentUser.user.id).collection("friends")

        adapter = FriendAdapter(CurrentUser.user.friends, requireContext(),this)
        binding.rvFriends.adapter = adapter
        binding.rvFriends.layoutManager = LinearLayoutManager(view?.context)

        return binding.root
    }

    override fun onFriendClicked(friend: User) {

    }
}