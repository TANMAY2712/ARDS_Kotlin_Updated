package com.ards.ui.predict.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ards.R
import com.ards.ui.predict.model.PredictFault
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PredictFaultImageAdapter(
    private val faultList: List<PredictFault>,
    private val onTimestampClick: (String) -> Unit // Callback for timestamp click
) : RecyclerView.Adapter<PredictFaultImageAdapter.FaultViewHolder>() {

    private val selectedStatus = mutableMapOf<Int, String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fault_image_list, parent, false)
        return FaultViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaultViewHolder, position: Int) {
        val fault = faultList[position]
        holder.bind(fault, selectedStatus[position])
    }

    override fun getItemCount(): Int = faultList.size

    inner class FaultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val fault_image: ImageView = itemView.findViewById(R.id.fault_image)

        fun bind(fault: PredictFault, status: String?) {

            val faultTimestamp = fault.faultTimestamp!!
            val faultTime = convertFaultTimestamp(faultTimestamp)

            Glide.with(fault_image.context)
                .load(fault.imageUrl)
                .apply(RequestOptions().fitCenter()) // Ensures the image fits while maintaining aspect ratio
                .into(fault_image)


            fault_image.setOnClickListener { onTimestampClick(faultTime) }



        }
    }
    fun convertFaultTimestamp(faultTimestamp: String): String {
        return try {
            val inputFormat = SimpleDateFormat("mm:ss.SSS", Locale.getDefault())
            val outputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

            val date = inputFormat.parse(faultTimestamp)
            outputFormat.format(date ?: Date(0)) // Default to "00:00:00" if parsing fails
        } catch (e: Exception) {
            "00:00:00" // Return a default value in case of an error
        }
    }
}
