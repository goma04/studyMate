package goma.tanulotars.adapter.recyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import goma.tanulotars.R
import goma.tanulotars.databinding.SubjectListItemBinding
import goma.tanulotars.model.Subject

class SubjectAdapter(
    val subjectClickListener: SubjectClickListener,
    private val selectedSubjects: MutableList<Subject>
) :
    RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>() {

    interface SubjectClickListener {
        fun onSubjectAdded(subject: Subject)
        fun onSubjectRemoved(subject: Subject)
    }

    inner class SubjectViewHolder(val binding: SubjectListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var subject: Subject? = null
        var selected: Boolean? = null

        init {
            itemView.setOnClickListener {
                subject?.let { subject ->
                    if (!selected!!) {
                        subjectClickListener.onSubjectAdded(subject)
                        makeHolderAppearance(
                            this,
                            R.style.ListItemTextSelected,
                            R.drawable.custom_subject_list_background_selected
                        )

                    } else {
                        subjectClickListener.onSubjectRemoved(subject)
                        makeHolderAppearance(
                            this,
                            R.style.ListItemText,
                            R.drawable.custom_subject_list_background
                        )
                    }
                    selected = !selected!!
                }
            }
        }
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SubjectViewHolder(
        SubjectListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: SubjectAdapter.SubjectViewHolder, position: Int) {
        val subject = subjects[position]


        holder.subject = subject

        holder.binding.tvSubjectName.text = subject.name
        holder.selected = false

        if (selectedSubjects.contains(subject)) {
            Log.d("selected", "current: $subject | $selectedSubjects")
            holder.selected = true
            makeHolderAppearance(
                holder,
                R.style.ListItemTextSelected,
                R.drawable.custom_subject_list_background_selected
            )
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