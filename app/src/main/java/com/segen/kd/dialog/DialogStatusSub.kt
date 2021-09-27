package com.segen.kd.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.segen.kd.R

class DialogStatusSub : DialogFragment() {
    internal lateinit var listener: dialogListener

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    interface dialogListener {
        fun onDialogStatusSubClick(value: String)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = parentFragment as dialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                (context.toString() +
                        " must implement dialogListener")
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.status_sub)
                .setItems(
                    R.array.status_sub_detail,
                    DialogInterface.OnClickListener { dialog, which ->
                        val obj = context?.resources?.getStringArray(R.array.status_sub_detail)
                        if (obj != null) {
                            listener.onDialogStatusSubClick(obj.get(which))
                        }
                        // The 'which' argument contains the index position
                        // of the selected item
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}