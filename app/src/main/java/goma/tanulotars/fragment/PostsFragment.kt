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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import goma.tanulotars.ImageIdGetter
import goma.tanulotars.R
import goma.tanulotars.activity.CreatePostActivity
import goma.tanulotars.activity.ProfileActivity
import goma.tanulotars.adapter.recyclerView.PostsAdapter
import goma.tanulotars.adapter.recyclerView.StudentAdapter
import goma.tanulotars.databinding.FragmentPostsBinding
import goma.tanulotars.model.Post
import goma.tanulotars.model.User

class PostsFragment : Fragment(), PostsAdapter.PostClickListener,
    AdapterView.OnItemSelectedListener,
    StudentAdapter.FriendClickListener {
    private lateinit var binding: FragmentPostsBinding
    private lateinit var postsAdapter: PostsAdapter
    private lateinit var studentsAdapter: StudentAdapter
    private val db = Firebase.firestore

    private val allStudent = mutableListOf<User>()
    private val allPosts = mutableListOf<Post>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostsBinding.inflate(layoutInflater, container, false)
        postsAdapter = PostsAdapter(requireContext(), this)
        studentsAdapter = StudentAdapter(mutableListOf(), requireContext(), this)

        binding.rvPosts.adapter = postsAdapter
        binding.rvPosts.layoutManager = LinearLayoutManager(view?.context).apply {
            reverseLayout = true
            stackFromEnd = true
        }


        binding.rvPeople.adapter = studentsAdapter
        binding.rvPeople.layoutManager = LinearLayoutManager(view?.context)


        binding.floatingActionButton.setOnClickListener {
            val createPostIntent = Intent(context, CreatePostActivity::class.java)
            startActivity(createPostIntent)
        }

        initSpinner()
        initStudentRW()
        initPostsListener()
        initButtons()
        return binding.root
    }

    private fun initSpinner() {
        val spinner: Spinner = binding.spinner

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_subjects,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = this
    }


    private fun initPostsListener() {
        postsAdapter.clearList()

        db.collection("posts")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }


                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            allPosts.add(dc.document.toObject())
                            postsAdapter.addPost(dc.document.toObject())
                            binding.rvPosts.scrollToPosition(postsAdapter.itemCount - 1)
                        }
                        DocumentChange.Type.MODIFIED -> {
                            val updatedPost = dc.document.toObject<Post>()
                            allPosts[allPosts.indexOf(allPosts.find { it.uid == updatedPost.uid })] =
                                updatedPost
                            postsAdapter.update(dc.document.toObject<Post>())
                        }
                        DocumentChange.Type.REMOVED -> {
                            allPosts.remove(dc.document.toObject())
                            postsAdapter.removePost(dc.document.toObject())
                        }

                    }
                }
            }
    }

    private fun initStudentRW() {

        db.collection("users")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            allStudent.add(dc.document.toObject())

                        }
                        DocumentChange.Type.MODIFIED -> {
                            val updatedUser = dc.document.toObject<User>()
                            allStudent[allStudent.indexOf(allStudent.find { it.id == updatedUser.id })] =
                                updatedUser
                        }
                        DocumentChange.Type.REMOVED -> {
                            allStudent.remove(dc.document.toObject())
                        }

                    }
                }

                studentsAdapter.update(allStudent)
            }
    }

    override fun onResume() {
        super.onResume()
        studentsAdapter.update(allStudent)
        postsAdapter.update(allPosts)
        binding.rvPosts.scrollToPosition(postsAdapter.itemCount - 1)
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        val name = parent.getItemAtPosition(pos) as String

        if (name == "mindegy") {
            studentsAdapter.update(allStudent)
            postsAdapter.update(allPosts)
        } else {
            studentsAdapter.update(allStudent.filter { it.subjects.any { it.name == name } })
            postsAdapter.update(allPosts.filter { it.subject == name })
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    private fun initButtons() {
        binding.btPeople.setOnClickListener {
            binding.rvPeople.visibility = View.VISIBLE
            binding.rvPosts.visibility = View.GONE
            setActiveButton(binding.btPeople, binding.btPost)
        }

        binding.btPost.setOnClickListener {
            binding.rvPosts.visibility = View.VISIBLE
            binding.rvPeople.visibility = View.GONE
            setActiveButton(binding.btPost, binding.btPeople)
        }
    }

    private fun setActiveButton(btnActive: Button, btnNotActive: Button) {
        btnActive.setBackgroundResource(R.color.lightGreen)
        btnNotActive.setBackgroundResource(R.color.SlateGray)
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


