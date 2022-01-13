package goma.tanulotars.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import goma.tanulotars.adapter.recyclerView.SubjectAdapter
import goma.tanulotars.databinding.FragmentSummaryBinding
import goma.tanulotars.model.Student
import goma.tanulotars.model.Subject

class SummaryFragment(val student: Student) : Fragment(), SubjectAdapter.SubjectClickListener {
    private lateinit var binding: FragmentSummaryBinding
    private val subjectsAdapter = SubjectAdapter(this, mutableListOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentSummaryBinding.inflate(layoutInflater, container, false)
        binding.rvSubjectsSummaryFragment.adapter = subjectsAdapter
        binding.rvSubjectsSummaryFragment.layoutManager = LinearLayoutManager(view?.context)

        loadItems()
        return binding.root
    }

    private fun loadItems() {
        subjectsAdapter.update(student.subjects)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            binding.tvIntroSummaryFragment.text =  student.introduction

            binding.tvName.text = student.name
            binding.ivProfilePictureSummaryFragment.setImageDrawable(student.profilePicture)
        }
    }

    override fun onSubjectAdded(subject: Subject) {

    }

    override fun onSubjectRemoved(subject: Subject) {

    }
}
