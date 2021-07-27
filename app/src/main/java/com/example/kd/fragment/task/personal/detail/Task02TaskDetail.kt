package com.example.kd.fragment.task.personal.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.kd.R
import com.example.kd.fragment.task.collection.Task01CollectionDirections
import com.google.android.material.card.MaterialCardView
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter.ofPattern
import java.util.*

class Task02TaskDetail : Fragment() {

    companion object {
        fun newInstance() = Task02TaskDetail()
    }

    private lateinit var viewModel: Task02TaskDetailViewModel
    private lateinit var fileCard: MaterialCardView

    private lateinit var tanggal: TextView
    private lateinit var deadline: TextView
    private val value: Task02TaskDetailArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.frag_22_collection_02_detail, container, false)
        fileCard = view.findViewById(R.id.file_card)
        tanggal = view.findViewById(R.id.tanggal)
        deadline = view.findViewById(R.id.deadline)

        val date: DateTime = DateTime(Date())
        val valueTanggal = date.toString(activity?.resources?.getString(R.string.format_date_1))
        val valueDeadline =
            date.plusMonths(1).toString(activity?.resources?.getString(R.string.format_date_2))

        tanggal.setText("Tanggal : " + valueTanggal)
        deadline.setText("Tenggat Waktu : " + valueDeadline)
        fileCard.setOnClickListener {
            val action =
                Task02TaskDetailDirections.actionTask02TaskDetailToFile()
            view.findNavController().navigate(action)
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(Task02TaskDetailViewModel::class.java)
        val arg = value.value
        viewModel.initial(arg, activity)
        // TODO: Use the ViewModel
    }

}