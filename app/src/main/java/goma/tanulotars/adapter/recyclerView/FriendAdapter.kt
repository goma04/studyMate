package goma.tanulotars.adapter.recyclerView

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import goma.tanulotars.ImageIdGetter
import goma.tanulotars.databinding.FriendListItemBinding
import goma.tanulotars.model.User

class FriendAdapter(
    private val friendList: MutableList<User>,
    val context: Context,
    val friendClickListener: FriendClickListener
) :
    RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {



    interface FriendClickListener {
        fun onFriendClicked(friend: User)
    }

    inner class FriendViewHolder(val binding: FriendListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var friend: User? = null
        init{
            itemView.setOnClickListener{
                friendClickListener.onFriendClicked(friend!!)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FriendAdapter.FriendViewHolder = FriendViewHolder(
        FriendListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: FriendAdapter.FriendViewHolder, position: Int) {
        val friend = friendList[position]
        holder.friend = friend

        var tvSubjectsText = ""
        for (subject in friend.subjects) {
            tvSubjectsText += subject.name

            if (friend.subjects.last() != subject)
                tvSubjectsText += ", "
        }

        val res = ImageIdGetter.getImageId(context, friend.profilePictureId)
        holder.binding.ivProfilePictureFriendList.setImageDrawable(context.resources.getDrawable(res, context.theme))
        holder.binding.tvFriendName.text = friend.name
        holder.binding.tvFriendSubjects.text = tvSubjectsText

    }



    override fun getItemCount(): Int {
        return friendList.size
    }
}