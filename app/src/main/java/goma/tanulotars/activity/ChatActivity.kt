package goma.tanulotars.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import goma.tanulotars.ImageIdGetter
import goma.tanulotars.MyButtonObserver
import goma.tanulotars.MyScrollToBottomObserver
import goma.tanulotars.adapter.recyclerView.MessageAdapter
import goma.tanulotars.databinding.ActivityChatBinding
import goma.tanulotars.model.CurrentUser
import goma.tanulotars.model.Message
import goma.tanulotars.model.User

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var manager: LinearLayoutManager
    private lateinit var db: FirebaseDatabase
    private lateinit var adapter: MessageAdapter
    private var friend: User = User()

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)

        val gson = Gson()
        val userJson = intent.extras!!.getString("userJson")
        friend = gson.fromJson(userJson, friend::class.java)

        binding.tvFriendChatName.text = friend.name

        val res = ImageIdGetter.getImageId(this, friend.profilePictureId)
        binding.ivProfilePictureChat.setImageDrawable(this.resources.getDrawable(res, this.theme))

        // Initialize Realtime Database
        db = Firebase.database
        val messagesRef = db.reference.child(MESSAGES_CHILD)

        // The FirebaseRecyclerAdapter class and options come from the FirebaseUI library
        // See: https://github.com/firebase/FirebaseUI-Android
        val options = FirebaseRecyclerOptions.Builder<goma.tanulotars.model.Message>()
            .setQuery(messagesRef, goma.tanulotars.model.Message::class.java)
            .build()
        adapter = MessageAdapter(options)

        manager = LinearLayoutManager(this)
        manager.stackFromEnd = true
        binding.messageRecyclerView.layoutManager = manager
        binding.messageRecyclerView.adapter = adapter

        // Scroll down when a new message arrives
        // See MyScrollToBottomObserver for details
        adapter.registerAdapterDataObserver(
            MyScrollToBottomObserver(binding.messageRecyclerView, adapter, manager)
        )

        // Disable the send button when there's no text in the input field
        // See MyButtonObserver for details
        binding.messageEditText.addTextChangedListener(MyButtonObserver(binding.btnSend))

        // When the send button is clicked, send a text message
        binding.btnSend.setOnClickListener {
            val message = Message(
                binding.messageEditText.text.toString(),
                CurrentUser.user.name,
                CurrentUser.user.profilePictureId, //TODO ez miért
                null /* no image */
            )
            db.reference.child(MESSAGES_CHILD).push().setValue(message)
            binding.messageEditText.setText("")
        }


        setContentView(binding.root)
    }





    companion object {
        private const val TAG = "MainActivity"
        const val MESSAGES_CHILD = "messages"
        const val ANONYMOUS = "anonymous"
        private const val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"
    }
}