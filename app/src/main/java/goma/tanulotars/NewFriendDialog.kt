package goma.tanulotars

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class NewFriendDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.new_friend)
                .setPositiveButton(
                    "ok",
                    DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}