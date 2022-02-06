package goma.tanulotars.adapter.recyclerView

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import goma.tanulotars.ImageIdGetter
import goma.tanulotars.databinding.FriendListItemBinding
import goma.tanulotars.model.User

class StudentAdapter(
    private val studentList: MutableList<User>,
    val context: Context,
    val studentClickListener: FriendClickListener
) :
    RecyclerView.Adapter<StudentAdapter.FriendViewHolder>() {


    interface FriendClickListener {
        fun onFriendClicked(friend: User)
    }

    inner class FriendViewHolder(val binding: FriendListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var friend: User? = null

        init {
            itemView.setOnClickListener {
                studentClickListener.onFriendClicked(friend!!)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StudentAdapter.FriendViewHolder = FriendViewHolder(
        FriendListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: StudentAdapter.FriendViewHolder, position: Int) {
        val friend = studentList[position]
        holder.friend = friend

        var tvSubjectsText = ""
        for (subject in friend.subjects) {
            tvSubjectsText += subject.name

            if (friend.subjects.last() != subject)
                tvSubjectsText += ", "
        }

        val res = ImageIdGetter.getImageId(context, friend.profilePictureId)
        holder.binding.ivProfilePictureFriendList.setImageDrawable(
            context.resources.getDrawable(
                res,
                context.theme
            )
        )
        holder.binding.tvFriendName.text = friend.name
        holder.binding.tvFriendSubjects.text = tvSubjectsText

    }


    override fun getItemCount(): Int {
        return studentList.size
    }

    fun addStudent(student: User?) {
        student ?: return

        studentList.add(student)

        notifyDataSetChanged()


    }

    fun update(student: User?) {
        student ?: return

        studentList[studentList.indexOf(studentList.find{it.id == student.id})] = student
        notifyDataSetChanged()
    }

    fun update(students: List<User>?) {
        students ?: return

        studentList.clear()
        studentList.addAll(students)
        notifyDataSetChanged()
    }

    fun removeStudent(student: User?) {
        student ?: return

        studentList.remove(student)
    }
}