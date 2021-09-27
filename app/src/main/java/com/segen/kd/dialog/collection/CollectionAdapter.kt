package com.segen.kd.dialog.collection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.segen.kd.R
import com.segen.kd.modelbody.ListCollectionModel

class CollectionAdapter(context: Context, resource: List<ListCollectionModel>?) :
    ArrayAdapter<ListCollectionModel>(context, R.layout.dialog_marketing, resource!!) {
    private lateinit var marketing: TextView
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val cusView: View = inflater.inflate(R.layout.dialog_marketing, parent, false)
        marketing = cusView.findViewById(R.id.nama)
        marketing.setText(getItem(position)!!.nama +"-"+getItem(position)!!.nopk)
        return cusView
    }
}