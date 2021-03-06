package com.segen.kd.dialog.marketing

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment


class DialogMarketing2(private val adapter: MarketingAdapter) : DialogFragment() {

    internal lateinit var listener: dialogListenerMarketing2

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    interface dialogListenerMarketing2 {
        fun onDialogClickMarketing2(value1: String, value2: String)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = parentFragment as dialogListenerMarketing2
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


            builder.setTitle("Marketing")
                .setAdapter(
                    adapter
                ) { dialog, which ->

                    if (adapter.getItem(which) != null) {
                        listener.onDialogClickMarketing2(adapter.getItem(which)!!.id,adapter.getItem(which)!!.nama)
                    }


                    // The 'which' argument contains the index position
                    // of the selected item
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}