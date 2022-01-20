package goma.tanulotars.model

import java.io.Serializable

data class Subject(
    val ID: Int = 0,
    var name: String = "",
    var level: Level = Level.ADVANCED
):Serializable{

}
