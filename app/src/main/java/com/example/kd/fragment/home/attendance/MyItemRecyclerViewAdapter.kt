package com.example.kd.fragment.home.attendance

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.kd.R
import com.example.kd.databinding.FragmentAttendanceListBinding

import com.example.kd.modelbody.AttModel
import org.joda.time.DateTime
import org.joda.time.LocalDate

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyItemRecyclerViewAdapter(
    private val values: List<AttModel>, private  val context: Context
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


        val title: LocalDate = DateTime(item.title).toLocalDate()
        val jam = DateTime(item.jam)

        holder.title.text = "Tanggal : "+title.toString(context.applicationContext.resources.getString(R.string.format_date_1))
        holder.jam.text = "Jam : "+jam.toString(context.applicationContext.resources.getString(R.string.format_date_3))
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