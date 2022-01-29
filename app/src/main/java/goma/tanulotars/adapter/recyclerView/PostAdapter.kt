package goma.tanulotars.adapter.recyclerView

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import goma.tanulotars.databinding.CardPostBinding
import goma.tanulotars.model.Post


class PostsAdapter(val context: Context, val postClickListener: PostClickListener) :
    ListAdapter<Post, PostsAdapter.PostViewHolder>(itemCallback) {

    private var postList: MutableList<Post> = mutableListOf()
    private var lastPosition = -1

    interface PostClickListener {
        fun onPostClicked(post: Post)

    }

    inner class PostViewHolder(val binding: CardPostBinding) : RecyclerView.ViewHolder(binding.root) {
        var post: Post? = null


        init{
            itemView.setOnClickListener{
                postClickListener.onPostClicked(post!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PostViewHolder(CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val tmpPost = postList[position]
        holder.post = tmpPost
        holder.binding.tvAuthor.text = tmpPost.author
        holder.binding.tvTitle.text = tmpPost.title
        holder.binding.tvBody.text = tmpPost.body


        val res = getImageId(context, tmpPost.profilePictureId.toString())

        holder.binding.ivProfilePicture.setImageDrawable(context.resources.getDrawable(res, context.theme))
    }

    fun addPost(post: Post?) {
        post ?: return

        postList += (post)
        submitList((postList))
    }

    fun removePost(post: Post?){
        post ?: return

        postList.remove(post)
        submitList((postList))
    }

    //TODO ezt ki lehetne szervezni valami extension methoddá
    private fun getImageId(context: Context, imageName: String): Int {
        return context.resources
            .getIdentifier("drawable/$imageName", null, context.packageName)
    }

    public fun clearList(){
        postList.clear()
    }

    companion object {
        object itemCallback : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.uid == newItem.uid
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }
        }
    }
}

