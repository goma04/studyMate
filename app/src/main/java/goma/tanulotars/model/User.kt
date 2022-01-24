package goma.tanulotars.model

import android.graphics.Bitmap
import java.io.Serializable

@SuppressWarnings("serial")
class User(
    var name: String = "",
    var introduction: String = "",
    val subjects: MutableList<Subject> = mutableListOf<Subject>(),
    val friends: MutableList<String> = mutableListOf<String>(),
    var profilePicture: Bitmap? = null,
    var profilePictureId: String = "avatar1",
    var token :String="",
    var email: String = ""

) : Entity(), Serializable {

}