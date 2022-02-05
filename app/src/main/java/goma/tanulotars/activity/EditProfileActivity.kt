package goma.tanulotars.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import goma.tanulotars.ImageIdGetter
import goma.tanulotars.R
import goma.tanulotars.adapter.recyclerView.SubjectAdapter
import goma.tanulotars.databinding.ActivityEditProfileBinding
import goma.tanulotars.firebase.FirebaseUtility
import goma.tanulotars.model.*

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
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
        binding = ActivityEditProfileBinding.inflate(layoutInflater)

        binding.editTextTextPersonName.setText(CurrentUser.user.name)
        binding.etInstagram.setText(CurrentUser.user.instagram)
        binding.etFacebook.setText(CurrentUser.user.facebook)
        binding.etOther.setText(CurrentUser.user.otherContact)
        binding.etIntro.setText(CurrentUser.user.introduction)
        binding.btnStart.setText("Mentés")

        binding.rvSubjects.adapter = subjectAdapter
        binding.rvSubjects.layoutManager = LinearLayoutManager(this)

        binding.btnStart.setOnClickListener {

            if (!checkValidForm()) {
                binding.tvExplain.setTextColor(Color.parseColor("#FF0000"))
                return@setOnClickListener
            }

            CurrentUser.user.introduction = binding.etIntro.text.toString()
            CurrentUser.user.facebook = binding.etFacebook.text.toString()
            CurrentUser.user.instagram = binding.etInstagram.text.toString()
            CurrentUser.user.name = binding.editTextTextPersonName.text.toString()
            CurrentUser.user.otherContact = binding.etOther.text.toString()
            setUserProfilePicture()

            FirebaseUtility.updateOrCreateUser(CurrentUser.user)
            Toast.makeText(
                this,
                "Profil sikeresen módosítva!",
                Toast.LENGTH_SHORT
            ).show()

            updatePostsInfo(CurrentUser.user)


            startActivity(Intent(this, MainActivity::class.java))

            finish()
        }

        changeSelectedProfilePicture(binding.imageButton1)

        initButtons()
        loadSubjects()
        setContentView(binding.root)
    }

    //Frissítjük a posztokat, amit a jelenlegi user posztolt az új adatokkal
    private fun updatePostsInfo(user: User) {
        val db = Firebase.firestore

        db.collection("posts")
            .whereEqualTo("userId", CurrentUser.user.id)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val postID = document.toObject<Post>().uid!!
                    db.collection("posts").document(postID)
                        .update("author", user.name)
                    db.collection("posts").document(postID)
                        .update("profilePictureId", user.profilePictureId)


                }
            }
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

    private fun loadSubjects() {
        subjectAdapter.update(subjects.filter { it.level == Level.INTERMEDIATE })
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
    }

    private fun changeSelectedProfilePicture(newSelected: ImageView) {
        currentlySelectedProfilePicture?.setBackgroundResource(0)
        currentlySelectedProfilePicture = newSelected
        newSelected.setBackgroundResource(R.drawable.border)
    }

}