package com.segen.kd.fragment.marketing.submission.loan.core

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.segen.kd.R
import com.segen.kd.databinding.FragmentSub31LoanBinding
import com.segen.kd.modelbody.TaskModelLoan
import com.google.android.material.card.MaterialCardView
import org.joda.time.DateTime
import org.joda.time.LocalDate

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyItemRecyclerViewAdapter(
    private val values: List<TaskModelLoan>, private val context : Context
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {


    var onItemClick: ((TaskModelLoan) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentSub31LoanBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.taskTitle.text = item.pengajuanTitle
        holder.taskType.text = item.pengajuanProduk
        holder.taskStatus.text = item.status

        if (item.status.equals("Draft", true)) {
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
        val dt: LocalDate = DateTime(item.createdat).toLocalDate()
//        val today: LocalDate = DateTime(item.deadline).toLocalDate()
//        if ((dt.isEqual(today) && !item.status.equals(
//                "Selesai",
//                true
//            )) || (dt.isBefore(today) && !item.status.equals("Selesai", true))
//        ) {
//            holder.card.setBackgroundResource(R.drawable.shape_deadline)
//        }else{
//            holder.card.setBackgroundResource(R.drawable.shape_deadline1)
//        }


        holder.card.setBackgroundResource(R.drawable.shape_deadline1)

        holder.taskDeadline.text =
            "Tanggal : " + dt.toString(context.applicationContext.resources.getString(R.string.format_date_1))
        holder.taskAttachment.text = "-"
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentSub31LoanBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val taskTitle: TextView = binding.subTitle
        val taskType: TextView = binding.subType
        val taskStatus: TextView = binding.subStatus
        val taskDeadline: TextView = binding.subTanggal
        val taskAttachment: TextView = binding.subAttachment
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