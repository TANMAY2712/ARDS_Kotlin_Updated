package com.ards.ui.processed.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ards.R
import com.ards.model.Playground
import com.bumptech.glide.Glide

class FaultListAdapter(
    private var faultList: List<Playground>,
    private val onItemClick: (Playground) -> Unit
) : RecyclerView.Adapter<FaultListAdapter.FaultViewHolder>() {

    class FaultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.trainName)
      //  private val thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)

        fun bind(playground: Playground, onItemClick: (Playground) -> Unit) {
            tvTitle.text = playground.title
            itemView.setOnClickListener { onItemClick(playground) }


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fault_list, parent, false)
        return FaultViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaultViewHolder, position: Int) {
        holder.bind(faultList[position], onItemClick)
    }

    override fun getItemCount(): Int = faultList.size

    fun updateData(newFaults: List<Playground>) {
        faultList = newFaults
        notifyDataSetChanged()
    }
}
