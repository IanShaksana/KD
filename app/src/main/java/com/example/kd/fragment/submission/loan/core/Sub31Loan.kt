package com.example.kd.fragment.submission.loan.core

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.kd.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * A fragment representing a list of Items.
 */
class Sub31Loan : Fragment() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var createButton: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sub31_loan_list, container, false)

        recyclerView = view.findViewById(R.id.list)
        createButton = view.findViewById(R.id.fab)

        createButton.setOnClickListener {
            Toast.makeText(context, "T", Toast.LENGTH_SHORT).show()
            val action =
                Sub31LoanDirections.actionNavLoanSubmitToSub31LoanCreate()
            view.findNavController().navigate(action)
        }
//        val myItemRecyclerViewAdapter =
//            MyItemRecyclerViewAdapter(PlaceholderContent.ITEMS)
//        myItemRecyclerViewAdapter.onItemClick = {
//            val action =
//                Sub31LoanDirections.actionNavLoanSubmitToSub01LoanDetail(it.id + " " + it.content)
//            view.findNavController().navigate(action)
//        }
//        recyclerView.adapter = myItemRecyclerViewAdapter


        return view
    }

}