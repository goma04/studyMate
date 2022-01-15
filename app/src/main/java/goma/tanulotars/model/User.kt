package goma.tanulotars.model

import android.graphics.drawable.Drawable

class User(
    var name: String = "",
    var introduction: String = "",
    val subjects: MutableList<Subject> = mutableListOf<Subject>(),
    val friends: MutableList<Relationship> = mutableListOf<Relationship>(),
    var profilePicture: Drawable? = null
) : Entity() {

}