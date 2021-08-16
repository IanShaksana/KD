package com.example.kd.fragment.marketing.task

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.kd.R
import com.example.kd.databinding.FragItemFrag21Collection01Binding

import com.example.kd.modelbody.TaskModel
import com.google.android.material.card.MaterialCardView
import org.joda.time.DateTime
import org.joda.time.LocalDate

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyItemRecyclerViewAdapter(
    private val values: List<TaskModel>, private val context : Context
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    var onItemClick: ((TaskModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragItemFrag21Collection01Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.taskTitle.text = item.title
        holder.taskType.text = item.tipe
        holder.taskStatus.text = item.status

        if (item.status.equals("To Do", true)) {
            holder.taskStatus.setBackgroundResource(R.drawable.shape_status_1)
        }
        if (item.status.equals("Review", true)) {
            holder.taskStatus.setBackgroundResource(R.drawable.shape_status_2)
        }
        if (item.status.equals("Selesai", true)) {
            holder.taskStatus.setBackgroundResource(R.drawable.shape_status_3)
        }
        if (item.status.equals("Ditolak", true)) {
            holder.taskStatus.setBackgroundResource(R.drawable.shape_status_4)
        }
        val dt: LocalDate = DateTime(item.deadline).toLocalDate()
        val today: LocalDate = DateTime(item.deadline).toLocalDate()
        if ((dt.isEqual(today) && !item.status.equals(
                "Selesai",
                true
            )) || (dt.isBefore(today) && !item.status.equals("Selesai", true))
        ) {
            holder.card.setBackgroundResource(R.drawable.shape_deadline)
        }else{
            holder.card.setBackgroundResource(R.drawable.shape_deadline1)
        }
        holder.taskDeadline.text =
            "Deadline : " + dt.toString(context.applicationContext.resources.getString(R.string.format_date_1))
        holder.taskAttachment.text = item.attachment

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragItemFrag21Collection01Binding) :
        RecyclerView.ViewHolder(binding.root) {
        val taskTitle: TextView = binding.taskTitle
        val taskType: TextView = binding.taskType
        val taskStatus: TextView = binding.taskStatus
        val taskDeadline: TextView = binding.taskDeadline
        val taskAttachment: TextView = binding.taskAttachment
        val card: MaterialCardView = binding.collectionCard


        override fun toString(): String {
            return super.toString() + " '" + taskTitle.text + "'"
        }

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(values[adapterPosition])
            }
        }
    }

}