package goma.tanulotars.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import goma.tanulotars.adapter.recyclerView.SubjectAdapter
import goma.tanulotars.databinding.FragmentSummaryBinding
import goma.tanulotars.model.Subject
import goma.tanulotars.model.User

class SummaryFragment(val user: User) : Fragment(), SubjectAdapter.SubjectClickListener {
    private lateinit var binding: FragmentSummaryBinding
    private val intermediateSubjectsAdapter = SubjectAdapter(this, mutableListOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSummaryBinding.inflate(layoutInflater, container, false)
        binding.rvSubjectsSummaryFragment.adapter = intermediateSubjectsAdapter
        binding.rvSubjectsSummaryFragment.layoutManager = LinearLayoutManager(view?.context)
        binding.btnStart.setOnClickListener {
            val db = Firebase.firestore
            val currentUser = Firebase.auth.currentUser

            val profileUpdate = userProfileChangeRequest {
                displayName = user.name
                photoUri = Uri.parse("https://ibb.co/kDZbF1V")
            }

            user.id = currentUser!!.uid

            val newUser = hashMapOf(
                "name" to user.name,
                "subjects" to user.subjects
            )

            db.collection("users")
                .document(user.id)
                .set(newUser)
        }
        loadItems()
        return binding.root
    }

    private fun loadItems() {
        intermediateSubjectsAdapter.update(user.subjects)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            binding.tvIntro.text = user.introduction

            binding.tvName.text = user.name
            binding.ivProfilePictureSummaryFragment.setImageDrawable(user.profilePicture)
        }
    }

    override fun onSubjectAdded(subject: Subject) {

    }

    override fun onSubjectRemoved(subject: Subject) {

    }
}
