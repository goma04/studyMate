package goma.tanulotars.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import goma.tanulotars.R
import goma.tanulotars.databinding.ActivityCreatePostBinding
import goma.tanulotars.extension.validateNonEmpty
import goma.tanulotars.model.CurrentUser
import goma.tanulotars.model.Post
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class CreatePostActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private var chosenSubjectName: String = "mindegy"
    private lateinit var binding: ActivityCreatePostBinding

    companion object {
        private const val REQUEST_CODE = 101
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.btnSend.setOnClickListener { sendClick() }

        initSpinner()

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
        val localDate = LocalDateTime.now()
        val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val dateString: String = localDate.format(formatter)

        val newPost = Post(
            UUID.randomUUID().toString(),
            CurrentUser.user.name,
            binding.etTitle.text.toString(),
            binding.etBody.text.toString(),
            CurrentUser.user.profilePictureId,
            CurrentUser.user.id,
            dateString,
            chosenSubjectName
        )

        val db = Firebase.firestore


        db.collection("posts")
            .document(newPost.uid!!)
            .set(newPost)
            .addOnSuccessListener {
                Toast.makeText(this, "Sikeres!", Toast.LENGTH_SHORT).show()
                finish()
            }

    }

    private fun initSpinner() {
        val spinner: Spinner = binding.spinnerSubject

        ArrayAdapter.createFromResource(
            this,
            R.array.spinner_subjects,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>, p1: View?, pos: Int, p3: Long) {
        chosenSubjectName = parent.getItemAtPosition(pos) as String
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }


}