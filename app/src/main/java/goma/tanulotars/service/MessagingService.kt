package goma.tanulotars.service

import com.google.firebase.messaging.FirebaseMessagingService

class MessagingService: FirebaseMessagingService()  {

    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
       // CurrentUser.user.token = token

    }
}