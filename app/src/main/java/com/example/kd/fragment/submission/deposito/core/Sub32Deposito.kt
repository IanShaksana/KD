package com.example.kd.fragment.submission.deposito.core

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kd.R
import com.example.kd.fragment.submission.deposito.core.placeholder.PlaceholderContent
import com.example.kd.fragment.submission.loan.core.Sub31LoanDirections
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * A fragment representing a list of Items.
 */
class Sub32Deposito : Fragment() {

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }


    private lateinit var recyclerView: RecyclerView
    private lateinit var createButton: FloatingActionButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sub32_deposito_list, container, false)

        recyclerView = view.findViewById(R.id.list)
        createButton = view.findViewById(R.id.fab)

        createButton.setOnClickListener {
            Toast.makeText(context, "T", Toast.LENGTH_SHORT).show()
            val action =
                Sub32DepositoDirections.actionNavDepositoSubmitToSub32DepositoCreate()
            view.findNavController().navigate(action)
        }

        val myItemRecyclerViewAdapter: MyItemRecyclerViewAdapter = MyItemRecyclerViewAdapter(PlaceholderContent.ITEMS)
        myItemRecyclerViewAdapter.onItemClick = {
            Toast.makeText(context, it.id + " " + it.content, Toast.LENGTH_SHORT).show()
            val action =
                Sub32DepositoDirections.actionNavDepositoSubmitToSub02DepositoDetail(it.id + " " + it.content)
            view.findNavController().navigate(action)
        }
        recyclerView.adapter = myItemRecyclerViewAdapter


        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyItemRecyclerViewAdapter(PlaceholderContent.ITEMS)
            }
        }
        return view
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            Sub32Deposito().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}