package goma.tanulotars.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import goma.tanulotars.databinding.ActivityCreatePostBinding
import goma.tanulotars.extension.validateNonEmpty
import goma.tanulotars.model.CurrentUser
import goma.tanulotars.model.Post
import java.util.*

class CreatePostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePostBinding

    companion object {
        private const val REQUEST_CODE = 101
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.btnSend.setOnClickListener { sendClick() }

    }

    private fun sendClick() {
        if (!validateForm()) {
            return
        }

        uploadPost()
    }

    private fun validateForm() =
        binding.etTitle.validateNonEmpty() && binding.etBody.validateNonEmpty()

    private fun uploadPost() {
        val newPost = Post(
            UUID.randomUUID().toString(),
            CurrentUser.user.name,
            binding.etTitle.text.toString(),
            binding.etBody.text.toString(),
            CurrentUser.user.profilePictureId,
            CurrentUser.user.id
        )

        val db = Firebase.firestore


        db.collection("posts")
            .add(newPost)
            .addOnSuccessListener {
                Toast.makeText(this, "Sikeres!", Toast.LENGTH_SHORT).show()
                finish()
            }

    }


}