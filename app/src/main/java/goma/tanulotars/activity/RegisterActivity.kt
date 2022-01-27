package goma.tanulotars.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import goma.tanulotars.R
import goma.tanulotars.adapter.recyclerView.SubjectAdapter
import goma.tanulotars.databinding.ActivityRegisterBinding
import goma.tanulotars.model.CurrentUser
import goma.tanulotars.model.Level
import goma.tanulotars.model.Subject

class RegisterActivity : AppCompatActivity(), SubjectAdapter.SubjectClickListener {
    private lateinit var binding: ActivityRegisterBinding
    private val intermediateAdapter = SubjectAdapter(this, CurrentUser.user.subjects)
    private val advancedAdapter = SubjectAdapter(this, CurrentUser.user.subjects)
    private var intermediate = true
    private val subjects = listOf(
        Subject(1, "Magyar", Level.INTERMEDIATE),
        Subject(2, "Matek", Level.INTERMEDIATE),
        Subject(3, "Történelem", Level.INTERMEDIATE),
        Subject(4, "Fizika", Level.INTERMEDIATE),
        Subject(5, "Angol", Level.INTERMEDIATE),
        Subject(6, "Német", Level.INTERMEDIATE),
        Subject(7, "Informatika", Level.INTERMEDIATE),
        Subject(8, "Etika", Level.INTERMEDIATE),
        Subject(9, "Földrajz", Level.INTERMEDIATE),
        Subject(10, "Informatika", Level.INTERMEDIATE),
        Subject(11, "Földrajz", Level.INTERMEDIATE),
        Subject(23, "Fizika", Level.INTERMEDIATE),
        Subject(33, "Történelem", Level.ADVANCED),
        Subject(34, "Matek", Level.ADVANCED),
        Subject(35, "Magyar", Level.ADVANCED),

        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()


        loadItems()

    }

    override fun onSubjectAdded(subject: Subject) {
        TODO("Not yet implemented")
    }

    override fun onSubjectRemoved(subject: Subject) {
        TODO("Not yet implemented")
    }

    private fun initRecyclerView() {
        binding.rvIntermediate.adapter = intermediateAdapter
        binding.rvIntermediate.layoutManager = LinearLayoutManager(this)

    }

    private fun loadItems() {
        intermediateAdapter.update(subjects.filter { it.level == Level.INTERMEDIATE })
        advancedAdapter.update(subjects.filter { it.level == Level.ADVANCED })
    }
}