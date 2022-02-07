package goma.tanulotars.model

import java.util.*

abstract class Entity {
    public var id: String = UUID.randomUUID().toString()
}