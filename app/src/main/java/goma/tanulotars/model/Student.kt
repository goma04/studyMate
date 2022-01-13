package goma.tanulotars.model

import android.graphics.drawable.Drawable

class Student(
    var name: String = "init",
    var introduction: String = "",
    val subjects: MutableList<Subject> = mutableListOf<Subject>(),
    val friends: MutableList<Relationship> = mutableListOf<Relationship>(),
    var profilePicture: Drawable? = null
) : Entity() {

}