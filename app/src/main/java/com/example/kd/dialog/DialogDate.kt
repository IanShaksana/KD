package com.example.kd.dialog

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

class DialogDate : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]
        val day = c[Calendar.DAY_OF_MONTH]
        val wasd = DatePickerDialog(
            requireContext(),
            AlertDialog.THEME_HOLO_LIGHT,
            parentFragment as OnDateSetListener,
            year,
            month,
            day
        )
        wasd.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK", wasd)
        wasd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel", wasd)


        // Create a new instance of DatePickerDialog and return it
        return wasd
    }
}