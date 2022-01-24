package goma.tanulotars.firebase

import android.content.ContentValues
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import goma.tanulotars.ImageIdGetter
import goma.tanulotars.interfaces.OnUserLoaded
import goma.tanulotars.model.CurrentUser
import goma.tanulotars.model.User

object FirebaseUtility {


    fun updateOrCreateUser(user: User) {
        val db = Firebase.firestore

        val friendListToSave = mutableListOf<HashMap<String, Any>>()

        for(friend in user.friends){
            friendListToSave += hashMapOf(
                "name" to friend.name,
                "introduction" to friend.introduction,
                "subjects" to friend.subjects,
                "profilePictureId" to friend.profilePictureId,
                "id" to friend.id,
            )
        }



        val newUser = hashMapOf(
            "name" to user.name,
            "introduction" to user.introduction,
            "subjects" to user.subjects,
            "profilePictureId" to user.profilePictureId,
            "id" to user.id,
            "friends" to friendListToSave
        )

        db.collection("users")
            .document(CurrentUser.user.id)
            .set(newUser)
    }

    fun getUserById(id: String, context: Context, listener: OnUserLoaded? = null) {
        val db = Firebase.firestore
        val docRef = db.collection("users").document(id)

        var user = User()

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")
                    user = document.toObject<User>()!!

                    val res = ImageIdGetter.getImageId(context, user.profilePictureId)
                    user.profilePicture = BitmapFactory.decodeResource(context.resources, res)
                    listener?.onUserLoaded(user)


                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("failedUser", "get failed with ", exception)
            }

    }

}