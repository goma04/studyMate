package goma.tanulotars.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import goma.tanulotars.databinding.ActivityForgottenPasswordBinding

class ForgottenPassword : AppCompatActivity() {
    private lateinit var binding: ActivityForgottenPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgottenPasswordBinding.inflate(layoutInflater)


        setContentView(binding.root)

        binding.btnSend.setOnClickListener {
            val emailAddress = binding.etEmail.text.toString()

            Firebase.auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                        Toast.makeText(this, "Email elküldve", Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(this, "Sikertelen művelet", Toast.LENGTH_SHORT).show()
                }

            finish()
        }
    }
}