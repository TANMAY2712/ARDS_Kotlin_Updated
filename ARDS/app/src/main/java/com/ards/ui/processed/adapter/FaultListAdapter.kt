package com.ards.ui.processed.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ards.R
import com.ards.ui.processed.model.FaultItem

class FaultListAdapter(
    private val faultList: List<FaultItem>,
    private val context: Context,
    private val onItemClick: (Long) -> Unit // Callback to seek video position
) : RecyclerView.Adapter<FaultListAdapter.FaultViewHolder>() {

    private var selectedCorrect = -1 // Track correct selection
    private var selectedWrong = -1   // Track wrong selection

    class FaultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvFaultPart: TextView = view.findViewById(R.id.tvFaultPart)
        val tvTimeStamp: TextView = view.findViewById(R.id.tvTimeStamp)
        val btnCorrect: ImageButton = view.findViewById(R.id.btnCorrect)
        val btnWrong: ImageButton = view.findViewById(R.id.btnWrong)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fault_list, parent, false)
        return FaultViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaultViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val fault = faultList[position]

        holder.tvFaultPart.text = fault.faultPart
        holder.tvTimeStamp.text = fault.timeStamp

        // Set correct button color
        holder.btnCorrect.setColorFilter(
            ContextCompat.getColor(context, if (position == selectedCorrect) R.color.green else R.color.gray)
        )

        // Set wrong button color
        holder.btnWrong.setColorFilter(
            ContextCompat.getColor(context, if (position == selectedWrong) R.color.red else R.color.gray)
        )

        // Handle correct button click
        holder.btnCorrect.setOnClickListener {
            selectedCorrect = position
            selectedWrong = -1 // Reset wrong selection
            notifyDataSetChanged()
        }

        // Handle wrong button click
        holder.btnWrong.setOnClickListener {
            selectedWrong = position
            selectedCorrect = -1 // Reset correct selection
            notifyDataSetChanged()
        }

        // Handle timestamp click to seek video
        holder.tvTimeStamp.setOnClickListener {
            onItemClick(fault.seekTimeMs)
        }
    }

    override fun getItemCount(): Int = faultList.size
}
