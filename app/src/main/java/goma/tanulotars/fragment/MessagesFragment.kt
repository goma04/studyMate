package goma.tanulotars.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import goma.tanulotars.MyButtonObserver
import goma.tanulotars.MyScrollToBottomObserver
import goma.tanulotars.adapter.recyclerView.MessageAdapter
import goma.tanulotars.databinding.FragmentMessagesBinding
import goma.tanulotars.model.CurrentUser
import goma.tanulotars.model.Message

class MessagesFragment : Fragment() {
    private lateinit var binding: FragmentMessagesBinding
    private lateinit var manager: LinearLayoutManager
    private lateinit var db: FirebaseDatabase
    private lateinit var adapter: MessageAdapter


    companion object {
        private const val TAG = "MainActivity"
        const val MESSAGES_CHILD = "messages"
        const val ANONYMOUS = "anonymous"
        private const val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessagesBinding.inflate(layoutInflater, container, false)

        // Initialize Realtime Database
        db = Firebase.database
        val messagesRef = db.reference.child(MESSAGES_CHILD)

        // The FirebaseRecyclerAdapter class and options come from the FirebaseUI library
        // See: https://github.com/firebase/FirebaseUI-Android
        val options = FirebaseRecyclerOptions.Builder<goma.tanulotars.model.Message>()
            .setQuery(messagesRef, goma.tanulotars.model.Message::class.java)
            .build()
        adapter = MessageAdapter(options)
        binding.progressBar.visibility = ProgressBar.INVISIBLE
        manager = LinearLayoutManager(view?.context)
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



        return binding.root
    }





    public override fun onPause() {
        adapter.stopListening()
        super.onPause()
    }

    public override fun onResume() {
        super.onResume()
        adapter.startListening()
    }


}