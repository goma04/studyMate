package goma.tanulotars.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import goma.tanulotars.R
import goma.tanulotars.adapter.recyclerView.SubjectAdapter
import goma.tanulotars.databinding.FragmentSubjectBinding
import goma.tanulotars.model.Level
import goma.tanulotars.model.User
import goma.tanulotars.model.Subject

class SubjectFragment(val user: User) : Fragment(), SubjectAdapter.SubjectClickListener {
    private lateinit var binding: FragmentSubjectBinding

    private val intermediateAdapter = SubjectAdapter(this, user.subjects)
    private val advancedAdapter = SubjectAdapter(this, user.subjects)
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


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubjectBinding.inflate(layoutInflater, container, false)

        initRecyclerView()
        initButtons()

        loadItems()
        return binding.root
    }

    private fun initRecyclerView() {
        binding.rvIntermediate.adapter = intermediateAdapter
        binding.rvIntermediate.layoutManager = LinearLayoutManager(view?.context)
        binding.rvAdvanced.adapter = advancedAdapter
        binding.rvAdvanced.layoutManager = LinearLayoutManager(view?.context)
    }

    private fun initButtons() {
        binding.btAdvanced.setOnClickListener {
            binding.rvAdvanced.visibility = View.VISIBLE
            binding.rvIntermediate.visibility = View.INVISIBLE
            setActiveButton(binding.btAdvanced, binding.btIntermediate)

        }

        binding.btIntermediate.setOnClickListener {
            binding.rvIntermediate.visibility = View.VISIBLE
            binding.rvAdvanced.visibility = View.INVISIBLE
            setActiveButton(binding.btIntermediate, binding.btAdvanced)

        }
    }

    private fun setActiveButton(btnActive: Button, btnNotActive: Button) {
        btnActive.setBackgroundResource(R.color.darkGreen)
        btnNotActive.setBackgroundResource(R.color.buttonGrey)
        intermediate = !intermediate
    }




    private fun loadItems() {
        intermediateAdapter.update(subjects.filter { it.level == Level.INTERMEDIATE })
        advancedAdapter.update(subjects.filter { it.level == Level.ADVANCED })
    }



    override fun onSubjectAdded(subject: Subject) {
        user.subjects.add(subject)
    }

    override fun onSubjectRemoved(subject: Subject) {
        user.subjects.remove(subject)
    }
}