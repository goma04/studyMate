package goma.tanulotars.adapter.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import goma.tanulotars.databinding.SubjectListItemBinding
import goma.tanulotars.model.Subject

class SubjectAdapter(val subjectClickListener: SubjectClickListener): RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>() {

    interface SubjectClickListener{
        fun onSubjectClick(subject: Subject)
    }

    inner class SubjectViewHolder( val binding: SubjectListItemBinding): RecyclerView.ViewHolder(binding.root){
        var subject: Subject? = null

        init {
            itemView.setOnClickListener{
                subject?.let{subject ->
                    subjectClickListener.onSubjectClick(subject)
                }
            }
        }
    }

    private val subjects = mutableListOf<Subject>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SubjectViewHolder (
        SubjectListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: SubjectAdapter.SubjectViewHolder, position: Int) {
       val subject = subjects[position]

        holder.binding.tvSubjectName.text = subject.name
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