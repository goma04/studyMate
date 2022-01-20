package goma.tanulotars.fragment.registerFragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import goma.tanulotars.activity.LoginActivity
import goma.tanulotars.activity.MainActivity
import goma.tanulotars.adapter.recyclerView.MessageAdapter.Companion.TAG
import goma.tanulotars.adapter.recyclerView.SubjectAdapter
import goma.tanulotars.databinding.FragmentSummaryBinding
import goma.tanulotars.model.CurrentUser
import goma.tanulotars.model.Subject
import kotlin.concurrent.thread

class SummaryFragment() : Fragment(), SubjectAdapter.SubjectClickListener {
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
            val currentFirebaseUser = Firebase.auth.currentUser

            val profileUpdate = userProfileChangeRequest {
                displayName = CurrentUser.user.name
                photoUri = Uri.parse("https://ibb.co/kDZbF1V")
            }

            CurrentUser.user.id = currentFirebaseUser!!.uid
            CurrentUser.user.email = currentFirebaseUser.email.toString()

            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result
                CurrentUser.user.token = token.toString()
            })

            val newUser = hashMapOf(
                "name" to CurrentUser.user.name,
                "introduction" to CurrentUser.user.introduction,
                "subjects" to CurrentUser.user.subjects,
                "profilePictureId" to CurrentUser.user.profilePictureId,
                "id" to CurrentUser.user.id
            )

            db.collection("users")
                .document(CurrentUser.user.id)
                .set(newUser)

            startActivity(Intent(context, MainActivity::class.java))
            thread {
                //TODO ez rettentő csúnya így
                Thread.sleep(200)
                LoginActivity.onUserLoadedListener?.onUserLoaded(CurrentUser.user)
            }


        }
        loadItems()
        return binding.root
    }

    private fun loadItems() {
        intermediateSubjectsAdapter.update(CurrentUser.user.subjects)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            binding.tvIntro.text = CurrentUser.user.introduction

            binding.tvName.text = CurrentUser.user.name
            binding.ivProfilePictureSummaryFragment.setImageBitmap(CurrentUser.user.profilePicture)
        }
    }

    override fun onSubjectAdded(subject: Subject) {

    }

    override fun onSubjectRemoved(subject: Subject) {

    }
}
