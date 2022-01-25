package goma.tanulotars.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import goma.tanulotars.activity.ChatActivity
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
        adapter = FriendAdapter(CurrentUser.user.friends, requireContext(),this)
        binding.rvFriends.adapter = adapter
        binding.rvFriends.layoutManager = LinearLayoutManager(view?.context)

        return binding.root
    }

    override fun onFriendClicked(friend: User) {
        val gson = Gson()
        val intent = Intent(context, ChatActivity()::class.java)

        intent.putExtra("userJson", gson.toJson(friend))
        startActivity(intent)

    }
}