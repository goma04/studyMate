package goma.tanulotars.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import goma.tanulotars.adapter.recyclerView.SubjectAdapter
import goma.tanulotars.databinding.FragmentSubjectBinding
import goma.tanulotars.model.Level
import goma.tanulotars.model.Subject
import kotlin.concurrent.thread

class SubjectFragment : Fragment(), SubjectAdapter.SubjectClickListener {
    private lateinit var binding: FragmentSubjectBinding
    private val adapter = SubjectAdapter(this)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubjectBinding.inflate(layoutInflater, container, false)
        binding.rvSubjects.adapter = adapter
        binding.rvSubjects.layoutManager = LinearLayoutManager(view?.context)


        loadItemsInBackground()

        return binding.root
    }

    private fun loadItemsInBackground() {
        val subjects = listOf(
            Subject(1, "Magyar", Level.INTERMEDIATE),
            Subject(2, "Matek", Level.INTERMEDIATE),
            Subject(3, "Irodalom", Level.INTERMEDIATE),
            Subject(3, "Irodalom", Level.INTERMEDIATE),
            Subject(3, "Irodalom", Level.INTERMEDIATE),
            Subject(3, "Irodalom", Level.INTERMEDIATE),
            Subject(3, "Irodalom", Level.INTERMEDIATE),
            Subject(3, "Irodalom", Level.INTERMEDIATE),
            Subject(3, "Irodalom", Level.INTERMEDIATE),
            Subject(3, "Irodalom", Level.INTERMEDIATE),
            Subject(3, "Irodalom", Level.INTERMEDIATE),

        )

        thread {

            adapter.update(subjects)
        }
    }

    override fun onSubjectClick(subject: Subject) {
       Log.d("szia", "szia")
    }
}