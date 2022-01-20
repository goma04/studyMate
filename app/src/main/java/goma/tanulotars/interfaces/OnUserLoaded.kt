package goma.tanulotars.interfaces

import goma.tanulotars.model.User

interface OnUserLoaded{
    fun onUserLoaded(user: User)
}