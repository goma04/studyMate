package goma.tanulotars.model

import android.graphics.Bitmap
import java.io.Serializable


@SuppressWarnings("serial")
class User(
    var name: String = "",
    var introduction: String = "",
    val subjects: MutableList<Subject> = mutableListOf<Subject>(),
    @Transient var friends: MutableList<User> = mutableListOf<User>(), //GSON nem serializálja, utólag kell feltölteni
    var friendsId: MutableList<String> = mutableListOf(),
    @Transient var profilePicture: Bitmap? = null,
    var profilePictureId: String = "avatar1",
    var token :String="",
    var instagram :String="",
    var facebook :String="",
    var email: String = "",
    var otherContact: String = ""

) : Entity(), Serializable {

}