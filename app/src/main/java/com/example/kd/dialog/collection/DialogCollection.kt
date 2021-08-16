package com.example.kd.dialog.collection

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.kd.R
import android.widget.ArrayAdapter
import com.example.kd.modelbody.ListCollectionModel
import com.example.kd.modelbody.ListMarketingModel


class DialogCollection(private val adapter: ArrayAdapter<ListCollectionModel>) : DialogFragment() {

    internal lateinit var listener: dialogListenerCollection

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    interface dialogListenerCollection {
        fun onDialogClickCollection(value1: String, value2: Int)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = parentFragment as dialogListenerCollection
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


            builder.setTitle(R.string.label_loan_dialog_title)
                .setAdapter(
                    adapter
                ) { dialog, which ->

                    if (adapter.getItem(which) != null) {
                        listener.onDialogClickCollection(adapter.getItem(which)!!.idpayment,which)
                    }


                    // The 'which' argument contains the index position
                    // of the selected item
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}