package goma.tanulotars.fragment.registerFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import goma.tanulotars.activity.MainActivity
import goma.tanulotars.adapter.recyclerView.SubjectAdapter
import goma.tanulotars.databinding.FragmentSummaryBinding
import goma.tanulotars.firebase.FirebaseUtility
import goma.tanulotars.model.CurrentUser
import kotlin.concurrent.thread

class SummaryFragment() : Fragment() {
    private lateinit var binding: FragmentSummaryBinding
    private val intermediateSubjectsAdapter = SubjectAdapter( mutableListOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSummaryBinding.inflate(layoutInflater, container, false)
        binding.rvSubjectsSummaryFragment.adapter = intermediateSubjectsAdapter
        binding.rvSubjectsSummaryFragment.layoutManager = LinearLayoutManager(view?.context)
        binding.btnStart.setOnClickListener {

            //Todo

            /* FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                 if (!task.isSuccessful) {
                     Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                     return@OnCompleteListener
                 }

                 // Get new FCM registration token
                 val token = task.result
                 CurrentUser.user.token = token.toString()
             })*/

            val currentFirebaseUser = Firebase.auth.currentUser
            CurrentUser.user.id = currentFirebaseUser!!.uid
            CurrentUser.user.email = currentFirebaseUser.email.toString()
            FirebaseUtility.updateOrCreateUser(CurrentUser.user)

            startActivity(Intent(context, MainActivity::class.java))
            thread {
                //TODO ez rettentő csúnya így
                Thread.sleep(200)
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


}
