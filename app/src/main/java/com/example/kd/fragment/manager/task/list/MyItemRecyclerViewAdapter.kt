package com.example.kd.fragment.manager.task.list

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.kd.R
import com.example.kd.databinding.FragmentTaskManagerBinding
import com.example.kd.modelbody.TaskModelManager
import com.google.android.material.card.MaterialCardView
import org.joda.time.DateTime
import org.joda.time.LocalDate
import java.util.*


/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyItemRecyclerViewAdapter(
    private val values: List<TaskModelManager>, private val context: Context
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {


    var onItemClick: ((TaskModelManager) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentTaskManagerBinding.inflate(
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
        val today: LocalDate = DateTime(Date()).toLocalDate()
        if ((dt.isEqual(today) && !item.status.equals(
                "Selesai",
                true
            )) || (dt.isBefore(today) && !item.status.equals("Selesai", true))
        ) {
            holder.card.setBackgroundResource(R.drawable.shape_deadline)
        } else {
            holder.card.setBackgroundResource(R.drawable.shape_deadline1)
        }
        holder.taskDeadline.text =
            "Deadline : " + dt.toString(context.applicationContext.resources.getString(R.string.format_date_1))
        holder.taskMarketing.text = "Marketing : " + item.marketing
        holder.taskAttachment.text = item.attachment

        val finishDate: LocalDate = DateTime(item.finishdate).toLocalDate()
        if (item.status.equals("Review", true) || item.status.equals(
                "Selesai",
                true
            ) || item.status.equals("Ditolak", true)
        )
            holder.taskFinishDate.text = "Tanggal selesai : " + finishDate.toString(
                context.applicationContext.resources.getString(R.string.format_date_1)
            ) else {
            holder.taskFinishDate.text = "Tanggal selesai : "
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentTaskManagerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val taskTitle: TextView = binding.taskTitle
        val taskType: TextView = binding.taskType
        val taskStatus: TextView = binding.taskStatus
        val taskDeadline: TextView = binding.taskDeadline
        val taskMarketing: TextView = binding.taskMarketing
        val taskAttachment: TextView = binding.taskAttachment
        val taskFinishDate: TextView = binding.taskFinishDate
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