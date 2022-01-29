package goma.tanulotars.model

import android.graphics.Bitmap
import java.io.Serializable

@SuppressWarnings("serial")
class User(
    var name: String = "",
    var introduction: String = "",
    val subjects: MutableList<Subject> = mutableListOf<Subject>(),
    val friends: MutableList<User> = mutableListOf<User>(),
    var profilePicture: Bitmap? = null,
    var profilePictureId: String = "avatar1",
    var token :String="",
    var instagram :String="",
    var facebook :String="",
    var email: String = "",
    var otherContact: String = ""

) : Entity(), Serializable {

}