package goma.tanulotars.activity

import android.content.ContentValues
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import goma.tanulotars.ImageIdGetter
import goma.tanulotars.R
import goma.tanulotars.adapter.recyclerView.MessageAdapter.Companion.TAG
import goma.tanulotars.adapter.recyclerView.SubjectAdapter
import goma.tanulotars.databinding.ActivityRegisterBinding
import goma.tanulotars.firebase.FirebaseUtility
import goma.tanulotars.model.CurrentUser
import goma.tanulotars.model.Level
import goma.tanulotars.model.Subject


class RegisterActivity : AppCompatActivity(), android.text.TextWatcher {
    private lateinit var binding: ActivityRegisterBinding
    private val subjectAdapter = SubjectAdapter(CurrentUser.user.subjects)

    private var currentlySelectedProfilePicture: ImageView? = null
    private val subjects = listOf(
        Subject(1, "matematika", Level.INTERMEDIATE),
        Subject(42, "magyar nyelv és irodalom", Level.INTERMEDIATE),
        Subject(2, "történelem", Level.INTERMEDIATE),
        Subject(3, "angol nyelv", Level.INTERMEDIATE),
        Subject(4, "német nyelv", Level.INTERMEDIATE),
        Subject(5, "francia nyelv", Level.INTERMEDIATE),
        Subject(6, "spanyol nyelv", Level.INTERMEDIATE),
        Subject(7, "olasz nyelv", Level.INTERMEDIATE),
        Subject(8, "orosz nyelv", Level.INTERMEDIATE),
        Subject(41, "japán nyelv", Level.INTERMEDIATE),
        Subject(9, "kémia", Level.INTERMEDIATE),
        Subject(10, "földrajz", Level.INTERMEDIATE),
        Subject(11, "biológia", Level.INTERMEDIATE),
        Subject(12, "informatika", Level.INTERMEDIATE),
        Subject(13, "ének-zene", Level.INTERMEDIATE),
        Subject(14, "belügyi rendészeti ismeretek", Level.INTERMEDIATE),
        Subject(15, "fizika", Level.INTERMEDIATE),
        Subject(16, "automatikai és elektronikai ismeretek", Level.INTERMEDIATE),
        Subject(17, "egészségügyi ismeretek", Level.INTERMEDIATE),
        Subject(18, "épületgépészeti ismeretek", Level.INTERMEDIATE),
        Subject(19, "faipari ismeretek", Level.INTERMEDIATE),
        Subject(20, "gazdasági ismeretek", Level.INTERMEDIATE),
        Subject(21, "mozgóképkultúra és médiaismeret", Level.INTERMEDIATE),
        Subject(22, "társadalomismeret", Level.INTERMEDIATE),
        Subject(23, "turizmus", Level.INTERMEDIATE),
        Subject(24, "vizuális kultúra", Level.INTERMEDIATE),
        Subject(25, "gépgyártás-technológiai ismeretek", Level.INTERMEDIATE),
        Subject(26, "ember- és társadalomismeret, etika", Level.INTERMEDIATE),
        Subject(27, "irodai ügyviteli ismeretek", Level.INTERMEDIATE),
        Subject(28, "kereskedelmi ismeretek", Level.INTERMEDIATE),
        Subject(29, "közgazdasági ismeretek", Level.INTERMEDIATE),
        Subject(30, "vegyész ismeretek", Level.ADVANCED),
        Subject(31, "vendéglátóipari ismeretek", Level.ADVANCED),
        Subject(32, "művészettörténet", Level.ADVANCED),
        Subject(33, "filozófia", Level.ADVANCED),
        Subject(34, "dráma", Level.ADVANCED),
        Subject(35, "pszichológia", Level.ADVANCED),
        Subject(36, "ágazati informatika", Level.ADVANCED),
        Subject(37, "pedagógiai ismeretek", Level.ADVANCED),
        Subject(38, "rajz", Level.ADVANCED),
        Subject(39, "hittan", Level.ADVANCED),
        Subject(40, "pszichológia", Level.ADVANCED),

        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etEmail.addTextChangedListener(this)
        binding.etPassword.addTextChangedListener(this)

        binding.btnStart.setOnClickListener {

            if (!checkValidForm()) {
                binding.tvExplain.setTextColor(Color.parseColor("#FF0000"))
                return@setOnClickListener
            }

            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            val auth = Firebase.auth
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(ContentValues.TAG, "createUserWithEmail:success")
                        setUserTextData()
                        setUserProfilePicture()

                        FirebaseUtility.updateOrCreateUser(CurrentUser.user)

                        auth.currentUser?.sendEmailVerification()

                        showDialog("Sikeres regisztráció! Elküldtünk egy emailt a megadott címre.")

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)

                        Toast.makeText(this, "Sikertelen regisztráció", Toast.LENGTH_SHORT).show()

                        try {
                            throw task.exception!!
                        } catch (e: FirebaseAuthWeakPasswordException) {
                            binding.passwordContainer.setError(getString(R.string.error_weak_password))
                            binding.passwordContainer.requestFocus()
                        }
                        catch (e: FirebaseAuthUserCollisionException) {
                            binding.emailContainer.setError(getString(R.string.error_user_exists))
                            binding.emailContainer.requestFocus()
                        }catch(e: Exception) {
                            e.message?.let { it1 -> Log.e(TAG, it1) };
                        }
                    }
                }
        }

        initButtons()
        initRecyclerView()
        loadItems()
        changeSelectedProfilePicture(binding.imageButton1)
    }

    private fun showDialog(message: String) {
        MaterialAlertDialogBuilder(this)
            .setMessage(message)
            .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                finish()
            }
            .setCancelable(false)
            .show()
    }

    private fun setUserTextData() {
        val currentFirebaseUser = Firebase.auth.currentUser
        CurrentUser.user.id = currentFirebaseUser!!.uid
        CurrentUser.user.email = currentFirebaseUser.email.toString()
        CurrentUser.user.introduction = binding.etIntro.text.toString()
        CurrentUser.user.facebook = binding.etFacebook.text.toString()
        CurrentUser.user.instagram = binding.etInstagram.text.toString()
        CurrentUser.user.name = binding.editTextTextPersonName.text.toString()
        CurrentUser.user.otherContact = binding.etOther.text.toString()
    }

    private fun setUserProfilePicture() {
        val photoId = currentlySelectedProfilePicture!!.tag.toString()
        CurrentUser.user.profilePictureId = photoId
        val res = ImageIdGetter.getImageId(this, photoId)
        CurrentUser.user.profilePicture = BitmapFactory.decodeResource(resources, res)
    }

    private fun checkValidForm(): Boolean {


        if (binding.editTextTextPersonName.text.isEmpty())
            return false

        if (binding.etFacebook.text.isEmpty() && binding.etInstagram.text.isEmpty() && binding.etOther.text.isEmpty())
            return false

        return true
    }

    private fun initButtons() {
        binding.imageButton1.setOnClickListener {
            changeSelectedProfilePicture(
                binding.imageButton1
            )
        }

        binding.imageButton2.setOnClickListener {
            changeSelectedProfilePicture(
                binding.imageButton2
            )
        }

        binding.imageButton3.setOnClickListener {
            changeSelectedProfilePicture(
                binding.imageButton3
            )
        }

        binding.imageButton4.setOnClickListener {
            changeSelectedProfilePicture(
                binding.imageButton4
            )
        }

        binding.imageButton5.setOnClickListener {
            changeSelectedProfilePicture(
                binding.imageButton5
            )
        }

        binding.imageButton6.setOnClickListener {
            changeSelectedProfilePicture(
                binding.imageButton6
            )
        }

        binding.imageButton7.setOnClickListener {
            changeSelectedProfilePicture(
                binding.imageButton7
            )
        }

        binding.imageButton8.setOnClickListener {
            changeSelectedProfilePicture(
                binding.imageButton8
            )
        }

        binding.imageButton9.setOnClickListener {
            changeSelectedProfilePicture(
                binding.imageButton9
            )
        }

        binding.imageButton10.setOnClickListener {
            changeSelectedProfilePicture(
                binding.imageButton10
            )
        }

        binding.imageButton10.setOnClickListener {
            changeSelectedProfilePicture(
                binding.imageButton10
            )
        }

        binding.imageButton11.setOnClickListener {
            changeSelectedProfilePicture(
                binding.imageButton11
            )
        }

        binding.imageButton12.setOnClickListener {
            changeSelectedProfilePicture(
                binding.imageButton12
            )
        }

        binding.imageButton13.setOnClickListener {
            changeSelectedProfilePicture(
                binding.imageButton13
            )
        }

        binding.imageButton14.setOnClickListener {
            changeSelectedProfilePicture(
                binding.imageButton14
            )
        }

        binding.imageButton15.setOnClickListener {
            changeSelectedProfilePicture(
                binding.imageButton15
            )
        }

        binding.imageButton16.setOnClickListener {
            changeSelectedProfilePicture(
                binding.imageButton16
            )
        }

        binding.imageButton17.setOnClickListener {
            changeSelectedProfilePicture(
                binding.imageButton17
            )
        }

        binding.imageButton18.setOnClickListener {
            changeSelectedProfilePicture(
                binding.imageButton18
            )
        }
    }

    private fun changeSelectedProfilePicture(newSelected: ImageView) {
        currentlySelectedProfilePicture?.setBackgroundResource(0)
        currentlySelectedProfilePicture = newSelected
        newSelected.setBackgroundResource(R.drawable.border)
    }

    private fun initRecyclerView() {
        binding.rvSubjects.adapter = subjectAdapter
        binding.rvSubjects.layoutManager = LinearLayoutManager(this)

    }

    private fun loadItems() {
        subjectAdapter.update(subjects.filter { it.level == Level.INTERMEDIATE })
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(p0: Editable?) {
        binding.emailContainer.helperText = "Rossz formátum!"
        binding.passwordContainer.helperText = "A jelszónak legalább 6 karakter hosszúnak kell lennie!"

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text).matches()) {
            binding.emailContainer.helperText = ""
        }

        if (binding.etPassword.text.length > 6) {
            binding.passwordContainer.helperText = ""
        }
    }
}