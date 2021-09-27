package com.segen.kd.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.segen.kd.R

class DialogJaminanTipe : DialogFragment(){
    private lateinit var listener: dialogListener

    /*
     * The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    interface dialogListener {
        fun onDialogJaminanTipeClick(value:String)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = parentFragment as dialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException((context.toString() +
                    " must implement dialogListener"))
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.label_jaminan_tipe_dialog_title)
                .setItems(
                    R.array.list_tipe_jaminan,
                    DialogInterface.OnClickListener { _, which ->
                        val obj = context?.resources?.getStringArray(R.array.list_tipe_jaminan)
                        if (obj != null) {
                            listener.onDialogJaminanTipeClick(obj[which])
                        }
                        // The 'which' argument contains the index position
                        // of the selected item
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}