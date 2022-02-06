package goma.tanulotars.activity

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import goma.tanulotars.databinding.ActivityLoginBinding
import goma.tanulotars.extension.validateNonEmpty
import goma.tanulotars.model.CurrentUser
import goma.tanulotars.model.User

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        if (auth.currentUser != null) {
            val user = auth.currentUser

            initUser(user)
            startActivity(Intent(this, MainActivity::class.java))

        }


        super.onCreate(savedInstanceState)
        // When running in debug mode, connect to the Firebase Emulator Suite.
        // "10.0.2.2" is a special IP address which allows the Android Emulator
        // to connect to "localhost" on the host computer. The port values (9xxx)
        // must match the values defined in the firebase.json file.
        /*if (BuildConfig.DEBUG) {
            Firebase.database.useEmulator("10.0.2.2", 9000)
            Firebase.auth.useEmulator("10.0.2.2", 9099)
            Firebase.storage.useEmulator("10.0.2.2", 8080)
        }*/

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)



        initRegisterButton()

        initLoginButton()
    }

    private fun initLoginButton() {

        binding.btnLogin.setOnClickListener {

            if (!(binding.etEmail.validateNonEmpty() && binding.etPassword.validateNonEmpty())) {
                Toast.makeText(this,"Ne hagyd üresen a mezőket!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()



            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEmail:success")

                        val user = auth.currentUser

                        initUser(user)
                        startActivity(Intent(this, MainActivity::class.java))

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Sikertelen bejelentkezés",
                            Toast.LENGTH_SHORT
                        ).show()
                        //TODO failed login ui
                    }
                }
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initUser(
        user: FirebaseUser?
    ) {
        val db = Firebase.firestore
        val docRef = db.collection("users").document(user!!.uid)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    CurrentUser.user.id = user.uid
                    CurrentUser.user = document.toObject<User>()!!
                    val res = getImageId(this, CurrentUser.user.profilePictureId.toString())
                    //TODO remove CurrentUser.user.profilePicture = resources.getDrawable(res, this.theme)
                    CurrentUser.user.profilePicture = BitmapFactory.decodeResource(resources, res)


                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }

    private fun initRegisterButton() {
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun getImageId(context: Context, imageName: String): Int {
        return context.resources
            .getIdentifier("drawable/$imageName", null, context.packageName)
    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        /* val currentUser = auth.currentUser
         if(currentUser != null){
             openInfoActivity()
         }*/
    }
}