package goma.tanulotars.adapter.recyclerView

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import goma.tanulotars.R
import goma.tanulotars.databinding.SubjectListItemBinding
import goma.tanulotars.model.Subject

class SubjectAdapter(

    private val currentUserSubjects: MutableList<Subject>
) :
    RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>() {



    inner class SubjectViewHolder(val binding: SubjectListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var subject: Subject? = null
        val row_linearLayout = binding.subjectLinearLay
    }

    private fun makeHolderAppearance(
        holder: SubjectAdapter.SubjectViewHolder,
        textStyle: Int,
        backgroundStyle: Int
    ) {
        TextViewCompat.setTextAppearance(holder.binding.tvSubjectName, textStyle)
        holder.itemView.setBackgroundResource(backgroundStyle)
    }

    private val subjects = mutableListOf<Subject>()
    private var row_index = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SubjectViewHolder(
        SubjectListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: SubjectAdapter.SubjectViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val subject = subjects[position]

        holder.row_linearLayout.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                row_index = position
                if (!currentUserSubjects.remove(subject)) {
                    currentUserSubjects += subject
                }

                notifyDataSetChanged()
            }
        })

        holder.binding.tvSubjectName.text = subject.name

        if (currentUserSubjects.any{ it.id == subject.id }) {
            makeHolderAppearance(
                holder,
                R.style.ListItemText,
                R.drawable.custom_subject_list_background_selected)
        }else{
            makeHolderAppearance(
                holder,
                R.style.ListItemTextSelected,
                R.drawable.custom_subject_list_background)
        }

    }

    override fun getItemCount(): Int {
        return subjects.size
    }

    fun update(items: List<Subject>) {
        subjects.clear()
        subjects.addAll(items)

        notifyDataSetChanged()
    }
}