package goma.tanulotars.model

data class Post(
    val uid: String? = null,
    val author: String? = null,
    val title: String? = null,
    val body: String? = null,
    val profilePictureId: String? = null,
    val userId: String? = null,
    val date: String = "1997-10-10",
    val subject: String = ""
)
