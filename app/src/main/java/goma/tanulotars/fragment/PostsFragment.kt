package goma.tanulotars.fragment

import android.annotation.SuppressLint
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
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import goma.tanulotars.ImageIdGetter
import goma.tanulotars.activity.CreatePostActivity
import goma.tanulotars.activity.ProfileActivity
import goma.tanulotars.adapter.recyclerView.PostsAdapter
import goma.tanulotars.databinding.FragmentPostsBinding
import goma.tanulotars.model.Post
import goma.tanulotars.model.User

class PostsFragment : Fragment(), PostsAdapter.PostClickListener {
    private lateinit var binding: FragmentPostsBinding
    private lateinit var postsAdapter: PostsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostsBinding.inflate(layoutInflater, container, false)
        postsAdapter = PostsAdapter(requireContext(), this)
        binding.rvPosts.adapter = postsAdapter
        binding.rvPosts.layoutManager = LinearLayoutManager(view?.context).apply {
            reverseLayout = true
            stackFromEnd = true
        }
        binding.floatingActionButton.setOnClickListener {
            val createPostIntent = Intent(context, CreatePostActivity::class.java)
            startActivity(createPostIntent)
        }

        initPostsListener()
        return binding.root
    }

    private fun initPostsListener() {
        val db = Firebase.firestore
        db.collection("posts")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> postsAdapter.addPost(dc.document.toObject<Post>())
                        DocumentChange.Type.MODIFIED -> {}//TODO
                        DocumentChange.Type.REMOVED
                        -> postsAdapter.removePost(dc.document.toObject<Post>())
                    }
                }
            }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onPostClicked(post: Post) {
        val db = Firebase.firestore
        val docRef = db.collection("users").document(post.userId.toString())

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")
                    val user = document.toObject<User>()!!

                    val res = ImageIdGetter.getImageId(requireContext(), user.profilePictureId)
                    user.profilePicture  = BitmapFactory.decodeResource(resources, res)

                    val gson = Gson()
                    val intent = Intent(context,ProfileActivity()::class.java)

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


