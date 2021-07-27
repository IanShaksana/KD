package com.example.kd.fragment.task.personal

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kd.R
import com.example.kd.fragment.task.collection.placeholder.PlaceholderContent

/**
 * A fragment representing a list of Items.
 */
class Task02Personal : Fragment() {

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_21_collection_01, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                var myItemRecyclerViewAdapter: Task02PersonalRecyclerViewAdapter =
                    Task02PersonalRecyclerViewAdapter(PlaceholderContent.ITEMS)
                adapter = myItemRecyclerViewAdapter
                myItemRecyclerViewAdapter.onItemClick = {
                    val action =
                        Task02PersonalDirections.actionNavTaskToTask02TaskDetail(it.id + " " + it.content)
                    view.findNavController().navigate(action)

                }


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
            Task02Personal().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}