package com.example.kd.fragment.home.attendance

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.kd.databinding.FragmentAttendanceListBinding

import com.example.kd.modelbody.AttModel

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyItemRecyclerViewAdapter(
    private val values: List<AttModel>
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentAttendanceListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.title.text = "Tanggal : "+item.title
        holder.jam.text = "Jam : "+item.jam.toString()
        holder.late.text = "Keterlambatan : "+item.late+" menit"
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentAttendanceListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title: TextView = binding.attTitle
        val jam: TextView = binding.attJam
        val late: TextView = binding.attLate

        override fun toString(): String {
            return super.toString() + " '" + title.text + "'"
        }
    }

}